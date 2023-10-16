package net.elpasteltogrunon.journeyneymod.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.math.Axis;
import com.mojang.math.Transformation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;

import static net.elpasteltogrunon.journeyneymod.block.custom.CableBlock.propFromDir;
import static net.elpasteltogrunon.journeyneymod.event.ModEventBusClientEvents.CABLE_CORE;
import static net.elpasteltogrunon.journeyneymod.event.ModEventBusClientEvents.resFromDir;
import static net.elpasteltogrunon.journeyneymod.event.ModEventBusClientEvents.modelOf;

public class CableBlockModel implements IDynamicBakedModel
{
    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
            @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) 
    {
        List<BakedQuad> quads = new ArrayList<>();
        quads.addAll(modelOf(CABLE_CORE).getQuads(state, side, rand, extraData, renderType));
        for(Direction dir : Direction.values())
        {
            if(state.getValue(propFromDir.get(dir)))
                quads.addAll(modelOf(resFromDir.get(dir)).getQuads(state, side, rand, extraData, renderType));
        }
        return quads;
    }
    
    public static Transformation rotateTransformation(Direction toDirection) 
    {
        Quaternionf quaternion = new Quaternionf();
        switch (toDirection) {
        case UP -> quaternion.mul(Axis.ZP.rotationDegrees(180));
        case NORTH -> quaternion.mul(Axis.XP.rotationDegrees(90));
        case SOUTH -> quaternion.mul(Axis.XN.rotationDegrees(90));
        case WEST -> quaternion.mul(Axis.ZN.rotationDegrees(90));
        case EAST -> quaternion.mul(Axis.ZP.rotationDegrees(90));
        default -> {}
        }
        Transformation transformation = new Transformation(null, quaternion, null, null);
        return transformation.applyOrigin(new Vector3f(.5f, .5f, .5f));
    }

    @Nullable
    public static Direction rotateDirection(Direction toDirection, @Nullable Direction toTransform) 
    {
        if (toTransform == null) {
            return null;
        }

        return switch (toDirection) {
            case DOWN -> toTransform;
            case UP -> toTransform.getClockWise(Direction.Axis.Z).getClockWise(Direction.Axis.Z);
            case NORTH -> toTransform.getCounterClockWise(Direction.Axis.X).getClockWise(Direction.Axis.Y).getClockWise(Direction.Axis.Y);
            case SOUTH -> toTransform.getClockWise(Direction.Axis.X).getClockWise(Direction.Axis.Y).getClockWise(Direction.Axis.Y);
            case WEST -> toTransform.getCounterClockWise(Direction.Axis.Z).getClockWise(Direction.Axis.Y);
            case EAST -> toTransform.getClockWise(Direction.Axis.Z).getClockWise(Direction.Axis.Y);
        };
    }

    @Override
    public boolean useAmbientOcclusion() 
    {
        return false;
    }

    @Override
    public boolean isGui3d()
    {
        return false;
    }

    @Override
    public boolean usesBlockLight() 
    {
        return false;
    }

    @Override
    public boolean isCustomRenderer() 
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() 
    {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation());
    }

    @Override
    public ItemOverrides getOverrides() 
    {
        return ItemOverrides.EMPTY;
    }

    
}
