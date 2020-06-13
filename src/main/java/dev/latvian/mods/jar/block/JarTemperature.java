package dev.latvian.mods.jar.block;

import dev.latvian.mods.jar.recipe.JarModRecipeSerializers;
import dev.latvian.mods.jar.recipe.NoInventory;
import dev.latvian.mods.jar.recipe.TemperatureModifierRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public enum JarTemperature implements IStringSerializable
{
	NORMAL("normal"),
	HOT("hot"),
	COLD("cold");

	public final String name;

	JarTemperature(String n)
	{
		name = n;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public static TemperatureModifierRecipe get(World world, BlockState state)
	{
		for (TemperatureModifierRecipe recipe : world.getRecipeManager().getRecipes(JarModRecipeSerializers.TEMPERATURE_MODIFIER_TYPE, NoInventory.INSTANCE, world))
		{
			if (recipe.test(state))
			{
				return recipe;
			}
		}

		return TemperatureModifierRecipe.NONE;
	}
}