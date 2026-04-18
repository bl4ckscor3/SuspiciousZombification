package suszombification.datagen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import suszombification.item.ItemStackComponent;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZDataComponents;
import suszombification.registration.SZItems;
import suszombification.registration.SZLoot;

public record ChestLootTableGenerator(HolderLookup.Provider lookupProvider) implements LootTableSubProvider {
	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
		Map<ResourceKey<LootTable>, LootTable.Builder> lootTables = new HashMap<>();

		//@formatter:off
		lootTables.put(SZLoot.PEN_BARREL, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.SUGAR)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.PUMPKIN)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.EGG)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(4.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(SZItems.SUSPICIOUS_PUMPKIN_PIE.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(SetComponentsFunction.setComponent(SZDataComponents.INGREDIENT.get(), new ItemStackComponent(new ItemStackTemplate(Items.ROTTEN_FLESH)))))));
		lootTables.put(SZLoot.RITUAL_BARREL, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.SUGAR)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.PUMPKIN)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.EGG)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.GOLDEN_APPLE)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.POTION)
								.apply(SetComponentsFunction.setComponent(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WEAKNESS)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(4.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
		lootTables.put(SZLoot.TREASURE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.IRON_INGOT)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 3.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(3.0F))
						.add(LootItem.lootTableItem(Items.CARROT)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 3.0F))))
						.add(LootItem.lootTableItem(Items.POTATO)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 3.0F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(SZBlocks.CARROT_TROPHY.get()).setWeight(40))
						.add(LootItem.lootTableItem(SZBlocks.POTATO_TROPHY.get()).setWeight(40))
						.add(LootItem.lootTableItem(SZBlocks.IRON_INGOT_TROPHY.get()).setWeight(20)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.when(LootItemRandomChanceCondition.randomChance(0.1F))
						.add(LootItem.lootTableItem(Items.IRON_SWORD)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment(lookupProvider)
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_AXE)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment(lookupProvider)
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_HOE)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment(lookupProvider)
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_SHOVEL)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment(lookupProvider)
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_PICKAXE)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment(lookupProvider)
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_BOOTS)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment(lookupProvider)
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_CHESTPLATE)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment(lookupProvider)
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_HELMET)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment(lookupProvider)
										.when(LootItemRandomChanceCondition.randomChance(0.25F))))
						.add(LootItem.lootTableItem(Items.IRON_LEGGINGS)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment(lookupProvider)
										.when(LootItemRandomChanceCondition.randomChance(0.25F)))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.when(LootItemRandomChanceCondition.randomChance(0.05F))
						.add(LootItem.lootTableItem(SZItems.CARAMEL_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.CHOCOLATE_CREAM_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.CINNAMON_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.HONEY_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.MELON_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.PEPPERMINT_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.PUMPKIN_CANDY.get()))
						.add(LootItem.lootTableItem(SZItems.VANILLA_CREAM_CANDY.get()))));
		//@formatter:on
		lootTables.forEach((path, loot) -> consumer.accept(path, loot.setParamSet(LootContextParamSets.CHEST)));
	}
}
