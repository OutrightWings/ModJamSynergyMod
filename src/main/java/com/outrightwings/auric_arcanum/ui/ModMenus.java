package com.outrightwings.auric_arcanum.ui;

import com.outrightwings.auric_arcanum.Main;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, Main.MODID);

    public static final DeferredHolder<MenuType<?>,MenuType<ElementMenu>> ELEMENT_MENU = MENUS.register("element_menu",()-> new MenuType<ElementMenu>(ElementMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
