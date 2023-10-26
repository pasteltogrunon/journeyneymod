package net.elpasteltogrunon.journeyneymod.util;

import net.minecraft.world.level.block.state.BlockState;

public interface EggLayer 
{
    boolean hasEgg();

    void setHasEgg(boolean hasEgg);

    BlockState getEggBlockState();
}
