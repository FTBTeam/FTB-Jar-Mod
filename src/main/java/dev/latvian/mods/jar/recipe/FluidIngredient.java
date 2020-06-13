package dev.latvian.mods.jar.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author LatvianModder
 */
public abstract class FluidIngredient implements Predicate<FluidStack>
{
	public static class FromID extends FluidIngredient
	{
		public static final int ID = 1;

		public final Fluid fluid;

		public FromID(Fluid f)
		{
			fluid = f;
		}

		@Override
		public boolean test(FluidStack fluidStack)
		{
			return fluid.isEquivalentTo(fluidStack.getFluid());
		}

		@Override
		public void write(PacketBuffer buffer)
		{
			buffer.writeByte(ID);
			buffer.writeResourceLocation(fluid.getRegistryName());
		}
	}

	public static class FromTag extends FluidIngredient
	{
		public static final int ID = 2;

		public final Tag<Fluid> tag;

		public FromTag(Tag<Fluid> t)
		{
			tag = t;
		}

		@Override
		public boolean test(FluidStack fluidStack)
		{
			return tag.contains(fluidStack.getFluid());
		}

		@Override
		public void write(PacketBuffer buffer)
		{
			buffer.writeByte(ID);
			buffer.writeResourceLocation(tag.getId());
		}
	}

	public static class FromList extends FluidIngredient
	{
		public static final int ID = 3;

		public final List<FluidIngredient> list = new ArrayList<>();

		@Override
		public boolean test(FluidStack fluidStack)
		{
			for (FluidIngredient ingredient : list)
			{
				if (ingredient.test(fluidStack))
				{
					return true;
				}
			}

			return false;
		}

		@Override
		public void write(PacketBuffer buffer)
		{
			buffer.writeByte(ID);
			buffer.writeVarInt(list.size());

			for (FluidIngredient ingredient : list)
			{
				ingredient.write(buffer);
			}
		}
	}

	public static FluidIngredient deserialize(@Nullable JsonElement json)
	{
		if (json instanceof JsonArray)
		{
			FromList list = new FromList();

			for (JsonElement e : (JsonArray) json)
			{
				list.list.add(deserialize(e));
			}

			return list;
		}
		else if (json instanceof JsonObject)
		{
			JsonObject o = (JsonObject) json;

			if (o.has("tag"))
			{
				Tag<Fluid> tag = FluidTags.getCollection().get(new ResourceLocation(o.get("tag").getAsString()));

				if (tag != null)
				{
					return new FromTag(tag);
				}
			}
			else if (o.has("fluid"))
			{
				Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(o.get("fluid").getAsString()));

				if (fluid != null && fluid != Fluids.EMPTY)
				{
					return new FromID(fluid);
				}
			}
		}
		else if (json instanceof JsonPrimitive)
		{
			Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(json.getAsString()));

			if (fluid != null && fluid != Fluids.EMPTY)
			{
				return new FromID(fluid);
			}
		}

		return new FromList();
	}

	public static FluidIngredient read(PacketBuffer buffer)
	{
		switch (buffer.readByte())
		{
			case FromID.ID:
			{
				Fluid fluid = ForgeRegistries.FLUIDS.getValue(buffer.readResourceLocation());

				if (fluid != null && fluid != Fluids.EMPTY)
				{
					return new FromID(fluid);
				}
			}
			case FromTag.ID:
			{
				Tag<Fluid> tag = FluidTags.getCollection().get(buffer.readResourceLocation());

				if (tag != null)
				{
					return new FromTag(tag);
				}
			}
			case FromList.ID:
			{
				int s = buffer.readVarInt();
				FromList list = new FromList();

				for (int i = 0; i < s; i++)
				{
					list.list.add(read(buffer));
				}

				return list;
			}
		}

		return new FromList();
	}

	public abstract void write(PacketBuffer buffer);
}