package suszombification.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.sheep.Sheep;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedSheep;
import suszombification.renderer.layers.ZombifiedSheepWoolLayer;
import suszombification.renderer.layers.ZombifiedSheepWoolUndercoatLayer;

public class ZombifiedSheepRenderer extends SheepRenderer {
	private static final ResourceLocation SHEEP_LOCATION = SuspiciousZombification.resLoc("textures/entity/zombified_sheep/zombified_sheep.png");

	public ZombifiedSheepRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		layers.clear();
		addLayer(new ZombifiedSheepWoolUndercoatLayer(this, ctx.getModelSet()));
		addLayer(new ZombifiedSheepWoolLayer(this, ctx.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(SheepRenderState renderState) {
		return SHEEP_LOCATION;
	}

	@Override
	public SheepRenderState createRenderState() {
		return new ZombifiedRenderState.Sheep();
	}

	@Override
	public void extractRenderState(Sheep sheep, SheepRenderState renderState, float partialTicks) {
		super.extractRenderState(sheep, renderState, partialTicks);
		((ZombifiedRenderState.Sheep) renderState).isConverting = ((ZombifiedSheep) sheep).isConverting();
	}

	@Override
	protected boolean isShaking(SheepRenderState renderState) {
		return super.isShaking(renderState) || ((ZombifiedRenderState.Sheep) renderState).isConverting;
	}
}
