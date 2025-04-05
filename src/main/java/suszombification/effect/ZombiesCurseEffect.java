package suszombification.effect;

import java.util.Set;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.EffectCure;
import suszombification.SZConfig;
import suszombification.SZTags;
import suszombification.registration.SZEffects;

public class ZombiesCurseEffect extends VicinityAffectingEffect {
	public ZombiesCurseEffect(MobEffectCategory category, int color) {
		super(category, color,
		//@formatter:off
				amplifier -> 15,
				e -> !e.getType().is(SZTags.EntityTypes.AFFECTED_BY_ZOMBIES_GRACE)
					&& !e.hasEffect(SZEffects.DECOMPOSING)
					&& (e instanceof Player || (e instanceof Animal a
							&& !a.hasCustomName() //don't affect animals with name tags
							&& (!(a instanceof TamableAnimal ta) || !ta.isTame()) //don't affect tamed animals
							&& (!(a instanceof Bucketable b) || !b.fromBucket()))), //don't affect animals that were spawned from a bucket
				() -> new MobEffectInstance(SZEffects.DECOMPOSING, 300));
		//@formatter:on
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return super.shouldApplyEffectTickThisTick(duration, amplifier) && SZConfig.INSTANCE.zombiesCurseZombification.get();
	}

	@Override
	public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {}
}
