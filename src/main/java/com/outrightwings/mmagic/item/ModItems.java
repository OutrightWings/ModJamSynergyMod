package com.outrightwings.mmagic.item;

import com.outrightwings.mmagic.Main;
import com.outrightwings.mmagic.elements.Elements;
import com.outrightwings.mmagic.item.components.SelectedElementsComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Main.MODID);

    public static final DeferredItem<Item> WAND = ITEMS.register("wand",()->new Wand(new Item.Properties().stacksTo(1).component(ModComponents.SELECTED_ELEMENTS_COMPONENT.value(),new SelectedElementsComponent.SelectedElements(new int[Elements.MAX_SELECTED]))));
    //public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);
    //public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(1).saturationModifier(2f).build()));



    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_CREATIVE_TAB = CREATIVE_MODE_TABS.register("mmagic_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.mmagic"))
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .icon(() -> WAND.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(WAND.get()); // Add the example item to the tab.
        }).build());
}
