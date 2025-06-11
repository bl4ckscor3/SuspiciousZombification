package suszombification.renderer.layers;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.ColdCowModel;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.WarmCowModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import suszombification.SZClientHandler;
import suszombification.SuspiciousZombification;

public class ZombifiedCowZombieLayer extends RenderLayer<CowRenderState, CowModel> {
	private final Map<CowVariant.ModelType, ModelAndTexture<CowModel>> models;

	public ZombifiedCowZombieLayer(RenderLayerParent<CowRenderState, CowModel> parentRenderer, EntityModelSet modelSet) {
		super(parentRenderer);
		//@formatter:off
		models = Maps.newEnumMap(Map.of(
				CowVariant.ModelType.NORMAL, new ModelAndTexture<>(new CowModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_COW_ZOMBIE_LAYER)), SuspiciousZombification.resLoc("entity/zombified_cow/temperate_layer")),
				CowVariant.ModelType.COLD, new ModelAndTexture<>(new ColdCowModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_COLD_COW_ZOMBIE_LAYER)), SuspiciousZombification.resLoc("entity/zombified_cow/cold_layer")),
				CowVariant.ModelType.WARM, new ModelAndTexture<>(new WarmCowModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_WARM_COW_ZOMBIE_LAYER)), SuspiciousZombification.resLoc("entity/zombified_cow/warm_layer"))
		));
		//@formatter:on
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, CowRenderState renderState, float yRot, float xRot) {
		ModelAndTexture<CowModel> tam = models.get(renderState.variant.modelAndTexture().model());

		coloredCutoutModelCopyLayerRender(tam.model(), tam.asset().texturePath(), pose, buffer, packedLight, renderState, 0xFFFFFFFF);
	}
}
