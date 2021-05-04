package dev.ftb.mods.ftbjarmod.jei;


import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.heat.Temperature;
import dev.ftb.mods.ftbjarmod.recipe.TemperatureSourceRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;

/**
 * @author LatvianModder
 */
public class TemperatureSourceCategory implements IRecipeCategory<TemperatureSourceRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(FTBJarMod.MOD_ID + ":temperature_source");

	private final IDrawable background;
	private final IDrawable icon;

	public TemperatureSourceCategory(IGuiHelper guiHelper) {
		background = guiHelper.drawableBuilder(new ResourceLocation(FTBJarMod.MOD_ID + ":textures/gui/temperature_source_jei.png"), 0, 0, 112, 30).setTextureSize(128, 64).build();
		icon = guiHelper.createDrawableIngredient(Temperature.LOW);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends TemperatureSourceRecipe> getRecipeClass() {
		return TemperatureSourceRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.get(FTBJarMod.MOD_ID + ".temperature");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(TemperatureSourceRecipe recipe, IIngredients ingredients) {
		ingredients.setOutput(FTBJarModIngredients.TEMPERATURE, recipe.temperature);//.setBurnTime(recipe.burnTime));

		if (!recipe.item.isEmpty()) {
			ingredients.setInput(VanillaTypes.ITEM, recipe.item);
		}

		if (!recipe.resultItem.isEmpty()) {
			ingredients.setOutput(VanillaTypes.ITEM, recipe.resultItem);
		}
	}

	@Override
	public void setRecipe(IRecipeLayout layout, TemperatureSourceRecipe recipe, IIngredients ingredients) {
		IGuiIngredientGroup<Temperature> tStacks = layout.getIngredientsGroup(FTBJarModIngredients.TEMPERATURE);
		IGuiItemStackGroup itemStacks = layout.getItemStacks();

		tStacks.init(0, false, 48, 7);

		if (!recipe.item.isEmpty()) {
			itemStacks.init(0, true, 2, 6);
		}

		if (!recipe.resultItem.isEmpty()) {
			itemStacks.init(1, false, 92, 6);
		}

		tStacks.set(ingredients);
		itemStacks.set(ingredients);
	}
}
