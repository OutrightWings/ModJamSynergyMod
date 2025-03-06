package com.outrightwings.mmagic.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.outrightwings.mmagic.Main;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Arrays;

public class SelectedElementsComponent{
    public record SelectedElements(int[] elements) implements CustomPacketPayload {
        public SelectedElements{
            elements = elements.clone();
        }
        public int[] elements(){
            return elements.clone();
        }
        public static final CustomPacketPayload.Type<SelectedElements> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MODID, "selected_elements_packet"));

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
    public static final Codec<SelectedElements> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.listOf().fieldOf("elements").forGetter(record -> Arrays.stream(record.elements).boxed().toList())
            ).apply(instance, list -> new SelectedElements(list.stream().mapToInt(Integer::intValue).toArray()))
    );
    public static final StreamCodec<ByteBuf, SelectedElements> STREAM_CODEC = StreamCodec.of(
            (buf,record) -> {
                buf.writeInt(record.elements.length);
                for(int v : record.elements){
                    buf.writeInt(v);
                }
            },
            buf -> {
                int l = buf.readInt();
                int[] values = new int[l];
                for(int i = 0; i < l; i++){
                    values[i] = buf.readInt();
                }
                return new SelectedElements(values);
            }
    );

    public static void handleDataOnMain(final SelectedElements data, final IPayloadContext context) {
        Player player = context.player();

        ItemStack stack = player.getMainHandItem();
        if(stack.has(ModComponents.SELECTED_ELEMENTS_COMPONENT)){
            stack.set(ModComponents.SELECTED_ELEMENTS_COMPONENT,data);
        }
    }
}