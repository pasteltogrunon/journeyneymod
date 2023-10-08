package net.elpasteltogrunon.journeyneymod.item;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.elpasteltogrunon.journeyneymod.item.custom.NabonyteItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JourneyneyMod.MOD_ID);

    public static final RegistryObject<Item> NABONITE_DUST = ITEMS.register("nabonite_dust", 
        () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> NABONITE_INGOT = ITEMS.register("nabonite_ingot", 
        () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> NABONIZED_GUNPOWDER = ITEMS.register("nabonized_gunpowder", 
        () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EDIBLE_NABE = ITEMS.register("edible_nabe", 
        () -> new Item(new Item.Properties().food(ModFoods.EDIBLE_NABE)));

    public static final RegistryObject<Item> NABONYTE = ITEMS.register("nabonyte", 
        () -> new NabonyteItem(new Item.Properties().durability(50)));

    public static final RegistryObject<Item> NABONITE_SWORD = ITEMS.register("nabonite_sword", 
        () -> new SwordItem(ModToolTiers.NABONITE, 3, -2f, new Item.Properties()));

    public static final RegistryObject<Item> NABONITE_SHOVEL = ITEMS.register("nabonite_shovel", 
        () -> new ShovelItem(ModToolTiers.NABONITE, 1, -2.5f, new Item.Properties()));
    
    public static final RegistryObject<Item> NABONITE_PICKAXE = ITEMS.register("nabonite_pickaxe", 
        () -> new PickaxeItem(ModToolTiers.NABONITE, 2, -2.8f, new Item.Properties()));

    public static final RegistryObject<Item> NABONITE_AXE = ITEMS.register("nabonite_axe", 
        () -> new AxeItem(ModToolTiers.NABONITE, 5, -3f, new Item.Properties()));

    public static final RegistryObject<Item> NABONITE_HOE = ITEMS.register("nabonite_hoe", 
        () -> new HoeItem(ModToolTiers.NABONITE, 2, 0f, new Item.Properties()));

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}