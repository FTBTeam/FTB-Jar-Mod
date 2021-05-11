package dev.ftb.mods.ftbjarmod.block.entity;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.block.FTBJarModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class FTBJarModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, FTBJarMod.MOD_ID);

	public static final RegistryObject<BlockEntityType<JarBlockEntity>> JAR = REGISTRY.register("jar", () -> BlockEntityType.Builder.of(JarBlockEntity::new, FTBJarModBlocks.JAR.get()).build(null));
	public static final RegistryObject<BlockEntityType<TemperedJarBlockEntity>> TEMPERED_JAR = REGISTRY.register("tempered_jar", () -> BlockEntityType.Builder.of(TemperedJarBlockEntity::new, FTBJarModBlocks.TEMPERED_JAR.get()).build(null));
	public static final RegistryObject<BlockEntityType<HeatSinkBlockEntity>> HEAT_SINK = REGISTRY.register("heat_sink", () -> BlockEntityType.Builder.of(HeatSinkBlockEntity::new, FTBJarModBlocks.LOW_TEMPERATURE_HEAT_SINK.get(), FTBJarModBlocks.HIGH_TEMPERATURE_HEAT_SINK.get(), FTBJarModBlocks.SUBZERO_TEMPERATURE_HEAT_SINK.get()).build(null));
}