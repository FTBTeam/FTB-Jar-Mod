package dev.latvian.mods.jarmod.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.jarmod.heat.Temperature;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
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

		if (json.has("inputItems")) {
			for (JsonElement e : json.get("inputItems").getAsJsonArray()) {
				JsonObject o = e.getAsJsonObject();
				Ingredient ingredient = Ingredient.fromJson(o.get("ingredient"));
				int count = o.has("count") ? o.get("count").getAsInt() : 1;
				r.inputItems.add(new ItemIngredientPair(ingredient, count));
			}
		}

		if (json.has("inputFluids")) {
			for (JsonElement e : json.get("inputFluids").getAsJsonArray()) {
				JsonObject o = e.getAsJsonObject();
				FluidStack stack = new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(o.get("fluid").getAsString())), FluidAttributes.BUCKET_VOLUME);

				if (o.has("amount")) {
					stack.setAmount(o.get("amount").getAsInt());
				}

				if (!stack.isEmpty()) {
					r.inputFluids.add(stack);
				}
			}
		}

		if (json.has("outputItems")) {
			for (JsonElement e : json.get("outputItems").getAsJsonArray()) {
				ItemStack stack = ShapedRecipe.itemFromJson(e.getAsJsonObject());

				if (!stack.isEmpty()) {
					r.outputItems.add(stack);
				}
			}
		}

		if (json.has("outputFluids")) {
			for (JsonElement e : json.get("outputFluids").getAsJsonArray()) {
				JsonObject o = e.getAsJsonObject();
				FluidStack stack = new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(o.get("fluid").getAsString())), FluidAttributes.BUCKET_VOLUME);

				if (o.has("amount")) {
					stack.setAmount(o.get("amount").getAsInt());
				}

				if (!stack.isEmpty()) {
					r.outputFluids.add(stack);
				}
			}
		}

		return r;
	}

	@Override
	public JarRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		JarRecipe r = new JarRecipe(recipeId, buffer.readUtf(Short.MAX_VALUE));
		r.time = buffer.readVarInt();
		r.temperature = Temperature.VALUES[buffer.readVarInt()];
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

		return r;
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, JarRecipe r) {
		buffer.writeUtf(r.getGroup(), Short.MAX_VALUE);
		buffer.writeVarInt(r.time);
		buffer.writeVarInt(r.temperature.ordinal());
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
