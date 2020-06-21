package dev.latvian.mods.jarmod.recipe;

import dev.latvian.mods.jarmod.JarMod;
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

	public static final RegistryObject<IRecipeSerializer<?>> HEAT_SOURCE = REGISTRY.register("heat_source", HeatSourceRecipeSerializer::new);
	public static final IRecipeType<HeatSourceRecipe> HEAT_SOURCE_TYPE = IRecipeType.register(JarMod.MOD_ID + ":heat_source");
}