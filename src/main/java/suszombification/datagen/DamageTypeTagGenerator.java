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
		tag(DamageTypeTags.BYPASSES_ARMOR).add(SZDamageSources.DECOMPOSING, SZDamageSources.RITUAL_SACRIFICE);
		tag(DamageTypeTags.IS_EXPLOSION).add(SZDamageSources.SPP_EXPLOSION);
	}
}
