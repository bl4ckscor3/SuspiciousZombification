package suszombification;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class SZTags {
	public static class Blocks {
		public static final IOptionalNamedTag<Block> ROTTEN_WOOL = tag("rotten_wool");

		private static IOptionalNamedTag<Block> tag(String name) {
			return BlockTags.createOptional(new ResourceLocation(SuspiciousZombification.MODID, name));
		}
	}

	public static class Items {
		public static final IOptionalNamedTag<Item> ROTTEN_WOOL = tag("rotten_wool");

		private static IOptionalNamedTag<Item> tag(String name) {
			return ItemTags.createOptional(new ResourceLocation(SuspiciousZombification.MODID, name));
		}
	}
}