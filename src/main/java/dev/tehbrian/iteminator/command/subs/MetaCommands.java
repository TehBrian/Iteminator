package dev.tehbrian.iteminator.command.subs;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.minecraft.extras.AudienceProvider;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.iteminator.Color;
import dev.tehbrian.iteminator.Iteminator;
import dev.tehbrian.iteminator.Permission;
import dev.tehbrian.iteminator.config.LangConfig;
import dev.tehbrian.iteminator.user.User;
import dev.tehbrian.iteminator.user.UserService;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.NodePath;

import java.util.ArrayList;
import java.util.List;

public final class MetaCommands {

  private static final String ROOT_NAME = "iteminator";

  private final Iteminator iteminator;
  private final UserService userService;
  private final LangConfig langConfig;

  @Inject
  public MetaCommands(
      final Iteminator iteminator,
      final UserService userService,
      final LangConfig langConfig
  ) {
    this.iteminator = iteminator;
    this.userService = userService;
    this.langConfig = langConfig;
  }

  /**
   * Prepend the root command name to non-empty, non-numerical queries that
   * don't already have the root command at the start of the string.
   *
   * @param s the string
   * @return the string with the root command name prepended
   */
  private static String prependRoot(final String s) {
    if (!s.startsWith(ROOT_NAME) && !isInteger(s)) {
      return ROOT_NAME + " " + s;
    }
    return s;
  }

  /**
   * @param str a string
   * @return whether the string can be parsed as an integer
   */
  private static boolean isInteger(final String str) {
    try {
      Integer.parseInt(str);
    } catch (final NumberFormatException e) {
      return false;
    }
    return true;
  }

  /**
   * @param commandManager the manager to register the commands to
   * @param parent         the command to register the subcommands under
   */
  public void registerMeta(
      final PaperCommandManager<CommandSender> commandManager,
      final Command.Builder<CommandSender> parent
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
            Color.LIGHT_GRAY,
            Color.LIGHT_BLUE,
            Color.DARK_BLUE,
            Color.WHITE,
            Color.DARK_GRAY
        )
    );

    final var cHelp = parent.literal("help")
        .argument(StringArgument.<CommandSender>builder("query")
            .greedy().asOptional()
            .withSuggestionsProvider((c, input) -> {
              final List<String> suggestions = new ArrayList<>();
              final var helpTopic = commandManager.createCommandHelpHandler().queryRootIndex(c.getSender());
              for (final var entry : helpTopic.getEntries()) {
                // remove first word (root).
                final String[] splitSyntax = entry.getSyntaxString().split(" ", 2);
                if (splitSyntax.length < 2) {
                  // if it was a suggestion for the root command itself.
                  continue;
                }
                suggestions.add(splitSyntax[1]);
              }
              return suggestions;
            }))
        .handler(c -> help.queryCommands(
            c.<String>getOptional("query").map(MetaCommands::prependRoot).orElse(""),
            c.getSender()
        ));

    final var cReload = parent.literal("reload")
        .meta(CommandMeta.DESCRIPTION, "Reload the plugin's config.")
        .permission(Permission.RELOAD)
        .handler(c -> {
          if (this.iteminator.loadConfiguration()) {
            c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "success")));
          } else {
            c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "fail")));
          }
        });

    final var cFormat = parent.literal("format")
        .meta(CommandMeta.DESCRIPTION, "Toggle your ability to format text.")
        .permission(Permission.FORMAT)
        .senderType(Player.class)
        .handler(c -> {
          final var sender = (Player) c.getSender();
          if (this.userService.getUser(sender).toggleFormatEnabled()) {
            sender.sendMessage(this.langConfig.c(NodePath.path("format", "enable")));
          } else {
            sender.sendMessage(this.langConfig.c(NodePath.path("format", "disable")));
          }
        });

    final var cFormatFormattingType = cFormat
        .argument(EnumArgument.of(User.FormattingType.class, "formatting_type"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          final User.FormattingType formattingType = c.get("formatting_type");

          this.userService.getUser(sender).formattingType(formattingType);
          sender.sendMessage(this.langConfig.c(
              NodePath.path("format", "set"),
              Placeholder.unparsed("formatting_type", formattingType.toString())
          ));
        });

    commandManager
        .command(cMain)
        .command(cHelp)
        .command(cReload)
        .command(cFormat)
        .command(cFormatFormattingType);
  }

}
