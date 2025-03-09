package com.outrightwings.auric_arcanum.elements;

import com.outrightwings.auric_arcanum.Main;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

import java.util.Map;

public class Elements {
    public static final int MAX_SELECTED = 6;
    public enum CastingForms {
        PROJECTILE,
        SELF;
        public static String getTranslationKey(int id){
            return "form."+ Main.MODID+"." + values()[id].name();
        }
    }
    public enum ElementType {
        NONE,
        FIRE,
        ICE,
        ROCK,
        LIFE,
        DEATH,
        WATER;
        public static String getTranslationKey(int id){
            return "element."+ Main.MODID+"." + values()[id].name();
        }
    }
    public enum AttackForms {
        NONE,
        SPRAY,
        PROJECTILE,
        BEAM
    }

    private static final Map<Point,AttackForms> ATTACK_FORM_MAP = Map.ofEntries(
            //Position is both element ids offset by -1
            Map.entry(new Point(0,0),AttackForms.SPRAY),
            Map.entry(new Point(0,1),AttackForms.PROJECTILE),
            Map.entry(new Point(0,2),AttackForms.PROJECTILE),
            Map.entry(new Point(0,3),AttackForms.SPRAY),
            Map.entry(new Point(0,4),AttackForms.PROJECTILE),
            Map.entry(new Point(0,5),AttackForms.SPRAY),

            Map.entry(new Point(1,1),AttackForms.SPRAY),
            Map.entry(new Point(1,2),AttackForms.PROJECTILE),
            Map.entry(new Point(1,3),AttackForms.BEAM),
            Map.entry(new Point(1,4),AttackForms.BEAM),
            Map.entry(new Point(1,5),AttackForms.PROJECTILE),

            Map.entry(new Point(2,2),AttackForms.PROJECTILE),
            Map.entry(new Point(2,3),AttackForms.PROJECTILE),
            Map.entry(new Point(2,4),AttackForms.PROJECTILE),
            Map.entry(new Point(2,5),AttackForms.PROJECTILE),

            Map.entry(new Point(3,3),AttackForms.BEAM),
            Map.entry(new Point(3,4),AttackForms.BEAM),
            Map.entry(new Point(3,5),AttackForms.SPRAY),

            Map.entry(new Point(4,4),AttackForms.BEAM),
            Map.entry(new Point(4,5),AttackForms.SPRAY),

            Map.entry(new Point(5,5),AttackForms.SPRAY)

    );
    private static final Map<Point, ParticleOptions> PARTICLE_MAP = Map.ofEntries(
            //Position is both element ids
            Map.entry(new Point(0,0),ParticleTypes.FLAME),
            Map.entry(new Point(0,1),ParticleTypes.SNOWFLAKE),
            Map.entry(new Point(0,2),ParticleTypes.FALLING_LAVA),
            Map.entry(new Point(0,3),ParticleTypes.LAVA),
            Map.entry(new Point(0,4),ParticleTypes.ELECTRIC_SPARK),
            Map.entry(new Point(0,5),ParticleTypes.CLOUD),

            Map.entry(new Point(1,1),ParticleTypes.SNOWFLAKE),
            Map.entry(new Point(1,2),ParticleTypes.SNOWFLAKE),
            Map.entry(new Point(1,3),ParticleTypes.SNOWFLAKE),
            Map.entry(new Point(1,4),ParticleTypes.SNOWFLAKE),
            Map.entry(new Point(1,5),ParticleTypes.SNOWFLAKE),

            Map.entry(new Point(2,2),ParticleTypes.WHITE_ASH),
            Map.entry(new Point(2,3),ParticleTypes.WHITE_ASH),
            Map.entry(new Point(2,4),ParticleTypes.DUST_PLUME),
            Map.entry(new Point(2,5),ParticleTypes.WHITE_ASH),

            Map.entry(new Point(3,3),ParticleTypes.COMPOSTER),
            Map.entry(new Point(3,4),ParticleTypes.ASH),
            Map.entry(new Point(3,5),ParticleTypes.BUBBLE),

            Map.entry(new Point(4,4),ParticleTypes.CRIT),
            Map.entry(new Point(4,5),ParticleTypes.BUBBLE),

            Map.entry(new Point(5,5),ParticleTypes.BUBBLE)
    );
    public static AttackForms getAttackForm(int a, int b){
        AttackForms form;
        if(a == -1 || b == -1)
            form = AttackForms.NONE;
        else if(a > b)
            form = ATTACK_FORM_MAP.get(new Point(b,a));
        else
            form = ATTACK_FORM_MAP.get(new Point(a,b));
        return form;
    }
    public static ParticleOptions getParticle(int a, int b){
        ParticleOptions form;
        if(a == -1 || b == -1)
            form = ParticleTypes.ANGRY_VILLAGER;
        else if(a > b)
            form = PARTICLE_MAP.get(new Point(b,a));
        else
            form = PARTICLE_MAP.get(new Point(a,b));
        return form;
    }
    private record Point(int x, int y){}
}
