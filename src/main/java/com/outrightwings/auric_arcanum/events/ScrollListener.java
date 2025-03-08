package com.outrightwings.auric_arcanum.events;

import com.outrightwings.auric_arcanum.elements.Elements;
import com.outrightwings.auric_arcanum.item.Wand;
import com.outrightwings.auric_arcanum.network.components.ModComponents;
import com.outrightwings.auric_arcanum.network.components.SelectedFormComponent;
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
