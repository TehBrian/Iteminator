package dev.tehbrian.iteminator.command;

import org.incendo.cloud.help.result.CommandEntry;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.suggestion.Suggestion;

import static org.incendo.cloud.component.DefaultValue.constant;
import static org.incendo.cloud.parser.standard.StringParser.greedyStringParser;
import static org.incendo.cloud.suggestion.SuggestionProvider.blocking;

public final class FluCommand {

	public void register(final PaperCommandManager<Source> commandManager) {
		final var cFlu = commandManager.commandBuilder("flu");

		commandManager.command(cFlu
				.literal("animal").literal("dog")
				.handler(c -> c.sender().source().sendMessage("epic!")));
		commandManager.command(cFlu
				.literal("animal").literal("cat")
				.handler(c -> c.sender().source().sendMessage("epic!")));
		commandManager.command(cFlu
				.literal("food").literal("icecream").literal("vanilla")
				.handler(c -> c.sender().source().sendMessage("epic!")));
		commandManager.command(cFlu
				.literal("food").literal("icecream").literal("chocolate")
				.handler(c -> c.sender().source().sendMessage("epic!")));
		commandManager.command(cFlu
				.literal("place").literal("europe").literal("korea").literal("north")
				.handler(c -> c.sender().source().sendMessage("epic!")));
		commandManager.command(cFlu
				.literal("place").literal("europe").literal("korea").literal("south")
				.handler(c -> c.sender().source().sendMessage("epic!")));

		final var fluHelp = MinecraftHelp.create(
				"/flu help",
				commandManager,
				Source::source
		);

		commandManager.command(cFlu.literal("help")
				.optional(
						"query", greedyStringParser(), constant(""),
						blocking((c, _) ->
								commandManager.createHelpHandler().queryRootIndex(c.sender())
										.entries().stream().map(CommandEntry::syntax).map(Suggestion::suggestion).toList()
						)
				)
				.handler(c -> fluHelp.queryCommands(c.get("query"), c.sender()))
		);
	}

}
