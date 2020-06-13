package dev.latvian.mods.jar.block.entity;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author LatvianModder
 */
public class JarItemHandler implements IItemHandlerModifiable
{
	public final TemperedJarBlockEntity entity;
	public ItemStack[] items;

	public JarItemHandler(TemperedJarBlockEntity e, int size)
	{
		entity = e;
		items = new ItemStack[size];
		Arrays.fill(items, ItemStack.EMPTY);
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack)
	{
		validateSlotIndex(slot);
		items[slot] = stack;
		onContentsChanged(slot);
	}

	@Override
	public int getSlots()
	{
		return items.length;
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot(int slot)
	{
		validateSlotIndex(slot);
		return items[slot];
	}

	@Override
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		if (!isItemValid(slot, stack))
		{
			return stack;
		}

		validateSlotIndex(slot);

		ItemStack existing = items[slot];

		int limit = getStackLimit(slot, stack);

		if (!existing.isEmpty())
		{
			if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
			{
				return stack;
			}

			limit -= existing.getCount();
		}

		if (limit <= 0)
		{
			return stack;
		}

		boolean reachedLimit = stack.getCount() > limit;

		if (!simulate)
		{
			if (existing.isEmpty())
			{
				items[slot] = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
			}
			else
			{
				existing.grow(reachedLimit ? limit : stack.getCount());
			}
			onContentsChanged(slot);
		}

		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Override
	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (amount == 0)
		{
			return ItemStack.EMPTY;
		}

		validateSlotIndex(slot);

		ItemStack existing = items[slot];

		if (existing.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		int toExtract = Math.min(amount, existing.getMaxStackSize());

		if (existing.getCount() <= toExtract)
		{
			if (!simulate)
			{
				items[slot] = ItemStack.EMPTY;
				onContentsChanged(slot);
				return existing;
			}
			else
			{
				return existing.copy();
			}
		}
		else
		{
			if (!simulate)
			{
				items[slot] = ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract);
				onContentsChanged(slot);
			}

			return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
		}
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	protected int getStackLimit(int slot, @Nonnull ItemStack stack)
	{
		return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	{
		return true;
	}

	protected void validateSlotIndex(int slot)
	{
		if (slot < 0 || slot >= items.length)
		{
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + items.length + ")");
		}
	}

	private void onContentsChanged(int slot)
	{
		entity.markDirtyAndSend();
	}
}