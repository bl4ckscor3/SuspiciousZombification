package suszombification.datagen;

import java.util.List;
import java.util.Set;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import suszombification.SuspiciousZombification;

@EventBusSubscriber(modid = SuspiciousZombification.MODID)
public class DataGenHandler {
	private DataGenHandler() {}

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent.Client event) {
		event.createProvider(BiomeTagGenerator::new);
		event.createProvider(DamageTypeTagGenerator::new);
		event.createProvider(EntityTypeTagGenerator::new);
		event.createProvider(GlobalLootModifierGenerator::new);
		event.createBlockAndItemTags(BlockTagGenerator::new, ItemTagGenerator::new);
		//@formatter:off
		event.createProvider((output, lookupProvider) -> new LootTableProvider(output, Set.of(), List.of(
				new SubProviderEntry(BlockLootTableGenerator::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(ChestLootTableGenerator::new, LootContextParamSets.CHEST),
				new SubProviderEntry(EntityLootTableGenerator::new, LootContextParamSets.ENTITY),
				new SubProviderEntry(GiftLootTableGenerator::new, LootContextParamSets.GIFT)), lookupProvider));
		//@formatter:on
		event.createProvider(RecipeGenerator.Runner::new);
	}
}
