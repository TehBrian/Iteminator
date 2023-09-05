package dev.tehbrian.iteminator.user;

import dev.tehbrian.iteminator.Permissions;
import dev.tehbrian.iteminator.util.Format;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class UserService {

  private final Map<UUID, User> userMap = new HashMap<>();

  public User getUser(final UUID uuid) {
    return this.userMap.computeIfAbsent(uuid, User::new);
  }

  public User getUser(final Player player) {
    return this.getUser(player.getUniqueId());
  }

  /**
   * Why is this in {@link UserService}? Because frick you. That's why. (Maybe
   * this should be moved to somewhere more appropriate?)
   *
   * @param string the string to be formatted
   * @param player the player to get the format from
   * @return the string formatted with the user's formatting type
   */
  public Component formatWithUserFormat(final String string, final Player player) {
    if (string.isEmpty()) {
      return Component.empty();
    }

    final User user = this.getUser(player.getUniqueId());

    if (player.hasPermission(Permissions.FORMAT) && user.formatEnabled()) {
      if (user.formattingType() == User.FormattingType.LEGACY && player.hasPermission(Permissions.LEGACY)) {
        return Format.legacy(string);
      } else if (user.formattingType() == User.FormattingType.MINIMESSAGE && player.hasPermission(Permissions.MINIMESSAGE)) {
        return Format.miniMessage(string);
      }
    }

    return Format.plain(string);
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType") // used for utility, not logic.
  public Component formatWithUserFormat(final Optional<String> string, final Player player) {
    if (string.isPresent()) {
      return this.formatWithUserFormat(string.get(), player);
    } else {
      return Component.empty();
    }
  }

}
