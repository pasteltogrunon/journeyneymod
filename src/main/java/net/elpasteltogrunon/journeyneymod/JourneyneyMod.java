package net.elpasteltogrunon.journeyneymod;

//import com.mojang.logging.LogUtils;

import net.elpasteltogrunon.journeyneymod.block.ModBlocks;
import net.elpasteltogrunon.journeyneymod.block.custom.BatteryBlock;
import net.elpasteltogrunon.journeyneymod.block.entity.ModBlockEntities;
import net.elpasteltogrunon.journeyneymod.enchantment.ModEnchantments;
import net.elpasteltogrunon.journeyneymod.entity.ModEntities;
import net.elpasteltogrunon.journeyneymod.entity.client.TortoisimRenderer;
import net.elpasteltogrunon.journeyneymod.item.ModCreativeModeTabs;
import net.elpasteltogrunon.journeyneymod.item.ModItems;
import net.elpasteltogrunon.journeyneymod.recipe.ModRecipes;
import net.elpasteltogrunon.journeyneymod.screen.BatteryScreen;
import net.elpasteltogrunon.journeyneymod.screen.NabonizerScreen;
import net.elpasteltogrunon.journeyneymod.screen.NabonyticGeneratorScreen;
import net.elpasteltogrunon.journeyneymod.screen.menu.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(JourneyneyMod.MOD_ID)
public class JourneyneyMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "journeyneymod";
    // Directly reference a slf4j logger
    //private static final Logger LOGGER = LogUtils.getLogger();

    public JourneyneyMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModEnchantments.register(modEventBus);
        ModEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS)
        {
            event.accept(ModItems.NABONITE_DUST);
            event.accept(ModItems.NABONITE_INGOT);
            event.accept(ModItems.NABONIZED_GUNPOWDER);
        }
        else if(event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS)
        {
            event.accept(ModItems.EDIBLE_NABE);
        }
        else if(event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS)
        {
            event.accept(ModBlocks.NABONITE_ORE);
            event.accept(ModBlocks.DEEPSLATE_NABONITE_ORE);
        }
        else if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
        {
            event.accept(ModItems.NABONITE_PICKAXE);
            event.accept(ModItems.NABONITE_AXE);
            event.accept(ModItems.NABONITE_SHOVEL);
            event.accept(ModItems.NABONITE_HOE);
            event.accept(ModItems.NABONYTE);
        }
        else if(event.getTabKey() == CreativeModeTabs.COMBAT)
        {
            event.accept(ModItems.NABONITE_SWORD);
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
                MenuScreens.register(ModMenuTypes.NABONIZER_MENU.get(), NabonizerScreen::new);
                MenuScreens.register(ModMenuTypes.NABONYTIC_GENERATOR_MENU.get(), NabonyticGeneratorScreen::new);
                MenuScreens.register(ModMenuTypes.BATTERY_MENU.get(), BatteryScreen::new);
                EntityRenderers.register(ModEntities.TORTOISIM.get(), TortoisimRenderer::new);
        }
    }
}
