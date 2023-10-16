package net.elpasteltogrunon.journeyneymod.block.custom;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.elpasteltogrunon.journeyneymod.block.entity.CableBlockEntity;
import net.elpasteltogrunon.journeyneymod.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CableBlock extends Block implements EntityBlock, SimpleWaterloggedBlock
{ 
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final Property<Boolean> NORTH = BooleanProperty.create("north");
    public static final Property<Boolean> SOUTH = BooleanProperty.create("south");
    public static final Property<Boolean> UP = BooleanProperty.create("up");
    public static final Property<Boolean> DOWN = BooleanProperty.create("down");
    public static final Property<Boolean> WEST = BooleanProperty.create("west");
    public static final Property<Boolean> EAST = BooleanProperty.create("east");

    public static final Map<Direction, Property<Boolean>> propFromDir = new HashMap<Direction, Property<Boolean>>()
    {
        {
            put(Direction.DOWN, CableBlock.DOWN);
            put(Direction.UP, CableBlock.UP);
            put(Direction.NORTH, CableBlock.NORTH);
            put(Direction.SOUTH, CableBlock.SOUTH);
            put(Direction.EAST, CableBlock.EAST);
            put(Direction.WEST, CableBlock.WEST);
        }
    };

    public CableBlock(Properties properties) 
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(NORTH, Boolean.FALSE)
            .setValue(SOUTH, Boolean.FALSE)
            .setValue(UP, Boolean.FALSE)
            .setValue(DOWN, Boolean.FALSE)
            .setValue(WEST, Boolean.FALSE)
            .setValue(EAST, Boolean.FALSE));

    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(NORTH).add(SOUTH).add(UP).add(DOWN).add(WEST).add(EAST).add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) 
    {
        return defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) 
    {
        return new CableBlockEntity(pos, state);
    }


    //BLOCK ENTITY
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        if (level.getBlockEntity(pos) instanceof CableBlockEntity cableEntity)
        {
            return cableEntity.getShape();
        }
        return Shapes.block();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) 
    {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) 
    {
        return this.getShape(state, level, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) 
    {
        return this.getShape(state, level, pos, context);
    }


    public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) 
    {
        return 1.0F;
    }
  
     public boolean propagatesSkylightDown(BlockState p_48740_, BlockGetter p_48741_, BlockPos p_48742_) 
     {
        return true;
    }


    public FluidState getFluidState(BlockState p_56397_) {
      return (Boolean)p_56397_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_56397_);
   }

   public BlockState updateShape(BlockState p_56381_, Direction p_56382_, BlockState p_56383_, LevelAccessor p_56384_, BlockPos p_56385_, BlockPos p_56386_) {
      if ((Boolean)p_56381_.getValue(WATERLOGGED)) {
         p_56384_.scheduleTick(p_56385_, Fluids.WATER, Fluids.WATER.getTickDelay(p_56384_));
      }

      return super.updateShape(p_56381_, p_56382_, p_56383_, p_56384_, p_56385_, p_56386_);
   }
  

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) 
    {
        return createTickerHelper(type, ModBlockEntities.CABLE.get(), CableBlockEntity::tick);
    }


    //For debugging purposes
    /*@Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof CableBlockEntity) 
                ((CableBlockEntity) entity).sendMasterChatMessage();
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }*/

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) 
    {
        if (pState.getBlock() != pNewState.getBlock()) 
        {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof CableBlockEntity) 
                ((CableBlockEntity)blockEntity).removeFromNetwork(pPos);

        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
   protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actualType, BlockEntityType<E> targetType, BlockEntityTicker<? super E> ticker) {
      return targetType == actualType ? (BlockEntityTicker<A>)ticker : null;
   }

}
