package dev.latvian.mods.jarmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author LatvianModder
 */
public class TankWallBlock extends Block
{
	public TankWallBlock()
	{
		super(Properties.create(Material.IRON).hardnessAndResistance(5F, 6F).sound(SoundType.METAL));
	}
}
