package dev.tehbrian.iteminator.command.subs;

import dev.tehbrian.iteminator.Permission;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.inventory.ItemType;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;

import static dev.tehbrian.iteminator.util.HeldItemModifier.modify;
import static org.incendo.cloud.description.Description.description;
import static org.incendo.cloud.paper.parser.RegistryEntryParser.registryEntryParser;
import static org.incendo.cloud.parser.standard.IntegerParser.integerParser;

@SuppressWarnings("UnstableApiUsage")
public final class ItemStackCommands {

	/**
	 * @param commandManager the manager to register the commands to
	 * @param parent         the command to register the subcommands under
	 */
	public void register(
			final PaperCommandManager<Source> commandManager,
			final Command.Builder<PlayerSource> parent
	) {
		final var cAmount = parent.literal("amount")
				.commandDescription(description("Set the amount."))
				.permission(Permission.AMOUNT)
				.required("amount", integerParser(0, 99))
				.handler(c -> {
					modify(c, i -> i.amount(c.get("amount")));
				});

		final var cType = parent.literal("type")
				.commandDescription(description("Set the type."))
				.permission(Permission.TYPE)
				.required("type", registryEntryParser(RegistryKey.ITEM, TypeToken.get(ItemType.class)))
				.handler(c -> {
					modify(c, i -> i.type(c.get("type")));
				});

		commandManager
				.command(cAmount)
				.command(cType);
	}

}
