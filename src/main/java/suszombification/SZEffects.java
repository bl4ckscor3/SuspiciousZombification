package suszombification;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import suszombification.misc.DecomposingEffect;

public class SZEffects {
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SuspiciousZombification.MODID);

	public static final RegistryObject<MobEffect> DECOMPOSING = EFFECTS.register("decomposing", () -> new DecomposingEffect(MobEffectCategory.HARMFUL, 0x799C65).addAttributeModifier(Attributes.MOVEMENT_SPEED, "71c0aa55-a4ea-410d-8a42-99e9daa37ef5", -0.2F, Operation.MULTIPLY_TOTAL));
}