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
		final var cUnbreakable = parent.literal("unbreakable")
				.commandDescription(description("Edit unbreakable."))
				.permission(Permission.UNBREAKABLE);

		final var cUnbreakableSet = cUnbreakable.literal("set")
				.commandDescription(description("Set unbreakable."))
				.handler(c -> modify(c, i -> i.set(UNBREAKABLE)));

		final var cUnbreakableUnset = cUnbreakable.literal("unset")
				.commandDescription(description("Unset unbreakable."))
				.handler(c -> modify(c, i -> i.unset(UNBREAKABLE)));

		final var cUnbreakableReset = cUnbreakable.literal("reset")
				.commandDescription(description("Reset unbreakable."))
				.handler(c -> modify(c, i -> i.reset(UNBREAKABLE)));

		final var cCustomName = parent.literal("custom-name")
				.commandDescription(description("Edit custom name."))
				.permission(Permission.CUSTOM_NAME);

		final var cCustomNameSet = cCustomName.literal("set")
				.commandDescription(description("Set custom name."))
				.optional("value", greedyStringParser(), constant(""))
				.handler(c -> modify(c, i -> i.set(CUSTOM_NAME, this.userFormat(c, c.get("value")))));

		final var cCustomNameUnset = cCustomName.literal("unset")
				.commandDescription(description("Unset custom name."))
				.handler(c -> modify(c, i -> i.unset(CUSTOM_NAME)));

		final var cCustomNameReset = cCustomName.literal("reset")
				.commandDescription(description("Reset custom name."))
				.handler(c -> modify(c, i -> i.reset(CUSTOM_NAME)));

		final var cItemName = parent.literal("item-name")
				.commandDescription(description("Edit item name."))
				.permission(Permission.ITEM_NAME);

		final var cItemNameSet = cItemName.literal("set")
				.commandDescription(description("Set item name."))
				.optional("value", greedyStringParser(), constant(""))
				.handler(c -> modify(c, i -> i.set(ITEM_NAME, this.userFormat(c, c.get("value")))));

		final var cItemNameUnset = cItemName.literal("unset")
				.commandDescription(description("Unset item name."))
				.handler(c -> modify(c, i -> i.unset(ITEM_NAME)));

		final var cItemNameReset = cItemName.literal("reset")
				.commandDescription(description("Reset item name."))
				.handler(c -> modify(c, i -> i.reset(ITEM_NAME)));

		final var cDamageType = parent.literal("damage-type")
				.commandDescription(description("Edit damage type."))
				.permission(Permission.DAMAGE_TYPE);

		final var cDamageTypeSet = cDamageType.literal("set")
				.commandDescription(description("Set damage type."))
				.required("value", registryEntryParser(RegistryKey.DAMAGE_TYPE, TypeToken.get(DamageType.class)))
				.handler(c -> modify(c, i -> i.set(DAMAGE_TYPE, c.<RegistryEntry<DamageType>>get("value").value())));

		final var cDamageTypeUnset = cDamageType.literal("unset")
				.commandDescription(description("Unset damage type."))
				.handler(c -> modify(c, i -> i.unset(DAMAGE_TYPE)));

		final var cDamageTypeReset = cDamageType.literal("reset")
				.commandDescription(description("Reset damage type."))
				.handler(c -> modify(c, i -> i.reset(DAMAGE_TYPE)));

		commandManager
				.command(cUnbreakableSet)
				.command(cUnbreakableUnset)
				.command(cUnbreakableReset)
				.command(cCustomNameSet)
				.command(cCustomNameUnset)
				.command(cCustomNameReset)
				.command(cItemNameSet)
				.command(cItemNameUnset)
				.command(cItemNameReset)
				.command(cDamageTypeSet)
				.command(cDamageTypeUnset)
				.command(cDamageTypeReset);
	}

}
