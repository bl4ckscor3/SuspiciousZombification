package suszombification.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.ZombieHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import suszombification.entity.ZombifiedAnimal;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.misc.AnimalUtil;
import suszombification.registration.SZAttachmentTypes;

@Mixin(ZombieHorse.class)
public class ZombieHorseMixin extends AbstractHorse implements ZombifiedAnimal, NeutralMob {
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.LEATHER);
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

	protected ZombieHorseMixin(EntityType<? extends AbstractHorse> type, Level level) {
		super(type, level);
	}

	@Inject(method = "addBehaviourGoals", at = @At("Head"))
	protected void suszombification$addSusZGoals(CallbackInfo ci) {
		goalSelector.addGoal(3, new SPPTemptGoal(this, 1.0D, Ingredient.of(Items.LEATHER), false));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	@Inject(method = "createAttributes", at = @At("HEAD"), cancellable = true)
	private static void suszombification$createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> callback) {
		callback.setReturnValue(ZombieHorse.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.ATTACK_DAMAGE, 2.0F));
	}

	@Override
	public void tick() {
		AnimalUtil.tick(this);
		super.tick();
	}

	@Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
	private void suszombification$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callback) {
		InteractionResult returnValue = AnimalUtil.mobInteract(this, player, hand);

		if (returnValue != InteractionResult.PASS)
			callback.setReturnValue(returnValue);
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
		tag.getInt("ConversionTime").ifPresent(conversionTime -> setData(SZAttachmentTypes.ZOMBIE_HORSE_CONVERSION_TIME, conversionTime));
		tag.getInt("Variant").ifPresent(variant -> setData(SZAttachmentTypes.ZOMBIE_HORSE_VARIANT, variant));

		Optional<Object> angryAt = getData(SZAttachmentTypes.ZOMBIE_HORSE_ANGRY_AT);

		if (angryAt.isPresent())
			setTarget(EntityReference.getLivingEntity((EntityReference<LivingEntity>) angryAt.get(), level()));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput tag) {
		super.addAdditionalSaveData(tag);
		addPersistentAngerSaveData(tag);
	}

	@Override
	public long getPersistentAngerEndTime() {
		return getData(SZAttachmentTypes.ZOMBIE_HORSE_ANGER_END_TIME);
	}

	@Override
	public void setPersistentAngerEndTime(long time) {
		setData(SZAttachmentTypes.ZOMBIE_HORSE_ANGER_END_TIME, time);
	}

	@Override
	public EntityReference<LivingEntity> getPersistentAngerTarget() {
		Optional<Object> angryAt = getData(SZAttachmentTypes.ZOMBIE_HORSE_ANGRY_AT);

		return (EntityReference<LivingEntity>) angryAt.orElse(null);
	}

	@Override
	public void setPersistentAngerTarget(EntityReference<LivingEntity> entity) {
		setData(SZAttachmentTypes.ZOMBIE_HORSE_ANGRY_AT, Optional.ofNullable(entity));
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
		return EntityType.HORSE;
	}

	@Override
	public void readFromVanilla(Animal animal) {
		if (animal instanceof Horse horse)
			setData(SZAttachmentTypes.ZOMBIE_HORSE_VARIANT, horse.getTypeVariant());
	}

	@Override
	public void writeToVanilla(Animal animal) {
		if (animal instanceof Horse horse)
			horse.setTypeVariant(getData(SZAttachmentTypes.ZOMBIE_HORSE_VARIANT));
	}

	@Override
	public boolean isConverting() {
		return getData(SZAttachmentTypes.ZOMBIE_HORSE_CONVERSION_TIME) >= 0;
	}

	@Override
	public void setConverting() {} //Conversion is started through setConversionTime

	@Override
	public void setConversionTime(int conversionTime) {
		setData(SZAttachmentTypes.ZOMBIE_HORSE_CONVERSION_TIME, conversionTime);
	}

	@Override
	public int getConversionTime() {
		return getData(SZAttachmentTypes.ZOMBIE_HORSE_CONVERSION_TIME);
	}
}
