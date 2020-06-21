package dev.latvian.mods.jarmod.block.entity;

import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class JarItemHandlerProcess extends JarItemHandler
{
	public JarItemHandlerProcess(TemperedJarBlockEntity e, int s)
	{
		super(e, s);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return ItemStack.EMPTY;
	}
}
