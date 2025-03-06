package com.outrightwings.mmagic.elements;

import com.outrightwings.mmagic.entity.MagicBall;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class MagicProps {
    Elements.CastingForms castingForm;
    Elements.AttackForms attackForm;
    int[] rawElements;
    int[] elementCounts;
    //Things that need to go on the MagicBall
    PotionContents potionEffects = null;
    int fireTicks = 0, freezeTicks = 0;
    float velocity =  .5f, inaccuracy = 0.1f;
    int lifetime = 10;
    int damage = 0;
    float knockback = 0;


    public MagicProps(int form, int[] rawElements){
        this.rawElements = rawElements;
        this.elementCounts = countElements();
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
        return Elements.getAttackForm(a,b);
    }
    private int[] countElements(){
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
                            effects.add(new MobEffectInstance(MobEffects.WATER_BREATHING, counts[i] * 2));
                        } else {
                            knockback += counts[i] * 0.5f;
                        }
                    }
                    break;
                case DEATH:
                    if(counts[i] > 0) {
                        if (counts[lifeIndex] != 0) {
                            counts[lifeIndex]--;
                            effects.add(new MobEffectInstance(MobEffects.WITHER, counts[i]));
                        } else {
                            damage += counts[i];
                        }
                    }
                    break;
                case ROCK:
                    if(counts[i] > 0) {
                        if (counts[lifeIndex] != 0) {
                            counts[lifeIndex]--;
                            effects.add(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, counts[i]));
                        } else {
                            damage += counts[i];
                            knockback += counts[i] * 0.2f;
                        }
                    }
                    break;
                case ICE:
                    if(counts[i] > 0) {
                        effects.add(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, counts[i]));
                        effects.add(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, counts[i] / 2));
                        if (counts[i] > 1) {
                            freezeTicks = 2 * (counts[i] - 1);
                        }
                    }
                    break;
                case FIRE:
                    if(counts[i] > 0) {
                        if (counts[lifeIndex] != 0) {
                            counts[lifeIndex]--;
                            effects.add(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, counts[i]));
                        } else {
                            fireTicks = 2 * counts[i];
                        }
                    }
                    break;
                case LIFE:
                    break;
            }
        }
        //LIFE
        if(counts[lifeIndex] > 0) {
            effects.add(new MobEffectInstance(MobEffects.REGENERATION, counts[lifeIndex]));
        }

        //Create potion out of effects
        if(!effects.isEmpty()||counts[Elements.ElementType.WATER.ordinal()-1] > 0){
            var p = new Potion(effects.toArray(MobEffectInstance[]::new));;
            potionEffects = new PotionContents(Holder.direct(p));

        }
    }
    public boolean cast(Player player, Level level, ItemStack itemStack){
        switch(attackForm){
            case PROJECTILE:
                lifetime = 1000;
                velocity =  1f;
                inaccuracy = 0.5f;
                spawnBall(player,level);
                break;
            case BEAM:
                lifetime = 200;
                velocity =  5f;
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
        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
        return true;
    }
    private void spawnBeam(Player player, Level level){
        MagicBall m = MagicBall.spawnAtPlayer(player,level,createPotion());
        m.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, velocity, inaccuracy);
        m.setNoGravity(true);
        m.setMaxAge(lifetime);
        m.placeInWorld(level);
    }
    private void spawnSpray(Player player, Level level){
        Random random = new Random();
        int count = 15;
        float spread = 45;
        for(int i = 0; i < count; i++){
            float pitch = player.getXRot() + (random.nextFloat() * spread - spread/2);
            float yaw = player.getYRot() + (random.nextFloat() * spread - spread/2);

            MagicBall m = MagicBall.spawnAtPlayer(player,level,createPotion());
            m.shootFromRotation(player, pitch, yaw, 0, velocity, inaccuracy);
            m.setNoGravity(true);
            m.setMaxAge(lifetime);
            m.placeInWorld(level);
        }
    }
    private void spawnBall(Player player, Level level){
        MagicBall m = MagicBall.spawnAtPlayer(player,level, createPotion() );
        m.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, velocity, inaccuracy);
        m.setMaxAge(lifetime);
        m.placeInWorld(level);
    }

    private ItemStack createPotion(){
        if(potionEffects != null) {
            var item = Items.SPLASH_POTION.getDefaultInstance();
            item.set(DataComponents.POTION_CONTENTS, potionEffects);
            return item;
        }
        return null;
    }
}
