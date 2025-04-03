package suszombification.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import suszombification.SZTags;
import suszombification.SuspiciousZombification;
import suszombification.registration.SZBlocks;

public class BlockTagGenerator extends BlockTagsProvider {
	public BlockTagGenerator(PackOutput output, CompletableFuture<Provider> lookupProvider) {
		super(output, lookupProvider, SuspiciousZombification.MODID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		//@formatter:off
		tag(SZTags.Blocks.ROTTEN_WOOL).add(
				SZBlocks.WHITE_ROTTEN_WOOL.get(),
				SZBlocks.ORANGE_ROTTEN_WOOL.get(),
				SZBlocks.MAGENTA_ROTTEN_WOOL.get(),
				SZBlocks.LIGHT_BLUE_ROTTEN_WOOL.get(),
				SZBlocks.YELLOW_ROTTEN_WOOL.get(),
				SZBlocks.LIME_ROTTEN_WOOL.get(),
				SZBlocks.PINK_ROTTEN_WOOL.get(),
				SZBlocks.GRAY_ROTTEN_WOOL.get(),
				SZBlocks.LIGHT_GRAY_ROTTEN_WOOL.get(),
				SZBlocks.CYAN_ROTTEN_WOOL.get(),
				SZBlocks.PURPLE_ROTTEN_WOOL.get(),
				SZBlocks.BLUE_ROTTEN_WOOL.get(),
				SZBlocks.BROWN_ROTTEN_WOOL.get(),
				SZBlocks.GREEN_ROTTEN_WOOL.get(),
				SZBlocks.RED_ROTTEN_WOOL.get(),
				SZBlocks.BLACK_ROTTEN_WOOL.get());
		tag(SZTags.Blocks.TROPHIES).add(
				SZBlocks.CARROT_TROPHY.get(),
				SZBlocks.POTATO_TROPHY.get(),
				SZBlocks.IRON_INGOT_TROPHY.get());
		tag(BlockTags.WOOL).addTag(SZTags.Blocks.ROTTEN_WOOL);
		tag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(SZTags.Blocks.TROPHIES);
		tag(BlockTags.NEEDS_IRON_TOOL).addTag(SZTags.Blocks.TROPHIES);
		//@formatter:on
	}

	@Override
	public String getName() {
		return "Block Tags: " + SuspiciousZombification.MODID;
	}
}
