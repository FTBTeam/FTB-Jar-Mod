package dev.ftb.mods.ftbjarmod.gui;


import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.net.SelectJarRecipePacket;
import dev.ftb.mods.ftbjarmod.recipe.FTBJarModRecipeSerializers;
import dev.ftb.mods.ftbjarmod.recipe.JarRecipe;
import dev.ftb.mods.ftbjarmod.recipe.NoInventory;
import dev.ftb.mods.ftbjarmod.temperature.Temperature;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ImageIcon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.MenuScreenWrapper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TimeUtils;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class JarScreen extends BaseScreen {
	private static final Icon TEXTURE = new ImageIcon(new ResourceLocation(FTBJarMod.MOD_ID + ":textures/gui/tempered_jar.png"));

	private static Icon sub(int x, int y, int w, int h) {
		return TEXTURE.withUV(x, y, w, h, 256, 256);
	}

	private static final Icon BACKGROUND = sub(0, 0, 205, 198);
	private static final Icon IN_FOUND = sub(249, 241, 7, 7);
	private static final Icon IN_NOT_FOUND = sub(249, 249, 7, 7);
	private static final Icon IN_PARTIAL = sub(249, 233, 7, 7);
	private static final Icon SLOT = sub(230, 238, 18, 18);
	private static final Icon START_RECIPE = sub(222, 0, 34, 18);
	private static final Icon START_RECIPE_OVER = sub(222, 19, 34, 18);
	private static final Icon STOP_RECIPE = sub(222, 38, 34, 18);
	private static final Icon STOP_RECIPE_OVER = sub(222, 57, 34, 18);
	private static final Icon VIEW_ALL_RECIPES = sub(212, 0, 9, 13);
	private static final Icon VIEW_ALL_RECIPES_OVER = sub(212, 19, 9, 13);
	private static final Icon VIEW_AVAILABLE_RECIPES = sub(212, 38, 9, 13);
	private static final Icon VIEW_AVAILABLE_RECIPES_OVER = sub(212, 57, 9, 13);
	private static final Icon RECIPE = sub(0, 218, 159, 21);

	public static AbstractContainerScreen<JarMenu> makeScreen(JarMenu m, Inventory inventory, Component component) {
		return new MenuScreenWrapper<>(new JarScreen(m), m, inventory, component);
	}

	private class ProgressWidget extends Widget {
		private double prevProgress = 0D;

		public ProgressWidget(Panel p) {
			super(p);
		}

		@Override
		public void draw(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
			if (menu.recipe != null && menu.getRecipeTime() > 0) {
				double rtime = menu.getRecipeTime();
				double mtime = menu.getMaxRecipeTime();
				double progress = Mth.clamp((mtime - rtime) / mtime, 0D, 1D);
				int w1 = (int) (w * Mth.lerp(getPartialTicks(), prevProgress, progress));
				sub(0, 240, w1, h).draw(matrixStack, x, y, w1, h);
				prevProgress = progress;
			} else {
				prevProgress = 0D;
			}
		}

		@Override
		public void addMouseOverText(TooltipList list) {
			if (menu.recipe != null && menu.getRecipeTime() > 0) {
				list.string(TimeUtils.prettyTimeString(Mth.ceil(menu.getRecipeTime() / 20D)));
			}
		}

		@Override
		public void tick() {
			super.tick();
		}
	}

	private class IngredientWidget extends Widget {
		private final int index;

		public IngredientWidget(Panel panel, int i) {
			super(panel);
			index = i;
		}

		@Override
		public void draw(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
			if (menu.recipe == null) {
				return;
			}

			matrixStack.pushPose();
			matrixStack.translate(0, 0, 300);

			int a = menu.getAvailableResource(index);

			if (a == 0) {
				IN_NOT_FOUND.draw(matrixStack, x, y, w, h);
			} else if (a != -1 && a < menu.recipe.inputAmounts[index]) {
				IN_PARTIAL.draw(matrixStack, x, y, w, h);
			} else if (a != -1) {
				IN_FOUND.draw(matrixStack, x, y, w, h);
			}

			matrixStack.popPose();
		}

		@Override
		public void addMouseOverText(TooltipList list) {
			int a = menu.getAvailableResource(index);
			int m = menu.recipe.inputAmounts[index];

			if (a != -1 && a < m) {
				list.string(a + " / " + m);
			}
		}
	}

	private class StartButton extends Button {
		public StartButton(Panel p) {
			super(p);
		}

		@Override
		public void draw(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
			if (menu.recipe != null) {
				if (isMouseOver()) {
					(menu.getRecipeTime() > 0 ? STOP_RECIPE_OVER : START_RECIPE_OVER).draw(matrixStack, x, y, w, h);
				} else {
					(menu.getRecipeTime() > 0 ? STOP_RECIPE : START_RECIPE).draw(matrixStack, x, y, w, h);
				}
			}
		}

		@Override
		public void onClicked(MouseButton mouseButton) {
			playClickSound();
			Minecraft.getInstance().gameMode.handleInventoryButtonClick(menu.containerId, 0);
		}
	}

	private class RightTempWidget extends Widget {
		public RightTempWidget(Panel panel) {
			super(panel);
		}

		@Override
		public void draw(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
			matrixStack.pushPose();
			matrixStack.translate(0, 0, 300);

			if (menu.recipe == null) {
				// IN_UNKNOWN.draw(matrixStack, x, y, w, h);
			} else if (menu.isRightTemperature()) {
				IN_FOUND.draw(matrixStack, x, y, w, h);
			} else {
				IN_NOT_FOUND.draw(matrixStack, x, y, w, h);
			}

			matrixStack.popPose();
		}
	}

	private class ChangeRecipeButton extends Panel {
		public final JarRecipe recipe;
		public final boolean available;

		public ChangeRecipeButton(Panel panel, JarRecipe r, boolean a) {
			super(panel);
			setSize(159, 21);
			recipe = r;
			available = a;
		}

		@Override
		public void addWidgets() {
			for (int i = 0; i < recipe.inputObjects.length; i++) {
				Widget w;

				if (recipe.inputObjects[i] instanceof Ingredient) {
					w = new ItemButton(this, ((Ingredient) recipe.inputObjects[i]).getItems(), recipe.inputAmounts[i]);
				} else {
					FluidStack fs = ((FluidStack) recipe.inputObjects[i]).copy();
					fs.setAmount(recipe.inputAmounts[i]);
					w = new FluidButton(this, fs);
				}

				add(w.setPosAndSize(2 + i * 20, 2, 16, 16));
			}

			add(new TemperatureButton(this, recipe.temperature, menu.jar.getTemperature().getRecipeTime(recipe) / 20).setPosAndSize(72, 2, 16, 16));

			for (int i = 0; i < recipe.outputFluids.size(); i++) {
				add(new FluidButton(this, recipe.outputFluids.get(i)).setPosAndSize(101 + i * 20, 2, 16, 16));
			}

			for (int i = 0; i < recipe.outputItems.size(); i++) {
				add(new ItemButton(this, new ItemStack[]{recipe.outputItems.get(i)}, recipe.outputItems.get(i).getCount()).setPosAndSize(101 + (i + recipe.outputFluids.size()) * 20, 2, 16, 16));
			}
		}

		@Override
		public void alignWidgets() {
		}

		@Override
		public void drawBackground(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
			GuiHelper.setupDrawing();
			RECIPE.draw(matrixStack, x, y, w, h);

			if (recipe == menu.recipe) {
				Color4I.LIGHT_GREEN.withAlpha(80).draw(matrixStack, x, y, w, h);
			}
		}

		@Override
		public void draw(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
			super.draw(matrixStack, theme, x, y, w, h);

			if (!available) {
				matrixStack.pushPose();
				matrixStack.translate(0, 0, 300);
				Color4I.BLACK.withAlpha(100).draw(matrixStack, x, y, w, h);
				matrixStack.popPose();
			} else if (isMouseOver()) {
				matrixStack.pushPose();
				matrixStack.translate(0, 0, 300);
				Color4I.WHITE.withAlpha(100).draw(matrixStack, x, y, w, h);
				matrixStack.popPose();
			}
		}

		@Override
		public boolean mousePressed(MouseButton button) {
			if (isMouseOver()) {
				if (available && button.isLeft() && menu.recipe != recipe) {
					playClickSound();

					if (menu.hasAnyResources()) {
						Minecraft.getInstance().setScreen(new ConfirmScreen(t -> {
							if (t) {
								menu.jar.recipe = recipe.getId();
								menu.recipe = recipe;
								JarScreen.this.refreshWidgets();
								new SelectJarRecipePacket(menu.jar.getBlockPos(), recipe.getId()).sendToServer();
							}

							openGui();
						}, new TextComponent("Change recipe?"), new TextComponent("You may lose stored resources")));
					} else {
						menu.jar.recipe = recipe.getId();
						menu.recipe = recipe;
						JarScreen.this.refreshWidgets();
						new SelectJarRecipePacket(menu.jar.getBlockPos(), recipe.getId()).sendToServer();
					}
				}

				return true;
			}

			return false;
		}
	}

	public final JarMenu menu;
	public final Panel recipePanel;
	public final Panel resourcePanel;
	public final TextBox searchBox;

	public JarScreen(JarMenu m) {
		menu = m;
		setSize(205, 198);

		recipePanel = new Panel(this) {
			@Override
			public void addWidgets() {
				Temperature t = menu.jar.getTemperature().temperature;
				String search = searchBox.getText().trim().toLowerCase();

				List<ChangeRecipeButton> availableList = new ArrayList<>();
				List<ChangeRecipeButton> unavailableList = new ArrayList<>();

				Minecraft.getInstance().level.getRecipeManager()
						.getRecipesFor(FTBJarModRecipeSerializers.JAR_TYPE, NoInventory.INSTANCE, Minecraft.getInstance().level)
						.stream()
						.sorted(JarRecipe.COMPARATOR)
						.forEach(recipe -> {
							if (search.isEmpty() || recipe.getFilterText().contains(search)) {
								boolean available = recipe.temperature == t && recipe.isAvailableFor(Minecraft.getInstance().player);

								if (available && recipe != menu.recipe && notAvailable(recipe)) {
									available = false;
								}

								(available ? availableList : unavailableList).add(new ChangeRecipeButton(this, recipe, available));
							}
						});

				addAll(availableList);
				addAll(unavailableList);
			}

			@Override
			public void alignWidgets() {
				align(WidgetLayout.VERTICAL);
			}
		};

		recipePanel.setPosAndSize(37, 88, 159, 102);

		resourcePanel = new Panel(this) {
			@Override
			public void addWidgets() {
				for (FluidStack stack : menu.availableFluids) {
					add(new FluidButton(this, stack).setPosAndSize(0, 0, 16, 16));
				}

				for (ItemStack stack : menu.availableItems) {
					add(new ItemButton(this, new ItemStack[]{stack}, stack.getCount()).setPosAndSize(0, 0, 16, 16));
				}
			}

			@Override
			public void alignWidgets() {
				align(new WidgetLayout.Vertical(0, 1, 0));
			}
		};

		resourcePanel.setPosAndSize(6, 14, 16, 168);

		searchBox = new TextBox(this) {
			@Override
			public void onTextChanged() {
				recipePanel.setScrollY(0);
				recipePanel.refreshWidgets();
			}
		};

		searchBox.ghostText = I18n.get("gui.search_box");
		searchBox.setPosAndSize(36, 70, 161, 13);

	}

	private boolean notAvailable(JarRecipe recipe) {
		for (int i = 0; i < recipe.inputObjects.length; i++) {
			int found = 0;

			if (recipe.inputObjects[i] instanceof FluidStack) {
				FluidStack in = (FluidStack) recipe.inputObjects[i];

				for (FluidStack fs : menu.availableFluids) {
					if (fs.isFluidEqual(in)) {
						found += fs.getAmount();
					}
				}
			} else if (recipe.inputObjects[i] instanceof Ingredient) {
				Ingredient in = (Ingredient) recipe.inputObjects[i];

				for (ItemStack is : menu.availableItems) {
					if (in.test(is)) {
						found += is.getCount();
					}
				}
			}

			if (found < recipe.inputAmounts[i]) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void addWidgets() {
		add(new ProgressWidget(this).setPosAndSize(37, 36, 121, 16));

		// new SelectJarRecipePacket(menu.jar.getBlockPos(), recipe.getId()).sendToServer();

		if (menu.recipe != null) {
			for (int i = 0; i < menu.recipe.inputFluids.size(); i++) {
				add(new FluidButton(this, menu.recipe.inputFluids.get(i)).setPosAndSize(37 + i * 22, 14, 16, 16));
			}

			for (int i = 0; i < menu.recipe.inputItems.size(); i++) {
				add(new ItemButton(this, menu.recipe.inputItems.get(i).ingredient.getItems(), menu.recipe.inputItems.get(i).amount).setPosAndSize(37 + (i + menu.recipe.inputFluids.size()) * 22, 14, 16, 16));
			}

			add(new TemperatureButton(this, menu.recipe.temperature, menu.jar.getTemperature().getRecipeTime(menu.recipe) / 20).setPosAndSize(109, 14, 16, 16));

			for (int i = 0; i < menu.recipe.outputFluids.size(); i++) {
				add(new FluidButton(this, menu.recipe.outputFluids.get(i)).setPosAndSize(136 + i * 22, 14, 16, 16));
			}

			for (int i = 0; i < menu.recipe.outputItems.size(); i++) {
				add(new ItemButton(this, new ItemStack[]{menu.recipe.outputItems.get(i)}, menu.recipe.outputItems.get(i).getCount()).setPosAndSize(136 + (i + menu.recipe.outputFluids.size()) * 22, 14, 16, 16));
			}

			for (int i = 0; i < menu.recipe.inputItems.size() + menu.recipe.inputFluids.size(); i++) {
				add(new IngredientWidget(this, i).setPosAndSize(50 + i * 22, 10, 7, 7));
			}

			add(new RightTempWidget(this).setPosAndSize(122, 10, 7, 7));

			add(new StartButton(this).setPosAndSize(163, 35, 34, 18));
		}

		add(recipePanel);
		add(resourcePanel);
		add(searchBox);
	}

	@Override
	public void drawBackground(PoseStack matrixStack, Theme theme, int x, int y, int w, int h) {
		BACKGROUND.draw(matrixStack, x, y, w, h);
	}
}