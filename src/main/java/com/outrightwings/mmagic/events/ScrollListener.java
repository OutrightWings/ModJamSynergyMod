package com.outrightwings.mmagic.events;

import com.outrightwings.mmagic.elements.Elements;
import com.outrightwings.mmagic.item.Wand;
import com.outrightwings.mmagic.network.components.ModComponents;
import com.outrightwings.mmagic.network.components.SelectedFormComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

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
                if(item.getItem() instanceof Wand){
                    cancel = true;
                    int form = item.get(ModComponents.SELECTED_FORM_COMPONENT).id();
                    form += ((int)delta);
                    form = form >= Elements.CastingForms.values().length ? 0 : form;
                    form = form < 0 ? Elements.CastingForms.values().length-1 : form;
                    var formComp = new SelectedFormComponent(form);
                    item.set(ModComponents.SELECTED_FORM_COMPONENT, formComp);
                    PacketDistributor.sendToServer(formComp);
                }
            }
        }
        return cancel;
    }
}
