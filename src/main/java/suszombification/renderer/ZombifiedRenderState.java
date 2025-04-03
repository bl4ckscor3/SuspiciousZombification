package suszombification.renderer;

import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.client.renderer.entity.state.SheepRenderState;

public class ZombifiedRenderState {
	public static class Cat extends CatRenderState {
		public boolean isConverting;
	}

	public static class Chicken extends ChickenRenderState {
		public boolean isConverting;
	}

	public static class Cow extends CowRenderState {
		public boolean isConverting;
	}

	public static class Horse extends EquineRenderState {
		public boolean isConverting;
	}

	public static class Pig extends PigRenderState {
		public boolean isConverting;
	}

	public static class Sheep extends SheepRenderState {
		public boolean isConverting;
	}
}
