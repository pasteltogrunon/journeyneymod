package net.elpasteltogrunon.journeyneymod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NabonyticGeneratorBlockEntity extends EnergyBlockEntity
 {

    public NabonyticGeneratorBlockEntity(BlockPos pos, BlockState state) 
    {
        super(ModBlockEntities.NABONYTIC_GENERATOR.get(), pos, state);
    }
    


    public static void tick(Level level, BlockPos pos, BlockState state, NabonyticGeneratorBlockEntity pEntity) 
    {
        if(level.isClientSide()) return;

        BlockEntity receiverCandidate = level.getBlockEntity(pos.below());

        if(receiverCandidate != null && EnergyBlockEntity.class.isAssignableFrom(receiverCandidate.getClass()))
        {
            EnergyBlockEntity receiver = (EnergyBlockEntity) receiverCandidate;
            receiver.receiveEnergy(pEntity.maxTransfer);
        }
    }
}
