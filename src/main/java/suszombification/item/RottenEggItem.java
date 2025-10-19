package suszombification.item;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import suszombification.entity.ThrownRottenEgg;

public class RottenEggItem extends Item implements ProjectileItem {
	public RottenEggItem(Properties properties) {
		super(properties);

		DispenserBlock.registerProjectileBehavior(this);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

		if (!level.isClientSide()) {
			ThrownRottenEgg egg = new ThrownRottenEgg(level, player, getDefaultInstance());

			egg.setItem(stack);
			egg.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
			level.addFreshEntity(egg);
		}

		player.awardStat(Stats.ITEM_USED.get(this));

		if (!player.getAbilities().instabuild)
			stack.shrink(1);

		return InteractionResult.SUCCESS.heldItemTransformedTo(stack);
	}

	@Override
	public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
		ThrownRottenEgg egg = new ThrownRottenEgg(level, pos.x(), pos.y(), pos.z(), getDefaultInstance());

		egg.setItem(stack);
		return egg;
	}
}
