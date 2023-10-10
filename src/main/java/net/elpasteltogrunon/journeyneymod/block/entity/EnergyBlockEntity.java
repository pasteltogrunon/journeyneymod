package net.elpasteltogrunon.journeyneymod.block.entity;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class EnergyBlockEntity extends BlockEntity
{
    protected int energy = 0;
    protected int maxEnergy = 1000;
    private int maxTransfer = 100;
    public boolean isReceiver;

    public EnergyBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) 
    {
        super(blockEntityType, pos, state);
    }

    public int getMaxTransfer() {
        return maxTransfer;
    }

    public void setMaxTransfer(int maxTransfer) {
        this.maxTransfer = maxTransfer;
    }

    public int getEnergy()
    {
        return this.energy;
    }

    public int getMaxEnergy()
    {
        return this.maxEnergy;
    }

    public int receiveEnergy(int amount)
    {
        int a = Math.min(this.energy + amount, this.maxEnergy);
        int clampedAmount = a - this.energy;
        this.energy = a;
        return clampedAmount;
    }

    public int extractEnergy(int amount)
    {
        int a = Math.max(this.energy - amount, 0);
        int clampedAmount = this.energy - a;
        this.energy = a;
        return clampedAmount;
    }


    @Override
    public void load(@NotNull CompoundTag nbt)
    {
        super.load(nbt);

        this.energy = nbt.getInt("energy");
        this.maxEnergy = nbt.getInt("max_energy");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt)
    {
        super.saveAdditional(nbt);

        nbt.putInt("energy", this.energy);
        nbt.putInt("max_energy", this.maxEnergy);
    }
}
