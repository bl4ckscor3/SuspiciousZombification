package suszombification;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.PillagerOutpostFeature;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import suszombification.entity.ZombifiedAnimal;
import suszombification.entity.ZombifiedChicken;
import suszombification.entity.ZombifiedCow;
import suszombification.entity.ZombifiedPig;
import suszombification.entity.ZombifiedSheep;
import suszombification.item.SuspiciousPumpkinPieItem;
import suszombification.misc.SuspiciousRitual;

@EventBusSubscriber(modid = SuspiciousZombification.MODID)
public class SZEventHandler {
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();

		if(entity instanceof PathfinderMob mob) {
			EntityType<?> type = mob.getType();

			if(type == EntityType.CHICKEN)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedChicken.class, 4.0F, 1.0F, 1.2F));
			else if(type == EntityType.COW)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedCow.class, 4.0F, 1.0F, 1.2F));
			else if(type == EntityType.PIG)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedPig.class, 4.0F, 1.0F, 1.2F));
			else if(type == EntityType.SHEEP)
				mob.goalSelector.addGoal(0, new AvoidEntityGoal<>(mob, ZombifiedSheep.class, 4.0F, 1.0F, 1.2F));
		}
	}

	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		Entity entity = event.getTarget();
		Player player = event.getPlayer();

		if(entity instanceof Animal animal && ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.containsKey(animal.getType())) {
			ItemStack stack = player.getItemInHand(event.getHand());

			if(stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && SuspiciousPumpkinPieItem.hasIngredient(stack, Items.ROTTEN_FLESH) && !animal.hasEffect(SZEffects.DECOMPOSING.get())) {
				animal.addEffect(new MobEffectInstance(SZEffects.DECOMPOSING.get(), TimeUtil.rangeOfSeconds(50, 70).sample(animal.getRandom())));

				if(!player.getAbilities().instabuild)
					stack.shrink(1);

				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.SUCCESS);
			}
		}
	}

	@SubscribeEvent
	public static void onRightclickBlock(PlayerInteractEvent.RightClickBlock event) {
		Level level = event.getWorld();

		if(!level.isClientSide) {
			BlockPos pos = event.getPos();
			BlockState state = level.getBlockState(pos);
			Player player = event.getPlayer();

			if(state.is(BlockTags.WOODEN_FENCES) && player.getItemInHand(event.getHand()).is(Items.LEAD) && !level.isNight() && SuspiciousRitual.isStructurePresent(level, pos))
				player.displayClientMessage(new TranslatableComponent("message.suszombification.ritual.need_night"), true);
		}
	}

	@SubscribeEvent
	public static void onEntityConvert(LivingConversionEvent.Pre event) {
		if(event.getEntityLiving() instanceof ZombifiedPig && event.getOutcome() == EntityType.ZOMBIFIED_PIGLIN)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onLivingSetAttackTarget(LivingSetAttackTargetEvent event) {
		if(event.getTarget() != null && event.getTarget().hasEffect(SZEffects.ZOMBIES_GRACE.get()) && event.getEntityLiving().getType().is(SZTags.EntityTypes.AFFECTED_BY_ZOMBIES_GRACE))
			((Mob)event.getEntityLiving()).setTarget(null);
	}

	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		LivingEntity livingEntity = event.getEntityLiving();
		Entity killer = event.getSource().getEntity();
		Level level = livingEntity.level;

		if(!level.isClientSide && (level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && killer instanceof ZombifiedAnimal zombifiedAnimal) {
			EntityType<? extends Mob> conversionType = (EntityType<? extends Mob>)killer.getType();

			if(livingEntity instanceof Animal killedEntity && killedEntity.getType() == zombifiedAnimal.getNormalVariant() && ForgeEventFactory.canLivingConvert(livingEntity, conversionType, timer -> {})) {
				if(level.getDifficulty() != Difficulty.HARD && level.random.nextBoolean())
					return;

				Mob convertedAnimal = killedEntity.convertTo(conversionType, false);

				convertedAnimal.finalizeSpawn((ServerLevel)level, level.getCurrentDifficultyAt(convertedAnimal.blockPosition()), MobSpawnType.CONVERSION, null, null);
				((ZombifiedAnimal)convertedAnimal).readFromVanilla(killedEntity);
				ForgeEventFactory.onLivingConvert(livingEntity, convertedAnimal);

				if(!killer.isSilent())
					level.levelEvent(null, LevelEvent.SOUND_ZOMBIE_INFECTED, killer.blockPosition(), 0);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingFall(LivingFallEvent event) {
		if(event.getEntityLiving().hasEffect(SZEffects.CUSHION.get()) && event.getDistance() > 3.0F) {
			MobEffectInstance cushionEffect = event.getEntityLiving().getEffect(SZEffects.CUSHION.get());

			event.setDamageMultiplier((Math.max(event.getDamageMultiplier() * (0.3F - cushionEffect.getAmplifier() * 0.2F), 0)));
			event.getEntityLiving().playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.5F);
		}
	}

	@SubscribeEvent
	public static void onKnockback(LivingKnockBackEvent event) {
		if(event.getEntityLiving().hasEffect(SZEffects.CUSHION.get())) {
			MobEffectInstance cushionEffect = event.getEntityLiving().getEffect(SZEffects.CUSHION.get());

			event.setStrength(Math.max(event.getOriginalStrength() * (0.3F - cushionEffect.getAmplifier() * 0.2F), 0));
			event.getEntityLiving().playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.5F);
		}
	}

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		if(event.getGeneration().getStructures().stream().anyMatch(csf -> csf.get().feature instanceof PillagerOutpostFeature)) {
			if(event.getCategory() == BiomeCategory.DESERT)
				event.getGeneration().addStructureStart(SZConfiguredStructures.DESERT_ZOMBIE_COVE);
			else
				event.getGeneration().addStructureStart(SZConfiguredStructures.ZOMBIE_COVE);
		}
	}
}
