package dev.tehbrian.iteminator.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class Format {

  private Format() {
  }

  /**
   * @param string the Legacy-formatted string
   * @return the string as a component
   */
  public static Component legacy(final String string) {
    return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
  }

  /**
   * @param string the MiniMessage-formatted string
   * @return the string as a component
   */
  public static Component miniMessage(final String string) {
    return MiniMessage.miniMessage().deserialize(string);
  }

  /**
   * @param string the plain string
   * @return the string as a component
   */
  public static Component plain(final String string) {
    return PlainTextComponentSerializer.plainText().deserialize(string);
  }

}
