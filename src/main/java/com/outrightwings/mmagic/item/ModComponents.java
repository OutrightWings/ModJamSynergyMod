package com.outrightwings.mmagic.item;

import com.outrightwings.mmagic.Main;
import com.outrightwings.mmagic.item.components.SelectedElementsComponent;
import com.outrightwings.mmagic.item.components.SelectedFormComponent;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;

import static com.outrightwings.mmagic.item.components.SelectedElementsComponent.SELECTED_ELEMENTS_CODEC;
import static com.outrightwings.mmagic.item.components.SelectedElementsComponent.SELECTED_ELEMENTS_STREAM_CODEC;
import static com.outrightwings.mmagic.item.components.SelectedFormComponent.SELECTED_FORM_CODEC;
import static com.outrightwings.mmagic.item.components.SelectedFormComponent.SELECTED_FORM_STREAM_CODEC;

public class ModComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Main.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SelectedElementsComponent.SelectedElements>> SELECTED_ELEMENTS_COMPONENT = COMPONENTS.registerComponentType(
            "elements",
            builder -> builder
                    .persistent(SELECTED_ELEMENTS_CODEC)
                    .networkSynchronized(SELECTED_ELEMENTS_STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SelectedFormComponent.SelectedForm>> SELECTED_FORM_COMPONENT = COMPONENTS.registerComponentType(
            "form",
            builder -> builder
                    .persistent(SELECTED_FORM_CODEC)
                    .networkSynchronized(SELECTED_FORM_STREAM_CODEC)
    );
}
