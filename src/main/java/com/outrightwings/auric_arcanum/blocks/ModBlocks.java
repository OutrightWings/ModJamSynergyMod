package com.outrightwings.auric_arcanum.blocks;

import com.outrightwings.auric_arcanum.Main;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Main.MODID);
    public static final DeferredBlock<Block> TEMP_MAGIC_STONE = BLOCKS.register("magic_stone",()-> new TempMagicBlocks(Blocks.COBBLESTONE.properties()));
    public static final DeferredBlock<Block> TEMP_MAGIC_MUD = BLOCKS.register("magic_mud",()-> new TempMud(Blocks.MUD.properties()));
    public static final DeferredBlock<Block> TEMP_MAGIC_CLAY = BLOCKS.register("magic_clay",()-> new TempMagicBlocks(Blocks.CLAY.properties()));
    public static final DeferredBlock<Block> TEMP_MAGIC_ICE = BLOCKS.register("magic_ice",()-> new TempMagicBlocks(Blocks.ICE.properties()));
    public static final DeferredBlock<Block> TEMP_MAGIC_MAGMA = BLOCKS.register("magic_magma",()-> new TempMagicBlocks(Blocks.MAGMA_BLOCK.properties()));
}
