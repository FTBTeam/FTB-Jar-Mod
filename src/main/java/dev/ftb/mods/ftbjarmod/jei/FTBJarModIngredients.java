package dev.ftb.mods.ftbjarmod.jei;

import dev.ftb.mods.ftbjarmod.heat.Temperature;
import mezz.jei.api.ingredients.IIngredientType;

/**
 * @author LatvianModder
 */
public class FTBJarModIngredients {
	public static final IIngredientType<Temperature> TEMPERATURE = () -> Temperature.class;
}
