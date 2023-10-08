package net.elpasteltogrunon.journeyneymod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties EDIBLE_NABE = new FoodProperties.Builder().nutrition(3).alwaysEat()
            .saturationMod(0.7f).effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 10, 10), 0.9f).build();
}
