package com.outrightwings.mmagic.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class InvisiblePotionRenderer extends ThrownItemRenderer<InvisiblePotion> {
    public InvisiblePotionRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(InvisiblePotion pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        //Do nothing please
    }
}
