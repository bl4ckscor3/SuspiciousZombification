package suszombification;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class SZConfig {
	public static final ForgeConfigSpec SERVER_SPEC;
	public static final SZConfig INSTANCE;
	public final BooleanValue animalZombification;
	public final BooleanValue candyMorningGifts;
	public final BooleanValue zombiesCurseZombification;

	static {
		Pair<SZConfig, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(SZConfig::new);

		SERVER_SPEC = serverSpecPair.getRight();
		INSTANCE = serverSpecPair.getLeft();
	}

	private SZConfig(ForgeConfigSpec.Builder builder) {
		//@formatter:off
		animalZombification = builder
				.comment("Set this to false to disable the feature of zombified animals being able to zombify its prey on Normal and Hard difficulty.")
				.define("animal_zombification", true);
		candyMorningGifts = builder
				.comment("Set this to false to disable the feature of cats being able to gift players candy.")
				.define("candy_morning_gifts", true);
		zombiesCurseZombification = builder
				.comment("Set this to false to disable the feature of a player with the Zombies' Curse effect converting every animal in range to its zombified variant.")
				.define("zombies_curse_zombification", true);
		//@formatter:on
	}
}
