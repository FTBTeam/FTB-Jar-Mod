package dev.ftb.mods.ftbjarmod.recipe;

import com.google.gson.JsonObject;
import dev.ftb.mods.ftbjarmod.heat.Temperature;
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

		if (json.has("resultBlock")) {
			r.setResultBlockString(json.get("resultBlock").getAsString());
		}

		if (json.has("temperature")) {
			r.temperature = Temperature.byName(json.get("temperature").getAsString());
		}

		if (json.has("burnTime")) {
			r.burnTime = json.get("burnTime").getAsInt();
		}

		if (json.has("item")) {
			if (json.get("item").isJsonObject()) {
				r.item = ShapedRecipe.itemFromJson(json.get("item").getAsJsonObject());
			} else {
				r.item = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("item").getAsString())));
			}
		}

		if (json.has("resultItem")) {
			if (json.get("resultItem").isJsonObject()) {
				r.resultItem = ShapedRecipe.itemFromJson(json.get("resultItem").getAsJsonObject());
			} else {
				r.resultItem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("resultItem").getAsString())));
			}
		}

		return r;
	}

	@Override
	public TemperatureSourceRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		TemperatureSourceRecipe r = new TemperatureSourceRecipe(recipeId, buffer.readUtf(Short.MAX_VALUE));
		r.setBlockString(buffer.readUtf(Short.MAX_VALUE));
		r.setResultBlockString(buffer.readUtf(Short.MAX_VALUE));
		r.temperature = Temperature.VALUES[buffer.readVarInt()];
		r.burnTime = buffer.readVarInt();
		r.item = buffer.readItem();
		r.resultItem = buffer.readItem();
		return r;
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, TemperatureSourceRecipe r) {
		buffer.writeUtf(r.getGroup(), Short.MAX_VALUE);
		buffer.writeUtf(r.getBlockString(), Short.MAX_VALUE);
		buffer.writeUtf(r.getResultBlockString(), Short.MAX_VALUE);
		buffer.writeVarInt(r.temperature.ordinal());
		buffer.writeVarInt(r.burnTime);
		buffer.writeItem(r.item);
		buffer.writeItem(r.resultItem);
	}
}
