package suszombification.renderer;

import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import suszombification.SuspiciousZombification;
import suszombification.entity.ZombifiedCow;

public class ZombifiedCowRenderer extends CowRenderer {
	private static final ResourceLocation COW_LOCATION = SuspiciousZombification.resLoc("textures/entity/zombified_cow.png");

	public ZombifiedCowRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(CowRenderState renderState) {
		return COW_LOCATION;
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
