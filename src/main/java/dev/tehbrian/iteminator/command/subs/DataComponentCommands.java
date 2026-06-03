package dev.tehbrian.iteminator.command.subs;

import com.google.inject.Inject;
import dev.tehbrian.iteminator.Permission;
import dev.tehbrian.iteminator.config.LangConfig;
import dev.tehbrian.iteminator.user.UserService;
import dev.tehbrian.iteminator.util.ItemModifier;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;

import static dev.tehbrian.iteminator.util.HeldItemModifier.modify;
import static org.incendo.cloud.component.DefaultValue.constant;
import static org.incendo.cloud.description.Description.description;
import static org.incendo.cloud.parser.standard.StringParser.greedyStringParser;

@SuppressWarnings("UnstableApiUsage")
public final class DataComponentCommands {

	private final UserService userService;
	private final LangConfig langConfig;

	@Inject
	public DataComponentCommands(
			final UserService userService,
			final LangConfig langConfig
	) {
		this.userService = userService;
		this.langConfig = langConfig;
	}

	/**
	 * @param commandManager the manager to register the commands to
	 * @param parent         the command to register the subcommands under
	 */
	public void register(
			final PaperCommandManager<Source> commandManager,
			final Command.Builder<PlayerSource> parent
	) {
		final var cSet = parent.literal("set")
				.commandDescription(description("Set a data component."));

		final var cUnset = parent.literal("unset")
				.commandDescription(description("Unset a data component."));

		final var cReset = parent.literal("reset")
				.commandDescription(description("Reset a data component."));

		final var cSetUnbreakable = cSet.literal("unbreakable")
				.commandDescription(description("Set unbreakable."))
				.permission(Permission.UNBREAKABLE)
				.handler(c -> modify(c, ItemModifier::setUnbreakable));

		final var cUnsetUnbreakable = cUnset.literal("unbreakable")
				.commandDescription(description("Unset unbreakable."))
				.permission(Permission.UNBREAKABLE)
				.handler(c -> modify(c, ItemModifier::unsetUnbreakable));

		final var cResetUnbreakable = cReset.literal("unbreakable")
				.commandDescription(description("Reset unbreakable."))
				.permission(Permission.UNBREAKABLE)
				.handler(c -> modify(c, ItemModifier::resetUnbreakable));

		final var cSetCustomName = cSet.literal("custom-name")
				.commandDescription(description("Set custom name."))
				.permission(Permission.CUSTOM_NAME)
				.optional("value", greedyStringParser(), constant(""))
				.handler(c -> modify(
						c, i -> i.setCustomName(
								this.userService.formatWithUserFormat(c.get("value"), c.sender().source())
						)
				));

		final var cUnsetCustomName = cUnset.literal("custom-name")
				.commandDescription(description("Unset custom name."))
				.permission(Permission.CUSTOM_NAME)
				.handler(c -> modify(c, ItemModifier::unsetCustomName));

		final var cResetCustomName = cReset.literal("custom-name")
				.commandDescription(description("Reset custom name."))
				.permission(Permission.CUSTOM_NAME)
				.handler(c -> modify(c, ItemModifier::resetCustomName));

		final var cSetItemName = cSet.literal("item-name")
				.commandDescription(description("Set name."))
				.permission(Permission.CUSTOM_NAME)
				.optional("value", greedyStringParser(), constant(""))
				.handler(c -> modify(
						c, i -> i.setItemName(
								this.userService.formatWithUserFormat(c.get("value"), c.sender().source())
						)
				));

		final var cUnsetItemName = cUnset.literal("item-name")
				.commandDescription(description("Unset name."))
				.permission(Permission.CUSTOM_NAME)
				.handler(c -> modify(c, ItemModifier::unsetItemName));

		final var cResetItemName = cReset.literal("item-name")
				.commandDescription(description("Reset name."))
				.permission(Permission.CUSTOM_NAME)
				.handler(c -> modify(c, ItemModifier::resetItemName));

		commandManager
				.command(cSetUnbreakable)
				.command(cUnsetUnbreakable)
				.command(cResetUnbreakable)
				.command(cSetCustomName)
				.command(cUnsetCustomName)
				.command(cResetCustomName)
				.command(cSetItemName)
				.command(cUnsetItemName)
				.command(cResetItemName);
	}

}
