package dev.latvian.mods.jarmod.client;

import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.JarModCommon;
import dev.latvian.mods.jarmod.block.JarModBlocks;
import dev.latvian.mods.jarmod.block.entity.JarModBlockEntities;
import dev.latvian.mods.jarmod.block.entity.TemperedJarBlockEntity;
import dev.latvian.mods.jarmod.block.entity.render.JarBlockEntityRenderer;
import dev.latvian.mods.jarmod.block.entity.render.TemperedJarBlockEntityRenderer;
import dev.latvian.mods.jarmod.client.gui.TemperedJarScreen;
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
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = JarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JarModClient extends JarModCommon {
	@Override
	public void init() {
	}

	@SubscribeEvent
	public static void setup(FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(JarModBlocks.JAR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JarModBlocks.TEMPERED_JAR.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JarModBlocks.LOW_TEMPERATURE_HEAT_SINK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JarModBlocks.HIGH_TEMPERATURE_HEAT_SINK.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(JarModBlocks.SUBZERO_TEMPERATURE_HEAT_SINK.get(), RenderType.cutout());

		ClientRegistry.bindTileEntityRenderer(JarModBlockEntities.JAR.get(), JarBlockEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(JarModBlockEntities.TEMPERED_JAR.get(), TemperedJarBlockEntityRenderer::new);
	}

	@Override
	public void openTemperedJarScreen(TemperedJarBlockEntity entity) {
		Minecraft.getInstance().setScreen(new TemperedJarScreen(entity));
	}
}
