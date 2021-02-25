package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.JarMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class JarModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, JarMod.MOD_ID);

	public static final RegistryObject<Block> CAST_IRON_BLOCK = REGISTRY.register("cast_iron_block", () -> new Block(Block.Properties.of(Material.METAL).strength(5F, 6F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> JAR = REGISTRY.register("jar", JarBlock::new);
	public static final RegistryObject<Block> TEMPERED_JAR = REGISTRY.register("tempered_jar", TemperedJarBlock::new);
	public static final RegistryObject<Block> TUBE = REGISTRY.register("tube", TubeBlock::new);
	public static final RegistryObject<Block> HEAT_SINK = REGISTRY.register("heat_sink", HeatSinkBlock::new);
	public static final RegistryObject<Block> ELECTRIC_HEAT_SINK = REGISTRY.register("electric_heat_sink", ElectricHeatSinkBlock::new);
	public static final RegistryObject<Block> CREATIVE_HEAT_SINK = REGISTRY.register("creative_heat_sink", ElectricHeatSinkBlock::new);
}
