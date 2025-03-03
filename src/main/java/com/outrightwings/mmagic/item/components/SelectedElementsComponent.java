package com.outrightwings.mmagic.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;

public class SelectedElementsComponent{
    public record SelectedElements(int[] elements){
        public SelectedElements{
            elements = elements.clone();
        }
        public int[] elements(){
            return elements.clone();
        }
    }
    public static final Codec<SelectedElements> SELECTED_ELEMENTS_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.listOf().fieldOf("elements").forGetter(record -> Arrays.stream(record.elements).boxed().toList())
            ).apply(instance, list -> new SelectedElements(list.stream().mapToInt(Integer::intValue).toArray()))
    );
    public static final StreamCodec<ByteBuf, SelectedElements> SELECTED_ELEMENTS_STREAM_CODEC = StreamCodec.of(
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
}