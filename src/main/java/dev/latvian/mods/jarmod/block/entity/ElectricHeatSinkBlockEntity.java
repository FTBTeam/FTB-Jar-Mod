package dev.latvian.mods.jarmod.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * @author LatvianModder
 */
public class ElectricHeatSinkBlockEntity extends TileEntity implements ITickableTileEntity
{
	public int heat;

	public ElectricHeatSinkBlockEntity()
	{
		super(JarModBlockEntities.ELECTRIC_HEAT_SINK.get());
	}

	@Override
	public void tick()
	{
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putInt("Heat", heat);
		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound)
	{
		heat = compound.getInt("Heat");
		super.read(state, compound);
	}
}
