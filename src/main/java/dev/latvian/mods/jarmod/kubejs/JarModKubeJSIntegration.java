package dev.latvian.mods.jarmod.kubejs;

import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;
import dev.latvian.mods.jarmod.JarMod;
import net.minecraft.resources.ResourceLocation;

public class JarModKubeJSIntegration {
	public static void init() {
		RegisterRecipeHandlersEvent.EVENT.register(event -> {
			event.register(new ResourceLocation(JarMod.MOD_ID, "jar"), JarRecipeJS::new);
		});
	}
}
