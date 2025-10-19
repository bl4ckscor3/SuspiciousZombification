package suszombification.registration;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import suszombification.SuspiciousZombification;
import suszombification.block.RottenWoolBlock;
import suszombification.block.TrophyBlock;
import suszombification.block.TrophyBlock.TrophyType;

public class SZBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SuspiciousZombification.MODID);
	public static final DeferredBlock<RottenWoolBlock> WHITE_ROTTEN_WOOL = BLOCKS.registerBlock("white_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.SNOW));
	public static final DeferredBlock<RottenWoolBlock> LIGHT_GRAY_ROTTEN_WOOL = BLOCKS.registerBlock("light_gray_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_LIGHT_GRAY));
	public static final DeferredBlock<RottenWoolBlock> GRAY_ROTTEN_WOOL = BLOCKS.registerBlock("gray_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_GRAY));
	public static final DeferredBlock<RottenWoolBlock> BLACK_ROTTEN_WOOL = BLOCKS.registerBlock("black_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_BLACK));
	public static final DeferredBlock<RottenWoolBlock> BROWN_ROTTEN_WOOL = BLOCKS.registerBlock("brown_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_BROWN));
	public static final DeferredBlock<RottenWoolBlock> RED_ROTTEN_WOOL = BLOCKS.registerBlock("red_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_RED));
	public static final DeferredBlock<RottenWoolBlock> ORANGE_ROTTEN_WOOL = BLOCKS.registerBlock("orange_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_ORANGE));
	public static final DeferredBlock<RottenWoolBlock> YELLOW_ROTTEN_WOOL = BLOCKS.registerBlock("yellow_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_YELLOW));
	public static final DeferredBlock<RottenWoolBlock> LIME_ROTTEN_WOOL = BLOCKS.registerBlock("lime_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_LIGHT_GREEN));
	public static final DeferredBlock<RottenWoolBlock> GREEN_ROTTEN_WOOL = BLOCKS.registerBlock("green_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_GREEN));
	public static final DeferredBlock<RottenWoolBlock> CYAN_ROTTEN_WOOL = BLOCKS.registerBlock("cyan_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_CYAN));
	public static final DeferredBlock<RottenWoolBlock> LIGHT_BLUE_ROTTEN_WOOL = BLOCKS.registerBlock("light_blue_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_LIGHT_BLUE));
	public static final DeferredBlock<RottenWoolBlock> BLUE_ROTTEN_WOOL = BLOCKS.registerBlock("blue_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_BLUE));
	public static final DeferredBlock<RottenWoolBlock> PURPLE_ROTTEN_WOOL = BLOCKS.registerBlock("purple_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_PURPLE));
	public static final DeferredBlock<RottenWoolBlock> MAGENTA_ROTTEN_WOOL = BLOCKS.registerBlock("magenta_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_MAGENTA));
	public static final DeferredBlock<RottenWoolBlock> PINK_ROTTEN_WOOL = BLOCKS.registerBlock("pink_rotten_wool", RottenWoolBlock::new, () -> rottenWool(MapColor.COLOR_PINK));
	public static final DeferredBlock<TrophyBlock> CARROT_TROPHY = BLOCKS.registerBlock("carrot_trophy", p -> new TrophyBlock(TrophyType.CARROT, p), SZBlocks::trophies);
	public static final DeferredBlock<TrophyBlock> POTATO_TROPHY = BLOCKS.registerBlock("potato_trophy", p -> new TrophyBlock(TrophyType.POTATO, p), SZBlocks::trophies);
	public static final DeferredBlock<TrophyBlock> IRON_INGOT_TROPHY = BLOCKS.registerBlock("iron_ingot_trophy", p -> new TrophyBlock(TrophyType.IRON_INGOT, p), SZBlocks::trophies);

	private SZBlocks() {}

	private static BlockBehaviour.Properties rottenWool(MapColor mapColor) {
		return BlockBehaviour.Properties.of().mapColor(mapColor).strength(0.8F).sound(SoundType.WOOL);
	}

	private static BlockBehaviour.Properties trophies() {
		return BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL);
	}
}
