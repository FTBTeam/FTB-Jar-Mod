package dev.latvian.mods.jar.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * @author LatvianModder
 */
public interface TubeConnection
{
	default boolean canTubeConnect(BlockState state, IWorld world, BlockPos pos, Direction face)
	{
		return true;
	}
}
