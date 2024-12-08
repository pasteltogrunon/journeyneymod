package net.elpasteltogrunon.journeyneymod.item;

import net.elpasteltogrunon.journeyneymod.effects.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties EDIBLE_NABE = new FoodProperties.Builder().nutrition(3).alwaysEat()
            .saturationMod(0.7f).effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 10, 10), 0.9f).build();
    public static final FoodProperties ARTRITHE= new FoodProperties.Builder().nutrition(1).alwaysEat()
            .saturationMod(0.7f).effect(() -> new MobEffectInstance(ModEffects.ARTHRITIS.get(), 150, 1), 1).build();
}
