package com.outrightwings.mmagic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.outrightwings.mmagic.Main;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;

public class ModComponents {
    public record SelectedElements(int[] elements){
        public SelectedElements{
            elements = elements.clone();
        }
        public int[] elements(){
            return elements.clone();
        }
    }
    public static final Codec<SelectedElements> BASIC_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.listOf().fieldOf("elements").forGetter(record -> Arrays.stream(record.elements).boxed().toList())
            ).apply(instance, list -> new SelectedElements(list.stream().mapToInt(Integer::intValue).toArray()))
    );
    public static final StreamCodec<ByteBuf, SelectedElements> BASIC_STREAM_CODEC = StreamCodec.of(
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


    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Main.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SelectedElements>> SELECTED_ELEMENTS_COMPONENT = COMPONENTS.registerComponentType(
            "basic",
            builder -> builder
                    .persistent(BASIC_CODEC)
                    .networkSynchronized(BASIC_STREAM_CODEC)
    );

}
