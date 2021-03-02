package dev.latvian.mods.jarmod.jei;

import dev.latvian.mods.jarmod.JarMod;
import dev.latvian.mods.jarmod.heat.Temperature;
import dev.latvian.mods.jarmod.item.JarModItems;
import dev.latvian.mods.jarmod.recipe.JarModRecipeSerializers;
import dev.latvian.mods.jarmod.recipe.NoInventory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Arrays;

/**
 * @author LatvianModder
 */
@JeiPlugin
public class JarModJEIPlugin implements IModPlugin {
	public static IJeiRuntime RUNTIME;

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(JarMod.MOD_ID + ":jei");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime r) {
		RUNTIME = r;
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		registration.register(JarModIngredients.TEMPERATURE, Arrays.asList(Temperature.VALUES), new TemperatureHelper(), new TemperatureRenderer());
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration r) {
		r.addRecipeCatalyst(new ItemStack(JarModItems.TEMPERED_JAR.get()), TemperedJarCategory.UID);
		//r.addRecipeCatalyst(new ItemStack(JarModItems.HEAT_SINK.get()), TemperatureSourceCategory.UID);
	}

	@Override
	public void registerRecipes(IRecipeRegistration r) {
		Level level = Minecraft.getInstance().level;
		r.addRecipes(level.getRecipeManager().getRecipesFor(JarModRecipeSerializers.JAR_TYPE, NoInventory.INSTANCE, level), TemperedJarCategory.UID);
		r.addRecipes(level.getRecipeManager().getRecipesFor(JarModRecipeSerializers.TEMPERATURE_SOURCE_TYPE, NoInventory.INSTANCE, level), TemperatureSourceCategory.UID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration r) {
		r.addRecipeCategories(new TemperedJarCategory(r.getJeiHelpers().getGuiHelper()));
		r.addRecipeCategories(new TemperatureSourceCategory(r.getJeiHelpers().getGuiHelper()));
	}
}