package dev.latvian.mods.jarmod.block.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class DynamicInventory implements IItemHandlerModifiable
{
	public List<ItemStack> stacks = new ArrayList<>();

	public void read(ListNBT nbt)
	{
	}

	public ListNBT write()
	{
		ListNBT nbt = new ListNBT();

		return nbt;
	}

	@Override
	public int getSlots()
	{
		return stacks.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return slot < 0 || slot >= stacks.size() ? ItemStack.EMPTY : stacks.get(slot);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		if (slot >= 0 && slot < stacks.size())
		{
			stacks.set(slot, stack);
		}
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		if (!isItemValid(slot, stack))
		{
			return stack;
		}

		ItemStack existing = stacks.get(slot);

		int limit = Math.min(getSlotLimit(slot), stack.getMaxStackSize());

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
				stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
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
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (amount == 0)
		{
			return ItemStack.EMPTY;
		}

		ItemStack existing = stacks.get(slot);

		if (existing.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		int toExtract = Math.min(amount, existing.getMaxStackSize());

		if (existing.getCount() <= toExtract)
		{
			if (!simulate)
			{
				stacks.set(slot, ItemStack.EMPTY);
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
				stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
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

	@Override
	public boolean isItemValid(int slot, ItemStack stack)
	{
		return true;
	}

	public void onContentsChanged(int slot)
	{
	}
}