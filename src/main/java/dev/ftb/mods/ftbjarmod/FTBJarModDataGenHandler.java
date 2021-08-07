package dev.ftb.mods.ftbjarmod;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import dev.ftb.mods.ftbjarmod.block.FTBJarModBlocks;
import dev.ftb.mods.ftbjarmod.block.TemperedJarBlock;
import dev.ftb.mods.ftbjarmod.heat.Temperature;
import dev.ftb.mods.ftbjarmod.item.FTBJarModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeLootTableProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = FTBJarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FTBJarModDataGenHandler {
	public static final String MODID = FTBJarMod.MOD_ID;

	@SubscribeEvent
	public static void dataGenEvent(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		ExistingFileHelper efh = event.getExistingFileHelper();

		if (event.includeClient()) {
			gen.addProvider(new JMLang(gen, MODID, "en_us"));
			gen.addProvider(new JMBlockStates(gen, MODID, event.getExistingFileHelper()));
			gen.addProvider(new JMBlockModels(gen, MODID, event.getExistingFileHelper()));
			gen.addProvider(new JMItemModels(gen, MODID, event.getExistingFileHelper()));
		}

		if (event.includeServer()) {
			JMBlockTags blockTags = new JMBlockTags(gen, MODID, efh);
			gen.addProvider(blockTags);
			gen.addProvider(new JMItemTags(gen, blockTags, MODID, efh));
			gen.addProvider(new JMRecipes(gen));
			gen.addProvider(new JMLootTableProvider(gen));
		}
	}

	private static class JMLang extends LanguageProvider {
		public JMLang(DataGenerator gen, String modid, String locale) {
			super(gen, modid, locale);
		}

		@Override
		protected void addTranslations() {
			add("itemGroup.ftbjarmod", "FTB Jar Mod");
			addItem(FTBJarModItems.FLUID, "Fluid Container");
			addItem(FTBJarModItems.CAST_IRON_INGOT, "Cast Iron Ingot");
			addItem(FTBJarModItems.CAST_IRON_NUGGET, "Cast Iron Nugget");
			addItem(FTBJarModItems.CAST_IRON_GEAR, "Cast Iron Gear");
			addItem(FTBJarModItems.TEMPERED_GLASS, "Tempered Glass");
			addBlock(FTBJarModBlocks.CAST_IRON_BLOCK, "Cast Iron Block");
			addBlock(FTBJarModBlocks.AUTO_PROCESSING_BLOCK, "Jar Auto-Processing Block");
			addBlock(FTBJarModBlocks.JAR, "Glass Jar");
			add("block.ftbjarmod.jar.empty", "Empty");
			add("block.ftbjarmod.jar.mb", "%d mB of %s");
			addBlock(FTBJarModBlocks.TEMPERED_JAR, "Tempered Glass Jar");
			add("block.ftbjarmod.tempered_jar.recipe_changed", "Recipe changed to %s");
			addBlock(FTBJarModBlocks.TUBE, "Cast Iron Tube");
			addBlock(FTBJarModBlocks.CREATIVE_LOW_TEMPERATURE_SOURCE, "Creative Low Temperature Source");
			addBlock(FTBJarModBlocks.CREATIVE_HIGH_TEMPERATURE_SOURCE, "Creative High Temperature Source");
			addBlock(FTBJarModBlocks.CREATIVE_SUBZERO_TEMPERATURE_SOURCE, "Creative Sub-Zero Temperature Source");
			add("ftbjarmod.temperature", "Temperature");
			add("ftbjarmod.temperature.none", "No Temperature");
			add("ftbjarmod.temperature.low", "Low Temperature");
			add("ftbjarmod.temperature.high", "High Temperature"); // 🔥
			add("ftbjarmod.temperature.subzero", "Sub-Zero Temperature");
			add("ftbjarmod.processing_time", "Processing Time: %d s");
			add("ftbjarmod.burn_time", "Burn Time: %d min");
		}
	}

	private static class JMBlockStates extends BlockStateProvider {
		public JMBlockStates(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
			super(gen, modid, exFileHelper);
		}

		@Override
		protected void registerStatesAndModels() {
			simpleBlock(FTBJarModBlocks.CAST_IRON_BLOCK.get());
			//simpleBlock(FTBJarModBlocks.AUTO_PROCESSING_BLOCK.get());
			simpleBlock(FTBJarModBlocks.CREATIVE_LOW_TEMPERATURE_SOURCE.get());
			simpleBlock(FTBJarModBlocks.CREATIVE_HIGH_TEMPERATURE_SOURCE.get());
			simpleBlock(FTBJarModBlocks.CREATIVE_SUBZERO_TEMPERATURE_SOURCE.get());
			simpleBlock(FTBJarModBlocks.JAR.get(), models().getExistingFile(modLoc("block/jar")));

			getVariantBuilder(FTBJarModBlocks.TEMPERED_JAR.get()).forAllStates(state -> {
				Temperature temp = state.getValue(TemperedJarBlock.TEMPERATURE);
				return ConfiguredModel.builder().modelFile(models().withExistingParent("tempered_jar_" + temp.getSerializedName(), modLoc("block/jar_base")).texture("cover", modLoc("block/cast_iron_jar_cover")).texture("glass_side", modLoc("block/jar_glass_side_" + temp.getSerializedName())).texture("glass_top", modLoc("block/jar_glass_tempered_top")).texture("glass_bottom", modLoc("block/jar_glass_bottom_" + temp.getSerializedName()))).build();
			});

			/*
			getVariantBuilder(FTBJarModBlocks.LOW_TEMPERATURE_HEAT_SINK.get()).forAllStates(state -> {
				Direction facing = state.getValue(StairBlock.FACING);
				Half half = state.getValue(StairBlock.HALF);
				StairsShape shape = state.getValue(StairBlock.SHAPE);
				int yRot = (int)facing.getClockWise().toYRot();
				if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
					yRot += 270;
				}

				if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
					yRot += 90;
				}

				yRot %= 360;
				boolean uvlock = yRot != 0 || half == Half.TOP;
				return ConfiguredModel.builder().modelFile(state.getValue(HeatSinkBlock.ACTIVE) == StairsShape.STRAIGHT ? stairs : (shape != StairsShape.INNER_LEFT && shape != StairsShape.INNER_RIGHT ? stairsOuter : stairsInner)).rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock).build();
			}, StairBlock.WATERLOGGED);
			 */

			//dropSelf(FTBJarModBlocks.LOW_TEMPERATURE_HEAT_SINK.get());
			//dropSelf(FTBJarModBlocks.HIGH_TEMPERATURE_HEAT_SINK.get());
			//dropSelf(FTBJarModBlocks.SUBZERO_TEMPERATURE_HEAT_SINK.get());

			//dropSelf(FTBJarModBlocks.JAR.get());
			//dropSelf(FTBJarModBlocks.TEMPERED_JAR.get());
			//dropSelf(FTBJarModBlocks.TUBE.get());

			//getMultipartBuilder(FTBJarModBlocks.TUBE.get())
			//		.part()
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
			singleTexture("cast_iron_ingot", mcLoc("item/generated"), "layer0", modLoc("item/cast_iron_ingot"));
			singleTexture("cast_iron_nugget", mcLoc("item/generated"), "layer0", modLoc("item/cast_iron_nugget"));
			singleTexture("cast_iron_gear", mcLoc("item/generated"), "layer0", modLoc("item/cast_iron_gear"));
			singleTexture("tempered_glass", mcLoc("item/generated"), "layer0", modLoc("item/tempered_glass"));

			withExistingParent("cast_iron_block", modLoc("block/cast_iron_block"));
			withExistingParent("auto_processing_block", modLoc("block/auto_processing_block"));
			withExistingParent("jar", modLoc("block/jar"));
			withExistingParent("tempered_jar", modLoc("block/tempered_jar_none"));
			// withExistingParent("tube", modLoc("block/tube"));
			// withExistingParent("low_temperature_heat_sink", modLoc("block/low_temperature_heat_sink"));
			// withExistingParent("high_temperature_heat_sink", modLoc("block/high_temperature_heat_sink"));
			// withExistingParent("subzero_temperature_heat_sink", modLoc("block/subzero_temperature_heat_sink"));
			withExistingParent("creative_low_temperature_source", modLoc("block/creative_low_temperature_source"));
			withExistingParent("creative_high_temperature_source", modLoc("block/creative_high_temperature_source"));
			withExistingParent("creative_subzero_temperature_source", modLoc("block/creative_subzero_temperature_source"));
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

			ShapedRecipeBuilder.shaped(FTBJarModItems.CAST_IRON_BLOCK.get()).unlockedBy("has_item", has(CAST_IRON_INGOT)).group(MODID + ":cast_iron").pattern("III").pattern("III").pattern("III").define('I', CAST_IRON_INGOT).save(consumer);

			ShapedRecipeBuilder.shaped(FTBJarModItems.CAST_IRON_INGOT.get()).unlockedBy("has_item", has(CAST_IRON_NUGGET)).group(MODID + ":cast_iron").pattern("III").pattern("III").pattern("III").define('I', CAST_IRON_NUGGET).save(consumer);

			ShapelessRecipeBuilder.shapeless(FTBJarModItems.CAST_IRON_INGOT.get(), 9).unlockedBy("has_item", has(CAST_IRON_BLOCK)).group(MODID + ":cast_iron").requires(CAST_IRON_BLOCK).save(consumer, new ResourceLocation(MODID, "cast_iron_ingot_from_block"));

			ShapelessRecipeBuilder.shapeless(FTBJarModItems.CAST_IRON_NUGGET.get(), 9).unlockedBy("has_item", has(CAST_IRON_INGOT)).group(MODID + ":cast_iron").requires(CAST_IRON_INGOT).save(consumer, new ResourceLocation(MODID, "cast_iron_nugget_from_ingot"));

			ShapedRecipeBuilder.shaped(FTBJarModItems.CAST_IRON_GEAR.get()).unlockedBy("has_item", has(CAST_IRON_INGOT)).group(MODID + ":cast_iron").pattern(" I ").pattern("I I").pattern(" I ").define('I', CAST_IRON_INGOT).save(consumer);

			SimpleCookingRecipeBuilder.cooking(Ingredient.of(IRON_INGOT), FTBJarModItems.CAST_IRON_INGOT.get(), 0.1F, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_item", has(IRON_INGOT)).save(consumer, new ResourceLocation(MODID, "cast_iron_ingot_from_smelting"));

			// Glass

			SimpleCookingRecipeBuilder.cooking(Ingredient.of(ItemTags.SAND), Items.GLASS_PANE, 0.1F, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_item", has(ItemTags.SAND)).save(consumer, new ResourceLocation(MODID, "glass_pane"));
			SimpleCookingRecipeBuilder.cooking(Ingredient.of(GLASS_PANES), FTBJarModItems.TEMPERED_GLASS.get(), 0.1F, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE).unlockedBy("has_item", has(GLASS_PANES)).save(consumer, new ResourceLocation(MODID, "tempered_glass_from_campfire"));
			SimpleCookingRecipeBuilder.smelting(Ingredient.of(GLASS_PANES), FTBJarModItems.TEMPERED_GLASS.get(), 0.1F, 200).unlockedBy("has_item", has(GLASS_PANES)).save(consumer, new ResourceLocation(MODID, "tempered_glass_from_smelting"));

			// Misc

			ShapedRecipeBuilder.shaped(FTBJarModItems.AUTO_PROCESSING_BLOCK.get()).unlockedBy("has_item", has(CAST_IRON_INGOT)).pattern("CDC").pattern("CPC").pattern("CHC").define('C', CAST_IRON_INGOT).define('D', Items.DROPPER).define('P', Items.PISTON).define('H', Items.HOPPER).save(consumer);

			// Jar

			ShapedRecipeBuilder.shaped(FTBJarModItems.JAR.get()).unlockedBy("has_item", has(GLASS_PANES)).group(MODID + ":jar").pattern("GCG").pattern("G G").pattern("GGG").define('G', GLASS_PANES).define('C', ItemTags.bind("minecraft:wooden_buttons")).save(consumer);

			ShapedRecipeBuilder.shaped(FTBJarModItems.TEMPERED_JAR.get()).unlockedBy("has_item", has(FTBJarModItems.TEMPERED_GLASS.get())).group(MODID + ":jar").pattern("GCG").pattern("G G").pattern("GGG").define('G', FTBJarModItems.TEMPERED_GLASS.get()).define('C', FTBJarModItems.TUBE.get()).save(consumer);

			ShapedRecipeBuilder.shaped(FTBJarModItems.TUBE.get(), 12).unlockedBy("has_item", has(CAST_IRON_INGOT)).group(MODID + ":jar").pattern("IGI").define('I', CAST_IRON_INGOT).define('G', CAST_IRON_GEAR).save(consumer);
		}
	}

	private static class JMLootTableProvider extends ForgeLootTableProvider {
		private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> lootTables = Lists.newArrayList(Pair.of(JMBlockLootTableProvider::new, LootContextParamSets.BLOCK));

		public JMLootTableProvider(DataGenerator dataGeneratorIn) {
			super(dataGeneratorIn);
		}

		@Override
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
			return lootTables;
		}
	}

	public static class JMBlockLootTableProvider extends BlockLoot {
		private final Map<ResourceLocation, LootTable.Builder> tables = Maps.newHashMap();

		@Override
		protected void addTables() {
			dropSelf(FTBJarModBlocks.CAST_IRON_BLOCK.get());
			dropSelf(FTBJarModBlocks.JAR.get());
			dropSelf(FTBJarModBlocks.TEMPERED_JAR.get());
			dropSelf(FTBJarModBlocks.TUBE.get());
			dropSelf(FTBJarModBlocks.CREATIVE_LOW_TEMPERATURE_SOURCE.get());
			dropSelf(FTBJarModBlocks.CREATIVE_HIGH_TEMPERATURE_SOURCE.get());
			dropSelf(FTBJarModBlocks.CREATIVE_SUBZERO_TEMPERATURE_SOURCE.get());
			dropSelf(FTBJarModBlocks.AUTO_PROCESSING_BLOCK.get());
		}

		@Override
		public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
			addTables();

			for (ResourceLocation rs : new ArrayList<>(tables.keySet())) {
				if (rs != BuiltInLootTables.EMPTY) {
					LootTable.Builder builder = tables.remove(rs);

					if (builder == null) {
						throw new IllegalStateException(String.format("Missing loottable '%s'", rs));
					}

					consumer.accept(rs, builder);
				}
			}

			if (!tables.isEmpty()) {
				throw new IllegalStateException("Created block loot tables for non-blocks: " + tables.keySet());
			}
		}

		@Override
		protected void add(Block blockIn, LootTable.Builder table) {
			tables.put(blockIn.getLootTable(), table);
		}
	}
}
