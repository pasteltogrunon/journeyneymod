package net.elpasteltogrunon.journeyneymod.block.entity;

import java.util.Optional;

import javax.annotation.Nullable;

import net.elpasteltogrunon.journeyneymod.block.custom.NabonizerBlock;
import net.elpasteltogrunon.journeyneymod.recipe.NabonizerRecipe;
import net.elpasteltogrunon.journeyneymod.screen.menu.NabonizerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NabonizerBlockEntity extends MachineBlockEntity implements MenuProvider
{
    public NabonizerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NABONIZER.get(), pos, state, 3, 1, 5);

        this.maxEnergy = 2000;
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
    protected int presentRecipeTime(SimpleContainer inventory)
    {
        Optional<NabonizerRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(NabonizerRecipe.Type.INSTANCE, inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem(level.registryAccess())) 
                ? recipe.get().getProcessTime() : 0;
    }

    @Override
    protected void craftItem(SimpleContainer inventory)
    {
        Optional<NabonizerRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(NabonizerRecipe.Type.INSTANCE, inventory, level);
        
        if(presentRecipeTime(inventory) > 0)
        {
            inputItemHandler.extractItem(0, 1, false);
            inputItemHandler.extractItem(1, 1, false);
            inputItemHandler.extractItem(2, 1, false);
            outputItemHandler.setStackInSlot(0, new ItemStack(recipe.get().getResultItem(level.registryAccess()).getItem(),
                    outputItemHandler.getStackInSlot(0).getCount() + 1));

            resetProgress();
        }
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(3).getItem() == stack.getItem() || inventory.getItem(3).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(3).getMaxStackSize() > inventory.getItem(3).getCount();
    }

    @Override
    protected void setLit(Level level, BlockPos pos, BlockState state, boolean flag)
    {
        level.setBlock(pos, state.setValue(NabonizerBlock.LIT, flag), 3);
        lit = flag;
    }
}
