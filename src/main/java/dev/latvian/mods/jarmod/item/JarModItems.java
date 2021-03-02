package dev.latvian.mods.jarmod.item;

import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.block.JarModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class JarModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, JarMod.MOD_ID);

	public static RegistryObject<Item> basicItem(String id) {
		return REGISTRY.register(id, () -> new Item(new Item.Properties().tab(JarMod.group)));
	}

	public static RegistryObject<BlockItem> blockItem(String id, Supplier<Block> sup) {
		return REGISTRY.register(id, () -> new BlockItem(sup.get(), new Item.Properties().tab(JarMod.group)));
	}

	public static final RegistryObject<Item> CAST_IRON_INGOT = basicItem("cast_iron_ingot");
	public static final RegistryObject<Item> CAST_IRON_NUGGET = basicItem("cast_iron_nugget");
	public static final RegistryObject<Item> CAST_IRON_GEAR = basicItem("cast_iron_gear");
	public static final RegistryObject<Item> TEMPERED_GLASS = basicItem("tempered_glass");

	public static final RegistryObject<BlockItem> CAST_IRON_BLOCK = blockItem("cast_iron_block", JarModBlocks.CAST_IRON_BLOCK);
	public static final RegistryObject<BlockItem> JAR = blockItem("jar", JarModBlocks.JAR);
	public static final RegistryObject<BlockItem> TEMPERED_JAR = blockItem("tempered_jar", JarModBlocks.TEMPERED_JAR);
	public static final RegistryObject<BlockItem> TUBE = blockItem("tube", JarModBlocks.TUBE);
	public static final RegistryObject<BlockItem> LOW_TEMPERATURE_HEAT_SINK = blockItem("low_temperature_heat_sink", JarModBlocks.LOW_TEMPERATURE_HEAT_SINK);
	public static final RegistryObject<BlockItem> HIGH_TEMPERATURE_HEAT_SINK = blockItem("high_temperature_heat_sink", JarModBlocks.HIGH_TEMPERATURE_HEAT_SINK);
	public static final RegistryObject<BlockItem> SUBZERO_TEMPERATURE_HEAT_SINK = blockItem("subzero_temperature_heat_sink", JarModBlocks.SUBZERO_TEMPERATURE_HEAT_SINK);
	public static final RegistryObject<BlockItem> CREATIVE_LOW_TEMPERATURE_SOURCE = blockItem("creative_low_temperature_source", JarModBlocks.CREATIVE_LOW_TEMPERATURE_SOURCE);
	public static final RegistryObject<BlockItem> CREATIVE_HIGH_TEMPERATURE_SOURCE = blockItem("creative_high_temperature_source", JarModBlocks.CREATIVE_HIGH_TEMPERATURE_SOURCE);
	public static final RegistryObject<BlockItem> CREATIVE_SUBZERO_TEMPERATURE_SOURCE = blockItem("creative_subzero_temperature_source", JarModBlocks.CREATIVE_SUBZERO_TEMPERATURE_SOURCE);
}