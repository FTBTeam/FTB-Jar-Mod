package dev.latvian.mods.jarmod.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class BrickFurnacePartBlockEntity extends TileEntity
{
	public BlockPos offset = BlockPos.ZERO;
	private TileEntity cachedTile;

	public BrickFurnacePartBlockEntity()
	{
		super(JarModBlockEntities.BRICK_FURNACE_PART.get());
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putInt("OffsetX", offset.getX());
		compound.putInt("OffsetY", offset.getY());
		compound.putInt("OffsetZ", offset.getZ());
		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound)
	{
		offset = new BlockPos(compound.getInt("OffsetX"), compound.getInt("OffsetY"), compound.getInt("OffsetZ"));
		super.read(state, compound);
	}

	@Override
	public void updateContainingBlockInfo()
	{
		cachedTile = null;
		super.updateContainingBlockInfo();
	}

	@Nullable
	private TileEntity getFurnace()
	{
		if (cachedTile == null || cachedTile.isRemoved())
		{
			cachedTile = null;

			if (world != null && pos != null && !offset.equals(BlockPos.ZERO))
			{
				cachedTile = world.getTileEntity(pos.add(offset));
			}
		}

		return cachedTile;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side)
	{
		TileEntity tileEntity = getFurnace();
		return tileEntity == null ? LazyOptional.empty() : tileEntity.getCapability(cap, side);
	}
}
