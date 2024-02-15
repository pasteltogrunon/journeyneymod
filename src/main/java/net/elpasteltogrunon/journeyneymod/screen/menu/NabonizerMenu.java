package net.elpasteltogrunon.journeyneymod.screen.menu;

import java.util.List;

import net.elpasteltogrunon.journeyneymod.block.ModBlocks;
import net.elpasteltogrunon.journeyneymod.block.entity.NabonizerBlockEntity;
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

public class NabonizerMenu extends MachineMenu<NabonizerBlockEntity>
{

    public NabonizerMenu(int id, Inventory inv, FriendlyByteBuf extraData)
    {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public NabonizerMenu(int id, Inventory inv, BlockEntity entity, ContainerData data)
    {
        super(ModMenuTypes.NABONIZER_MENU.get(), id, inv, entity, data, 4);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 12, 26));
            this.addSlot(new SlotItemHandler(handler, 1, 12, 49));
            this.addSlot(new SlotItemHandler(handler, 2, 86, 15));
            this.addSlot(new OutputSlot(handler, 3, 86, 60));
        });

        progressArrowSize = 26;
        energyBarSize = 64;
    }

    @Override
    public boolean stillValid(Player player) 
    {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, ModBlocks.NABONIZER.get());
    }

    
}
