package suszombification.renderer.layers;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.animal.cow.ColdCowModel;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.animal.cow.WarmCowModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.cow.CowVariant;
import suszombification.SZClientHandler;
import suszombification.SuspiciousZombification;

public class ZombifiedCowZombieLayer extends RenderLayer<CowRenderState, CowModel> {
	private final Map<CowVariant.ModelType, ModelAndTextureWithBaby<CowModel>> models;

	public ZombifiedCowZombieLayer(RenderLayerParent<CowRenderState, CowModel> parentRenderer, EntityModelSet modelSet) {
		super(parentRenderer);
		//@formatter:off
		models = Maps.newEnumMap(Map.of(
				CowVariant.ModelType.NORMAL, new ModelAndTextureWithBaby<>(
						new CowModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_COW_ZOMBIE_LAYER)),
						new CowModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_COW_ZOMBIE_LAYER_BABY)),
						() -> new ClientAsset.ResourceTexture(SuspiciousZombification.resLoc("entity/zombified_cow/temperate_layer")).texturePath()),
				CowVariant.ModelType.COLD, new ModelAndTextureWithBaby<>(
						new ColdCowModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_COLD_COW_ZOMBIE_LAYER)),
						new ColdCowModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_COLD_COW_ZOMBIE_LAYER_BABY)),
						() -> new ClientAsset.ResourceTexture(SuspiciousZombification.resLoc("entity/zombified_cow/cold_layer")).texturePath()),
				CowVariant.ModelType.WARM, new ModelAndTextureWithBaby<>(
						new WarmCowModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_WARM_COW_ZOMBIE_LAYER)),
						new WarmCowModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_WARM_COW_ZOMBIE_LAYER_BABY)),
						() -> new ClientAsset.ResourceTexture(SuspiciousZombification.resLoc("entity/zombified_cow/warm_layer")).texturePath())
		));
		//@formatter:on
	}

	@Override
	public void submit(PoseStack pose, SubmitNodeCollector submitNodeCollector, int packedLight, CowRenderState renderState, float yRot, float xRot) {
		ModelAndTextureWithBaby<CowModel> mat = models.get(renderState.variant.modelAndTexture().model());

		coloredCutoutModelCopyLayerRender(renderState.isBaby ? mat.babyModel() : mat.model(), mat.asset().id(), pose, submitNodeCollector, packedLight, renderState, -1, 1);
	}
}
