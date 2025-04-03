package suszombification.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import suszombification.entity.ZombifiedAnimal;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.misc.AnimalUtil;

@Mixin(ZombieHorse.class)
public class ZombieHorseMixin extends AbstractHorse implements ZombifiedAnimal, NeutralMob {
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.LEATHER);
	private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombieHorse.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(ZombieHorse.class, EntityDataSerializers.INT);
	private int conversionTime;
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	protected ZombieHorseMixin(EntityType<? extends AbstractHorse> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2D));
		goalSelector.addGoal(2, new BreedGoal(this, 1.0D, AbstractHorse.class));
		goalSelector.addGoal(3, new SPPTemptGoal(this, 1.0D, Ingredient.of(Items.LEATHER), false));
		goalSelector.addGoal(4, new FollowParentGoal(this, 1.0D));
		goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, false));
		goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7D));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	@Inject(method = "createAttributes", at = @At("HEAD"), cancellable = true)
	private static void suszombification$createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> callback) {
		callback.setReturnValue(createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.ATTACK_DAMAGE, 2.0F));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_CONVERTING_ID, false);
		builder.define(DATA_ID_TYPE_VARIANT, 0);
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

	@Override
	public boolean isFood(ItemStack stack) {
		return AnimalUtil.isFood(stack, FOOD_ITEMS) || super.isFood(stack);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		int conversionTime = tag.getIntOr("ConversionTime", -1);

		if (conversionTime > -1)
			startConverting(conversionTime);

		entityData.set(DATA_ID_TYPE_VARIANT, tag.getIntOr("Variant", 0));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ConversionTime", isConverting() ? conversionTime : -1);
		tag.putInt("Variant", entityData.get(DATA_ID_TYPE_VARIANT));
	}

	@Override
	public int getRemainingPersistentAngerTime() {
		return remainingPersistentAngerTime;
	}

	@Override
	public void setRemainingPersistentAngerTime(int time) {
		remainingPersistentAngerTime = time;
	}

	@Override
	public UUID getPersistentAngerTarget() {
		return persistentAngerTarget;
	}

	@Override
	public void setPersistentAngerTarget(UUID target) {
		persistentAngerTarget = target;
	}

	@Override
	public void startPersistentAngerTimer() {
		setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(random));
	}

	@Override
	public EntityType<? extends Animal> getNormalVariant() {
		return EntityType.HORSE;
	}

	@Override
	public void readFromVanilla(Animal animal) {
		if (animal instanceof Horse horse)
			entityData.set(DATA_ID_TYPE_VARIANT, horse.getTypeVariant());
	}

	@Override
	public void writeToVanilla(Animal animal) {
		if (animal instanceof Horse horse)
			horse.setTypeVariant(entityData.get(DATA_ID_TYPE_VARIANT));
	}

	@Override
	public boolean isConverting() {
		return entityData.get(DATA_CONVERTING_ID);
	}

	@Override
	public void setConverting() {
		entityData.set(DATA_CONVERTING_ID, true);
	}

	@Override
	public void setConversionTime(int conversionTime) {
		this.conversionTime = conversionTime;
	}

	@Override
	public int getConversionTime() {
		return conversionTime;
	}
}
