package dev.latvian.mods.jarmod.block.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author LatvianModder
 */
public class ElectricHeatSinkBlockEntity extends BlockEntity implements TickableBlockEntity {
	public int heat;

	public ElectricHeatSinkBlockEntity() {
		super(JarModBlockEntities.ELECTRIC_HEAT_SINK.get());
	}

	@Override
	public void tick() {
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		compound.putInt("Heat", heat);
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundTag compound) {
		heat = compound.getInt("Heat");
		super.load(state, compound);
	}
}
