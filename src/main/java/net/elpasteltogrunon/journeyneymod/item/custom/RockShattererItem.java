package net.elpasteltogrunon.journeyneymod.item.custom;

import net.elpasteltogrunon.journeyneymod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class RockShattererItem extends Item
{
    private static final Map<Block, Block> blockMap = new HashMap<Block, Block>()
    {
        {
            put(Blocks.STONE, Blocks.GRAVEL);
            put(Blocks.ANDESITE, ModBlocks.ANDESITE_GRAVEL.get());
            put(Blocks.DIORITE, ModBlocks.DIORITE_GRAVEL.get());
            put(Blocks.GRANITE, ModBlocks.GRANITE_GRAVEL.get());
            put(Blocks.TUFF, ModBlocks.TUFF_GRAVEL.get());
            put(Blocks.DEEPSLATE, ModBlocks.DEEPSLATE_GRAVEL.get());
        }
    };

    public RockShattererItem(Properties pProperties){super(pProperties);}

    @Override
    public InteractionResult useOn(UseOnContext pContext)
    {
        BlockPos clickedPos = pContext.getClickedPos();
        Block clickedBlock = pContext.getLevel().getBlockState(clickedPos).getBlock();
        if(blockMap.containsKey(clickedBlock))
        {
            Player clickingPlayer = pContext.getPlayer();
            if(!pContext.getLevel().isClientSide()) {
                //Server side
                pContext.getLevel().setBlock(clickedPos, blockMap.get(clickedBlock).defaultBlockState(), 3);
                ServerLevel serverLevel = (ServerLevel) pContext.getLevel();
                showFX(serverLevel, clickedPos, clickedBlock);

                if(!pContext.getPlayer().isCreative()){
                    pContext.getItemInHand().hurtAndBreak(1, clickingPlayer,
                            _player -> _player.broadcastBreakEvent(_player.getUsedItemHand()));
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;

    }

    private void showFX(ServerLevel serverLevel, BlockPos pos, Block block)
    {
        for(int i = 0; i<9; i++)
        {
            double a = serverLevel.random.nextDouble();
            double b = serverLevel.random.nextDouble();
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, block.defaultBlockState()), pos.getX()+a, pos.getY()+1.1d, pos.getZ()+b, 1,0, 0.1d, 0, 0);
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, block.defaultBlockState()), pos.getX()+1.1, pos.getY()+b, pos.getZ()+a, 1,0, 0.1d, 0, 0);
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, block.defaultBlockState()), pos.getX()+b, pos.getY()+a, pos.getZ()+1.1d, 1,0, 0.1d, 0, 0);
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, block.defaultBlockState()), pos.getX()+a, pos.getY()+b, pos.getZ()-0.1d, 1,0, 0.1d, 0, 0);
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, block.defaultBlockState()), pos.getX()-0.1d, pos.getY()+a, pos.getZ()+b, 1,0, 0.1d, 0, 0);
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, block.defaultBlockState()), pos.getX()+b, pos.getY()-0.1d, pos.getZ()+a, 1,0, 0.1d, 0, 0);
        }

        for(int i=0; i<3; i++)
        {

        }
            serverLevel.sendParticles(ParticleTypes.SMOKE, pos.getX()+serverLevel.random.nextDouble(), pos.getY()+1.1d, pos.getZ()+serverLevel.random.nextDouble(), 1,0, 0.1d, 0, 0);

        serverLevel.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1, 1);
    }
}
