package dev.ftb.mods.ftbjarmod.client;

import com.mojang.blaze3d.platform.NativeImage;
import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.FTBJarModCommon;
import dev.ftb.mods.ftbjarmod.block.FTBJarModBlocks;
import dev.ftb.mods.ftbjarmod.block.entity.FTBJarModBlockEntities;
import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import dev.ftb.mods.ftbjarmod.block.entity.render.JarBlockEntityRenderer;
import dev.ftb.mods.ftbjarmod.block.entity.render.TemperedJarBlockEntityRenderer;
import dev.ftb.mods.ftbjarmod.client.gui.SelectJarRecipeScreen;
import dev.ftb.mods.ftbjarmod.item.FTBJarModItems;
import dev.ftb.mods.ftbjarmod.item.FluidItem;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = FTBJarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FTBJarModClient extends FTBJarModCommon {
	private static final Map<FluidKey, Integer> fluidColorCache = new HashMap<>();

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

		ClientRegistry.bindTileEntityRenderer(FTBJarModBlockEntities.JAR.get(), JarBlockEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(FTBJarModBlockEntities.TEMPERED_JAR.get(), TemperedJarBlockEntityRenderer::new);
	}

	@SubscribeEvent
	public static void colorRegistry(ColorHandlerEvent.Item event) {
		event.getItemColors().register((stack, i) -> {
			if (i == 1) {
				FluidStack fluidStack = FluidItem.getFluidStack(stack);
				FluidKey key = new FluidKey(fluidStack);
				Integer c = fluidColorCache.get(key);

				if (c == null) {
					c = calculateFluidColor(fluidStack);
					fluidColorCache.put(key, c);
				}

				return c;
			}

			return 0xFFFFFFFF;
		}, FTBJarModItems.FLUID.get());
	}

	private static int calculateFluidColor(FluidStack fluidStack) {
		AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS);

		if (!(texture instanceof TextureAtlas)) {
			return 0xFFFF00FF;
		}

		TextureAtlasSprite sprite = ((TextureAtlas) texture).getSprite(fluidStack.getFluid().getAttributes().getStillTexture(fluidStack));

		if (sprite == null || sprite instanceof MissingTextureAtlasSprite || sprite.getFrameCount() == 0) {
			return 0xFFFF00FF;
		}

		Color4I col = Color4I.rgb(fluidStack.getFluid().getAttributes().getColor(fluidStack));

		float[] rgba = {0F, 0F, 0F, 0F};
		float tintR = col.redf();
		float tintG = col.greenf();
		float tintB = col.bluef();

		for (int frame = 0; frame < sprite.getFrameCount(); frame++) {
			for (int y = 0; y < sprite.getHeight(); y++) {
				for (int x = 0; x < sprite.getWidth(); x++) {
					int color = sprite.getPixelRGBA(frame, x, y);
					float a = NativeImage.getA(color) / 255F;

					if (a > 0F) {
						rgba[0] += NativeImage.getR(color) / 255F * tintR;
						rgba[1] += NativeImage.getG(color) / 255F * tintG;
						rgba[2] += NativeImage.getB(color) / 255F * tintB;
						rgba[3] += a;
					}
				}
			}
		}

		if (rgba[3] == 0F) {
			return 0xFFFF00FF;
		}

		return Color4I.rgba((int) (rgba[0] * 255F / rgba[3]), (int) (rgba[1] * 255F / rgba[3]), (int) (rgba[2] * 255F / rgba[3]), 255).rgba();
	}

	@Override
	public void openTemperedJarScreen(TemperedJarBlockEntity entity) {
		new SelectJarRecipeScreen(entity).openGui();
	}
}
