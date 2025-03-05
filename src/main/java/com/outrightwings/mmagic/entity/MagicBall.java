package com.outrightwings.mmagic.entity;

import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
    public void setMaxAge(int ticks){
        this.maxAge = ticks;
    }
    @Override
    protected Item getDefaultItem() {
        return Items.STONE;
    }
    public void spawnTrailParticles(){
        // Projectile particle code
        var movement = this.getDeltaMovement();
        double velX = movement.x;
        double velY = movement.y;
        double velZ = movement.z;
        for (int j = 0; j < 4; ++j) {
            double pY = this.getEyeY();
            level().addParticle(ParticleTypes.FLAME,
                    getX() - velX * 0.25D, pY - velY * 0.25D, getZ() - velZ * 0.25D,
                    0, 0, 0);
        }
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
//    @Override
//    public boolean isLingering(){
//        return true;
//    }
//    @Override
//    protected void onHit(HitResult pResult) {
//        super.onHit(pResult);
//        if (!this.level().isClientSide) {
//            ItemStack itemstack = this.getItem();
//            PotionContents potioncontents = itemstack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
//            if (potioncontents.is(Potions.WATER)) {
//                this.applyWater();
//            } else if (potioncontents.hasEffects()) {
//                if (this.isLingering()) {
//                    this.makeAreaOfEffectCloud(potioncontents);
//                } else {
//                    this.applySplash(
//                            potioncontents.getAllEffects(), pResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) pResult).getEntity() : null
//                    );
//                }
//            }
//            this.level().broadcastEntityEvent(this, (byte) 3);
//            this.discard();
//        }
//    }
}
