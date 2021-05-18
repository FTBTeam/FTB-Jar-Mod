package dev.ftb.mods.ftbjarmod.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftbjarmod.heat.Temperature;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.network.chat.TranslatableComponent;

public class TemperatureButton extends Widget {
	public final Temperature temperature;
	public final int time;

	public TemperatureButton(Panel p, Temperature t, int ti) {
		super(p);
		temperature = t;
		time = ti;
	}

	@Override
	public void addMouseOverText(TooltipList list) {
		list.add(temperature.getName());
		list.add(new TranslatableComponent("ftbjarmod.processing_time", time));
	}

	@Override
	public void draw(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
		GuiHelper.setupDrawing();
		temperature.getIcon().draw(matrixStack, x, y, w, h);
	}

	@Override
	public Object getIngredientUnderMouse() {
		return temperature;
	}
}