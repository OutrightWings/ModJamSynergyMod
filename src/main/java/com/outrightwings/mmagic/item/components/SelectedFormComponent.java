package com.outrightwings.mmagic.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.outrightwings.mmagic.Main;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SelectedFormComponent {
    public record SelectedForm(int id) implements CustomPacketPayload{
        public static final CustomPacketPayload.Type<SelectedFormComponent.SelectedForm> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MODID, "selected_form_packet"));
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static final Codec<SelectedForm> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("id").forGetter(SelectedForm::id)
            ).apply(instance, SelectedForm::new)
    );
    public static final StreamCodec<ByteBuf, SelectedForm> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SelectedForm::id,
            SelectedForm::new
    );

    public static void handleDataOnMain(final SelectedFormComponent.SelectedForm data, final IPayloadContext context) {
        Player player = context.player();

        ItemStack stack = player.getMainHandItem();
        if(stack.has(ModComponents.SELECTED_FORM_COMPONENT)){
            stack.set(ModComponents.SELECTED_FORM_COMPONENT,data);
        }
    }
}