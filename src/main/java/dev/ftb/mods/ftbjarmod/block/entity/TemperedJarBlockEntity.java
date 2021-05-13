package dev.ftb.mods.ftbjarmod.block.entity;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.block.FTBJarModBlocks;
import dev.ftb.mods.ftbjarmod.block.TemperedJarBlock;
import dev.ftb.mods.ftbjarmod.block.TubeBlock;
import dev.ftb.mods.ftbjarmod.item.FluidItem;
import dev.ftb.mods.ftbjarmod.recipe.ItemIngredientPair;
import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import dev.ftb.mods.ftblibrary.util.TimeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author LatvianModder
 */
public class TemperedJarBlockEntity extends BlockEntity implements TickableBlockEntity {
	private static final Direction[] DIRECTIONS = Direction.values();

	public int recipeTime;
	public int temperatureTime;
	public ResourceLocation recipe;
	public boolean redstonePowered;

	public TemperedJarBlockEntity() {
		super(FTBJarModBlockEntities.TEMPERED_JAR.get());
		recipeTime = 0;
		temperatureTime = 0;
		recipe = null;
		redstonePowered = false;
	}

	@Override
	public void tick() {
		if (recipeTime <= 0) {
			if (redstonePowered && !level.isClientSide() && level.getGameTime() % 20L == 18L) {
				startProgress();
			}

			return;
		}

		JarRecipe r = getRecipe();

		if (r == null) {
			return;
		}

		if (getBlockState().getValue(TemperedJarBlock.TEMPERATURE) != r.temperature) {
			return;
		}

		recipeTime--;

		if (recipeTime == 0) {
			if (!level.isClientSide()) {
				Pair<Set<IItemHandler>, Set<IFluidHandler>> connectedBlocks = getConnectedBlocks(r.hasItems(), r.hasFluids());
				System.out.println(connectedBlocks.getLeft() + " / " + connectedBlocks.getRight());
				List<ItemStack> itemStacks = new ArrayList<>(r.outputItems);

				for (FluidStack fs : r.outputFluids) {
					int amount = fs.getAmount();

					for (IFluidHandler handler : connectedBlocks.getRight()) {
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
						itemStacks.add(FluidItem.of(fs1));
					}
				}

				for (ItemStack is : itemStacks) {
					ItemStack is1 = is;

					for (IItemHandler handler : connectedBlocks.getLeft()) {
						is1 = ItemHandlerHelper.insertItem(handler, is, false);

						if (is1.isEmpty()) {
							break;
						}
					}

					if (!is1.isEmpty()) {
						Block.popResource(level, worldPosition.above(), is1.copy());
					}
				}

				if (r.canRepeat && consumeResources(r, connectedBlocks, false)) {
					consumeResources(r, connectedBlocks, true);
					recipeTime = r.time;
					setChangedAndSend();
				}
			}
		}
	}

	public void rightClick(Player player, InteractionHand hand, ItemStack item) {
		if (level == null) {
			return;
		}

		if (recipeTime > 0) {
			if (!level.isClientSide()) {
				if (player.isCrouching()) {
					player.displayClientMessage(new TextComponent("Recipe stopped!"), true);
					recipeTime = 0;
					setChangedAndSend();
				} else {
					player.displayClientMessage(new TextComponent(TimeUtils.prettyTimeString(recipeTime / 20L) + " left"), true);
				}
			}

			return;
		}

		if (player.isCrouching() || recipe == null) {
			if (level.isClientSide()) {
				FTBJarMod.PROXY.openTemperedJarScreen(this);
			}

			return;
		}

		startProgress();

		if (!level.isClientSide()) {
			player.displayClientMessage(new TextComponent(TimeUtils.prettyTimeString(recipeTime / 20L) + " left"), true);
		}
	}

	public void startProgress() {
		if (level.isClientSide() || recipeTime > 0) {
			return;
		}

		JarRecipe r = getRecipe();

		if (r == null) {
			return;
		}

		if (getBlockState().getValue(TemperedJarBlock.TEMPERATURE) != r.temperature) {
			return;
		}

		Pair<Set<IItemHandler>, Set<IFluidHandler>> connectedBlocks = getConnectedBlocks(r.hasItems(), r.hasFluids());

		if (consumeResources(r, connectedBlocks, false)) {
			consumeResources(r, connectedBlocks, true);
			recipeTime = r.time;
			setChangedAndSend();
		}
	}

