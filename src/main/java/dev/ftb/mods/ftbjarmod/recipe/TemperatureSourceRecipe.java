package dev.ftb.mods.ftbjarmod.recipe;

import com.mojang.brigadier.StringReader;
import dev.ftb.mods.ftbjarmod.temperature.Temperature;
import dev.ftb.mods.ftbjarmod.temperature.TemperaturePair;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class TemperatureSourceRecipe implements Recipe<NoInventory> {
	private final ResourceLocation id;
	private final String group;
	private String blockString;
	private Block block;
	private Map<Property<?>, Comparable<?>> blockProperties;
	public TemperaturePair temperaturePair;
	public ItemStack item;
	public boolean hideFromJEI;

	public TemperatureSourceRecipe(ResourceLocation i, String g) {
		id = i;
		group = g;
		blockString = "";
		temperaturePair = new TemperaturePair(Temperature.LOW, 1D);
		item = ItemStack.EMPTY;
		hideFromJEI = false;
	}

	public void setBlockString(String s) {
		blockString = s;

		try {
			BlockStateParser input = new BlockStateParser(new StringReader(blockString), false).parse(false);
			block = Objects.requireNonNull(input.getState()).getBlock();
			blockProperties = input.getProperties();
		} catch (Exception ex) {
			ex.printStackTrace();
			block = Blocks.AIR;
			blockProperties = Collections.emptyMap();
		}

		if (item.isEmpty()) {
			item = new ItemStack(block);
		}
	}

	public String getBlockString() {
		return blockString;
	}

	public boolean test(BlockState state) {
		if (block == Blocks.AIR || block != state.getBlock()) {
			return false;
		}

		for (Map.Entry<Property<?>, Comparable<?>> entry : blockProperties.entrySet()) {
			if (!state.getValue(entry.getKey()).equals(entry.getValue())) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean matches(NoInventory inv, Level world) {
		return true;
	}

	@Override
	public ItemStack assemble(NoInventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return FTBJarModRecipeSerializers.TEMPERATURE_SOURCE.get();
	}

	@Override
	public RecipeType<?> getType() {
		return FTBJarModRecipeSerializers.TEMPERATURE_SOURCE_TYPE;
	}
}