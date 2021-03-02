package dev.latvian.mods.jarmod.kubejs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.fluid.FluidStackJS;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.item.ingredient.IngredientStackJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;
import org.jetbrains.annotations.Nullable;

public class JarRecipeJS extends RecipeJS {
	@Override
	public void create(ListJS args) {
		JsonArray outputFluids = new JsonArray();
		JsonArray inputFluids = new JsonArray();

		for (Object o : ListJS.orSelf(args.get(0))) {
			if (o instanceof FluidStackJS) {
				JsonObject j = new JsonObject();
				j.addProperty("fluid", ((FluidStackJS) o).getId());
				j.addProperty("amount", ((FluidStackJS) o).getAmount());
				outputFluids.add(j);
			} else {
				outputItems.add(parseResultItem(o));
			}
		}

		for (Object o : ListJS.orSelf(args.get(1))) {
			if (o instanceof FluidStackJS) {
				JsonObject j = new JsonObject();
				j.addProperty("fluid", ((FluidStackJS) o).getId());
				j.addProperty("amount", ((FluidStackJS) o).getAmount());
				inputFluids.add(j);
			} else {
				inputItems.add(parseIngredientItem(o).asIngredientStack());
			}
		}

		json.add("outputFluids", outputFluids);
		json.add("inputFluids", inputFluids);
	}

	public JarRecipeJS time(int t) {
		json.addProperty("time", t);
		save();
		return this;
	}

	public JarRecipeJS timeScale(double t) {
		json.addProperty("timeScale", t);
		save();
		return this;
	}

	public JarRecipeJS temperature(String s) {
		json.addProperty("temperature", s);
		save();
		return this;
	}

	public JarRecipeJS highTemperature() {
		return temperature("high");
	}

	public JarRecipeJS lowTemperature() {
		return temperature("low");
	}

	public JarRecipeJS subzeroTemperature() {
		return temperature("subzero");
	}

	@Override
	public void deserialize() {
		inputItems.addAll(parseIngredientItemStackList(json.get("inputItems")));
		outputItems.addAll(parseResultItemList(json.get("outputItems")));
	}

	@Override
	public void serialize() {
		if (serializeOutputs) {
			JsonArray array = new JsonArray();

			for (ItemStackJS in : outputItems) {
				array.add(in.toResultJson());
			}

			json.add("outputItems", array);
		}

		if (serializeInputs) {
			JsonArray array = new JsonArray();

			for (IngredientJS in : inputItems) {
				array.add(in.toJson());
			}

			json.add("inputItems", array);
		}
	}

	@Nullable
	@Override
	public JsonElement serializeIngredientStack(IngredientStackJS in) {
		JsonObject o = new JsonObject();
		o.add("ingredient", in.ingredient.toJson());
		o.addProperty("count", in.getCount());
		return o;
	}
}
