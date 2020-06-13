package dev.latvian.mods.jar.recipe;

import com.mojang.brigadier.StringReader;
import dev.latvian.mods.jar.JarMod;
import dev.latvian.mods.jar.block.JarTemperature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.state.IProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class TemperatureModifierRecipe implements IRecipe<NoInventory>
{
	public static final TemperatureModifierRecipe NONE = new TemperatureModifierRecipe(new ResourceLocation(JarMod.MOD_ID + ":temperature_modifiers/none"), "");

	private final ResourceLocation id;
	private final String group;
	private String blockString;
	private Block block;
	private Map<IProperty<?>, Comparable<?>> properties;
	public int temperature;
	public double efficiency;
	public ItemStack item;

	public TemperatureModifierRecipe(ResourceLocation i, String g)
	{
		id = i;
		group = g;
		blockString = "";
		temperature = 0;
		efficiency = 1D;
		item = ItemStack.EMPTY;
	}

	public JarTemperature getJarTemperature()
	{
		return temperature == 0 ? JarTemperature.NORMAL : temperature > 0 ? JarTemperature.HOT : JarTemperature.COLD;
	}

	public void setBlockString(String s)
	{
		blockString = s;

		try
		{
			BlockStateParser input = new BlockStateParser(new StringReader(blockString), false).parse(false);
			block = input.getState().getBlock();
			properties = input.getProperties();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			block = Blocks.AIR;
			properties = Collections.emptyMap();
		}

		if (item.isEmpty())
		{
			item = new ItemStack(block);
		}
	}

	public String getBlockString()
	{
		return blockString;
	}

	public boolean test(BlockState state)
	{
		if (block == Blocks.AIR || block != state.getBlock())
		{
			return false;
		}

		for (Map.Entry<IProperty<?>, Comparable<?>> entry : properties.entrySet())
		{
			if (!state.get(entry.getKey()).equals(entry.getValue()))
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean matches(NoInventory inv, World world)
	{
		return true;
	}

	@Override
	public ItemStack getCraftingResult(NoInventory inv)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return true;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId()
	{
		return id;
	}

	@Override
	public String getGroup()
	{
		return group;
	}

	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return JarModRecipeSerializers.TEMPERATURE_MODIFIER.get();
	}

	@Override
	public IRecipeType<?> getType()
	{
		return JarModRecipeSerializers.TEMPERATURE_MODIFIER_TYPE;
	}
}