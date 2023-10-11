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

        this.isReceiver = false;
        this.maxEnergy = 5000;
        this.energy = 5000;
    }
    


    public static void tick(Level level, BlockPos pos, BlockState state, NabonyticGeneratorBlockEntity pEntity) 
    {
        if(level.isClientSide()) return;

        BlockEntity receiverCandidate = level.getBlockEntity(pos.below());

        if(receiverCandidate instanceof EnergyBlockEntity && !(receiverCandidate instanceof CableBlockEntity))
        {
            EnergyBlockEntity receiver = (EnergyBlockEntity) receiverCandidate;
            pEntity.transferEnergy(pEntity.getMaxTransfer(), receiver);
        }
    }
}
