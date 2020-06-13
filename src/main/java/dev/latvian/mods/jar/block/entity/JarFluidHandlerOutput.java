package dev.latvian.mods.jar.block.entity;

import net.minecraftforge.fluids.FluidStack;

/**
 * @author LatvianModder
 */
public class JarFluidHandlerOutput extends JarFluidHandler
{
	public JarFluidHandlerOutput(TemperedJarBlockEntity e, int s)
	{
		super(e, s);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action)
	{
		return 0;
	}
}