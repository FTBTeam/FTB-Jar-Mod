package dev.latvian.mods.jarmod.block.entity;

import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.block.JarModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class JarModBlockEntities
{
	public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, JarMod.MOD_ID);

	public static final RegistryObject<TileEntityType<JarBlockEntity>> JAR = REGISTRY.register("jar", () -> TileEntityType.Builder.create(JarBlockEntity::new, JarModBlocks.JAR.get()).build(null));
	public static final RegistryObject<TileEntityType<TemperedJarBlockEntity>> TEMPERED_JAR = REGISTRY.register("tempered_jar", () -> TileEntityType.Builder.create(TemperedJarBlockEntity::new, JarModBlocks.TEMPERED_JAR.get()).build(null));
	public static final RegistryObject<TileEntityType<HeatSinkBlockEntity>> HEAT_SINK = REGISTRY.register("heat_sink", () -> TileEntityType.Builder.create(HeatSinkBlockEntity::new, JarModBlocks.HEAT_SINK.get()).build(null));
	public static final RegistryObject<TileEntityType<BoilerBlockEntity>> BOILER = REGISTRY.register("boiler", () -> TileEntityType.Builder.create(BoilerBlockEntity::new, JarModBlocks.BOILER.get()).build(null));
	public static final RegistryObject<TileEntityType<BrickFurnaceBlockEntity>> BRICK_FURNACE = REGISTRY.register("brick_furnace", () -> TileEntityType.Builder.create(BrickFurnaceBlockEntity::new, JarModBlocks.BRICK_FURNACE.get()).build(null));
	public static final RegistryObject<TileEntityType<BrickFurnacePartBlockEntity>> BRICK_FURNACE_PART = REGISTRY.register("brick_furnace_part", () -> TileEntityType.Builder.create(BrickFurnacePartBlockEntity::new, JarModBlocks.BRICK_FURNACE_PART.get()).build(null));
	public static final RegistryObject<TileEntityType<ElectricHeatSinkBlockEntity>> ELECTRIC_HEAT_SINK = REGISTRY.register("electric_heat_sink", () -> TileEntityType.Builder.create(ElectricHeatSinkBlockEntity::new, JarModBlocks.ELECTRIC_HEAT_SINK.get()).build(null));
	public static final RegistryObject<TileEntityType<StoneFactoryBlockEntity>> STONE_FACTORY = REGISTRY.register("stone_factory", () -> TileEntityType.Builder.create(StoneFactoryBlockEntity::new, JarModBlocks.STONE_FACTORY.get()).build(null));
	public static final RegistryObject<TileEntityType<CreativeFEBlockEntity>> CREATIVE_FE = REGISTRY.register("creative_fe", () -> TileEntityType.Builder.create(CreativeFEBlockEntity::new, JarModBlocks.CREATIVE_FE.get()).build(null));
}