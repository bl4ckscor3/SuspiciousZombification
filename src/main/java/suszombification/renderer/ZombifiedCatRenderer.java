package suszombification.renderer;

import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.world.entity.animal.feline.Cat;
import suszombification.entity.ZombifiedCat;
import suszombification.renderer.layers.ZombifiedCatZombieLayer;

public class ZombifiedCatRenderer extends CatRenderer {
	public ZombifiedCatRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		addLayer(new ZombifiedCatZombieLayer(this, ctx.getModelSet()));
	}

	@Override
	public CatRenderState createRenderState() {
		return new ZombifiedRenderState.Cat();
	}

	@Override
	public void extractRenderState(Cat cat, CatRenderState renderState, float partialTicks) {
		super.extractRenderState(cat, renderState, partialTicks);
		((ZombifiedRenderState.Cat) renderState).isConverting = ((ZombifiedCat) cat).isConverting();
	}

	@Override
	protected boolean isShaking(CatRenderState renderState) {
		return super.isShaking(renderState) || ((ZombifiedRenderState.Cat) renderState).isConverting;
	}
}
