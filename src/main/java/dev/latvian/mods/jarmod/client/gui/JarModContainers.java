package dev.latvian.mods.jarmod.client.gui;

import dev.latvian.mods.jarmod.JarMod;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class JarModContainers
{
	public static final DeferredRegister<ContainerType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, JarMod.MOD_ID);

	//public static final RegistryObject<ContainerType<TemperedJarContainer>> TEMPERED_JAR = REGISTRY.register("tempered_jar", () -> new ContainerType<>((IContainerFactory<TemperedJarContainer>) TemperedJarContainer::new));
}
