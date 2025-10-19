package suszombification.renderer;

import org.joml.Quaternionf;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class TrophyRenderState extends BlockEntityRenderState {
	public ItemStackRenderState item = new ItemStackRenderState();
	public Quaternionf rotation;
}
