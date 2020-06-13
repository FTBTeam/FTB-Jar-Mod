package dev.latvian.mods.jar.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * @author LatvianModder
 */
public class TemperatureModifierRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TemperatureModifierRecipe>
{
	@Override
	public TemperatureModifierRecipe read(ResourceLocation recipeId, JsonObject json)
	{
		TemperatureModifierRecipe r = new TemperatureModifierRecipe(recipeId, json.has("group") ? json.get("group").getAsString() : "");

		if (json.has("block"))
		{
			r.setBlockString(json.get("block").getAsString());
		}

		if (json.has("temperature"))
		{
			r.temperature = json.get("temperature").getAsInt();
		}

		if (json.has("efficiency"))
		{
			r.efficiency = json.get("efficiency").getAsDouble();
		}

		if (json.has("item"))
		{
			if (json.get("item").isJsonObject())
			{
				r.item = ShapedRecipe.deserializeItem(json.get("item").getAsJsonObject());
			}
			else
			{
				r.item = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("item").getAsString())));
			}
		}

		return r;
	}

	@Override
	public TemperatureModifierRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
	{
		TemperatureModifierRecipe r = new TemperatureModifierRecipe(recipeId, buffer.readString(Short.MAX_VALUE));
		r.setBlockString(buffer.readString(Short.MAX_VALUE));
		r.temperature = buffer.readVarInt();
		r.efficiency = buffer.readDouble();
		r.item = buffer.readItemStack();
		return r;
	}

	@Override
	public void write(PacketBuffer buffer, TemperatureModifierRecipe r)
	{
		buffer.writeString(r.getGroup(), Short.MAX_VALUE);
		buffer.writeString(r.getBlockString(), Short.MAX_VALUE);
		buffer.writeVarInt(r.temperature);
		buffer.writeDouble(r.efficiency);
		buffer.writeItemStack(r.item);
	}
}
