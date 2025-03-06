package com.outrightwings.mmagic.elements;

import java.util.Map;

public class Elements {
    public static final int MAX_SELECTED = 6;
    public enum CastingForms {
        RAY,
        WALL,
        SELF
    }
    public enum ElementType {
        NONE,
        FIRE,
        ICE,
        ROCK,
        LIFE,
        DEATH,
        WATER
    }
    public enum AttackForms {
        NONE,
        SPRAY,
        PROJECTILE,
        BEAM
    }

    private static Map<Point,AttackForms> ATTACK_FORM_MAP = Map.ofEntries(
            //Position is both element ids
            Map.entry(new Point(0,0),AttackForms.SPRAY),
            Map.entry(new Point(0,1),AttackForms.PROJECTILE),
            Map.entry(new Point(0,2),AttackForms.PROJECTILE),
            Map.entry(new Point(0,3),AttackForms.SPRAY),
            Map.entry(new Point(0,4),AttackForms.SPRAY),
            Map.entry(new Point(0,5),AttackForms.SPRAY),

            Map.entry(new Point(1,1),AttackForms.PROJECTILE),
            Map.entry(new Point(1,2),AttackForms.PROJECTILE),
            Map.entry(new Point(1,3),AttackForms.BEAM),
            Map.entry(new Point(1,4),AttackForms.PROJECTILE),
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
    private record Point(int x, int y){}
}
