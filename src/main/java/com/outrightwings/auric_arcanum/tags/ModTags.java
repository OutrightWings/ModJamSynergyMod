package com.outrightwings.auric_arcanum.tags;

import com.outrightwings.auric_arcanum.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Block> DEATH_KILLABLE = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(Main.MODID, "death_killable")
    );
    public static final TagKey<Block> DEATH_DIRTABLE = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(Main.MODID, "death_dirtable")
    );
}
