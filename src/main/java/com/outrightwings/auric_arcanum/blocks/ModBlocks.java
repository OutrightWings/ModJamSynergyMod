package com.outrightwings.auric_arcanum.blocks;

import com.outrightwings.auric_arcanum.Main;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Main.MODID);
    public static final DeferredBlock<Block> TEMP_MAGIC_STONE = BLOCKS.register("magic_stone",()-> new TempMagicBlocks(BlockBehaviour.Properties.of()));
}
