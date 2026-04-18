package suszombification.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import suszombification.item.CandyItem;
import suszombification.item.SuspiciousPumpkinPieItem;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZItems;

public class RecipeGenerator extends RecipeProvider {
	public RecipeGenerator(HolderLookup.Provider lookupProvider, RecipeOutput output) {
		super(lookupProvider, output);
	}

	@Override
	protected void buildRecipes() {
		//@formatter:off
		List<TagKey<Item>> dyes = List.of(
				Tags.Items.DYES_BLACK,
				Tags.Items.DYES_BLUE,
				Tags.Items.DYES_BROWN,
				Tags.Items.DYES_CYAN,
				Tags.Items.DYES_GRAY,
				Tags.Items.DYES_GREEN,
				Tags.Items.DYES_LIGHT_BLUE,
				Tags.Items.DYES_LIGHT_GRAY,
				Tags.Items.DYES_LIME,
				Tags.Items.DYES_MAGENTA,
				Tags.Items.DYES_ORANGE,
				Tags.Items.DYES_PINK,
				Tags.Items.DYES_PURPLE,
				Tags.Items.DYES_RED,
				Tags.Items.DYES_YELLOW,
				Tags.Items.DYES_WHITE);
		List<Item> woolBlocks = List.of(
				Items.BLACK_WOOL,
				Items.BLUE_WOOL,
				Items.BROWN_WOOL,
				Items.CYAN_WOOL,
				Items.GRAY_WOOL,
				Items.GREEN_WOOL,
				Items.LIGHT_BLUE_WOOL,
				Items.LIGHT_GRAY_WOOL,
				Items.LIME_WOOL,
				Items.MAGENTA_WOOL,
				Items.ORANGE_WOOL,
				Items.PINK_WOOL,
				Items.PURPLE_WOOL,
				Items.RED_WOOL,
				Items.YELLOW_WOOL,
				Items.WHITE_WOOL
		);
		List<Item> rottenWoolBlocks = Stream.of(
				SZBlocks.BLACK_ROTTEN_WOOL,
				SZBlocks.BLUE_ROTTEN_WOOL,
				SZBlocks.BROWN_ROTTEN_WOOL,
				SZBlocks.CYAN_ROTTEN_WOOL,
				SZBlocks.GRAY_ROTTEN_WOOL,
				SZBlocks.GREEN_ROTTEN_WOOL,
				SZBlocks.LIGHT_BLUE_ROTTEN_WOOL,
				SZBlocks.LIGHT_GRAY_ROTTEN_WOOL,
				SZBlocks.LIME_ROTTEN_WOOL,
				SZBlocks.MAGENTA_ROTTEN_WOOL,
				SZBlocks.ORANGE_ROTTEN_WOOL,
				SZBlocks.PINK_ROTTEN_WOOL,
				SZBlocks.PURPLE_ROTTEN_WOOL,
				SZBlocks.RED_ROTTEN_WOOL,
				SZBlocks.YELLOW_ROTTEN_WOOL,
				SZBlocks.WHITE_ROTTEN_WOOL
		).map(DeferredHolder::get).map(ItemLike::asItem).toList();
		//@formatter:on
		SZItems.ITEMS.getEntries().stream().map(DeferredHolder::get).filter(CandyItem.class::isInstance).map(CandyItem.class::cast).forEach(this::addSusPieRecipe);
		woolBlocks.forEach(this::addSusPieRecipe);
		rottenWoolBlocks.forEach(this::addSusPieRecipe);
		addSusPieRecipe(Items.GOLDEN_APPLE);
		addSusPieRecipe(Items.ROTTEN_FLESH);
		addSusPieRecipe(Items.CHICKEN);
		addSusPieRecipe(Items.FEATHER);
		addSusPieRecipe(Items.BEEF);
		addSusPieRecipe(Items.LEATHER);
		addSusPieRecipe(Items.PORKCHOP);
		addSusPieRecipe(Items.MUTTON);
		addSusPieRecipe(Items.STRING);
		addSusPieRecipe(Items.GUNPOWDER);
		addSusPieRecipe(Items.NAUTILUS_SHELL);
		addSusPieRecipe(Items.CACTUS);
		addSusPieRecipe(SZItems.SPOILED_MILK_BUCKET.get());
		addSusPieRecipe(SZItems.ROTTEN_EGG.get());
		addSusPieRecipe(SZItems.BROWN_ROTTEN_EGG.get());
		addSusPieRecipe(SZItems.BLUE_ROTTEN_EGG.get());
		//@formatter:off
		ShapedRecipeBuilder.shaped(items, RecipeCategory.TRANSPORTATION, SZItems.PORKCHOP_ON_A_STICK.get())
		.pattern("R ")
		.pattern(" P")
		.define('R', Items.FISHING_ROD)
		.define('P', Items.PORKCHOP)
		.unlockedBy("has_porkchop", has(Items.PORKCHOP))
		.save(output);

		for (int i = 0; i < dyes.size(); i++) {
			TagKey<Item> dye = dyes.get(i);
			Item wool = rottenWoolBlocks.get(i).asItem();

			//@formatter:off
			ShapelessRecipeBuilder.shapeless(items, RecipeCategory.BUILDING_BLOCKS, wool)
			.group("suszombification:rotten_wool")
			.requires(dye)
			.requires(Ingredient.of(rottenWoolBlocks.stream().filter(check -> !check.equals(wool))))
			.unlockedBy("has_needed_dye", has(dye))
			.save(output, "suszombification:dye_" + getItemName(wool));
   			//@formatter:on
		}
	}

	private void addSusPieRecipe(Item ingredient) {
		ItemStackTemplate result = SuspiciousPumpkinPieItem.createPieWithIngredient(ingredient);

		//@formatter:off
		ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, result)
		.group("suszombification:suspicious_pumpkin_pie")
		.requires(Items.SUGAR)
		.requires(ItemTags.EGGS)
		.requires(Items.PUMPKIN)
		.requires(ingredient)
		.unlockedBy(getHasName(ingredient), has(ingredient))
		.save(output, "suszombification:suspicious_pumpkin_pie_from_" + getItemName(ingredient));
		//@formatter:on
	}

	public static final class Runner extends RecipeProvider.Runner {
		public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(output, lookupProvider);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider lookupProvider, RecipeOutput output) {
			return new RecipeGenerator(lookupProvider, output);
		}

		@Override
		public String getName() {
			return "Suspicious Zombification recipes";
		}
	}
}
