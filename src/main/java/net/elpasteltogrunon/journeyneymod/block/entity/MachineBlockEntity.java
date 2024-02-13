package net.elpasteltogrunon.journeyneymod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class MachineBlockEntity extends EnergyBlockEntity
{
    protected int CONSUMING_RATE;
    protected ItemStackHandler inputItemHandler;
    protected ItemStackHandler outputItemHandler;

    private LazyOptional<IItemHandler> lazyInputItemHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyOutputItemHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();


    protected final ContainerData data;
    protected int progress = 0;
    protected int maxProgress = 100;
    protected boolean lit;

    public MachineBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, int inputSize, int outputSize, int CONSUMING_RATE) {
        super(blockEntityType, pos, state);

        this.energy = 0;
        this.isReceiver = true;
        this.isEmitter = false;

        this.inputItemHandler = new ItemStackHandler(inputSize);
        this.outputItemHandler = new ItemStackHandler(outputSize);

        this.CONSUMING_RATE = CONSUMING_RATE;

        this.data = new ContainerData()
        {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> MachineBlockEntity.this.progress;
                    case 1 -> MachineBlockEntity.this.maxProgress;
                    case 2 -> MachineBlockEntity.this.energy;
                    case 3 -> MachineBlockEntity.this.maxEnergy;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> MachineBlockEntity.this.progress = value;
                    case 1 -> MachineBlockEntity.this.maxProgress = value;
                    case 2 -> MachineBlockEntity.this.energy = value;
                    case 3 -> MachineBlockEntity.this.maxEnergy = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public void load(@NotNull CompoundTag nbt)
    {
        super.load(nbt);

        this.inputItemHandler.deserializeNBT(nbt.getCompound("input"));
        this.outputItemHandler.deserializeNBT(nbt.getCompound("output"));
        this.progress = nbt.getInt("progress");
        this.maxProgress = nbt.getInt("max_progress");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt)
    {
        super.saveAdditional(nbt);

        nbt.put("input", this.inputItemHandler.serializeNBT());
        nbt.put("output", this.outputItemHandler.serializeNBT());
        nbt.putInt("progress", this.progress);
        nbt.putInt("max_progress", this.maxProgress);

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if(cap == ForgeCapabilities.ITEM_HANDLER)
        {
            if(side == Direction.DOWN) {
                return this.lazyOutputItemHandler.cast();
            }
            else if (side!= null) {
                return this.lazyInputItemHandler.cast();
            }
            else {
                return this.lazyItemHandler.cast();
            }
        }
        return super.getCapability(cap);

    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        lazyInputItemHandler = LazyOptional.of(() -> this.inputItemHandler);
        lazyOutputItemHandler = LazyOptional.of(()-> this.outputItemHandler);
        lazyItemHandler = LazyOptional.of(() -> new CombinedInvWrapper(this.inputItemHandler, this.outputItemHandler));
    }

    @Override
    public void invalidateCaps()
    {
        super.invalidateCaps();
        this.lazyInputItemHandler.invalidate();
        this.lazyOutputItemHandler.invalidate();
        this.lazyItemHandler.invalidate();
    }

    public void drops()
    {
        Containers.dropContents(this.level, this.worldPosition, getInventory(this));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MachineBlockEntity pEntity)
    {
        if(level.isClientSide()) return;

        //If there is recipe and there is enough energy
        int time = pEntity.presentRecipeTime(getInventory(pEntity));
        if(time > 0)
        {
            if(pEntity.energy >= pEntity.CONSUMING_RATE)
            {
                pEntity.extractEnergy(pEntity.CONSUMING_RATE);

                if(!pEntity.lit)
                {
                    pEntity.setLit(level, pos, state, true);
                }

                pEntity.maxProgress = time;
                pEntity.progress++;

                if(pEntity.progress >= pEntity.maxProgress)
                {
                    pEntity.craftItem(getInventory(pEntity));
                }
            }
            else if(pEntity.lit)
            {
                pEntity.setLit(level, pos, state, false);
            }
            setChanged(level, pos, state);
        }
        else
        {
            if(pEntity.lit)
            {
                pEntity.setLit(level, pos, state, false);
                setChanged(level, pos, state);
            }
            pEntity.resetProgress();
        }
        //addToNeighborCables(level, pos, state, (EnergyBlockEntity) pEntity);
    }

    protected void resetProgress()
    {
        this.maxProgress = 100;
        this.progress = 0;
    }

    protected static SimpleContainer getInventory(MachineBlockEntity entity){
        int inputSize = entity.inputItemHandler.getSlots();
        int outputSize = entity.outputItemHandler.getSlots();

        SimpleContainer inventory = new SimpleContainer(inputSize + outputSize);
        for (int i = 0; i < inputSize; i++) {
            inventory.setItem(i, entity.inputItemHandler.getStackInSlot(i));
        }
        for (int i = 0; i < outputSize; i++) {
            inventory.setItem(i+inputSize, entity.outputItemHandler.getStackInSlot(i));
        }

        return inventory;
    }

    //Returns the recipe process time if there is any, and zero if there is not any recipe
    protected int presentRecipeTime(SimpleContainer inventory)
    {
        return 0;
    }

    protected void craftItem(SimpleContainer inventory)
    {
        return;
    }


    protected void setLit(Level level, BlockPos pos, BlockState state, boolean flag)
    {
        return;
    }
}
