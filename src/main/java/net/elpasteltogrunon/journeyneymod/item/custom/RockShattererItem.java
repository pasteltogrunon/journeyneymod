package net.elpasteltogrunon.journeyneymod.item.custom;

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
import net.minecraft.world.level.block.Blocks;

public class RockShattererItem extends Item
{
    public RockShattererItem(Properties pProperties){super(pProperties);}

    @Override
    public InteractionResult useOn(UseOnContext pContext)
    {
        BlockPos clickedPos = pContext.getClickedPos();
        if(pContext.getLevel().getBlockState(clickedPos).getBlock() == Blocks.STONE)
        {
            Player clickingPlayer = pContext.getPlayer();
            if(!pContext.getLevel().isClientSide()) {
                //Server side
                pContext.getLevel().setBlock(clickedPos, Blocks.GRAVEL.defaultBlockState(), 3);
                ServerLevel serverLevel = (ServerLevel) pContext.getLevel();
                showFX(serverLevel, clickedPos);

                if(!pContext.getPlayer().isCreative()){
                    pContext.getItemInHand().hurtAndBreak(1, clickingPlayer,
                            _player -> _player.broadcastBreakEvent(_player.getUsedItemHand()));
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;

    }

    private void showFX(ServerLevel serverLevel, BlockPos pos)
    {
        for(int i = 0; i<9; i++)
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()), pos.getX()+serverLevel.random.nextDouble(), pos.getY()+1.1d, pos.getZ()+serverLevel.random.nextDouble(), 1,0, 0.1d, 0, 0);

        for(int i=0; i<3; i++)
            serverLevel.sendParticles(ParticleTypes.SMOKE, pos.getX()+serverLevel.random.nextDouble(), pos.getY()+1.1d, pos.getZ()+serverLevel.random.nextDouble(), 1,0, 0.1d, 0, 0);

        serverLevel.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1, 1);
    }
}
