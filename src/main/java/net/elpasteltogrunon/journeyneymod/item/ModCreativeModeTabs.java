package net.elpasteltogrunon.journeyneymod.item;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.elpasteltogrunon.journeyneymod.block.ModBlocks;
import net.elpasteltogrunon.journeyneymod.enchantment.ModEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JourneyneyMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ONE_TAB = CREATIVE_MODE_TABS.register("one_tab",
    () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.NABONITE_DUST.get()))
            .title(Component.translatable("creativetab.one_tab"))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(ModItems.NABONITE_DUST.get());
                pOutput.accept(ModItems.NABONITE_INGOT.get());
                pOutput.accept(ModBlocks.NABONITE_ORE.get());
                pOutput.accept(ModBlocks.DEEPSLATE_NABONITE_ORE.get());
                pOutput.accept(ModBlocks.NABONITE_BLOCK.get());
                pOutput.accept(ModBlocks.NABONITE_DUST_BLOCK.get());

                pOutput.accept(ModBlocks.DIORITE_GRAVEL.get());
                pOutput.accept(ModBlocks.ANDESITE_GRAVEL.get());
                pOutput.accept(ModBlocks.GRANITE_GRAVEL.get());
                pOutput.accept(ModBlocks.TUFF_GRAVEL.get());
                pOutput.accept(ModBlocks.DEEPSLATE_GRAVEL.get());

                pOutput.accept(ModItems.ALBITE.get());
                pOutput.accept(ModItems.ANORTHITE.get());
                pOutput.accept(ModItems.ORTHOCLASE.get());
                pOutput.accept(ModItems.MICA.get());

                pOutput.accept(ModItems.EDIBLE_NABE.get());
                pOutput.accept(ModItems.NABONYTE.get());
                pOutput.accept(ModItems.ROCK_SHATTERER.get());
                pOutput.accept(ModItems.NABONIZED_GUNPOWDER.get());
                pOutput.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(ModEnchantments.NABONIAC.get(), 3)));
                pOutput.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(ModEnchantments.NABONYTIC_REST.get(), 5)));

                pOutput.accept(ModItems.NABONITE_SWORD.get());
                pOutput.accept(ModItems.NABONITE_SHOVEL.get());
                pOutput.accept(ModItems.NABONITE_PICKAXE.get());
                pOutput.accept(ModItems.NABONITE_AXE.get());
                pOutput.accept(ModItems.NABONITE_HOE.get());

                pOutput.accept(ModBlocks.MACHINE_BLOCK.get());
                pOutput.accept(ModBlocks.NABONIZER.get());
                pOutput.accept(ModBlocks.NABONYTIC_FURNACE.get());
                pOutput.accept(ModBlocks.NABONYTIC_GENERATOR.get());
                pOutput.accept(ModBlocks.NABONITE_BATTERY.get());
                pOutput.accept(ModBlocks.CABLE.get());
                pOutput.accept(ModItems.NABONIZED_COAL.get());
                pOutput.accept(ModItems.NABONYTIC_MIX.get());
                pOutput.accept(ModItems.RE_BATTERY.get());
                pOutput.accept(ModItems.ELECTROMAGNETIC_FREQUENTIAL_TOROID.get());

                pOutput.accept(ModItems.TORTOISIM_SPAWN_EGG.get());
                pOutput.accept(ModItems.TORTOISIM_SCUTE.get());
                pOutput.accept(ModBlocks.TORTOSIM_EGG.get());

            })
            .build());

    public static void register(IEventBus event)
    {
        CREATIVE_MODE_TABS.register(event);
    }
}