package suszombification.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import suszombification.block.entity.TrophyBlockEntity;
import suszombification.registration.SZBlockEntityTypes;

public class TrophyBlock extends HorizontalDirectionalBlock implements EntityBlock {
	private static final VoxelShape SHAPE = Shapes.or(Block.box(6.0D, 7.0D, 6.0D, 10.0D, 8.0D, 10.0D), Block.box(4.0D, 0.0D, 4.0D, 12.0D, 1.0D, 12.0D), Block.box(5.0D, 1.0D, 5.0D, 11.0D, 2.0D, 11.0D), Block.box(7.0D, 2.0D, 7.0D, 9.0D, 7.0D, 9.0D));
	private final TrophyType trophyType;

	public TrophyBlock(TrophyType trophyType, Properties properties) {
		super(properties);

		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
		this.trophyType = trophyType;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TrophyBlockEntity(SZBlockEntityTypes.TROPHY.get(), pos, state, trophyType);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public enum TrophyType {
		CARROT(new ItemStackTemplate(Items.CARROT)),
		POTATO(new ItemStackTemplate(Items.POTATO)),
		IRON_INGOT(new ItemStackTemplate(Items.IRON_INGOT));

		private final ItemStackTemplate displayItem;

		TrophyType(ItemStackTemplate displayItem) {
			this.displayItem = displayItem;
		}

		public ItemStack displayItem() {
			return displayItem.create();
		}
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return null;
	}
}
