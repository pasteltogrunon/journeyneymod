package net.elpasteltogrunon.journeyneymod.block.entity;

import java.util.Optional;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import net.elpasteltogrunon.journeyneymod.block.custom.NabonyticFurnaceBlock;
import net.elpasteltogrunon.journeyneymod.screen.menu.NabonyticFurnaceMenu;
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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class NabonyticFurnaceBlockEntity extends EnergyBlockEntity implements MenuProvider
{
    private static final int CONSUMING_RATE = 2;


    private final ItemStackHandler itemHandler = new ItemStackHandler(3)
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
    private int progress = 0;
    private int maxProgress = 150;
    private boolean lit;

    public NabonyticFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NABONYTIC_FURNACE.get(), pos, state);

        this.energy = 0;
        this.maxEnergy = 2000;
        this.isReceiver = true;
        this.isEmitter = false;
        this.data = new ContainerData() 
        {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> NabonyticFurnaceBlockEntity.this.progress;
                    case 1 -> NabonyticFurnaceBlockEntity.this.maxProgress;
                    case 2 -> NabonyticFurnaceBlockEntity.this.energy;
                    case 3 -> NabonyticFurnaceBlockEntity.this.maxEnergy;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> NabonyticFurnaceBlockEntity.this.progress = value;
                    case 1 -> NabonyticFurnaceBlockEntity.this.maxProgress = value;
                    case 2 -> NabonyticFurnaceBlockEntity.this.energy = value;
                    case 3 -> NabonyticFurnaceBlockEntity.this.maxEnergy = value;
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
        return new NabonyticFurnaceMenu(id, inventory, this, this.data);
    }

    @Override
    public Component getDisplayName()
    {
        return Component.literal("Nabonytic Furnace");
    }
    
    @Override
    public void load(@NotNull CompoundTag nbt)
    {
        super.load(nbt);

        this.itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.progress = nbt.getInt("progress");
        this.maxProgress = nbt.getInt("max_progress");
    }
    
    @Override
    public void saveAdditional(@NotNull CompoundTag nbt)
    {
        super.saveAdditional(nbt);

        nbt.put("inventory", this.itemHandler.serializeNBT());
        nbt.putInt("progress", this.progress);
        nbt.putInt("max_progress", this.maxProgress);

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

    public static void tick(Level level, BlockPos pos, BlockState state, NabonyticFurnaceBlockEntity pEntity) 
    {
        if(level.isClientSide()) return;

        //If there is recipe and there is enough energy
        if(hasRecipe(pEntity))
        {
            if(pEntity.energy >= CONSUMING_RATE)
            {
                pEntity.extractEnergy(CONSUMING_RATE);
    
                if(!pEntity.lit)
                {
                    level.setBlock(pos, state.setValue(NabonyticFurnaceBlock.LIT, true), 3);
                    pEntity.lit = true;
                }
    
                pEntity.progress++;
                
                if(pEntity.progress >= pEntity.maxProgress)
                {
                    craftItem(pEntity);
                }
            }
            else if(pEntity.lit)
            {
                level.setBlock(pos, state.setValue(NabonyticFurnaceBlock.LIT, false), 3);
                pEntity.lit = false;
            }

        }
        else
        {
            if(pEntity.lit)
            {
                level.setBlock(pos, state.setValue(NabonyticFurnaceBlock.LIT, false), 3);
                pEntity.lit = false;
            }
            pEntity.resetProgress();
        }

        setChanged(level, pos, state);
        //addToNeighborCables(level, pos, state, (EnergyBlockEntity) pEntity);
    }

    private void resetProgress()
    {
        this.maxProgress = 150;
        this.progress = 0;
    }

    //Returns the recipe process time if there is any, and zero if there is not any recipe
    private static boolean hasRecipe(NabonyticFurnaceBlockEntity entity)
    {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<SmeltingRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem(level.registryAccess()));
    }

    private static void craftItem(NabonyticFurnaceBlockEntity pEntity)
    {
        Level level = pEntity.level;
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<SmeltingRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, inventory, level);
        
        if(hasRecipe(pEntity)) 
        {
            pEntity.itemHandler.extractItem(0, 1, false);
            pEntity.itemHandler.setStackInSlot(2, new ItemStack(recipe.get().getResultItem(level.registryAccess()).getItem(),
                    pEntity.itemHandler.getStackInSlot(2).getCount() + 1));

            pEntity.resetProgress();
        }
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) 
    {
        return inventory.getItem(2).getItem() == stack.getItem() || inventory.getItem(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) 
    {
        return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
    }
}
