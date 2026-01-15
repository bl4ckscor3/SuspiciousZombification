package suszombification.registration;

import java.util.Optional;

import com.mojang.serialization.Codec;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariants;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.level.entity.UniquelyIdentifyable;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import suszombification.SuspiciousZombification;

public class SZAttachmentTypes {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SuspiciousZombification.MODID);
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Optional<EntityReference<UniquelyIdentifyable>>>> ANGRY_AT = ATTACHMENT_TYPES.register("angry_at", () -> AttachmentType.builder(SZAttachmentTypes::empty).serialize(EntityReference.codec().optionalFieldOf("angry_at")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> ANGER_END_TIME = ATTACHMENT_TYPES.register("anger_end_time", () -> AttachmentType.builder(() -> -1L).serialize(Codec.LONG.fieldOf("anger_end_time")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> CONVERSION_TIME = ATTACHMENT_TYPES.register("conversion_time", () -> AttachmentType.builder(() -> -1).serialize(Codec.INT.fieldOf("ConversionTime")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> ZOMBIE_HORSE_VARIANT = ATTACHMENT_TYPES.register("zombie_horse_variant", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("Variant")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Holder<ZombieNautilusVariant>>> ZOMBIE_NAUTILUS_VARIANT = ATTACHMENT_TYPES.register("zombie_nautilus_variant", () -> AttachmentType.builder(holder -> VariantUtils.getDefaultOrAny(((Entity) holder).registryAccess(), ZombieNautilusVariants.TEMPERATE)).serialize(ZombieNautilusVariant.CODEC.fieldOf("variant")).build());

	// Needs to be here so Java generics are happy
	private static Optional<EntityReference<UniquelyIdentifyable>> empty() {
		return Optional.empty();
	}

	// You guessed it, generics again
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static EntityReference<LivingEntity> castReference(EntityReference toCast) {
		return (EntityReference<LivingEntity>) toCast;
	}

	// No comment
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Optional<EntityReference<UniquelyIdentifyable>> castOptional(Optional toCast) {
		return (Optional<EntityReference<UniquelyIdentifyable>>) toCast;
	}
}
