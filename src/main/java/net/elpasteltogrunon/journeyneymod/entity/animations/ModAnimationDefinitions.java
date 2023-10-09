package net.elpasteltogrunon.journeyneymod.entity.animations;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class ModAnimationDefinitions
{
    public static final AnimationDefinition TORTOISIM_WALK = AnimationDefinition.Builder.withLength(1.4167667f).looping()
.addAnimation("shell",
	new AnimationChannel(AnimationChannel.Targets.POSITION, 
		new Keyframe(0f, KeyframeAnimations.posVec(0f, -0.15f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(0.16766666f, KeyframeAnimations.posVec(0f, 0f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(0.5f, KeyframeAnimations.posVec(0f, -0.3f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(0.8343334f, KeyframeAnimations.posVec(0f, 0f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.2083433f, KeyframeAnimations.posVec(0f, -0.3f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.4167667f, KeyframeAnimations.posVec(0f, -0.15f, 0f),
			AnimationChannel.Interpolations.LINEAR)))
.addAnimation("legs",
	new AnimationChannel(AnimationChannel.Targets.POSITION, 
		new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
			AnimationChannel.Interpolations.LINEAR)))
.addAnimation("head",
	new AnimationChannel(AnimationChannel.Targets.POSITION, 
		new Keyframe(0f, KeyframeAnimations.posVec(0f, -0.4f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(0.375f, KeyframeAnimations.posVec(0f, 0.25f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(0.5f, KeyframeAnimations.posVec(0f, -1f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.0834333f, KeyframeAnimations.posVec(0f, 0f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.2083433f, KeyframeAnimations.posVec(0f, -1f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.4167667f, KeyframeAnimations.posVec(0f, -0.4f, 0f),
			AnimationChannel.Interpolations.LINEAR)))
.addAnimation("bone4",
	new AnimationChannel(AnimationChannel.Targets.POSITION, 
		new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, -3f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(0.375f, KeyframeAnimations.posVec(0f, 1f, 2f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 3f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.4167667f, KeyframeAnimations.posVec(0f, 0f, -3f),
			AnimationChannel.Interpolations.LINEAR)))
.addAnimation("bone",
	new AnimationChannel(AnimationChannel.Targets.POSITION, 
		new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 2f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.2083433f, KeyframeAnimations.posVec(0f, 0f, -2f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.2916767f, KeyframeAnimations.posVec(0f, 1f, 1f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.4167667f, KeyframeAnimations.posVec(0f, 0f, 2f),
			AnimationChannel.Interpolations.LINEAR)))
.addAnimation("bone2",
	new AnimationChannel(AnimationChannel.Targets.POSITION, 
		new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, -1f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, -2f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(0.5834334f, KeyframeAnimations.posVec(0f, 1f, 1.5f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(0.7083434f, KeyframeAnimations.posVec(0f, 0f, 2f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.4167667f, KeyframeAnimations.posVec(0f, 0f, -1f),
			AnimationChannel.Interpolations.LINEAR)))
.addAnimation("bone3",
	new AnimationChannel(AnimationChannel.Targets.POSITION, 
		new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 2f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(0.7083434f, KeyframeAnimations.posVec(0f, 0f, -2f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.0834333f, KeyframeAnimations.posVec(0f, 1f, 2f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.2083433f, KeyframeAnimations.posVec(0f, 0f, 3f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(1.4167667f, KeyframeAnimations.posVec(0f, 0f, 2f),
			AnimationChannel.Interpolations.LINEAR))).build();
public static final AnimationDefinition TORTOISIM_IDLE = AnimationDefinition.Builder.withLength(4f).looping()
.addAnimation("shell",
	new AnimationChannel(AnimationChannel.Targets.POSITION, 
		new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(2f, KeyframeAnimations.posVec(0f, -0.2f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(4f, KeyframeAnimations.posVec(0f, 0f, 0f),
			AnimationChannel.Interpolations.LINEAR)))
.addAnimation("head",
	new AnimationChannel(AnimationChannel.Targets.POSITION, 
		new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(2.375f, KeyframeAnimations.posVec(0f, -0.2f, 0f),
			AnimationChannel.Interpolations.LINEAR), 
		new Keyframe(4f, KeyframeAnimations.posVec(0f, 0f, 0f),
			AnimationChannel.Interpolations.LINEAR))).build();
}
