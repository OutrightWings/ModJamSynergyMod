package com.outrightwings.mmagic.events;

import com.outrightwings.mmagic.Main;
import com.outrightwings.mmagic.elements.Elements;
import com.outrightwings.mmagic.item.ModComponents;
import com.outrightwings.mmagic.item.ModItems;
import com.outrightwings.mmagic.item.components.SelectedFormComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public class ScrollListener {
    public static boolean onScroll(double delta){
        boolean cancel = false;
        //Do not do it when there is a screen open!
        if(Minecraft.getInstance().screen == null) {
            LocalPlayer player = Minecraft.getInstance().player;
            //Player must be shifting
            if(player != null && player.isShiftKeyDown()){
                ItemStack item = player.getMainHandItem();
                //Check for wand Item
                if(item.is(ModItems.WAND.get())){
                    cancel = true;
                    int form = item.get(ModComponents.SELECTED_FORM_COMPONENT).id();
                    form += ((int)delta);
                    form = form >= Elements.forms.values().length ? 0 : form;
                    form = form < 0 ? Elements.forms.values().length-1 : form;
                    item.set(ModComponents.SELECTED_FORM_COMPONENT, new SelectedFormComponent.SelectedForm(form));
                }
            }
        }
        return cancel;
    }
}
