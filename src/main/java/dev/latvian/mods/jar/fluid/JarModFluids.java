package dev.latvian.mods.jar.fluid;

import dev.latvian.mods.jar.JarMod;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class JarModFluids
{
	public static final DeferredRegister<Fluid> REGISTRY = new DeferredRegister<>(ForgeRegistries.FLUIDS, JarMod.MOD_ID);

	public static final RegistryObject<Fluid> STEAM = REGISTRY.register("steam", SteamFluid::new);
}