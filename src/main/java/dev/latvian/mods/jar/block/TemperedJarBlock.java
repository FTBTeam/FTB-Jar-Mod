package dev.latvian.mods.jar.block;

import dev.latvian.mods.jar.block.entity.TemperedJarBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class TemperedJarBlock extends JarBlock
{
	public static final EnumProperty<JarTemperature> TEMPERATURE = EnumProperty.create("temperature", JarTemperature.class);

	public TemperedJarBlock()
	{
		setDefaultState(getStateContainer().getBaseState().with(TEMPERATURE, JarTemperature.NORMAL));
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TemperedJarBlockEntity();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(TEMPERATURE);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return getDefaultState().with(TEMPERATURE, JarTemperature.get(context.getWorld(), context.getWorld().getBlockState(context.getPos().down())).getJarTemperature());
	}

	@Override
	@Deprecated
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos)
	{
		return facing == Direction.DOWN && world instanceof World ? state.with(TEMPERATURE, JarTemperature.get((World) world, facingState).getJarTemperature()) : super.updatePostPlacement(state, facing, facingState, world, pos, facingPos);
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
