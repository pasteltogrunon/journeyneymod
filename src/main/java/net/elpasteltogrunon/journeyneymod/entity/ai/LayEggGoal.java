package net.elpasteltogrunon.journeyneymod.entity.ai;

import net.elpasteltogrunon.journeyneymod.util.EggLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class LayEggGoal extends MoveToBlockGoal
{
    private final Animal mob;

    private final EggLayer eggLayer;

    private final int maxTime;

    private int layEggCounter;

    private float startBodyRot;

    public LayEggGoal(Animal mob, int maxTime, double speed) {
        super(mob, speed, 16);
        this.mob = mob;
        this.eggLayer = (EggLayer) this.mob;
        this.maxTime = maxTime;
    }

    public boolean canUse() 
    {
        return super.canUse() && this.eggLayer.hasEgg();
    }

    public boolean canContinueToUse()
     {
        return super.canContinueToUse() && this.eggLayer.hasEgg();
    }

    public void start() {
        super.start();
        layEggCounter = 0;
    }

    public double acceptedDistance() 
    {
        return 4.0D;
    }

    public void tick() {
        super.tick();
        if (this.isReachedTarget())
        {
            //eggLayer.onLayEggTick(this.blockPos.above(), layEggCounter);
            if (this.layEggCounter++ > this.maxTime) {
                Level level = this.mob.level();
                level.playSound((Player) null, blockPos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
                BlockPos blockpos1 = this.blockPos.above();
                BlockState egg = eggLayer.getEggBlockState();
                level.setBlockAndUpdate(blockpos1, egg);
                level.gameEvent(GameEvent.BLOCK_PLACE, blockpos1, GameEvent.Context.of(this.mob, egg));
                this.eggLayer.setHasEgg(false);
                this.mob.setInLoveTime(600);
                this.mob.level().broadcastEntityEvent(this.mob, (byte) 78);
            }
        } else {
            startBodyRot = mob.yBodyRot;
            layEggCounter = 0;
        }

    }

    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        return levelReader.isEmptyBlock(blockPos.above());
    }
}
