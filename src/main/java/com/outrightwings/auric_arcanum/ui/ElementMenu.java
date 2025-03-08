package com.outrightwings.auric_arcanum.ui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class ElementMenu extends AbstractContainerMenu {


    public ElementMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }
    public ElementMenu(int id, Inventory inventory, final ContainerLevelAccess access) {
        super(ModMenus.ELEMENT_MENU.get(), id);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

}
