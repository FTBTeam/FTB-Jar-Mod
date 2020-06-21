package dev.latvian.mods.jarmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * @author LatvianModder
 */
public class TankControllerBlock extends Block
{
	public TankControllerBlock()
	{
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(5F, 6F).sound(SoundType.METAL));
	}
}
