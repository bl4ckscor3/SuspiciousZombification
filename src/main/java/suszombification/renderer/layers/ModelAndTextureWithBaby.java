package suszombification.renderer.layers;

import net.minecraft.core.ClientAsset;

public record ModelAndTextureWithBaby<T>(T model, T babyModel, ClientAsset asset) {}