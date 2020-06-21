package dev.latvian.mods.jarmod.util;

import dev.latvian.mods.jarmod.JarMod;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public final class Heat
{
	public static final Heat NONE = new Heat(0);
	public static final Heat ANY = new Heat(1);

	public static Heat of(int temperature)
	{
		return temperature <= 0 ? NONE : temperature == 1 ? ANY : new Heat(temperature);
	}

	public static final ResourceLocation TEXTURE_HEAT = new ResourceLocation(JarMod.MOD_ID, "textures/gui/heat.png");
	public static final ResourceLocation TEXTURE_NO_HEAT = new ResourceLocation(JarMod.MOD_ID, "textures/gui/no_heat.png");

	private final int temperature;

	private Heat(int t)
	{
		temperature = t;
	}

	public int getTemperature()
	{
		return temperature;
	}

	public ResourceLocation getTexture()
	{
		return getTemperature() <= 0 ? TEXTURE_NO_HEAT : TEXTURE_HEAT;
	}

	public boolean isNone()
	{
		return getTemperature() <= 0;
	}
}