package dev.latvian.mods.jarmod.jei;

import dev.latvian.mods.jarmod.heat.Heat;
import mezz.jei.api.ingredients.IIngredientType;

/**
 * @author LatvianModder
 */
public class JarModIngredients
{
	public static final IIngredientType<Heat> HEAT = () -> Heat.class;
}
