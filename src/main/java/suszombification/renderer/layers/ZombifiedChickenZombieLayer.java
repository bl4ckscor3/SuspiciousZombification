package suszombification.renderer.layers;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.ColdChickenModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.ChickenVariant;
import suszombification.SZClientHandler;
import suszombification.SuspiciousZombification;

public class ZombifiedChickenZombieLayer extends RenderLayer<ChickenRenderState, ChickenModel> {
	private final Map<ChickenVariant.ModelType, ModelAndTextureWithBaby<ChickenModel>> models;

	public ZombifiedChickenZombieLayer(RenderLayerParent<ChickenRenderState, ChickenModel> parentRenderer, EntityModelSet modelSet) {
		super(parentRenderer);
		//@formatter:off
		models = Maps.newEnumMap(Map.of(
				ChickenVariant.ModelType.NORMAL, new ModelAndTextureWithBaby<>(
						new ChickenModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_CHICKEN_ZOMBIE_LAYER)),
						new ChickenModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_CHICKEN_ZOMBIE_LAYER_BABY)),
						() -> new ClientAsset.ResourceTexture(SuspiciousZombification.resLoc("entity/zombified_chicken/temperate_layer")).texturePath()),
				ChickenVariant.ModelType.COLD, new ModelAndTextureWithBaby<>(
						new ColdChickenModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_COLD_CHICKEN_ZOMBIE_LAYER)),
						new ColdChickenModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_COLD_CHICKEN_ZOMBIE_LAYER_BABY)),
						() -> new ClientAsset.ResourceTexture(SuspiciousZombification.resLoc("entity/zombified_chicken/cold_layer")).texturePath())
		));
		//@formatter:on
	}

	@Override
	public void submit(PoseStack pose, SubmitNodeCollector submitNodeCollector, int packedLight, ChickenRenderState renderState, float yRot, float xRot) {
		ModelAndTextureWithBaby<ChickenModel> mat = models.get(renderState.variant.modelAndTexture().model());

		coloredCutoutModelCopyLayerRender(renderState.isBaby ? mat.babyModel() : mat.model(), mat.asset().id(), pose, submitNodeCollector, packedLight, renderState, -1, 1);
	}
}
