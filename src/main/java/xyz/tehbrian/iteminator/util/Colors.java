package xyz.tehbrian.iteminator.util;

import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

public final class Colors {

    // yeah, I'm wrapping a non-parameter in Objects.requireNonNull. sue me.
    public static final TextColor WHITE = Objects.requireNonNull(TextColor.fromCSSHexString("#FFEEFF"));
    public static final TextColor GRAY = Objects.requireNonNull(TextColor.fromCSSHexString("#333344"));
    public static final TextColor LIGHT_BLUE = Objects.requireNonNull(TextColor.fromCSSHexString("#88EEFF"));
    public static final TextColor DARK_BLUE = Objects.requireNonNull(TextColor.fromCSSHexString("#44BBDD"));
    public static final TextColor RED = Objects.requireNonNull(TextColor.fromCSSHexString("#DD5577"));

    private Colors() {
    }

}
