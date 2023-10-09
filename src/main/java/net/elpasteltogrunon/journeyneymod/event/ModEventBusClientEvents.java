package net.elpasteltogrunon.journeyneymod.event;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.elpasteltogrunon.journeyneymod.entity.client.ModModelLayers;
import net.elpasteltogrunon.journeyneymod.entity.client.TortoisimModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JourneyneyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents
{
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) 
    {
        event.registerLayerDefinition(ModModelLayers.TORTOISIM_LAYER, TortoisimModel::createBodyLayer);
    }
}
