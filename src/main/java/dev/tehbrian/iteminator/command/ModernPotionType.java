package dev.tehbrian.iteminator.command;

import org.bukkit.potion.PotionType;

public enum ModernPotionType {
  AWKWARD(PotionType.AWKWARD),
  THICK(PotionType.THICK),
  MUNDANE(PotionType.MUNDANE),
  WATER(PotionType.WATER),
  SWIFTNESS(PotionType.SWIFTNESS),
  SLOWNESS(PotionType.SLOWNESS),
  STRENGTH(PotionType.STRENGTH),
  HEALING(PotionType.HEALING),
  HARMING(PotionType.HARMING),
  LEAPING(PotionType.LEAPING),
  REGENERATION(PotionType.REGENERATION),
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
  public PotionType unwrap() {
    return this.type;
  }

}
