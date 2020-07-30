package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.block.entity.BrickFurnaceBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class BrickFurnaceBlock extends Block
{
	public static final BooleanProperty CONSTRUCTED = BooleanProperty.create("constructed");

	public BrickFurnaceBlock()
	{
		super(Properties.create(Material.ROCK).hardnessAndResistance(5F, 6F).sound(SoundType.STONE));
		setDefaultState(getStateContainer().getBaseState().with(CONSTRUCTED, false));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new BrickFurnaceBlockEntity();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(CONSTRUCTED);
	}

	@Override
	@Deprecated
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!state.isIn(newState.getBlock()))
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof BrickFurnaceBlockEntity)
			{
				//InventoryHelper.dropInventoryItems(world, pos, (AbstractFurnaceTileEntity) tileEntity);
			}

			super.onReplaced(state, world, pos, newState, isMoving);
		}
		else if (!state.get(CONSTRUCTED).equals(newState.get(CONSTRUCTED)) && !newState.get(CONSTRUCTED))
		{
			for (int x = -1; x <= 1; x++)
			{
				for (int y = -1; y <= 1; y++)
				{
					for (int z = -1; z <= 1; z++)
					{
						if (x == 0 && y == 0 && z == 0)
						{
							continue;
						}

						BlockPos p = pos.add(x, y, z);

						if (world.getBlockState(p).getBlock() == JarModBlocks.BRICK_FURNACE_PART.get())
						{
							world.setBlockState(p, Blocks.BRICKS.getDefaultState(), Constants.BlockFlags.DEFAULT);
						}
					}
				}
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(new TranslationTextComponent("block.jarmod.brick_furnace.tooltip").mergeStyle(TextFormatting.GRAY));
	}
}