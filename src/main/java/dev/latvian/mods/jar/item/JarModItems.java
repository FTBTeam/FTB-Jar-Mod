package dev.latvian.mods.jar.item;

import dev.latvian.mods.jar.JarMod;
import dev.latvian.mods.jar.block.JarModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class JarModItems
{
	public static final DeferredRegister<Item> REGISTRY = new DeferredRegister<>(ForgeRegistries.ITEMS, JarMod.MOD_ID);

	public static RegistryObject<Item> basicItem(String id)
	{
		return REGISTRY.register(id, () -> new Item(new Item.Properties().group(JarMod.group)));
	}

	public static RegistryObject<BlockItem> blockItem(String id, Supplier<Block> sup)
	{
		return REGISTRY.register(id, () -> new BlockItem(sup.get(), new Item.Properties().group(JarMod.group)));
	}

	public static final RegistryObject<Item> CAST_IRON_INGOT = basicItem("cast_iron_ingot");
	public static final RegistryObject<Item> CAST_IRON_NUGGET = basicItem("cast_iron_nugget");
	public static final RegistryObject<Item> CAST_IRON_GEAR = basicItem("cast_iron_gear");
	public static final RegistryObject<Item> TEMPERED_GLASS = basicItem("tempered_glass");

	public static final RegistryObject<BlockItem> CAST_IRON_BLOCK = blockItem("cast_iron_block", JarModBlocks.CAST_IRON_BLOCK);
	public static final RegistryObject<BlockItem> JAR = blockItem("jar", JarModBlocks.JAR);
	public static final RegistryObject<BlockItem> TEMPERED_JAR = blockItem("tempered_jar", JarModBlocks.TEMPERED_JAR);
	public static final RegistryObject<BlockItem> TUBE = blockItem("tube", JarModBlocks.TUBE);
}
