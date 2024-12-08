package net.elpasteltogrunon.journeyneymod.effects;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects
{
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, JourneyneyMod.MOD_ID);

    public static final RegistryObject<MobEffect> ARTHRITIS = MOB_EFFECTS.register("arthritis", ()-> new ArthritisEffect(MobEffectCategory.HARMFUL, 14866800));

    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
