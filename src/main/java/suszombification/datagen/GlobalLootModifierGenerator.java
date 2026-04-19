package suszombification.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import suszombification.SuspiciousZombification;
import suszombification.glm.CatMorningGiftModifier;
import suszombification.glm.NoDecomposingDropsModifier;
import suszombification.registration.SZLoot;

public class GlobalLootModifierGenerator extends GlobalLootModifierProvider {
	public GlobalLootModifierGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, SuspiciousZombification.MODID);
	}

	@Override
	protected void start() {
		add("cat_morning_gift", new CatMorningGiftModifier(new LootItemCondition[] {
				//@formatter:off
				LootTableIdCondition.builder(BuiltInLootTables.CAT_MORNING_GIFT.identifier()).build(),
				LootItemRandomChanceCondition.randomChance(0.5F).build()
				//@formatter:on
		}, 100));
		add("no_decomposing_drops", new NoDecomposingDropsModifier(new LootItemCondition[] {
				InvertedLootItemCondition.invert(LootTableIdCondition.builder(SZLoot.DEATH_BY_DECOMPOSING.identifier())).build()
		}, 100));
	}
}
