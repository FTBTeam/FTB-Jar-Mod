package dev.ftb.mods.ftbjarmod.recipe;

import com.google.gson.JsonObject;
import dev.ftb.mods.ftbjarmod.temperature.Temperature;
import dev.ftb.mods.ftbjarmod.temperature.TemperaturePair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * @author LatvianModder
 */
public class TemperatureSourceRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<TemperatureSourceRecipe> {
	@Override
	public TemperatureSourceRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		TemperatureSourceRecipe r = new TemperatureSourceRecipe(recipeId, json.has("group") ? json.get("group").getAsString() : "");

		if (json.has("block")) {
			r.setBlockString(json.get("block").getAsString());
		}

		Temperature temperature = Temperature.NONE;
		double efficiency = 1D;

		if (json.has("temperature")) {
			temperature = Temperature.byName(json.get("temperature").getAsString());
		}

		if (json.has("item")) {
			if (json.get("item").isJsonObject()) {
				r.item = ShapedRecipe.itemFromJson(json.get("item").getAsJsonObject());
			} else {
				r.item = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("item").getAsString())));
			}
		}

		if (json.has("hideFromJEI")) {
			r.hideFromJEI = json.get("hideFromJEI").getAsBoolean();
		}

		if (json.has("efficiency")) {
			efficiency = json.get("efficiency").getAsDouble();
		}

		r.temperaturePair = new TemperaturePair(temperature, efficiency);

		return r;
	}

	@Override
	public TemperatureSourceRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		TemperatureSourceRecipe r = new TemperatureSourceRecipe(recipeId, buffer.readUtf(Short.MAX_VALUE));
		r.setBlockString(buffer.readUtf(Short.MAX_VALUE));
		r.temperaturePair = new TemperaturePair(Temperature.VALUES[buffer.readVarInt()], buffer.readDouble());
		r.item = buffer.readItem();
		r.hideFromJEI = buffer.readBoolean();
		return r;
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, TemperatureSourceRecipe r) {
		buffer.writeUtf(r.getGroup(), Short.MAX_VALUE);
		buffer.writeUtf(r.getBlockString(), Short.MAX_VALUE);
		buffer.writeVarInt(r.temperaturePair.temperature.ordinal());
		buffer.writeDouble(r.temperaturePair.efficiency);
		buffer.writeItem(r.item);
		buffer.writeBoolean(r.hideFromJEI);
	}
}
