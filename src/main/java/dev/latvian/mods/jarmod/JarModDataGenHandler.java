package dev.latvian.mods.jarmod;

import dev.latvian.mods.jarmod.block.JarModBlocks;
import dev.latvian.mods.jarmod.item.JarModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = JarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JarModDataGenHandler {
	public static final String MODID = JarMod.MOD_ID;
	//private static Gson GSON;

	@SubscribeEvent
	public static void dataGenEvent(GatherDataEvent event) {
		/*
		GSON = new GsonBuilder()
				.registerTypeAdapter(Variant.class, new Variant.Deserializer())
				.registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer())
				.registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer())
				.create();
		 */

		DataGenerator gen = event.getGenerator();
		ExistingFileHelper efh = event.getExistingFileHelper();

		if (event.includeClient()) {
			gen.addProvider(new JMLang(gen, MODID, "en_us"));
			gen.addProvider(new JMBlockModels(gen, MODID, event.getExistingFileHelper()));
			gen.addProvider(new JMItemModels(gen, MODID, event.getExistingFileHelper()));
		}

		if (event.includeServer()) {
			JMBlockTags blockTags = new JMBlockTags(gen, MODID, efh);
			gen.addProvider(blockTags);
			gen.addProvider(new JMItemTags(gen, blockTags, MODID, efh));
			gen.addProvider(new JMRecipes(gen));
		}
	}

	private static class JMLang extends LanguageProvider {
		public JMLang(DataGenerator gen, String modid, String locale) {
			super(gen, modid, locale);
		}

		@Override
		protected void addTranslations() {
			add("itemGroup.jarmod", "Jar Mod");
			addItem(JarModItems.CAST_IRON_INGOT, "Cast Iron Ingot");
			addItem(JarModItems.CAST_IRON_NUGGET, "Cast Iron Nugget");
			addItem(JarModItems.CAST_IRON_GEAR, "Cast Iron Gear");
			addItem(JarModItems.TEMPERED_GLASS, "Tempered Glass");
			addBlock(JarModBlocks.CAST_IRON_BLOCK, "Cast Iron Block");
			addBlock(JarModBlocks.JAR, "Glass Jar");
			add("block.jarmod.jar.empty", "Empty");
			add("block.jarmod.jar.mb", "%d mB of %s");
			addBlock(JarModBlocks.TEMPERED_JAR, "Tempered Glass Jar");
			add("block.jarmod.tempered_jar.recipe_changed", "Recipe changed to %s");
			addBlock(JarModBlocks.TUBE, "Cast Iron Tube");
			addBlock(JarModBlocks.LOW_TEMPERATURE_HEAT_SINK, "Low Temperature Heat Sink");
			addBlock(JarModBlocks.HIGH_TEMPERATURE_HEAT_SINK, "High Temperature Heat Sink");
			addBlock(JarModBlocks.SUBZERO_TEMPERATURE_HEAT_SINK, "Sub-Zero Temperature Heat Sink");
			add("block.jarmod.brick_furnace.tooltip", "Place in middle of 3x3x3 cube of Bricks");
			addBlock(JarModBlocks.CREATIVE_LOW_TEMPERATURE_SOURCE, "Creative Low Temperature Source");
			addBlock(JarModBlocks.CREATIVE_HIGH_TEMPERATURE_SOURCE, "Creative High Temperature Source");
			addBlock(JarModBlocks.CREATIVE_SUBZERO_TEMPERATURE_SOURCE, "Creative Sub-Zero Temperature Source");
			add("jarmod.temperature", "Temperature");
			add("jarmod.temperature.none", "No Temperature");
			add("jarmod.temperature.low", "Low Temperature");
			add("jarmod.temperature.high", "High Temperature"); // 🔥
			add("jarmod.temperature.subzero", "Sub-Zero Temperature");
			add("jarmod.processing_time", "Processing Time: %d s");
			add("jarmod.burn_time", "Burn Time: %d min");
		}
	}

	private static class JMBlockModels extends BlockModelProvider {
		public JMBlockModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
			super(generator, modid, existingFileHelper);
		}

		@Override
		protected void registerModels() {
		}
	}

	private static class JMItemModels extends ItemModelProvider {
		public JMItemModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
			super(generator, modid, existingFileHelper);
		}

		@Override
		protected void registerModels() {
		}
	}

	private static class JMBlockTags extends BlockTagsProvider {
		public JMBlockTags(DataGenerator generatorIn, String modId, ExistingFileHelper existingFileHelper) {
			super(generatorIn, modId, existingFileHelper);
		}

		@Override
		protected void addTags() {
		}
	}

	private static class JMItemTags extends ItemTagsProvider {
		public JMItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, String modId, ExistingFileHelper existingFileHelper) {
			super(dataGenerator, blockTagProvider, modId, existingFileHelper);
		}

		@Override
		protected void addTags() {
		}
	}

	private static class JMRecipes extends RecipeProvider {
		public final Tag<Item> IRON_INGOT = ItemTags.bind("forge:ingots/iron");
		public final Tag<Item> GLASS_PANES = ItemTags.bind("forge:glass_panes");
		public final Tag<Item> CAST_IRON_INGOT = ItemTags.bind("forge:ingots/cast_iron");
		public final Tag<Item> CAST_IRON_NUGGET = ItemTags.bind("forge:nuggets/cast_iron");
		public final Tag<Item> CAST_IRON_BLOCK = ItemTags.bind("forge:storage_blocks/cast_iron");
		public final Tag<Item> CAST_IRON_GEAR = ItemTags.bind("forge:gears/cast_iron");

		public JMRecipes(DataGenerator generatorIn) {
			super(generatorIn);
		}

		@Override
		protected void buildShapelessRecipes(Consumer<FinishedRecipe> consumer) {
			// Cast Iron

			ShapedRecipeBuilder.shaped(JarModItems.CAST_IRON_BLOCK.get())
					.unlockedBy("has_item", has(CAST_IRON_INGOT))
					.group(MODID + ":cast_iron")
					.pattern("III")
					.pattern("III")
					.pattern("III")
					.define('I', CAST_IRON_INGOT)
					.save(consumer);

			ShapedRecipeBuilder.shaped(JarModItems.CAST_IRON_INGOT.get())
					.unlockedBy("has_item", has(CAST_IRON_NUGGET))
					.group(MODID + ":cast_iron")
					.pattern("III")
					.pattern("III")
					.pattern("III")
					.define('I', CAST_IRON_NUGGET)
					.save(consumer);

			ShapelessRecipeBuilder.shapeless(JarModItems.CAST_IRON_INGOT.get(), 9)
					.unlockedBy("has_item", has(CAST_IRON_BLOCK))
					.group(MODID + ":cast_iron")
					.requires(CAST_IRON_BLOCK)
					.save(consumer, new ResourceLocation(MODID, "cast_iron_ingot_from_block"));

			ShapelessRecipeBuilder.shapeless(JarModItems.CAST_IRON_NUGGET.get(), 9)
					.unlockedBy("has_item", has(CAST_IRON_INGOT))
					.group(MODID + ":cast_iron")
					.requires(CAST_IRON_INGOT)
					.save(consumer, new ResourceLocation(MODID, "cast_iron_nugget_from_ingot"));

			ShapedRecipeBuilder.shaped(JarModItems.CAST_IRON_GEAR.get())
					.unlockedBy("has_item", has(CAST_IRON_INGOT))
					.group(MODID + ":cast_iron")
					.pattern(" I ")
					.pattern("I I")
					.pattern(" I ")
					.define('I', CAST_IRON_INGOT)
					.save(consumer);

			SimpleCookingRecipeBuilder.cooking(Ingredient.of(IRON_INGOT), JarModItems.CAST_IRON_INGOT.get(), 0.1F, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE)
					.unlockedBy("has_item", has(IRON_INGOT))
					.save(consumer, new ResourceLocation(MODID, "cast_iron_ingot_from_smelting"));

			// Glass

			SimpleCookingRecipeBuilder.cooking(Ingredient.of(ItemTags.SAND), Items.GLASS_PANE, 0.1F, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_item", has(ItemTags.SAND)).save(consumer, new ResourceLocation(MODID, "glass_pane"));
			SimpleCookingRecipeBuilder.cooking(Ingredient.of(GLASS_PANES), JarModItems.TEMPERED_GLASS.get(), 0.1F, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_item", has(GLASS_PANES)).save(consumer, new ResourceLocation(MODID, "tempered_glass_from_campfire"));
			SimpleCookingRecipeBuilder.smelting(Ingredient.of(GLASS_PANES), JarModItems.TEMPERED_GLASS.get(), 0.1F, 200).unlockedBy("has_item", has(GLASS_PANES)).save(consumer, new ResourceLocation(MODID, "tempered_glass_from_smelting"));

			// Misc

			ShapedRecipeBuilder.shaped(JarModItems.LOW_TEMPERATURE_HEAT_SINK.get())
					.unlockedBy("has_item", has(IRON_INGOT))
					.pattern("PPP")
					.pattern("BBB")
					.pattern("PPP")
					.define('P', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
					.define('B', Items.IRON_BARS)
					.save(consumer);

			ShapedRecipeBuilder.shaped(JarModItems.HIGH_TEMPERATURE_HEAT_SINK.get())
					.unlockedBy("has_item", has(JarModItems.LOW_TEMPERATURE_HEAT_SINK.get()))
					.pattern("N N")
					.pattern(" H ")
					.pattern("N N")
					.define('H', JarModItems.LOW_TEMPERATURE_HEAT_SINK.get())
					.define('N', Items.NETHERITE_INGOT)
					.save(consumer);

			ShapedRecipeBuilder.shaped(JarModItems.SUBZERO_TEMPERATURE_HEAT_SINK.get())
					.unlockedBy("has_item", has(JarModItems.LOW_TEMPERATURE_HEAT_SINK.get()))
					.pattern("N N")
					.pattern(" H ")
					.pattern("N N")
					.define('H', JarModItems.LOW_TEMPERATURE_HEAT_SINK.get())
					.define('N', Items.PACKED_ICE)
					.save(consumer);

			// Jar

			ShapedRecipeBuilder.shaped(JarModItems.JAR.get())
					.unlockedBy("has_item", has(GLASS_PANES))
					.group(MODID + ":jar")
					.pattern("GCG")
					.pattern("G G")
					.pattern("GGG")
					.define('G', GLASS_PANES)
					.define('C', ItemTags.bind("minecraft:wooden_buttons"))
					.save(consumer);

			ShapedRecipeBuilder.shaped(JarModItems.TEMPERED_JAR.get())
					.unlockedBy("has_item", has(JarModItems.TEMPERED_GLASS.get()))
					.group(MODID + ":jar")
					.pattern("GCG")
					.pattern("G G")
					.pattern("GGG")
					.define('G', JarModItems.TEMPERED_GLASS.get())
					.define('C', CAST_IRON_INGOT)
					.save(consumer);

			ShapedRecipeBuilder.shaped(JarModItems.TUBE.get(), 12)
					.unlockedBy("has_item", has(CAST_IRON_INGOT))
					.group(MODID + ":jar")
					.pattern("IGI")
					.define('I', CAST_IRON_INGOT)
					.define('G', CAST_IRON_GEAR)
					.save(consumer);
		}
	}
}
