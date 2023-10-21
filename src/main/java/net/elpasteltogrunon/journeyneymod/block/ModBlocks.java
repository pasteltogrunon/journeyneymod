package net.elpasteltogrunon.journeyneymod.block;

import java.util.function.Supplier;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.elpasteltogrunon.journeyneymod.block.custom.BatteryBlock;
import net.elpasteltogrunon.journeyneymod.block.custom.CableBlock;
import net.elpasteltogrunon.journeyneymod.block.custom.NabonizerBlock;
import net.elpasteltogrunon.journeyneymod.block.custom.NabonyticFurnaceBlock;
import net.elpasteltogrunon.journeyneymod.block.custom.NabonyticGeneratorBlock;
import net.elpasteltogrunon.journeyneymod.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks 
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JourneyneyMod.MOD_ID);

    public static final RegistryObject<Block> NABONITE_ORE = registerBlock("nabonite_ore", ()-> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE), UniformInt.of(0, 2)));
    public static final RegistryObject<Block> DEEPSLATE_NABONITE_ORE = registerBlock("deepslate_nabonite_ore", ()-> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE), UniformInt.of(0, 3)));
    public static final RegistryObject<Block> NABONITE_DUST_BLOCK = registerBlock("nabonite_dust_block", ()-> new Block(BlockBehaviour.Properties.copy(Blocks.SAND)));
    public static final RegistryObject<Block> NABONITE_BLOCK = registerBlock("nabonite_block", ()-> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> MACHINE_BLOCK = registerBlock("machine_block", ()-> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<NabonizerBlock> NABONIZER = registerBlock("nabonizer", ()-> new NabonizerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
        public static final RegistryObject<NabonyticFurnaceBlock> NABONYTIC_FURNACE = registerBlock("nabonytic_furnace", ()-> new NabonyticFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<NabonyticGeneratorBlock> NABONYTIC_GENERATOR = registerBlock("nabonytic_generator", ()-> new NabonyticGeneratorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<CableBlock> CABLE = registerBlock("cable", ()-> new CableBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_STAINED_GLASS).noCollission().sound(SoundType.WOOL).dynamicShape()));
    public static final RegistryObject<BatteryBlock> NABONITE_BATTERY = registerBlock("nabonite_battery", ()-> new BatteryBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), 40000, "Nabonite Battery"));
    
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block)
    {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block)
    {
        return ModItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus event)
    {
        BLOCKS.register(event);
    }

}