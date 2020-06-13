package dev.latvian.mods.jar.block;

import dev.latvian.mods.jar.block.entity.JarBlockEntity;
import dev.latvian.mods.jar.block.entity.TemperedJarBlockEntity;
import dev.latvian.mods.jar.item.JarModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class JarBlock extends Block implements TubeConnection
{
	public static final VoxelShape SHAPE = VoxelShapes.or(
			makeCuboidShape(3, 0, 3, 13, 11, 13),
			makeCuboidShape(4, 11, 4, 12, 12, 12),
			makeCuboidShape(5, 12, 5, 11, 13, 11),
			makeCuboidShape(6, 13, 6, 10, 14, 10),
			makeCuboidShape(5, 14, 5, 11, 16, 11)
	);

	public JarBlock()
	{
		super(Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.6F).notSolid());
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new JarBlockEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}

	@Override
	@Deprecated
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}

	@Override
	@Deprecated
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
	{
		return 0;
	}

	@Override
	@Deprecated
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			TileEntity entity = world.getTileEntity(pos);

			if (entity instanceof JarBlockEntity)
			{
				world.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	@Deprecated
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
	{
		return false;
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		ItemStack item = player.getHeldItem(hand);

		if (item.getItem() == JarModItems.JAR.get() || item.getItem() == JarModItems.TEMPERED_JAR.get() || item.getItem() == JarModItems.TUBE.get())
		{
			return ActionResultType.PASS;
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);

		if (tileEntity instanceof JarBlockEntity)
		{
			((JarBlockEntity) tileEntity).rightClick(player, hand, item);
		}
		else if (tileEntity instanceof TemperedJarBlockEntity)
		{
			((TemperedJarBlockEntity) tileEntity).rightClick(player, hand, item);
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public boolean canTubeConnect(BlockState state, IWorld world, BlockPos pos, Direction face)
	{
		return face == Direction.UP;
	}
}
