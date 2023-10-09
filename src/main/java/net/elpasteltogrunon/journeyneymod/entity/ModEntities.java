package net.elpasteltogrunon.journeyneymod.entity;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.elpasteltogrunon.journeyneymod.entity.custom.TortoisimEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, JourneyneyMod.MOD_ID);

    public static final RegistryObject<EntityType<TortoisimEntity>> TORTOISIM =
            ENTITY_TYPES.register("tortoisim", () -> EntityType.Builder.of(TortoisimEntity::new, MobCategory.CREATURE)
                    .sized(1.1f, 0.7f).build("tortoisim"));


    public static void register(IEventBus eventBus)
    {
        ENTITY_TYPES.register(eventBus);
    }
}
