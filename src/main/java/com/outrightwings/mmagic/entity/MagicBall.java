package com.outrightwings.mmagic.entity;

import com.outrightwings.mmagic.Main;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class MagicBall extends ThrownPotion {
//    public MagicBall(Level level, Player player, int x){
//        this(ModEntities.MAGIC_BALL.get(),level);
//    }
    public MagicBall(EntityType<MagicBall> type, Level level) {
        super(type, level);
    }
    //I cant get multi constructors to work for some ungodly reason so we are doing this now
    public static MagicBall spawnAndShootAtPlayer(Player player, Level level){
        MagicBall m = new MagicBall(ModEntities.MAGIC_BALL.get(), level);
        m.teleportTo(player.getX(), player.getEyeY() - 0.1F, player.getZ());
        m.setOwner(player);
        m.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 1.0F);
        setMagicProps(m);


        level.addFreshEntity(m);
        return m;
    }
    public static void setMagicProps(MagicBall m){
        var i = Items.STONE.getDefaultInstance();
        i.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.POISON));
    }
    @Override
    protected Item getDefaultItem() {
        return Items.STONE;
    }

    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItem();
        return (ParticleOptions)(!itemstack.isEmpty() && !itemstack.is(this.getDefaultItem())
                ? new ItemParticleOption(ParticleTypes.ITEM, itemstack)
                : ParticleTypes.ITEM_SNOWBALL);
    }

//    @Override
//    public void handleEntityEvent(byte pId) {
//        if (pId == 3) {
//            ParticleOptions particleoptions = this.getParticle();
//
//            for (int i = 0; i < 8; i++) {
//                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
//            }
//        }
//    }
//
//    @Override
//    protected void onHitEntity(EntityHitResult pResult) {
//        super.onHitEntity(pResult);
//        Entity entity = pResult.getEntity();
//        int i = entity instanceof Blaze ? 3 : 0;
//        entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float)i);
//
//    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            ItemStack itemstack = this.getItem();
            PotionContents potioncontents = itemstack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            if (potioncontents.is(Potions.WATER)) {
                this.applyWater();
            } else if (potioncontents.hasEffects()) {
                if (this.isLingering()) {
                    this.makeAreaOfEffectCloud(potioncontents);
                } else {
                    this.applySplash(
                            potioncontents.getAllEffects(), pResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) pResult).getEntity() : null
                    );
                }
            }
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }
}
