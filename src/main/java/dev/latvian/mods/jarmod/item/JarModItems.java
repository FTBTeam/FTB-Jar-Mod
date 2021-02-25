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
	public static final RegistryObject<BlockItem> HEAT_SINK = blockItem("heat_sink", JarModBlocks.HEAT_SINK);
	public static final RegistryObject<BlockItem> ELECTRIC_HEAT_SINK = blockItem("electric_heat_sink", JarModBlocks.ELECTRIC_HEAT_SINK);
	public static final RegistryObject<BlockItem> CREATIVE_HEAT_SINK = blockItem("creative_heat_sink", JarModBlocks.CREATIVE_HEAT_SINK);
}