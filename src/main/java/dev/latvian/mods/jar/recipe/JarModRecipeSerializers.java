package dev.latvian.mods.jar.recipe;

import dev.latvian.mods.jar.JarMod;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class JarModRecipeSerializers
{
	public static final DeferredRegister<IRecipeSerializer<?>> REGISTRY = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, JarMod.MOD_ID);

	public static final RegistryObject<IRecipeSerializer<?>> JAR = REGISTRY.register("jar", JarRecipeSerializer::new);
	public static final IRecipeType<JarRecipe> JAR_TYPE = IRecipeType.register(JarMod.MOD_ID + ":jar");

	public static final RegistryObject<IRecipeSerializer<?>> TEMPERATURE_MODIFIER = REGISTRY.register("temperature_modifier", TemperatureModifierRecipeSerializer::new);
	public static final IRecipeType<TemperatureModifierRecipe> TEMPERATURE_MODIFIER_TYPE = IRecipeType.register(JarMod.MOD_ID + ":temperature_modifier");
}