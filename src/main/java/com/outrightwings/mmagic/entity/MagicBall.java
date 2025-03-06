package com.outrightwings.mmagic.entity;

import com.outrightwings.mmagic.Main;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class MagicBall extends ImprovedProjectileEntity {
    private UUID potionUUID; // Store the UUID reference
    private ThrownPotion potion; // Cached reference (not saved)
    private int damage, fireticks;
    private float knockback;

    public MagicBall(EntityType<MagicBall> type, Level level) {
        super(type, level);
    }
    //I cant get multi constructors to work for some ungodly reason so we are doing this now
    public static MagicBall spawnAtPlayer(Player player, Level level, ItemStack p){
        MagicBall m = new MagicBall(ModEntities.MAGIC_BALL.get(), level);
        Vec3 pos = player.getEyePosition().add(player.getLookAngle().scale(.5)).add(0,-0.5,0);
        m.setPos(pos);
        m.setOwner(player);
        if(p != null){
            m.potion = new ThrownPotion(level,player);
            m.potionUUID = m.potion.getUUID();
            m.potion.setItem(p);
            m.potion.startRiding(m);

        }
        m.setInvisible(true);
        m.maxStuckTime = 0;
        return m;
    }
    public void placeInWorld(Level level){
        level.addFreshEntity(this);
        if(potion != null) {
            level.addFreshEntity(potion);
            potion.setInvisible(true);
        }
    }
    @Override
    public void reachedEndOfLife(){
        if(potion != null)
            potion.remove(RemovalReason.DISCARDED);
        super.reachedEndOfLife();
    }
    public void setMaxAge(int ticks){
        this.maxAge = ticks;
    }
    @Override
    protected Item getDefaultItem() {
        return Items.AIR;
    }
    public void spawnTrailParticles(){
        // Projectile particle code
        var movement = this.getDeltaMovement();
        double velX = movement.x;
        double velY = movement.y;
        double velZ = movement.z;
        //for (int j = 0; j < 4; ++j) {
            double pY = this.getEyeY();
            level().addParticle(ParticleTypes.FLAME,
                    getX() - velX * 0.25D, pY - velY * 0.25D, getZ() - velZ * 0.25D,
                    0, 0, 0);
        //}
        super.spawnTrailParticles();
    }



    @Override
    protected void onHit(HitResult result) {
        if (!this.level().isClientSide) {
            if(potion != null){
                this.potion.onHit(result);
                this.potion = null;
            }
        }
        super.onHit(result);
    }
    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.level().isClientSide) {
            if(potion != null){
                this.potion.onHitBlock(result);
            }
        }
        super.onHitBlock(result);
    }
    @Override
    public void tick(){
        if(this.potion == null && potionUUID != null)
            this.potion = getPotionEntity();
        super.tick();
    }
    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (potionUUID != null) {
            nbt.putUUID("potionUUID", potionUUID);
        }
        nbt.putInt("fireTicks",fireticks);
        nbt.putInt("damage",damage);
        nbt.putFloat("knockback",knockback);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        if (nbt.hasUUID("potionUUID")) {
            potionUUID = nbt.getUUID("potionUUID");
        }
        fireticks = nbt.getInt("fireTicks");
        damage = nbt.getInt("damage");
        knockback = nbt.getFloat("knockback");
    }
    private ThrownPotion getPotionEntity() {
        if (potion == null && potionUUID != null) {
            // Find the potion in the world using its UUID
            Entity entity = ((ServerLevel) this.level()).getEntity(potionUUID);
            if (entity instanceof ThrownPotion) {
                potion = (ThrownPotion) entity;
            }
        }
        return potion;
    }


}
