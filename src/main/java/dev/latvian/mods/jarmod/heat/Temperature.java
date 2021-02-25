package dev.latvian.mods.jarmod.heat;

import dev.latvian.mods.jarmod.JarMod;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author LatvianModder
 */
public enum Temperature implements StringRepresentable {
	NONE("none", 0),
	LOW("low", 500),
	HIGH("high", 1000),
	SUBZERO("subzero", -10);

	public static final Temperature[] VALUES = values();
	public static final Map<String, Temperature> MAP = Arrays.stream(VALUES).collect(Collectors.toMap(t -> t.id, t -> t));

	private final String id;
	private final int value;
	private final Component name;
	private final ResourceLocation texture;

	Temperature(String n, int v) {
		id = n;
		value = v;
		name = new TranslatableComponent("jarmod.temperature." + id);
		texture = new ResourceLocation(JarMod.MOD_ID, "textures/gui/temperature/" + id + ".png");
	}

	@Override
	public String getSerializedName() {
		return id;
	}

	public int getValue() {
		return value;
	}

	public Component getName() {
		return name;
	}

	public ResourceLocation getTexture() {
		return texture;
	}

	public boolean isNone() {
		return getValue() == 0;
	}

	public boolean isSubzero() {
		return getValue() < 0;
	}

	public boolean isHeat() {
		return getValue() > 0;
	}

	public static Temperature byName(String name) {
		return MAP.getOrDefault(name.toLowerCase(), NONE);
	}
}