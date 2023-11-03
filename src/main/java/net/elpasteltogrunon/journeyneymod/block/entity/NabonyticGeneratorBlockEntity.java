package net.elpasteltogrunon.journeyneymod.block.entity;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import net.elpasteltogrunon.journeyneymod.item.custom.NabonyticFuelItem;
import net.elpasteltogrunon.journeyneymod.screen.menu.NabonyticGeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class NabonyticGeneratorBlockEntity extends EnergyBlockEntity implements MenuProvider
 {
    private static final int GENERATING_RATE = 10;

    private final ItemStackHandler itemHandler = new ItemStackHandler(1)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int burnTimeLeft = 0;

    public NabonyticGeneratorBlockEntity(BlockPos pos, BlockState state) 
    {
        super(ModBlockEntities.NABONYTIC_GENERATOR.get(), pos, state);

        this.isReceiver = false;
        this.isEmitter = true;
        this.maxEnergy = 5000;
        this.energy = 0;
        this.data = new ContainerData() 
        {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> NabonyticGeneratorBlockEntity.this.burnTimeLeft;
                    case 1 -> NabonyticGeneratorBlockEntity.this.energy;
                    case 2 -> NabonyticGeneratorBlockEntity.this.maxEnergy;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> NabonyticGeneratorBlockEntity.this.burnTimeLeft = value;
                    case 1 -> NabonyticGeneratorBlockEntity.this.energy = value;
                    case 2 -> NabonyticGeneratorBlockEntity.this.maxEnergy = value;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }
    

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player)
    {
        return new NabonyticGeneratorMenu(id, inventory, this, this.data);
    }

    @Override
    public Component getDisplayName()
    {
        return Component.literal("Nabonytic Generator");
    }

    @Override
    public void load(@NotNull CompoundTag nbt)
    {
        super.load(nbt);

        this.itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.burnTimeLeft = nbt.getInt("burn_time_left");
    }
    
    @Override
    public void saveAdditional(@NotNull CompoundTag nbt)
    {
        super.saveAdditional(nbt);

        nbt.put("inventory", this.itemHandler.serializeNBT());
        nbt.putInt("burn_time_left", this.burnTimeLeft);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if(cap == ForgeCapabilities.ITEM_HANDLER)
        {
            return this.lazyItemHandler.cast();
        }
        return super.getCapability(cap);

    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> this.itemHandler);
    }

    @Override
    public void invalidateCaps()
    {
        super.invalidateCaps();
        this.lazyItemHandler.invalidate();
    }


    public void drops() 
    {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) 
        {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, NabonyticGeneratorBlockEntity pEntity) 
    {
        if(level.isClientSide()) return;

        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        BlockEntity receiverCandidate = level.getBlockEntity(pos.below());

        if(receiverCandidate instanceof EnergyBlockEntity && !(receiverCandidate instanceof CableBlockEntity))
        {
            EnergyBlockEntity receiver = (EnergyBlockEntity) receiverCandidate;
            pEntity.transferEnergy(pEntity.getMaxTransfer(), receiver);
        }

        if(pEntity.burnTimeLeft > 0)
        {
            pEntity.burnTimeLeft--;
            pEntity.receiveEnergy(GENERATING_RATE);
            setChanged(level, pos, state);
        }
        else
        {
            if(pEntity.getEnergy() != pEntity.getMaxEnergy() && inventory.getItem(0).getItem() instanceof NabonyticFuelItem fuelItem)
            {
                pEntity.burnTimeLeft = fuelItem.getNabonyticBurnTime();
                pEntity.itemHandler.extractItem(0, 1, false);
                setChanged(level, pos, state);
            }
        }


        //addToNeighborCables(level, pos, state, (EnergyBlockEntity) pEntity);
    }
}
