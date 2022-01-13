package xyz.tehbrian.iteminator.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class Format {

    private Format() {
    }

    /**
     * @param string the Legacy-formatted string
     * @return the string as a component
     */
    public static @NonNull Component legacy(final @NonNull String string) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
    }

    /**
     * @param string the MiniMessage-formatted string
     * @return the string as a component
     */
    public static @NonNull Component miniMessage(final @NonNull String string) {
        return MiniMessage.miniMessage().deserialize(string);
    }

    /**
     * @param string the plain string
     * @return the string as a component
     */
    public static @NonNull Component plain(final @NonNull String string) {
        return PlainTextComponentSerializer.plainText().deserialize(string);
    }

}
