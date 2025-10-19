package suszombification.item;

import com.mojang.datafixers.util.Unit;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import suszombification.registration.SZDataComponents;
import suszombification.registration.SZEffects;

public class TrophyItem extends BlockItem {
	public TrophyItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
		if (level.getDifficulty() != Difficulty.PEACEFUL) {
			Player player = (Player) entity;

			if (!player.getAbilities().instabuild && !player.isSpectator() && !player.hasEffect(SZEffects.ZOMBIES_CURSE) && !stack.has(SZDataComponents.CURSE_GIVEN)) {
				stack.set(SZDataComponents.CURSE_GIVEN, Unit.INSTANCE);
				player.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 0.9F);
				player.playSound(SoundEvents.ZOMBIE_AMBIENT, 0.5F, 0.8F);
				player.addEffect(new MobEffectInstance(SZEffects.ZOMBIES_CURSE, -1));

				if (!level.isClientSide())
					player.displayClientMessage(Component.translatable("message.suszombification.curse.warning").withStyle(ChatFormatting.RED), false);
			}
		}
	}
}
