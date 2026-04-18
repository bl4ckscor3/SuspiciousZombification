package suszombification.item;

import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;

public record ItemStackComponent(ItemStackTemplate stack) {
	//@formatter:off
	public static final Codec<ItemStackComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(ItemStackTemplate.CODEC.fieldOf("stack").forGetter(ItemStackComponent::stack))
			.apply(instance, stack -> new ItemStackComponent(new ItemStackTemplate(stack.item(), 1, stack.components()))));
	public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackComponent> STREAM_CODEC = StreamCodec.composite(
			ItemStackTemplate.STREAM_CODEC, ItemStackComponent::stack,
			ItemStackComponent::new);
	//@formatter:on

	public boolean is(Item item) {
		return stack.is(item);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof ItemStackComponent(ItemStackTemplate check) && isSameItemSameComponents(stack, check);
	}

	@Override
	public int hashCode() {
		return hashItemAndComponents(stack);
	}

	private static boolean isSameItemSameComponents(ItemStackTemplate a, ItemStackTemplate b) {
		return a.item() == b.item() && Objects.equals(a.components(), b.components());
	}

	private static int hashItemAndComponents(ItemStackTemplate stack) {
		if (stack != null) {
			int result = 31 + stack.item().hashCode();

			return 31 * result + stack.components().hashCode();
		}
		else
			return 0;
	}
}
