package suszombification.datagen;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.minecraft.DetectedVersion;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent.DataProviderFromOutputLookup;
import suszombification.SuspiciousZombification;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD)
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
		event.createProvider((DataProviderFromOutputLookup<LootTableProvider>) (output, lookupProvider) -> new LootTableProvider(output, Set.of(), List.of(
				new SubProviderEntry(BlockLootTableGenerator::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(ChestLootTableGenerator::new, LootContextParamSets.CHEST),
				new SubProviderEntry(EntityLootTableGenerator::new, LootContextParamSets.ENTITY),
				new SubProviderEntry(GiftLootTableGenerator::new, LootContextParamSets.GIFT)), lookupProvider));
		event.createProvider(output -> new PackMetadataGenerator(output)
                .add(PackMetadataSection.TYPE, new PackMetadataSection(Component.literal("Suspicious Zombification resources & data"),
                        DetectedVersion.BUILT_IN.packVersion(PackType.CLIENT_RESOURCES),
                        Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE)))));
		//@formatter:on
		event.createProvider(RecipeGenerator.Runner::new);
	}
}
