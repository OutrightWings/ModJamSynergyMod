package com.outrightwings.mmagic.item;

import com.outrightwings.mmagic.Main;
import com.outrightwings.mmagic.elements.Elements;
import com.outrightwings.mmagic.elements.MagicProps;
import com.outrightwings.mmagic.entity.MagicBall;
import com.outrightwings.mmagic.entity.ModEntities;
import com.outrightwings.mmagic.item.components.ModComponents;
import com.outrightwings.mmagic.item.components.SelectedElementsComponent;
import com.outrightwings.mmagic.item.components.SelectedFormComponent;
import com.outrightwings.mmagic.ui.ElementMenu;
import net.mehvahdjukaar.moonlight.api.item.ILeftClickReact;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
//todo add tooltips/tell player what they have selected
public class Wand extends Item implements ILeftClickReact {
    public Wand(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand){
        if(!level.isClientSide && player instanceof ServerPlayer serverPlayer){
            if(player.isShiftKeyDown()){
                clearElements(player.getItemInHand(hand));
            }else{
                serverPlayer.openMenu(new SimpleMenuProvider((i,inventory,playerE)-> new ElementMenu(i,inventory), Component.translatable(Main.MODID+".elemental_gui")));
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand),level.isClientSide);
    }
    public void clearElements(ItemStack item){
        item.set(ModComponents.SELECTED_ELEMENTS_COMPONENT,new SelectedElementsComponent(new int[Elements.MAX_SELECTED]));
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
            var props = new MagicProps(form, elements);
            return props.cast(player,level,itemStack);
        }
        return false;
    }
}
