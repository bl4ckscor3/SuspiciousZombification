package suszombification.misc;

import java.util.function.Predicate;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.event.EventHooks;
import suszombification.entity.ZombifiedAnimal;
import suszombification.item.ItemStackComponent;
import suszombification.item.SuspiciousPumpkinPieItem;
import suszombification.registration.SZDataComponents;
import suszombification.registration.SZItems;

public class AnimalUtil {
	private AnimalUtil() {}

	public static void tick(LivingEntity me) {
		if (!me.level().isClientSide() && me.isAlive()) {
			ZombifiedAnimal zombifiedAnimal = (ZombifiedAnimal) me;

			if (zombifiedAnimal.isConverting()) {
				zombifiedAnimal.setConversionTime(zombifiedAnimal.getConversionTime() - zombifiedAnimal.getConversionProgress());

				if (zombifiedAnimal.getConversionTime() <= 0 && EventHooks.canLivingConvert(me, zombifiedAnimal.getNormalVariant(), zombifiedAnimal::setConversionTime))
					zombifiedAnimal.finishConversion((ServerLevel) me.level());
			}
		}
	}

	public static InteractionResult mobInteract(LivingEntity me, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get()) && SuspiciousPumpkinPieItem.hasIngredient(stack, Items.GOLDEN_APPLE)) {
			if (me.hasEffect(MobEffects.WEAKNESS)) {
				if (!player.getAbilities().instabuild)
					stack.shrink(1);

				if (!me.level().isClientSide())
					((ZombifiedAnimal) me).startConverting(me.getRandom().nextInt(2401) + 3600);

				me.level().gameEvent(me, GameEvent.ENTITY_INTERACT, me.getEyePosition());
				return InteractionResult.SUCCESS;
			}

			return InteractionResult.CONSUME;
		}

		return InteractionResult.PASS;
	}

	public static boolean handleEntityEvent(LivingEntity me, byte id) {
		if (id == EntityEvent.ZOMBIE_CONVERTING) {
			if (!me.isSilent())
				me.level().playLocalSound(me.position().x, me.getEyeY(), me.position().z, SoundEvents.ZOMBIE_VILLAGER_CURE, me.getSoundSource(), 1.0F + me.getRandom().nextFloat(), me.getRandom().nextFloat() * 0.7F + 0.3F, false);

			return true;
		}

		return false;
	}

	public static boolean isFood(ItemStack stack, Ingredient foodItems) {
		return isFood(stack, foodItems, $ -> true);
	}

	public static boolean isFood(ItemStack stack, Ingredient foodItems, Predicate<ItemStack> extraTest) {
		if (stack.is(SZItems.SUSPICIOUS_PUMPKIN_PIE.get())) {
			ItemStackComponent ingredient = stack.get(SZDataComponents.INGREDIENT);

			if (ingredient == null)
				return false;

			ItemStack template = ingredient.stack().create();

			return foodItems.test(template) || extraTest.test(template);
		}

		return false;
	}
}
