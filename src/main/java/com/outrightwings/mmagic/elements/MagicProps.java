package com.outrightwings.mmagic.elements;

import com.outrightwings.mmagic.entity.MagicBall;
import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

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
    //Things that need to go on the MagicBall
    public ItemStack potion = null;
    public int fireTicks = 0, freezeTicks = 0;
    float velocity =  .5f, inaccuracy = 0.1f;
    public int lifetime = 10;
    public int damage = 0;
    public float knockback = 0;
    public boolean gravity = true;

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
                            effects.add(new MobEffectInstance(MobEffects.WATER_BREATHING, counts[i] * minPotionTime));
                        } else {
                            knockback += counts[i] * 0.5f;
                        }
                    }
                    break;
                case DEATH:
                    if(counts[i] > 0) {
                        if (counts[lifeIndex] != 0) {
                            counts[lifeIndex]--;
                            effects.add(new MobEffectInstance(MobEffects.WITHER, counts[i]*minPotionTime));
                        } else {
                            damage += counts[i];
                        }
                    }
                    break;
                case ROCK:
                    if(counts[i] > 0) {
                        if (counts[lifeIndex] != 0) {
                            counts[lifeIndex]--;
                            effects.add(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, counts[i]*minPotionTime));
                        } else {
                            damage += counts[i];
                            knockback += counts[i] * 0.2f;
                        }
                    }
                    break;
                case ICE:
                    if(counts[i] > 0) {
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
        }
        if(!effects.isEmpty())
            potion = createPotion(effects);
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
        this.gravity = false;
        MagicBall m = MagicBall.spawnAtPlayer(player,level,this);
        m.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, velocity, inaccuracy);

        m.placeInWorld(level);
    }
    private void spawnSpray(Player player, Level level){
        this.gravity = false;
        Random random = new Random();
        int count = 15;
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

        // Create potion contents directly with effects (without using an unregistered Potion instance)
        var contents = new PotionContents(Optional.of(Potions.WATER), Optional.empty(), effects);

        // Create the potion item stack and set its potion contents
        var stack = new ItemStack(item);
        stack.set(DataComponents.POTION_CONTENTS, contents);

        return stack;
    }
}
