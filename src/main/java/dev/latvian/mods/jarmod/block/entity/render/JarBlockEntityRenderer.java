package dev.latvian.mods.jarmod.block.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.latvian.mods.jarmod.block.entity.JarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author LatvianModder
 */
public class JarBlockEntityRenderer extends TileEntityRenderer<JarBlockEntity>
{
	public JarBlockEntityRenderer(TileEntityRendererDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(JarBlockEntity entity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
	{
		Minecraft mc = Minecraft.getInstance();
		Matrix4f m = matrixStack.getLast().getMatrix();

		if (entity.tank.isEmpty())
		{
			return;
		}

		FluidStack fluid = entity.tank.getFluid();

		IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent()).getVertexBuilder();

		mc.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite sprite = mc.getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(fluid.getFluid().getAttributes().getStillTexture(fluid));

		Matrix3f n = matrixStack.getLast().getNormal();
		int color = fluid.getFluid().getAttributes().getColor(fluid);
		float r = ((color >> 16) & 255) / 255F;
		float g = ((color >> 8) & 255) / 255F;
		float b = ((color >> 0) & 255) / 255F;
		float a = 1F;

		float s0 = 3.2F / 16F;
		float s1 = 1F - s0;

		float y0 = 0.2F / 16F;
		float y1 = (0.2F + 12.6F * entity.tank.getFluidAmount() / (float) entity.tank.getCapacity()) / 16F;

		float u0 = sprite.getMinU();
		float v0 = sprite.getMinV();
		float u1 = sprite.getMaxU();
		float v1 = sprite.getMaxV();

		// UP

		builder.pos(m, s0, y1, s0).color(r, g, b, a).tex(u0, v0).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s0, y1, s1).color(r, g, b, a).tex(u0, v1).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s1, y1, s1).color(r, g, b, a).tex(u1, v1).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s1, y1, s0).color(r, g, b, a).tex(u1, v0).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();

		builder.pos(m, s0, y0, s0).color(r, g, b, a).tex(u0, v0).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s1, y0, s0).color(r, g, b, a).tex(u1, v0).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s1, y0, s1).color(r, g, b, a).tex(u1, v1).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s0, y0, s1).color(r, g, b, a).tex(u0, v1).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();

		builder.pos(m, s0, y1, s1).color(r, g, b, a).tex(u0, v0).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s0, y0, s1).color(r, g, b, a).tex(u0, v1).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s1, y0, s1).color(r, g, b, a).tex(u1, v1).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s1, y1, s1).color(r, g, b, a).tex(u1, v0).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();

		builder.pos(m, s0, y1, s0).color(r, g, b, a).tex(u0, v0).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s1, y1, s0).color(r, g, b, a).tex(u1, v0).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s1, y0, s0).color(r, g, b, a).tex(u1, v1).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s0, y0, s0).color(r, g, b, a).tex(u0, v1).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();

		builder.pos(m, s0, y1, s0).color(r, g, b, a).tex(u0, v0).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s0, y0, s0).color(r, g, b, a).tex(u0, v1).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s0, y0, s1).color(r, g, b, a).tex(u1, v1).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s0, y1, s1).color(r, g, b, a).tex(u1, v0).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();

		builder.pos(m, s1, y1, s0).color(r, g, b, a).tex(u0, v0).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s1, y1, s1).color(r, g, b, a).tex(u1, v0).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s1, y0, s1).color(r, g, b, a).tex(u1, v1).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
		builder.pos(m, s1, y0, s0).color(r, g, b, a).tex(u0, v1).overlay(combinedOverlay).lightmap(combinedLight).normal(n, 0F, 1F, 0F).endVertex();
	}
}
