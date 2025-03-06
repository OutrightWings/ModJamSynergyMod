package com.outrightwings.mmagic.elements;

import com.outrightwings.mmagic.entity.MagicBall;
import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class MagicProps {
    Elements.CastingForms castingForm;
    Elements.AttackForms attackForm;
    PotionContents effects;
    float velocity =  .5f, inaccuracy = 0.1f, range = 10;
    int[] rawElements;
    public MagicProps(int form, int[] rawElements){
        this.rawElements = rawElements;
    }
    public boolean cast(Player player, Level level, ItemStack itemStack){

        switch(Elements.ElementType.values()[rawElements[0]]){
            case FIRE:
                //Spray form
                spawnSpray(player, level,player.getEyePosition(),player.getLookAngle().normalize());
                break;
            case ICE:
                //Projectile form
                spawnBall(player, level);
                break;
            case LIFE:
                //Testing Beam Form
                spawnBeam(player, level);
                break;
            case ROCK:
                break;
            case DEATH:
                break;
            case WATER:
                break;
            default:
                break;
        }
        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
        return true;
    }

    private void spawnBeam(Player player, Level level){
        MagicBall m = MagicBall.spawnAtPlayer(player,level,createPotion());
        m.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, velocity, inaccuracy);
        m.setNoGravity(true);
        m.setItem(Items.SNOWBALL.getDefaultInstance());
        m.placeInWorld(level);
    }
    private void spawnSpray(Player player, Level level, Vec3 start, Vec3 direction){
        Random random = new Random();
        int count = 15;
        float spread = 45;
        for(int i = 0; i < count; i++){
            float pitch = player.getXRot() + (random.nextFloat() * spread - spread/2);
            float yaw = player.getYRot() + (random.nextFloat() * spread - spread/2);

            MagicBall m = MagicBall.spawnAtPlayer(player,level,createPotion());
            m.shootFromRotation(player, pitch, yaw, 0, velocity, inaccuracy);
            m.setNoGravity(true);
            m.setMaxAge(5);
            m.setItem(Items.SNOWBALL.getDefaultInstance());
            m.placeInWorld(level);
        }
    }
    private void spawnBall(Player player, Level level){
        MagicBall m = MagicBall.spawnAtPlayer(player,level, createPotion() );
        m.setItem(Items.SNOWBALL.getDefaultInstance());
        m.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, velocity, inaccuracy);
        m.placeInWorld(level);
    }
    private ItemStack createPotion(){
        return PotionContents.createItemStack(Items.SPLASH_POTION,Potions.POISON);
    }
}
