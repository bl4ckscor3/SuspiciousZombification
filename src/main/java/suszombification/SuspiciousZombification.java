package suszombification;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import suszombification.registration.SZAttachmentTypes;
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
		SZAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
		SZBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
		SZCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
		SZDataComponents.DATA_COMPONENTS.register(modEventBus);
		SZEffects.EFFECTS.register(modEventBus);
		SZEntityTypes.ENTITY_TYPES.register(modEventBus);
		SZItems.ITEMS.register(modEventBus);
	}

	public static Identifier resLoc(String path) {
		return Identifier.fromNamespaceAndPath(MODID, path);
	}
}
