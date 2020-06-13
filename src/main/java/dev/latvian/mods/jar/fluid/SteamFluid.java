package dev.latvian.mods.jar.fluid;

import dev.latvian.mods.jar.JarMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

/**
 * @author LatvianModder
 */
public class SteamFluid extends ForgeFlowingFluid.Source
{
	public SteamFluid()
	{
		super(new Properties(JarModFluids.STEAM, JarModFluids.STEAM, FluidAttributes.builder(
				new ResourceLocation(JarMod.MOD_ID, "block/steam_still"),
				new ResourceLocation(JarMod.MOD_ID, "block/steam_flow"))
				.translationKey("block.jarmod.steam")
				.density(590)
				.viscosity(100)
				.temperature(100))
		);
	}
}
