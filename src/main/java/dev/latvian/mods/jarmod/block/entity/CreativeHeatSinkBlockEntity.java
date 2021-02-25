package dev.latvian.mods.jarmod.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

/**
 * @author LatvianModder
 */
public class CreativeHeatSinkBlockEntity extends BlockEntity implements TickableBlockEntity {
	public CreativeHeatSinkBlockEntity() {
		super(JarModBlockEntities.ELECTRIC_HEAT_SINK.get());
	}

	@Override
	public void tick() {
	}
}