	public boolean consumeResources(JarRecipe r, Pair<Set<IItemHandler>, Set<IFluidHandler>> connectedBlocks, boolean actual) {
		Map<Ingredient, MutableInt> items = new HashMap<>();
		Map<FluidStack, MutableInt> fluids = new HashMap<>();

		for (ItemIngredientPair is : r.inputItems) {
			items.put(is.ingredient, new MutableInt(is.amount));
		}

		for (FluidStack fs : r.inputFluids) {
			fluids.put(fs, new MutableInt(fs.getAmount()));
		}

		if (actual) {
			System.out.println(connectedBlocks.getLeft() + " / " + connectedBlocks.getRight());
		}

		return true;
	}

	public Pair<Set<IItemHandler>, Set<IFluidHandler>> getConnectedBlocks(boolean lookForItems, boolean lookForFluids) {
		if (!lookForItems && !lookForFluids) {
			return Pair.of(Collections.emptySet(), Collections.emptySet());
		}

		LinkedHashMap<BlockPos, BlockState> known = new LinkedHashMap<>();
		Set<BlockPos> traversed = new HashSet<>();
		Deque<BlockPos> openSet = new ArrayDeque<>();
		openSet.add(worldPosition);
		traversed.add(worldPosition);

		while (!openSet.isEmpty()) {
			BlockPos ptr = openSet.pop();
			BlockState state = Blocks.AIR.defaultBlockState();

			if ((ptr == worldPosition || level.isLoaded(ptr) && ((state = level.getBlockState(ptr)).getBlock()) instanceof TubeBlock) && known.put(ptr, state) == null) {
				if (known.size() >= 64) {
					break;
				}

				for (Direction side : DIRECTIONS) {
					BlockPos offset = ptr.relative(side);

					if (traversed.add(offset)) {
						openSet.add(offset);
					}
				}
			}
		}

		known.remove(worldPosition);
		Set<IItemHandler> itemHandlers = lookForItems ? new LinkedHashSet<>() : Collections.emptySet();
		Set<IFluidHandler> fluidHandlers = lookForFluids ? new LinkedHashSet<>() : Collections.emptySet();

		for (Map.Entry<BlockPos, BlockState> entry : known.entrySet()) {
			if (entry.getValue().getBlock() instanceof TubeBlock) {
				for (int i = 0; i < 6; i++) {
					if (entry.getValue().getValue(TubeBlock.TUBE[i]).hasConnection()) {
						BlockEntity blockEntity = level.getBlockEntity(entry.getKey().relative(DIRECTIONS[i]));

						if (blockEntity != null) {
							if (lookForItems) {
								IItemHandler itemHandler = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, DIRECTIONS[i].getOpposite()).orElse(null);

								if (itemHandler != null) {
									itemHandlers.add(itemHandler);
								}
							}

							if (lookForFluids) {
								IFluidHandler fluidHandler = blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, DIRECTIONS[i].getOpposite()).orElse(null);

								if (fluidHandler != null) {
									fluidHandlers.add(fluidHandler);
								}
							}
						}
					}
				}
			}
		}

		return Pair.of(itemHandlers, fluidHandlers);
	}

	public void setRecipe(Player player, @Nullable JarRecipe r) {
		if (level.isClientSide()) {
			return;
		}

		JarRecipe prev = getRecipe();

		if (prev == r) {
			return;
		}

		// verify if player can set recipe //

		recipeTime = 0;

		if (r == null) {
			recipe = null;
		} else {
			recipe = r.getId();
		}

		setChangedAndSend();
	}

	public void setChangedAndSend() {
		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
	}

	@Nullable
	public JarRecipe getRecipe() {
		Recipe<?> r = level.getRecipeManager().byKey(recipe).orElse(null);
		return r instanceof JarRecipe ? (JarRecipe) r : null;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		tag.putInt("RecipeTime", recipeTime);
		tag.putInt("TemperatureTime", temperatureTime);
		tag.putString("Recipe", recipe == null ? "" : recipe.toString());
		tag.putBoolean("RedstonePowered", redstonePowered);
		return super.save(tag);
	}

	@Override
	public void load(BlockState state, CompoundTag tag) {
		super.load(state, tag);
		recipeTime = tag.getInt("RecipeTime");
		temperatureTime = tag.getInt("TemperatureTime");
		String r = tag.getString("Recipe");
		recipe = r.isEmpty() ? null : new ResourceLocation(r);
		redstonePowered = tag.getBoolean("RedstonePowered");
	}

	@Override
	public CompoundTag getUpdateTag() {
		return save(new CompoundTag());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundTag tag) {
		load(state, tag);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(worldPosition, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		handleUpdateTag(FTBJarModBlocks.TEMPERED_JAR.get().defaultBlockState(), pkt.getTag());
	}

	public void neighborChanged() {
		boolean p = level.hasNeighborSignal(worldPosition);

		if (redstonePowered != p) {
			redstonePowered = p;

			if (p) {
				startProgress();
			}
		}
	}
}