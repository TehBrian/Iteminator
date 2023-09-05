package dev.tehbrian.iteminator.util;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FormatTest {

  private static final String TEST_STRING = "<red>We're using <gold>MiniMessage</gold> format, but &1now &9we're using &b&lLegacy!";

  @Test
  public void legacy() {
    assertEquals(LegacyComponentSerializer.legacyAmpersand().deserialize(TEST_STRING), Format.legacy(TEST_STRING));
  }

  @Test
  public void miniMessage() {
    assertEquals(MiniMessage.miniMessage().deserialize(TEST_STRING), Format.miniMessage(TEST_STRING));
  }

  @Test
  public void plain() {
    assertEquals(PlainTextComponentSerializer.plainText().deserialize(TEST_STRING), Format.plain(TEST_STRING));
  }

}
