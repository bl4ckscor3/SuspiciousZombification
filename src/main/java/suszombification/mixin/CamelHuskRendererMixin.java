package suszombification.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.model.animal.camel.CamelModel;
import net.minecraft.client.renderer.entity.CamelHuskRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.CamelRenderState;
import net.minecraft.world.entity.animal.camel.Camel;
import suszombification.entity.ZombifiedAnimal;
import suszombification.renderer.ZombifiedRenderState;

@Mixin(CamelHuskRenderer.class)
public abstract class CamelHuskRendererMixin extends MobRenderer<Camel, CamelRenderState, CamelModel> {
	public CamelHuskRendererMixin(EntityRendererProvider.Context context, CamelModel model, float shadow) {
		super(context, model, shadow);
	}

	@Override
	public CamelRenderState createRenderState() {
		return new ZombifiedRenderState.Camel();
	}

	@Override
	public void extractRenderState(Camel camel, CamelRenderState renderState, float partialTicks) {
		super.extractRenderState(camel, renderState, partialTicks);
		((ZombifiedRenderState.Camel) renderState).isConverting = camel instanceof ZombifiedAnimal zombifiedAnimal && zombifiedAnimal.isConverting();
	}

	@Override
	protected boolean isShaking(CamelRenderState renderState) {
		return super.isShaking(renderState) || ((ZombifiedRenderState.Camel) renderState).isConverting;
	}
}
