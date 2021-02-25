package dev.latvian.mods.jarmod.jei;

import com.google.common.base.MoreObjects;
import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.heat.Temperature;
import mezz.jei.api.ingredients.IIngredientHelper;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class TemperatureHelper implements IIngredientHelper<Temperature> {
	@Override
	public Temperature getMatch(Iterable<Temperature> iterable, Temperature temperature) {
		return temperature;
	}

	@Override
	public String getDisplayName(Temperature v) {
		return v.getName().getString();
	}

	@Override
	public String getUniqueId(Temperature v) {
		return JarMod.MOD_ID + ":temperature." + v.getSerializedName();
	}

	@Override
	public String getModId(Temperature v) {
		return JarMod.MOD_ID;
	}

	@Override
	public String getResourceId(Temperature v) {
		return "temperature." + v.getSerializedName();
	}

	@Override
	public Temperature copyIngredient(Temperature v) {
		return v;
	}

	@Override
	public String getErrorInfo(@Nullable Temperature v) {
		if (v == null) {
			return "null";
		}

		return MoreObjects.toStringHelper(Temperature.class).add("ID", v.getSerializedName()).toString();
	}
}