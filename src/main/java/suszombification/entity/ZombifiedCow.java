package suszombification.entity;

import java.util.UUID;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
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
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.misc.AnimalUtil;
import suszombification.registration.SZEntityTypes;
import suszombification.registration.SZItems;

public class ZombifiedCow extends Cow implements NeutralMob, ZombifiedAnimal {
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.BEEF, Items.LEATHER);
	private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombifiedCow.class, EntityDataSerializers.BOOLEAN);
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int conversionTime;
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedCow(EntityType<? extends ZombifiedCow> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_CONVERTING_ID, false);
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

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimalAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.18D).add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	@Override
	public void tick() {
		AnimalUtil.tick(this);
		super.tick();
	}

	@Override
	public float getVoicePitch() {
		return isBaby() ? (random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F : (random.nextFloat() - random.nextFloat()) * 0.2F;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.is(Items.BUCKET) && !isBaby()) {
			ItemStack filledBucket = ItemUtils.createFilledResult(stack, player, SZItems.SPOILED_MILK_BUCKET.get().getDefaultInstance());

			player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
			player.setItemInHand(hand, filledBucket);
			return InteractionResult.SUCCESS;
		}

		InteractionResult returnValue = AnimalUtil.mobInteract(this, player, hand);

		if (returnValue != InteractionResult.PASS)
			return returnValue;

		return super.mobInteract(player, hand);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (!AnimalUtil.handleEntityEvent(this, id))
			super.handleEntityEvent(id);
	}

	@Override
	public ZombifiedCow getBreedOffspring(ServerLevel level, AgeableMob parent) {
		ZombifiedCow cow = SZEntityTypes.ZOMBIFIED_COW.get().create(level, EntitySpawnReason.BREEDING);

		if (cow != null && parent instanceof ZombifiedCow cow1)
			cow.setVariant(random.nextBoolean() ? getVariant() : cow1.getVariant());

		return cow;
	}

	@Override
	public int getBaseExperienceReward(ServerLevel level) {
		return super.getBaseExperienceReward(level) + 5;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return AnimalUtil.isFood(stack, FOOD_ITEMS);
	}

	@Override
	public void readAdditionalSaveData(ValueInput tag) {
		super.readAdditionalSaveData(tag);

		int conversionTime = tag.getIntOr("ConversionTime", -1);

		if (conversionTime > -1)
			startConverting(conversionTime);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ConversionTime", isConverting() ? conversionTime : -1);
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
		return EntityType.COW;
	}

	@Override
	public void readFromVanilla(Animal animal) {
		if (animal instanceof Cow cow)
			setVariant(cow.getVariant());
	}

	@Override
	public void writeToVanilla(Animal animal) {
		if (animal instanceof Cow cow)
			cow.setVariant(getVariant());
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

	@Override
	public int getConversionTime() {
		return conversionTime;
	}
}
