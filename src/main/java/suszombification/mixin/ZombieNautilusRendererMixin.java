package suszombification.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.AdultAndBabyModelPair;
import net.minecraft.client.model.animal.nautilus.NautilusModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.nautilus.ZombieNautilusCoralModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.ZombieNautilusRenderer;
import net.minecraft.client.renderer.entity.state.NautilusRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
import suszombification.SZModelLayers;
import suszombification.entity.ZombifiedAnimal;
import suszombification.renderer.ZombifiedRenderState;

@Mixin(ZombieNautilusRenderer.class)
public abstract class ZombieNautilusRendererMixin extends MobRenderer<ZombieNautilus, NautilusRenderState, NautilusModel> {
	@Unique
	private Map<ZombieNautilusVariant.ModelType, AdultAndBabyModelPair<NautilusModel>> suszombification$models;

	public ZombieNautilusRendererMixin(EntityRendererProvider.Context ctx) {
		super(ctx, null, 0.7F);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void suszombification$setModels(EntityRendererProvider.Context ctx, CallbackInfo ci) {
		suszombification$models = suszombification$bakeModels(ctx);
	}

	@Unique
	private Map<ZombieNautilusVariant.ModelType, AdultAndBabyModelPair<NautilusModel>> suszombification$bakeModels(EntityRendererProvider.Context ctx) {
		return Maps.newEnumMap(
				Map.of(
						ZombieNautilusVariant.ModelType.NORMAL,
						new AdultAndBabyModelPair<>(
								new NautilusModel(ctx.bakeLayer(ModelLayers.ZOMBIE_NAUTILUS)),
								new NautilusModel(ctx.bakeLayer(SZModelLayers.ZOMBIE_NAUTILUS_BABY))
						),
						ZombieNautilusVariant.ModelType.WARM,
						new AdultAndBabyModelPair<>(
								new ZombieNautilusCoralModel(ctx.bakeLayer(ModelLayers.ZOMBIE_NAUTILUS_CORAL)),
								new ZombieNautilusCoralModel(ctx.bakeLayer(SZModelLayers.ZOMBIE_NAUTILUS_CORAL_BABY))
						)
				)
		);
	}

	@Override
	public void submit(NautilusRenderState state, PoseStack pose, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		if (state.variant != null) {
			model = suszombification$models.get(state.variant.modelAndTexture().model()).getModel(state.isBaby);
			super.submit(state, pose, submitNodeCollector, camera);
		}
	}

	@Override
	public NautilusRenderState createRenderState() {
		return new ZombifiedRenderState.Nautilus();
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/nautilus/ZombieNautilus;Lnet/minecraft/client/renderer/entity/state/NautilusRenderState;F)V", at = @At("TAIL"))
	public void extractRenderState(ZombieNautilus nautilus, NautilusRenderState renderState, float partialTicks, CallbackInfo ci) {
		((ZombifiedRenderState.Nautilus) renderState).isConverting = nautilus instanceof ZombifiedAnimal zombifiedAnimal && zombifiedAnimal.isConverting();
	}

	@Override
	protected boolean isShaking(NautilusRenderState renderState) {
		return super.isShaking(renderState) || ((ZombifiedRenderState.Nautilus) renderState).isConverting;
	}
}
