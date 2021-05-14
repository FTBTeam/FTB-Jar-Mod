package dev.ftb.mods.ftbjarmod.client.gui;

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

	public FluidButton(Panel p, FluidStack fs) {
		super(p);
		fluid = fs;
		icon = Icon.getIcon(fluid.getFluid().getAttributes().getStillTexture(fluid)).withTint(Color4I.rgba(fluid.getFluid().getAttributes().getColor(fluid)));
	}

	@Override
	public void addMouseOverText(TooltipList list) {
		list.add(new TranslatableComponent("block.ftbjarmod.jar.mb", fluid.getAmount(), fluid.getDisplayName()));
	}

	@Override
	public void draw(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
		GuiHelper.setupDrawing();
		icon.draw(matrixStack, x, y, w, h);

		String s = Integer.toString(fluid.getAmount());
		matrixStack.pushPose();
		matrixStack.translate(x + w - theme.getStringWidth(s) / 2F, y + h - 4, 250);
		matrixStack.scale(0.5F, 0.5F, 1F);
		theme.drawString(matrixStack, s, 0, 0);
		matrixStack.popPose();
	}

	@Override
	public Object getIngredientUnderMouse() {
		return fluid;
	}
}