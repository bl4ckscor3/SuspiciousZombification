package suszombification.registration;

import java.util.List;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.FoodOnAStickItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedPig;
import suszombification.item.CandyItem;
import suszombification.item.RottenEggItem;
import suszombification.item.SuspiciousPumpkinPieItem;
import suszombification.item.TrophyItem;

public class SZItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SuspiciousZombification.MODID);
	public static final DeferredItem<SuspiciousPumpkinPieItem> SUSPICIOUS_PUMPKIN_PIE = ITEMS.registerItem("suspicious_pumpkin_pie", SuspiciousPumpkinPieItem::new, new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.3F).alwaysEdible().build()));
	//candies
	public static final DeferredItem<CandyItem> CARAMEL_CANDY = ITEMS.registerItem("caramel_candy", p -> new CandyItem(MobEffects.SLOW_FALLING, 20, p));
	public static final DeferredItem<CandyItem> CHOCOLATE_CREAM_CANDY = ITEMS.registerItem("chocolate_cream_candy", p -> new CandyItem(MobEffects.MINING_FATIGUE, 20, p));
	public static final DeferredItem<CandyItem> CINNAMON_CANDY = ITEMS.registerItem("cinnamon_candy", p -> new CandyItem(MobEffects.GLOWING, 20, p));
	public static final DeferredItem<CandyItem> HONEY_CANDY = ITEMS.registerItem("honey_candy", p -> new CandyItem(MobEffects.STRENGTH, 20, p));
	public static final DeferredItem<CandyItem> MELON_CANDY = ITEMS.registerItem("melon_candy", p -> new CandyItem(MobEffects.WATER_BREATHING, 20, p));
	public static final DeferredItem<CandyItem> PEPPERMINT_CANDY = ITEMS.registerItem("peppermint_candy", p -> new CandyItem(MobEffects.LEVITATION, 20, p));
	public static final DeferredItem<CandyItem> PUMPKIN_CANDY = ITEMS.registerItem("pumpkin_candy", p -> new CandyItem(MobEffects.INVISIBILITY, 20, p));
	public static final DeferredItem<CandyItem> VANILLA_CREAM_CANDY = ITEMS.registerItem("vanilla_cream_candy", p -> new CandyItem(MobEffects.HASTE, 20, p));
	//TODO: maybe more candy flavours?
	//other items
	//@formatter:off
	public static final DeferredItem<Item> SPOILED_MILK_BUCKET = ITEMS.registerSimpleItem("spoiled_milk_bucket",
			new Item.Properties()
				.stacksTo(1)
				.craftRemainder(Items.BUCKET)
				.component(DataComponents.CONSUMABLE,
					Consumables.defaultDrink()
						.onConsume(new ApplyStatusEffectsConsumeEffect(
							List.of(
								new MobEffectInstance(MobEffects.NAUSEA, 300),
								new MobEffectInstance(MobEffects.POISON, 300, 2)))).build())
				.usingConvertsTo(Items.BUCKET));
	//@formatter:on
	public static final DeferredItem<RottenEggItem> ROTTEN_EGG = ITEMS.registerItem("rotten_egg", RottenEggItem::new, new Item.Properties().stacksTo(16));
	public static final DeferredItem<RottenEggItem> BROWN_ROTTEN_EGG = ITEMS.registerItem("brown_rotten_egg", RottenEggItem::new, new Item.Properties().stacksTo(16));
	public static final DeferredItem<RottenEggItem> BLUE_ROTTEN_EGG = ITEMS.registerItem("blue_rotten_egg", RottenEggItem::new, new Item.Properties().stacksTo(16));
	public static final DeferredItem<FoodOnAStickItem<ZombifiedPig>> PORKCHOP_ON_A_STICK = ITEMS.registerItem("porkchop_on_a_stick", p -> new FoodOnAStickItem<>(SZEntityTypes.ZOMBIFIED_PIG.get(), 7, p), new Item.Properties().durability(50));
	//spawn eggs
	public static final DeferredItem<SpawnEggItem> ZOMBIFIED_CAT_SPAWN_EGG = ITEMS.registerItem("zombified_cat_spawn_egg", p -> new SpawnEggItem(SZEntityTypes.ZOMBIFIED_CAT.get(), p));
	public static final DeferredItem<SpawnEggItem> ZOMBIFIED_CHICKEN_SPAWN_EGG = ITEMS.registerItem("zombified_chicken_spawn_egg", p -> new SpawnEggItem(SZEntityTypes.ZOMBIFIED_CHICKEN.get(), p));
	public static final DeferredItem<SpawnEggItem> ZOMBIFIED_COW_SPAWN_EGG = ITEMS.registerItem("zombified_cow_spawn_egg", p -> new SpawnEggItem(SZEntityTypes.ZOMBIFIED_COW.get(), p));
	public static final DeferredItem<SpawnEggItem> ZOMBIFIED_PIG_SPAWN_EGG = ITEMS.registerItem("zombified_pig_spawn_egg", p -> new SpawnEggItem(SZEntityTypes.ZOMBIFIED_PIG.get(), p));
	public static final DeferredItem<SpawnEggItem> ZOMBIFIED_SHEEP_SPAWN_EGG = ITEMS.registerItem("zombified_sheep_spawn_egg", p -> new SpawnEggItem(SZEntityTypes.ZOMBIFIED_SHEEP.get(), p));
	//trophies
	public static final DeferredItem<TrophyItem> CARROT_TROPHY = ITEMS.registerItem("carrot_trophy", p -> new TrophyItem(SZBlocks.CARROT_TROPHY.get(), p), new Item.Properties().stacksTo(1).useBlockDescriptionPrefix());
	public static final DeferredItem<TrophyItem> POTATO_TROPHY = ITEMS.registerItem("potato_trophy", p -> new TrophyItem(SZBlocks.POTATO_TROPHY.get(), p), new Item.Properties().stacksTo(1).useBlockDescriptionPrefix());
	public static final DeferredItem<TrophyItem> IRON_INGOT_TROPHY = ITEMS.registerItem("iron_ingot_trophy", p -> new TrophyItem(SZBlocks.IRON_INGOT_TROPHY.get(), p), new Item.Properties().stacksTo(1).useBlockDescriptionPrefix());

	private SZItems() {}
}
