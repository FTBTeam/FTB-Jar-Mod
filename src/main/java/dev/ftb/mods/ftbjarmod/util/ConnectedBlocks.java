package dev.ftb.mods.ftbjarmod.util;

import dev.ftb.mods.ftbjarmod.block.AutoProcessingBlock;
import dev.ftb.mods.ftbjarmod.block.TubeBlock;
import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import dev.ftb.mods.ftblibrary.item.FTBLibraryItems;
import dev.ftb.mods.ftblibrary.item.forge.FluidContainerItem;
import dev.ftb.mods.ftblibrary.util.ContainerKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ConnectedBlocks {
	public static final Direction[] DIRECTIONS = Direction.values();
	public static final Direction[] HDIRECTIONS = Arrays.stream(DIRECTIONS).filter(d -> d.getAxis().isHorizontal()).toArray(Direction[]::new);

	public static final ConnectedBlocks NONE = new ConnectedBlocks();

	public Set<IItemHandler> itemHandlers = Collections.emptySet();
	public Set<IFluidHandler> fluidHandlers = Collections.emptySet();

	private static boolean isValidBlock(BlockState state) {
		return state.getBlock() instanceof TubeBlock || state.getBlock() instanceof AutoProcessingBlock;
	}

	public static ConnectedBlocks get(Level level, BlockPos worldPosition, @Nullable JarRecipe r) {
		boolean lookForItems = r == null || r.hasItems();
		boolean lookForFluids = r == null || r.hasFluids();

		if (!lookForItems && !lookForFluids) {
			return ConnectedBlocks.NONE;
		}

		LinkedHashMap<BlockPos, BlockState> known = new LinkedHashMap<>();
		Set<BlockPos> traversed = new HashSet<>();
		Deque<BlockPos> openSet = new ArrayDeque<>();
		openSet.add(worldPosition);
		traversed.add(worldPosition);

		while (!openSet.isEmpty()) {
			BlockPos ptr = openSet.pop();
			BlockState state = Blocks.AIR.defaultBlockState();

			if ((ptr == worldPosition || level.isLoaded(ptr) && isValidBlock(state = level.getBlockState(ptr))) && known.put(ptr, state) == null) {
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
		ConnectedBlocks connectedBlocks = new ConnectedBlocks();

		if (lookForItems) {
			connectedBlocks.itemHandlers = new LinkedHashSet<>();
		}

		if (lookForFluids) {
			connectedBlocks.fluidHandlers = new LinkedHashSet<>();
		}

		Set<ContainerKey> knownContainers = lookForItems ? new HashSet<>() : Collections.emptySet();

		for (Map.Entry<BlockPos, BlockState> entry : known.entrySet()) {
			for (int i = 0; i < 6; i++) {
				if (!(entry.getValue().getBlock() instanceof TubeBlock) || entry.getValue().getValue(TubeBlock.TUBE[i]).hasConnection()) {
					BlockEntity blockEntity = level.getBlockEntity(entry.getKey().relative(DIRECTIONS[i]));

					if (blockEntity != null) {
						if (lookForItems) {
							IItemHandler itemHandler = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, DIRECTIONS[i].getOpposite()).orElse(null);

							if (itemHandler instanceof InvWrapper ? knownContainers.add(new ContainerKey(((InvWrapper) itemHandler).getInv())) : itemHandler != null) {
								connectedBlocks.itemHandlers.add(itemHandler);
							}
						}

						if (lookForFluids) {
							IFluidHandler fluidHandler = blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, DIRECTIONS[i].getOpposite()).orElse(null);

							if (fluidHandler != null) {
								connectedBlocks.fluidHandlers.add(fluidHandler);
							}
						}
					}
				}
			}
		}

		return connectedBlocks;
	}

	public int extract(Object object, int amount) {
		if (object instanceof Ingredient) {
			return extractItem((Ingredient) object, amount);
		} else if (object instanceof FluidStack) {
			return extractFluid((FluidStack) object, amount);
		}

		return 0;
	}

	public int extractItem(Ingredient ingredient, int amount) {
		int left = amount;

		itemHandlerBreak:
		for (IItemHandler handler : itemHandlers) {
			for (int i = 0; i < handler.getSlots(); i++) {
				if (ingredient.test(handler.getStackInSlot(i))) {
					left -= handler.extractItem(i, left, false).getCount();

					if (left <= 0) {
						break itemHandlerBreak;
					}
				}
			}
		}

		return amount - left;
	}

	public int extractFluid(FluidStack fluid, int amount) {
		int left = amount;

		itemHandlerBreak:
		for (IItemHandler handler : itemHandlers) {
			if (handler instanceof IItemHandlerModifiable) {
				for (int i = 0; i < handler.getSlots(); i++) {
					ItemStack itemStack = handler.getStackInSlot(i);
					if (itemStack.getItem() == FTBLibraryItems.FLUID_CONTAINER.get()) {
						FluidStack fs = FluidContainerItem.getFluidStack(itemStack);

						if (fs.isFluidEqual(fluid)) {
							int a = Math.min(left, fs.getAmount());
							left -= a;
							fs.setAmount(fs.getAmount() - a);

							if (fs.isEmpty()) {
								((IItemHandlerModifiable) handler).setStackInSlot(i, ItemStack.EMPTY);
							} else {
								((IItemHandlerModifiable) handler).setStackInSlot(i, FluidContainerItem.of(fs));
							}
						}

						if (left <= 0) {
							break itemHandlerBreak;
						}
					}
				}
			}
		}

		for (IFluidHandler handler : fluidHandlers) {
			FluidStack toDrain = fluid.copy();
			toDrain.setAmount(amount);
			left -= handler.drain(toDrain, IFluidHandler.FluidAction.EXECUTE).getAmount();

			if (left <= 0) {
				break;
			}
		}

		return amount - left;
	}

	public ItemStack insertItem(ItemStack stack) {
		ItemStack is1 = stack;

		for (IItemHandler handler : itemHandlers) {
			is1 = insertItem(handler, is1);

			if (is1.isEmpty()) {
				return ItemStack.EMPTY;
			}
		}

		return is1;
	}

	public static ItemStack insertItem(IItemHandler dest, ItemStack stack) {
		for (int i = 0; i < dest.getSlots(); i++) {
			if (!dest.getStackInSlot(i).isEmpty()) {
				stack = dest.insertItem(i, stack, false);

				if (stack.isEmpty()) {
					return ItemStack.EMPTY;
				}
			}
		}

		for (int i = 0; i < dest.getSlots(); i++) {
			stack = dest.insertItem(i, stack, false);

			if (stack.isEmpty()) {
				return ItemStack.EMPTY;
			}
		}

		return stack;
	}
}
