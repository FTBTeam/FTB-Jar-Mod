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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = JarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JarModClient extends JarModCommon
{
	@Override
	public void init()
	{
	}

	@SubscribeEvent
	public static void setup(FMLClientSetupEvent event)
	{
		RenderTypeLookup.setRenderLayer(JarModBlocks.JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(JarModBlocks.TEMPERED_JAR.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(JarModBlocks.TANK_GLASS.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(JarModBlocks.HEAT_SINK.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(JarModBlocks.SLUICE.get(), RenderType.getTranslucent());

		ClientRegistry.bindTileEntityRenderer(JarModBlockEntities.JAR.get(), JarBlockEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(JarModBlockEntities.TEMPERED_JAR.get(), TemperedJarBlockEntityRenderer::new);
	}

	@Override
	public void openTemperedJarScreen(TemperedJarBlockEntity entity)
	{
		Minecraft.getInstance().displayGuiScreen(new TemperedJarScreen(entity));
	}
}
