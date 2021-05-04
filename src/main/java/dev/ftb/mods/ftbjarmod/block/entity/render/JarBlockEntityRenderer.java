package dev.ftb.mods.ftbjarmod.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import dev.ftb.mods.ftbjarmod.block.entity.JarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author LatvianModder
 */
public class JarBlockEntityRenderer extends BlockEntityRenderer<JarBlockEntity> {
	public JarBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(JarBlockEntity entity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		Minecraft mc = Minecraft.getInstance();
		Matrix4f m = matrixStack.last().pose();

		if (entity.tank.isEmpty()) {
			return;
		}

		FluidStack fluid = entity.tank.getFluid();

		VertexConsumer builder = buffer.getBuffer(RenderType.translucent()).getVertexBuilder();

		mc.getTextureManager().bind(TextureAtlas.LOCATION_BLOCKS);
		TextureAtlasSprite sprite = mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(fluid.getFluid().getAttributes().getStillTexture(fluid));

		Matrix3f n = matrixStack.last().normal();
		int color = fluid.getFluid().getAttributes().getColor(fluid);
		float r = ((color >> 16) & 255) / 255F;
		float g = ((color >> 8) & 255) / 255F;
		float b = ((color >> 0) & 255) / 255F;
		float a = 1F;

		float s0 = 3.2F / 16F;
		float s1 = 1F - s0;

		float y0 = 0.2F / 16F;
		float y1 = (0.2F + 12.6F * entity.tank.getFluidAmount() / (float) entity.tank.getCapacity()) / 16F;

		float u0 = sprite.getU(3D);
		float v0 = sprite.getV0();
		float u1 = sprite.getU(13D);
		float v1 = sprite.getV(y1 * 16D);

		float u0top = sprite.getU(3D);
		float v0top = sprite.getV(3D);
		float u1top = sprite.getU(13D);
		float v1top = sprite.getV(13D);

		builder.vertex(m, s0, y1, s0).color(r, g, b, a).uv(u0top, v0top).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s0, y1, s1).color(r, g, b, a).uv(u0top, v1top).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s1, y1, s1).color(r, g, b, a).uv(u1top, v1top).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s1, y1, s0).color(r, g, b, a).uv(u1top, v0top).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();

		builder.vertex(m, s0, y0, s0).color(r, g, b, a).uv(u0top, v0top).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s1, y0, s0).color(r, g, b, a).uv(u1top, v0top).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s1, y0, s1).color(r, g, b, a).uv(u1top, v1top).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s0, y0, s1).color(r, g, b, a).uv(u0top, v1top).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();

		builder.vertex(m, s0, y1, s1).color(r, g, b, a).uv(u0, v0).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s0, y0, s1).color(r, g, b, a).uv(u0, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s1, y0, s1).color(r, g, b, a).uv(u1, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s1, y1, s1).color(r, g, b, a).uv(u1, v0).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();

		builder.vertex(m, s0, y1, s0).color(r, g, b, a).uv(u0, v0).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s1, y1, s0).color(r, g, b, a).uv(u1, v0).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s1, y0, s0).color(r, g, b, a).uv(u1, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s0, y0, s0).color(r, g, b, a).uv(u0, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();

		builder.vertex(m, s0, y1, s0).color(r, g, b, a).uv(u0, v0).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s0, y0, s0).color(r, g, b, a).uv(u0, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s0, y0, s1).color(r, g, b, a).uv(u1, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s0, y1, s1).color(r, g, b, a).uv(u1, v0).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();

		builder.vertex(m, s1, y1, s0).color(r, g, b, a).uv(u0, v0).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s1, y1, s1).color(r, g, b, a).uv(u1, v0).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s1, y0, s1).color(r, g, b, a).uv(u1, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.vertex(m, s1, y0, s0).color(r, g, b, a).uv(u0, v1).overlayCoords(combinedOverlay).uv2(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
	}
}
