package dev.tehbrian.iteminator;

import net.kyori.adventure.text.format.TextColor;

import java.util.Objects;

// https://coolors.co/eeeeff-aaaabb-333344-88eeff-44aabb-dd5577
public final class Color {

  // yeah, I'm wrapping a non-parameter in Objects.requireNonNull. sue me.
  public static final TextColor WHITE = Objects.requireNonNull(TextColor.fromCSSHexString("#EEEEFF"));
  public static final TextColor LIGHT_GRAY = Objects.requireNonNull(TextColor.fromCSSHexString("#AAAABB"));
  public static final TextColor DARK_GRAY = Objects.requireNonNull(TextColor.fromCSSHexString("#333344"));
  public static final TextColor LIGHT_BLUE = Objects.requireNonNull(TextColor.fromCSSHexString("#88EEFF"));
  public static final TextColor DARK_BLUE = Objects.requireNonNull(TextColor.fromCSSHexString("#44AABB"));
  public static final TextColor RED = Objects.requireNonNull(TextColor.fromCSSHexString("#DD5577"));

  private Color() {
  }

}
