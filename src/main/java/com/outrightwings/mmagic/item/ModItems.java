package com.outrightwings.mmagic.item;

import com.outrightwings.mmagic.Main;
import com.outrightwings.mmagic.elements.Elements;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Main.MODID);

    public static final DeferredItem<Item> WAND = ITEMS.register("wand",()->new Wand(new Item.Properties().stacksTo(1).component(ModComponents.SELECTED_ELEMENTS_COMPONENT.value(),new ModComponents.SelectedElements(new int[Elements.MAX_SELECTED]))));
    //public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);
    //public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(1).saturationModifier(2f).build()));

}
