package dev.latvian.mods.jarmod.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import dev.latvian.mods.jarmod.heat.Temperature;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TemperatureRenderer implements IIngredientRenderer<Temperature> {
	@Override
	public void render(PoseStack matrix, int x, int y, @Nullable Temperature stack) {
		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		Minecraft.getInstance().getTextureManager().bind((stack == null ? Temperature.NONE : stack).getTexture());

		Matrix4f m = matrix.last().pose();
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuilder();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(m, x, y + 16, 100).uv(0F, 1F).endVertex();
		bufferBuilder.vertex(m, x + 16, y + 16, 100).uv(1F, 1F).endVertex();
		bufferBuilder.vertex(m, x + 16, y, 100).uv(1F, 0F).endVertex();
		bufferBuilder.vertex(m, x, y, 100).uv(0F, 0F).endVertex();
		tessellator.end();

		RenderSystem.disableAlphaTest();
		RenderSystem.disableBlend();
	}

	@Override
	public List<Component> getTooltip(Temperature temperature, TooltipFlag tooltipFlag) {
		return Collections.singletonList(temperature.getName());
	}
}
