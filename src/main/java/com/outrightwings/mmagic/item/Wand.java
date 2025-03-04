package com.outrightwings.mmagic.item;

import com.outrightwings.mmagic.Main;
import com.outrightwings.mmagic.item.components.ModComponents;
import com.outrightwings.mmagic.item.components.SelectedFormComponent;
import com.outrightwings.mmagic.ui.ElementMenu;
import net.mehvahdjukaar.moonlight.api.item.ILeftClickReact;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Wand extends Item implements ILeftClickReact {
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
    @Override
    public boolean canAttackBlock(BlockState pState, Level level, BlockPos pPos, Player player) {
        return !player.isCreative();
    }
    @Override
    public boolean onLeftClick(ItemStack itemStack, Player player, InteractionHand hand) {
        //Server side only logic
        var level = player.level();
        if (!level.isClientSide) {
            //Get relavent things
            int form = itemStack.get(ModComponents.SELECTED_FORM_COMPONENT).id();
            int[] elements = itemStack.get(ModComponents.SELECTED_ELEMENTS_COMPONENT).elements();
            //Do things if elements
            if(elements[0] != 0){
                ItemStack itemstack = player.getItemInHand(hand);
                Snowball snowball = new Snowball(level, player);
                snowball.setItem(itemstack);
                snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(snowball);
                player.awardStat(Stats.ITEM_USED.get(this));

                return true;
            }
        }
        return false;
    }
}
