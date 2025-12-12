package suszombification.mixin;

import org.spongepowered.asm.mixin.Mixin;

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

	@Override
	public void extractRenderState(ZombieNautilus nautilus, NautilusRenderState renderState, float partialTicks) {
		super.extractRenderState(nautilus, renderState, partialTicks);
		((ZombifiedRenderState.Nautilus) renderState).isConverting = nautilus instanceof ZombifiedAnimal zombifiedAnimal && zombifiedAnimal.isConverting();
	}

	@Override
	protected boolean isShaking(NautilusRenderState renderState) {
		return super.isShaking(renderState) || ((ZombifiedRenderState.Nautilus) renderState).isConverting;
	}
}
