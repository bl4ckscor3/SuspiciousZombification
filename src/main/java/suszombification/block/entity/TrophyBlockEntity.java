package suszombification.block.entity;

import com.mojang.datafixers.util.Unit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import suszombification.block.TrophyBlock.TrophyType;
import suszombification.registration.SZBlockEntityTypes;
import suszombification.registration.SZDataComponents;

public class TrophyBlockEntity extends BlockEntity {
	private TrophyType trophyType;
	private boolean curseGiven;

	public TrophyBlockEntity(BlockPos pos, BlockState state) {
		super(SZBlockEntityTypes.TROPHY.get(), pos, state);
	}

	public TrophyBlockEntity(BlockEntityType<? extends TrophyBlockEntity> blockEntityType, BlockPos pos, BlockState state, TrophyType trophyType) {
		super(blockEntityType, pos, state);

		this.trophyType = trophyType;
	}

	public void setCurseGiven(boolean curseGiven) {
		this.curseGiven = curseGiven;
	}

	public boolean isCurseGiven() {
		return curseGiven;
	}

	public TrophyType getTrophyType() {
		return trophyType;
	}

	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		tag.putInt("TrophyType", trophyType.ordinal());
		tag.putBoolean("CurseGiven", curseGiven);
		super.saveAdditional(tag, lookupProvider);
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		super.loadAdditional(tag, lookupProvider);

		int savedOrdinal = tag.getIntOr("TrophyType", 0);

		if (savedOrdinal < 0 || savedOrdinal >= TrophyType.values().length)
			trophyType = TrophyType.CARROT;
		else
			trophyType = TrophyType.values()[savedOrdinal];

		curseGiven = tag.getBooleanOr("CurseGiven", false);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		return saveCustomOnly(lookupProvider);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter getter) {
		curseGiven = getter.get(SZDataComponents.CURSE_GIVEN) != null;
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		if (curseGiven)
			components.set(SZDataComponents.CURSE_GIVEN, Unit.INSTANCE);
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		tag.remove("CurseGiven");
	}
}
