package dev.ftb.mods.ftbjarmod.kubejs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.ftb.mods.ftbjarmod.item.FluidItem;
import dev.latvian.kubejs.fluid.FluidStackJS;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.item.ingredient.IngredientStackJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class JarRecipeJS extends RecipeJS {
	private ItemStackJS fromFluid(FluidStackJS fluid) {
		CompoundTag tag = new CompoundTag();
		fluid.getFluidStack().write(tag);
		return ItemStackJS.of(FluidItem.of(FluidStack.loadFluidStackFromNBT(tag)));
	}

	@Override
	public void create(ListJS args) {
		for (Object o : ListJS.orSelf(args.get(0))) {
			if (o instanceof FluidStackJS) {
				outputItems.add(fromFluid((FluidStackJS) o));
			} else {
				outputItems.add(parseResultItem(o));
			}
		}

		for (Object o : ListJS.orSelf(args.get(1))) {
			if (o instanceof FluidStackJS) {
				inputItems.add(fromFluid((FluidStackJS) o).asIngredientStack());
			} else {
				inputItems.add(parseIngredientItem(o).asIngredientStack());
			}
		}
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

	public JarRecipeJS highTemp() {
		return temperature("high");
	}

	public JarRecipeJS lowTemp() {
		return temperature("low");
	}

	public JarRecipeJS subzeroTemp() {
		return temperature("subzero");
	}

	@Override
	public void deserialize() {
		inputItems.addAll(parseIngredientItemStackList(json.get("input")));
		outputItems.addAll(parseResultItemList(json.get("output")));
	}

	@Override
	public void serialize() {
		if (serializeOutputs) {
			JsonArray array = new JsonArray();

			for (ItemStackJS in : outputItems) {
				array.add(in.toResultJson());
			}

			json.add("output", array);
		}

		if (serializeInputs) {
			JsonArray array = new JsonArray();

			for (IngredientJS in : inputItems) {
				array.add(in.toJson());
			}

			json.add("input", array);
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

	@Override
	public RecipeJS stage(String s) {
		json.addProperty("stage", s);
		save();
		return this;
	}
}
