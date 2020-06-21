package dev.latvian.mods.jarmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author LatvianModder
 */
public class TankGlassBlock extends Block
{
	public TankGlassBlock()
	{
		super(Properties.create(Material.IRON).hardnessAndResistance(5F, 6F).sound(SoundType.GLASS).notSolid());
	}

	@Override
	@Deprecated
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader world, BlockPos pos)
	{
		return 1F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader world, BlockPos pos)
	{
		return true;
	}

	@Override
	@Deprecated
	public boolean causesSuffocation(BlockState state, IBlockReader world, BlockPos pos)
	{
		return false;
	}

	@Override
	@Deprecated
	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos)
	{
		return false;
	}

	@Override
	@Deprecated
	public boolean canEntitySpawn(BlockState state, IBlockReader world, BlockPos pos, EntityType<?> type)
	{
		return false;
	}

	@Override
	@Deprecated
	@OnlyIn(Dist.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState adjacentState, Direction side)
	{
		return adjacentState.getBlock() == this || super.isSideInvisible(state, adjacentState, side);
	}
}
