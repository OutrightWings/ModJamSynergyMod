package com.outrightwings.mmagic.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
public class SelectedFormComponent {
    public record SelectedForm(int id){}

    public static final Codec<SelectedForm> SELECTED_FORM_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("id").forGetter(SelectedForm::id)
            ).apply(instance, SelectedForm::new)
    );
    public static final StreamCodec<ByteBuf, SelectedForm> SELECTED_FORM_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SelectedForm::id,
            SelectedForm::new
    );

}