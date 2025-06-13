package suszombification.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.ResourceLocation;
import suszombification.SuspiciousZombification;

public class ZombifiedSheepWoolLayer extends RenderLayer<SheepRenderState, SheepModel> {
	private static final ResourceLocation SHEEP_WOOL_LOCATION = SuspiciousZombification.resLoc("textures/entity/zombified_sheep/zombified_sheep_wool.png");
	private final EntityModel<SheepRenderState> adultModel;
	private final EntityModel<SheepRenderState> babyModel;

	public ZombifiedSheepWoolLayer(RenderLayerParent<SheepRenderState, SheepModel> parent, EntityModelSet modelSet) {
		super(parent);
		this.adultModel = new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_WOOL));
		this.babyModel = new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_BABY_WOOL));
	}

	@Override
	public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, SheepRenderState renderState, float headYaw, float headPitch) {
		if (!renderState.isSheared) {
			EntityModel<SheepRenderState> model = renderState.isBaby ? babyModel : adultModel;

			if (renderState.isInvisible) {
				if (renderState.appearsGlowing) {
					VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.outline(SHEEP_WOOL_LOCATION));

					model.setupAnim(renderState);
					model.renderToBuffer(pose, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(renderState, 0.0F), -16777216);
				}
			}
			else
				coloredCutoutModelCopyLayerRender(model, SHEEP_WOOL_LOCATION, pose, buffer, packedLight, renderState, renderState.getWoolColor());
		}
	}
}
