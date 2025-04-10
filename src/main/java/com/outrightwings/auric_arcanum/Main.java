package com.outrightwings.auric_arcanum;

import com.outrightwings.auric_arcanum.blocks.ModBlocks;
import com.outrightwings.auric_arcanum.entity.ModEntities;
import com.outrightwings.auric_arcanum.events.ClientModEvents;
import com.outrightwings.auric_arcanum.network.components.ModComponents;
import com.outrightwings.auric_arcanum.item.ModItems;
import com.outrightwings.auric_arcanum.ui.ModMenus;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
@Mod(Main.MODID)
public class Main
{
    public static final String MODID = "auric_arcanum";
    public static final Logger LOGGER = LogUtils.getLogger();
    public Main(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModComponents.COMPONENTS.register(modEventBus);
        ModItems.CREATIVE_MODE_TABS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        modEventBus.register(ClientModEvents.class);
        modEventBus.register(ModComponents.class);
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }
}
