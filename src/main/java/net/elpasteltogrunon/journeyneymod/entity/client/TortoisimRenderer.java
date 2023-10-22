package net.elpasteltogrunon.journeyneymod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.elpasteltogrunon.journeyneymod.entity.custom.TortoisimEntity;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class TortoisimRenderer extends MobRenderer<TortoisimEntity, TortoisimModel<TortoisimEntity>>
{
    private static final ResourceLocation TEXTURE  = new ResourceLocation(JourneyneyMod.MOD_ID, "textures/entity/tortoisim.png");


    public TortoisimRenderer(EntityRendererProvider.Context pContext) 
    {
        super(pContext, new TortoisimModel<>(pContext.bakeLayer(ModModelLayers.TORTOISIM_LAYER)), 1.1f);
    }

    @Override
    public ResourceLocation getTextureLocation(TortoisimEntity pEntity) 
    {
        return TEXTURE;
    }

    @Override
    public void render(TortoisimEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) 
        {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
    
}
