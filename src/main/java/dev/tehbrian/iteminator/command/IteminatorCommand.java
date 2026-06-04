package dev.tehbrian.iteminator.command;

import com.google.inject.Inject;
import dev.tehbrian.iteminator.command.subs.DataComponentCommands;
import dev.tehbrian.iteminator.command.subs.ItemStackCommands;
import dev.tehbrian.iteminator.command.subs.MetaCommands;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;

import static org.incendo.cloud.description.Description.description;

public final class IteminatorCommand {

	private final MetaCommands metaCommands;
	private final ItemStackCommands itemStackCommands;
	private final DataComponentCommands dataComponentCommands;

	@Inject
	public IteminatorCommand(
			final MetaCommands metaCommands,
			final ItemStackCommands itemStackCommands,
			final DataComponentCommands dataComponentCommands
	) {
		this.metaCommands = metaCommands;
		this.itemStackCommands = itemStackCommands;
		this.dataComponentCommands = dataComponentCommands;
	}

	public void register(final PaperCommandManager<Source> commandManager) {
		final var cMain = commandManager.commandBuilder("iteminator", "ia")
				.commandDescription(description("Access the Iteminator."));

		this.metaCommands.register(commandManager, cMain);

		final var cEdit = cMain.literal("edit")
				.commandDescription(description("Edit commands with ease."))
				.senderType(PlayerSource.class);

		this.itemStackCommands.register(commandManager, cEdit);
		this.dataComponentCommands.register(commandManager, cEdit);

		commandManager.commandBuilder("ie").proxies(cEdit.build());
	}

}
