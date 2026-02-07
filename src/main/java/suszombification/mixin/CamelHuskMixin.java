package suszombification.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.camel.CamelHusk;
import net.minecraft.world.entity.player.Player;
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
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

	protected CamelHuskMixin(EntityType<? extends Camel> type, Level level) {
		super(type, level);
	}

	@Override
	protected void addBehaviourGoals() {
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
