package com.outrightwings.mmagic.events;

import com.outrightwings.mmagic.Main;
import com.outrightwings.mmagic.ui.ElementScreen;
import com.outrightwings.mmagic.ui.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event){
        NeoForge.EVENT_BUS.addListener((InputEvent.MouseScrollingEvent e) -> {
            Main.LOGGER.debug("SCROLLLLLL");
            var cancel = false;//ShiftScrollListener.onScrollInGameplay(e.getScrollDelta());
            e.setCanceled(cancel);
        });
    }
    @SubscribeEvent
    public static void screens(RegisterMenuScreensEvent event){
        event.register(ModMenus.ELEMENT_MENU.get(), ElementScreen::new);
    }
}