package net.elpasteltogrunon.journeyneymod.block.custom;

import net.elpasteltogrunon.journeyneymod.entity.ModEntities;
import net.elpasteltogrunon.journeyneymod.entity.custom.TortoisimEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TortoisimEggBlock extends Block
{
    public TortoisimEggBlock(Properties arg0) 
    {
        super(arg0);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return Shapes.box(.1875, 0, .1875, .8125, .6875, .8125);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) 
    {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) 
    {
        TortoisimEntity tortoisimEntity = ModEntities.TORTOISIM.get().spawn(world, pos, MobSpawnType.BREEDING);
        tortoisimEntity.setBaby(true);
        world.addFreshEntity(tortoisimEntity);
        world.playSound((Player) null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F);
        world.destroyBlock(pos, false);
    }
}
