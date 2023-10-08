package net.elpasteltogrunon.journeyneymod.screen;

import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;

import net.elpasteltogrunon.journeyneymod.JourneyneyMod;
import net.elpasteltogrunon.journeyneymod.screen.menu.NabonizerMenu;
import net.elpasteltogrunon.journeyneymod.util.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.player.Inventory;

public class NabonizerScreen extends AbstractContainerScreen<NabonizerMenu>
{   
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(JourneyneyMod.MOD_ID,"textures/gui/nabonizer_gui.png");

    public NabonizerScreen(NabonizerMenu menu, Inventory inv, Component comp)
    {
        super(menu, inv, comp);
    }

    @Override
    protected void init() 
    {
        super.init();
    }
    
    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) 
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(graphics, x, y);
        renderEnergyBar(graphics, x, y);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int pMouseX, int pMouseY)
    {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(graphics, pMouseX, pMouseY, x, y);
        super.renderTooltip(graphics, pMouseX, pMouseY);
    }

    private void renderProgressArrow(GuiGraphics graphics, int x, int y) 
    {
        if(menu.isCrafting()) {
            graphics.blit(TEXTURE, x + 105, y + 33, 176, 0, 8, menu.getScaledProgress());
        }
    }

    private void renderEnergyBar(GuiGraphics graphics, int x, int y) 
    {
        graphics.blit(TEXTURE, x + 156, y + 13, 187, 0, 8, menu.getScaledEnergy());
    }

    private void renderEnergyAreaTooltips(GuiGraphics graphics, int pMouseX, int pMouseY, int x, int y)
     {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 156, 13, 8, 64)) 
        {
            graphics.renderComponentTooltip(this.font, menu.getEnergyTooltips(), pMouseX, pMouseY);
        }
    }
    
    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float delta)
    {
        renderBackground(graphics);
        super.render(graphics, pMouseX, pMouseY, delta);
        renderTooltip(graphics, pMouseX, pMouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height)
     {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
