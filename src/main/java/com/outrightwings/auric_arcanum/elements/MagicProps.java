package com.outrightwings.auric_arcanum.elements;

import com.outrightwings.auric_arcanum.Main;
import com.outrightwings.auric_arcanum.entity.MagicBall;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class MagicProps {

    int minPotionTime = 20*3;
    int minFireFreezeTime = 20;
    Elements.CastingForms castingForm;
    Elements.AttackForms attackForm;
    int[] rawElements;
    int[] elementCounts;


    //Server side props
    public ItemStack potion = null;
    public ItemStack projectile = Items.AIR.getDefaultInstance();
    public int fireTicks = 0, freezeTicks = 0;
    float velocity =  .5f, inaccuracy = 0.1f;
    public int lifetime = 10;
    public int damage = 0;
    public float knockback = 0;
    public boolean gravity = true;
    public boolean isWet = false, isDeath = false, isCold = false, isFire = false, isLife = false, isRock = false;
    //Client side props
    public ParticleOptions particle = ParticleTypes.BUBBLE;



    public MagicProps(int form, int[] rawElements){
        this.rawElements = rawElements;
        this.elementCounts = countElements(rawElements);
        this.castingForm = Elements.CastingForms.values()[form];
        //todo check casting form to choose a different kind of form here like a defensiveForm
        this.attackForm = getAttackForm();
        calculateProps();
    }
    private Elements.AttackForms getAttackForm(){
        int a = -1, b = -1;
        int va = 0, vb = 0;
        for(int i = 0; i < elementCounts.length; i++){
            if(elementCounts[i] > va){
                b = a;
                a = i;
                vb = va;
                va = elementCounts[i];
            } else if(elementCounts[i] > vb){
                b = i;
                vb = elementCounts[i];
            }
        }
        if(a != -1 && b == -1){
            b = a;
        }
        this.particle = Elements.getParticle(a,b);
        return Elements.getAttackForm(a,b);
    }
    public static int[] countElements(int[] rawElements){
        //Offset by 1 to not include type 0 which is none
        int[] counts = new int[Elements.ElementType.values().length-1];

        for(int i : rawElements){
            if(i != 0)
                counts[i-1]++;
        }
        return counts;
    }
    private void calculateProps(){
        List<MobEffectInstance> effects = new ArrayList<>();
        int lifeIndex = Elements.ElementType.LIFE.ordinal()-1;
        int[] counts = elementCounts.clone();
        //Loop through all elements
        for(int i = 0; i < counts.length; i++){
            //Offset by 1 as 0 is none
            switch(Elements.ElementType.values()[i+1]){
                case WATER:
                    if(counts[i] > 0) {
                        if (counts[lifeIndex] != 0) {
                            counts[lifeIndex]--;
                            effects.add(new MobEffectInstance(MobEffects.WATER_BREATHING, counts[i] * minPotionTime));
                        } else {
                            knockback += counts[i] * 0.5f;
                            if(isRock && isCold)
                                projectile = Items.BLUE_ICE.getDefaultInstance();
                            else if(isCold){
                                projectile = Items.ICE.getDefaultInstance();
                            } else if (isRock)
                                projectile = Items.CLAY.getDefaultInstance();
                            isWet = true;

                        }
                    }
                    break;
                case DEATH:
                    if(counts[i] > 0) {
                        int waterIndex = Elements.ElementType.WATER.ordinal()-1;
                        if(counts[waterIndex] > 0){
                            counts[i]--;
                            effects.add(new MobEffectInstance(MobEffects.POISON, counts[i]*minPotionTime));
                        }
                        if (counts[lifeIndex] != 0) {
                            counts[lifeIndex]--;
                            effects.add(new MobEffectInstance(MobEffects.WITHER, counts[i]*minPotionTime));
                        } else {
                            damage += counts[i];
                            isDeath = true;
                        }
                    }
                    break;
                case ROCK:
                    if(counts[i] > 0) {
                        if (counts[lifeIndex] != 0) {
                            counts[lifeIndex]--;
                            effects.add(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, counts[i]*minPotionTime));
                        } else {
                            isRock = true;
                            damage += counts[i];
                            knockback += counts[i] * 0.2f;
                            if(isFire)
                                projectile = Items.MAGMA_BLOCK.getDefaultInstance();
                            else if (isCold)
                                projectile = Items.PACKED_ICE.getDefaultInstance();
                            else if (isDeath)
                                projectile = Items.SOUL_SAND.getDefaultInstance();
                            else if (isWet)
                                projectile = Items.CLAY.getDefaultInstance();
                            else
                                projectile = Items.COBBLESTONE.getDefaultInstance();
                        }
                    }
                    break;
                case ICE:
                    if(counts[i] > 0) {
                        isCold = true;
                        effects.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, counts[i]*minPotionTime));
                        effects.add(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, counts[i] * minPotionTime));
                        if (counts[i] > 1) {
                            freezeTicks = minFireFreezeTime * (counts[i] - 1);
                        }
                    }
                    break;
                case FIRE:
                    if(counts[i] > 0) {
                        if (counts[lifeIndex] != 0) {
                            counts[lifeIndex]--;
                            effects.add(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, counts[i] * minPotionTime));
                        } else {
                            fireTicks = minFireFreezeTime * counts[i];
                            isFire = true;
                        }
                    }
                    break;
                case LIFE:
                    break;
            }
        }
        //LIFE
        if(counts[lifeIndex] > 0) {
            effects.add(new MobEffectInstance(MobEffects.REGENERATION, counts[lifeIndex]*minPotionTime));
            isLife = true;
        }
        if(!effects.isEmpty() || isWet)
            potion = createPotion(effects);
    }
    public boolean cast(Player player, Level level, ItemStack itemStack){
        switch(castingForm){
            case PROJECTILE:
                switch(attackForm){
                    case PROJECTILE:
                        lifetime = 1000;
                        velocity =  1f;
                        inaccuracy = 0.5f;
                        spawnBall(player,level);
                        break;
                    case BEAM:
                        lifetime = 200;
                        velocity =  1f;
                        inaccuracy = 0f;
                        spawnBeam(player,level);
                        break;
                    case SPRAY:
                        lifetime = 20;
                        velocity =  .5f;
                        inaccuracy = 2f;
                        damage = Math.max(damage/3, 1);
                        spawnSpray(player,level);
                        break;
                    case NONE:
                        break;
                }
                break;
            case SELF:
                var p = potion != null ?  potion.get(DataComponents.POTION_CONTENTS).customEffects().toArray(new MobEffectInstance[0]) : null;
                castOnEntity(player,level, Vec3.ZERO,damage,freezeTicks,knockback,fireTicks,p!=null,p);
                break;
        }
        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
        return true;
    }
    public static void castOnEntity(Entity entity, Level level, Vec3 motion,int damage, int freezeTicks, float knockback, int fireTicks,boolean doEffects, MobEffectInstance[] effects){
        // Apply damage if greater than 0
        if (damage > 0) {
            entity.hurt(level.damageSources().thrown(entity, entity), damage);
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
            livingEntity.push(motion.x, 0.1, motion.z);
        }

        //Apply potion effects for self cast
        if(doEffects && entity instanceof LivingEntity livingEntity){
            for (MobEffectInstance mobeffectinstance : effects) {
                Holder<MobEffect> holder = mobeffectinstance.getEffect();
                if (holder.value().isInstantenous()) {
                    holder.value().applyInstantenousEffect(entity, entity, livingEntity, mobeffectinstance.getAmplifier(), 1);
                } else {
                        livingEntity.addEffect(mobeffectinstance, entity);
                }
            }
        }

    }
    private void spawnBeam(Player player, Level level){
        this.gravity = false;
        MagicBall m = MagicBall.spawnAtPlayer(player,level,this);
        m.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, velocity, inaccuracy);

        m.placeInWorld(level);
    }
    private void spawnSpray(Player player, Level level){
        this.gravity = true;
        Random random = new Random();
        int count = 0;
        for(var element : rawElements){
            if(element != 0) count += 2;
        }
        float spread = 45;
        for(int i = 0; i < count; i++){
            float pitch = player.getXRot() + (random.nextFloat() * spread - spread/2);
            float yaw = player.getYRot() + (random.nextFloat() * spread - spread/2);

            MagicBall m = MagicBall.spawnAtPlayer(player,level,this);
            m.shootFromRotation(player, pitch, yaw, 0, velocity, inaccuracy);
            m.placeInWorld(level);
        }
    }
    private void spawnBall(Player player, Level level){
        this.gravity = true;
        MagicBall m = MagicBall.spawnAtPlayer(player,level, this);
        m.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, velocity, inaccuracy);
        m.placeInWorld(level);
    }

    private ItemStack createPotion(List<MobEffectInstance> effects) {
        var item = Items.SPLASH_POTION;
        var p = isWet ? Potions.WATER : Potions.AWKWARD;
        var contents = new PotionContents(Optional.of(p), Optional.empty(), effects);
        var stack = new ItemStack(item);
        stack.set(DataComponents.POTION_CONTENTS, contents);
        return stack;
    }
}
