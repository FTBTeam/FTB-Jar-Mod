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
	public static final RegistryObject<BlockEntityType<HeatSinkBlockEntity>> HEAT_SINK = REGISTRY.register("heat_sink", () -> BlockEntityType.Builder.of(HeatSinkBlockEntity::new, JarModBlocks.LOW_TEMPERATURE_HEAT_SINK.get(), JarModBlocks.HIGH_TEMPERATURE_HEAT_SINK.get(), JarModBlocks.SUBZERO_TEMPERATURE_HEAT_SINK.get()).build(null));
}