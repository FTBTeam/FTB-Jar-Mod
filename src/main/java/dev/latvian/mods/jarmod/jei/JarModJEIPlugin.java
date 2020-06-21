package dev.latvian.mods.jarmod.jei;

import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.item.JarModItems;
import dev.latvian.mods.jarmod.recipe.JarModRecipeSerializers;
import dev.latvian.mods.jarmod.recipe.NoInventory;
import dev.latvian.mods.jarmod.util.Heat;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

/**
 * @author LatvianModder
 */
@JeiPlugin
public class JarModJEIPlugin implements IModPlugin
{
	public static IJeiRuntime RUNTIME;

	@Override
	public ResourceLocation getPluginUid()
	{
		return new ResourceLocation(JarMod.MOD_ID + ":jei");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime r)
	{
		RUNTIME = r;
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration)
	{
		registration.register(JarModIngredients.HEAT, Arrays.asList(Heat.NONE, Heat.ANY), new HeatHelper(), new HeatRenderer());
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration r)
	{
		r.addRecipeCatalyst(new ItemStack(JarModItems.TEMPERED_JAR.get()), TemperedJarCategory.UID);
		r.addRecipeCatalyst(new ItemStack(JarModItems.HEAT_SINK.get()), HeatSourceCategory.UID);
	}

	@Override
	public void registerRecipes(IRecipeRegistration r)
	{
		r.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipes(JarModRecipeSerializers.JAR_TYPE, NoInventory.INSTANCE, Minecraft.getInstance().world), TemperedJarCategory.UID);
		r.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipes(JarModRecipeSerializers.HEAT_SOURCE_TYPE, NoInventory.INSTANCE, Minecraft.getInstance().world), HeatSourceCategory.UID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration r)
	{
		r.addRecipeCategories(new TemperedJarCategory(r.getJeiHelpers().getGuiHelper()));
		r.addRecipeCategories(new HeatSourceCategory(r.getJeiHelpers().getGuiHelper()));
	}
}