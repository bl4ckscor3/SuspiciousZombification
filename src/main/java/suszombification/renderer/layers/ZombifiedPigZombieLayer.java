package suszombification.renderer.layers;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.ColdPigModel;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.PigVariant.ModelType;
import suszombification.SZClientHandler;
import suszombification.SuspiciousZombification;

public class ZombifiedPigZombieLayer extends RenderLayer<PigRenderState, PigModel> {
	private final Map<ModelType, ModelAndTextureWithBaby<PigModel>> models;

	public ZombifiedPigZombieLayer(RenderLayerParent<PigRenderState, PigModel> parentRenderer, EntityModelSet modelSet) {
		super(parentRenderer);
		//@formatter:off
		models = Maps.newEnumMap(Map.of(
				ModelType.NORMAL, new ModelAndTextureWithBaby<>(
						new PigModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_PIG_ZOMBIE_LAYER)),
						new PigModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_PIG_ZOMBIE_LAYER_BABY)),
						() -> new ClientAsset.ResourceTexture(SuspiciousZombification.resLoc("entity/zombified_pig/temperate_layer")).texturePath()),
				ModelType.COLD, new ModelAndTextureWithBaby<>(
						new ColdPigModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_COLD_PIG_ZOMBIE_LAYER)),
						new ColdPigModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_COLD_PIG_ZOMBIE_LAYER_BABY)),
						() -> new ClientAsset.ResourceTexture(SuspiciousZombification.resLoc("entity/zombified_pig/cold_layer")).texturePath())
		));
		//@formatter:on
	}

	@Override
	public void submit(PoseStack pose, SubmitNodeCollector submitNodeCollector, int packedLight, PigRenderState renderState, float yRot, float xRot) {
		ModelAndTextureWithBaby<PigModel> mat = models.get(renderState.variant.modelAndTexture().model());

		coloredCutoutModelCopyLayerRender(renderState.isBaby ? mat.babyModel() : mat.model(), mat.asset().id(), pose, submitNodeCollector, packedLight, renderState, -1, renderState.outlineColor);
	}
}
