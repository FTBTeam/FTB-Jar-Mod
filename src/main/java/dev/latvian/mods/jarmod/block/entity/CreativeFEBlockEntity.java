package dev.latvian.mods.jarmod.block.entity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class CreativeFEBlockEntity extends TileEntity implements IEnergyStorage, ITickableTileEntity
{
	private final LazyOptional<IEnergyStorage> energyStorageLazyOptional;

	public CreativeFEBlockEntity()
	{
		super(JarModBlockEntities.CREATIVE_FE.get());
		energyStorageLazyOptional = LazyOptional.of(() -> this);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		return maxReceive;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return maxExtract;
	}

	@Override
	public int getEnergyStored()
	{
		return 1000000000;
	}

	@Override
	public int getMaxEnergyStored()
	{
		return 2000000000;
	}

	@Override
	public boolean canExtract()
	{
		return true;
	}

	@Override
	public boolean canReceive()
	{
		return true;
	}

	@Override
	public void tick()
	{
		for (Direction direction : Direction.values())
		{
			TileEntity tileEntity = world.getTileEntity(pos.offset(direction));

			if (tileEntity != null && tileEntity.getClass() != CreativeFEBlockEntity.class)
			{
				IEnergyStorage energyStorage = tileEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).orElse(null);

				if (energyStorage != null)
				{
					energyStorage.receiveEnergy(Integer.MAX_VALUE, false);
				}
			}
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
	{
		return cap == CapabilityEnergy.ENERGY ? energyStorageLazyOptional.cast() : super.getCapability(cap, side);
	}
}
