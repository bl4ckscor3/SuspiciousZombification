package suszombification.registration;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.storage.loot.LootTable;
import suszombification.SuspiciousZombification;

public class SZLoot {
	//gameplay
	public static final ResourceKey<LootTable> DEATH_BY_DECOMPOSING = create("gameplay/death_by_decomposing");
	public static final ResourceKey<LootTable> ZOMBIFIED_CAT_MORNING_GIFT = create("gameplay/zombified_cat_morning_gift");
	public static final ResourceKey<LootTable> ZOMBIFIED_CHICKEN_LAY = create("gameplay/zombified_chicken_lay");
	//entities
	public static final Map<DyeColor, ResourceKey<LootTable>> ZOMBIFIED_SHEEP_BY_DYE = Util.make(new EnumMap<>(DyeColor.class), map -> makeDyeKeyMap(map, "entities/zombified_sheep"));
	public static final ResourceKey<LootTable> SHEAR_ZOMBIFIED_SHEEP = create("shearing/zombified_sheep");
	public static final Map<DyeColor, ResourceKey<LootTable>> SHEAR_ZOMBIFIED_SHEEP_BY_DYE = Util.make(new EnumMap<>(DyeColor.class), map -> makeDyeKeyMap(map, "shearing/zombified_sheep"));
	//zombie cove
	public static final ResourceKey<LootTable> PEN_BARREL = create("zombie_cove/pen_barrel");
	public static final ResourceKey<LootTable> RITUAL_BARREL = create("zombie_cove/ritual_barrel");
	public static final ResourceKey<LootTable> TREASURE = create("zombie_cove/treasure");

	private SZLoot() {}

	private static void makeDyeKeyMap(EnumMap<DyeColor, ResourceKey<LootTable>> map, String path) {
		for (DyeColor dyecolor : DyeColor.values()) {
			map.put(dyecolor, create(path + "/" + dyecolor.getName()));
		}
	}

	private static ResourceKey<LootTable> create(String name) {
		return ResourceKey.create(Registries.LOOT_TABLE, SuspiciousZombification.resLoc(name));
	}
}
