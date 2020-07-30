package dev.latvian.mods.jarmod.heat;

import dev.latvian.mods.jarmod.JarMod;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public final class Heat
{
	public static final Heat NONE = new Heat(0);
	public static final Heat ANY = new Heat(1);

	public static Heat of(int value)
	{
		return value <= 0 ? NONE : value == 1 ? ANY : new Heat(value);
	}

	public static final ResourceLocation TEXTURE_HEAT = new ResourceLocation(JarMod.MOD_ID, "textures/gui/heat.png");
	public static final ResourceLocation TEXTURE_NO_HEAT = new ResourceLocation(JarMod.MOD_ID, "textures/gui/no_heat.png");

	private final int value;
	private int burnTime;

	private Heat(int v)
	{
		value = v;
		burnTime = 0;
	}

	public Heat add(int h)
	{
		return of(value + h);
	}

	public Heat add(Heat h)
	{
		return add(h.value);
	}

	public int getValue()
	{
		return value;
	}

	public Heat setBurnTime(int b)
	{
		burnTime = b;
		return this;
	}

	public int getBurnTime()
	{
		return burnTime;
	}

	public ResourceLocation getTexture()
	{
		return getValue() <= 0 ? TEXTURE_NO_HEAT : TEXTURE_HEAT;
	}

	public boolean isNone()
	{
		return getValue() <= 0;
	}
}