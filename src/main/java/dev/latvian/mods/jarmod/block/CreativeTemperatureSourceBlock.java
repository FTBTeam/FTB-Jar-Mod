package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.heat.Temperature;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

/**
 * @author LatvianModder
 */
public class CreativeTemperatureSourceBlock extends Block {
	public final Temperature temperature;

	public CreativeTemperatureSourceBlock(Temperature t) {
		super(Properties.of(Material.METAL).noDrops().strength(-1.0F, 3600000.0F).noDrops().sound(SoundType.METAL));
		temperature = t;
	}
}
