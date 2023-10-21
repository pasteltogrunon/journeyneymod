package net.elpasteltogrunon.journeyneymod.screen.menu;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes 
{
        public static final DeferredRegister<MenuType<?>> MENUS =
                DeferredRegister.create(ForgeRegistries.MENU_TYPES, JourneyneyMod.MOD_ID);

        public static final RegistryObject<MenuType<NabonizerMenu>> NABONIZER_MENU =
                registerMenuType(NabonizerMenu::new, "nabonizer_menu");

        public static final RegistryObject<MenuType<NabonyticFurnaceMenu>> NABONYTIC_FURNACE_MENU =
                registerMenuType(NabonyticFurnaceMenu::new, "nabonytic_furnace_menu");

        public static final RegistryObject<MenuType<NabonyticGeneratorMenu>> NABONYTIC_GENERATOR_MENU =
                registerMenuType(NabonyticGeneratorMenu::new, "nabonytic_generator_menu");

        public static final RegistryObject<MenuType<BatteryMenu>> BATTERY_MENU =
                registerMenuType(BatteryMenu::new, "battery_menu");


        private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                        String name) {
                return MENUS.register(name, () -> IForgeMenuType.create(factory));
        }

        public static void register(IEventBus eventBus) {
                MENUS.register(eventBus);
        }
}
