package suszombification.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.Vec3;
import suszombification.block.entity.TrophyBlockEntity;

public class TrophyRenderer implements BlockEntityRenderer<TrophyBlockEntity, TrophyRenderState> {
	private final ItemModelResolver itemModelResolver;

	public TrophyRenderer(BlockEntityRendererProvider.Context ctx) {
		itemModelResolver = ctx.itemModelResolver();
	}

	@Override
	public void submit(TrophyRenderState state, PoseStack pose, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		pose.pushPose();
		pose.translate(0.5D, 0.6D, 0.5D);
		pose.scale(0.7F, 0.7F, 0.7F);
		pose.mulPose(state.rotation);
		state.item.submit(pose, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		pose.popPose();
	}

	@Override
	public TrophyRenderState createRenderState() {
		return new TrophyRenderState();
	}

	@Override
	public void extractRenderState(TrophyBlockEntity be, TrophyRenderState state, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
		Direction direction = be.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
		int additionalRotation = direction.getAxis() == Direction.Axis.X ? 180 : 0; //fixes item being mirrored when the trophy is placed facing on the X axis

		state.rotation = Axis.YP.rotationDegrees(direction.toYRot() + additionalRotation);
		itemModelResolver.updateForTopItem(state.item, be.getTrophyType().displayItem, ItemDisplayContext.GROUND, be.getLevel(), null, (int) be.getBlockPos().asLong());
		BlockEntityRenderer.super.extractRenderState(be, state, partialTick, cameraPosition, breakProgress);

	}
}
