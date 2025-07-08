package suszombification.renderer.layers;

import net.minecraft.core.ClientAsset;
import net.minecraft.resources.ResourceLocation;

public record ModelAndTextureWithBaby<T>(T model, T babyModel, ClientAsset asset) {
	public ModelAndTextureWithBaby(T model, T babyModel, ResourceLocation texture) {
		this(model, babyModel, new ClientAsset(texture));
	}
}