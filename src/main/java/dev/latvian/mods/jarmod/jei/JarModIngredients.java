package dev.latvian.mods.jarmod.jei;

import dev.latvian.mods.jarmod.heat.Temperature;
import mezz.jei.api.ingredients.IIngredientType;

/**
 * @author LatvianModder
 */
public class JarModIngredients {
	public static final IIngredientType<Temperature> TEMPERATURE = () -> Temperature.class;
}
