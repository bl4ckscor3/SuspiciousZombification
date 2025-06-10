package suszombification.renderer;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.ColdChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.ChickenVariant;
import suszombification.entity.ZombifiedChicken;
import suszombification.renderer.layers.ZombifiedChickenZombieLayer;

public class ZombifiedChickenRenderer extends MobRenderer<ZombifiedChicken, ZombifiedRenderState.Chicken, ChickenModel> {
	private final Map<ChickenVariant.ModelType, AdultAndBabyModelPair<ChickenModel>> models;

	public ZombifiedChickenRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new ChickenModel(ctx.bakeLayer(ModelLayers.CHICKEN)), 0.3F);
		addLayer(new ZombifiedChickenZombieLayer(this, ctx.getModelSet()));
		//@formatter:off
		models = Maps.newEnumMap(Map.of(
				ChickenVariant.ModelType.NORMAL, new AdultAndBabyModelPair<>(new ChickenModel(ctx.bakeLayer(ModelLayers.CHICKEN)), new ChickenModel(ctx.bakeLayer(ModelLayers.CHICKEN_BABY))),
				ChickenVariant.ModelType.COLD, new AdultAndBabyModelPair<>(new ColdChickenModel(ctx.bakeLayer(ModelLayers.COLD_CHICKEN)), new ColdChickenModel(ctx.bakeLayer(ModelLayers.COLD_CHICKEN_BABY)))
		));
		//@formatter:on
	}

	public void render(ZombifiedRenderState.Chicken renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		if (renderState.variant != null) {
			model = models.get(renderState.variant.modelAndTexture().model()).getModel(renderState.isBaby);
			super.render(renderState, poseStack, buffer, packedLight);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(ZombifiedRenderState.Chicken renderState) {
		return renderState.variant == null ? MissingTextureAtlasSprite.getLocation() : renderState.variant.modelAndTexture().asset().texturePath();
	}

	@Override
	public ZombifiedRenderState.Chicken createRenderState() {
		return new ZombifiedRenderState.Chicken();
	}

	@Override
	public void extractRenderState(ZombifiedChicken chicken, ZombifiedRenderState.Chicken renderState, float partialTicks) {
		super.extractRenderState(chicken, renderState, partialTicks);
		renderState.isConverting = chicken.isConverting();
		renderState.flap = Mth.lerp(partialTicks, chicken.getPreviousFlap(), chicken.getFlap());
		renderState.flapSpeed = Mth.lerp(partialTicks, chicken.getPreviousFlapSpeed(), chicken.getFlapSpeed());
		renderState.variant = chicken.getVariant().value();
	}

	@Override
	protected boolean isShaking(ZombifiedRenderState.Chicken renderState) {
		return super.isShaking(renderState) || renderState.isConverting;
	}
}
