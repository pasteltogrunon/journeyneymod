package net.elpasteltogrunon.journeyneymod.screen.menu.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class OutputSlot extends SlotItemHandler
{

    public OutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) 
    {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) 
    {
        return false;
    }
}
