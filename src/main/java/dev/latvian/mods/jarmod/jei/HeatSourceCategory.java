package dev.latvian.mods.jarmod.jei;


import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.heat.Heat;
import dev.latvian.mods.jarmod.recipe.HeatSourceRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class HeatSourceCategory implements IRecipeCategory<HeatSourceRecipe>
{
	public static final ResourceLocation UID = new ResourceLocation(JarMod.MOD_ID + ":heat_source");

	private final IDrawable background;
	private final IDrawable icon;

	public HeatSourceCategory(IGuiHelper guiHelper)
	{
		background = guiHelper.drawableBuilder(new ResourceLocation(JarMod.MOD_ID + ":textures/gui/heat_source_jei.png"), 0, 0, 112, 30).setTextureSize(128, 64).build();
		icon = guiHelper.createDrawableIngredient(Heat.ANY);
	}

	@Override
	public ResourceLocation getUid()
	{
		return UID;
	}

	@Override
	public Class<? extends HeatSourceRecipe> getRecipeClass()
	{
		return HeatSourceRecipe.class;
	}

	@Override
	public String getTitle()
	{
		return I18n.format("jarmod.heat");
	}

	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public void setIngredients(HeatSourceRecipe recipe, IIngredients ingredients)
	{
		ingredients.setOutput(JarModIngredients.HEAT, Heat.of(recipe.temperature).setBurnTime(recipe.burnTime));

		if (!recipe.item.isEmpty())
		{
			ingredients.setInput(VanillaTypes.ITEM, recipe.item);
		}

		if (!recipe.resultItem.isEmpty())
		{
			ingredients.setOutput(VanillaTypes.ITEM, recipe.resultItem);
		}
	}

	@Override
	public void setRecipe(IRecipeLayout layout, HeatSourceRecipe recipe, IIngredients ingredients)
	{
		IGuiIngredientGroup<Heat> heatStacks = layout.getIngredientsGroup(JarModIngredients.HEAT);
		IGuiItemStackGroup itemStacks = layout.getItemStacks();

		heatStacks.init(0, false, 48, 7);

		if (!recipe.item.isEmpty())
		{
			itemStacks.init(0, true, 2, 6);
		}

		if (!recipe.resultItem.isEmpty())
		{
			itemStacks.init(1, false, 92, 6);
		}

		heatStacks.set(ingredients);
		itemStacks.set(ingredients);
	}
}
