package dev.tehbrian.iteminator.user;

import dev.tehbrian.iteminator.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.UUID;

public final class User {

  private final UUID uuid;

  private boolean formatEnabled;
  private FormattingType formattingType = FormattingType.LEGACY;

  /**
   * @param uuid the unique identifier of the user
   */
  public User(final UUID uuid) {
    this.uuid = uuid;
    final Player player = Objects.requireNonNull(this.getPlayer());
    this.formatEnabled = player.hasPermission(Permissions.FORMAT);
  }

  /**
   * @return the Bukkit {@link Player} with this user's uuid
   */
  public @Nullable Player getPlayer() {
    return Bukkit.getPlayer(this.uuid);
  }

  /**
   * @return whether the user has formatting enabled
   */
  public boolean formatEnabled() {
    return this.formatEnabled;
  }

  /**
   * @param formatEnabled whether the user has formatting enabled
   */
  public void formatEnabled(final boolean formatEnabled) {
    this.formatEnabled = formatEnabled;
  }

  /**
   * Toggles whether the user has formatting enabled.
   *
   * @return whether the user has formatting enabled after toggle
   */
  public boolean toggleFormatEnabled() {
    this.formatEnabled(!this.formatEnabled());
    return this.formatEnabled();
  }

  /**
   * @return the user's formatting type
   */
  public User.FormattingType formattingType() {
    return this.formattingType;
  }

  /**
   * @param formattingType the user's formatting type
   */
  public void formattingType(final User.FormattingType formattingType) {
    this.formattingType = formattingType;
  }

  public enum FormattingType {
    LEGACY,
    MINIMESSAGE
  }

}
