package dev.ftb.mods.ftbjarmod.block.entity;

import dev.ftb.mods.ftbjarmod.block.FTBJarModBlocks;
import dev.ftb.mods.ftbjarmod.block.TemperedJarBlock;
import dev.ftb.mods.ftbjarmod.net.DisplayErrorPacket;
import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import dev.ftb.mods.ftbjarmod.temperature.Temperature;
import dev.ftb.mods.ftbjarmod.temperature.TemperaturePair;
import dev.ftb.mods.ftbjarmod.util.ConnectedBlocks;
import dev.ftb.mods.ftblibrary.item.FTBLibraryItems;
import dev.ftb.mods.ftblibrary.item.forge.FluidContainerItem;
import dev.ftb.mods.ftblibrary.util.forge.FluidKey;
import dev.ftb.mods.ftblibrary.util.forge.ItemKey;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.mutable.MutableLong;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class TemperedJarBlockEntity extends BlockEntity implements ContainerData {
	private long finishTime;
	public ResourceLocation recipe;
	private final int[] availableResources;
	private TemperaturePair temperature;
	private int lastMaxTime;

	public TemperedJarBlockEntity() {
		super(FTBJarModBlockEntities.TEMPERED_JAR.get());
		finishTime = 0L;
		recipe = null;
		availableResources = new int[]{-1, -1, -1};
		temperature = null;
	}

	public ConnectedBlocks getConnectedBlocks(@Nullable JarRecipe r) {
		return ConnectedBlocks.get(level, worldPosition, r);
	}

	public void setRecipe(Player player, @Nullable JarRecipe r) {
		if (r != null && !r.isAvailableFor(player)) {
			return;
		}

		if (r == null) {
			recipe = null;
		} else if (Objects.equals(recipe, r.getId())) {
			return;
		} else {
			recipe = r.getId();
		}

		finishTime = 0L;
		Arrays.fill(availableResources, -1);
		setChanged();
	}

	@Nullable
	public static JarRecipe getRecipe(@Nullable Level level, @Nullable ResourceLocation recipe) {
		if (level == null || recipe == null) {
			return null;
		}

		Recipe<?> r = level.getRecipeManager().byKey(recipe).orElse(null);
		return r instanceof JarRecipe ? (JarRecipe) r : null;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		tag.putLong("FinishTime", finishTime);
		tag.putString("Recipe", recipe == null ? "" : recipe.toString());
		tag.putIntArray("AvailableResources", availableResources);
		tag.putInt("LastMaxTime", lastMaxTime);
		return super.save(tag);
	}

	@Override
	public void load(BlockState state, CompoundTag tag) {
		super.load(state, tag);
		finishTime = tag.getLong("FinishTime");
		String r = tag.getString("Recipe");
		recipe = r.isEmpty() ? null : new ResourceLocation(r);
		int[] ar = tag.getIntArray("AvailableResources");

		if (ar.length >= availableResources.length) {
			System.arraycopy(ar, 0, availableResources, 0, availableResources.length);
		} else {
			Arrays.fill(availableResources, -1);
		}

		lastMaxTime = tag.getInt("LastMaxTime");

		temperature = null;
	}

	public static boolean tryStart(int[] res, JarRecipe r, ConnectedBlocks connectedBlocks) {
		for (int i = 0; i < res.length; i++) {
			if (res[i] == -1) {
				res[i] = 0;
			}
		}

		boolean start = true;

		for (int i = 0; i < r.inputObjects.length; i++) {
			if (res[i] < r.inputAmounts[i]) {
				res[i] += connectedBlocks.extract(r.inputObjects[i], r.inputAmounts[i] - res[i]);

				if (res[i] < r.inputAmounts[i]) {
					start = false;
				}
			}
		}

		return start;
	}

	public void writeMenu(Player player, FriendlyByteBuf buf) {
		buf.writeBlockPos(worldPosition);
		buf.writeUtf(recipe == null ? "" : recipe.toString(), Short.MAX_VALUE);
		writeResources(player, buf);
	}

	public void writeResources(@Nullable Player player, FriendlyByteBuf buf) {
		Map<ItemKey, MutableLong> items = new LinkedHashMap<>();
		Map<FluidKey, MutableLong> fluids = new LinkedHashMap<>();

		ConnectedBlocks connectedBlocks = getConnectedBlocks(null);

		if (player != null) {
			IItemHandler handler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);

			if (handler != null) {
				if (connectedBlocks.itemHandlers.isEmpty()) {
					connectedBlocks.itemHandlers = new LinkedHashSet<>();
				}

				connectedBlocks.itemHandlers.add(handler);
			}
		}

		for (IItemHandler handler : connectedBlocks.itemHandlers) {
			for (int i = 0; i < handler.getSlots(); i++) {
				ItemStack stack = handler.extractItem(i, Integer.MAX_VALUE, true);

				if (stack.getCount() > 0) {
					if (stack.getItem() == FTBLibraryItems.FLUID_CONTAINER.get()) {
						FluidStack fstack = FluidContainerItem.getFluidStack(stack);

						if (fstack.getAmount() > 0) {
							fluids.computeIfAbsent(new FluidKey(fstack), k -> new MutableLong(0L)).add(fstack.getAmount());
						}
					} else {
						items.computeIfAbsent(new ItemKey(stack), k -> new MutableLong(0L)).add(stack.getCount());
					}
				}
			}
		}

		for (IFluidHandler handler : connectedBlocks.fluidHandlers) {
			for (int i = 0; i < handler.getTanks(); i++) {
				FluidStack stack = handler.getFluidInTank(i);

				if (stack.getAmount() > 0) {
					fluids.computeIfAbsent(new FluidKey(stack), k -> new MutableLong(0L)).add(stack.getAmount());
				}
			}
		}

		buf.writeVarInt(items.size());

		for (Map.Entry<ItemKey, MutableLong> entry : items.entrySet()) {
			ItemStack stack = entry.getKey().stack.copy();
			stack.setCount(1);
			buf.writeItem(stack);
			buf.writeVarInt((int) Math.min(1000000001L, entry.getValue().longValue()));
		}

		buf.writeVarInt(fluids.size());

		for (Map.Entry<FluidKey, MutableLong> entry : fluids.entrySet()) {
			FluidStack stack = entry.getKey().stack.copy();
			stack.setAmount((int) Math.min(1000000001L, entry.getValue().longValue()));
			stack.writeToPacket(buf);
		}
	}

	public boolean start(BlockState state, @Nullable ServerPlayer player) {
		if (player != null && state.getValue(TemperedJarBlock.ACTIVE)) {
			new DisplayErrorPacket(new TextComponent("Recipe already in progress!")).sendTo(player);
			return false;
		}

		JarRecipe r = getRecipe(level, recipe);

		if (r == null) {
			if (player != null) {
				new DisplayErrorPacket(new TextComponent("Invalid recipe!")).sendTo(player);
			}

			return false;
		}

		if (player != null && !r.isAvailableFor(player)) {
			new DisplayErrorPacket(new TextComponent("Recipe isn't available for you!")).sendTo(player);
			return false;
		}

		if (getTemperature().temperature != r.temperature) {
			if (player != null) {
				new DisplayErrorPacket(new TextComponent("Temperature isn't right!")).sendTo(player);
			}

			return false;
		}

		ConnectedBlocks connectedBlocks = getConnectedBlocks(r);

		if (player != null && r.hasItems()) {
			IItemHandler handler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);

			if (handler != null) {
				if (connectedBlocks.itemHandlers.isEmpty()) {
					connectedBlocks.itemHandlers = new LinkedHashSet<>();
				}

				connectedBlocks.itemHandlers.add(handler);
			}
		}

		if (tryStart(availableResources, r, connectedBlocks)) {
			lastMaxTime = getTemperature().getRecipeTime(r);
			finishTime = level.getGameTime() + lastMaxTime;

			if (player != null) {
				level.setBlock(worldPosition, state.setValue(TemperedJarBlock.ACTIVE, true), Constants.BlockFlags.DEFAULT);
			}

			setChanged();
			level.getBlockTicks().scheduleTick(worldPosition, state.getBlock(), lastMaxTime);
			return true;
		} else if (player != null) {
			new DisplayErrorPacket(new TextComponent("Insufficient resources!")).sendTo(player);
		}

		return false;
	}

	public void stop() {
		BlockState state = getBlockState();

		if (state.getValue(TemperedJarBlock.ACTIVE)) {
			finish(state);
		}
	}

	public void tick(BlockState state) {
		if (state.getValue(TemperedJarBlock.ACTIVE)) {
			boolean repeat = true;
			JarRecipe r = getRecipe(level, recipe);

			if (r == null) {
				finish(state);
				return;
			}

			ConnectedBlocks connectedBlocks = getConnectedBlocks(r);

			for (int i = 0; i < r.inputAmounts.length; i++) {
				availableResources[i] -= r.inputAmounts[i];
			}

			List<ItemStack> itemStacks = new ArrayList<>();

			for (ItemStack is : r.outputItems) {
				itemStacks.add(is.copy());
			}

			for (FluidStack fs : r.outputFluids) {
				int amount = fs.getAmount();

				for (IFluidHandler handler : connectedBlocks.fluidHandlers) {
					FluidStack fs1 = fs.copy();
					fs1.setAmount(amount);
					amount -= handler.fill(fs, IFluidHandler.FluidAction.EXECUTE);

					if (amount <= 0) {
						break;
					}
				}

				if (amount > 0) {
					FluidStack fs1 = fs.copy();
					fs1.setAmount(amount);
					itemStacks.add(FluidContainerItem.of(fs1));
				}
			}

			for (ItemStack is : itemStacks) {
				ItemStack is1 = connectedBlocks.insertItem(is);

				if (!is1.isEmpty()) {
					dropItem(is1);
					repeat = false;
				}
			}

			repeat = repeat && !Level.isOutsideBuildHeight(worldPosition.above()) && level.getBlockState(worldPosition.above()).is(FTBJarModBlocks.AUTO_PROCESSING_BLOCK.get());

			if (!repeat || !start(state, null)) {
				finish(state);
			}
		}
	}

	private void dropItem(ItemStack stack) {
		for (Direction dir : ConnectedBlocks.HDIRECTIONS) {
			if (level.isEmptyBlock(worldPosition.relative(dir))) {
				Block.popResource(level, worldPosition.relative(dir), stack);
				return;
			}
		}

		Block.popResource(level, worldPosition, stack);
	}


	public void finish(BlockState state) {
		level.setBlock(worldPosition, state.setValue(TemperedJarBlock.ACTIVE, false), Constants.BlockFlags.DEFAULT);
		finishTime = 0L;
		setChanged();
	}

	@Override
	public void clearCache() {
		super.clearCache();
		temperature = null;
	}

	public TemperaturePair getTemperature() {
		if (temperature == null) {
			temperature = Temperature.fromLevel(level, worldPosition.below());
		}

		return temperature;
	}

	@Override
	public int get(int i) {
		switch (i) {
			case 0:
			case 1:
			case 2:
				return availableResources[i];
			case 3:
				return finishTime == 0L ? -1 : (int) (finishTime - level.getGameTime());
			case 4:
				return lastMaxTime;
			default:
				return 0;
		}
	}

	@Override
	public void set(int i, int j) {
	}

	@Override
	public int getCount() {
		return 5;
	}
}