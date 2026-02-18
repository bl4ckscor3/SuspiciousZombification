package suszombification;

import java.util.Set;

import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.animal.nautilus.NautilusModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.model.monster.nautilus.ZombieNautilusCoralModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(Dist.CLIENT)
public class SZModelLayers {
	public static final MeshTransformer ZOMBIE_NAUTILUS_BABY_TRANSFORMER = new BabyModelTransform(false, 4.0F, 4.0F, Set.of());
	public static final ModelLayerLocation ZOMBIE_NAUTILUS_BABY = new ModelLayerLocation(SuspiciousZombification.resLoc("zombie_nautilus_baby"), "main");
	public static final ModelLayerLocation ZOMBIE_NAUTILUS_CORAL_BABY = new ModelLayerLocation(SuspiciousZombification.resLoc("zombie_nautilus_coral_baby"), "main");

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ZOMBIE_NAUTILUS_BABY, () -> NautilusModel.createBodyLayer().apply(ZOMBIE_NAUTILUS_BABY_TRANSFORMER));
		event.registerLayerDefinition(ZOMBIE_NAUTILUS_CORAL_BABY, () -> ZombieNautilusCoralModel.createBodyLayer().apply(ZOMBIE_NAUTILUS_BABY_TRANSFORMER));
	}
}
