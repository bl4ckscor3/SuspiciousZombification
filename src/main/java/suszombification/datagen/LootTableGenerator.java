package suszombification.datagen;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import suszombification.SZEntityTypes;
import suszombification.SuspiciousZombification;

public class LootTableGenerator implements DataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private final DataGenerator generator;
	public static final ResourceLocation ZOMBIFIED_SHEEP_WHITE = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/white");
	public static final ResourceLocation ZOMBIFIED_SHEEP_ORANGE = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/orange");
	public static final ResourceLocation ZOMBIFIED_SHEEP_MAGENTA = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/magenta");
	public static final ResourceLocation ZOMBIFIED_SHEEP_LIGHT_BLUE = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/light_blue");
	public static final ResourceLocation ZOMBIFIED_SHEEP_YELLOW = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/yellow");
	public static final ResourceLocation ZOMBIFIED_SHEEP_LIME = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/lime");
	public static final ResourceLocation ZOMBIFIED_SHEEP_PINK = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/pink");
	public static final ResourceLocation ZOMBIFIED_SHEEP_GRAY = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/gray");
	public static final ResourceLocation ZOMBIFIED_SHEEP_LIGHT_GRAY = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/light_gray");
	public static final ResourceLocation ZOMBIFIED_SHEEP_CYAN = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/cyan");
	public static final ResourceLocation ZOMBIFIED_SHEEP_PURPLE = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/purple");
	public static final ResourceLocation ZOMBIFIED_SHEEP_BLUE = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/blue");
	public static final ResourceLocation ZOMBIFIED_SHEEP_BROWN = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/brown");
	public static final ResourceLocation ZOMBIFIED_SHEEP_GREEN = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/green");
	public static final ResourceLocation ZOMBIFIED_SHEEP_RED = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/red");
	public static final ResourceLocation ZOMBIFIED_SHEEP_BLACK = new ResourceLocation(SuspiciousZombification.MODID, "entities/zombified_sheep/black");

	public LootTableGenerator(DataGenerator generator) {
		this.generator = generator;
	}

	private Map<ResourceLocation, LootTable.Builder> generateEntityLootTables() {
		Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();

		lootTables.put(SZEntityTypes.ZOMBIFIED_CHICKEN.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.FEATHER)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
		lootTables.put(SZEntityTypes.ZOMBIFIED_COW.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.LEATHER)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
		lootTables.put(SZEntityTypes.ZOMBIFIED_PIG.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
		lootTables.put(SZEntityTypes.ZOMBIFIED_SHEEP.get().getDefaultLootTable(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
								.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
		lootTables.put(ZOMBIFIED_SHEEP_BLACK, createSheepTable(Blocks.BLACK_WOOL)); //TODO: Rotten Wools
		lootTables.put(ZOMBIFIED_SHEEP_BLUE, createSheepTable(Blocks.BLUE_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_BROWN, createSheepTable(Blocks.BROWN_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_CYAN, createSheepTable(Blocks.CYAN_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_GRAY, createSheepTable(Blocks.GRAY_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_GREEN, createSheepTable(Blocks.GREEN_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_LIGHT_BLUE, createSheepTable(Blocks.LIGHT_BLUE_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_LIGHT_GRAY, createSheepTable(Blocks.LIGHT_GRAY_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_LIME, createSheepTable(Blocks.LIME_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_MAGENTA, createSheepTable(Blocks.MAGENTA_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_ORANGE, createSheepTable(Blocks.ORANGE_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_PINK, createSheepTable(Blocks.PINK_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_PURPLE, createSheepTable(Blocks.PURPLE_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_RED, createSheepTable(Blocks.RED_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_WHITE, createSheepTable(Blocks.WHITE_WOOL));
		lootTables.put(ZOMBIFIED_SHEEP_YELLOW, createSheepTable(Blocks.YELLOW_WOOL));

		return lootTables;
	}

	private static LootTable.Builder createSheepTable(ItemLike wool) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(wool)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootTableReference.lootTableReference(SZEntityTypes.ZOMBIFIED_SHEEP.get().getDefaultLootTable())));
	}

	@Override
	public void run(HashCache cache) {
		Map<ResourceLocation, LootTable> tables = new HashMap<>();

		generateEntityLootTables().forEach((path, loot) -> tables.put(path, loot.setParamSet(LootContextParamSets.ENTITY).build()));

		tables.forEach((key, lootTable) -> {
			try {
				DataProvider.save(GSON, cache, LootTables.serialize(lootTable), generator.getOutputFolder().resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json"));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public String getName() {
		return "Suspicious Zombification Loot Tables";
	}
}