package dev.latvian.mods.jarmod.block.entity;

import net.minecraftforge.fluids.FluidStack;

/**
 * @author LatvianModder
 */
public class JarFluidHandlerProcess extends JarFluidHandler
{
	public JarFluidHandlerProcess(TemperedJarBlockEntity e, int s)
	{
		super(e, s);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action)
	{
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action)
	{
		return FluidStack.EMPTY;
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action)
	{
		return FluidStack.EMPTY;
	}
}