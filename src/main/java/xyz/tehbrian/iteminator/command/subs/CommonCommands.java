package xyz.tehbrian.iteminator.command.subs;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.DoubleArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.parsers.MaterialArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.iteminator.command.LowerBooleanArgument;
import xyz.tehbrian.iteminator.command.ModernEnchantment;
import xyz.tehbrian.iteminator.config.LangConfig;
import xyz.tehbrian.iteminator.user.UserService;
import xyz.tehbrian.iteminator.util.HeldItemModifier;
import xyz.tehbrian.iteminator.util.Permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class CommonCommands {

    private final UserService userService;
    private final LangConfig langConfig;

    /**
     * @param userService injected
     * @param langConfig  injected
     */
    @Inject
    public CommonCommands(
            final @NonNull UserService userService,
            final @NonNull LangConfig langConfig
    ) {
        this.userService = userService;
        this.langConfig = langConfig;
    }

    /**
     * @param commandManager the manager to register the commands to
     * @param parent         the command to register the subcommands under
     */
    public void registerCommon(
            final @NonNull PaperCommandManager<CommandSender> commandManager,
            final Command.@NonNull Builder<CommandSender> parent
    ) {
        final var cAmount = parent.literal("amount")
                .meta(CommandMeta.DESCRIPTION, "Set the amount.")
                .permission(Permissions.AMOUNT)
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("amount").withMin(0).withMax(127))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.amount(c.get("amount")));
                });

        final var cCustomModelData = parent.literal("custom-model-data")
                .meta(CommandMeta.DESCRIPTION, "Set the custom model data. Pass nothing to reset.")
                .permission(Permissions.CUSTOM_MODEL_DATA)
                .senderType(Player.class)
                .argument(IntegerArgument.optional("data"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.customModelData(c.<Integer>getOptional("data").orElse(null)));
                });

        final var cMaterial = parent.literal("material")
                .meta(CommandMeta.DESCRIPTION, "Set the material.")
                .permission(Permissions.MATERIAL)
                .senderType(Player.class)
                .argument(MaterialArgument.of("material"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.material(c.get("material")));
                });

        final var cName = parent.literal("name")
                .meta(CommandMeta.DESCRIPTION, "Set the name. Pass nothing to reset.")
                .permission(Permissions.NAME)
                .senderType(Player.class)
                .argument(StringArgument.optional("text", StringArgument.StringMode.GREEDY))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> {
                        final @NonNull Optional<String> text = c.getOptional("text");
                        return text.map(s -> b.name(this.userService.formatWithUserFormat(s, sender)))
                                .orElseGet(() -> b.name(null));
                    });
                });

        final var cUnbreakable = parent.literal("unbreakable")
                .meta(CommandMeta.DESCRIPTION, "Set the unbreakable flag.")
                .permission(Permissions.UNBREAKABLE)
                .senderType(Player.class)
                .argument(LowerBooleanArgument.of("boolean"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.unbreakable(c.get("boolean")));
                });

        commandManager.command(cAmount)
                .command(cCustomModelData)
                .command(cMaterial)
                .command(cName)
                .command(cUnbreakable);

        final var cAttribute = parent.literal("attribute")
                .meta(CommandMeta.DESCRIPTION, "Attribute-related commands.")
                .permission(Permissions.ATTRIBUTE);

        final var cAttributeAdd = cAttribute.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add an attribute.")
                .senderType(Player.class)
                .argument(EnumArgument.of(Attribute.class, "attribute"))
                .argument(StringArgument.quoted("name"))
                .argument(DoubleArgument.of("amount"))
                .argument(EnumArgument.of(AttributeModifier.Operation.class, "operation"))
                .argument(EnumArgument.optional(EquipmentSlot.class, "equipment_slot"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();

                    final var modifier = new AttributeModifier(
                            UUID.randomUUID(), // let's hope for no collision! :D
                            c.get("name"),
                            c.<Double>get("amount"),
                            c.get("operation"),
                            c.getOrDefault("equipment_slot", null)
                    );

                    HeldItemModifier.modify(sender, b -> b.addAttributeModifier(c.get("attribute"), modifier));
                });

        final var cAttributeRemove = cAttribute.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove an attribute.")
                .senderType(Player.class)
                .argument(EnumArgument.of(Attribute.class, "attribute"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.removeAttributeModifier(c.<Attribute>get("attribute")));
                });

        final var cAttributeClear = cAttribute.literal("clear")
                .meta(CommandMeta.DESCRIPTION, "Clear the attributes.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.attributeModifiers(null));
                });

        commandManager
                .command(cAttributeAdd)
                .command(cAttributeRemove)
                .command(cAttributeClear);

        final var cEnchantment = parent.literal("enchantment")
                .meta(CommandMeta.DESCRIPTION, "Enchantment-related commands.")
                .permission(Permissions.ENCHANTMENT);

        final var cEnchantmentAdd = cEnchantment.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add an enchantment.")
                .senderType(Player.class)
                .argument(EnumArgument.of(ModernEnchantment.class, "type"))
                .argument(IntegerArgument.<CommandSender>newBuilder("level").withMin(0).withMax(255))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.addEnchant(c.<ModernEnchantment>get("type").unwrap(), c.<Integer>get("level")));
                });

        final var cEnchantmentRemove = cEnchantment.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove an enchantment.")
                .senderType(Player.class)
                .argument(EnumArgument.of(ModernEnchantment.class, "type"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.removeEnchant(c.<ModernEnchantment>get("type").unwrap()));
                });

        final var cEnchantmentClear = cEnchantment.literal("clear")
                .meta(CommandMeta.DESCRIPTION, "Clear the enchantments.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.enchants(null));
                });

        commandManager
                .command(cEnchantmentAdd)
                .command(cEnchantmentRemove)
                .command(cEnchantmentClear);

        final var cFlags = parent.literal("flags")
                .meta(CommandMeta.DESCRIPTION, "Flag-related commands.")
                .permission(Permissions.FLAGS);

        final var cFlagsAdd = cFlags.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add a flag.")
                .senderType(Player.class)
                .argument(EnumArgument.of(ItemFlag.class, "flag"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.addFlag(c.<ItemFlag>get("flag")));
                });

        final var cFlagsRemove = cFlags.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove a flag.")
                .senderType(Player.class)
                .argument(EnumArgument.of(ItemFlag.class, "flag"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.removeFlag(c.<ItemFlag>get("flag")));
                });

        final var cFlagsClear = cFlags.literal("clear")
                .meta(CommandMeta.DESCRIPTION, "Clear the flags.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.flags(null));
                });

        commandManager
                .command(cFlagsAdd)
                .command(cFlagsRemove)
                .command(cFlagsClear);

        final var cLore = parent.literal("lore")
                .meta(CommandMeta.DESCRIPTION, "Lore-related commands.")
                .permission(Permissions.LORE);

        final var cLoreAdd = cLore.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add a line of lore.")
                .senderType(Player.class)
                .argument(StringArgument.greedy("text"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> {
                        final @NonNull List<Component> lore = Optional
                                .ofNullable(b.lore())
                                .map(ArrayList::new)
                                .orElse(new ArrayList<>());

                        lore.add(this.userService.formatWithUserFormat(c.get("text"), sender));
                        return b.lore(lore);
                    });
                });

        final var cLoreSet = cLore.literal("set")
                .meta(CommandMeta.DESCRIPTION, "Set a line of lore.")
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("index").withMin(0))
                .argument(StringArgument.greedy("text"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> {
                        final @Nullable List<Component> lore = b.lore();

                        final int line = c.get("index");
                        if (lore == null || lore.size() <= line) {
                            sender.sendMessage(this.langConfig.c(NodePath.path("out-of-bounds")));
                            return null;
                        }

                        lore.set(line, this.userService.formatWithUserFormat(c.get("text"), sender));
                        return b.lore(lore);
                    });
                });

        final var cLoreRemove = cLore.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove a line of lore.")
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("index").withMin(0))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> {
                        final @Nullable List<Component> lore = b.lore();

                        final int line = c.get("index");
                        if (lore == null || lore.size() <= line) {
                            sender.sendMessage(this.langConfig.c(NodePath.path("out-of-bounds")));
                            return null;
                        }

                        lore.remove(line);
                        return b.lore(lore);
                    });
                });

        final var cLoreClear = cLore.literal("clear")
                .meta(CommandMeta.DESCRIPTION, "Clear the lore.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modify(sender, b -> b.lore(null));
                });

        commandManager
                .command(cLoreAdd)
                .command(cLoreSet)
                .command(cLoreRemove)
                .command(cLoreClear);
    }

}
