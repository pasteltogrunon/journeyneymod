package net.elpasteltogrunon.journeyneymod.block.entity;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

import net.elpasteltogrunon.journeyneymod.block.custom.NabonizerBlock;
import net.elpasteltogrunon.journeyneymod.recipe.NabonizerRecipe;
import net.elpasteltogrunon.journeyneymod.screen.menu.NabonizerMenu;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class NabonizerBlockEntity extends EnergyBlockEntity implements MenuProvider
{
    private static final int CONSUMING_RATE = 5;


    private final ItemStackHandler inputItemHandler = new ItemStackHandler(3)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    private final ItemStackHandler outputItemHandler = new ItemStackHandler(1)
    {
        @Override
        protected void onContentsChanged(int slot)
        {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyInputItemHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyOutputItemHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();


    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;
    private boolean lit;

    public NabonizerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NABONIZER.get(), pos, state);

        this.energy = 0;
        this.maxEnergy = 2000;
        this.isReceiver = true;
        this.isEmitter = false;
        this.data = new ContainerData() 
        {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> NabonizerBlockEntity.this.progress;
                    case 1 -> NabonizerBlockEntity.this.maxProgress;
                    case 2 -> NabonizerBlockEntity.this.energy;
                    case 3 -> NabonizerBlockEntity.this.maxEnergy;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> NabonizerBlockEntity.this.progress = value;
                    case 1 -> NabonizerBlockEntity.this.maxProgress = value;
                    case 2 -> NabonizerBlockEntity.this.energy = value;
                    case 3 -> NabonizerBlockEntity.this.maxEnergy = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player)
    {
        return new NabonizerMenu(id, inventory, this, this.data);
    }

    @Override
    public Component getDisplayName()
    {
        return Component.literal("Nabonizer");
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
            if(side == Direction.DOWN)
            {
                return this.lazyOutputItemHandler.cast();
            }
            else if (side!= null)
            {
                return this.lazyInputItemHandler.cast();
            }
            else
            {
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
    }

    public void drops() 
    {
        SimpleContainer inventory = new SimpleContainer(inputItemHandler.getSlots() + outputItemHandler.getSlots());
        for (int i = 0; i < inputItemHandler.getSlots(); i++)
        {
            inventory.setItem(i, inputItemHandler.getStackInSlot(i));
        }
        inventory.setItem(3, outputItemHandler.getStackInSlot(3));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, NabonizerBlockEntity pEntity) 
    {
        if(level.isClientSide()) return;

        //If there is recipe and there is enough energy
        int time = hasRecipe(pEntity);
        if(time > 0)
        {
            if(pEntity.energy >= CONSUMING_RATE)
            {
                pEntity.extractEnergy(CONSUMING_RATE);
    
                if(!pEntity.lit)
                {
                    level.setBlock(pos, state.setValue(NabonizerBlock.LIT, true), 3);
                    pEntity.lit = true;
                }
    
                pEntity.maxProgress = time;
                pEntity.progress++;
                
                if(pEntity.progress >= pEntity.maxProgress)
                {
                    craftItem(pEntity);
                }
            }
            else if(pEntity.lit)
            {
                level.setBlock(pos, state.setValue(NabonizerBlock.LIT, false), 3);
                pEntity.lit = false;
            }
            setChanged(level, pos, state);
        }
        else
        {
            if(pEntity.lit)
            {
                level.setBlock(pos, state.setValue(NabonizerBlock.LIT, false), 3);
                pEntity.lit = false;
                setChanged(level, pos, state);
            }
            pEntity.resetProgress();
        }
        //addToNeighborCables(level, pos, state, (EnergyBlockEntity) pEntity);
    }

    private void resetProgress()
    {
        this.maxProgress = 100;
        this.progress = 0;
    }

    //Returns the recipe process time if there is any, and zero if there is not any recipe
    private static int hasRecipe(NabonizerBlockEntity entity)
    {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.inputItemHandler.getSlots() + entity.outputItemHandler.getSlots());
        for (int i = 0; i < entity.inputItemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.inputItemHandler.getStackInSlot(i));
        }
        inventory.setItem(3, entity.outputItemHandler.getStackInSlot(0));

        Optional<NabonizerRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(NabonizerRecipe.Type.INSTANCE, inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem(level.registryAccess())) 
                ? recipe.get().getProcessTime() : 0;
    }

    private static void craftItem(NabonizerBlockEntity entity)
    {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.inputItemHandler.getSlots() + entity.outputItemHandler.getSlots());
        for (int i = 0; i < entity.inputItemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.inputItemHandler.getStackInSlot(i));
        }
        inventory.setItem(3, entity.outputItemHandler.getStackInSlot(0));

        Optional<NabonizerRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(NabonizerRecipe.Type.INSTANCE, inventory, level);
        
        if(hasRecipe(entity) > 0) 
        {
            entity.inputItemHandler.extractItem(0, 1, false);
            entity.inputItemHandler.extractItem(1, 1, false);
            entity.inputItemHandler.extractItem(2, 1, false);
            entity.outputItemHandler.setStackInSlot(0, new ItemStack(recipe.get().getResultItem(level.registryAccess()).getItem(),
                    entity.outputItemHandler.getStackInSlot(0).getCount() + 1));

            entity.resetProgress();
        }
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(3).getItem() == stack.getItem() || inventory.getItem(3).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(3).getMaxStackSize() > inventory.getItem(3).getCount();
    }
}
