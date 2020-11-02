package dev.latvian.mods.jarmod;

import dev.latvian.mods.jarmod.block.JarModBlocks;
import dev.latvian.mods.jarmod.fluid.JarModFluids;
import dev.latvian.mods.jarmod.item.JarModItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
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
	//private static Gson GSON;

	@SubscribeEvent
	public static void dataGenEvent(GatherDataEvent event)
	{
		/*
		GSON = new GsonBuilder()
				.registerTypeAdapter(Variant.class, new Variant.Deserializer())
				.registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer())
				.registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer())
				.create();
		 */

		DataGenerator gen = event.getGenerator();

		if (event.includeClient())
		{
			gen.addProvider(new JMLang(gen, MODID, "en_us"));
			gen.addProvider(new JMBlockModels(gen, MODID, event.getExistingFileHelper()));
			gen.addProvider(new JMItemModels(gen, MODID, event.getExistingFileHelper()));
		}

		if (event.includeServer())
		{
			JMBlockTags blockTags = new JMBlockTags(gen);
			gen.addProvider(blockTags);
			gen.addProvider(new JMItemTags(gen, blockTags));
			gen.addProvider(new JMRecipes(gen));
		}
	}

	private static class JMLang extends LanguageProvider
	{
		public JMLang(DataGenerator gen, String modid, String locale)
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

	private static class JMBlockModels extends BlockModelProvider
	{
		public JMBlockModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
		{
			super(generator, modid, existingFileHelper);
		}

		@Override
		protected void registerModels()
		{
		}
	}

	private static class JMItemModels extends ItemModelProvider
	{
		public JMItemModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
		{
			super(generator, modid, existingFileHelper);
		}

		@Override
		protected void registerModels()
		{
		}
	}

	private static class JMBlockTags extends BlockTagsProvider
	{
		public JMBlockTags(DataGenerator generatorIn)
		{
			super(generatorIn);
		}

		@Override
		protected void registerTags()
		{
		}
	}

	private static class JMItemTags extends ItemTagsProvider
	{
		public JMItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider)
		{
			super(dataGenerator, blockTagProvider);
		}

		@Override
		protected void registerTags()
		{
		}
	}

	private static class JMRecipes extends RecipeProvider
	{
		public final ITag<Item> IRON_INGOT = ItemTags.makeWrapperTag("forge:ingots/iron");
		public final ITag<Item> GOLD_INGOT = ItemTags.makeWrapperTag("forge:ingots/gold");
		public final ITag<Item> DIAMOND_GEM = ItemTags.makeWrapperTag("forge:gems/diamond");
		public final ITag<Item> STRING = ItemTags.makeWrapperTag("forge:string");
		public final ITag<Item> STICK = ItemTags.makeWrapperTag("forge:rods/wooden");
		public final ITag<Item> GLASS_PANES = ItemTags.makeWrapperTag("forge:glass_panes");
		public final ITag<Item> CAST_IRON_INGOT = ItemTags.makeWrapperTag("forge:ingots/cast_iron");
		public final ITag<Item> CAST_IRON_NUGGET = ItemTags.makeWrapperTag("forge:nuggets/cast_iron");
		public final ITag<Item> CAST_IRON_BLOCK = ItemTags.makeWrapperTag("forge:storage_blocks/cast_iron");
		public final ITag<Item> CAST_IRON_GEAR = ItemTags.makeWrapperTag("forge:gears/cast_iron");

		public JMRecipes(DataGenerator generatorIn)
		{
			super(generatorIn);
		}

		@Override
		protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
		{
			// Cast Iron

			ShapedRecipeBuilder.shapedRecipe(JarModItems.CAST_IRON_BLOCK.get())
					.addCriterion("has_item", hasItem(CAST_IRON_INGOT))
					.setGroup(MODID + ":cast_iron")
					.patternLine("III").patternLine("III").patternLine("III")
					.key('I', CAST_IRON_INGOT)
					.build(consumer);

			ShapedRecipeBuilder.shapedRecipe(JarModItems.CAST_IRON_INGOT.get())
					.addCriterion("has_item", hasItem(CAST_IRON_NUGGET))
					.setGroup(MODID + ":cast_iron")
					.patternLine("III").patternLine("III").patternLine("III")
					.key('I', CAST_IRON_NUGGET)
					.build(consumer);

			ShapelessRecipeBuilder.shapelessRecipe(JarModItems.CAST_IRON_INGOT.get(), 9)
					.addCriterion("has_item", hasItem(CAST_IRON_BLOCK))
					.setGroup(MODID + ":cast_iron")
					.addIngredient(CAST_IRON_BLOCK)
					.build(consumer, new ResourceLocation(MODID, "cast_iron_ingot_from_block"));

			ShapelessRecipeBuilder.shapelessRecipe(JarModItems.CAST_IRON_NUGGET.get(), 9)
					.addCriterion("has_item", hasItem(CAST_IRON_INGOT))
					.setGroup(MODID + ":cast_iron")
					.addIngredient(CAST_IRON_INGOT)
					.build(consumer, new ResourceLocation(MODID, "cast_iron_nugget_from_ingot"));

			ShapedRecipeBuilder.shapedRecipe(JarModItems.CAST_IRON_GEAR.get())
					.addCriterion("has_item", hasItem(CAST_IRON_INGOT))
					.setGroup(MODID + ":cast_iron")
					.patternLine(" I ")
					.patternLine("I I")
					.patternLine(" I ")
					.key('I', CAST_IRON_INGOT)
					.build(consumer);

			CookingRecipeBuilder.cookingRecipe(Ingredient.fromTag(IRON_INGOT), JarModItems.CAST_IRON_INGOT.get(), 0.1F, 600, IRecipeSerializer.CAMPFIRE_COOKING)
					.addCriterion("has_item", hasItem(IRON_INGOT))
					.build(consumer, new ResourceLocation(MODID, "cast_iron_ingot_from_smelting"));

			// Glass

			CookingRecipeBuilder.cookingRecipe(Ingredient.fromTag(ItemTags.SAND), Items.GLASS_PANE, 0.1F, 600, IRecipeSerializer.CAMPFIRE_COOKING).addCriterion("has_item", hasItem(ItemTags.SAND)).build(consumer);
			CookingRecipeBuilder.cookingRecipe(Ingredient.fromTag(GLASS_PANES), JarModItems.TEMPERED_GLASS.get(), 0.1F, 600, IRecipeSerializer.CAMPFIRE_COOKING).addCriterion("has_item", hasItem(GLASS_PANES)).build(consumer, new ResourceLocation(MODID, "glass_pane_from_campfire"));
			CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(GLASS_PANES), JarModItems.TEMPERED_GLASS.get(), 0.1F, 200).addCriterion("has_item", hasItem(GLASS_PANES)).build(consumer, new ResourceLocation(MODID, "glass_pane_from_smelting"));

			// Misc

			CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(JarModItems.BIOMASS.get()), JarModItems.BIOPLASTIC.get(), 0.1F, 200)
					.addCriterion("has_item", hasItem(JarModItems.BIOMASS.get()))
					.build(consumer);

			ShapedRecipeBuilder.shapedRecipe(JarModItems.HEAT_SINK.get())
					.addCriterion("has_item", hasItem(IRON_INGOT))
					.patternLine("PPP")
					.patternLine("BBB")
					.patternLine("PPP")
					.key('P', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
					.key('B', Items.IRON_BARS)
					.build(consumer);

			// Jar

			ShapedRecipeBuilder.shapedRecipe(JarModItems.JAR.get())
					.addCriterion("has_item", hasItem(GLASS_PANES))
					.setGroup(MODID + ":jar")
					.patternLine("GCG")
					.patternLine("G G")
					.patternLine("GGG")
					.key('G', GLASS_PANES)
					.key('C', ItemTags.makeWrapperTag("minecraft:wooden_buttons"))
					.build(consumer);

			ShapedRecipeBuilder.shapedRecipe(JarModItems.TEMPERED_JAR.get())
					.addCriterion("has_item", hasItem(JarModItems.TEMPERED_GLASS.get()))
					.setGroup(MODID + ":jar")
					.patternLine("GCG")
					.patternLine("G G")
					.patternLine("GGG")
					.key('G', JarModItems.TEMPERED_GLASS.get())
					.key('C', CAST_IRON_INGOT)
					.build(consumer);

			ShapedRecipeBuilder.shapedRecipe(JarModItems.TUBE.get(), 12)
					.addCriterion("has_item", hasItem(CAST_IRON_INGOT))
					.setGroup(MODID + ":jar")
					.patternLine("III")
					.patternLine("   ")
					.patternLine("III")
					.key('I', CAST_IRON_INGOT)
					.build(consumer);

			// Tank

			ShapedRecipeBuilder.shapedRecipe(JarModItems.TANK_WALL.get(), 12)
					.addCriterion("has_item", hasItem(JarModItems.TEMPERED_GLASS.get()))
					.setGroup(MODID + ":tank")
					.patternLine("CGC")
					.patternLine("GTG")
					.patternLine("CGC")
					.key('C', CAST_IRON_BLOCK)
					.key('G', JarModItems.TEMPERED_GLASS.get())
					.key('T', JarModItems.TUBE.get())
					.build(consumer);

			ShapedRecipeBuilder.shapedRecipe(JarModItems.TANK_CONTROLLER.get())
					.addCriterion("has_item", hasItem(JarModItems.TANK_WALL.get()))
					.setGroup(MODID + ":tank")
					.patternLine("WTW")
					.patternLine("TGT")
					.patternLine("WTW")
					.key('W', JarModItems.TANK_WALL.get())
					.key('T', JarModItems.TUBE.get())
					.key('G', CAST_IRON_GEAR)
					.build(consumer);

			ShapedRecipeBuilder.shapedRecipe(JarModItems.TANK_GLASS.get(), 4)
					.addCriterion("has_item", hasItem(JarModItems.TANK_WALL.get()))
					.setGroup(MODID + ":tank")
					.patternLine("WGW")
					.patternLine("G G")
					.patternLine("WGW")
					.key('W', JarModItems.TANK_WALL.get())
					.key('G', JarModItems.TEMPERED_GLASS.get())
					.build(consumer);
		}
	}

}
