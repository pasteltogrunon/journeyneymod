package net.elpasteltogrunon.journeyneymod.item.custom;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.elpasteltogrunon.journeyneymod.enchantment.ModEnchantments;
import net.elpasteltogrunon.journeyneymod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;


public class NabonyteItem extends Item
{
    private int destroySize = 1;
    private int cost = 5;

    //@ObjectHolder(registryName = "minecraft:item", value="journeyneymod:nabonite_dust")
    public static Item NABONIZED_GUNPOWDER = ModItems.NABONIZED_GUNPOWDER.get();

    public NabonyteItem(Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext)
    {
        if(!pContext.getLevel().isClientSide())
        {
            BlockPos posClicked = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            Inventory inventory = player.getInventory();
            
            int naboniac_level = pContext.getItemInHand().getEnchantmentLevel(ModEnchantments.NABONIAC.get());
            
            if(player.isCreative())
            {
                destroyArea(pContext, posClicked, destroySize + naboniac_level, false);
                return InteractionResult.SUCCESS;
            }
            
            int nabonitic_rest_level = pContext.getItemInHand().getEnchantmentLevel(ModEnchantments.NABONYTIC_REST.get());
            int totalCost = Math.max(1, cost + naboniac_level * naboniac_level - nabonitic_rest_level*2);

            int slot = inventory.findSlotMatchingItem(new ItemStack(NABONIZED_GUNPOWDER));

            if(slot != -1 && inventory.getItem(slot).getCount() >= totalCost)
            {
                destroyArea(pContext, posClicked, destroySize + naboniac_level, true);
                inventory.removeItem(slot, totalCost);
                
                pContext.getItemInHand().hurtAndBreak(1, player,
                    _player -> _player.broadcastBreakEvent(_player.getUsedItemHand()));
                
                return InteractionResult.SUCCESS;
            }

        }

        return InteractionResult.FAIL;
    }

    private void destroyArea(UseOnContext pContext, BlockPos posClicked, int size, boolean doDrop)
    {
        for(int i=-size; i<=size; i++)
                for(int j=-1; j<=size*2-1; j++)
                    for(int k=-size; k<=size; k++)
                    {
                        BlockPos thisPos = new BlockPos(posClicked.getX() + i, posClicked.getY() + j, posClicked.getZ() + k);
                        pContext.getLevel().destroyBlock(thisPos, doDrop);
                    }
    }

    public int getEnchantmentValue() {
        return 15;
     }

}
