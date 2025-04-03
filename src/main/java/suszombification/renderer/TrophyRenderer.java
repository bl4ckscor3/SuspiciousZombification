package suszombification.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.Vec3;
import suszombification.block.entity.TrophyBlockEntity;

public class TrophyRenderer implements BlockEntityRenderer<TrophyBlockEntity> {
	public TrophyRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(TrophyBlockEntity be, float partialTick, PoseStack pose, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 cameraPos) {
		ItemStack stackToRender = be.getTrophyType().displayItem;
		Direction direction = be.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
		int additionalRotation = direction.getAxis() == Direction.Axis.X ? 180 : 0; //fixes item being mirrored when the trophy is placed facing on the X axis

		pose.pushPose();
		pose.translate(0.5D, 0.6D, 0.5D);
		pose.scale(0.7F, 0.7F, 0.7F);
		pose.mulPose(Axis.YP.rotationDegrees(direction.toYRot() + additionalRotation));
		Minecraft.getInstance().getItemRenderer().renderStatic(stackToRender, ItemDisplayContext.GROUND, packedLight, packedOverlay, pose, bufferSource, be.getLevel(), 0);
		pose.popPose();
	}
}
