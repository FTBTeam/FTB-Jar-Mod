package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.block.entity.BoilerBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

/**
 * @author LatvianModder
 */
public class BoilerBlock extends Block
{
	public static final VoxelShape SHAPE = makeCuboidShape(1.7, 0, 1.7, 14.3, 16, 14.3);
	public static final BooleanProperty TOP = BooleanProperty.create("top");
	public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
	public static final BooleanProperty BOILING = BooleanProperty.create("boiling");

	public BoilerBlock()
	{
		super(Properties.create(Material.IRON).hardnessAndResistance(5F, 6F).sound(SoundType.METAL));
		setDefaultState(stateContainer.getBaseState().with(TOP, false).with(BOTTOM, false).with(BOILING, false));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new BoilerBlockEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(TOP, BOTTOM, BOILING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return getDefaultState().with(TOP, context.getWorld().getBlockState(context.getPos().up()).getBlock() != this).with(BOTTOM, context.getWorld().getBlockState(context.getPos().down()).getBlock() != this);
	}

	@Override
	@Deprecated
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos)
	{
		if (facing == Direction.UP)
		{
			return state.with(TOP, facingState.getBlock() != this);
		}
		else if (facing == Direction.DOWN)
		{
			return state.with(BOTTOM, facingState.getBlock() != this);
		}

		return state;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
	{
		if (state.get(BOILING))
		{
			for (Direction direction : Direction.values())
			{
				if (world.rand.nextInt(4) == 0)
				{
					Direction.Axis a = direction.getAxis();
					double d1 = a == Direction.Axis.X ? 0.5D + 0.4625D * direction.getXOffset() : world.rand.nextFloat();
					double d2 = a == Direction.Axis.Y ? 0.5D + 0.4625D * direction.getYOffset() : world.rand.nextFloat();
					double d3 = a == Direction.Axis.Z ? 0.5D + 0.4625D * direction.getZOffset() : world.rand.nextFloat();
					double p0 = world.rand.nextGaussian() * 0.02D;
					double p1 = world.rand.nextGaussian() * 0.02D;
					double p2 = world.rand.nextGaussian() * 0.02D;
					world.addParticle(ParticleTypes.POOF, pos.getX() + d1, pos.getY() + d2, pos.getZ() + d3, p0, p1, p2);
				}
			}
		}
	}
}
