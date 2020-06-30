package dev.latvian.mods.jarmod.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * @author LatvianModder
 */
public class BoilerBlockEntity extends TileEntity implements ITickableTileEntity, IFluidHandler
{
	public int water = 0;
	public int steam = 0;

	public BoilerBlockEntity()
	{
		super(JarModBlockEntities.BOILER.get());
	}

	@Override
	public void tick()
	{
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putInt("Water", water);
		compound.putInt("Steam", steam);
		return super.write(compound);
	}

	@Override
	public void func_230337_a_(BlockState state, CompoundNBT compound)
	{
		water = compound.getInt("Water");
		steam = compound.getInt("Steam");
		super.func_230337_a_(state, compound);
	}

	@Override
	public int getTanks()
	{
		return 2;
	}

	@Override
	public FluidStack getFluidInTank(int tank)
	{
		return FluidStack.EMPTY;
	}

	@Override
	public int getTankCapacity(int tank)
	{
		return 32 * FluidAttributes.BUCKET_VOLUME;
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack)
	{
		return false;
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
