package suszombification.entity.ai;

import java.util.function.Predicate;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import suszombification.SZConfig;
import suszombification.entity.ZombifiedAnimal;

public class NearestNormalVariantTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
	protected final EntityType<? extends Animal> targetType;
	protected final Predicate<Animal> mobPredicate;

	public NearestNormalVariantTargetGoal(ZombifiedAnimal zombifiedAnimal, boolean mustSee, boolean mustReach) {
		this(zombifiedAnimal, mustSee, mustReach, animal -> true);
	}

	public NearestNormalVariantTargetGoal(ZombifiedAnimal zombifiedAnimal, boolean mustSee, boolean mustReach, Predicate<Animal> predicate) {
		super((Animal) zombifiedAnimal, LivingEntity.class, mustSee, mustReach);
		targetType = zombifiedAnimal.getNormalVariant();
		mobPredicate = predicate;
	}

	@Override
	public boolean canUse() {
		return SZConfig.INSTANCE.hostileZombieAnimals.get() && !((ZombifiedAnimal) mob).isConverting() && mobPredicate.test((Animal) mob) && super.canUse();
	}

	@Override
	protected void findTarget() {
		target = mob.level().getNearestEntity(mob.level().getEntities(targetType, getTargetSearchArea(getFollowDistance()), e -> true), targetConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ());
	}
}
