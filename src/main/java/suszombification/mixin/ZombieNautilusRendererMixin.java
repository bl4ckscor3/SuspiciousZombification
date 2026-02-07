package suszombification.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.animal.nautilus.NautilusModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.ZombieNautilusRenderer;
import net.minecraft.client.renderer.entity.state.NautilusRenderState;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
import suszombification.entity.ZombifiedAnimal;
import suszombification.renderer.ZombifiedRenderState;

@Mixin(ZombieNautilusRenderer.class)
public abstract class ZombieNautilusRendererMixin extends MobRenderer<ZombieNautilus, NautilusRenderState, NautilusModel> {
	public ZombieNautilusRendererMixin(EntityRendererProvider.Context ctx) {
		super(ctx, null, 0.7F);
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
