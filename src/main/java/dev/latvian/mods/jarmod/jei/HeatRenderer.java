package dev.latvian.mods.jarmod.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.latvian.mods.jarmod.heat.Heat;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
	public void render(MatrixStack matrix, int x, int y, @Nullable Heat stack)
	{
		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		Minecraft.getInstance().getTextureManager().bindTexture((stack == null ? Heat.NONE : stack).getTexture());

		Matrix4f m = matrix.getLast().getMatrix();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.pos(m, x, y + 16, 100).tex(0F, 1F).endVertex();
		bufferBuilder.pos(m, x + 16, y + 16, 100).tex(1F, 1F).endVertex();
		bufferBuilder.pos(m, x + 16, y, 100).tex(1F, 0F).endVertex();
		bufferBuilder.pos(m, x, y, 100).tex(0F, 0F).endVertex();
		tessellator.draw();

		RenderSystem.disableAlphaTest();
		RenderSystem.disableBlend();
	}

	@Override
	public List<ITextComponent> getTooltip(Heat stack, ITooltipFlag tooltipFlag)
	{
		if (stack.isNone())
		{
			return Collections.singletonList(new TranslationTextComponent("jarmod.no_heat"));
		}

		return Arrays.asList(
				new TranslationTextComponent("jarmod.heat_value", stack.getValue()),
				new TranslationTextComponent("jarmod.burn_time", stack.getBurnTime() / 1200).mergeStyle(TextFormatting.GRAY)
		);
	}
}
