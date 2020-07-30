package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.block.entity.CreativeFEBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

/**
 * @author LatvianModder
 */
public class CreativeFEBlock extends Block
{
	public CreativeFEBlock()
	{
		super(Properties.create(Material.ROCK).hardnessAndResistance(5F, 6F).sound(SoundType.STONE));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new CreativeFEBlockEntity();
	}
}