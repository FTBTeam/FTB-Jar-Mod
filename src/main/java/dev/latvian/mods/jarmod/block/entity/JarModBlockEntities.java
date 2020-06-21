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
	public static final DeferredRegister<TileEntityType<?>> REGISTRY = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, JarMod.MOD_ID);

	public static final RegistryObject<TileEntityType<JarBlockEntity>> JAR = REGISTRY.register("jar", () -> TileEntityType.Builder.create(JarBlockEntity::new, JarModBlocks.JAR.get()).build(null));
	public static final RegistryObject<TileEntityType<TemperedJarBlockEntity>> TEMPERED_JAR = REGISTRY.register("tempered_jar", () -> TileEntityType.Builder.create(TemperedJarBlockEntity::new, JarModBlocks.TEMPERED_JAR.get()).build(null));
	public static final RegistryObject<TileEntityType<HeatSinkBlockEntity>> HEAT_SINK = REGISTRY.register("heat_sink", () -> TileEntityType.Builder.create(HeatSinkBlockEntity::new, JarModBlocks.HEAT_SINK.get()).build(null));
}