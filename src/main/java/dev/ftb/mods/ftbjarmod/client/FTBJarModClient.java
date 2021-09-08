package dev.ftb.mods.ftbjarmod.client;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.FTBJarModCommon;
import dev.ftb.mods.ftbjarmod.block.FTBJarModBlocks;
import dev.ftb.mods.ftbjarmod.block.entity.FTBJarModBlockEntities;
import dev.ftb.mods.ftbjarmod.block.entity.render.JarBlockEntityRenderer;
import dev.ftb.mods.ftbjarmod.block.entity.render.TemperedJarBlockEntityRenderer;
import dev.ftb.mods.ftbjarmod.gui.FTBJarModMenus;
import dev.ftb.mods.ftbjarmod.gui.JarScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = FTBJarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FTBJarModClient extends FTBJarModCommon {
	@Override
	public void init() {
	}

	@SubscribeEvent
	public static void setup(FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(FTBJarModBlocks.JAR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(FTBJarModBlocks.TEMPERED_JAR.get(), RenderType.translucent());

		ClientRegistry.bindTileEntityRenderer(FTBJarModBlockEntities.JAR.get(), JarBlockEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(FTBJarModBlockEntities.TEMPERED_JAR.get(), TemperedJarBlockEntityRenderer::new);

		MenuScreens.register(FTBJarModMenus.JAR.get(), JarScreen::makeScreen);
	}

	@Override
	public void displayError(Component message) {
		Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.NARRATOR_TOGGLE, message, null));
	}
}
