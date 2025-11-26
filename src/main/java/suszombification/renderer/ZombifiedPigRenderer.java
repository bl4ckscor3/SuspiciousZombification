package suszombification.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.world.entity.animal.pig.Pig;
import suszombification.entity.ZombifiedPig;
import suszombification.renderer.layers.ZombifiedPigZombieLayer;

public class ZombifiedPigRenderer extends PigRenderer {
	public ZombifiedPigRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		addLayer(new ZombifiedPigZombieLayer(this, ctx.getModelSet()));
	}

	@Override
	public PigRenderState createRenderState() {
		return new ZombifiedRenderState.Pig();
	}

	@Override
	public void extractRenderState(Pig pig, PigRenderState renderState, float partialTicks) {
		super.extractRenderState(pig, renderState, partialTicks);
		((ZombifiedRenderState.Pig) renderState).isConverting = ((ZombifiedPig) pig).isConverting();
	}

	@Override
	protected boolean isShaking(PigRenderState renderState) {
		return super.isShaking(renderState) || ((ZombifiedRenderState.Pig) renderState).isConverting;
	}
}
