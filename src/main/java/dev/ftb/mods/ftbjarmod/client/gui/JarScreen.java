package dev.ftb.mods.ftbjarmod.client.gui;


import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.block.entity.TemperedJarBlockEntity;
import dev.ftb.mods.ftbjarmod.net.SelectJarRecipePacket;
import dev.ftb.mods.ftbjarmod.net.StartJarPacket;
import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ImageIcon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TimeUtils;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.function.BooleanSupplier;

/**
 * @author LatvianModder
 */
public class JarScreen extends BaseScreen {
	private static final Icon TEXTURE = new ImageIcon(new ResourceLocation(FTBJarMod.MOD_ID + ":textures/gui/tempered_jar.png"));

	private static Icon sub(int x, int y, int w, int h) {
		return TEXTURE.withUV(x, y, w, h, 256, 256);
	}

	private static final Icon BACKGROUND = sub(0, 0, 172, 54);
	private static final Icon IN_FOUND = sub(249, 0, 7, 7);
	private static final Icon IN_NOT_FOUND = sub(249, 8, 7, 7);
	private static final Icon CHANGE_RECIPE = sub(0, 55, 34, 18);
	private static final Icon CHANGE_RECIPE_OVER = sub(0, 74, 34, 18);
	private static final Icon START_RECIPE = sub(35, 55, 34, 18);
	private static final Icon START_RECIPE_OVER = sub(35, 74, 34, 18);
	private static final Icon STOP_RECIPE = sub(70, 55, 34, 18);
	private static final Icon STOP_RECIPE_OVER = sub(70, 74, 34, 18);

	private class OpenRecipeButton extends Button {
		public OpenRecipeButton(Panel panel) {
			super(panel, TextComponent.EMPTY, Icon.EMPTY);
		}

		@Override
		public void onClicked(MouseButton mouseButton) {
			if (jar.recipeTime == 0) {
				new SelectJarRecipeScreen(jar).openGui();
			}
		}

		@Override
		public void addMouseOverText(TooltipList list) {
			if (jar.recipeTime == 0) {
				list.add(new TextComponent("Change Recipe"));
			}
		}

		@Override
		public void drawBackground(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
			if (jar.recipeTime == 0) {
				(isMouseOver() ? CHANGE_RECIPE_OVER : CHANGE_RECIPE).draw(matrixStack, x, y, w, h);
			}
		}
	}

	private class StartRecipeButton extends Button {
		public StartRecipeButton(Panel panel) {
			super(panel, TextComponent.EMPTY, Icon.EMPTY);
		}

		@Override
		public void onClicked(MouseButton mouseButton) {
			if (rightTemperature && TemperedJarBlockEntity.canStart(ingredientsFound)) {
				if (jar.recipeTime > 0) {
					new SelectJarRecipePacket(jar.getBlockPos(), recipe.getId()).sendToServer();
				} else {
					new StartJarPacket(jar.getBlockPos()).sendToServer();
				}
			}
		}

		@Override
		public void drawBackground(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
			if (!rightTemperature || !TemperedJarBlockEntity.canStart(ingredientsFound)) {
				return;
			}

			if (jar.recipeTime > 0) {
				(isMouseOver() ? STOP_RECIPE_OVER : STOP_RECIPE).draw(matrixStack, x, y, w, h);
			} else {
				(isMouseOver() ? START_RECIPE_OVER : START_RECIPE).draw(matrixStack, x, y, w, h);
			}
		}
	}

	private class ProgressWidget extends Widget {
		public ProgressWidget(Panel p) {
			super(p);
		}

		@Override
		public void draw(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
			if (recipe != null && jar.recipeTime > 0) {
				double progress = Mth.clamp((recipe.time - jar.recipeTime) / (double) recipe.time, 0D, 1D);
				int w1 = (int) (w * progress);
				sub(0, 93, w1, h).draw(matrixStack, x, y, w1, h);
			}
		}

		@Override
		public void addMouseOverText(TooltipList list) {
			if (recipe != null && jar.recipeTime > 0) {
				list.string(TimeUtils.prettyTimeString(Mth.ceil(jar.recipeTime / 20D)));
			}
		}
	}

	private static class IngredientButton extends Widget {
		private final BooleanSupplier found;

		public IngredientButton(Panel panel, BooleanSupplier b) {
			super(panel);
			found = b;
		}

		@Override
		public void addMouseOverText(TooltipList list) {
			if (!found.getAsBoolean()) {
				list.add(new TextComponent("Ingredient not found!"));
			}
		}

		@Override
		public void draw(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
			matrixStack.pushPose();
			matrixStack.translate(0, 0, 300);
			(found.getAsBoolean() ? IN_FOUND : IN_NOT_FOUND).draw(matrixStack, x, y, w, h);
			matrixStack.popPose();
		}
	}

	public final TemperedJarBlockEntity jar;
	public JarRecipe recipe;
	public boolean[] ingredientsFound;
	public boolean rightTemperature;

	public JarScreen(TemperedJarBlockEntity j, boolean[] in) {
		setSize(172, 54);
		jar = j;
		updateRecipe(in);
	}

	public void updateRecipe(boolean[] in) {
		recipe = jar.getRecipe();
		ingredientsFound = in;
		rightTemperature = recipe != null && jar.isTemperature(recipe.temperature);
	}

	@Override
	public void addWidgets() {
		add(new OpenRecipeButton(this).setPosAndSize(131, 29, 34, 18));
		add(new StartRecipeButton(this).setPosAndSize(93, 29, 34, 18));
		add(new ProgressWidget(this).setPosAndSize(8, 30, 80, 16));

		if (recipe != null) {
			add(new TemperatureButton(this, recipe.temperature, recipe.time / 20).setPosAndSize(78, 8, 16, 16));

			for (int i = 0; i < recipe.inputFluids.size(); i++) {
				add(new FluidButton(this, recipe.inputFluids.get(i)).setPosAndSize(8 + i * 22, 8, 16, 16));
			}

			for (int i = 0; i < recipe.inputItems.size(); i++) {
				add(new ItemButton(this, recipe.inputItems.get(i).ingredient.getItems(), recipe.inputItems.get(i).amount).setPosAndSize(8 + (i + recipe.inputFluids.size()) * 22, 8, 16, 16));
			}

			for (int i = 0; i < recipe.outputFluids.size(); i++) {
				add(new FluidButton(this, recipe.outputFluids.get(i)).setPosAndSize(104 + i * 22, 8, 16, 16));
			}

			for (int i = 0; i < recipe.outputItems.size(); i++) {
				add(new ItemButton(this, new ItemStack[]{recipe.outputItems.get(i)}, recipe.outputItems.get(i).getCount()).setPosAndSize(104 + (i + recipe.outputFluids.size()) * 22, 8, 16, 16));
			}

			add(new IngredientButton(this, () -> rightTemperature).setPosAndSize(90, 5, 7, 7));

			for (int i = 0; i < ingredientsFound.length; i++) {
				final int j = i;
				add(new IngredientButton(this, () -> j < ingredientsFound.length && ingredientsFound[j]).setPosAndSize(20 + j * 22, 5, 7, 7));
			}
		}
	}

	@Override
	public void drawBackground(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
		BACKGROUND.draw(matrixStack, x, y, w, h);
	}
}
