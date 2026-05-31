package dev.tehbrian.iteminator.command.subs;

import com.google.inject.Inject;
import dev.tehbrian.iteminator.Permission;
import dev.tehbrian.iteminator.command.EnumEquipmentSlotGroup;
import dev.tehbrian.iteminator.config.LangConfig;
import dev.tehbrian.iteminator.user.UserService;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.spongepowered.configurate.NodePath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.tehbrian.iteminator.util.HeldItemModifier.modify;
import static org.incendo.cloud.bukkit.parser.MaterialParser.materialParser;
import static org.incendo.cloud.component.DefaultValue.constant;
import static org.incendo.cloud.description.Description.description;
import static org.incendo.cloud.paper.parser.RegistryEntryParser.registryEntryParser;
import static org.incendo.cloud.parser.standard.BooleanParser.booleanParser;
import static org.incendo.cloud.parser.standard.DoubleParser.doubleParser;
import static org.incendo.cloud.parser.standard.EnumParser.enumParser;
import static org.incendo.cloud.parser.standard.IntegerParser.integerParser;
import static org.incendo.cloud.parser.standard.StringParser.greedyStringParser;
import static org.incendo.cloud.parser.standard.StringParser.quotedStringParser;

public final class CommonCommands {

	private final UserService userService;
	private final LangConfig langConfig;

	@Inject
	public CommonCommands(
			final UserService userService,
			final LangConfig langConfig
	) {
		this.userService = userService;
		this.langConfig = langConfig;
	}

	public static Player sender(final CommandContext<PlayerSource> c) {
		return c.sender().source();
	}

