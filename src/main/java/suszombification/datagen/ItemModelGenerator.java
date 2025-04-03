package suszombification.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import suszombification.SuspiciousZombification;
import suszombification.registration.SZItems;

public class ItemModelGenerator extends ItemModelProvider {
	public ItemModelGenerator(PackOutput output) {
		super(output, SuspiciousZombification.MODID);
	}

	@Override
	protected void registerModels() {
		for (DeferredHolder<Item, ? extends Item> holder : SZItems.ITEMS.getEntries()) {
			flatItem(holder.get());
		}

		//@formatter:off
		flatItem(SZItems.HONEY_CANDY.get())
		.transforms() //same transforms as item/handheld, but with a lower thirdperson scaling
		.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0.0F, -90.0F, 55.0F).translation(0.0F, 4.0F, 0.5F).scale(0.66F).end()
		.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0.0F, 90.0F, -55.0F).translation(0.0F, 4.0F, 0.5F).scale(0.66F).end()
		.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0.0F, -90.0F, 25.0F).translation(1.13F, 3.2F, 1.13F).scale(0.68F).end()
		.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0.0F, 90.0F, -25.0F).translation(1.13F, 3.2F, 1.13F).scale(0.68F).end()
		.end();
		//@formatter:on
		handheldRodItem(SZItems.PORKCHOP_ON_A_STICK.get());
	}

	private ItemModelBuilder flatItem(Item item) {
		String name = BuiltInRegistries.ITEM.getKey(item).getPath();

		return getBuilder(name).parent(new UncheckedModelFile("item/generated")).texture("layer0", SuspiciousZombification.resLoc("item/" + name));
	}

	private void handheldRodItem(Item item) {
		String name = BuiltInRegistries.ITEM.getKey(item).getPath();

		getBuilder(name).parent(new UncheckedModelFile("item/handheld_rod")).texture("layer0", SuspiciousZombification.resLoc("item/" + name));
	}
}
