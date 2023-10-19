package net.elpasteltogrunon.journeyneymod.screen.menu;

import java.util.List;

import net.elpasteltogrunon.journeyneymod.block.ModBlocks;
import net.elpasteltogrunon.journeyneymod.block.entity.NabonyticGeneratorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class NabonyticGeneratorMenu extends AbstractContainerMenu
{
    public static NabonyticGeneratorBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public NabonyticGeneratorMenu(int id, Inventory inv, FriendlyByteBuf extraData)
    {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public NabonyticGeneratorMenu(int id, Inventory inv, BlockEntity entity, ContainerData data)
    {
        super(ModMenuTypes.NABONYTIC_GENERATOR_MENU.get(), id);
        checkContainerSize(inv, 1);
        blockEntity = (NabonyticGeneratorBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 80, 47));
        });

        addDataSlots(data);
    }

    public boolean isBurning() 
    {
        return data.get(0) > 0;
    }

    public int getScaledBurnTime() 
    {
        int burnTime = this.data.get(0);
        int burnTimeIndicatorSize = 15;

        return burnTime != 0 ? Math.min(burnTime * burnTimeIndicatorSize / 800, burnTimeIndicatorSize) : 0;
    }

    public int getScaledEnergy()
    {
        int energy = this.data.get(1);
        int maxEnergy = this.data.get(2); 
        int energyBarSize = 64; 

        return maxEnergy != 0 && energy != 0 ? energy * energyBarSize / maxEnergy : 0;
    }

    public List<Component> getEnergyTooltips()
    {
        int energy = this.data.get(1);
        int maxEnergy = this.data.get(2);
        return List.of(Component.literal(energy+"/"+maxEnergy+" NU"));
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = 1;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) 
    {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) 
        {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) 
            {
                return ItemStack.EMPTY;
            }
        }
         else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) 
         {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) 
            {
                return ItemStack.EMPTY;
            }
        }
        else 
        {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) 
        {
            sourceSlot.set(ItemStack.EMPTY);
        }
        else 
        {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) 
    {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, ModBlocks.NABONYTIC_GENERATOR.get());
    }

    private void addPlayerInventory(Inventory playerInventory) 
    {
        for (int i = 0; i < 3; ++i) 
            for (int l = 0; l < 9; ++l)
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));

    }

    private void addPlayerHotbar(Inventory playerInventory) 
    {
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
    }
    
}
