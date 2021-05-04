package dev.ftb.mods.ftbjarmod.client;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.FTBJarModCommon;
import dev.ftb.mods.ftbjarmod.block.FTBJarModBlocks;
import dev.ftb.mods.ftbjarmod.block.entity.JarModBlockEntities;
import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import dev.ftb.mods.ftbjarmod.block.entity.render.JarBlockEntityRenderer;
import dev.ftb.mods.ftbjarmod.block.entity.render.TemperedJarBlockEntityRenderer;
import dev.ftb.mods.ftbjarmod.client.gui.TemperedJarScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
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
		ItemBlockRenderTypes.setRenderLayer(FTBJarModBlocks.LOW_TEMPERATURE_HEAT_SINK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(FTBJarModBlocks.HIGH_TEMPERATURE_HEAT_SINK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(FTBJarModBlocks.SUBZERO_TEMPERATURE_HEAT_SINK.get(), RenderType.cutout());

		ClientRegistry.bindTileEntityRenderer(JarModBlockEntities.JAR.get(), JarBlockEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(JarModBlockEntities.TEMPERED_JAR.get(), TemperedJarBlockEntityRenderer::new);
	}

	@Override
	public void openTemperedJarScreen(TemperedJarBlockEntity entity) {
		Minecraft.getInstance().setScreen(new TemperedJarScreen(entity));
	}
}
