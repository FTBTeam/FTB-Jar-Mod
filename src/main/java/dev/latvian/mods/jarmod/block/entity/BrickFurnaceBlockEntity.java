package dev.latvian.mods.jarmod.block.entity;

import dev.latvian.mods.jarmod.block.BrickFurnaceBlock;
import dev.latvian.mods.jarmod.block.JarModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

/**
 * @author LatvianModder
 */
public class BrickFurnaceBlockEntity extends TileEntity implements ITickableTileEntity
{
	public int heat;

	public BrickFurnaceBlockEntity()
	{
		super(JarModBlockEntities.BRICK_FURNACE.get());
	}

	@Override
	public void tick()
	{
		if (!getBlockState().get(BrickFurnaceBlock.CONSTRUCTED))
		{
			if (world.getGameTime() % 20L != 0L)
			{
				return;
			}

			for (int x = -1; x <= 1; x++)
			{
				for (int y = -1; y <= 1; y++)
				{
					for (int z = -1; z <= 1; z++)
					{
						if (x == 0 && y == 0 && z == 0)
						{
							continue;
						}

						BlockState state = world.getBlockState(pos.add(x, y, z));

						if (state.getBlock() != Blocks.BRICKS)
						{
							return;
						}
					}
				}
			}

			for (int x = -1; x <= 1; x++)
			{
				for (int y = -1; y <= 1; y++)
				{
					for (int z = -1; z <= 1; z++)
					{
						if (x == 0 && y == 0 && z == 0)
						{
							continue;
						}

						BlockPos p = pos.add(x, y, z);
						world.setBlockState(p, JarModBlocks.BRICK_FURNACE_PART.get().getDefaultState(), Constants.BlockFlags.DEFAULT);
						TileEntity te = world.getTileEntity(p);
						((BrickFurnacePartBlockEntity) te).offset = new BlockPos(-x, -y, -z);
						te.markDirty();
					}
				}
			}

			world.setBlockState(pos, getBlockState().with(BrickFurnaceBlock.CONSTRUCTED, true), Constants.BlockFlags.DEFAULT);
			markDirty();
			return;
		}

		if (world.isAirBlock(pos.up(2)))
		{
			Random r = world.rand;

			boolean active = false;

			if (active)
			{
				double x = pos.getX() + r.nextDouble();
				double y = pos.getY() + 1.2D + r.nextDouble() * 0.6D + r.nextDouble() * 0.6D;
				double z = pos.getZ() + r.nextDouble();

				if (r.nextInt(10) == 0)
				{
					x += r.nextGaussian() * 0.8D;
					z += r.nextGaussian() * 0.8D;
				}

				world.addOptionalParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, x, y, z, 0.0D, 0.07D, 0.0D);
			}
			else if (r.nextInt(10) == 0)
			{
				double x = pos.getX() + r.nextDouble();
				double y = pos.getY() + 1.2D + r.nextDouble() * 0.6D + r.nextDouble() * 0.6D;
				double z = pos.getZ() + r.nextDouble();
				world.addOptionalParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, x, y, z, 0.0D, 0.07D, 0.0D);
			}
		}

		if (heat == 0)
		{
			return;
		}

		heat--;

		if (heat % 60 == 0)
		{
			markDirty();
		}
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
