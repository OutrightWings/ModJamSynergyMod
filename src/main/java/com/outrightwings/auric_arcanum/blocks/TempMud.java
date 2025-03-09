package com.outrightwings.auric_arcanum.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TempMud extends TempMagicBlocks{
    public TempMud(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }
    protected void placeAfter(ServerLevel level, BlockPos pos){
        level.setBlock(pos, Blocks.DIRT.defaultBlockState(),2);
    }
}
