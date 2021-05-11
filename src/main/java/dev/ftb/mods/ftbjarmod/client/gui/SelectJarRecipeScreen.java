package dev.ftb.mods.ftbjarmod.client.gui;


import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import dev.ftb.mods.ftbjarmod.heat.Temperature;
import dev.ftb.mods.ftbjarmod.net.SelectJarRecipePacket;
import dev.ftb.mods.ftbjarmod.recipe.FTBJarModRecipeSerializers;
import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import dev.ftb.mods.ftbjarmod.recipe.NoInventory;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ImageIcon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetType;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.ButtonListBaseScreen;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.WrappedIngredient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author LatvianModder
 */
public class SelectJarRecipeScreen extends ButtonListBaseScreen {
	private static final Icon TEXTURE = new ImageIcon(new ResourceLocation(FTBJarMod.MOD_ID + ":textures/gui/tempered_jar_recipe.png")).withUV(0, 0, 150, 18, 256, 32);

	public static class ItemButton extends Widget {
		public final ItemStack[] items;
		public final Icon[] icons;

		public ItemButton(Panel p, ItemStack[] is, int a) {
			super(p);
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

			String s = Integer.toString(items[getIndex()].getCount());
			matrixStack.pushPose();
			matrixStack.translate(x + w - theme.getStringWidth(s) / 2F, y + h - 4, 250);
			matrixStack.scale(0.5F, 0.5F, 1F);
			theme.drawString(matrixStack, s, 0, 0);
			matrixStack.popPose();
		}

		@Override
		public Object getIngredientUnderMouse() {
			return new WrappedIngredient(items[getIndex()]).tooltip();
		}
	}

	public static class FluidButton extends Widget {
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

	public static class TemperatureButton extends Widget {
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

	public class JarRecipeButton extends Panel {
		public final JarRecipe recipe;
		public final String filterText;

		public JarRecipeButton(Panel panel, JarRecipe r) {
			super(panel);
			setSize(154, 22);
			recipe = r;

			StringBuilder stringBuilder = new StringBuilder();

			for (FluidStack stack : recipe.outputFluids) {
				stringBuilder.append(stack.getDisplayName().getString());
				stringBuilder.append(' ');
			}

			for (ItemStack stack : recipe.outputItems) {
				stringBuilder.append(stack.getHoverName().getString());
				stringBuilder.append(' ');
			}

			filterText = stringBuilder.toString().trim().toLowerCase();
		}

		@Override
		public void addWidgets() {
			add(new TemperatureButton(this, recipe.temperature, recipe.time / 20).setPosAndSize(69, 3, 16, 16));

			for (int i = 0; i < recipe.inputFluids.size(); i++) {
				add(new FluidButton(this, recipe.inputFluids.get(i)).setPosAndSize(3 + i * 20, 3, 16, 16));
			}

			for (int i = 0; i < recipe.inputItems.size(); i++) {
				add(new ItemButton(this, recipe.inputItems.get(i).ingredient.getItems(), recipe.inputItems.get(i).amount).setPosAndSize(3 + (i + recipe.inputFluids.size()) * 20, 3, 16, 16));
			}

			for (int i = 0; i < recipe.outputFluids.size(); i++) {
				add(new FluidButton(this, recipe.outputFluids.get(i)).setPosAndSize(95 + i * 20, 3, 16, 16));
			}

			for (int i = 0; i < recipe.outputItems.size(); i++) {
				add(new ItemButton(this, new ItemStack[]{recipe.outputItems.get(i)}, recipe.outputItems.get(i).getCount()).setPosAndSize(95 + (i + recipe.outputFluids.size()) * 20, 3, 16, 16));
			}
		}

		@Override
		public void alignWidgets() {
		}

		@Override
		public void drawBackground(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
			GuiHelper.setupDrawing();
			TEXTURE.draw(matrixStack, x + 2, y + 2, 150, 18);
		}

		@Override
		public void draw(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
			super.draw(matrixStack, theme, x, y, w, h);

			if (isMouseOver()) {
				matrixStack.pushPose();
				matrixStack.translate(0, 0, 300);
				Color4I.WHITE.withAlpha(100).draw(matrixStack, x, y, w, h);
				matrixStack.popPose();
			}
		}

		@Override
		public boolean mousePressed(MouseButton button) {
			if (isMouseOver()) {
				if (getWidgetType() != WidgetType.DISABLED) {
					playClickSound();
					new SelectJarRecipePacket(jar.getBlockPos(), recipe.getId()).sendToServer();
					closeGui();
				}

				return true;
			}

			return false;
		}
	}

	public final TemperedJarBlockEntity jar;

	public SelectJarRecipeScreen(TemperedJarBlockEntity j) {
		jar = j;
		//setTitle(new TranslatableComponent("block." + FTBJarMod.MOD_ID + ".tempered_jar"));
		setTitle(new TextComponent("Select Recipe"));
		setHasSearchBox(true);
	}

	@Override
	public void addButtons(Panel panel) {
		Minecraft mc = Minecraft.getInstance();
		boolean first = true;

		for (JarRecipe recipe : mc.level.getRecipeManager().getRecipesFor(FTBJarModRecipeSerializers.JAR_TYPE, NoInventory.INSTANCE, mc.level)) {
			if (recipe.isAvailableFor(mc.player)) {
				if (first) {
					first = false;
				} else {
					panel.add(new Widget(panel).setPosAndSize(0, 0, 1, 5));
				}

				panel.add(new JarRecipeButton(panel, recipe));
			}
		}
	}

	@Override
	public String getFilterText(Widget widget) {
		return ((JarRecipeButton) widget).filterText;
	}
}
