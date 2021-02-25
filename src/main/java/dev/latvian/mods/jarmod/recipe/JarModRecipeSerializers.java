package dev.latvian.mods.jarmod.recipe;

import dev.latvian.mods.jarmod.JarMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class JarModRecipeSerializers {
	public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, JarMod.MOD_ID);

	public static final RegistryObject<RecipeSerializer<?>> JAR = REGISTRY.register("jar", JarRecipeSerializer::new);
	public static final RecipeType<JarRecipe> JAR_TYPE = RecipeType.register(JarMod.MOD_ID + ":jar");

	public static final RegistryObject<RecipeSerializer<?>> TEMPERATURE_SOURCE = REGISTRY.register("temperature_source", TemperatureSourceRecipeSerializer::new);
	public static final RecipeType<TemperatureSourceRecipe> TEMPERATURE_SOURCE_TYPE = RecipeType.register(JarMod.MOD_ID + ":temperature_source");
}