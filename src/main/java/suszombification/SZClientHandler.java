package suszombification;

import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.ColdChickenModel;
import net.minecraft.client.model.ColdCowModel;
import net.minecraft.client.model.ColdPigModel;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.WarmCowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import suszombification.registration.SZBlockEntityTypes;
import suszombification.registration.SZEntityTypes;
import suszombification.renderer.TrophyRenderer;
import suszombification.renderer.ZombifiedCatRenderer;
import suszombification.renderer.ZombifiedChickenRenderer;
import suszombification.renderer.ZombifiedCowRenderer;
import suszombification.renderer.ZombifiedPigRenderer;
import suszombification.renderer.ZombifiedSheepRenderer;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class SZClientHandler {
	public static final ModelLayerLocation ZOMBIFIED_CAT_ZOMBIE_LAYER = new ModelLayerLocation(ResourceLocation.withDefaultNamespace("cat"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_COW_ZOMBIE_LAYER = new ModelLayerLocation(ResourceLocation.withDefaultNamespace("cow"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_COLD_COW_ZOMBIE_LAYER = new ModelLayerLocation(ResourceLocation.withDefaultNamespace("cold_cow"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_WARM_COW_ZOMBIE_LAYER = new ModelLayerLocation(ResourceLocation.withDefaultNamespace("warm_cow"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_PIG_ZOMBIE_LAYER = new ModelLayerLocation(ResourceLocation.withDefaultNamespace("pig"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_COLD_PIG_ZOMBIE_LAYER = new ModelLayerLocation(ResourceLocation.withDefaultNamespace("cold_pig"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_CHICKEN_ZOMBIE_LAYER = new ModelLayerLocation(ResourceLocation.withDefaultNamespace("chicken"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_COLD_CHICKEN_ZOMBIE_LAYER = new ModelLayerLocation(ResourceLocation.withDefaultNamespace("cold_chicken"), "zombie");

	private SZClientHandler() {}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SZEntityTypes.ZOMBIFIED_CAT.get(), ZombifiedCatRenderer::new);
		event.registerEntityRenderer(SZEntityTypes.ZOMBIFIED_CHICKEN.get(), ZombifiedChickenRenderer::new);
		event.registerEntityRenderer(SZEntityTypes.ZOMBIFIED_COW.get(), ZombifiedCowRenderer::new);
		event.registerEntityRenderer(SZEntityTypes.ZOMBIFIED_PIG.get(), ZombifiedPigRenderer::new);
		event.registerEntityRenderer(SZEntityTypes.ZOMBIFIED_SHEEP.get(), ZombifiedSheepRenderer::new);
		event.registerEntityRenderer(SZEntityTypes.ROTTEN_EGG.get(), ThrownItemRenderer::new);
		event.registerBlockEntityRenderer(SZBlockEntityTypes.TROPHY.get(), TrophyRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ZOMBIFIED_CAT_ZOMBIE_LAYER, () -> LayerDefinition.create(OcelotModel.createBodyMesh(new CubeDeformation(0.01F)), 64, 32).apply(CatModel.CAT_TRANSFORMER));
		event.registerLayerDefinition(ZOMBIFIED_COW_ZOMBIE_LAYER, CowModel::createBodyLayer);
		event.registerLayerDefinition(ZOMBIFIED_COLD_COW_ZOMBIE_LAYER, ColdCowModel::createBodyLayer);
		event.registerLayerDefinition(ZOMBIFIED_WARM_COW_ZOMBIE_LAYER, WarmCowModel::createBodyLayer);
		event.registerLayerDefinition(ZOMBIFIED_PIG_ZOMBIE_LAYER, () -> PigModel.createBodyLayer(CubeDeformation.NONE));
		event.registerLayerDefinition(ZOMBIFIED_COLD_PIG_ZOMBIE_LAYER, () -> ColdPigModel.createBodyLayer(CubeDeformation.NONE));
		event.registerLayerDefinition(ZOMBIFIED_CHICKEN_ZOMBIE_LAYER, ChickenModel::createBodyLayer);
		event.registerLayerDefinition(ZOMBIFIED_COLD_CHICKEN_ZOMBIE_LAYER, ColdChickenModel::createBodyLayer);
	}
}
