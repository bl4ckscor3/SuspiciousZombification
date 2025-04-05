package suszombification.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.event.ForgeEventFactory;
import suszombification.SZDamageSources;
import suszombification.entity.ZombifiedAnimal;
import suszombification.registration.SZLoot;

public class DecomposingEffect extends MobEffect {
	public DecomposingEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (!entity.level().isClientSide) {
			if (entity instanceof Animal animal) {
				EntityType<? extends Animal> conversionType = ZombifiedAnimal.VANILLA_TO_ZOMBIFIED.get(animal.getType());

				if (conversionType != null && ForgeEventFactory.canLivingConvert(animal, conversionType, timer -> {})) {
					Mob convertedAnimal = animal.convertTo(conversionType, false);

                    if (convertedAnimal != null) {
				    	convertedAnimal.finalizeSpawn((ServerLevel) animal.level(), animal.level().getCurrentDifficultyAt(convertedAnimal.blockPosition()), MobSpawnType.CONVERSION, null, null);
			    		((ZombifiedAnimal) convertedAnimal).readFromVanilla(animal);
			    		ForgeEventFactory.onLivingConvert(animal, convertedAnimal);

				    	if (!animal.isSilent())
					    	animal.level().levelEvent(null, LevelEvent.SOUND_ZOMBIE_INFECTED, animal.blockPosition(), 0);
                    }
				}
				else {
					entity.hurt(SZDamageSources.decomposing(entity.level().registryAccess()), Float.MAX_VALUE);

					if (entity.isDeadOrDying())
						spawnDecomposingDrops(entity);
				}
			}
			else if (entity instanceof Player player && !player.getAbilities().instabuild) {
				entity.hurt(SZDamageSources.decomposing(entity.level().registryAccess()), Float.MAX_VALUE);
				spawnDecomposingDrops(entity);
			}
		}
	}

	private void spawnDecomposingDrops(LivingEntity entity) {
		LootTable lootTable = entity.level().getServer().getLootData().getLootTable(SZLoot.DEATH_BY_DECOMPOSING);
		//@formatter:off
		LootParams lootParams = new LootParams.Builder((ServerLevel) entity.level())
				.withParameter(LootContextParams.THIS_ENTITY, entity)
				.withParameter(LootContextParams.ORIGIN, entity.position())
				.withOptionalParameter(LootContextParams.DAMAGE_SOURCE, SZDamageSources.decomposing(entity.level().registryAccess()))
				.create(LootContextParamSets.ENTITY);
		//@formatter:on

		lootTable.getRandomItems(lootParams).forEach(entity::spawnAtLocation);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration == 1;
	}
}
