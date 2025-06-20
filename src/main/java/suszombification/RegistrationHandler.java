package suszombification;

import java.util.List;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;
import net.neoforged.neoforge.registries.RegisterEvent;
import suszombification.block.TrophyBlock;
import suszombification.entity.ZombifiedAnimal;
import suszombification.glm.CatMorningGiftModifier;
import suszombification.glm.NoDecomposingDropsModifier;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZEntityTypes;
import suszombification.registration.SZItems;

@EventBusSubscriber(modid = SuspiciousZombification.MODID)
public class RegistrationHandler {
	private RegistrationHandler() {}

	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.CAT, SZEntityTypes.ZOMBIFIED_CAT.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.CHICKEN, SZEntityTypes.ZOMBIFIED_CHICKEN.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.COW, SZEntityTypes.ZOMBIFIED_COW.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.PIG, SZEntityTypes.ZOMBIFIED_PIG.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.SHEEP, SZEntityTypes.ZOMBIFIED_SHEEP.get());
			ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.put(EntityType.HORSE, EntityType.ZOMBIE_HORSE);
		});
	}

	@SubscribeEvent
	public static void registerItems(RegisterEvent event) {
		event.register(Registries.ITEM, helper -> {
			//register block items from blocks
			for (DeferredHolder<Block, ? extends Block> holder : SZBlocks.BLOCKS.getEntries()) {
				Block block = holder.get();

				if (!(block instanceof TrophyBlock))
					helper.register(holder.getId(), new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, holder.getId()))));
			}
		});
		event.register(Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, helper -> {
			helper.register(SuspiciousZombification.resLoc("cat_morning_gift"), CatMorningGiftModifier.CODEC);
			helper.register(SuspiciousZombification.resLoc("no_decomposing_drops"), NoDecomposingDropsModifier.CODEC);
		});
	}

	@SubscribeEvent
	public static void onCreativeModeTabBuildContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.COLORED_BLOCKS) {
			event.acceptAll(List.of( //@formatter:off
					new ItemStack(SZBlocks.WHITE_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.LIGHT_GRAY_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.GRAY_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.BLACK_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.BROWN_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.RED_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.ORANGE_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.YELLOW_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.LIME_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.GREEN_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.CYAN_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.LIGHT_BLUE_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.BLUE_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.PURPLE_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.MAGENTA_ROTTEN_WOOL.get()),
					new ItemStack(SZBlocks.PINK_ROTTEN_WOOL.get())));
			//@formatter:on
		}
		else if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			for (DeferredHolder<Item, ? extends Item> holder : SZItems.ITEMS.getEntries()) {
				Item item = holder.get();

				if (item instanceof SpawnEggItem)
					event.accept(item);
			}
		}
	}
}
