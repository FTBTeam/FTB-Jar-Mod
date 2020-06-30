package dev.latvian.mods.jarmod.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.latvian.mods.jarmod.util.Heat;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class HeatRenderer implements IIngredientRenderer<Heat>
{
	@Override
	public void render(int x, int y, @Nullable Heat stack)
	{
		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		Minecraft.getInstance().getTextureManager().bindTexture((stack == null ? Heat.NONE : stack).getTexture());

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.pos(x, y + 16, 100).tex(0F, 1F).endVertex();
		bufferBuilder.pos(x + 16, y + 16, 100).tex(1F, 1F).endVertex();
		bufferBuilder.pos(x + 16, y, 100).tex(1F, 0F).endVertex();
		bufferBuilder.pos(x, y, 100).tex(0F, 0F).endVertex();
		tessellator.draw();

		RenderSystem.disableAlphaTest();
		RenderSystem.disableBlend();
	}

	@Override
	public List<String> getTooltip(Heat stack, ITooltipFlag tooltipFlag)
	{
		if (stack.isNone())
		{
			return Collections.singletonList(I18n.format("jarmod.no_heat"));
		}

		return Arrays.asList(
				I18n.format("jarmod.heat"),
				TextFormatting.GRAY + I18n.format("jarmod.temperature", stack.getTemperature()),
				TextFormatting.GRAY + I18n.format("jarmod.burn_time", stack.getBurnTime() / 1200)
		);
	}
}
