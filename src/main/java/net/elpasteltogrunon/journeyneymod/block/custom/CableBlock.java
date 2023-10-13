package net.elpasteltogrunon.journeyneymod.block.custom;

import java.util.List;

import javax.annotation.Nullable;

import net.elpasteltogrunon.journeyneymod.block.entity.CableBlockEntity;
import net.elpasteltogrunon.journeyneymod.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CableBlock extends BaseEntityBlock
{ 
    public static final Property<Boolean> NORTH = BooleanProperty.create("north");
    public static final Property<Boolean> SOUTH = BooleanProperty.create("south");
    public static final Property<Boolean> UP = BooleanProperty.create("up");
    public static final Property<Boolean> DOWN = BooleanProperty.create("down");
    public static final Property<Boolean> WEST = BooleanProperty.create("west");
    public static final Property<Boolean> EAST = BooleanProperty.create("east");

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
        builder.add(NORTH).add(SOUTH).add(UP).add(DOWN).add(WEST).add(EAST);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) 
    {
        return new CableBlockEntity(pos, state);
    }


    //BLOCK ENTITY
    @Override
    public RenderShape getRenderShape(BlockState bState)
    {
        return RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        if (level.getBlockEntity(pos) instanceof CableBlockEntity cableEntity)
        {
            return cableEntity.getShape();
        }
        return Shapes.block();
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

    public void updateBlockState(List<Direction> connections)
    {
        for(Direction dir : connections)
        {
            
        }
    }

}
