package xyz.tehbrian.iteminator.command;

import org.bukkit.potion.PotionType;
import org.checkerframework.checker.nullness.qual.NonNull;

public enum ModernPotionType {
    AWKWARD(PotionType.AWKWARD),
    THICK(PotionType.THICK),
    MUNDANE(PotionType.MUNDANE),
    UNCRAFTABLE(PotionType.UNCRAFTABLE),
    WATER(PotionType.WATER),
    SPEED(PotionType.SPEED),
    SLOWNESS(PotionType.SLOWNESS),
    STRENGTH(PotionType.STRENGTH),
    INSTANT_HEALTH(PotionType.INSTANT_HEAL),
    INSTANT_DAMAGE(PotionType.INSTANT_DAMAGE),
    JUMP_BOOST(PotionType.JUMP),
    REGENERATION(PotionType.REGEN),
    FIRE_RESISTANCE(PotionType.FIRE_RESISTANCE),
    WATER_BREATHING(PotionType.WATER_BREATHING),
    INVISIBILITY(PotionType.INVISIBILITY),
    NIGHT_VISION(PotionType.NIGHT_VISION),
    WEAKNESS(PotionType.WEAKNESS),
    POISON(PotionType.POISON),
    LUCK(PotionType.LUCK),
    SLOW_FALLING(PotionType.SLOW_FALLING),
    TURTLE_MASTER(PotionType.TURTLE_MASTER);

    private final PotionType type;

    /**
     * @param type the wrapped type
     */
    ModernPotionType(final PotionType type) {
        this.type = type;
    }

    /**
     * @return the wrapped type
     */
    public @NonNull PotionType unwrap() {
        return this.type;
    }

}
