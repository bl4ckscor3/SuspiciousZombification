package suszombification.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.model.animal.equine.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import suszombification.entity.ZombifiedAnimal;
import suszombification.renderer.ZombifiedRenderState;

@Mixin(UndeadHorseRenderer.class)
public abstract class UndeadHorseRendererMixin extends AbstractHorseRenderer<AbstractHorse, EquineRenderState, HorseModel> {
	public UndeadHorseRendererMixin(EntityRendererProvider.Context ctx, ModelLayerLocation layer, ModelLayerLocation babyLayer, boolean isSkeletonHorse) {
		super(ctx, null, null);
	}

	@Inject(method = "createRenderState", at = @At("HEAD"), cancellable = true)
	private void suszombification$overrideRenderState(CallbackInfoReturnable<EquineRenderState> cir) {
		cir.setReturnValue(new ZombifiedRenderState.Horse());
	}

	@Override
	public void extractRenderState(AbstractHorse horse, EquineRenderState renderState, float partialTicks) {
		super.extractRenderState(horse, renderState, partialTicks);
		((ZombifiedRenderState.Horse) renderState).isConverting = horse instanceof ZombifiedAnimal zombifiedAnimal && zombifiedAnimal.isConverting();
	}

	@Override
	protected boolean isShaking(EquineRenderState renderState) {
		return super.isShaking(renderState) || ((ZombifiedRenderState.Horse) renderState).isConverting;
	}
}
