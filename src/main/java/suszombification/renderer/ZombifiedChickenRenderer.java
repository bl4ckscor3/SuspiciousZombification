package suszombification.renderer;

import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.world.entity.animal.Chicken;
import suszombification.entity.ZombifiedChicken;
import suszombification.renderer.layers.ZombifiedChickenZombieLayer;

public class ZombifiedChickenRenderer extends ChickenRenderer {
	public ZombifiedChickenRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		addLayer(new ZombifiedChickenZombieLayer(this, ctx.getModelSet()));
	}

	@Override
	public ZombifiedRenderState.Chicken createRenderState() {
		return new ZombifiedRenderState.Chicken();
	}

	@Override
	public void extractRenderState(Chicken chicken, ChickenRenderState renderState, float partialTicks) {
		super.extractRenderState(chicken, renderState, partialTicks);
		((ZombifiedRenderState.Chicken) renderState).isConverting = ((ZombifiedChicken) chicken).isConverting();
	}

	@Override
	protected boolean isShaking(ChickenRenderState renderState) {
		return super.isShaking(renderState) || ((ZombifiedRenderState.Chicken) renderState).isConverting;
	}
}
