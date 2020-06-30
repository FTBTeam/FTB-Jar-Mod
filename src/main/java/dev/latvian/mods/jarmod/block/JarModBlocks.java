package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.JarMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class JarModBlocks
{
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, JarMod.MOD_ID);

	public static final RegistryObject<Block> CAST_IRON_BLOCK = REGISTRY.register("cast_iron_block", () -> new Block(Block.Properties.create(Material.IRON).hardnessAndResistance(5F, 6F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> JAR = REGISTRY.register("jar", JarBlock::new);
	public static final RegistryObject<Block> TEMPERED_JAR = REGISTRY.register("tempered_jar", TemperedJarBlock::new);
	public static final RegistryObject<Block> TUBE = REGISTRY.register("tube", TubeBlock::new);
	public static final RegistryObject<Block> TANK_CONTROLLER = REGISTRY.register("tank_controller", TankControllerBlock::new);
	public static final RegistryObject<Block> TANK_WALL = REGISTRY.register("tank_wall", TankWallBlock::new);
	public static final RegistryObject<Block> TANK_GLASS = REGISTRY.register("tank_glass", TankGlassBlock::new);
	public static final RegistryObject<Block> HEAT_SINK = REGISTRY.register("heat_sink", HeatSinkBlock::new);
	public static final RegistryObject<Block> SLUICE = REGISTRY.register("sluice", SluiceBlock::new);
	public static final RegistryObject<Block> BOILER = REGISTRY.register("boiler", BoilerBlock::new);
}
