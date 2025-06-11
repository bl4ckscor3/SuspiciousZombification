package suszombification.renderer.layers;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.ColdPigModel;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.world.entity.animal.PigVariant.ModelType;
import net.minecraft.world.entity.variant.ModelAndTexture;
import suszombification.SZClientHandler;
import suszombification.SuspiciousZombification;

public class ZombifiedPigZombieLayer extends RenderLayer<PigRenderState, PigModel> {
	private final Map<ModelType, ModelAndTexture<PigModel>> models;

	public ZombifiedPigZombieLayer(RenderLayerParent<PigRenderState, PigModel> parentRenderer, EntityModelSet modelSet) {
		super(parentRenderer);
		//@formatter:off
		models = Maps.newEnumMap(Map.of(
				ModelType.NORMAL, new ModelAndTexture<>(new PigModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_PIG_ZOMBIE_LAYER)), SuspiciousZombification.resLoc("entity/zombified_pig/temperate_layer")),
				ModelType.COLD, new ModelAndTexture<>(new ColdPigModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_COLD_PIG_ZOMBIE_LAYER)), SuspiciousZombification.resLoc("entity/zombified_pig/cold_layer"))
		));
		//@formatter:on
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, PigRenderState renderState, float yRot, float xRot) {
		ModelAndTexture<PigModel> tam = models.get(renderState.variant.modelAndTexture().model());

		coloredCutoutModelCopyLayerRender(tam.model(), tam.asset().texturePath(), pose, buffer, packedLight, renderState, 0xFFFFFFFF);
	}
}
