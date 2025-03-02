package com.outrightwings.mmagic.item;

import com.outrightwings.mmagic.Main;
import com.outrightwings.mmagic.ui.ElementMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Wand extends Item {
    public Wand(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand){
        if(!level.isClientSide && player instanceof ServerPlayer serverPlayer){
            serverPlayer.openMenu(new SimpleMenuProvider((i,inventory,playerE)-> new ElementMenu(i,inventory), Component.translatable(Main.MODID+".elemental_gui")));
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand),level.isClientSide);
    }
}
