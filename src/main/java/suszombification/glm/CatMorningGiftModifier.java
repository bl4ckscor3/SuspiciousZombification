package suszombification.glm;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import suszombification.SZConfigHandler;
import suszombification.registration.SZLoot;

public class CatMorningGiftModifier extends LootModifier {
	public static final MapCodec<CatMorningGiftModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> codecStart(instance).apply(instance, CatMorningGiftModifier::new));

	public CatMorningGiftModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (!SZConfigHandler.INSTANCE.enableCandyMorningGifts.get())
			return generatedLoot;

		return context.getLevel().getServer().reloadableRegistries().getLootTable(SZLoot.ZOMBIFIED_CAT_MORNING_GIFT).getRandomItems(context);
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
}
