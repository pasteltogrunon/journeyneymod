package net.elpasteltogrunon.journeyneymod.recipe;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes 
{
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, JourneyneyMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<NabonizerRecipe>> NABONIZER_SERIALIZER =
            SERIALIZERS.register("nabonizing", () -> NabonizerRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
