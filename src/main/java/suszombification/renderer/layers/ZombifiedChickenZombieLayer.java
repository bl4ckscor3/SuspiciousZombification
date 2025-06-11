package suszombification.renderer.layers;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.ColdChickenModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.animal.ChickenVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import suszombification.SZClientHandler;
import suszombification.SuspiciousZombification;
import suszombification.renderer.ZombifiedRenderState;

public class ZombifiedChickenZombieLayer extends RenderLayer<ZombifiedRenderState.Chicken, ChickenModel> {
	private final Map<ChickenVariant.ModelType, ModelAndTexture<ChickenModel>> models;

	public ZombifiedChickenZombieLayer(RenderLayerParent<ZombifiedRenderState.Chicken, ChickenModel> parentRenderer, EntityModelSet modelSet) {
		super(parentRenderer);
		//@formatter:off
		models = Maps.newEnumMap(Map.of(
				ChickenVariant.ModelType.NORMAL, new ModelAndTexture<>(new ChickenModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_CHICKEN_ZOMBIE_LAYER)), SuspiciousZombification.resLoc("entity/zombified_chicken/temperate_layer")),
				ChickenVariant.ModelType.COLD, new ModelAndTexture<>(new ColdChickenModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_COLD_CHICKEN_ZOMBIE_LAYER)), SuspiciousZombification.resLoc("entity/zombified_chicken/cold_layer"))
		));
		//@formatter:on
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, ZombifiedRenderState.Chicken renderState, float yRot, float xRot) {
		ModelAndTexture<ChickenModel> tam = models.get(renderState.variant.modelAndTexture().model());

		coloredCutoutModelCopyLayerRender(tam.model(), tam.asset().texturePath(), pose, buffer, packedLight, renderState, 0xFFFFFFFF);
	}
}
