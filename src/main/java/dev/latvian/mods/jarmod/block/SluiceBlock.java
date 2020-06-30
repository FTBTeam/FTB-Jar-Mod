package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.block.entity.SluiceBlockEntity;
import dev.latvian.mods.jarmod.item.JarModItems;
import dev.latvian.mods.jarmod.item.MeshItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class SluiceBlock extends Block
{
	public static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 8, 16);

	public enum Mesh implements IStringSerializable
	{
		NONE("none", () -> ItemStack.EMPTY),
		CLOTH("cloth", () -> new ItemStack(JarModItems.CLOTH_MESH.get())),
		IRON("iron", () -> new ItemStack(JarModItems.IRON_MESH.get())),
		GOLD("gold", () -> new ItemStack(JarModItems.GOLD_MESH.get())),
		DIAMOND("diamond", () -> new ItemStack(JarModItems.DIAMOND_MESH.get()));

		private final String name;
		public final Supplier<ItemStack> meshItem;

		Mesh(String n, Supplier<ItemStack> m)
		{
			name = n;
			meshItem = m;
		}

		@Override
		public String func_176610_l()
		{
			return name;
		}
	}

	public static final EnumProperty<Mesh> MESH = EnumProperty.create("mesh", Mesh.class);
	public static final BooleanProperty WATER = BooleanProperty.create("water");

	public SluiceBlock()
	{
		super(Properties.create(Material.WOOD).hardnessAndResistance(0.9F).notSolid());
		setDefaultState(stateContainer.getBaseState()
				.with(MESH, Mesh.NONE)
				.with(WATER, false)
				.with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new SluiceBlockEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		ItemStack itemStack = player.getHeldItem(hand);

		if (player.isSneaking())
		{
			if (state.get(MESH) != Mesh.NONE && itemStack.isEmpty())
			{
				ItemStack current = state.get(MESH).meshItem.get();
				world.setBlockState(pos, state.with(MESH, Mesh.NONE), 3);

				if (!world.isRemote())
				{
					ItemHandlerHelper.giveItemToPlayer(player, current);
				}
			}

			return ActionResultType.SUCCESS;
		}
		else if (itemStack.getItem() instanceof MeshItem)
		{
			if (state.get(MESH) != ((MeshItem) itemStack.getItem()).mesh)
			{
				ItemStack current = state.get(MESH).meshItem.get();
				world.setBlockState(pos, state.with(MESH, ((MeshItem) itemStack.getItem()).mesh), 3);
				itemStack.shrink(1);

				if (!world.isRemote())
				{
					ItemHandlerHelper.giveItemToPlayer(player, current);
				}
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(MESH, WATER, BlockStateProperties.HORIZONTAL_FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState facingState = context.getWorld().getBlockState(context.getPos().up());
		return getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(WATER, facingState.getBlock() == Blocks.WATER || facingState.getBlock() == this && facingState.get(WATER));
	}

	@Override
	@Deprecated
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos)
	{
		return facing == Direction.UP ? state.with(WATER, facingState.getBlock() == Blocks.WATER || facingState.getBlock() == this && facingState.get(WATER)) : state;
	}
}
