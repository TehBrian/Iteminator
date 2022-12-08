package xyz.tehbrian.iteminator.command;

import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.NonNull;

public enum ModernPotionEffectType {
    SPEED(PotionEffectType.SPEED),
    SLOWNESS(PotionEffectType.SLOW),
    HASTE(PotionEffectType.FAST_DIGGING),
    MINING_FATIGUE(PotionEffectType.SLOW_DIGGING),
    STRENGTH(PotionEffectType.INCREASE_DAMAGE),
    INSTANT_HEALTH(PotionEffectType.HEAL),
    INSTANT_DAMAGE(PotionEffectType.HARM),
    JUMP_BOOST(PotionEffectType.JUMP),
    NAUSEA(PotionEffectType.CONFUSION),
    REGENERATION(PotionEffectType.REGENERATION),
    RESISTANCE(PotionEffectType.DAMAGE_RESISTANCE),
    FIRE_RESISTANCE(PotionEffectType.FIRE_RESISTANCE),
    WATER_BREATHING(PotionEffectType.WATER_BREATHING),
    INVISIBILITY(PotionEffectType.INVISIBILITY),
    BLINDNESS(PotionEffectType.BLINDNESS),
    NIGHT_VISION(PotionEffectType.NIGHT_VISION),
    HUNGER(PotionEffectType.HUNGER),
    WEAKNESS(PotionEffectType.WEAKNESS),
    POISON(PotionEffectType.POISON),
    WITHER(PotionEffectType.WITHER),
    HEALTH_BOOST(PotionEffectType.HEALTH_BOOST),
    ABSORPTION(PotionEffectType.ABSORPTION),
    SATURATION(PotionEffectType.SATURATION),
    GLOWING(PotionEffectType.GLOWING),
    LEVITATION(PotionEffectType.LEVITATION),
    LUCK(PotionEffectType.LUCK),
    BAD_LUCK(PotionEffectType.UNLUCK),
    SLOW_FALLING(PotionEffectType.SLOW_FALLING),
    CONDUIT_POWER(PotionEffectType.CONDUIT_POWER),
    DOLPHINS_GRACE(PotionEffectType.DOLPHINS_GRACE),
    BAD_OMEN(PotionEffectType.BAD_OMEN),
    HERO_OF_THE_VILLAGE(PotionEffectType.HERO_OF_THE_VILLAGE);

    private final PotionEffectType type;

    /**
     * @param type the wrapped type
     */
    ModernPotionEffectType(final PotionEffectType type) {
        this.type = type;
    }

    /**
     * @return the wrapped type
     */
    public PotionEffectType unwrap() {
        return this.type;
    }

}
