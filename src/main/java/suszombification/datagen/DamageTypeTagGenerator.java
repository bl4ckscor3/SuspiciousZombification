package suszombification.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import suszombification.SZDamageSources;
import suszombification.SuspiciousZombification;

public class DamageTypeTagGenerator extends TagsProvider<DamageType> {
	protected DamageTypeTagGenerator(PackOutput output, CompletableFuture<Provider> lookupProvider) {
		super(output, Registries.DAMAGE_TYPE, lookupProvider, SuspiciousZombification.MODID);
	}

	@Override
	protected void addTags(Provider provider) {
		//@formatter:off
		tag(DamageTypeTags.BYPASSES_ARMOR)
				.addOptional(SZDamageSources.DECOMPOSING.location())
				.addOptional(SZDamageSources.RITUAL_SACRIFICE.location());
		tag(DamageTypeTags.IS_EXPLOSION)
				.addOptional(SZDamageSources.SPP_EXPLOSION.location());
		//@formatter:on
	}
}
