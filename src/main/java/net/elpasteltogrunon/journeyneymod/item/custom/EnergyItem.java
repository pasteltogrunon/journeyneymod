package net.elpasteltogrunon.journeyneymod.item.custom;


import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class EnergyItem extends Item
{
    private int maxEnergy;


    public EnergyItem(Properties pProperties, int maxEnergy) 
    {
        super(pProperties);
        this.maxEnergy = maxEnergy;
    }

    public InteractionResult useOn(UseOnContext pContext)
    {
        if(!pContext.getLevel().isClientSide())
        {
            if(pContext.getPlayer().isSecondaryUseActive())
            {
                extractEnergy(5, pContext.getItemInHand());
                return InteractionResult.SUCCESS;
            }
            else
            {
                receiveEnergy(5, pContext.getItemInHand());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    protected CompoundTag getOrCreateTag(ItemStack pStack)
    {
        if(pStack.hasTag())
        {
            return pStack.getTag();
        }
        else
        {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("energy", 0);
            nbt.putInt("max_energy", maxEnergy);
            pStack.setTag(nbt);
            return nbt;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) 
    {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack pStack) 
    {
        CompoundTag nbt = getOrCreateTag(pStack);
        int energy = nbt.getInt("energy");
        int maxEnergy = nbt.getInt("max_energy");
        return Math.round((float)energy * 13.0F / (float)maxEnergy);
    }

    @Override
    public int getBarColor(ItemStack pStack) 
    {
        CompoundTag nbt = getOrCreateTag(pStack);
        int energy = nbt.getInt("energy");
        int maxEnergy = nbt.getInt("max_energy");
        float stackMaxDamage = (float)maxEnergy;
        float f = Math.max(0.0F, ((float)energy) / stackMaxDamage) * 1.5f;
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    public int getEnergy(ItemStack pStack)
    {
        CompoundTag nbt = getOrCreateTag(pStack);
        return nbt.getInt("energy");
    }

    public void setEnergy(int energy, ItemStack pStack)
    {
        CompoundTag nbt = getOrCreateTag(pStack);
        nbt.putInt("energy", energy);
        pStack.setTag(nbt);
    }

    public int getMaxEnergy(ItemStack pStack)
    {
        CompoundTag nbt = getOrCreateTag(pStack);
        return nbt.getInt("max_energy");
    }

    public void setMaxEnergy(int maxEnergy)
    {
        this.maxEnergy = maxEnergy;
    }

     public int receiveEnergy(int amount, ItemStack pStack)
    {
        int a = Math.min(this.getEnergy(pStack) + amount, this.getMaxEnergy(pStack));
        int clampedAmount = a - this.getEnergy(pStack);
        this.setEnergy(a, pStack);
        return clampedAmount;
    }

    public int extractEnergy(int amount, ItemStack pStack)
    {
        int clampedAmount = Math.min(this.getEnergy(pStack), amount);
        this.setEnergy(this.getEnergy(pStack) - clampedAmount, pStack);
        return clampedAmount;
    }

    /*public int transferEnergy(int amount, EnergyBlockEntity target)
    {
        int transferredEnergy = Math.min(amount, this.getEnergy());
        return this.extractEnergy(target.receiveEnergy(transferredEnergy));
    }

    public int suckEnergy(int amount, EnergyBlockEntity target)
    {
        int requestedEnergy = Math.min(this.getMaxEnergy()-this.getEnergy(), amount);
        return this.receiveEnergy(target.extractEnergy(requestedEnergy));
    }*/

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltips, TooltipFlag flag) 
    {
        CompoundTag nbt = getOrCreateTag(pStack);
        pTooltips.add(Component.literal(nbt.getInt("energy") + "/" + nbt.getInt("max_energy") + " NU"));
    }

}
