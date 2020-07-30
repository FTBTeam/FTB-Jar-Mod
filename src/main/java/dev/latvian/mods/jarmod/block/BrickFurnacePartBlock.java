package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.block.entity.BrickFurnacePartBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

/**
 * @author LatvianModder
 */
public class BrickFurnacePartBlock extends Block
{
	public BrickFurnacePartBlock()
	{
		super(Properties.create(Material.ROCK).hardnessAndResistance(5F, 6F).sound(SoundType.STONE).notSolid().setAllowsSpawn((state, world, pos, entityType) -> false));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new BrickFurnacePartBlockEntity();
	}

	@Override
	@Deprecated
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
	{
		return true;
	}

	@Override
	@Deprecated
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return 1.0F;
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		return new ItemStack(JarModBlocks.BRICK_FURNACE.get());
	}

	@Override
	@Deprecated
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.isIn(newState.getBlock()))
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof BrickFurnacePartBlockEntity)
			{
				BlockPos p = pos.add(((BrickFurnacePartBlockEntity) tileEntity).offset);
				BlockState s = world.getBlockState(p);

				if (s.getBlock() == JarModBlocks.BRICK_FURNACE.get())
				{
					world.setBlockState(p, s.with(BrickFurnaceBlock.CONSTRUCTED, false), Constants.BlockFlags.DEFAULT);
				}
			}

			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}
}