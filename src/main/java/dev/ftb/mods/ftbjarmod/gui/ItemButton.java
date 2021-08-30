package dev.ftb.mods.ftbjarmod.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.WrappedIngredient;
import net.minecraft.world.item.ItemStack;

public class ItemButton extends Widget {
	public final String countString;
	public final ItemStack[] items;
	public final Icon[] icons;

	public ItemButton(Panel p, ItemStack[] is, int a) {
		super(p);
		countString = a >= 1000000001 ? "\u221E" : Integer.toString(a);
		items = is.length == 0 ? new ItemStack[]{ItemStack.EMPTY} : is;
		icons = new Icon[items.length];

		for (int i = 0; i < items.length; i++) {
			items[i] = items[i].copy();
			items[i].setCount(a);
			icons[i] = ItemIcon.getItemIcon(items[i]);
		}
	}

	public int getIndex() {
		if (items.length == 1) {
			return 0;
		}

		return (int) ((System.currentTimeMillis() / 1000L) % items.length);
	}

	@Override
	public void addMouseOverText(TooltipList list) {
	}

	@Override
	public void draw(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
		GuiHelper.setupDrawing();
		icons[getIndex()].draw(matrixStack, x, y, w, h);

		float sw = theme.getStringWidth(countString);
		float scale = Math.min(0.5F, w / sw);

		matrixStack.pushPose();
		matrixStack.translate(x + w - sw * scale, y + h - 8F * scale, 250);
		matrixStack.scale(scale, scale, 1F);
		theme.drawString(matrixStack, countString, 0, 0);
		matrixStack.popPose();
	}

	@Override
	public Object getIngredientUnderMouse() {
		return new WrappedIngredient(items[getIndex()]).tooltip();
	}
}
