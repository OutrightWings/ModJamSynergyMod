package com.outrightwings.mmagic.entity;

import com.outrightwings.mmagic.Main;
import com.outrightwings.mmagic.elements.MagicProps;
import com.outrightwings.mmagic.tags.ModTags;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

import java.util.UUID;

public class MagicBall extends ImprovedProjectileEntity implements IEntityWithComplexSpawn {
    private UUID potionUUID;
    private ThrownPotion potion;
    private int damage, fireTicks, freezeTicks;
    private float knockback;
    private boolean isWet, isDeath;

    public ParticleOptions particle = ParticleTypes.ANGRY_VILLAGER;

    public MagicBall(EntityType<MagicBall> type, Level level) {
        super(type, level);
    }
    //I cant get multi constructors to work for some ungodly reason, so we are doing this now
    public static MagicBall spawnAtPlayer(Player player, Level level, MagicProps props){
        MagicBall m = new MagicBall(ModEntities.MAGIC_BALL.get(), level);
        Vec3 pos = player.getEyePosition();//.add(player.getLookAngle().scale(.5)).add(0,-0.5,0);
        m.setPos(pos);
        m.setOwner(player);
        m.damage = props.damage;
        m.maxAge = props.lifetime;
        m.knockback = props.knockback;
        m.fireTicks = props.fireTicks;
        m.freezeTicks = props.freezeTicks;
        m.setNoGravity(!props.gravity);
        m.particle = props.particle;
        m.isWet = props.isWet;
        m.isDeath = props.isDeath;
        if(props.potion != null){
            m.potion = InvisiblePotion.getNewPotion(player,level);
            m.potionUUID = m.potion.getUUID();
            m.potion.setItem(props.potion);
            m.potion.startRiding(m);
        }
        m.maxStuckTime = 0;
        return m;
    }
    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(BuiltInRegistries.PARTICLE_TYPE.getId(particle.getType()));
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buf) {
        int particleId = buf.readVarInt();
        this.particle = (ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.byId(particleId);
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
            level().addParticle(particle,
                    getX() - velX * 0.25D, pY - velY * 0.25D, getZ() - velZ * 0.25D,
                    0, 0, 0);
        //}
        super.spawnTrailParticles();
    }


    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity entity = result.getEntity();

            // Apply damage if greater than 0
            if (damage > 0) {
                entity.hurt(this.damageSources().thrown(this, this.getOwner()), damage);
            }

            // Apply fire ticks if greater than 0
            if (fireTicks > 0) {
                entity.setRemainingFireTicks(fireTicks);
                entity.setSharedFlagOnFire(true);
            }

            // Apply freeze ticks if greater than 0
            if (freezeTicks > 0 && entity instanceof LivingEntity livingEntity) {
                livingEntity.setIsInPowderSnow(true);
                livingEntity.setTicksFrozen(livingEntity.getTicksRequiredToFreeze()+ freezeTicks *10);
            }

            // Apply knockback
            if (knockback > 0 && entity instanceof LivingEntity livingEntity) {
                Vec3 motion = entity.position().subtract(this.position()).normalize().scale(knockback);
                livingEntity.push(motion.x, 0.1, motion.z);
            }
        }
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
        super.onHitBlock(result);
        Level level = this.level();
        if (!level.isClientSide) {
            if(potion != null){
                this.potion.onHitBlock(result);
            }
            BlockState state = level.getBlockState(result.getBlockPos());
            if(isDeath && state.is(ModTags.DEATH_KILLABLE)){
                level.setBlockAndUpdate(result.getBlockPos(), Blocks.AIR.defaultBlockState());
            }
            if(isDeath && state.is(ModTags.DEATH_DIRTABLE)){
                level.setBlockAndUpdate(result.getBlockPos(), Blocks.DIRT.defaultBlockState());
            }

            if ((this.isWet && state.is(BlockTags.CONVERTABLE_TO_MUD)) || (isDeath && isWet && state.is(ModTags.DEATH_DIRTABLE))){
                level.setBlockAndUpdate(result.getBlockPos(), Blocks.MUD.defaultBlockState());
            }
        }
    }
    @Override
    public void tick(){
        Level level = this.level();
        if(!level.isClientSide){
            if(this.potion == null && potionUUID != null)
                this.potion = getPotionEntity();

            //Kill plants we fly through
            BlockState state = level.getBlockState(this.blockPosition());
            if(isDeath && state.is(ModTags.DEATH_KILLABLE)){
                level.setBlockAndUpdate(this.blockPosition(), Blocks.AIR.defaultBlockState());
            }

        }
        super.tick();
    }
    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        if (potionUUID != null) {
            nbt.putUUID("potionUUID", potionUUID);
        }
        nbt.putInt("fireTicks", fireTicks);
        nbt.putInt("damage",damage);
        nbt.putFloat("knockback",knockback);
        nbt.putInt("freezeTicks", freezeTicks);
        nbt.putBoolean("wet",isWet);
        nbt.putBoolean("death",isDeath);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        if (nbt.hasUUID("potionUUID")) {
            potionUUID = nbt.getUUID("potionUUID");
        }
        fireTicks = nbt.getInt("fireTicks");
        damage = nbt.getInt("damage");
        knockback = nbt.getFloat("knockback");
        freezeTicks = nbt.getInt("freezeTicks");
        isWet = nbt.getBoolean("wet");
        isDeath = nbt.getBoolean("death");
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
