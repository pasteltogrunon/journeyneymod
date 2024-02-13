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

public class NabonyticFurnaceBlockEntity extends MachineBlockEntity implements MenuProvider
{
    private int maxProgress = 150;


    public NabonyticFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NABONYTIC_FURNACE.get(), pos, state, 2, 1, 2);

        this.maxEnergy = 2000;
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
    protected void resetProgress()
    {
        this.maxProgress = 150;
        this.progress = 0;
    }

    @Override
    protected int presentRecipeTime(SimpleContainer inventory)
    {
        Optional<SmeltingRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, inventory, level);

        return (recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem(level.registryAccess()))) ? 150 : 0;
    }

    @Override
    protected void craftItem(SimpleContainer inventory)
    {
        Optional<SmeltingRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, inventory, level);
        
        if(presentRecipeTime(inventory)>0)
        {
            inputItemHandler.extractItem(0, 1, false);
            outputItemHandler.setStackInSlot(0, new ItemStack(recipe.get().getResultItem(level.registryAccess()).getItem(),
                    outputItemHandler.getStackInSlot(0).getCount() + 1));

            resetProgress();
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
