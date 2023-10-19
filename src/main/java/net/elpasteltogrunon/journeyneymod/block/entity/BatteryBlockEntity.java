package net.elpasteltogrunon.journeyneymod.block.entity;

import javax.annotation.Nullable;

import net.elpasteltogrunon.journeyneymod.block.custom.BatteryBlock;
import net.elpasteltogrunon.journeyneymod.screen.menu.BatteryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BatteryBlockEntity extends EnergyBlockEntity implements MenuProvider
{
    protected final ContainerData data;
    protected String displayName;

    public BatteryBlockEntity(BlockPos pos, BlockState state) 
    {
        super(ModBlockEntities.BATTERY.get(), pos, state);
        
        BatteryBlock block = (BatteryBlock) getBlockState().getBlock();

        this.displayName = block.getDisplayName();
        this.isReceiver = true;
        this.isEmitter = true;
        this.maxEnergy = block.getMaxEnergy();
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
        return new BatteryMenu(id, inventory, this, this.data);
    }

    @Override
    public Component getDisplayName()
    {
        return Component.literal(displayName);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BatteryBlockEntity pEntity) 
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

