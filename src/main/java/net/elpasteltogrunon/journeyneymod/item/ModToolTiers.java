package net.elpasteltogrunon.journeyneymod.item;

import java.util.List;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

public class ModToolTiers 
{
    public static Tier NABONITE = TierSortingRegistry.registerTier(
        new ForgeTier(2, 200, 14f, 1f, 50, 
        BlockTags.NEEDS_IRON_TOOL, ()-> Ingredient.of(ModItems.NABONITE_INGOT.get())),
        new ResourceLocation(JourneyneyMod.MOD_ID, "nabonite"), List.of(Tiers.STONE), List.of(Tiers.IRON));
    
}
