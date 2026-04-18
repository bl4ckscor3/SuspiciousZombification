package suszombification;

import java.util.List;
import java.util.Set;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import suszombification.block.TrophyBlock;
import suszombification.item.SuspiciousPumpkinPieItem;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZItems;

//@formatter:off
public class SZCreativeModeTabs {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SuspiciousZombification.MODID);
	public static final DeferredHolder<CreativeModeTab,CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.icon(() -> new ItemStack(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()))
			.title(Component.translatable("itemGroup.suszombification"))
			.displayItems((displayParameters, output) -> {
				for (DeferredHolder<Block, ? extends Block> holder : SZBlocks.BLOCKS.getEntries()) {
					Block block = holder.get();

					if (!(block instanceof TrophyBlock))
						output.accept(block);
				}

				List<Item> ingredients = SuspiciousPumpkinPieItem.getAllDifferentIngredients();
				Set<ItemStack> differentPumpkinPies = ItemStackLinkedSet.createTypeAndComponentsSet();

				for (Item ingredient : ingredients) {
					differentPumpkinPies.add(SuspiciousPumpkinPieItem.createPieWithIngredient(ingredient).create());
				}

				output.acceptAll(differentPumpkinPies);

				for (DeferredHolder<Item, ? extends Item> holder : SZItems.ITEMS.getEntries()) {
					Item item = holder.get();

					if (item != SZItems.SUSPICIOUS_PUMPKIN_PIE.get())
						output.accept(item);
				}
			}).build());

	private SZCreativeModeTabs() {}
}
