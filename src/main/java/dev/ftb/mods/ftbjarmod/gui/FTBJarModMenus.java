package dev.ftb.mods.ftbjarmod.gui;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class FTBJarModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, FTBJarMod.MOD_ID);

	public static final RegistryObject<MenuType<JarMenu>> JAR = REGISTRY.register("jar", () -> new MenuType<>((IContainerFactory<JarMenu>) JarMenu::new));
}
