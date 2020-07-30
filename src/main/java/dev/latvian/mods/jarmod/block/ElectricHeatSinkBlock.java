package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.block.entity.ElectricHeatSinkBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

/**
 * @author LatvianModder
 */
public class ElectricHeatSinkBlock extends Block
{
	public static final VoxelShape[] SHAPES = {
			VoxelShapes.or(
					makeCuboidShape(0, 0, 0, 2, 16, 16),
					makeCuboidShape(14, 0, 0, 16, 16, 16),
					makeCuboidShape(2, 1, 1, 14, 15, 15)
			),
			VoxelShapes.or(
					makeCuboidShape(0, 0, 0, 16, 2, 16),
					makeCuboidShape(0, 14, 0, 16, 16, 16),
					makeCuboidShape(1, 2, 1, 15, 14, 15)
			),
			VoxelShapes.or(
					makeCuboidShape(0, 0, 0, 16, 16, 2),
					makeCuboidShape(0, 0, 14, 16, 16, 16),
					makeCuboidShape(1, 1, 2, 15, 15, 14)
			)
	};

	public ElectricHeatSinkBlock()
	{
		super(Properties.create(Material.IRON).hardnessAndResistance(5F, 6F).sound(SoundType.METAL));
		setDefaultState(getStateContainer().getBaseState().with(BlockStateProperties.FACING, Direction.UP));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new ElectricHeatSinkBlockEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPES[state.get(BlockStateProperties.FACING).getAxis().ordinal()];
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return getDefaultState().with(BlockStateProperties.FACING, context.getFace());
	}
}
