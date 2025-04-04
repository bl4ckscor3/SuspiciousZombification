package suszombification;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingConversionEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import suszombification.entity.ZombifiedAnimal;
import suszombification.entity.ZombifiedCat;
import suszombification.entity.ZombifiedChicken;
import suszombification.entity.ZombifiedCow;
import suszombification.entity.ZombifiedPig;
import suszombification.entity.ZombifiedSheep;
import suszombification.item.SuspiciousPumpkinPieItem;
import suszombification.misc.SuspiciousRitual;
import suszombification.registration.SZEffects;
import suszombification.registration.SZItems;

@EventBusSubscriber(modid = SuspiciousZombification.MODID)
public class SZEventHandler {
	private SZEventHandler() {}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();

		if (entity instanceof PathfinderMob mob) {
			EntityType<?> type = mob.getType();

			if (type == EntityType.CAT)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedCat.class, 6.0F, 1.0F, 1.2F, animal -> !((ZombifiedCat) animal).isTame()));
			else if (type == EntityType.CHICKEN)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedChicken.class, 4.0F, 1.0F, 1.2F));
			else if (type == EntityType.COW)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedCow.class, 4.0F, 1.0F, 1.2F));
			else if (type == EntityType.PIG)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedPig.class, 4.0F, 1.0F, 1.2F));
			else if (type == EntityType.SHEEP)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedSheep.class, 4.0F, 1.0F, 1.2F));
			else if (type == EntityType.HORSE)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombieHorse.class, 4.0F, 1.0F, 1.2F));
		}
	}

	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		Entity entity = event.getTarget();
		Player player = event.getEntity();

		if (entity instanceof Animal animal && ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.containsKey(animal.getType())) {
			ItemStack stack = player.getItemInHand(event.getHand());

			if (stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && SuspiciousPumpkinPieItem.hasIngredient(stack, Items.ROTTEN_FLESH) && !animal.hasEffect(SZEffects.DECOMPOSING)) {
				animal.addEffect(new MobEffectInstance(SZEffects.DECOMPOSING, TimeUtil.rangeOfSeconds(50, 70).sample(animal.getRandom())));

				if (!player.getAbilities().instabuild)
					stack.shrink(1);

				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.SUCCESS);
			}
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();

		if (player.getItemInHand(event.getHand()).is(Items.LEAD))
			SuspiciousRitual.maybeSendInfoMessages(null, event.getLevel(), event.getPos(), player);
	}

	@SubscribeEvent
	public static void onEntityConvert(LivingConversionEvent.Pre event) {
		if (event.getEntity() instanceof ZombifiedPig && event.getOutcome() == EntityType.ZOMBIFIED_PIGLIN)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onLivingSetAttackTarget(LivingChangeTargetEvent event) {
		LivingEntity newAboutToBeSetTarget = event.getNewAboutToBeSetTarget();

		if (newAboutToBeSetTarget != null && newAboutToBeSetTarget.hasEffect(SZEffects.ZOMBIES_GRACE) && event.getEntity().getType().is(SZTags.EntityTypes.AFFECTED_BY_ZOMBIES_GRACE))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		LivingEntity livingEntity = event.getEntity();
		Entity killer = event.getSource().getEntity();
		Level level = livingEntity.level();

		if (!level.isClientSide && SZConfigHandler.INSTANCE.enableAnimalZombification.get() && (level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && killer instanceof ZombifiedAnimal zombifiedAnimal) {
			EntityType<? extends Mob> conversionType = (EntityType<? extends Mob>) killer.getType();

			if (livingEntity instanceof Animal killedEntity && killedEntity.getType() == zombifiedAnimal.getNormalVariant() && EventHooks.canLivingConvert(livingEntity, conversionType, timer -> {})) {
				if (level.getDifficulty() != Difficulty.HARD && level.random.nextBoolean())
					return;

				killedEntity.convertTo(conversionType, ConversionParams.single(killedEntity, true, true), convertedAnimal -> {
					EventHooks.finalizeMobSpawn(convertedAnimal, (ServerLevel) level, level.getCurrentDifficultyAt(convertedAnimal.blockPosition()), EntitySpawnReason.CONVERSION, null);
					((ZombifiedAnimal) convertedAnimal).readFromVanilla(killedEntity);
					EventHooks.onLivingConvert(livingEntity, convertedAnimal);

					if (!killer.isSilent())
						level.levelEvent(null, LevelEvent.SOUND_ZOMBIE_INFECTED, killer.blockPosition(), 0);
				});
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if (event.getOriginal().hasEffect(SZEffects.ZOMBIES_CURSE))
			event.getEntity().addEffect(new MobEffectInstance(SZEffects.ZOMBIES_CURSE, Integer.MAX_VALUE));
	}

	@SubscribeEvent
	public static void onLivingFall(LivingFallEvent event) {
		if (event.getEntity().hasEffect(SZEffects.CUSHION) && event.getDistance() > 3.0F) {
			MobEffectInstance cushionEffect = event.getEntity().getEffect(SZEffects.CUSHION);

			event.setDamageMultiplier(Math.max(event.getDamageMultiplier() * (0.3F - cushionEffect.getAmplifier() * 0.2F), 0));
			event.getEntity().playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.5F);
		}
	}

	@SubscribeEvent
	public static void onKnockback(LivingKnockBackEvent event) {
		if (event.getEntity().hasEffect(SZEffects.CUSHION)) {
			MobEffectInstance cushionEffect = event.getEntity().getEffect(SZEffects.CUSHION);

			event.setStrength(Math.max(event.getOriginalStrength() * (0.3F - cushionEffect.getAmplifier() * 0.2F), 0));
			event.getEntity().playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.5F);
		}
	}
}
