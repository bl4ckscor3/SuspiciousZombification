package suszombification.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.camel.CamelHusk;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.UniquelyIdentifyable;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import suszombification.entity.ZombifiedAnimal;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.misc.AnimalUtil;
import suszombification.registration.SZAttachmentTypes;

@Mixin(CamelHusk.class)
public class CamelHuskMixin extends Camel implements ZombifiedAnimal, NeutralMob {
	@Unique
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CACTUS);
	@Unique
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

	protected CamelHuskMixin(EntityType<? extends Camel> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	@Override
	public void tick() {
		AnimalUtil.tick(this);
		super.tick();
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		InteractionResult returnValue = AnimalUtil.mobInteract(this, player, hand);

		if (returnValue != InteractionResult.PASS)
			return returnValue;
		else
			return super.mobInteract(player, hand);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (!AnimalUtil.handleEntityEvent(this, id))
			super.handleEntityEvent(id);
	}

	@Override
	public int getBaseExperienceReward(ServerLevel level) {
		return super.getBaseExperienceReward(level) + 5;
	}

	@ModifyReturnValue(method = "isFood", at = @At("RETURN"))
	public boolean suszombification$addSusZFood(boolean original, ItemStack stack) {
		return AnimalUtil.isFood(stack, FOOD_ITEMS) || original;
	}

	@Override
	public void readAdditionalSaveData(ValueInput tag) {
		super.readAdditionalSaveData(tag);
		readPersistentAngerSaveData(level(), tag);
		tag.getInt("ConversionTime").ifPresent(conversionTime -> setData(SZAttachmentTypes.CONVERSION_TIME, conversionTime));

		Optional<EntityReference<UniquelyIdentifyable>> angryAt = getData(SZAttachmentTypes.ANGRY_AT);

		if (angryAt.isPresent())
			setTarget(EntityReference.getLivingEntity(SZAttachmentTypes.castReference(angryAt.get()), level()));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput tag) {
		super.addAdditionalSaveData(tag);
		addPersistentAngerSaveData(tag);
	}

	@ModifyReturnValue(method = "canMate", at = @At("RETURN"))
	public boolean suszombification$makeMateable(boolean original, Animal otherAnimal) {
		return super.canMate(otherAnimal);
	}

	@ModifyReturnValue(method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/camel/Camel;", at = @At("RETURN"))
	public Camel suszombification$adjustBreedOffspring(Camel original, ServerLevel level, AgeableMob mob) {
		Camel zamel = EntityType.CAMEL_HUSK.create(level, EntitySpawnReason.BREEDING);

		zamel.setTamed(isTamed());
		zamel.setOwner(getOwner());
		return zamel;
	}

	@ModifyReturnValue(method = "canFallInLove", at = @At("RETURN"))
	public boolean suszombification$makeAbleToFallInLove(boolean original) {
		return true;
	}

	@Override
	protected boolean handleEating(Player player, ItemStack stack) {
		if (AnimalUtil.isFood(stack, FOOD_ITEMS) && isTamed() && getAge() == 0 && !isInLove()) {
			setInLove(player);
			return true;
		}

		return super.handleEating(player, stack);
	}

	@Override
	public long getPersistentAngerEndTime() {
		return getData(SZAttachmentTypes.ANGER_END_TIME);
	}

	@Override
	public void setPersistentAngerEndTime(long time) {
		setData(SZAttachmentTypes.ANGER_END_TIME, time);
	}

	@Override
	public EntityReference<LivingEntity> getPersistentAngerTarget() {
		Optional<EntityReference<UniquelyIdentifyable>> angryAt = getData(SZAttachmentTypes.ANGRY_AT);

		if (angryAt.isPresent())
			return SZAttachmentTypes.castReference(angryAt.get());
		else
			return null;
	}

	@Override
	public void setPersistentAngerTarget(EntityReference<LivingEntity> entity) {
		setData(SZAttachmentTypes.ANGRY_AT, SZAttachmentTypes.castOptional(Optional.ofNullable(entity)));
	}

	@Override
	public void startPersistentAngerTimer() {
		setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(random));
	}

	@Override
	public void customServerAiStep(ServerLevel level) {
		super.customServerAiStep(level);
		updatePersistentAnger(level, false);
	}

	@Override
	public EntityType<? extends Animal> getNormalVariant() {
		return EntityType.CAMEL;
	}

	@Override
	public boolean isConverting() {
		return getData(SZAttachmentTypes.CONVERSION_TIME) >= 0;
	}

	@Override
	public void setConverting() {} //Conversion is started through setConversionTime

	@Override
	public void setConversionTime(int conversionTime) {
		setData(SZAttachmentTypes.CONVERSION_TIME, conversionTime);
	}

	@Override
	public int getConversionTime() {
		return getData(SZAttachmentTypes.CONVERSION_TIME);
	}
}
