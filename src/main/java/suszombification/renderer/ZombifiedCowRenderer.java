package suszombification.renderer;

import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.world.entity.animal.cow.Cow;
import suszombification.entity.ZombifiedCow;
import suszombification.renderer.layers.ZombifiedCowZombieLayer;

public class ZombifiedCowRenderer extends CowRenderer {
	public ZombifiedCowRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		addLayer(new ZombifiedCowZombieLayer(this, ctx.getModelSet()));
	}

	@Override
	public CowRenderState createRenderState() {
		return new ZombifiedRenderState.Cow();
	}

	@Override
	public void extractRenderState(Cow cow, CowRenderState renderState, float partialTicks) {
		super.extractRenderState(cow, renderState, partialTicks);
		((ZombifiedRenderState.Cow) renderState).isConverting = ((ZombifiedCow) cow).isConverting();
	}

	@Override
	protected boolean isShaking(CowRenderState renderState) {
		return super.isShaking(renderState) || ((ZombifiedRenderState.Cow) renderState).isConverting;
	}
}
