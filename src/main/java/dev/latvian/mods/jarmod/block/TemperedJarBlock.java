package dev.latvian.mods.jarmod.block;


import dev.latvian.mods.jarmod.block.entity.TemperedJarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author LatvianModder
 */
public class TemperedJarBlock extends JarBlock {
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new TemperedJarBlockEntity();
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity entity = world.getBlockEntity(pos);

			if (entity instanceof TemperedJarBlockEntity) {
				TemperedJarBlockEntity jar = (TemperedJarBlockEntity) entity;

				for (int i = 0; i < jar.itemHandler.getSlots(); i++) {
					popResource(world, pos, jar.itemHandler.getStackInSlot(i));
				}

				world.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, world, pos, newState, isMoving);
		}
	}
}