	/**
	 * @param commandManager the manager to register the commands to
	 * @param parent         the command to register the subcommands under
	 */
	public void registerCommon(
			final PaperCommandManager<Source> commandManager,
			final Command.Builder<PlayerSource> parent
	) {
		final var cAmount = parent.literal("amount")
				.commandDescription(description("Set the amount."))
				.permission(Permission.AMOUNT)
				.required("amount", integerParser(0, 99))
				.handler(c -> modify(sender(c), b -> b.amount(c.get("amount"))));

		final var cCustomModelData = parent.literal("custom-model-data")
				.commandDescription(description("Set the custom model data. Pass nothing to reset."))
				.permission(Permission.CUSTOM_MODEL_DATA)
				.optional("data", integerParser())
				.handler(c -> modify(sender(c), b -> b.customModelData(c.getOrDefault("data", null))));

		final var cMaterial = parent.literal("material")
				.commandDescription(description("Set the material."))
				.permission(Permission.MATERIAL)
				.required("material", materialParser())
				.handler(c -> modify(sender(c), b -> b.material(c.get("material"))));

		final var cName = parent.literal("name")
				.commandDescription(description("Name-related commands."))
				.permission(Permission.NAME);

		final var cNameSet = cName.literal("set")
				.commandDescription(description("Set the name."))
				.optional("text", greedyStringParser(), constant(""))
				.handler(c -> modify(
						sender(c),
						b -> b.name(this.userService.formatWithUserFormat(c.get("text"), sender(c)))
				));

		final var cNameReset = cName.literal("reset")
				.commandDescription(description("Reset the name to the item's default."))
				.handler(c -> modify(sender(c), b -> b.name(null)));

		final var cUnbreakable = parent.literal("unbreakable")
				.commandDescription(description("Set the unbreakable flag."))
				.permission(Permission.UNBREAKABLE)
				.required("value", booleanParser())
				.handler(c -> modify(sender(c), b -> b.unbreakable(c.get("boolean"))));

		commandManager
				.command(cAmount)
				.command(cCustomModelData)
				.command(cMaterial)
				.command(cNameSet)
				.command(cNameReset)
				.command(cUnbreakable);

		final var cAttribute = parent.literal("attribute")
				.commandDescription(description("Attribute-related commands."))
				.permission(Permission.ATTRIBUTE);

		final var cAttributeAdd = cAttribute.literal("add")
				.commandDescription(description("Add an attribute."))
				.required("attribute", registryEntryParser(RegistryKey.ATTRIBUTE, TypeToken.get(Attribute.class)))
				.required("name", quotedStringParser())
				.required("amount", doubleParser())
				.required("operation", enumParser(AttributeModifier.Operation.class))
				.optional("equipment_slot", enumParser(EnumEquipmentSlotGroup.class), constant(EnumEquipmentSlotGroup.ANY))
				.handler(c -> {
					final var modifier = new AttributeModifier(
							new NamespacedKey("iteminator", c.get("name")),
							c.get("amount"),
							c.get("operation"),
							c.<EnumEquipmentSlotGroup>get("equipment_slot").get()
					);

					modify(sender(c), b -> b.addAttributeModifier(c.get("attribute"), modifier));
				});

		final var cAttributeRemove = cAttribute.literal("remove")
				.commandDescription(description("Remove an attribute."))
				.required("attribute", registryEntryParser(RegistryKey.ATTRIBUTE, TypeToken.get(Attribute.class)))
				.handler(c -> modify(sender(c), b -> b.removeAttributeModifier(c.get("attribute"))));

		final var cAttributeClear = cAttribute.literal("clear")
				.commandDescription(description("Clear the attributes."))
				.handler(c -> modify(sender(c), b -> b.attributeModifiers(null)));

		commandManager
				.command(cAttributeAdd)
				.command(cAttributeRemove)
				.command(cAttributeClear);

		final var cEnchantment = parent.literal("enchantment")
				.commandDescription(description("Enchantment-related commands."))
				.permission(Permission.ENCHANTMENT);

		final var cEnchantmentAdd = cEnchantment.literal("add")
				.commandDescription(description("Add an enchantment."))
				.required("type", registryEntryParser(RegistryKey.ENCHANTMENT, TypeToken.get(Enchantment.class)))
				.required("level", integerParser(0, 255))
				.handler(c -> modify(sender(c), b -> b.addEnchant(c.get("type"), c.get("level"))));

		final var cEnchantmentRemove = cEnchantment.literal("remove")
				.commandDescription(description("Remove an enchantment."))
				.required("type", registryEntryParser(RegistryKey.ENCHANTMENT, TypeToken.get(Enchantment.class)))
				.handler(c -> modify(sender(c), b -> b.removeEnchant(c.get("type"))));

		final var cEnchantmentClear = cEnchantment.literal("clear")
				.commandDescription(description("Clear the enchantments."))
				.handler(c -> modify(sender(c), b -> b.enchants(null)));

		commandManager
				.command(cEnchantmentAdd)
				.command(cEnchantmentRemove)
				.command(cEnchantmentClear);

		final var cFlags = parent.literal("flags")
				.commandDescription(description("Flag-related commands."))
				.permission(Permission.FLAGS);

		final var cFlagsAdd = cFlags.literal("add")
				.commandDescription(description("Add a flag."))
				.required("flag", enumParser(ItemFlag.class))
				.handler(c -> modify(sender(c), b -> b.addFlag(c.get("flag"))));

		final var cFlagsRemove = cFlags.literal("remove")
				.commandDescription(description("Remove a flag."))
				.required("flag", enumParser(ItemFlag.class))
				.handler(c -> modify(sender(c), b -> b.removeFlag(c.get("flag"))));

		final var cFlagsClear = cFlags.literal("clear")
				.commandDescription(description("Clear the flags."))
				.handler(c -> modify(sender(c), b -> b.flags(null)));

		commandManager
				.command(cFlagsAdd)
				.command(cFlagsRemove)
				.command(cFlagsClear);

		final var cLore = parent.literal("lore")
				.commandDescription(description("Lore-related commands."))
				.permission(Permission.LORE);

		final var cLoreAdd = cLore.literal("add")
				.commandDescription(description("Add a line of lore."))
				.optional("text", greedyStringParser(), constant(""))
				.handler(c -> modify(
						sender(c), b -> {
							final List<Component> lore = Optional.ofNullable(b.lore()).map(ArrayList::new).orElse(new ArrayList<>());
							lore.add(this.userService.formatWithUserFormat(c.get("text"), sender(c)));
							return b.lore(lore);
						}
				));

		final var cLoreSet = cLore.literal("set")
				.commandDescription(description("Set a line of lore."))
				.required("index", integerParser(0))
				.optional("text", greedyStringParser(), constant(""))
				.handler(c -> modify(
						sender(c), b -> {
							final @Nullable List<Component> lore = b.lore();
							final int line = c.get("index");
							if (lore == null || lore.size() <= line) {
								sender(c).sendMessage(this.langConfig.c(NodePath.path("error", "out-of-bounds")));
								return null;
							}

							lore.set(line, this.userService.formatWithUserFormat(c.get("text"), sender(c)));
							return b.lore(lore);
						}
				));

		final var cLoreRemove = cLore.literal("remove")
				.commandDescription(description("Remove a line of lore."))
				.required("index", integerParser(0))
				.handler(c -> modify(
						sender(c), b -> {
							final @Nullable List<Component> lore = b.lore();
							final int line = c.get("index");
							if (lore == null || lore.size() <= line) {
								sender(c).sendMessage(this.langConfig.c(NodePath.path("error", "out-of-bounds")));
								return null;
							}

							lore.remove(line);
							return b.lore(lore);
						}
				));

		final var cLoreClear = cLore.literal("clear")
				.commandDescription(description("Clear the lore."))
				.handler(c -> modify(sender(c), b -> b.lore(null)));

		commandManager
				.command(cLoreAdd)
				.command(cLoreSet)
				.command(cLoreRemove)
				.command(cLoreClear);
	}

}
