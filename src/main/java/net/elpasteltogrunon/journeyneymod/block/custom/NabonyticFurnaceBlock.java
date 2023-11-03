package net.elpasteltogrunon.journeyneymod.block.custom;

import javax.annotation.Nullable;

import net.elpasteltogrunon.journeyneymod.block.entity.EnergyBlockEntity;
import net.elpasteltogrunon.journeyneymod.block.entity.ModBlockEntities;
import net.elpasteltogrunon.journeyneymod.block.entity.NabonyticFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class NabonyticFurnaceBlock extends BaseEntityBlock
{
   public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public NabonyticFurnaceBlock(Properties properties) 
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) 
    {
        builder.add(LIT);
    }


    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NabonyticFurnaceBlockEntity(pos, state);
    }

    /*BLOCK ENTITY */
    @Override
    public RenderShape getRenderShape(BlockState bState)
    {
        return RenderShape.MODEL;
    }
    
    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) 
    {
        if (pState.getBlock() != pNewState.getBlock()) 
        {
            if (pLevel.getBlockEntity(pPos) instanceof EnergyBlockEntity pEntity) 
            {
                EnergyBlockEntity.addToNeighborCables(pLevel, pPos, pState, pEntity);
            }
        }

        super.onPlace(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) 
    {
        if (pState.getBlock() != pNewState.getBlock()) 
        {
            if (pLevel.getBlockEntity(pPos) instanceof NabonyticFurnaceBlockEntity furnaceEntity) 
            {
                furnaceEntity.drops();
                EnergyBlockEntity.removeFromNeighbourCables(pLevel, pPos, pNewState, furnaceEntity);
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) 
        {
            if(pLevel.getBlockEntity(pPos) instanceof NabonyticFurnaceBlockEntity furnaceEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), furnaceEntity, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.NABONYTIC_FURNACE.get(),
                NabonyticFurnaceBlockEntity::tick);
    }
}