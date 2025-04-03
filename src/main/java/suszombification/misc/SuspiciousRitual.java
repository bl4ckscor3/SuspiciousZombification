package suszombification.misc;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import suszombification.SZDamageSources;
import suszombification.SZTags;
import suszombification.entity.ZombifiedAnimal;
import suszombification.registration.SZEffects;

public final class SuspiciousRitual {
	private static final Predicate<BlockState> WOODEN_FENCE = state -> state.is(BlockTags.WOODEN_FENCES);
	private static final Predicate<BlockState> CHISELED_STONE_BRICKS = state -> state.is(Blocks.CHISELED_STONE_BRICKS);
	private static final Predicate<BlockState> ANY_OTHER_STONE_BRICKS = state -> state.is(BlockTags.STONE_BRICKS) && !CHISELED_STONE_BRICKS.test(state);
	private static final Predicate<BlockState> PUMPKIN = state -> state.is(Blocks.PUMPKIN);
	private static final Predicate<BlockState> REDSTONE_TORCH = state -> state.is(Blocks.REDSTONE_TORCH);
	private static final BiPredicate<BlockState, Direction> CARVED_PUMPKIN = (state, dir) -> state.is(Blocks.CARVED_PUMPKIN) && state.getValue(BlockStateProperties.HORIZONTAL_FACING) == dir;
	//@formatter:off
	private static final List<StructurePart> STRUCTURE_PARTS = List.of(
			new StructurePosition(0, 0, 0, WOODEN_FENCE),
			//technically part of the structure, but handled separately in isStructurePresent to allow ritual structures without a trophy to show the nighttime info text
			//new StructurePosition(0, 1, 0, state -> state.is(SZTags.Blocks.TROPHIES)),
			new StructurePosition(0, -1, 0, CHISELED_STONE_BRICKS),
			new StructureArea(-2, -1, -2, 2, -1, -1, ANY_OTHER_STONE_BRICKS),
			new StructureArea(-2, -1, 0, -1, -1, 0, ANY_OTHER_STONE_BRICKS),
			new StructureArea(1, -1, 0, 2, -1, 0, ANY_OTHER_STONE_BRICKS),
			new StructureArea(-2, -1, 1, 2, -1, 2, ANY_OTHER_STONE_BRICKS),
			new StructurePosition(-2, 0, -2, PUMPKIN),
			new StructurePosition(-2, 0, 2, PUMPKIN),
			new StructurePosition(2, 0, -2, PUMPKIN),
			new StructurePosition(2, 0, 2, PUMPKIN),
			new StructurePosition(-2, 1, -2, REDSTONE_TORCH),
			new StructurePosition(-2, 1, 2, REDSTONE_TORCH),
			new StructurePosition(2, 1, -2, REDSTONE_TORCH),
			new StructurePosition(2, 1, 2, REDSTONE_TORCH),
			new StructurePosition(-3, 0, 0, CHISELED_STONE_BRICKS),
			new StructurePosition(3, 0, 0, CHISELED_STONE_BRICKS),
			new StructurePosition(0, 0, -3, CHISELED_STONE_BRICKS),
			new StructurePosition(0, 0, 3, CHISELED_STONE_BRICKS),
			new StructurePosition(-3, 1, 0, state -> CARVED_PUMPKIN.test(state, Direction.EAST)),
			new StructurePosition(3, 1, 0, state -> CARVED_PUMPKIN.test(state, Direction.WEST)),
			new StructurePosition(0, 1, -3, state -> CARVED_PUMPKIN.test(state, Direction.SOUTH)),
			new StructurePosition(0, 1, 3, state -> CARVED_PUMPKIN.test(state, Direction.NORTH))
			);
	//@formatter:on

	/**
	 * Checks if the block structure of the suspicious ritual is built correctly at the given position.
	 *
	 * @param level The level the structure is built in
	 * @param structureOrigin The origin block position of the structure, in this case the position of the fence block in the
	 *            middle
	 * @param includeTrophy Whether to check for the trophy placed on top of the fence
	 * @return true if the structure has been built correctly, false otherwise
	 */
	public static boolean isStructurePresent(Level level, BlockPos structureOrigin, boolean includeTrophy) {
		boolean isStructurePresent = STRUCTURE_PARTS.stream().allMatch(part -> part.checkPart(level, structureOrigin));

		if (includeTrophy)
			isStructurePresent &= level.getBlockState(structureOrigin.above()).is(SZTags.Blocks.TROPHIES);

		return isStructurePresent;
	}

