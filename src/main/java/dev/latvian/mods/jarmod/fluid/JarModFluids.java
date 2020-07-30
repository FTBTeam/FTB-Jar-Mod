package dev.latvian.mods.jarmod.fluid;

import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.item.JarModItems;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
@SuppressWarnings("Convert2MethodRef")
public class JarModFluids
{
	public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, JarMod.MOD_ID);

	private static RegistryObject<Fluid> createFluid(String name, Supplier<Item> bucket, Consumer<FluidAttributes.Builder> b)
	{
		final List<RegistryObject<Fluid>> callback = new ArrayList<>(1);
		FluidAttributes.Builder builder = FluidAttributes.builder(new ResourceLocation(JarMod.MOD_ID, "fluid/" + name), new ResourceLocation(JarMod.MOD_ID, "fluid/" + name));
		builder.translationKey("fluid.jarmod." + name);
		b.accept(builder);
		RegistryObject<Fluid> f = REGISTRY.register(name, () -> new ForgeFlowingFluid.Source(new ForgeFlowingFluid.Properties(() -> callback.get(0).get(), () -> callback.get(0).get(), builder).bucket(bucket)));
		callback.add(f);
		return f;
	}

	public static final RegistryObject<Fluid> STEAM = createFluid("steam", () -> JarModItems.STEAM_BUCKET.get(), builder -> builder.density(590).viscosity(100).temperature(100));
	public static final RegistryObject<Fluid> BIOMASS = createFluid("biomass", () -> JarModItems.BIOMASS_BUCKET.get(), builder -> builder.density(3000).viscosity(6000));
	public static final RegistryObject<Fluid> BIOFUEL = createFluid("biofuel", () -> JarModItems.BIOFUEL_BUCKET.get(), builder -> builder.density(3000).viscosity(6000));
	public static final RegistryObject<Fluid> SULPHURIC_ACID = createFluid("sulphuric_acid", () -> JarModItems.SULPHURIC_ACID_BUCKET.get(), builder -> builder.density(3000).viscosity(6000));
}