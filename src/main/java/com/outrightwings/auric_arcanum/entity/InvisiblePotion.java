package com.outrightwings.auric_arcanum.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;

public class InvisiblePotion extends ThrownPotion {
    public boolean isLingering;
    public InvisiblePotion(EntityType<InvisiblePotion> type, Level level) {
        super(type, level);
    }
    public static InvisiblePotion getNewPotion(Player player, Level level){
        InvisiblePotion m = new InvisiblePotion(ModEntities.INVISIBLE_POTION.get(), level);
        m.setOwner(player);
        return m;
    }

}
