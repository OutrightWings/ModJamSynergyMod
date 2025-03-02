package com.outrightwings.mmagic.ui;

import com.outrightwings.mmagic.Main;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ElementScreen extends AbstractContainerScreen<ElementMenu> {
    private static final ResourceLocation BG_LOCATION = ResourceLocation.fromNamespaceAndPath(Main.MODID,"textures/gui/container/element_gui.png");


    public ElementScreen(ElementMenu menu, Inventory inventory, Component name) {
        super(menu,inventory,name);
    }
    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick){
        this.renderBackground(gui,mouseX,mouseY,partialTick);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        gui.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
