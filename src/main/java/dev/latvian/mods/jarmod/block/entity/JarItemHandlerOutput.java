package dev.latvian.mods.jarmod.block.entity;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author LatvianModder
 */
public class JarItemHandlerOutput extends JarItemHandler
{
	public JarItemHandlerOutput(TemperedJarBlockEntity e, int s)
	{
		super(e, s);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	{
		return false;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		return stack;
	}
}
