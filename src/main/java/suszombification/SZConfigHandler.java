package suszombification;

import org.apache.commons.lang3.tuple.Pair;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;

public class SZConfigHandler {
	public static final ModConfigSpec SERVER_SPEC;
	public static final SZConfigHandler INSTANCE;
	public final BooleanValue enableAnimalZombification;
	public final BooleanValue enableCandyMorningGifts;
	public final BooleanValue enableZombiesCurseZombification;

	static {
		Pair<SZConfigHandler, ModConfigSpec> serverSpecPair = new ModConfigSpec.Builder().configure(SZConfigHandler::new);

		SERVER_SPEC = serverSpecPair.getRight();
		INSTANCE = serverSpecPair.getLeft();
	}

	private SZConfigHandler(ModConfigSpec.Builder builder) {
		//@formatter:off
		enableAnimalZombification = builder
				.comment("Set this to false to disable the feature of zombified animals being able to zombify its prey on Normal and Hard difficulty.")
				.define("enable_animal_zombification", true);
		enableCandyMorningGifts = builder
				.comment("Set this to false to disable the feature of cats being able to gift players candy.")
				.define("enable_candy_morning_gift", true);
		enableZombiesCurseZombification = builder
				.comment("Set this to false to disable the feature of a player with the Zombie's Curse effect converting every animal in range to its zombified variant.")
				.define("enable_zombies_curse_zombification", true);
		//@formatter:on
	}
}
