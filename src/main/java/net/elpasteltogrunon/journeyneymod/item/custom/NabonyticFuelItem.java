package net.elpasteltogrunon.journeyneymod.item.custom;

import net.minecraft.world.item.Item;

public class NabonyticFuelItem extends Item
{
    private int nabonyticBurnTime;

    public NabonyticFuelItem(Properties pProperties, int nabonyticBurnTime) 
    {
        super(pProperties);
        this.nabonyticBurnTime = nabonyticBurnTime;
    }
    
    public int getNabonyticBurnTime() 
    {
        return nabonyticBurnTime;
    }
}
