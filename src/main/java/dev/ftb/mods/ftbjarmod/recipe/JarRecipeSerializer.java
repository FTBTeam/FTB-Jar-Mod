package dev.ftb.mods.ftbjarmod.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.ftb.mods.ftbjarmod.temperature.Temperature;
import dev.ftb.mods.ftblibrary.item.forge.FluidContainerItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * @author LatvianModder
 */
public class JarRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<JarRecipe> {
	@Override
	public JarRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		JarRecipe r = new JarRecipe(recipeId, json.has("group") ? json.get("group").getAsString() : "");

		if (json.has("time")) {
			r.time = json.get("time").getAsInt();
		}

		if (json.has("timeScale")) {
			r.time = (int) (r.time * json.get("timeScale").getAsDouble());
		}

		if (json.has("temperature")) {
			r.temperature = Temperature.byName(json.get("temperature").getAsString());
		}

		if (json.has("canRepeat")) {
			r.canRepeat = json.get("canRepeat").getAsBoolean();
		}

		if (json.has("stage")) {
			r.stage = json.get("stage").getAsString();
		}

		if (json.has("input")) {
			for (JsonElement e : json.get("input").getAsJsonArray()) {
				JsonObject o = e.getAsJsonObject();
				Ingredient ingredient = Ingredient.fromJson(o.get("ingredient"));
				int count = o.has("count") ? o.get("count").getAsInt() : 1;

				if (ingredient.getItems().length == 1 && ingredient.getItems()[0].getItem() instanceof FluidContainerItem) {
					r.inputFluids.add(FluidContainerItem.getFluidStack(ingredient.getItems()[0]));
				} else {
					r.inputItems.add(new ItemIngredientPair(ingredient, count));
				}
			}
		}

		if (json.has("output")) {
			for (JsonElement e : json.get("output").getAsJsonArray()) {
				JsonObject o = e.getAsJsonObject();
				ItemStack stack = ShapedRecipe.itemFromJson(o);

				if (stack.getItem() instanceof FluidContainerItem) {
					r.outputFluids.add(FluidContainerItem.getFluidStack(stack));
				} else {
					r.outputItems.add(stack);
				}
			}
		}

		if (r.inputItems.isEmpty() && r.inputFluids.isEmpty()) {
			throw new IllegalStateException("Can't have no input items and fluids!");
		}

		if (r.outputItems.isEmpty() && r.outputFluids.isEmpty()) {
			throw new IllegalStateException("Can't have no output items and fluids!");
		}

		r.updateInputs();
		return r;
	}

	@Override
	public JarRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		JarRecipe r = new JarRecipe(recipeId, buffer.readUtf(Short.MAX_VALUE));
		r.time = buffer.readVarInt();
		r.temperature = Temperature.VALUES[buffer.readVarInt()];
		r.canRepeat = buffer.readBoolean();
		r.stage = buffer.readUtf(Short.MAX_VALUE);
		int iin = buffer.readUnsignedByte();
		int fin = buffer.readUnsignedByte();
		int iout = buffer.readUnsignedByte();
		int fout = buffer.readUnsignedByte();

		for (int i = 0; i < iin; i++) {
			r.inputItems.add(new ItemIngredientPair(Ingredient.fromNetwork(buffer), buffer.readVarInt()));
		}

		for (int i = 0; i < fin; i++) {
			r.inputFluids.add(FluidStack.readFromPacket(buffer));
		}

		for (int i = 0; i < iout; i++) {
			r.outputItems.add(buffer.readItem());
		}

		for (int i = 0; i < fout; i++) {
			r.outputFluids.add(FluidStack.readFromPacket(buffer));
		}

		r.updateInputs();
		return r;
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, JarRecipe r) {
		buffer.writeUtf(r.getGroup(), Short.MAX_VALUE);
		buffer.writeVarInt(r.time);
		buffer.writeVarInt(r.temperature.ordinal());
		buffer.writeBoolean(r.canRepeat);
		buffer.writeUtf(r.stage, Short.MAX_VALUE);
		buffer.writeByte(r.inputItems.size());
		buffer.writeByte(r.inputFluids.size());
		buffer.writeByte(r.outputItems.size());
		buffer.writeByte(r.outputFluids.size());

		for (ItemIngredientPair p : r.inputItems) {
			p.ingredient.toNetwork(buffer);
			buffer.writeVarInt(p.amount);
		}

		for (FluidStack s : r.inputFluids) {
			s.writeToPacket(buffer);
		}

		for (ItemStack s : r.outputItems) {
			buffer.writeItem(s);
		}

		for (FluidStack s : r.outputFluids) {
			s.writeToPacket(buffer);
		}
	}
}
