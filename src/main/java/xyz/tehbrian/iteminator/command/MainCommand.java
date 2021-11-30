package xyz.tehbrian.iteminator.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.iteminator.Iteminator;
import xyz.tehbrian.iteminator.config.LangConfig;
import xyz.tehbrian.iteminator.user.UserService;

public final class MainCommand extends PaperCloudCommand<CommandSender> {

    private final Iteminator iteminator;
    private final UserService userService;
    private final LangConfig langConfig;

    @Inject
    public MainCommand(
            final @NonNull Iteminator iteminator,
            final @NonNull UserService userService,
            final @NonNull LangConfig langConfig
    ) {
        this.iteminator = iteminator;
        this.userService = userService;
        this.langConfig = langConfig;
    }

    @Override
    public void register(final @NonNull PaperCommandManager<CommandSender> commandManager) {
        final var main = commandManager.commandBuilder("iteminator", ArgumentDescription.of("The main command for Iteminator."))
                .handler(c -> {
                    c.getSender().sendMessage(this.langConfig.c(NodePath.path("main")));
                });

        final var name = main.literal("name")
                .meta(CommandMeta.DESCRIPTION, "Set the item's name.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                });

        commandManager.command(main)
                .command(name);
    }

}
