package com.outrightwings.mmagic.entity;

import com.outrightwings.mmagic.Main;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Main.MODID);

    public static final Supplier<EntityType<MagicBall>> MAGIC_BALL = ENTITIES.register("magic_ball",()-> EntityType.Builder.of(
            MagicBall::new,
            MobCategory.MISC
        )
            .updateInterval(10)
            .sized(0.15F, 0.15F)
            .clientTrackingRange(4)
            .build("magic_ball")
    );

    public static final Supplier<EntityType<InvisiblePotion>> INVISIBLE_POTION = ENTITIES.register("invisible_potion",()-> EntityType.Builder.of(
                    InvisiblePotion::new,
                            MobCategory.MISC
                    )
                    .updateInterval(10)
                    .sized(0.1F, 0.1F)
                    .clientTrackingRange(4)
                    .build("invisible_potion")
    );

}
