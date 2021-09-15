package dev.ftb.mods.ftbjarmod.temperature;

import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import net.minecraft.util.Mth;

public class TemperaturePair {
	public static final TemperaturePair DEFAULT = new TemperaturePair(Temperature.NONE, 1D);

	public final Temperature temperature;
	public final double efficiency;

	public TemperaturePair(Temperature t, double e) {
		temperature = t;
		efficiency = e;
	}

	public int getRecipeTime(JarRecipe r) {
		return Mth.clamp((int) (r.time / efficiency), 1, Short.MAX_VALUE);
	}
}
