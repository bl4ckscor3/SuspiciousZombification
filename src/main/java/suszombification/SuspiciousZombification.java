package suszombification;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import suszombification.registration.SZBlockEntityTypes;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZDataComponents;
import suszombification.registration.SZEffects;
import suszombification.registration.SZEntityTypes;
import suszombification.registration.SZItems;

@Mod(SuspiciousZombification.MODID)
public class SuspiciousZombification {
	public static final String MODID = "suszombification";

	public SuspiciousZombification(IEventBus modEventBus, ModContainer container) {
		container.registerConfig(ModConfig.Type.SERVER, SZConfig.SERVER_SPEC);
		SZBlocks.BLOCKS.register(modEventBus);
		SZBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
		SZCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
		SZDataComponents.DATA_COMPONENTS.register(modEventBus);
		SZEffects.EFFECTS.register(modEventBus);
		SZEntityTypes.ENTITY_TYPES.register(modEventBus);
		SZItems.ITEMS.register(modEventBus);
	}

	public static ResourceLocation resLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
}
