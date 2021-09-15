package dev.ftb.mods.ftbjarmod.block;

import net.minecraft.util.StringRepresentable;

/**
 * @author LatvianModder
 */
public enum TubeConnectionType implements StringRepresentable {
	NOT_CONNECTED("not_connected"),
	CONNECTED("connected"),
	IGNORED("ignored");

	private final String name;

	TubeConnectionType(String n) {
		name = n;
	}

	@Override
	public String getSerializedName() {
		return name;
	}

	public boolean hasConnection() {
		return this == CONNECTED;
	}
}
