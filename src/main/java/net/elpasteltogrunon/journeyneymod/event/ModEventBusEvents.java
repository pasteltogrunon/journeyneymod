package net.elpasteltogrunon.journeyneymod.event;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.elpasteltogrunon.journeyneymod.entity.ModEntities;
import net.elpasteltogrunon.journeyneymod.entity.custom.TortoisimEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = JourneyneyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents 
{
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) 
    {
        event.put(ModEntities.TORTOISIM.get(), TortoisimEntity.createAttributes().build());
    }
}
