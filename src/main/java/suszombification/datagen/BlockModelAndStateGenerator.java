package suszombification.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fmllegacy.RegistryObject;
import suszombification.SuspiciousZombification;
import suszombification.block.TrophyBlock;
import suszombification.registration.SZBlocks;

public class BlockModelAndStateGenerator extends BlockStateProvider {
	public BlockModelAndStateGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, SuspiciousZombification.MODID, existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		for(RegistryObject<Block> ro : SZBlocks.BLOCKS.getEntries()) {
			Block block = ro.get();

			if(block instanceof TrophyBlock)
				horizontalBlock(block, state -> new UncheckedModelFile(modLoc("block/trophy")));
			else
				simpleBlock(block);
		}
	}
}
