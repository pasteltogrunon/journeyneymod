package net.elpasteltogrunon.journeyneymod.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.elpasteltogrunon.journeyneymod.block.custom.CableBlock;
import net.elpasteltogrunon.journeyneymod.client.CableGeometry;
import net.elpasteltogrunon.journeyneymod.entity.client.ModModelLayers;
import net.elpasteltogrunon.journeyneymod.entity.client.TortoisimModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JourneyneyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents
{
    private static final List<ResourceLocation> MODEL_LOCATIONS = new ArrayList<>();
    private static final Map<ResourceLocation, BakedModel> MODELS = new HashMap<>();

    public static final ResourceLocation CABLE_CORE = loc("block/cable/cable_core");
    public static final ResourceLocation CABLE_CONNECTION_DOWN = loc("block/cable/cable_connection_down");
    public static final ResourceLocation CABLE_CONNECTION_UP = loc("block/cable/cable_connection_up");
    public static final ResourceLocation CABLE_CONNECTION_NORTH = loc("block/cable/cable_connection_north");
    public static final ResourceLocation CABLE_CONNECTION_SOUTH = loc("block/cable/cable_connection_south");
    public static final ResourceLocation CABLE_CONNECTION_EAST = loc("block/cable/cable_connection_east");
    public static final ResourceLocation CABLE_CONNECTION_WEST = loc("block/cable/cable_connection_west");

    public static final Map<Direction, ResourceLocation> resFromDir = new HashMap<Direction, ResourceLocation>()
    {
        {
            put(Direction.DOWN, CABLE_CONNECTION_DOWN);
            put(Direction.UP, CABLE_CONNECTION_UP);
            put(Direction.NORTH, CABLE_CONNECTION_NORTH);
            put(Direction.SOUTH, CABLE_CONNECTION_SOUTH);
            put(Direction.EAST, CABLE_CONNECTION_EAST);
            put(Direction.WEST, CABLE_CONNECTION_WEST);
        }
    };

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) 
    {
        event.registerLayerDefinition(ModModelLayers.TORTOISIM_LAYER, TortoisimModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void modelLoader(ModelEvent.RegisterGeometryLoaders event)
    {
        event.register("cable", new CableGeometry.Loader());
    }

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) 
    {
        for (ResourceLocation model : MODEL_LOCATIONS) 
            event.register(model);
    }

    @SubscribeEvent
    public static void bakingModelsFinished(ModelEvent.BakingCompleted event) 
    {
        for (ResourceLocation modelLocation : MODEL_LOCATIONS)
            MODELS.put(modelLocation, event.getModels().get(modelLocation));

    }

    private static ResourceLocation loc(String modelName)
    {
        ResourceLocation loc = new ResourceLocation(JourneyneyMod.MOD_ID, modelName);
        MODEL_LOCATIONS.add(loc);
        return loc;
    }

    public static BakedModel modelOf(ResourceLocation location)
     {
        return MODELS.get(location);
    }
}
