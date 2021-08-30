package dev.ftb.mods.ftbjarmod.block;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class FTBJarModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, FTBJarMod.MOD_ID);

	public static final RegistryObject<Block> CAST_IRON_BLOCK = REGISTRY.register("cast_iron_block", () -> new Block(Block.Properties.of(Material.METAL).strength(5F, 6F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> AUTO_PROCESSING_BLOCK = REGISTRY.register("auto_processing_block", AutoProcessingBlock::new);
	public static final RegistryObject<Block> JAR = REGISTRY.register("jar", JarBlock::new);
	public static final RegistryObject<Block> TEMPERED_JAR = REGISTRY.register("tempered_jar", TemperedJarBlock::new);
	public static final RegistryObject<Block> TUBE = REGISTRY.register("tube", TubeBlock::new);
	public static final RegistryObject<Block> CREATIVE_LOW_TEMPERATURE_SOURCE = REGISTRY.register("creative_low_temperature_source", CreativeTemperatureSourceBlock::new);
	public static final RegistryObject<Block> CREATIVE_HIGH_TEMPERATURE_SOURCE = REGISTRY.register("creative_high_temperature_source", CreativeTemperatureSourceBlock::new);
	public static final RegistryObject<Block> CREATIVE_SUBZERO_TEMPERATURE_SOURCE = REGISTRY.register("creative_subzero_temperature_source", CreativeTemperatureSourceBlock::new);
	public static final RegistryObject<Block> BLUE_MAGMA_BLOCK = REGISTRY.register("blue_magma_block", () -> new MagmaBlock(Block.Properties.of(Material.STONE, MaterialColor.NETHER).requiresCorrectToolForDrops().lightLevel(state -> 3).randomTicks().strength(0.5F).isValidSpawn((state, level, pos, entity) -> entity.fireImmune()).hasPostProcess((state, level, pos) -> true).emissiveRendering((state, level, pos) -> true)));
}
