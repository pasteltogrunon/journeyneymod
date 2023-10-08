package net.elpasteltogrunon.journeyneymod.enchantment;

import net.elpasteltogrunon.journeyneymod.item.custom.NabonyteItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ModEnchantmentCategory {
    public static final EnchantmentCategory NABONYTE = EnchantmentCategory.create("nabonyte", item -> item instanceof NabonyteItem);
}
