package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.block.entity.TemperedJarBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class TemperedJarBlock extends JarBlock
{
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TemperedJarBlockEntity();
	}

	@Override
	@Deprecated
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			TileEntity entity = world.getTileEntity(pos);

			if (entity instanceof TemperedJarBlockEntity)
			{
				TemperedJarBlockEntity jar = (TemperedJarBlockEntity) entity;

				for (int i = 0; i < jar.itemHandler.getSlots(); i++)
				{
					spawnAsEntity(world, pos, jar.itemHandler.getStackInSlot(i));
				}

				world.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}
}
