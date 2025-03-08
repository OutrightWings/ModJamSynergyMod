package com.outrightwings.auric_arcanum.events;

import com.outrightwings.auric_arcanum.Main;
import com.outrightwings.auric_arcanum.entity.InvisiblePotionRenderer;
import com.outrightwings.auric_arcanum.entity.ModEntities;
import com.outrightwings.auric_arcanum.ui.ElementScreen;
import com.outrightwings.auric_arcanum.ui.ModMenus;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
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
        EntityRenderers.register(ModEntities.MAGIC_BALL.get(), ThrownItemRenderer::new);
        EntityRenderers.register(ModEntities.INVISIBLE_POTION.get(), InvisiblePotionRenderer::new);
        NeoForge.EVENT_BUS.addListener((InputEvent.MouseScrollingEvent e) -> {
            var cancel = ScrollListener.onScroll(e.getScrollDeltaY());
            e.setCanceled(cancel);
        });
    }
    @SubscribeEvent
    public static void screens(RegisterMenuScreensEvent event){
        event.register(ModMenus.ELEMENT_MENU.get(), ElementScreen::new);
    }
}