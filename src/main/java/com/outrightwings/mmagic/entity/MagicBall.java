package com.outrightwings.mmagic.entity;

import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class MagicBall extends ImprovedProjectileEntity {
//    public MagicBall(Level level, Player player, int x){
//        this(ModEntities.MAGIC_BALL.get(),level);
//    }
    public MagicBall(EntityType<MagicBall> type, Level level) {
        super(type, level);
    }
    //I cant get multi constructors to work for some ungodly reason so we are doing this now
    public static MagicBall spawnAtPlayer(Player player, Level level){
        MagicBall m = new MagicBall(ModEntities.MAGIC_BALL.get(), level);
        m.teleportTo(player.getX(), player.getEyeY() - 0.1F, player.getZ());
        m.setOwner(player);
        return m;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.STONE;
    }


}
