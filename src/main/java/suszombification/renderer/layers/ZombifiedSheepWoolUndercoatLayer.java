package suszombification.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import suszombification.SuspiciousZombification;

public class ZombifiedSheepWoolUndercoatLayer extends RenderLayer<SheepRenderState, SheepModel> {
	private static final Identifier SHEEP_WOOL_UNDERCOAT_LOCATION = SuspiciousZombification.resLoc("textures/entity/zombified_sheep/zombified_sheep_wool_undercoat.png");
	private final EntityModel<SheepRenderState> adultModel;

	public ZombifiedSheepWoolUndercoatLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet modelSet) {
		super(renderer);
		adultModel = new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_WOOL_UNDERCOAT));
	}

	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, SheepRenderState renderState, float yRot, float xRot) {
		if (!renderState.isInvisible && (renderState.isJebSheep || renderState.woolColor != DyeColor.WHITE))
			coloredCutoutModelCopyLayerRender(adultModel, SHEEP_WOOL_UNDERCOAT_LOCATION, poseStack, submitNodeCollector, packedLight, renderState, renderState.getWoolColor(), 3);
	}
}
