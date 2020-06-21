package dev.latvian.mods.jarmod.block.entity;

import dev.latvian.mods.jarmod.recipe.JarRecipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Arrays;

/**
 * @author LatvianModder
 */
public class JarFluidHandler implements IFluidHandler
{
	public final TemperedJarBlockEntity entity;
	public FluidStack[] fluids;

	public JarFluidHandler(TemperedJarBlockEntity e, int s)
	{
		entity = e;
		fluids = new FluidStack[s];
		Arrays.fill(fluids, FluidStack.EMPTY);
	}

	@Override
	public int getTanks()
	{
		return fluids.length;
	}

	@Override
	public FluidStack getFluidInTank(int tank)
	{
		return fluids[tank];
	}

	@Override
	public int getTankCapacity(int tank)
	{
		JarRecipe r = entity.getRecipe();
		return r == null || tank >= r.inputFluids.size() ? 0 : r.inputFluids.get(tank).amount;
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack)
	{
		JarRecipe r = entity.getRecipe();
		return r != null && tank < r.inputFluids.size() && r.inputFluids.get(tank).ingredient.test(stack);
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
