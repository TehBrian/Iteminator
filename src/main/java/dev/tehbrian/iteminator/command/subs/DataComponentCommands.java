package dev.tehbrian.iteminator.command.subs;

import com.google.inject.Inject;
import dev.tehbrian.iteminator.Permission;
import dev.tehbrian.iteminator.config.LangConfig;
import dev.tehbrian.iteminator.user.UserService;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.damage.DamageType;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.parser.RegistryEntryParser;
import org.incendo.cloud.paper.parser.RegistryEntryParser.RegistryEntry;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;

import static dev.tehbrian.iteminator.util.HeldItemModifier.modify;
import static io.papermc.paper.datacomponent.DataComponentTypes.CUSTOM_NAME;
import static io.papermc.paper.datacomponent.DataComponentTypes.DAMAGE_TYPE;
import static io.papermc.paper.datacomponent.DataComponentTypes.ITEM_NAME;
import static io.papermc.paper.datacomponent.DataComponentTypes.UNBREAKABLE;
import static org.incendo.cloud.component.DefaultValue.constant;
import static org.incendo.cloud.description.Description.description;
import static org.incendo.cloud.paper.parser.RegistryEntryParser.registryEntryParser;
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

	private Component userFormat(
			final CommandContext<PlayerSource> c,
			final String text
	) {
		return this.userService.formatWithUserFormat(text, c.sender().source());
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
				.handler(c -> modify(c, i -> i.set(UNBREAKABLE)));

		final var cUnsetUnbreakable = cUnset.literal("unbreakable")
				.commandDescription(description("Unset unbreakable."))
				.permission(Permission.UNBREAKABLE)
				.handler(c -> modify(c, i -> i.unset(UNBREAKABLE)));

		final var cResetUnbreakable = cReset.literal("unbreakable")
				.commandDescription(description("Reset unbreakable."))
				.permission(Permission.UNBREAKABLE)
				.handler(c -> modify(c, i -> i.reset(UNBREAKABLE)));

		final var cSetCustomName = cSet.literal("custom-name")
				.commandDescription(description("Set custom name."))
				.permission(Permission.CUSTOM_NAME)
				.optional("value", greedyStringParser(), constant(""))
				.handler(c -> modify(c, i -> i.set(CUSTOM_NAME, userFormat(c, c.get("value")))));

		final var cUnsetCustomName = cUnset.literal("custom-name")
				.commandDescription(description("Unset custom name."))
				.permission(Permission.CUSTOM_NAME)
				.handler(c -> modify(c, i -> i.unset(CUSTOM_NAME)));

		final var cResetCustomName = cReset.literal("custom-name")
				.commandDescription(description("Reset custom name."))
				.permission(Permission.CUSTOM_NAME)
				.handler(c -> modify(c, i -> i.reset(CUSTOM_NAME)));

		final var cSetItemName = cSet.literal("item-name")
				.commandDescription(description("Set item name."))
				.permission(Permission.ITEM_NAME)
				.optional("value", greedyStringParser(), constant(""))
				.handler(c -> modify(c, i -> i.set(ITEM_NAME, userFormat(c, c.get("value")))));

		final var cUnsetItemName = cUnset.literal("item-name")
				.commandDescription(description("Unset item name."))
				.permission(Permission.ITEM_NAME)
				.handler(c -> modify(c, i -> i.unset(ITEM_NAME)));

		final var cResetItemName = cReset.literal("item-name")
				.commandDescription(description("Reset item name."))
				.permission(Permission.ITEM_NAME)
				.handler(c -> modify(c, i -> i.reset(ITEM_NAME)));

		final var cSetDamageType = cSet.literal("damage-type")
				.commandDescription(description("Set damage type."))
				.permission(Permission.DAMAGE_TYPE)
				.required("value", registryEntryParser(RegistryKey.DAMAGE_TYPE, TypeToken.get(DamageType.class)))
				.handler(c -> modify(c, i -> i.set(DAMAGE_TYPE, c.<RegistryEntry<DamageType>>get("value").value())));

		final var cUnsetDamageType = cUnset.literal("damage-type")
				.commandDescription(description("Unset damage type."))
				.permission(Permission.DAMAGE_TYPE)
				.handler(c -> modify(c, i -> i.unset(DAMAGE_TYPE)));

		final var cResetDamageType = cReset.literal("damage-type")
				.commandDescription(description("Reset damage type."))
				.permission(Permission.DAMAGE_TYPE)
				.handler(c -> modify(c, i -> i.reset(DAMAGE_TYPE)));

		commandManager
				.command(cSetUnbreakable)
				.command(cUnsetUnbreakable)
				.command(cResetUnbreakable)
				.command(cSetCustomName)
				.command(cUnsetCustomName)
				.command(cResetCustomName)
				.command(cSetItemName)
				.command(cUnsetItemName)
				.command(cResetItemName)
				.command(cSetDamageType)
				.command(cUnsetDamageType)
				.command(cResetDamageType);
	}

}
