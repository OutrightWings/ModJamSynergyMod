package com.outrightwings.auric_arcanum.item;

import com.outrightwings.auric_arcanum.Main;
import com.outrightwings.auric_arcanum.elements.Elements;
import com.outrightwings.auric_arcanum.elements.MagicProps;
import com.outrightwings.auric_arcanum.network.components.ModComponents;
import com.outrightwings.auric_arcanum.network.components.SelectedElementsComponent;
import com.outrightwings.auric_arcanum.ui.ElementMenu;
import net.mehvahdjukaar.moonlight.api.item.ILeftClickReact;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class Wand extends Item implements ILeftClickReact {
    public Wand(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public void appendHoverText(ItemStack item, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
        SelectedElementsComponent elementsComponent = item.get(ModComponents.SELECTED_ELEMENTS_COMPONENT);
        int[] counts = MagicProps.countElements(elementsComponent.elements());
        for(int id = 0; id < counts.length; id++){
            String name = Elements.ElementType.getTranslationKey(id+1);
            if(counts[id] != 0)
                list.add(Component.literal(String.format("%dx ",counts[id])).append(Component.translatable(name)).withStyle(ChatFormatting.GRAY));
        }
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
