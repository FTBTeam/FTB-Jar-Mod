package dev.ftb.mods.ftbjarmod.block;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.heat.Temperature;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class FTBJarModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, FTBJarMod.MOD_ID);

	public static final RegistryObject<Block> CAST_IRON_BLOCK = REGISTRY.register("cast_iron_block", () -> new Block(Block.Properties.of(Material.METAL).strength(5F, 6F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> JAR = REGISTRY.register("jar", JarBlock::new);
	public static final RegistryObject<Block> TEMPERED_JAR = REGISTRY.register("tempered_jar", TemperedJarBlock::new);
	public static final RegistryObject<Block> TUBE = REGISTRY.register("tube", TubeBlock::new);
	public static final RegistryObject<Block> LOW_TEMPERATURE_HEAT_SINK = REGISTRY.register("low_temperature_heat_sink", () -> new HeatSinkBlock(Temperature.LOW));
	public static final RegistryObject<Block> HIGH_TEMPERATURE_HEAT_SINK = REGISTRY.register("high_temperature_heat_sink", () -> new HeatSinkBlock(Temperature.HIGH));
	public static final RegistryObject<Block> SUBZERO_TEMPERATURE_HEAT_SINK = REGISTRY.register("subzero_temperature_heat_sink", () -> new HeatSinkBlock(Temperature.SUBZERO));
	public static final RegistryObject<Block> CREATIVE_LOW_TEMPERATURE_SOURCE = REGISTRY.register("creative_low_temperature_source", () -> new CreativeTemperatureSourceBlock(Temperature.LOW));
	public static final RegistryObject<Block> CREATIVE_HIGH_TEMPERATURE_SOURCE = REGISTRY.register("creative_high_temperature_source", () -> new CreativeTemperatureSourceBlock(Temperature.HIGH));
	public static final RegistryObject<Block> CREATIVE_SUBZERO_TEMPERATURE_SOURCE = REGISTRY.register("creative_subzero_temperature_source", () -> new CreativeTemperatureSourceBlock(Temperature.SUBZERO));

}
