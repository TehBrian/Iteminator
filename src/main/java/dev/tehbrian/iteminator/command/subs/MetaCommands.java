package dev.tehbrian.iteminator.command.subs;

import com.google.inject.Inject;
import dev.tehbrian.iteminator.Iteminator;
import dev.tehbrian.iteminator.Permission;
import dev.tehbrian.iteminator.config.LangConfig;
import dev.tehbrian.iteminator.user.User;
import dev.tehbrian.iteminator.user.UserService;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.incendo.cloud.Command;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.spongepowered.configurate.NodePath;

import static org.incendo.cloud.component.DefaultValue.constant;
import static org.incendo.cloud.description.Description.description;
import static org.incendo.cloud.minecraft.extras.MinecraftHelp.helpColors;
import static org.incendo.cloud.parser.standard.EnumParser.enumParser;
import static org.incendo.cloud.parser.standard.StringParser.greedyStringParser;
import static org.incendo.cloud.suggestion.SuggestionProvider.blockingStrings;

public final class MetaCommands {

	private static final String ROOT_NAME = "iteminator";

	private final Iteminator iteminator;
	private final UserService userService;
	private final LangConfig langConfig;

	private MinecraftHelp<Source> help;

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
		if (!s.startsWith(ROOT_NAME) && !isInt(s)) {
			return ROOT_NAME + " " + s;
		}
		return s;
	}

	/**
	 * @param str a string
	 * @return whether the string can be parsed as an integer
	 */
	private static boolean isInt(final String str) {
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
			final PaperCommandManager<Source> commandManager,
			final Command.Builder<Source> parent
	) {
		final var cMain = parent.handler(c -> c.sender().source().sendMessage(
				this.langConfig.c(
						NodePath.path("main"),
						Placeholder.unparsed("version", this.iteminator.getPluginMeta().getVersion())
				))
		);

		this.initHelp(commandManager);

		final var cHelp = parent.literal("help")
				.optional(
						"query", greedyStringParser(), constant(""),
						blockingStrings((c, _) ->
								commandManager.createHelpHandler()
										.queryRootIndex(c.sender())
										.entries()
										.stream()
										.map(entry -> entry.syntax().split(" ", 2))
										.filter(splitSyntax -> splitSyntax.length > 1) // skip root command.
										.map(splitSyntax -> splitSyntax[1])
										.toList())
				)
				.handler(c ->
						this.help.queryCommands(prependRoot(c.<String>get("query")), c.sender())
				);

		final var cReload = parent.literal("reload")
				.commandDescription(description("Reload the plugin's config."))
				.permission(Permission.RELOAD)
				.handler(c -> {
					if (this.iteminator.loadConfiguration()) {
						this.initHelp(commandManager);
						c.sender().source().sendMessage(this.langConfig.c(NodePath.path("reload", "success")));
					} else {
						c.sender().source().sendMessage(this.langConfig.c(NodePath.path("reload", "fail")));
					}
				});

		final var cFormat = parent.literal("format")
				.commandDescription(description("Toggle your ability to format text."))
				.permission(Permission.FORMAT)
				.senderType(PlayerSource.class)
				.handler(c -> {
					final var sender = c.sender().source();
					if (this.userService.getUser(sender).toggleFormatEnabled()) {
						sender.sendMessage(this.langConfig.c(NodePath.path("format", "enable")));
					} else {
						sender.sendMessage(this.langConfig.c(NodePath.path("format", "disable")));
					}
				});

		final var cFormatFormattingType = cFormat
				.required("formatting_type", enumParser(User.FormattingType.class))
				.handler(c -> {
					final var sender = c.sender().source();
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

	private void initHelp(final PaperCommandManager<Source> commandManager) {
		this.help = MinecraftHelp.<Source>builder()
				.commandManager(commandManager)
				.audienceProvider(Source::source)
				.commandPrefix("/iteminator help")
				.colors(helpColors(
						this.langConfig.color(NodePath.path("help-colors", "primary")),
						this.langConfig.color(NodePath.path("help-colors", "highlight")),
						this.langConfig.color(NodePath.path("help-colors", "alternate-highlight")),
						this.langConfig.color(NodePath.path("help-colors", "text")),
						this.langConfig.color(NodePath.path("help-colors", "accent"))
				))
				.build();
	}

}
