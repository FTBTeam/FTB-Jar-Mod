package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.block.entity.HeatSinkBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

/**
 * @author LatvianModder
 */
public class HeatSinkBlock extends Block
{
	public static final BooleanProperty HOT = BooleanProperty.create("hot");

	public static final VoxelShape SHAPE = VoxelShapes.or(
			makeCuboidShape(0, 0, 0, 16, 2, 16),
			makeCuboidShape(0, 14, 0, 16, 16, 16),
			makeCuboidShape(1, 2, 1, 15, 14, 15)
	);

	public HeatSinkBlock()
	{
		super(Properties.create(Material.IRON).hardnessAndResistance(5F, 6F).sound(SoundType.METAL));
		setDefaultState(stateContainer.getBaseState().with(HOT, false));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new HeatSinkBlockEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(HOT);
	}
}
