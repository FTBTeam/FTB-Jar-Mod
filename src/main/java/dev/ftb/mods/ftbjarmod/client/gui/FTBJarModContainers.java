package dev.ftb.mods.ftbjarmod.client.gui;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class FTBJarModContainers {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, FTBJarMod.MOD_ID);

	//public static final RegistryObject<ContainerType<TemperedJarContainer>> TEMPERED_JAR = REGISTRY.register("tempered_jar", () -> new ContainerType<>((IContainerFactory<TemperedJarContainer>) TemperedJarContainer::new));
}
