package net.elpasteltogrunon.journeyneymod.block.entity;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.elpasteltogrunon.journeyneymod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities 
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
         DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, JourneyneyMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<NabonizerBlockEntity>> NABONIZER = 
        BLOCK_ENTITIES.register("nabonizer", ()-> BlockEntityType.Builder.of(
            NabonizerBlockEntity:: new, ModBlocks.NABONIZER.get()).build(null));

    public static final RegistryObject<BlockEntityType<NabonyticGeneratorBlockEntity>> NABONYTIC_GENERATOR = 
        BLOCK_ENTITIES.register("nabonytic_generator", ()-> BlockEntityType.Builder.of(
            NabonyticGeneratorBlockEntity:: new, ModBlocks.NABONYTIC_GENERATOR.get()).build(null));

    
    public static void register(IEventBus eventBus)
    {
        BLOCK_ENTITIES.register(eventBus);
    }
}
