package net.elpasteltogrunon.journeyneymod.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class NaboniacEnchantment extends Enchantment
{

    public NaboniacEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slot) {
        super(rarity, category, slot);
    }

    public int getMinCost(int p_44652_) {
        return 20 + (p_44652_ - 1) * 25;
    }

    public int getMaxCost(int p_44660_) {
        return getMinCost(p_44660_) + 50;
    }
    
    
    public int getMaxLevel()
    {
        return 3;
    }

}
