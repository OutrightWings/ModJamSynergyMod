package com.outrightwings.mmagic.item.components;

import com.outrightwings.mmagic.Main;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Main.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SelectedElementsComponent>> SELECTED_ELEMENTS_COMPONENT = COMPONENTS.registerComponentType(
            "elements",
            builder -> builder
                    .persistent(SelectedElementsComponent.CODEC)
                    .networkSynchronized(SelectedElementsComponent.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SelectedFormComponent>> SELECTED_FORM_COMPONENT = COMPONENTS.registerComponentType(
            "form",
            builder -> builder
                    .persistent(SelectedFormComponent.CODEC)
                    .networkSynchronized(SelectedFormComponent.STREAM_CODEC)
    );

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                SelectedElementsComponent.TYPE,
                SelectedElementsComponent.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        SelectedElementsComponent::handleDataOnMain,
                        SelectedElementsComponent::handleDataOnMain
                )
        );
        registrar.playBidirectional(
                SelectedFormComponent.TYPE,
                SelectedFormComponent.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        SelectedFormComponent::handleDataOnMain,
                        SelectedFormComponent::handleDataOnMain
                )
        );
    }
}
