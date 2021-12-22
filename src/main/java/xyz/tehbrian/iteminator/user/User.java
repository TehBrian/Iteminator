package xyz.tehbrian.iteminator.user;

import dev.tehbrian.tehlib.paper.user.PaperUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.tehbrian.iteminator.util.Permissions;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class User extends PaperUser {

    private boolean formatEnabled;
    private @NonNull FormattingType formattingType = FormattingType.LEGACY;

    public User(final @NonNull UUID uuid) {
        super(uuid);

        final @NonNull Player player = Objects.requireNonNull(this.getPlayer());
        this.formatEnabled = player.hasPermission(Permissions.FORMAT);
    }

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public boolean formatEnabled() {
        return this.formatEnabled;
    }

    public void formatEnabled(final boolean formatEnabled) {
        this.formatEnabled = formatEnabled;
    }

    public boolean toggleFormatEnabled() {
        this.formatEnabled(!this.formatEnabled());
        return this.formatEnabled();
    }

    public @NonNull FormattingType formattingType() {
        return this.formattingType;
    }

    public void formattingType(final @NonNull FormattingType formattingType) {
        this.formattingType = formattingType;
    }

    public enum FormattingType {
        LEGACY,
        MINI_MESSAGE
    }

}