	/**
	 * Tries to perform the ritual for the given player.
	 *
	 * @param level The current level
	 * @param player The player to perform the ritual for
	 * @return true if the ritual was performed successfully, false otherwise
	 */
	public static boolean performRitual(Level level, Player player) {
		if (level.isDarkOutside() || player.getAbilities().instabuild) { //allow players in creative mode to bypass the night restriction
			Optional<Animal> potentialSacrifice = level.getEntitiesOfClass(Animal.class, new AABB(player.position(), player.position()).inflate(3), ZombifiedAnimal.class::isInstance).stream().filter(SuspiciousRitual::isGoodSacrifice).findFirst();

			if (potentialSacrifice.isPresent()) {
				Animal sacrifice = potentialSacrifice.get();
				Entity leashHolder = sacrifice.getLeashHolder();
				BlockPos ritualOrigin = leashHolder.blockPosition();

				//the player is within the ritual structure and also not under it
				if (player.distanceTo(leashHolder) <= 3.0F && Math.floor(player.position().y) >= ritualOrigin.getY()) {
					sacrifice.hurt(SZDamageSources.ritualSacrifice(player, level.registryAccess()), Float.MAX_VALUE);
					leashHolder.remove(RemovalReason.DISCARDED);
					level.removeBlock(ritualOrigin.above(), false); //remove the trophy
					player.removeAllEffects();
					player.addEffect(new MobEffectInstance(SZEffects.ZOMBIES_GRACE, 24000, 0, false, false, true));
					level.playSound(null, ritualOrigin, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.NEUTRAL, 1.0F, 1.0F);
					level.playSound(null, ritualOrigin, SoundEvents.ZOMBIE_AMBIENT, SoundSource.NEUTRAL, 2.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks if the given animal is an eligible sacrifice for use in the ritual
	 *
	 * @param animal The animal to check
	 * @return true if the given animal can be used in the ritual, false otherwise
	 */
	public static boolean isGoodSacrifice(Animal animal) {
		if (!animal.isLeashed())
			return false;

		//the animal is leashed to a fence
		if (!(animal.getLeashHolder() instanceof LeashFenceKnotEntity leashKnot))
			return false;

		//the animal is within the ritual structure
		if (animal.distanceTo(leashKnot) > 3.0F)
			return false;

		//the animal should not be under the ritual
		if (Math.floor(animal.position().y) < Math.floor(leashKnot.position().y))
			return false;

		//the animal needs to be leashed to a fence that is part of a correctly built ritual structure
		return isStructurePresent(animal.level(), leashKnot.blockPosition(), true);
	}

	public static void maybeSendInfoMessages(Leashable leashedMob, Level level, BlockPos pos, Player player) {
		if (!level.isClientSide && (leashedMob != null || !level.isDarkOutside())) {
			BlockState state = level.getBlockState(pos);

			if (state.is(BlockTags.WOODEN_FENCES) && isStructurePresent(level, pos, false)) {
				if (!(leashedMob instanceof ZombifiedAnimal))
					player.displayClientMessage(Component.translatable("message.suszombification.ritual.need_zombified_animal"), true);
				else if (!level.isDarkOutside())
					player.displayClientMessage(Component.translatable("message.suszombification.ritual.need_night"), true);
			}
		}
	}

	private static interface StructurePart {
		public boolean checkPart(Level level, BlockPos structureOrigin);
	}

	private static record StructurePosition(int x, int y, int z, Predicate<BlockState> check) implements StructurePart {
		@Override
		public boolean checkPart(Level level, BlockPos structureOrigin) {
			return check.test(level.getBlockState(structureOrigin.offset(x, y, z)));
		}
	}

	private static record StructureArea(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, Predicate<BlockState> check) implements StructurePart {
		@Override
		public boolean checkPart(Level level, BlockPos structureOrigin) {
			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
					for (int z = minZ; z <= maxZ; z++) {
						if (!check.test(level.getBlockState(structureOrigin.offset(x, y, z))))
							return false;
					}
				}
			}

			return true;
		}
	}
}
