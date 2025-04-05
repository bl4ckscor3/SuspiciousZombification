package suszombification.effect;

import java.util.List;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import suszombification.SZConfig;
import suszombification.SZTags;
import suszombification.registration.SZEffects;

public class ZombiesCurseEffect extends VicinityAffectingEffect {
	public ZombiesCurseEffect(MobEffectCategory category, int color) {
		super(category, color,
		//@formatter:off
				amplifier -> 15,
				e -> { return
						!e.getType().is(SZTags.EntityTypes.AFFECTED_BY_ZOMBIES_GRACE)
						&& !e.hasEffect(SZEffects.DECOMPOSING.get())
						&& (e instanceof Player || (e instanceof Animal a
								&& !a.hasCustomName() //don't affect animals with name tags
								&& (!(a instanceof TamableAnimal ta) || !ta.isTame()) //don't affect tamed animals
								&& (!(a instanceof Bucketable b) || !b.fromBucket()))); //don't affect animals that were spawned from a bucket
				},
				() -> new MobEffectInstance(SZEffects.DECOMPOSING.get(), 300));
		//@formatter:on
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return super.isDurationEffectTick(duration, amplifier) && SZConfig.INSTANCE.zombiesCurseZombification.get();
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return List.of();
	}
}
