package suszombification.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import suszombification.SuspiciousZombification;
import suszombification.effect.AmplifyingEffect;
import suszombification.effect.DecomposingEffect;
import suszombification.effect.VicinityAffectingEffect;
import suszombification.effect.ZombiesCurseEffect;

public class SZEffects {
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, SuspiciousZombification.MODID);
	public static final DeferredHolder<MobEffect, AmplifyingEffect> AMPLIFYING = EFFECTS.register("amplifying", () -> new AmplifyingEffect(MobEffectCategory.BENEFICIAL, 0xEBE294));
	public static final DeferredHolder<MobEffect, MobEffect> CUSHION = EFFECTS.register("cushion", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0xEDEDED));
	public static final DeferredHolder<MobEffect, DecomposingEffect> DECOMPOSING = EFFECTS.register("decomposing", () -> new DecomposingEffect(MobEffectCategory.HARMFUL, 0x799C65).addAttributeModifier(Attributes.MOVEMENT_SPEED, SuspiciousZombification.resLoc("zombie_slowness"), -0.2D, Operation.ADD_MULTIPLIED_TOTAL));
	//@formatter:off
	public static final DeferredHolder<MobEffect,VicinityAffectingEffect> STENCH = EFFECTS.register("stench", () -> new VicinityAffectingEffect(MobEffectCategory.BENEFICIAL, 0xCACC52,
			amplifier -> Math.max((amplifier + 1) * 3, 10),
			e -> true,
			() -> new MobEffectInstance(MobEffects.NAUSEA, 200, 1),
			() -> new MobEffectInstance(MobEffects.WEAKNESS, 200, 1),
			() -> new MobEffectInstance(MobEffects.SLOWNESS, 200, 0)));
	//@formatter:on
	public static final DeferredHolder<MobEffect, MobEffect> ZOMBIES_GRACE = EFFECTS.register("zombies_grace", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0x009E9E));
	public static final DeferredHolder<MobEffect, ZombiesCurseEffect> ZOMBIES_CURSE = EFFECTS.register("zombies_curse", () -> new ZombiesCurseEffect(MobEffectCategory.HARMFUL, 0xAE1A1A));

	private SZEffects() {}
}
