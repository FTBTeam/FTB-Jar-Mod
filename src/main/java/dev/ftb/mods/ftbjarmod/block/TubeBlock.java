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
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TubeBlock extends Block implements SimpleWaterloggedBlock, TubeConnection {
	public static final VoxelShape SHAPE_CENTER = box(6, 6, 6, 10, 10, 10);
	public static final VoxelShape SHAPE_D = box(6, 0, 6, 10, 6, 10);
	public static final VoxelShape SHAPE_U = box(6, 10, 6, 10, 16, 10);
	public static final VoxelShape SHAPE_N = box(6, 6, 0, 10, 10, 6);
	public static final VoxelShape SHAPE_S = box(6, 6, 10, 10, 10, 16);
	public static final VoxelShape SHAPE_W = box(0, 6, 6, 6, 10, 10);
	public static final VoxelShape SHAPE_E = box(10, 6, 6, 16, 10, 10);

	public static final VoxelShape[] SHAPES = new VoxelShape[64];
	public static final EnumProperty<TubeConnectionType>[] TUBE = new EnumProperty[6];

	static {
		for (int i = 0; i < 64; i++) {
			List<VoxelShape> shapes = new ArrayList<>();

			if (((i >> 0) & 1) != 0) {
				shapes.add(SHAPE_D);
			}

			if (((i >> 1) & 1) != 0) {
				shapes.add(SHAPE_U);
			}

			if (((i >> 2) & 1) != 0) {
				shapes.add(SHAPE_N);
			}

			if (((i >> 3) & 1) != 0) {
				shapes.add(SHAPE_S);
			}

			if (((i >> 4) & 1) != 0) {
				shapes.add(SHAPE_W);
			}

			if (((i >> 5) & 1) != 0) {
				shapes.add(SHAPE_E);
			}

			SHAPES[i] = shapes.isEmpty() ? SHAPE_CENTER : Shapes.or(SHAPE_CENTER, shapes.toArray(new VoxelShape[0]));
		}

		for (Direction direction : Direction.values()) {
			TUBE[direction.ordinal()] = EnumProperty.create(direction.getName().substring(0, 1), TubeConnectionType.class);
		}
	}

	public TubeBlock() {
		super(Properties.of(Material.METAL).strength(0.7F).sound(SoundType.NETHERITE_BLOCK));
		registerDefaultState(stateDefinition.any()
				.setValue(BlockStateProperties.WATERLOGGED, false)
				.setValue(TUBE[0], TubeConnectionType.NOT_CONNECTED)
				.setValue(TUBE[1], TubeConnectionType.NOT_CONNECTED)
				.setValue(TUBE[2], TubeConnectionType.NOT_CONNECTED)
				.setValue(TUBE[3], TubeConnectionType.NOT_CONNECTED)
				.setValue(TUBE[4], TubeConnectionType.NOT_CONNECTED)
				.setValue(TUBE[5], TubeConnectionType.NOT_CONNECTED)
		);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		int index = 0;

		for (Direction direction : Direction.values()) {
			if (state.getValue(TUBE[direction.ordinal()]).hasConnection()) {
				index |= 1 << direction.ordinal();
			}
		}

		return SHAPES[index];
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.WATERLOGGED, TUBE[0], TUBE[1], TUBE[2], TUBE[3], TUBE[4], TUBE[5]);
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

		return state.setValue(TUBE[facing.ordinal()], canTubeConnectFrom(facingState, world, facingPos, facing.getOpposite()) ? TubeConnectionType.CONNECTED : TubeConnectionType.NOT_CONNECTED);
	}

	private boolean canTubeConnectFrom(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		if (state.getBlock() instanceof TubeConnection) {
			return ((TubeConnection) state.getBlock()).canTubeConnect(state, world, pos, face);
		}

		BlockEntity t = world.getBlockEntity(pos);
		return t != null && (t.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face).isPresent() || t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face).isPresent());
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		return InteractionResult.PASS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = defaultBlockState();

		for (Direction direction : Direction.values()) {
			BlockPos p = pos.relative(direction);
			BlockState s = world.getBlockState(p);

			if (canTubeConnectFrom(s, world, p, direction.getOpposite())) {
				state = state.setValue(TUBE[direction.ordinal()], TubeConnectionType.CONNECTED);
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
}