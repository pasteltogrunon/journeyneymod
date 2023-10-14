package net.elpasteltogrunon.journeyneymod.client;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;

public class CableBlockModel implements IDynamicBakedModel
{

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
            @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) 
    {
        throw new UnsupportedOperationException("Unimplemented method 'getQuads'");
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
        throw new UnsupportedOperationException("Unimplemented method 'getParticleIcon'");
    }

    @Override
    public ItemOverrides getOverrides() 
    {
        return ItemOverrides.EMPTY;
    }

    
}
