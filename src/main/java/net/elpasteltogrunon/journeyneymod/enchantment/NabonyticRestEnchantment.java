package net.elpasteltogrunon.journeyneymod.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class NabonyticRestEnchantment extends Enchantment{

    public NabonyticRestEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slots) {
        super(rarity, category, slots);
    }

    public int getMinCost(int level) 
    {
        return 1 + (level - 1) * 10;
    }

    public int getMaxCost(int level) 
    {
        return getMinCost(level) + 30;
    }
    
    public int getMaxLevel()
    {
        return 5;
    }
}
