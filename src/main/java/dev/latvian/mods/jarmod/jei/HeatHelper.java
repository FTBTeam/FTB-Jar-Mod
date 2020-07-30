package dev.latvian.mods.jarmod.jei;

import com.google.common.base.MoreObjects;
import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.heat.Heat;
import mezz.jei.api.ingredients.IIngredientHelper;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class HeatHelper implements IIngredientHelper<Heat>
{
	@Nullable
	@Override
	public Heat getMatch(Iterable<Heat> iterable, Heat v)
	{
		return null;
	}

	@Override
	public String getDisplayName(Heat v)
	{
		return I18n.format("jarmod.heat_value", v.getValue());
	}

	@Override
	public String getUniqueId(Heat v)
	{
		return JarMod.MOD_ID + ":heat";
	}

	@Override
	public String getWildcardId(Heat v)
	{
		return getUniqueId(v);
	}

	@Override
	public String getModId(Heat v)
	{
		return JarMod.MOD_ID;
	}

	@Override
	public String getResourceId(Heat v)
	{
		return "heat";
	}

	@Override
	public Heat copyIngredient(Heat v)
	{
		return v;
	}

	@Override
	public Heat normalizeIngredient(Heat ingredient)
	{
		return Heat.ANY;
	}

	@Override
	public String getErrorInfo(@Nullable Heat v)
	{
		if (v == null)
		{
			return "null";
		}

		return MoreObjects.toStringHelper(Heat.class).add("Temperature", v.getValue()).toString();
	}
}