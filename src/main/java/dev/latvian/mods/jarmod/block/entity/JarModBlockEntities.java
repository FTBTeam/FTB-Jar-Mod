package dev.latvian.mods.jarmod.block.entity;

import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.block.JarModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class JarModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, JarMod.MOD_ID);

	public static final RegistryObject<BlockEntityType<JarBlockEntity>> JAR = REGISTRY.register("jar", () -> BlockEntityType.Builder.of(JarBlockEntity::new, JarModBlocks.JAR.get()).build(null));
	public static final RegistryObject<BlockEntityType<TemperedJarBlockEntity>> TEMPERED_JAR = REGISTRY.register("tempered_jar", () -> BlockEntityType.Builder.of(TemperedJarBlockEntity::new, JarModBlocks.TEMPERED_JAR.get()).build(null));
	public static final RegistryObject<BlockEntityType<HeatSinkBlockEntity>> HEAT_SINK = REGISTRY.register("heat_sink", () -> BlockEntityType.Builder.of(HeatSinkBlockEntity::new, JarModBlocks.HEAT_SINK.get()).build(null));
	public static final RegistryObject<BlockEntityType<ElectricHeatSinkBlockEntity>> ELECTRIC_HEAT_SINK = REGISTRY.register("electric_heat_sink", () -> BlockEntityType.Builder.of(ElectricHeatSinkBlockEntity::new, JarModBlocks.ELECTRIC_HEAT_SINK.get()).build(null));
	public static final RegistryObject<BlockEntityType<CreativeHeatSinkBlockEntity>> CREATIVE_HEAT_SINK = REGISTRY.register("creative_heat_sink", () -> BlockEntityType.Builder.of(CreativeHeatSinkBlockEntity::new, JarModBlocks.CREATIVE_HEAT_SINK.get()).build(null));
}