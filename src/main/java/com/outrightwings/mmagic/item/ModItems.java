package com.outrightwings.mmagic.item;

import com.outrightwings.mmagic.Main;
import com.outrightwings.mmagic.elements.Elements;
import com.outrightwings.mmagic.network.components.ModComponents;
import com.outrightwings.mmagic.network.components.SelectedElementsComponent;
import com.outrightwings.mmagic.network.components.SelectedFormComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Main.MODID);

    public static final DeferredItem<Item> BLUE_WAND = registerWand("blue_wand");
    public static final DeferredItem<Item> RED_WAND = registerWand("red_wand");
    public static final DeferredItem<Item> GREEN_WAND = registerWand("green_wand");
    public static final DeferredItem<Item> PURPLE_WAND = registerWand("purple_wand");
    public static final DeferredItem<Item> WHITE_WAND = registerWand("white_wand");
    public static final DeferredItem<Item> LIGHT_BLUE_WAND = registerWand("light_blue_wand");

    public static DeferredItem<Item> registerWand(String id){
        return ITEMS.register(id,()->new Wand(new Item.Properties()
                .stacksTo(1)
                .component(ModComponents.SELECTED_ELEMENTS_COMPONENT.value(),new SelectedElementsComponent(new int[Elements.MAX_SELECTED]))
                .component(ModComponents.SELECTED_FORM_COMPONENT.value(),new SelectedFormComponent(0))));
    }

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_CREATIVE_TAB = CREATIVE_MODE_TABS.register("mmagic_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.mmagic"))
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .icon(() -> BLUE_WAND.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(BLUE_WAND.get());
            output.accept(RED_WAND.get());
            output.accept(GREEN_WAND.get());
            output.accept(PURPLE_WAND.get());
            output.accept(WHITE_WAND.get());
            output.accept(LIGHT_BLUE_WAND.get());
        }).build());
}
