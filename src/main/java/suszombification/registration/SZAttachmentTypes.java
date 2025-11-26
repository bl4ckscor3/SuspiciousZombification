package suszombification.registration;

import java.util.Optional;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.EntityReference;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import suszombification.SuspiciousZombification;

public class SZAttachmentTypes {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SuspiciousZombification.MODID);
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Optional<Object>>> ZOMBIE_HORSE_ANGRY_AT = ATTACHMENT_TYPES.register("zombie_horse_angry_at", () -> AttachmentType.builder(Optional::empty).serialize((IAttachmentSerializer<Optional<Object>>) EntityReference.codec().optionalFieldOf("angry_at")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> ZOMBIE_HORSE_ANGER_END_TIME = ATTACHMENT_TYPES.register("zombie_horse_anger_end_time", () -> AttachmentType.builder(() -> -1L).serialize(Codec.LONG.fieldOf("anger_end_time")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> ZOMBIE_HORSE_CONVERSION_TIME = ATTACHMENT_TYPES.register("zombie_horse_conversion_time", () -> AttachmentType.builder(() -> -1).serialize(Codec.INT.fieldOf("ConversionTime")).build());
	public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> ZOMBIE_HORSE_VARIANT = ATTACHMENT_TYPES.register("zombie_horse_variant", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("Variant")).build());
}
