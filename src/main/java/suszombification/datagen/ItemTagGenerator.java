package suszombification.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import suszombification.SZTags;
import suszombification.SuspiciousZombification;
import suszombification.registration.SZItems;

public class ItemTagGenerator extends ItemTagsProvider {
	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagsProvider) {
		super(output, lookupProvider, blockTagsProvider, SuspiciousZombification.MODID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		copy(SZTags.Blocks.ROTTEN_WOOL, SZTags.Items.ROTTEN_WOOL);
		tag(SZTags.Items.ROTTEN_EGGS).add(SZItems.ROTTEN_EGG.get(), SZItems.BROWN_ROTTEN_EGG.get(), SZItems.BLUE_ROTTEN_EGG.get());
	}

	@Override
	public String getName() {
		return "Item Tags: " + SuspiciousZombification.MODID;
	}
}
