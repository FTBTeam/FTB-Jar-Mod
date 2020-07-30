package dev.latvian.mods.jarmod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.latvian.mods.jarmod.block.JarModBlocks;
import dev.latvian.mods.jarmod.fluid.JarModFluids;
import dev.latvian.mods.jarmod.item.JarModItems;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import net.minecraft.client.renderer.model.Variant;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = JarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JarModDataGenHandler
{
	public static final String MODID = JarMod.MOD_ID;
	private static Gson GSON;

	@SubscribeEvent
	public static void dataGenEvent(GatherDataEvent event)
	{
		GSON = new GsonBuilder()
				.registerTypeAdapter(Variant.class, new Variant.Deserializer())
				.registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer())
				.registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer())
				.create();

		DataGenerator gen = event.getGenerator();

		if (event.includeClient())
		{
			gen.addProvider(new Lang(gen, MODID, "en_us"));
		}

		if (event.includeServer())
		{
			gen.addProvider(new Recipes(gen));
		}
	}

	private static class Lang extends LanguageProvider
	{
		public Lang(DataGenerator gen, String modid, String locale)
		{
			super(gen, modid, locale);
		}

		public void addFluid(Supplier<? extends Fluid> key, String name)
		{
			add(key.get().getAttributes().getTranslationKey(), name);
		}

		@Override
		protected void addTranslations()
		{
			add("itemGroup.jarmod", "Jar Mod");
			addItem(JarModItems.CAST_IRON_INGOT, "Cast Iron Ingot");
			addItem(JarModItems.CAST_IRON_NUGGET, "Cast Iron Nugget");
			addItem(JarModItems.CAST_IRON_GEAR, "Cast Iron Gear");
			addItem(JarModItems.TEMPERED_GLASS, "Tempered Glass");
			addItem(JarModItems.BIOMASS, "Solid Biomass");
			addItem(JarModItems.BIOPLASTIC, "Bioplastic");
			addItem(JarModItems.CLOTH_MESH, "Cloth Mesh");
			addItem(JarModItems.IRON_MESH, "Iron Mesh");
			addItem(JarModItems.GOLD_MESH, "Gold Mesh");
			addItem(JarModItems.DIAMOND_MESH, "Diamond Mesh");
			addItem(JarModItems.STEAM_BUCKET, "Steam Bucket");
			addItem(JarModItems.BIOMASS_BUCKET, "Biomass Bucket");
			addItem(JarModItems.BIOFUEL_BUCKET, "Biofuel Bucket");
			addItem(JarModItems.SULPHURIC_ACID_BUCKET, "Sulphuric Acid Bucket");
			addBlock(JarModBlocks.CAST_IRON_BLOCK, "Cast Iron Block");
			addBlock(JarModBlocks.JAR, "Glass Jar");
			add("block.jarmod.jar.empty", "Empty");
			add("block.jarmod.jar.mb", "%d mB of %s");
			addBlock(JarModBlocks.TEMPERED_JAR, "Tempered Glass Jar");
			add("block.jarmod.tempered_jar.recipe_changed", "Recipe changed to, %s");
			addBlock(JarModBlocks.TUBE, "Cast Iron Tube");
			addBlock(JarModBlocks.TANK_CONTROLLER, "Tank Controller");
			addBlock(JarModBlocks.TANK_WALL, "Tank Wall");
			addBlock(JarModBlocks.TANK_GLASS, "Tank Glass");
			addBlock(JarModBlocks.HEAT_SINK, "Heat Sink");
			addBlock(JarModBlocks.SLUICE, "Sluice");
			addBlock(JarModBlocks.BOILER, "Boiler");
			addBlock(JarModBlocks.BRICK_FURNACE, "Brick Furnace");
			add("block.jarmod.brick_furnace.tooltip", "Place in middle of 3x3x3 cube of Bricks");
			addBlock(JarModBlocks.ELECTRIC_HEAT_SINK, "Electric Heat Sink");
			addBlock(JarModBlocks.STONE_FACTORY, "Stone Factory");
			addBlock(JarModBlocks.CREATIVE_FE, "Creative Energy Block");
			addFluid(JarModFluids.STEAM, "Steam");
			addFluid(JarModFluids.BIOMASS, "Biomass");
			addFluid(JarModFluids.BIOFUEL, "Biofuel");
			addFluid(JarModFluids.SULPHURIC_ACID, "Sulphuric Acid");
			add("jarmod.heat", "Heat");
			add("jarmod.no_heat", "No Heat");
			add("jarmod.heat_value", "Heat, %d \uD83D\uDD25");
			add("jarmod.processing_time", "Processing Time, %d s");
			add("jarmod.burn_time", "Burn Time, %d min");
		}
	}

	private static class Recipes extends RecipeProvider
	{
		public Recipes(DataGenerator generatorIn)
		{
			super(generatorIn);
		}

		@Override
		protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
		{
			ShapedRecipeBuilder.shapedRecipe(JarModItems.GOLD_MESH.get())
					.addCriterion("has_mesh", hasItem(JarModItems.GOLD_MESH.get()))
					.patternLine("SIS")
					.patternLine("ICI")
					.patternLine("SIS")
					.key('S', ItemTags.makeWrapperTag("forge:rods/wooden"))
					.key('C', ItemTags.makeWrapperTag("forge:string"))
					.key('I', ItemTags.makeWrapperTag("forge:ingots/gold"))
					.build(consumer);
		}
	}

}
