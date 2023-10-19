package net.elpasteltogrunon.journeyneymod.block.entity.battery;

import javax.annotation.Nullable;

import net.elpasteltogrunon.journeyneymod.block.entity.CableBlockEntity;
import net.elpasteltogrunon.journeyneymod.block.entity.EnergyBlockEntity;
import net.elpasteltogrunon.journeyneymod.block.entity.NabonyticGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BatteryBlockEntity extends EnergyBlockEntity implements MenuProvider
{
    protected final ContainerData data;
    protected String displayName;

    public BatteryBlockEntity(BlockEntityType<? extends BatteryBlockEntity> type, BlockPos pos, BlockState state, int maxEnergy, String displayName) 
    {
        super(type, pos, state);
        
        this.displayName = displayName;
        this.isReceiver = false;
        this.maxEnergy = maxEnergy;
        this.energy = 0;
        this.data = new ContainerData() 
        {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BatteryBlockEntity.this.energy;
                    case 1 -> BatteryBlockEntity.this.maxEnergy;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BatteryBlockEntity.this.energy = value;
                    case 1 -> BatteryBlockEntity.this.maxEnergy = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }
    

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player)
    {
        return null;
        //return new NabonyticGeneratorMenu(id, inventory, this, this.data);
    }

    @Override
    public Component getDisplayName()
    {
        return Component.literal(displayName);
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

        addToNeighborCables(level, pos, state, (EnergyBlockEntity) pEntity);
    }
}

