package xyz.tehbrian.iteminator.user;

import dev.tehbrian.tehlib.paper.user.PaperUserService;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.iteminator.util.Format;
import xyz.tehbrian.iteminator.util.Permissions;

import java.util.UUID;

public final class UserService extends PaperUserService<User> {

    @Override
    public @NonNull User getUser(final @NonNull UUID uuid) {
        return this.userMap.computeIfAbsent(uuid, User::new);
    }

    /**
     * Why is this in {@link UserService}? Because frick you. That's why. (Maybe
     * this should be moved to somewhere more appropriate?)
     *
     * @param string the string to be formatted
     * @param player the player to get the format from
     * @return the string formatted with the user's formatting type
     */
    public @NonNull Component formatWithUserFormat(final @NonNull String string, final @NonNull Player player) {
        final @NonNull User user = this.getUser(player.getUniqueId());

        if (player.hasPermission(Permissions.FORMAT) && user.formatEnabled()) {
            if (user.formattingType() == User.FormattingType.LEGACY && player.hasPermission(Permissions.LEGACY)) {
                return Format.legacy(string);
            } else if (user.formattingType() == User.FormattingType.MINIMESSAGE && player.hasPermission(Permissions.MINIMESSAGE)) {
                return Format.miniMessage(string);
            }
        }

        return Format.plain(string);
    }

}
