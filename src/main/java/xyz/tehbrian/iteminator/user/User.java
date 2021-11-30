package xyz.tehbrian.iteminator.user;

import dev.tehbrian.tehlib.paper.user.PaperUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.tehbrian.iteminator.Permissions;

import java.util.Objects;
import java.util.UUID;

public final class User extends PaperUser {

    private boolean colorEnabled;
    private @NonNull FormattingType formattingType = FormattingType.LEGACY;

    public User(final @NonNull UUID uuid) {
        super(uuid);

        final @NonNull Player player = Objects.requireNonNull(this.getPlayer());
        this.colorEnabled = player.hasPermission(Permissions.COLOR);
    }

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public boolean colorEnabled() {
        return this.colorEnabled;
    }

    public void colorEnabled(final boolean colorEnabled) {
        this.colorEnabled = colorEnabled;
    }

    public boolean toggleColorEnabled() {
        this.colorEnabled(!this.colorEnabled());
        return this.colorEnabled();
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
