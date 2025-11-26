package suszombification.entity;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.Util;
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
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import suszombification.entity.ai.NearestNormalVariantTargetGoal;
import suszombification.entity.ai.SPPTemptGoal;
import suszombification.misc.AnimalUtil;
import suszombification.registration.SZBlocks;
import suszombification.registration.SZEntityTypes;
import suszombification.registration.SZLoot;

public class ZombifiedSheep extends Sheep implements NeutralMob, ZombifiedAnimal {
	public static final Map<DyeColor, ItemLike> ITEM_BY_DYE = Util.make(Maps.newEnumMap(DyeColor.class), map -> {
		map.put(DyeColor.WHITE, SZBlocks.WHITE_ROTTEN_WOOL.get());
		map.put(DyeColor.ORANGE, SZBlocks.ORANGE_ROTTEN_WOOL.get());
		map.put(DyeColor.MAGENTA, SZBlocks.MAGENTA_ROTTEN_WOOL.get());
		map.put(DyeColor.LIGHT_BLUE, SZBlocks.LIGHT_BLUE_ROTTEN_WOOL.get());
		map.put(DyeColor.YELLOW, SZBlocks.YELLOW_ROTTEN_WOOL.get());
		map.put(DyeColor.LIME, SZBlocks.LIME_ROTTEN_WOOL.get());
		map.put(DyeColor.PINK, SZBlocks.PINK_ROTTEN_WOOL.get());
		map.put(DyeColor.GRAY, SZBlocks.GRAY_ROTTEN_WOOL.get());
		map.put(DyeColor.LIGHT_GRAY, SZBlocks.LIGHT_GRAY_ROTTEN_WOOL.get());
		map.put(DyeColor.CYAN, SZBlocks.CYAN_ROTTEN_WOOL.get());
		map.put(DyeColor.PURPLE, SZBlocks.PURPLE_ROTTEN_WOOL.get());
		map.put(DyeColor.BLUE, SZBlocks.BLUE_ROTTEN_WOOL.get());
		map.put(DyeColor.BROWN, SZBlocks.BROWN_ROTTEN_WOOL.get());
		map.put(DyeColor.GREEN, SZBlocks.GREEN_ROTTEN_WOOL.get());
		map.put(DyeColor.RED, SZBlocks.RED_ROTTEN_WOOL.get());
		map.put(DyeColor.BLACK, SZBlocks.BLACK_ROTTEN_WOOL.get());
	});
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.MUTTON);
	private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID = SynchedEntityData.defineId(ZombifiedSheep.class, EntityDataSerializers.BOOLEAN);
	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int conversionTime;
	private int remainingPersistentAngerTime;
	private UUID persistentAngerTarget;

	public ZombifiedSheep(EntityType<? extends Sheep> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_CONVERTING_ID, false);
	}

	@Override
	protected void registerGoals() {
		eatBlockGoal = new EatBlockGoal(this);
		goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
		goalSelector.addGoal(2, new SPPTemptGoal(this, 1.0D, FOOD_ITEMS, false, stack -> stack.is(ItemTags.WOOL)));
		goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));
		goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		goalSelector.addGoal(5, eatBlockGoal);
		goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestNormalVariantTargetGoal(this, true, false));
		targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createAnimalAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.23F).add(Attributes.ATTACK_DAMAGE, 2.0F);
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
	public void shear(ServerLevel level, SoundSource category, ItemStack stack) {
		level().playSound(null, this, SoundEvents.SHEEP_SHEAR, category, 1.0F, 1.0F);
		dropFromShearingLootTable(level, SZLoot.SHEAR_ZOMBIFIED_SHEEP, stack, (sameLevel, woolStack) -> {
			for (int i = 0; i < woolStack.getCount(); ++i) {
				ItemEntity item = spawnAtLocation(level, woolStack.copyWithCount(1), 1);

				if (item != null)
					item.setDeltaMovement(item.getDeltaMovement().add((random.nextFloat() - random.nextFloat()) * 0.1F, random.nextFloat() * 0.05F, (random.nextFloat() - random.nextFloat()) * 0.1F));
			}
		});
		setSheared(true);
	}

	@Override
	public Sheep getBreedOffspring(ServerLevel level, AgeableMob mob) {
		Sheep newSheep = SZEntityTypes.ZOMBIFIED_SHEEP.get().create(level, EntitySpawnReason.BREEDING);

		if (newSheep != null) {
			DyeColor myColor = getColor();
			DyeColor partnerColor = ((Sheep) mob).getColor();

			newSheep.setColor(DyeColor.getMixedColor(level, myColor, partnerColor));
		}

		return newSheep;
	}

	@Override
	public int getBaseExperienceReward(ServerLevel level) {
		return super.getBaseExperienceReward(level) + 5;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return AnimalUtil.isFood(stack, FOOD_ITEMS, ingredient -> ingredient.is(ItemTags.WOOL));
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
		return EntityType.SHEEP;
	}

	@Override
	public void readFromVanilla(Animal animal) {
		if (animal instanceof Sheep sheep) {
			setColor(sheep.getColor());
			setSheared(sheep.isSheared());
		}
	}

	@Override
	public void writeToVanilla(Animal animal) {
		if (animal instanceof Sheep sheep) {
			sheep.setColor(getColor());
			sheep.setSheared(isSheared());
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

	@Override
	public int getConversionTime() {
		return conversionTime;
	}
}
