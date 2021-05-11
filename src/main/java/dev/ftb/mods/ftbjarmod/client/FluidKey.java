package dev.ftb.mods.ftbjarmod.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public final class FluidKey {
	public final Fluid fluid;
	public final CompoundTag tag;

	public FluidKey(FluidStack fs) {
		fluid = fs.getFluid();
		tag = fs.getTag();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FluidKey fluidKey = (FluidKey) o;
		return fluid == fluidKey.fluid && Objects.equals(tag, fluidKey.tag);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fluid, tag);
	}
}
