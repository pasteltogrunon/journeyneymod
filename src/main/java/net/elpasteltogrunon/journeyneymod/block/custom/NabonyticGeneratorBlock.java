package net.elpasteltogrunon.journeyneymod.block.custom;

import javax.annotation.Nullable;

import net.elpasteltogrunon.journeyneymod.block.entity.ModBlockEntities;
import net.elpasteltogrunon.journeyneymod.block.entity.NabonyticGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NabonyticGeneratorBlock extends BaseEntityBlock
{

    public NabonyticGeneratorBlock(Properties properties) 
    {
        super(properties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) 
    {
        return new NabonyticGeneratorBlockEntity(pos, state);
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
        return createTickerHelper(type, ModBlockEntities.NABONYTIC_GENERATOR.get(), NabonyticGeneratorBlockEntity::tick);
    }
    
}
