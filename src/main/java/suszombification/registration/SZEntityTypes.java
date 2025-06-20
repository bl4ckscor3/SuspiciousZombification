package suszombification.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import suszombification.SuspiciousZombification;
import suszombification.entity.ThrownRottenEgg;
import suszombification.entity.ZombifiedCat;
import suszombification.entity.ZombifiedChicken;
import suszombification.entity.ZombifiedCow;
import suszombification.entity.ZombifiedPig;
import suszombification.entity.ZombifiedSheep;

@EventBusSubscriber(modid = SuspiciousZombification.MODID)
public class SZEntityTypes {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, SuspiciousZombification.MODID);
	//@formatter:off
	public static final DeferredHolder<EntityType<?>, EntityType<ZombifiedCat>> ZOMBIFIED_CAT = ENTITY_TYPES.register("zombified_cat", () -> EntityType.Builder.of(ZombifiedCat::new, MobCategory.CREATURE)
			.sized(0.6F, 0.7F)
			.clientTrackingRange(8)
			.build(key("zombified_cat")));
	public static final DeferredHolder<EntityType<?>, EntityType<ZombifiedChicken>> ZOMBIFIED_CHICKEN = ENTITY_TYPES.register("zombified_chicken", () -> EntityType.Builder.of(ZombifiedChicken::new, MobCategory.CREATURE)
			.sized(0.4F, 0.7F)
			.clientTrackingRange(10)
			.build(key("zombified_chicken")));
	public static final DeferredHolder<EntityType<?>, EntityType<ZombifiedCow>> ZOMBIFIED_COW = ENTITY_TYPES.register("zombified_cow", () -> EntityType.Builder.of(ZombifiedCow::new, MobCategory.CREATURE)
			.sized(0.9F, 1.4F)
			.clientTrackingRange(10)
			.build(key("zombified_cow")));
	public static final DeferredHolder<EntityType<?>, EntityType<ZombifiedPig>> ZOMBIFIED_PIG = ENTITY_TYPES.register("zombified_pig", () -> EntityType.Builder.of(ZombifiedPig::new, MobCategory.CREATURE)
			.sized(0.9F, 0.9F)
			.clientTrackingRange(10)
			.build(key("zombified_pig")));
	public static final DeferredHolder<EntityType<?>, EntityType<ZombifiedSheep>> ZOMBIFIED_SHEEP = ENTITY_TYPES.register("zombified_sheep", () -> EntityType.Builder.of(ZombifiedSheep::new, MobCategory.CREATURE)
			.sized(0.9F, 1.3F)
			.clientTrackingRange(10)
			.build(key("zombified_sheep")));
	public static final DeferredHolder<EntityType<?>, EntityType<ThrownRottenEgg>> ROTTEN_EGG = ENTITY_TYPES.register("rotten_egg", () -> EntityType.Builder.<ThrownRottenEgg>of(ThrownRottenEgg::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build(key("rotten_egg")));
	//@formatter:on
	private SZEntityTypes() {}

	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(SZEntityTypes.ZOMBIFIED_CAT.get(), ZombifiedCat.createAttributes().build());
		event.put(SZEntityTypes.ZOMBIFIED_CHICKEN.get(), ZombifiedChicken.createAttributes().build());
		event.put(SZEntityTypes.ZOMBIFIED_COW.get(), ZombifiedCow.createAttributes().build());
		event.put(SZEntityTypes.ZOMBIFIED_PIG.get(), ZombifiedPig.createAttributes().build());
		event.put(SZEntityTypes.ZOMBIFIED_SHEEP.get(), ZombifiedSheep.createAttributes().build());
	}

	private static ResourceKey<EntityType<?>> key(String name) {
		return ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(SuspiciousZombification.MODID, name));
	}
}
