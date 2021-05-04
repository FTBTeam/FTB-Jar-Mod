package dev.ftb.mods.ftbjarmod.jei;

import com.google.common.base.MoreObjects;
import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.heat.Temperature;
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
		return FTBJarMod.MOD_ID + ":temperature." + v.getSerializedName();
	}

	@Override
	public String getModId(Temperature v) {
		return FTBJarMod.MOD_ID;
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