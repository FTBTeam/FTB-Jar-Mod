package dev.ftb.mods.ftbjarmod.kubejs;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;
import net.minecraft.resources.ResourceLocation;

public class FTBJarModKubeJSPlugin extends KubeJSPlugin {
	@Override
	public void addRecipes(RegisterRecipeHandlersEvent event) {
		event.register(new ResourceLocation(FTBJarMod.MOD_ID, "jar"), JarRecipeJS::new);
	}
}
