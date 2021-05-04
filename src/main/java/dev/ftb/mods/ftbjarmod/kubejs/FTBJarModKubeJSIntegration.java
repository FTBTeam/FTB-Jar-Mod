package dev.ftb.mods.ftbjarmod.kubejs;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;
import net.minecraft.resources.ResourceLocation;

public class FTBJarModKubeJSIntegration {
	public static void init() {
		RegisterRecipeHandlersEvent.EVENT.register(event -> {
			event.register(new ResourceLocation(FTBJarMod.MOD_ID, "jar"), JarRecipeJS::new);
		});
	}
}
