package suszombification.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.animal.feline.CatModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import suszombification.SZClientHandler;
import suszombification.SuspiciousZombification;

public class ZombifiedCatZombieLayer extends RenderLayer<CatRenderState, CatModel> {
	private static final Identifier TEXTURE = new ClientAsset.ResourceTexture(SuspiciousZombification.resLoc("entity/zombified_cat_zombie_layer")).texturePath();
	private final CatModel model;
	private final CatModel babyModel;

	public ZombifiedCatZombieLayer(RenderLayerParent<CatRenderState, CatModel> parentRenderer, EntityModelSet modelSet) {
		super(parentRenderer);
		model = new CatModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_CAT_ZOMBIE_LAYER));
		babyModel = new CatModel(modelSet.bakeLayer(SZClientHandler.ZOMBIFIED_CAT_ZOMBIE_LAYER_BABY));
	}

	@Override
	public void submit(PoseStack pose, SubmitNodeCollector submitNodeCollector, int packedLight, CatRenderState renderState, float yRot, float xRot) {
		coloredCutoutModelCopyLayerRender(renderState.isBaby ? babyModel : model, TEXTURE, pose, submitNodeCollector, packedLight, renderState, -1, 2);
	}
}
