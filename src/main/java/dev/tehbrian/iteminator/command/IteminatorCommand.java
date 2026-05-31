package dev.tehbrian.iteminator.command;

import com.google.inject.Inject;
import dev.tehbrian.iteminator.command.subs.CommonCommands;
import dev.tehbrian.iteminator.command.subs.MetaCommands;
import dev.tehbrian.iteminator.command.subs.SpecialCommands;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;

import static org.incendo.cloud.description.Description.description;

public final class IteminatorCommand {

	private final MetaCommands metaCommands;
	private final CommonCommands commonCommands;
	private final SpecialCommands specialCommands;

	@Inject
	public IteminatorCommand(
			final MetaCommands metaCommands,
			final CommonCommands commonCommands,
			final SpecialCommands specialCommands
	) {
		this.metaCommands = metaCommands;
		this.commonCommands = commonCommands;
		this.specialCommands = specialCommands;
	}

	public void register(final PaperCommandManager<Source> commandManager) {
		final var cMain = commandManager.commandBuilder("iteminator", "ia")
				.commandDescription(description("The main command for Iteminator."));

		this.metaCommands.registerMeta(commandManager, cMain);

		final var cCommon = cMain.senderType(PlayerSource.class);
		this.commonCommands.registerCommon(commandManager, cCommon);

		final var cSpecial = cMain.literal("special", "s")
				.commandDescription(description("Commands special to specific item types."))
				.senderType(PlayerSource.class);
		this.specialCommands.registerSpecial(commandManager, cSpecial);
	}

}
