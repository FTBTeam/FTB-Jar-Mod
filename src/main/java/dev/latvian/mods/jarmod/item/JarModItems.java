package dev.latvian.mods.jarmod.item;

import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.block.JarModBlocks;
import dev.latvian.mods.jarmod.fluid.JarModFluids;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
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

	public static RegistryObject<BucketItem> bucketItem(String id, Supplier<Fluid> sup)
	{
		return REGISTRY.register(id, () -> new BucketItem(sup, new Item.Properties().group(JarMod.group)));
	}

	public static final RegistryObject<Item> CAST_IRON_INGOT = basicItem("cast_iron_ingot");
	public static final RegistryObject<Item> CAST_IRON_NUGGET = basicItem("cast_iron_nugget");
	public static final RegistryObject<Item> CAST_IRON_GEAR = basicItem("cast_iron_gear");
	public static final RegistryObject<Item> TEMPERED_GLASS = basicItem("tempered_glass");
	public static final RegistryObject<Item> BIOMASS = basicItem("biomass");
	public static final RegistryObject<Item> BIOPLASTIC = basicItem("bioplastic");

	public static final RegistryObject<BlockItem> CAST_IRON_BLOCK = blockItem("cast_iron_block", JarModBlocks.CAST_IRON_BLOCK);
	public static final RegistryObject<BlockItem> JAR = blockItem("jar", JarModBlocks.JAR);
	public static final RegistryObject<BlockItem> TEMPERED_JAR = blockItem("tempered_jar", JarModBlocks.TEMPERED_JAR);
	public static final RegistryObject<BlockItem> TUBE = blockItem("tube", JarModBlocks.TUBE);
	public static final RegistryObject<BlockItem> TANK_CONTROLLER = blockItem("tank_controller", JarModBlocks.TANK_CONTROLLER);
	public static final RegistryObject<BlockItem> TANK_WALL = blockItem("tank_wall", JarModBlocks.TANK_WALL);
	public static final RegistryObject<BlockItem> TANK_GLASS = blockItem("tank_glass", JarModBlocks.TANK_GLASS);
	public static final RegistryObject<BlockItem> HEAT_SINK = blockItem("heat_sink", JarModBlocks.HEAT_SINK);

	public static final RegistryObject<BucketItem> STEAM_BUCKET = bucketItem("steam_bucket", JarModFluids.STEAM);
	public static final RegistryObject<BucketItem> BIOMASS_BUCKET = bucketItem("biomass_bucket", JarModFluids.BIOMASS);
	public static final RegistryObject<BucketItem> BIOFUEL_BUCKET = bucketItem("biofuel_bucket", JarModFluids.BIOFUEL);
}