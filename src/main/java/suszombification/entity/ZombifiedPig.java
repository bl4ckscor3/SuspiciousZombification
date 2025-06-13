package suszombification.entity;

import java.util.UUID;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

public class ZombifiedPig extends Pig implements NeutralMob, ZombifiedAnimal {
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.PORKCHOP);
	private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombifiedPig.class, EntityDataSerializers.BOOLEAN);
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int conversionTime;
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedPig(EntityType<? extends ZombifiedPig> type, Level level) {
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
		goalSelector.addGoal(2, new TemptGoal(this, 1.2D, Ingredient.of(SZItems.PORKCHOP_ON_A_STICK.get()), false));
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
		return createAnimalAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.23D).add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	@Override
	public void tick() {
		AnimalUtil.tick(this);
		super.tick();
	}

	@Override
	public LivingEntity getControllingPassenger() {
		return getFirstPassenger() instanceof LivingEntity firstPassenger && canBeControlledBy(firstPassenger) ? firstPassenger : null;
	}

	public boolean canBeControlledBy(Entity entity) {
		return entity instanceof Player player && player.isHolding(SZItems.PORKCHOP_ON_A_STICK.get());
	}

	@Override
	public float getVoicePitch() {
		return isBaby() ? (random.nextFloat() - random.nextFloat()) * 0.2F + 0.5F : (random.nextFloat() - random.nextFloat()) * 0.2F;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
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
	public Pig getBreedOffspring(ServerLevel level, AgeableMob parent) {
		ZombifiedPig pig = SZEntityTypes.ZOMBIFIED_PIG.get().create(level, EntitySpawnReason.BREEDING);

		if (pig != null && parent instanceof ZombifiedPig pig1)
			pig.setVariant(random.nextBoolean() ? getVariant() : pig1.getVariant());

		return pig;
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
		return EntityType.PIG;
	}

	@Override
	public void readFromVanilla(Animal animal) {
		if (animal instanceof Pig pig)
			setVariant(pig.getVariant());
	}

	@Override
	public void writeToVanilla(Animal animal) {
		if (animal instanceof Pig pig)
			pig.setVariant(getVariant());
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
