package dev.ftb.mods.ftbjarmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author LatvianModder
 */
public interface TubeConnection {
	default boolean canTubeConnect(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return true;
	}
}
