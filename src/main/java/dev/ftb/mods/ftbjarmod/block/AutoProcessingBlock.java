package dev.ftb.mods.ftbjarmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * @author LatvianModder
 */
public class AutoProcessingBlock extends Block implements TubeConnection {
	public static final VoxelShape SHAPE = box(3, 0, 3, 13, 13, 13);

	public AutoProcessingBlock() {
		super(Properties.of(Material.METAL).strength(5F, 6F).sound(SoundType.METAL));
		registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.WATERLOGGED, false).setValue(TubeBlock.TUBE[1], TubeConnectionType.NOT_CONNECTED).setValue(TubeBlock.TUBE[2], TubeConnectionType.NOT_CONNECTED).setValue(TubeBlock.TUBE[3], TubeConnectionType.NOT_CONNECTED).setValue(TubeBlock.TUBE[4], TubeConnectionType.NOT_CONNECTED).setValue(TubeBlock.TUBE[5], TubeConnectionType.NOT_CONNECTED));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.WATERLOGGED, TubeBlock.TUBE[1], TubeBlock.TUBE[2], TubeBlock.TUBE[3], TubeBlock.TUBE[4], TubeBlock.TUBE[5]);
	}

	@Override
	@Deprecated
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}

		if (facing != Direction.DOWN) {
			return state.setValue(TubeBlock.TUBE[facing.ordinal()], canTubeConnectFrom(facingState, world, facingPos, facing.getOpposite()) ? TubeConnectionType.CONNECTED : TubeConnectionType.NOT_CONNECTED);
		}

		return state;
	}

	private boolean canTubeConnectFrom(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		if (state.getBlock() instanceof TubeConnection) {
			return ((TubeConnection) state.getBlock()).canTubeConnect(state, world, pos, face);
		}

		BlockEntity t = world.getBlockEntity(pos);
		return t != null && (t.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face).isPresent() || t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face).isPresent());
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = defaultBlockState();

		for (Direction direction : Direction.values()) {
			if (direction != Direction.DOWN) {
				BlockPos p = pos.relative(direction);
				BlockState s = world.getBlockState(p);

				if (canTubeConnectFrom(s, world, p, direction.getOpposite())) {
					state = state.setValue(TubeBlock.TUBE[direction.ordinal()], TubeConnectionType.CONNECTED);
				}
			}
		}

		return state.setValue(BlockStateProperties.WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return !state.getValue(BlockStateProperties.WATERLOGGED);
	}

	@Override
	@Deprecated
	public boolean isPathfindable(BlockState arg, BlockGetter arg2, BlockPos arg3, PathComputationType arg4) {
		return false;
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockPos pos1 = pos.below();

		if (level.getBlockState(pos1).is(FTBJarModBlocks.TEMPERED_JAR.get())) {
			return level.getBlockState(pos1).use(level, player, hand, hit.withPosition(pos1));
		}

		return InteractionResult.PASS;
	}
}
