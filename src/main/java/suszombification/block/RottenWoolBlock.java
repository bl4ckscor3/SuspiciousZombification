package suszombification.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RottenWoolBlock extends Block {
	public RottenWoolBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
		entity.causeFallDamage(fallDistance, 0.5F, entity.damageSources().fall());
		entity.playSound(SoundEvents.SLIME_SQUISH, 0.3F, 1.5F);
	}
}
