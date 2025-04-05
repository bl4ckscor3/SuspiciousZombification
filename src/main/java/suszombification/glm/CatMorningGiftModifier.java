package suszombification.glm;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import suszombification.SZConfig;
import suszombification.registration.SZLoot;

public class CatMorningGiftModifier extends LootModifier {
	public static final Supplier<Codec<CatMorningGiftModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(instance -> codecStart(instance).apply(instance, CatMorningGiftModifier::new)));

	public CatMorningGiftModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (!SZConfig.INSTANCE.candyMorningGifts.get())
			return generatedLoot;

		return context.getLevel().getServer().getLootData().getLootTable(SZLoot.ZOMBIFIED_CAT_MORNING_GIFT).getRandomItems(context);
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}
}
