package net.elpasteltogrunon.journeyneymod.screen.menu;

import java.util.List;

import net.elpasteltogrunon.journeyneymod.block.ModBlocks;
import net.elpasteltogrunon.journeyneymod.block.entity.NabonyticFurnaceBlockEntity;
import net.elpasteltogrunon.journeyneymod.screen.menu.slot.OutputSlot;
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

public class NabonyticFurnaceMenu extends MachineMenu<NabonyticFurnaceBlockEntity>
{

    public NabonyticFurnaceMenu(int id, Inventory inv, FriendlyByteBuf extraData)
    {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public NabonyticFurnaceMenu(int id, Inventory inv, BlockEntity entity, ContainerData data)
    {
        super(ModMenuTypes.NABONYTIC_FURNACE_MENU.get(), id, inv, entity, data, 3);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 36, 19));
            this.addSlot(new SlotItemHandler(handler, 1, 36, 55));
            this.addSlot(new OutputSlot(handler, 2, 96, 37));
        });

        progressArrowSize = 22;
    }

    @Override
    public boolean stillValid(Player player) 
    {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, ModBlocks.NABONYTIC_FURNACE.get());
    }

}
