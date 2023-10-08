package net.elpasteltogrunon.journeyneymod.enchantment;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments
{
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, JourneyneyMod.MOD_ID);

    public static RegistryObject<Enchantment> NABONIAC =
            ENCHANTMENTS.register("naboniac",
                    () -> new NaboniacEnchantment(Enchantment.Rarity.RARE,
                            ModEnchantmentCategory.NABONYTE, EquipmentSlot.MAINHAND));

    public static RegistryObject<Enchantment> NABONYTIC_REST =
            ENCHANTMENTS.register("nabonytic_rest",
                    () -> new NabonyticRestEnchantment(Enchantment.Rarity.UNCOMMON,
                            ModEnchantmentCategory.NABONYTE, EquipmentSlot.MAINHAND));

                            
    public static void register(IEventBus eventBus)
    {
        ENCHANTMENTS.register(eventBus);
    }
}
