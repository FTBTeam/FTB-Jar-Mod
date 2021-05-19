package dev.ftb.mods.ftbjarmod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

/**
 * @author LatvianModder
 */
public class AutoProcessingBlock extends Block implements TubeConnection {
	public AutoProcessingBlock() {
		super(Properties.of(Material.METAL).strength(5F, 6F).sound(SoundType.METAL));
	}
}
