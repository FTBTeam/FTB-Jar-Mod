package dev.ftb.mods.ftbjarmod.util;

import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;

public final class ContainerKey {
	public final Container container;

	public ContainerKey(Container c) {
		container = c;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ContainerKey that = (ContainerKey) o;

		if (container instanceof CompoundContainer && that.container instanceof CompoundContainer) {
			CompoundContainer a = (CompoundContainer) container;
			CompoundContainer b = (CompoundContainer) that.container;
			return a.container1 == b.container1 && a.container2 == b.container2 || a.container1 == b.container2 && a.container2 == b.container1;
		}

		return container == that.container;
	}

	@Override
	public int hashCode() {
		if (container instanceof CompoundContainer) {
			CompoundContainer c = (CompoundContainer) container;
			return c.container1.hashCode() ^ c.container2.hashCode();
		}

		return container.hashCode();
	}
}
