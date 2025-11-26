package suszombification.datagen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.chicken.ChickenVariants;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import suszombification.entity.ZombifiedSheep;
import suszombification.registration.SZEntityTypes;
import suszombification.registration.SZItems;
import suszombification.registration.SZLoot;

public record EntityLootTableGenerator(HolderLookup.Provider lookupProvider) implements LootTableSubProvider {
	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
		Map<ResourceKey<LootTable>, LootTable.Builder> lootTables = new HashMap<>();

		//@formatter:off
		//gameplay
		lootTables.put(SZLoot.DEATH_BY_DECOMPOSING, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
		lootTables.put(SZLoot.ZOMBIFIED_CHICKEN_LAY, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(AlternativesEntry.alternatives(
								LootItem.lootTableItem(SZItems.ROTTEN_EGG).when(
										LootItemEntityPropertyCondition.hasProperties(
												LootContext.EntityTarget.THIS,
												EntityPredicate.Builder.entity().components(
														DataComponentMatchers.Builder.components().exact(
																		DataComponentExactPredicate.expect(
																				DataComponents.CHICKEN_VARIANT,
																				new EitherHolder<>(lookupProvider.getOrThrow(ChickenVariants.TEMPERATE))))
																.build()))),
								LootItem.lootTableItem(SZItems.BROWN_ROTTEN_EGG).when(
										LootItemEntityPropertyCondition.hasProperties(
												LootContext.EntityTarget.THIS,
												EntityPredicate.Builder.entity().components(
														DataComponentMatchers.Builder.components().exact(
																		DataComponentExactPredicate.expect(
																				DataComponents.CHICKEN_VARIANT,
																				new EitherHolder<>(lookupProvider.getOrThrow(ChickenVariants.WARM))))
																.build()))),
								LootItem.lootTableItem(SZItems.BLUE_ROTTEN_EGG).when(
										LootItemEntityPropertyCondition.hasProperties(
												LootContext.EntityTarget.THIS,
												EntityPredicate.Builder.entity().components(
														DataComponentMatchers.Builder.components().exact(
																		DataComponentExactPredicate.expect(
																				DataComponents.CHICKEN_VARIANT,
																				new EitherHolder<>(lookupProvider.getOrThrow(ChickenVariants.COLD))))
																.build())))))));
		//entity drops
		lootTables.put(lootTableOf(SZEntityTypes.ZOMBIFIED_CAT), LootTable.lootTable().withPool(rottenFleshDrop(2.0F)));
		lootTables.put(lootTableOf(SZEntityTypes.ZOMBIFIED_CHICKEN), LootTable.lootTable().withPool(rottenFleshDrop(1.0F))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.FEATHER)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(EnchantedCountIncreaseFunction.lootingMultiplier(lookupProvider, UniformGenerator.between(0.0F, 1.0F))))));
		lootTables.put(lootTableOf(SZEntityTypes.ZOMBIFIED_COW), LootTable.lootTable().withPool(rottenFleshDrop(3.0F))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.LEATHER)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
								.apply(EnchantedCountIncreaseFunction.lootingMultiplier(lookupProvider, UniformGenerator.between(0.0F, 1.0F))))));
		//@formatter:on
		lootTables.put(lootTableOf(SZEntityTypes.ZOMBIFIED_PIG), LootTable.lootTable().withPool(rottenFleshDrop(3.0F)));
		lootTables.put(lootTableOf(SZEntityTypes.ZOMBIFIED_SHEEP), LootTable.lootTable().withPool(rottenFleshDrop(3.0F)).withPool(EntityLootSubProvider.createSheepDispatchPool(SZLoot.ZOMBIFIED_SHEEP_BY_DYE)));
		lootTables.put(SZLoot.SHEAR_ZOMBIFIED_SHEEP, LootTable.lootTable().withPool(EntityLootSubProvider.createSheepDispatchPool(SZLoot.SHEAR_ZOMBIFIED_SHEEP_BY_DYE)));
		ZombifiedSheep.ITEM_BY_DYE.forEach((dye, wool) -> {
			lootTables.put(SZLoot.SHEAR_ZOMBIFIED_SHEEP_BY_DYE.get(dye), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 3.0F)).add(LootItem.lootTableItem(wool))));
			lootTables.put(SZLoot.ZOMBIFIED_SHEEP_BY_DYE.get(dye), LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(wool))));
		});
		lootTables.forEach((path, loot) -> consumer.accept(path, loot.setParamSet(LootContextParamSets.ENTITY)));
	}

	private LootPool.Builder rottenFleshDrop(float max) {
		//@formatter:off
		return LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, max)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(lookupProvider, UniformGenerator.between(0.0F, 1.0F))));
		//@formatter:on
	}

	protected ResourceKey<LootTable> lootTableOf(Holder<EntityType<?>> entityTypeHolder) {
		EntityType<?> entityType = entityTypeHolder.value();

		return entityType.getDefaultLootTable().orElseThrow(() -> new IllegalStateException("Entity " + entityType + " has no loot table"));
	}
}
