package suszombification.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import suszombification.SuspiciousZombification;

public class ZombifiedSheepWoolUndercoatLayer extends RenderLayer<SheepRenderState, SheepModel> {
	private static final ResourceLocation SHEEP_WOOL_UNDERCOAT_LOCATION = SuspiciousZombification.resLoc("textures/entity/zombified_sheep/zombified_sheep_wool_undercoat.png");
	private final EntityModel<SheepRenderState> adultModel;
	private final EntityModel<SheepRenderState> babyModel;

	public ZombifiedSheepWoolUndercoatLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet modelSet) {
		super(renderer);
		adultModel = new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_WOOL_UNDERCOAT));
		babyModel = new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_BABY_WOOL_UNDERCOAT));
	}

	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, SheepRenderState renderState, float yRot, float xRot) {
		if (!renderState.isInvisible && (renderState.isJebSheep || renderState.woolColor != DyeColor.WHITE))
			coloredCutoutModelCopyLayerRender(renderState.isBaby ? babyModel : adultModel, SHEEP_WOOL_UNDERCOAT_LOCATION, poseStack, submitNodeCollector, packedLight, renderState, renderState.getWoolColor(), 3);
	}
}
