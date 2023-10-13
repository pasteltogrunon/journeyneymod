package net.elpasteltogrunon.journeyneymod.block.custom;

import javax.annotation.Nullable;

import net.elpasteltogrunon.journeyneymod.block.entity.CableBlockEntity;
import net.elpasteltogrunon.journeyneymod.block.entity.EnergyBlockEntity;
import net.elpasteltogrunon.journeyneymod.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CableBlock extends BaseEntityBlock
{ 
    public CableBlock(Properties properties) 
    {
        super(properties);
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
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) 
    {
        return createTickerHelper(type, ModBlockEntities.CABLE.get(), CableBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof CableBlockEntity) 
                ((CableBlockEntity) entity).sendMasterChatMessage();
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

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

}
