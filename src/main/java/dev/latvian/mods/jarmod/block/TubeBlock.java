package dev.latvian.mods.jarmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TubeBlock extends Block implements IWaterLoggable, TubeConnection
{
	public static final VoxelShape SHAPE_CENTER = makeCuboidShape(6, 6, 6, 10, 10, 10);
	public static final VoxelShape SHAPE_D = makeCuboidShape(6, 0, 6, 10, 6, 10);
	public static final VoxelShape SHAPE_U = makeCuboidShape(6, 10, 6, 10, 16, 10);
	public static final VoxelShape SHAPE_N = makeCuboidShape(6, 6, 0, 10, 10, 6);
	public static final VoxelShape SHAPE_S = makeCuboidShape(6, 6, 10, 10, 10, 16);
	public static final VoxelShape SHAPE_W = makeCuboidShape(0, 6, 6, 6, 10, 10);
	public static final VoxelShape SHAPE_E = makeCuboidShape(10, 6, 6, 16, 10, 10);

	public static final VoxelShape[] SHAPES = new VoxelShape[64];
	public static final BooleanProperty[] TUBE = new BooleanProperty[6];

	static
	{
		for (int i = 0; i < 64; i++)
		{
			List<VoxelShape> shapes = new ArrayList<>();

			if (((i >> 0) & 1) != 0)
			{
				shapes.add(SHAPE_D);
			}

			if (((i >> 1) & 1) != 0)
			{
				shapes.add(SHAPE_U);
			}

			if (((i >> 2) & 1) != 0)
			{
				shapes.add(SHAPE_N);
			}

			if (((i >> 3) & 1) != 0)
			{
				shapes.add(SHAPE_S);
			}

			if (((i >> 4) & 1) != 0)
			{
				shapes.add(SHAPE_W);
			}

			if (((i >> 5) & 1) != 0)
			{
				shapes.add(SHAPE_E);
			}

			SHAPES[i] = shapes.isEmpty() ? SHAPE_CENTER : VoxelShapes.or(SHAPE_CENTER, shapes.toArray(new VoxelShape[0]));
		}

		for (Direction direction : Direction.values())
		{
			TUBE[direction.getIndex()] = BooleanProperty.create(direction.getName().substring(0, 1));
		}
	}

	public TubeBlock()
	{
		super(Properties.create(Material.IRON).sound(SoundType.LANTERN));
		setDefaultState(getStateContainer().getBaseState()
				.with(BlockStateProperties.WATERLOGGED, false)
				.with(TUBE[0], false)
				.with(TUBE[1], false)
				.with(TUBE[2], false)
				.with(TUBE[3], false)
				.with(TUBE[4], false)
				.with(TUBE[5], false)
		);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
	{
		int index = 0;

		for (Direction direction : Direction.values())
		{
			if (state.get(TUBE[direction.getIndex()]))
			{
				index |= 1 << direction.getIndex();
			}
		}

		return SHAPES[index];
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(BlockStateProperties.WATERLOGGED, TUBE[0], TUBE[1], TUBE[2], TUBE[3], TUBE[4], TUBE[5]);
	}

	@Override
	@Deprecated
	public IFluidState getFluidState(BlockState state)
	{
		return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	@Deprecated
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos)
	{
		if (state.get(BlockStateProperties.WATERLOGGED))
		{
			world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return state.with(TUBE[facing.getIndex()], canTubeConnectFrom(facingState, world, facingPos, facing.getOpposite()));
	}

	private boolean canTubeConnectFrom(BlockState state, IWorld world, BlockPos pos, Direction face)
	{
		if (state.getBlock() instanceof TubeConnection)
		{
			return ((TubeConnection) state.getBlock()).canTubeConnect(state, world, pos, face);
		}

		TileEntity t = world.getTileEntity(pos);
		return t != null && (t.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face).isPresent() || t.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face).isPresent());
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		BlockState state = getDefaultState();

		for (Direction direction : Direction.values())
		{
			BlockPos p = pos.offset(direction);
			BlockState s = world.getBlockState(pos.offset(direction));

			if (canTubeConnectFrom(s, world, p, direction.getOpposite()))
			{
				state = state.with(TUBE[direction.getIndex()], true);
			}
		}

		return state.with(BlockStateProperties.WATERLOGGED, context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
	{
		return !state.get(BlockStateProperties.WATERLOGGED);
	}

	@Override
	@Deprecated
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
	{
		return false;
	}
}