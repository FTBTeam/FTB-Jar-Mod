package dev.latvian.mods.jarmod.recipe;

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
public class HeatSourceRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<HeatSourceRecipe>
{
	@Override
	public HeatSourceRecipe read(ResourceLocation recipeId, JsonObject json)
	{
		HeatSourceRecipe r = new HeatSourceRecipe(recipeId, json.has("group") ? json.get("group").getAsString() : "");

		if (json.has("block"))
		{
			r.setBlockString(json.get("block").getAsString());
		}

		if (json.has("resultBlock"))
		{
			r.setResultBlockString(json.get("resultBlock").getAsString());
		}

		if (json.has("temperature"))
		{
			r.temperature = json.get("temperature").getAsInt();
		}

		if (json.has("burnTime"))
		{
			r.burnTime = json.get("burnTime").getAsInt();
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

		if (json.has("resultItem"))
		{
			if (json.get("resultItem").isJsonObject())
			{
				r.resultItem = ShapedRecipe.deserializeItem(json.get("resultItem").getAsJsonObject());
			}
			else
			{
				r.resultItem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("resultItem").getAsString())));
			}
		}

		return r;
	}

	@Override
	public HeatSourceRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
	{
		HeatSourceRecipe r = new HeatSourceRecipe(recipeId, buffer.readString(Short.MAX_VALUE));
		r.setBlockString(buffer.readString(Short.MAX_VALUE));
		r.setResultBlockString(buffer.readString(Short.MAX_VALUE));
		r.temperature = buffer.readVarInt();
		r.burnTime = buffer.readVarInt();
		r.item = buffer.readItemStack();
		r.resultItem = buffer.readItemStack();
		return r;
	}

	@Override
	public void write(PacketBuffer buffer, HeatSourceRecipe r)
	{
		buffer.writeString(r.getGroup(), Short.MAX_VALUE);
		buffer.writeString(r.getBlockString(), Short.MAX_VALUE);
		buffer.writeString(r.getResultBlockString(), Short.MAX_VALUE);
		buffer.writeVarInt(r.temperature);
		buffer.writeVarInt(r.burnTime);
		buffer.writeItemStack(r.item);
		buffer.writeItemStack(r.resultItem);
	}
}
