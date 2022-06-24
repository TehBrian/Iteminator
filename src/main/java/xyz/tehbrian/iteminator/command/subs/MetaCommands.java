package xyz.tehbrian.iteminator.command.subs;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.minecraft.extras.AudienceProvider;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.iteminator.Iteminator;
import xyz.tehbrian.iteminator.config.LangConfig;
import xyz.tehbrian.iteminator.user.User;
import xyz.tehbrian.iteminator.user.UserService;
import xyz.tehbrian.iteminator.util.Colors;
import xyz.tehbrian.iteminator.util.Permissions;

@SuppressWarnings("ClassCanBeRecord")
public final class MetaCommands {

    private final Iteminator iteminator;
    private final UserService userService;
    private final LangConfig langConfig;

    @Inject
    public MetaCommands(
            final @NonNull Iteminator iteminator,
            final @NonNull UserService userService,
            final @NonNull LangConfig langConfig
    ) {
        this.iteminator = iteminator;
        this.userService = userService;
        this.langConfig = langConfig;
    }

    /**
     * @param commandManager the manager to register the commands to
     * @param parent         the command to register the subcommands under
     */
    public void registerMeta(
            final @NonNull PaperCommandManager<CommandSender> commandManager,
            final Command.@NonNull Builder<CommandSender> parent
    ) {
        final var cMain = parent.handler(c -> c.getSender().sendMessage(
                this.langConfig.c(
                        NodePath.path("main"),
                        Placeholder.unparsed("version", this.iteminator.getDescription().getVersion())
                ))
        );

        final var help = new MinecraftHelp<>(
                "/iteminator help",
                AudienceProvider.nativeAudience(), commandManager
        );

        help.setHelpColors(
                MinecraftHelp.HelpColors.of(
                        Colors.GRAY,
                        Colors.LIGHT_BLUE,
                        Colors.DARK_BLUE,
                        Colors.WHITE,
                        Colors.GRAY
                )
        );

        final var cHelp = parent.literal("help")
                .argument(StringArgument.optional("query", StringArgument.StringMode.GREEDY))
                .handler(c -> help.queryCommands(
                        // prepend "iteminator " to non-empty queries without root command for usability
                        c.<String>getOptional("query").map(s -> {
                            if (!s.startsWith("iteminator")) {
                                return "iteminator " + s;
                            }
                            return s;
                        }).orElse(""),
                        c.getSender()
                ));

        final var cReload = parent.literal("reload")
                .meta(CommandMeta.DESCRIPTION, "Reload the plugin's config.")
                .permission(Permissions.RELOAD)
                .handler(c -> {
                    if (this.iteminator.loadConfiguration()) {
                        c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "success")));
                    } else {
                        c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "fail")));
                    }
                });

        final var cFormat = parent.literal("format")
                .meta(CommandMeta.DESCRIPTION, "Toggle your ability to format text.")
                .permission(Permissions.FORMAT)
                .senderType(Player.class)
                .handler(c -> {
                    final Player sender = (Player) c.getSender();
                    if (this.userService.getUser(sender).toggleFormatEnabled()) {
                        sender.sendMessage(this.langConfig.c(NodePath.path("format", "enable")));
                    } else {
                        sender.sendMessage(this.langConfig.c(NodePath.path("format", "disable")));
                    }
                });

        final var cFormatFormattingType = cFormat
                .argument(EnumArgument.of(User.FormattingType.class, "formatting_type"))
                .handler(c -> {
                    final @NonNull Player player = (Player) c.getSender();
                    final User.@NonNull FormattingType formattingType = c.get("formatting_type");

                    this.userService.getUser(player).formattingType(formattingType);
                    player.sendMessage(this.langConfig.c(
                            NodePath.path("format", "set"),
                            Placeholder.unparsed("formatting_type", formattingType.toString())
                    ));
                });

        commandManager.command(cMain)
                .command(cHelp)
                .command(cReload)
                .command(cFormat)
                .command(cFormatFormattingType);
    }

}
