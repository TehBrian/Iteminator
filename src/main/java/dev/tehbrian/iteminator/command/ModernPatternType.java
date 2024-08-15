package dev.tehbrian.iteminator.command;

import org.bukkit.block.banner.PatternType;

public enum ModernPatternType {
  SQUARE_BOTTOM_LEFT(PatternType.SQUARE_BOTTOM_LEFT),
  SQUARE_BOTTOM_RIGHT(PatternType.SQUARE_BOTTOM_RIGHT),
  SQUARE_TOP_LEFT(PatternType.SQUARE_TOP_LEFT),
  SQUARE_TOP_RIGHT(PatternType.SQUARE_TOP_RIGHT),
  STRIPE_BOTTOM(PatternType.STRIPE_BOTTOM),
  STRIPE_TOP(PatternType.STRIPE_TOP),
  STRIPE_LEFT(PatternType.STRIPE_LEFT),
  STRIPE_RIGHT(PatternType.STRIPE_RIGHT),
  STRIPE_CENTER(PatternType.STRIPE_CENTER),
  STRIPE_MIDDLE(PatternType.STRIPE_MIDDLE),
  STRIPE_DOWNRIGHT(PatternType.STRIPE_DOWNRIGHT),
  STRIPE_DOWNLEFT(PatternType.STRIPE_DOWNLEFT),
  SMALL_STRIPES(PatternType.SMALL_STRIPES),
  CROSS(PatternType.CROSS),
  STRAIGHT_CROSS(PatternType.STRAIGHT_CROSS),
  TRIANGLE_BOTTOM(PatternType.TRIANGLE_BOTTOM),
  TRIANGLE_TOP(PatternType.TRIANGLE_TOP),
  TRIANGLES_BOTTOM(PatternType.TRIANGLES_BOTTOM),
  TRIANGLES_TOP(PatternType.TRIANGLES_TOP),
  DIAGONAL_LEFT(PatternType.DIAGONAL_LEFT),
  DIAGONAL_UP_RIGHT(PatternType.DIAGONAL_UP_RIGHT),
  DIAGONAL_UP_LEFT(PatternType.DIAGONAL_UP_LEFT),
  DIAGONAL_RIGHT(PatternType.DIAGONAL_RIGHT),
  CIRCLE(PatternType.CIRCLE),
  RHOMBUS(PatternType.RHOMBUS),
  HALF_VERTICAL(PatternType.HALF_VERTICAL),
  HALF_HORIZONTAL(PatternType.HALF_HORIZONTAL),
  HALF_VERTICAL_RIGHT(PatternType.HALF_VERTICAL_RIGHT),
  HALF_HORIZONTAL_BOTTOM(PatternType.HALF_HORIZONTAL_BOTTOM),
  BORDER(PatternType.BORDER),
  CURLY_BORDER(PatternType.CURLY_BORDER),
  CREEPER(PatternType.CREEPER),
  GRADIENT(PatternType.GRADIENT),
  GRADIENT_UP(PatternType.GRADIENT_UP),
  BRICKS(PatternType.BRICKS),
  SKULL(PatternType.SKULL),
  FLOWER(PatternType.FLOWER),
  MOJANG(PatternType.MOJANG),
  GLOBE(PatternType.GLOBE),
  PIGLIN(PatternType.PIGLIN),
  FLOW(PatternType.FLOW),
  GUSTER(PatternType.GUSTER);

  private final PatternType type;

  /**
   * @param type the wrapped type
   */
  ModernPatternType(final PatternType type) {
    this.type = type;
  }

  /**
   * @return the wrapped type
   */
  public PatternType unwrap() {
    return this.type;
  }

}
