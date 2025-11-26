package suszombification;

import net.minecraft.client.model.animal.chicken.ChickenModel;
import net.minecraft.client.model.animal.chicken.ColdChickenModel;
import net.minecraft.client.model.animal.cow.ColdCowModel;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.animal.cow.WarmCowModel;
import net.minecraft.client.model.animal.feline.CatModel;
import net.minecraft.client.model.animal.feline.FelineModel;
import net.minecraft.client.model.animal.pig.ColdPigModel;
import net.minecraft.client.model.animal.pig.PigModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import suszombification.registration.SZBlockEntityTypes;
import suszombification.registration.SZEntityTypes;
import suszombification.renderer.TrophyRenderer;
import suszombification.renderer.ZombifiedCatRenderer;
import suszombification.renderer.ZombifiedChickenRenderer;
import suszombification.renderer.ZombifiedCowRenderer;
import suszombification.renderer.ZombifiedPigRenderer;
import suszombification.renderer.ZombifiedSheepRenderer;

@EventBusSubscriber(modid = SuspiciousZombification.MODID, value = Dist.CLIENT)
public class SZClientHandler {
	public static final ModelLayerLocation ZOMBIFIED_CAT_ZOMBIE_LAYER = new ModelLayerLocation(Identifier.withDefaultNamespace("cat"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_CAT_ZOMBIE_LAYER_BABY = new ModelLayerLocation(Identifier.withDefaultNamespace("cat_baby"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_COW_ZOMBIE_LAYER = new ModelLayerLocation(Identifier.withDefaultNamespace("cow"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_COW_ZOMBIE_LAYER_BABY = new ModelLayerLocation(Identifier.withDefaultNamespace("cow_baby"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_COLD_COW_ZOMBIE_LAYER = new ModelLayerLocation(Identifier.withDefaultNamespace("cold_cow"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_COLD_COW_ZOMBIE_LAYER_BABY = new ModelLayerLocation(Identifier.withDefaultNamespace("cold_cow_baby"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_WARM_COW_ZOMBIE_LAYER = new ModelLayerLocation(Identifier.withDefaultNamespace("warm_cow"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_WARM_COW_ZOMBIE_LAYER_BABY = new ModelLayerLocation(Identifier.withDefaultNamespace("warm_cow_baby"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_PIG_ZOMBIE_LAYER = new ModelLayerLocation(Identifier.withDefaultNamespace("pig"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_PIG_ZOMBIE_LAYER_BABY = new ModelLayerLocation(Identifier.withDefaultNamespace("pig_baby"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_COLD_PIG_ZOMBIE_LAYER = new ModelLayerLocation(Identifier.withDefaultNamespace("cold_pig"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_COLD_PIG_ZOMBIE_LAYER_BABY = new ModelLayerLocation(Identifier.withDefaultNamespace("cold_pig_baby"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_CHICKEN_ZOMBIE_LAYER = new ModelLayerLocation(Identifier.withDefaultNamespace("chicken"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_CHICKEN_ZOMBIE_LAYER_BABY = new ModelLayerLocation(Identifier.withDefaultNamespace("chicken_baby"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_COLD_CHICKEN_ZOMBIE_LAYER = new ModelLayerLocation(Identifier.withDefaultNamespace("cold_chicken"), "zombie");
	public static final ModelLayerLocation ZOMBIFIED_COLD_CHICKEN_ZOMBIE_LAYER_BABY = new ModelLayerLocation(Identifier.withDefaultNamespace("cold_chicken_baby"), "zombie");

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
		LayerDefinition cat = LayerDefinition.create(FelineModel.createBodyMesh(new CubeDeformation(0.01F)), 64, 32).apply(CatModel.CAT_TRANSFORMER);
		LayerDefinition cow = CowModel.createBodyLayer();
		LayerDefinition coldCow = ColdCowModel.createBodyLayer();
		LayerDefinition warmCow = WarmCowModel.createBodyLayer();
		LayerDefinition pig = PigModel.createBodyLayer(CubeDeformation.NONE);
		LayerDefinition coldPig = ColdPigModel.createBodyLayer(CubeDeformation.NONE);
		LayerDefinition chicken = ChickenModel.createBodyLayer();
		LayerDefinition coldChicken = ColdChickenModel.createBodyLayer();

		event.registerLayerDefinition(ZOMBIFIED_CAT_ZOMBIE_LAYER, () -> cat);
		event.registerLayerDefinition(ZOMBIFIED_CAT_ZOMBIE_LAYER_BABY, () -> cat.apply(FelineModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(ZOMBIFIED_COW_ZOMBIE_LAYER, () -> cow);
		event.registerLayerDefinition(ZOMBIFIED_COW_ZOMBIE_LAYER_BABY, () -> cow.apply(CowModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(ZOMBIFIED_COLD_COW_ZOMBIE_LAYER, () -> coldCow);
		event.registerLayerDefinition(ZOMBIFIED_COLD_COW_ZOMBIE_LAYER_BABY, () -> coldCow.apply(ColdCowModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(ZOMBIFIED_WARM_COW_ZOMBIE_LAYER, () -> warmCow);
		event.registerLayerDefinition(ZOMBIFIED_WARM_COW_ZOMBIE_LAYER_BABY, () -> warmCow.apply(WarmCowModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(ZOMBIFIED_PIG_ZOMBIE_LAYER, () -> pig);
		event.registerLayerDefinition(ZOMBIFIED_PIG_ZOMBIE_LAYER_BABY, () -> pig.apply(PigModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(ZOMBIFIED_COLD_PIG_ZOMBIE_LAYER, () -> coldPig);
		event.registerLayerDefinition(ZOMBIFIED_COLD_PIG_ZOMBIE_LAYER_BABY, () -> coldPig.apply(ColdPigModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(ZOMBIFIED_CHICKEN_ZOMBIE_LAYER, () -> chicken);
		event.registerLayerDefinition(ZOMBIFIED_CHICKEN_ZOMBIE_LAYER_BABY, () -> chicken.apply(ChickenModel.BABY_TRANSFORMER));
		event.registerLayerDefinition(ZOMBIFIED_COLD_CHICKEN_ZOMBIE_LAYER, () -> coldChicken);
		event.registerLayerDefinition(ZOMBIFIED_COLD_CHICKEN_ZOMBIE_LAYER_BABY, () -> coldChicken.apply(ColdChickenModel.BABY_TRANSFORMER));
	}
}
