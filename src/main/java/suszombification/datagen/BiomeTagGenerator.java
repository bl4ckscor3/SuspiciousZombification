package suszombification.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import suszombification.SZTags;
import suszombification.SuspiciousZombification;

public class BiomeTagGenerator extends BiomeTagsProvider {
	public BiomeTagGenerator(PackOutput output, CompletableFuture<Provider> lookupProvider) {
		super(output, lookupProvider, SuspiciousZombification.MODID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(SZTags.Biomes.HAS_DESERT_ZOMBIE_COVE).add(Biomes.DESERT);
		//@formatter:off
		tag(SZTags.Biomes.HAS_ZOMBIE_COVE).addTag(BiomeTags.IS_MOUNTAIN).add(
				Biomes.PLAINS,
				Biomes.SAVANNA,
				Biomes.SNOWY_PLAINS,
				Biomes.TAIGA,
				Biomes.GROVE);
		//@formatter:on
	}

	@Override
	public String getName() {
		return "Biome Tags: " + SuspiciousZombification.MODID;
	}
}
