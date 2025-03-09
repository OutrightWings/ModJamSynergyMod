package com.outrightwings.auric_arcanum.item;

import com.outrightwings.auric_arcanum.Main;
import com.outrightwings.auric_arcanum.blocks.ModBlocks;
import com.outrightwings.auric_arcanum.blocks.TempMagicBlocks;
import com.outrightwings.auric_arcanum.elements.Elements;
import com.outrightwings.auric_arcanum.network.components.ModComponents;
import com.outrightwings.auric_arcanum.network.components.SelectedElementsComponent;
import com.outrightwings.auric_arcanum.network.components.SelectedFormComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
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

    public static final DeferredItem<BlockItem> TEMP_MAGIC_STONE = ITEMS.registerSimpleBlockItem("magic_stone", ModBlocks.TEMP_MAGIC_STONE, new Item.Properties());
    public static final DeferredItem<BlockItem> TEMP_MAGIC_MUD = ITEMS.registerSimpleBlockItem("magic_mud", ModBlocks.TEMP_MAGIC_MUD, new Item.Properties());
    public static final DeferredItem<BlockItem> TEMP_MAGIC_CLAY = ITEMS.registerSimpleBlockItem("magic_clay", ModBlocks.TEMP_MAGIC_CLAY, new Item.Properties());
    public static final DeferredItem<BlockItem> TEMP_MAGIC_ICE = ITEMS.registerSimpleBlockItem("magic_ice", ModBlocks.TEMP_MAGIC_ICE, new Item.Properties());
    public static final DeferredItem<BlockItem> TEMP_MAGIC_MAGMA = ITEMS.registerSimpleBlockItem("magic_magma", ModBlocks.TEMP_MAGIC_MAGMA, new Item.Properties());



    public static DeferredItem<Item> registerWand(String id){
        return ITEMS.register(id,()->new Wand(new Item.Properties()
                .stacksTo(1)
                .component(ModComponents.SELECTED_ELEMENTS_COMPONENT.value(),new SelectedElementsComponent(new int[Elements.MAX_SELECTED]))
                .component(ModComponents.SELECTED_FORM_COMPONENT.value(),new SelectedFormComponent(0))));
    }

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_CREATIVE_TAB = CREATIVE_MODE_TABS.register(Main.MODID+"_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup."+Main.MODID))
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
