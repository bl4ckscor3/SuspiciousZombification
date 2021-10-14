package suszombification.entity;

import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import suszombification.SZEntityTypes;
import suszombification.SZItems;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.item.SuspiciousPumpkinPieItem;

public class ZombifiedChicken extends Animal implements NeutralMob, ZombifiedAnimal { //can't extend Chicken because of the hardcoded egg laying logic in Chicken#aiStep
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CHICKEN, Items.FEATHER);
	private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombifiedChicken.class, EntityDataSerializers.BOOLEAN);
	private int conversionTime;
	public float flap;
	public float flapSpeed;
	public float oFlapSpeed;
	public float oFlap;
	public float flapping = 1.0F;
	private float nextFlap = 1.0F;
	public int eggTime = random.nextInt(6000) + 6000;
	public boolean isChickenJockey;
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedChicken(EntityType<? extends ZombifiedChicken> type, Level level) {
		super(type, level);
		entityData.define(DATA_CONVERTING_ID, false);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(2, new SPPTemptGoal(this, 1.0D, FOOD_ITEMS, false));
		goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
		goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
		return isBaby() ? size.height * 0.85F : size.height * 0.92F;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 1.0D);
	}

	@Override
	public void tick() {
		if (!level.isClientSide && isAlive() && isConverting()) {
			int i = getConversionProgress();

			conversionTime -= i;
			if (conversionTime <= 0 && ForgeEventFactory.canLivingConvert(this, EntityType.CHICKEN, this::setConversionTime)) {
				finishConversion((ServerLevel)level);
			}
		}

		super.tick();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		oFlap = flap;
		oFlapSpeed = flapSpeed;
		flapSpeed = (float)(flapSpeed + (onGround ? -1 : 4) * 0.3D);
		flapSpeed = Mth.clamp(flapSpeed, 0.0F, 1.0F);

		if (!onGround && flapping < 1.0F) {
			flapping = 1.0F;
		}

		flapping = flapping * 0.9F;

		Vec3 vec3 = getDeltaMovement();

		if (!onGround && vec3.y < 0.0D) {
			setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
		}

		flap += flapping * 2.0F;

		if (!level.isClientSide && isAlive() && !isBaby() && !isChickenJockey() && --eggTime <= 0) {
			playSound(SoundEvents.CHICKEN_EGG, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
			spawnAtLocation(SZItems.ROTTEN_EGG.get());
			eggTime = random.nextInt(6000) + 6000;
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && SuspiciousPumpkinPieItem.hasIngredient(stack, Items.GOLDEN_APPLE)) {
			if (hasEffect(MobEffects.WEAKNESS)) {
				if (!player.getAbilities().instabuild) {
					stack.shrink(1);
				}

				if (!level.isClientSide) {
					startConverting(random.nextInt(2401) + 3600);
				}

				gameEvent(GameEvent.MOB_INTERACT, eyeBlockPosition());
				return InteractionResult.SUCCESS;
			}

			return InteractionResult.CONSUME;
		}

		return super.mobInteract(player, hand);
	}

	@Override
	public void handleEntityEvent(byte pId) {
		if (pId == 16) {
			if (!isSilent()) {
				level.playLocalSound(getX(), getEyeY(), getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, getSoundSource(), 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
			}
		} else {
			super.handleEntityEvent(pId);
		}
	}

	@Override
	protected boolean isFlapping() {
		return flyDist > nextFlap;
	}

	@Override
	protected void onFlap() {
		this.nextFlap = flyDist + flapSpeed / 2.0F;
	}

	@Override
	public boolean causeFallDamage(float height, float mult, DamageSource source) {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.CHICKEN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.CHICKEN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.CHICKEN_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
	}

	@Override
	public float getVoicePitch() {
		return isBaby() ? (random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F : (random.nextFloat() - random.nextFloat()) * 0.2F;
	}

	@Override
	public ZombifiedChicken getBreedOffspring(ServerLevel level, AgeableMob parent) {
		return SZEntityTypes.ZOMBIFIED_CHICKEN.get().create(level);
	}

	@Override
	protected int getExperienceReward(Player player) {
		return (isChickenJockey() ? 10 : super.getExperienceReward(player)) + 5;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		if (stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && stack.hasTag() && stack.getTag().contains("Ingredient")) {
			CompoundTag ingredientTag = stack.getTag().getCompound("Ingredient");
			ItemStack ingredient = ItemStack.of(ingredientTag);

			return FOOD_ITEMS.test(ingredient);
		}

		return false;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.isChickenJockey = tag.getBoolean("IsChickenJockey");

		if (tag.contains("EggLayTime")) {
			this.eggTime = tag.getInt("EggLayTime");
		}

		if (tag.contains("ConversionTime", 99) && tag.getInt("ConversionTime") > -1) {
			startConverting(tag.getInt("ConversionTime"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("IsChickenJockey", isChickenJockey);
		tag.putInt("EggLayTime", eggTime);
		tag.putInt("ConversionTime", isConverting() ? conversionTime : -1);
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return isChickenJockey();
	}

	@Override
	public void positionRider(Entity passenger) {
		super.positionRider(passenger);

		float f = Mth.sin(yBodyRot * ((float)Math.PI / 180F));
		float f1 = Mth.cos(yBodyRot * ((float)Math.PI / 180F));

		passenger.setPos(getX() + 0.1F * f, getY(0.5D) + passenger.getMyRidingOffset() + 0.0D, getZ() - 0.1F * f1);

		if (passenger instanceof LivingEntity entity) {
			entity.yBodyRot = yBodyRot;
		}

	}

	public boolean isChickenJockey() {
		return isChickenJockey;
	}

	public void setChickenJockey(boolean jockey) {
		this.isChickenJockey = jockey;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
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
		this.persistentAngerTarget = target;
	}

	@Override
	public void startPersistentAngerTimer() {
		setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(random));
	}

	@Override
	public EntityType<? extends Animal> getNormalVariant() {
		return EntityType.CHICKEN;
	}

	@Override
	public void readFromVanilla(Animal animal) {
		if (animal instanceof Chicken chicken) {
			setChickenJockey(chicken.isChickenJockey());
		}
	}

	@Override
	public void writeToVanilla(Animal animal) {
		if (animal instanceof Chicken chicken) {
			chicken.setChickenJockey(isChickenJockey());
		}
	}

	@Override
	public boolean isConverting() {
		return getEntityData().get(DATA_CONVERTING_ID);
	}

	@Override
	public void setConverting() {
		getEntityData().set(DATA_CONVERTING_ID, true);
	}

	@Override
	public void setConversionTime(int conversionTime) {
		this.conversionTime = conversionTime;
	}
}
