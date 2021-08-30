package dev.ftb.mods.ftbjarmod.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fluids.FluidStack;

public class FluidButton extends Widget {
	public final FluidStack fluid;
	public final Icon icon;
	public final String amountString;

	public FluidButton(Panel p, FluidStack fs) {
		super(p);
		fluid = fs;
		icon = Icon.getIcon(fluid.getFluid().getAttributes().getStillTexture(fluid)).withTint(Color4I.rgba(fluid.getFluid().getAttributes().getColor(fluid)));
		amountString = fluid.getAmount() >= 1000000001 ? "\u221E" : (fluid.getAmount() % 1000 == 0 ? ((fluid.getAmount() / 1000) + " B") : ((fluid.getAmount() / 1000D) + " B"));
	}

	@Override
	public void addMouseOverText(TooltipList list) {
		list.add(new TranslatableComponent("block.ftbjarmod.jar.mb", fluid.getAmount(), fluid.getDisplayName()));
	}

	@Override
	public void draw(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
		GuiHelper.setupDrawing();
		icon.draw(matrixStack, x, y, w, h);
		float sw = theme.getStringWidth(amountString);
		float scale = Math.min(0.5F, w / sw);

		matrixStack.pushPose();
		matrixStack.translate(x + w - sw * scale, y + h - 8F * scale, 250);
		matrixStack.scale(scale, scale, 1F);
		theme.drawString(matrixStack, amountString, 0, 0);
		matrixStack.popPose();
	}

	@Override
	public Object getIngredientUnderMouse() {
		return fluid;
	}
}