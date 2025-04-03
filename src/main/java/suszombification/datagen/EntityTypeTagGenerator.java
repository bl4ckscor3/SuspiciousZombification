package suszombification.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import suszombification.SZTags;
import suszombification.SuspiciousZombification;
import suszombification.registration.SZEntityTypes;

public class EntityTypeTagGenerator extends EntityTypeTagsProvider {
	public EntityTypeTagGenerator(PackOutput output, CompletableFuture<Provider> lookupProvider) {
		super(output, lookupProvider, SuspiciousZombification.MODID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		//@formatter:off
		tag(SZTags.EntityTypes.AFFECTED_BY_ZOMBIES_GRACE).addTag(EntityTypeTags.ZOMBIES);
		tag(EntityTypeTags.ZOMBIES).add(
				SZEntityTypes.ZOMBIFIED_CAT.get(),
				SZEntityTypes.ZOMBIFIED_CHICKEN.get(),
				SZEntityTypes.ZOMBIFIED_COW.get(),
				SZEntityTypes.ZOMBIFIED_PIG.get(),
				SZEntityTypes.ZOMBIFIED_SHEEP.get());
		//@formatter:on
	}

	@Override
	public String getName() {
		return "Entity Type Tags: " + SuspiciousZombification.MODID;
	}
}
