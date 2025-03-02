package com.outrightwings.mmagic.ui;

import com.outrightwings.mmagic.Main;
import com.outrightwings.mmagic.elements.Elements;
import com.outrightwings.mmagic.item.ModComponents;
import com.outrightwings.mmagic.item.components.SelectedElementsComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ElementScreen extends AbstractContainerScreen<ElementMenu> {
    private static final ResourceLocation BG_LOCATION = ResourceLocation.fromNamespaceAndPath(Main.MODID,"textures/gui/container/element_gui.png");

    private final int ELEMENT_BUTTON_SIZE = 32;
    private final int[][] ELEMENT_BUTTON_CORNERS = {{48,16},{96,16},{128,64},{96,112},{48,112},{16,64}}; //in element order 0-5
    private final int ELEMENT_SMALL_ICON_SIZE = 10;
    private final int SELECTED_BAR_X = 71;
    private final int SELECTED_BAR_Y = 69;
    private final int SELECTED_BAR_GAP = 2;

    private final int ELEMENT_HIGHLIGHT_X = 256-ELEMENT_BUTTON_SIZE;
    private final int ELEMENT_HIGHLIGHT_Y = 64;
    Inventory inventory;
    public ElementScreen(ElementMenu menu, Inventory inventory, Component name) {
        super(menu,inventory,name);
        this.inventory = inventory;
    }
    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick){
        this.renderBackground(gui,mouseX,mouseY,partialTick);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        gui.blit(BG_LOCATION, x, y, 0, 0, this.imageWidth, this.imageHeight);

        //Loop through all element positions to check for hover
        for(int i = 0; i < ELEMENT_BUTTON_CORNERS.length; i++){
            int cx = x+ELEMENT_BUTTON_CORNERS[i][0];
            int cy = y+ELEMENT_BUTTON_CORNERS[i][1];
            if(inBounds(mouseX,mouseY,cx,cy,ELEMENT_BUTTON_SIZE)){
                gui.blit(BG_LOCATION, cx, cy, ELEMENT_HIGHLIGHT_X, ELEMENT_HIGHLIGHT_Y, ELEMENT_BUTTON_SIZE, ELEMENT_BUTTON_SIZE);
                break;
            }
        }

        //Loop through selected on wand
        ItemStack wand = inventory.getItem(inventory.selected);
        SelectedElementsComponent.SelectedElements selectedElements = wand.get(ModComponents.SELECTED_ELEMENTS_COMPONENT);
        int[] list = selectedElements.elements();
        for(int i = 0; i < list.length; i++){
            if(list[i] != 0){
                gui.blit(BG_LOCATION, SELECTED_BAR_X+x+((ELEMENT_SMALL_ICON_SIZE+2) *(i%3)), SELECTED_BAR_Y+y+((ELEMENT_SMALL_ICON_SIZE+2) *(i/3)), 256-ELEMENT_SMALL_ICON_SIZE, ELEMENT_SMALL_ICON_SIZE*(list[i]-1), ELEMENT_SMALL_ICON_SIZE, ELEMENT_SMALL_ICON_SIZE);
            }
        }
    }
    public boolean mouseClicked(double mouseX, double mouseY, int button){
        int x = this.leftPos;
        int y = this.topPos;
        //Loop through all element positions to check for click
        for(int i = 0; i < ELEMENT_BUTTON_CORNERS.length; i++){
            int cx = x+ELEMENT_BUTTON_CORNERS[i][0];
            int cy = y+ELEMENT_BUTTON_CORNERS[i][1];
            if(inBounds((int)mouseX,(int)mouseY,cx,cy,ELEMENT_BUTTON_SIZE)){
                addElement(i+1);
                return true;
            }
        }
        return super.mouseClicked(mouseX,mouseY,button);
    }
    private boolean inBounds(int mouseX, int mouseY, int x, int y, int width){
        boolean hoz = mouseX >= x && mouseX <= x + width;
        boolean vert = mouseY >= y && mouseY <= y +width;
        return hoz && vert;
    }
    private void addElement(int id){
        ItemStack wand = inventory.getItem(inventory.selected);
        SelectedElementsComponent.SelectedElements selectedElements = wand.get(ModComponents.SELECTED_ELEMENTS_COMPONENT);
        int[] list = selectedElements.elements();
        for(int i = 0; i < list.length; i++){
            if(list[i] == 0){
                //todo Fancy logic when you selected conflicting types. Life cancells out death/ maybe water + fire = steam?
                list[i] = id;
                break;
            }
        }
        wand.set(ModComponents.SELECTED_ELEMENTS_COMPONENT,new SelectedElementsComponent.SelectedElements(list));
    }
}
