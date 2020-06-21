package dev.latvian.mods.jarmod.recipe;

import com.mojang.brigadier.StringReader;
import dev.latvian.mods.jarmod.JarMod;
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
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class HeatSourceRecipe implements IRecipe<NoInventory>
{
	public static final HeatSourceRecipe NONE = new HeatSourceRecipe(new ResourceLocation(JarMod.MOD_ID + ":temperature_modifiers/none"), "");

	private final ResourceLocation id;
	private final String group;
	private String blockString;
	private Block block;
	private Map<IProperty<?>, Comparable<?>> blockProperties;
	public String resultBlockString;
	public BlockState resultBlock;
	public int temperature;
	public int burnTime;
	public ItemStack item;
	public ItemStack resultItem;

	public HeatSourceRecipe(ResourceLocation i, String g)
	{
		id = i;
		group = g;
		blockString = "";
		temperature = 0;
		burnTime = 24000;
		resultBlockString = "minecraft:air";
		resultBlock = Blocks.AIR.getDefaultState();
		item = ItemStack.EMPTY;
		resultItem = ItemStack.EMPTY;
	}

	public void setBlockString(String s)
	{
		blockString = s;

		try
		{
			BlockStateParser input = new BlockStateParser(new StringReader(blockString), false).parse(false);
			block = Objects.requireNonNull(input.getState()).getBlock();
			blockProperties = input.getProperties();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			block = Blocks.AIR;
			blockProperties = Collections.emptyMap();
		}

		if (item.isEmpty())
		{
			item = new ItemStack(block);
		}
	}

	public void setResultBlockString(String s)
	{
		resultBlockString = s;

		try
		{
			BlockStateParser input = new BlockStateParser(new StringReader(resultBlockString), false).parse(false);
			resultBlock = Objects.requireNonNull(input.getState());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			resultBlock = Blocks.AIR.getDefaultState();
		}

		if (resultItem.isEmpty())
		{
			resultItem = new ItemStack(resultBlock.getBlock());
		}
	}

	public String getBlockString()
	{
		return blockString;
	}

	public String getResultBlockString()
	{
		return resultBlockString;
	}

	public boolean test(BlockState state)
	{
		if (block == Blocks.AIR || block != state.getBlock())
		{
			return false;
		}

		for (Map.Entry<IProperty<?>, Comparable<?>> entry : blockProperties.entrySet())
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
		return JarModRecipeSerializers.HEAT_SOURCE.get();
	}

	@Override
	public IRecipeType<?> getType()
	{
		return JarModRecipeSerializers.HEAT_SOURCE_TYPE;
	}
}