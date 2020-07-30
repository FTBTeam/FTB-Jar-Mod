package dev.latvian.mods.jarmod.block.entity;

import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.block.JarModBlocks;
import dev.latvian.mods.jarmod.recipe.JarModRecipeSerializers;
import dev.latvian.mods.jarmod.recipe.JarRecipe;
import dev.latvian.mods.jarmod.recipe.NoInventory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TemperedJarBlockEntity extends TileEntity implements ITickableTileEntity
{
	public static final int STAGE_INPUT = 0;
	public static final int STAGE_PROCESS = 1;
	public static final int STAGE_OUTPUT = 2;

	public long tick;
	public double recipeTime;
	public int stage;
	public JarItemHandler itemHandler;
	public JarFluidHandler fluidHandler;
	public ResourceLocation recipe = null;
	private JarRecipe cachedRecipe = null;

	private LazyOptional<IItemHandler> itemOptional;
	private LazyOptional<IFluidHandler> fluidOptional;

	public TemperedJarBlockEntity()
	{
		super(JarModBlockEntities.TEMPERED_JAR.get());
		tick = 0L;
		recipeTime = 0D;
		setStageAndHandlers(STAGE_INPUT, 0, 0);
	}

	private void setStageAndHandlers(int s, int is, int fs)
	{
		stage = s;

		if (stage == STAGE_INPUT)
		{
			itemHandler = new JarItemHandlerInput(this, is);
			fluidHandler = new JarFluidHandlerInput(this, fs);
		}
		else if (stage == STAGE_OUTPUT)
		{
			itemHandler = new JarItemHandlerOutput(this, is);
			fluidHandler = new JarFluidHandlerOutput(this, fs);
		}
		else
		{
			itemHandler = new JarItemHandlerProcess(this, is);
			fluidHandler = new JarFluidHandlerProcess(this, fs);
		}

		itemOptional = LazyOptional.of(() -> itemHandler);
		fluidOptional = LazyOptional.of(() -> fluidHandler);
	}

	@Override
	public void tick()
	{
		tick++;

		JarRecipe r = getRecipe();

		if (r == null)
		{
			return;
		}

		if (stage == STAGE_INPUT)
		{
			ItemStack[] prevItems = new ItemStack[itemHandler.getSlots()];
			FluidStack[] prevFluids = new FluidStack[fluidHandler.fluids.length];

			for (int i = 0; i < itemHandler.items.length; i++)
			{
				prevItems[i] = itemHandler.items[i];

				if (r.inputItems.get(i).amount != prevItems[i].getCount())
				{
					return;
				}

				if (!r.inputItems.get(i).ingredient.test(prevItems[i]))
				{
					return;
				}
			}

			for (int i = 0; i < fluidHandler.fluids.length; i++)
			{
				prevFluids[i] = fluidHandler.fluids[i];

				// TODO: Test fluids
			}

			setStageAndHandlers(STAGE_PROCESS, r.inputItems.size(), r.inputFluids.size());

			System.arraycopy(prevItems, 0, itemHandler.items, 0, itemHandler.items.length);
			System.arraycopy(prevFluids, 0, fluidHandler.fluids, 0, fluidHandler.fluids.length);
			markDirty();
		}

		if (stage == STAGE_PROCESS)
		{
			TileEntity bottomEntity = world.getTileEntity(pos.down());

			if (bottomEntity instanceof HeatSinkBlockEntity && ((HeatSinkBlockEntity) bottomEntity).temperature >= r.temperature)
			{
				recipeTime++;
			}

			if (recipeTime >= r.time)
			{
				recipeTime = 0D;
				setStageAndHandlers(STAGE_OUTPUT, r.outputItems.size(), r.outputFluids.size());

				for (int i = 0; i < r.outputItems.size(); i++)
				{
					itemHandler.items[i] = r.outputItems.get(i).copy();
				}

				for (int i = 0; i < r.outputFluids.size(); i++)
				{
					fluidHandler.fluids[i] = r.outputFluids.get(i).copy();
				}

				markDirtyAndSend();
			}
		}

		if (stage == STAGE_OUTPUT)
		{
			for (ItemStack stack : itemHandler.items)
			{
				if (!stack.isEmpty())
				{
					return;
				}
			}

			setStageAndHandlers(STAGE_INPUT, r.inputItems.size(), r.inputFluids.size());
			markDirty();
		}
	}

	public void rightClick(PlayerEntity player, Hand hand, ItemStack item)
	{
		if (player.isSneaking())
		{
			if (world.isRemote())
			{
				JarMod.proxy.openTemperedJarScreen(this);
			}

			return;
		}

		if (world.isRemote())
		{
			return;
		}

		if (player.isSneaking())
		{
			JarRecipe r = getRecipe();
			List<JarRecipe> jarRecipes = world.getRecipeManager().getRecipes(JarModRecipeSerializers.JAR_TYPE, NoInventory.INSTANCE, world);
			setRecipe(player, jarRecipes.isEmpty() ? null : jarRecipes.get(((r == null ? -1 : jarRecipes.indexOf(r)) + 1) % jarRecipes.size()));
			player.sendStatusMessage(new TranslationTextComponent("block.jarmod.tempered_jar.recipe_changed", cachedRecipe == null ? "null" : cachedRecipe.getId()), true);
			return;
		}

		if (stage == STAGE_INPUT)
		{
			ItemStack ins;

			if ((ins = ItemHandlerHelper.insertItem(itemHandler, item, false)) != item)
			{
				player.setHeldItem(hand, ins);
				markDirtyAndSend();
			}
		}
		else if (stage == STAGE_OUTPUT)
		{
			boolean hadOutputItems = false;

			for (int i = 0; i < itemHandler.items.length; i++)
			{
				ItemStack stack = itemHandler.items[i];

				if (!stack.isEmpty())
				{
					hadOutputItems = true;
					ItemHandlerHelper.giveItemToPlayer(player, stack);
					itemHandler.items[i] = ItemStack.EMPTY;
				}
			}

			if (hadOutputItems)
			{
				markDirtyAndSend();
			}
		}
		else if (stage == STAGE_PROCESS)
		{
			JarRecipe r = getRecipe();

			if (r != null)
			{
				player.sendStatusMessage(new StringTextComponent((int) (recipeTime * 100D / (double) r.time) + "%"), true);
			}
		}
	}

	public void setRecipe(PlayerEntity player, @Nullable JarRecipe r)
	{
		if (world.isRemote())
		{
			return;
		}

		JarRecipe prev = getRecipe();

		if (prev == r)
		{
			return;
		}

		cachedRecipe = r;
		recipe = r == null ? null : r.getId();

		for (ItemStack stack : itemHandler.items)
		{
			ItemHandlerHelper.giveItemToPlayer(player, stack);
		}

		if (r != null)
		{
			setStageAndHandlers(STAGE_INPUT, r.inputItems.size(), r.inputFluids.size());
		}
		else
		{
			setStageAndHandlers(STAGE_INPUT, 0, 0);
		}

		recipeTime = 0;
		markDirtyAndSend();
	}

	public void markDirtyAndSend()
	{
		markDirty();
		world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
	{
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return itemOptional.cast();
		}
		else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return fluidOptional.cast();
		}

		return super.getCapability(cap, side);
	}

	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		cachedRecipe = null;
	}

	@Nullable
	public JarRecipe getRecipe()
	{
		if (cachedRecipe == null && recipe != null)
		{
			IRecipe<?> r = world.getRecipeManager().getRecipe(recipe).orElse(null);

			if (r instanceof JarRecipe)
			{
				cachedRecipe = (JarRecipe) r;
			}
			else
			{
				recipe = null;
				markDirty();
			}
		}

		return cachedRecipe;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putByte("Stage", (byte) stage);
		compound.putLong("Tick", tick);
		compound.putDouble("RecipeTime", recipeTime);
		compound.putString("Recipe", recipe == null ? "" : recipe.toString());
		compound.putByte("ItemCount", (byte) itemHandler.items.length);
		compound.putByte("FluidCount", (byte) fluidHandler.fluids.length);

		ListNBT itemList = new ListNBT();
		ListNBT fluidList = new ListNBT();

		for (ItemStack stack : itemHandler.items)
		{
			itemList.add(stack.isEmpty() ? new CompoundNBT() : stack.serializeNBT());
		}

		for (FluidStack stack : fluidHandler.fluids)
		{
			fluidList.add(stack.isEmpty() ? new CompoundNBT() : stack.writeToNBT(new CompoundNBT()));
		}

		compound.put("Items", itemList);
		compound.put("Fluids", fluidList);
		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound)
	{
		super.read(state, compound);
		stage = compound.getByte("Stage");
		tick = compound.getLong("Tick");
		recipeTime = compound.getDouble("RecipeTime");
		String r = compound.getString("Recipe");
		recipe = r.isEmpty() ? null : new ResourceLocation(r);
		cachedRecipe = null;

		setStageAndHandlers(stage, compound.getInt("ItemCount"), compound.getInt("FluidCount"));
		ListNBT itemList = compound.getList("Items", Constants.NBT.TAG_COMPOUND);
		ListNBT fluidList = compound.getList("Fluids", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < itemList.size(); i++)
		{
			itemHandler.items[i] = itemList.getCompound(i).isEmpty() ? ItemStack.EMPTY : ItemStack.read(itemList.getCompound(i));
		}

		for (int i = 0; i < fluidList.size(); i++)
		{
			fluidHandler.fluids[i] = fluidList.getCompound(i).isEmpty() ? FluidStack.EMPTY : FluidStack.loadFluidStackFromNBT(fluidList.getCompound(i));
		}
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag)
	{
		read(state, tag);
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		handleUpdateTag(JarModBlocks.TEMPERED_JAR.get().getDefaultState(), pkt.getNbtCompound());
	}
}