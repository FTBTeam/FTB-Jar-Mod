package dev.ftb.mods.ftbjarmod.jei;

import dev.ftb.mods.ftbjarmod.FTBJarMod;
import dev.ftb.mods.ftbjarmod.item.FTBJarModItems;
import dev.ftb.mods.ftbjarmod.item.FluidItem;
import dev.ftb.mods.ftbjarmod.recipe.FTBJarModRecipeSerializers;
import dev.ftb.mods.ftbjarmod.recipe.NoInventory;
import dev.ftb.mods.ftbjarmod.temperature.Temperature;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author LatvianModder
 */
@JeiPlugin
public class FTBJarModJEIPlugin implements IModPlugin {
	public static IJeiRuntime RUNTIME;

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(FTBJarMod.MOD_ID + ":jei");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime r) {
		RUNTIME = r;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(FTBJarModItems.FLUID.get(), FluidItem::getFluidStackHash);
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		registration.register(FTBJarModIngredients.TEMPERATURE, Arrays.asList(Temperature.VALUES), new TemperatureHelper(), new TemperatureRenderer());
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration r) {
		r.addRecipeCatalyst(new ItemStack(FTBJarModItems.TEMPERED_JAR.get()), TemperedJarCategory.UID);
		//r.addRecipeCatalyst(new ItemStack(JarModItems.HEAT_SINK.get()), TemperatureSourceCategory.UID);
	}

	@Override
	public void registerRecipes(IRecipeRegistration r) {
		Level level = Minecraft.getInstance().level;
		r.addRecipes(level.getRecipeManager().getRecipesFor(FTBJarModRecipeSerializers.JAR_TYPE, NoInventory.INSTANCE, level), TemperedJarCategory.UID);
		r.addRecipes(level.getRecipeManager().getRecipesFor(FTBJarModRecipeSerializers.TEMPERATURE_SOURCE_TYPE, NoInventory.INSTANCE, level).stream().filter(re -> !re.hideFromJEI).collect(Collectors.toList()), TemperatureSourceCategory.UID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration r) {
		r.addRecipeCategories(new TemperedJarCategory(r.getJeiHelpers().getGuiHelper()));
		r.addRecipeCategories(new TemperatureSourceCategory(r.getJeiHelpers().getGuiHelper()));
	}
}