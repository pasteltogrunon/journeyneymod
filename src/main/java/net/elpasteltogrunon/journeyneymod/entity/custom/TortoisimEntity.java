package net.elpasteltogrunon.journeyneymod.entity.custom;

import javax.annotation.Nullable;

import net.elpasteltogrunon.journeyneymod.block.ModBlocks;
import net.elpasteltogrunon.journeyneymod.entity.ModEntities;
import net.elpasteltogrunon.journeyneymod.entity.ai.EggBreedGoal;
import net.elpasteltogrunon.journeyneymod.entity.ai.LayEggGoal;
import net.elpasteltogrunon.journeyneymod.item.ModItems;
import net.elpasteltogrunon.journeyneymod.util.EggLayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TortoisimEntity extends Animal implements EggLayer
{
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private boolean hasEgg = false;

    public TortoisimEntity(EntityType<? extends Animal> pEntityType, Level pLevel) 
    {
        super(pEntityType, pLevel);
    }

    @Override  
    public void tick()
    {
        super.tick();

        if(this.level().isClientSide())
        {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates()
    {
        if(this.idleAnimationTimeout <= 0)
        {
            this.idleAnimationTimeout = this.random.nextInt(40) + 80;
            this.idleAnimationState.start(this.tickCount);
        }
        else
        {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick)
    {
        float f;
        if(this.getPose() == Pose.STANDING)
        {
            f = Math.min(pPartialTick * 6f, 1f);
        }
        else
        {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(2, new EggBreedGoal(this, 2));
        this.goalSelector.addGoal(2, new LayEggGoal(this, 100, 1));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1, Ingredient.of(ModItems.EDIBLE_NABE.get()), false));

        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));

        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 3));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }


    public static AttributeSupplier.Builder createAttributes()
    {
        return Animal.createLivingAttributes()
            .add(Attributes.MAX_HEALTH, 30)
            .add(Attributes.MOVEMENT_SPEED, 0.13)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
            .add(Attributes.ARMOR_TOUGHNESS, 0.2)
            .add(Attributes.FOLLOW_RANGE, 30);
    }

    @Override
    @Nullable
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) 
    {
        return ModEntities.TORTOISIM.get().create(pLevel);
    }
    
    @Override
    public boolean isFood(ItemStack pStack)
    {
        return pStack.is(ModItems.EDIBLE_NABE.get());
    }

    @Override
    public boolean hasEgg() 
    {
        return this.hasEgg;
    }

    @Override
    public void setHasEgg(boolean hasEgg) 
    {
        this.hasEgg = hasEgg;
    }

    @Override
    public BlockState getEggBlockState() 
    {
        return ModBlocks.TORTOSIM_EGG.get().defaultBlockState();
    }
}
