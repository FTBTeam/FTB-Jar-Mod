package dev.latvian.mods.jarmod.block;


import dev.latvian.mods.jarmod.block.entity.ElectricHeatSinkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * @author LatvianModder
 */
public class ElectricHeatSinkBlock extends Block {
	public static final VoxelShape[] SHAPES = {
			Shapes.or(
					box(0, 0, 0, 2, 16, 16),
					box(14, 0, 0, 16, 16, 16),
					box(2, 1, 1, 14, 15, 15)
			),
			Shapes.or(
					box(0, 0, 0, 16, 2, 16),
					box(0, 14, 0, 16, 16, 16),
					box(1, 2, 1, 15, 14, 15)
			),
			Shapes.or(
					box(0, 0, 0, 16, 16, 2),
					box(0, 0, 14, 16, 16, 16),
					box(1, 1, 2, 15, 15, 14)
			)
	};

	public ElectricHeatSinkBlock() {
		super(Properties.of(Material.METAL).strength(5F, 6F).sound(SoundType.METAL));
		registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.FACING, Direction.UP));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new ElectricHeatSinkBlockEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(BlockStateProperties.FACING).getAxis().ordinal()];
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(BlockStateProperties.FACING, context.getNearestLookingDirection());
	}
}
