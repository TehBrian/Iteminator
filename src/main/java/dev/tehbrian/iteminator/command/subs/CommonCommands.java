package dev.tehbrian.iteminator.command.subs;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.DoubleArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.parsers.EnchantmentArgument;
import cloud.commandframework.bukkit.parsers.MaterialArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.iteminator.Permission;
import dev.tehbrian.iteminator.config.LangConfig;
import dev.tehbrian.iteminator.user.UserService;
import dev.tehbrian.iteminator.util.HeldItemModifier;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

  /**
   * @param commandManager the manager to register the commands to
   * @param parent         the command to register the subcommands under
   */
  public void registerCommon(
      final PaperCommandManager<CommandSender> commandManager,
      final Command.Builder<CommandSender> parent
  ) {
    final var cAmount = parent.literal("amount")
        .meta(CommandMeta.DESCRIPTION, "Set the amount.")
        .permission(Permission.AMOUNT)
        .argument(IntegerArgument.<CommandSender>builder("amount").withMin(0).withMax(127))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(sender, b -> b.amount(c.get("amount")));
        });

    final var cCustomModelData = parent.literal("custom-model-data")
        .meta(CommandMeta.DESCRIPTION, "Set the custom model data. Pass nothing to reset.")
        .permission(Permission.CUSTOM_MODEL_DATA)
        .argument(IntegerArgument.optional("data"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(sender, b -> b.customModelData(c.<Integer>getOptional("data").orElse(null)));
        });

    final var cMaterial = parent.literal("material")
        .meta(CommandMeta.DESCRIPTION, "Set the material.")
        .permission(Permission.MATERIAL)
        .argument(MaterialArgument.of("material"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(sender, b -> b.material(c.get("material")));
        });

    final var cName = parent.literal("name")
        .meta(CommandMeta.DESCRIPTION, "Name-related commands.")
        .permission(Permission.NAME);

    final var cNameSet = cName.literal("set")
        .meta(CommandMeta.DESCRIPTION, "Set the name.")
        .argument(StringArgument.optional("text", StringArgument.StringMode.GREEDY))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(
              sender,
              b -> b.name(this.userService.formatWithUserFormat(c.getOptional("text"), sender))
          );
        });

    final var cNameReset = cName.literal("reset")
        .meta(CommandMeta.DESCRIPTION, "Reset the name to the item's default.")
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(sender, b -> b.name(null));
        });

    final var cUnbreakable = parent.literal("unbreakable")
        .meta(CommandMeta.DESCRIPTION, "Set the unbreakable flag.")
        .permission(Permission.UNBREAKABLE)
        .argument(BooleanArgument.of("boolean"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(sender, b -> b.unbreakable(c.get("boolean")));
        });

    commandManager
        .command(cAmount)
        .command(cCustomModelData)
        .command(cMaterial)
        .command(cNameSet)
        .command(cNameReset)
        .command(cUnbreakable);

    final var cAttribute = parent.literal("attribute")
        .meta(CommandMeta.DESCRIPTION, "Attribute-related commands.")
        .permission(Permission.ATTRIBUTE);

    final var cAttributeAdd = cAttribute.literal("add")
        .meta(CommandMeta.DESCRIPTION, "Add an attribute.")
        .argument(EnumArgument.of(Attribute.class, "attribute"))
        .argument(StringArgument.quoted("name"))
        .argument(DoubleArgument.of("amount"))
        .argument(EnumArgument.of(AttributeModifier.Operation.class, "operation"))
        .argument(EnumArgument.optional(EquipmentSlot.class, "equipment_slot"))
        .handler(c -> {
          final var sender = (Player) c.getSender();

          final var modifier = new AttributeModifier(
              new NamespacedKey("iteminator", c.get("name")),
              c.<Double>get("amount"),
              c.get("operation"),
              c.getOrDefault("equipment_slot", EquipmentSlotGroup.ANY)
          );

          HeldItemModifier.modify(sender, b -> b.addAttributeModifier(c.get("attribute"), modifier));
        });

    final var cAttributeRemove = cAttribute.literal("remove")
        .meta(CommandMeta.DESCRIPTION, "Remove an attribute.")
        .argument(EnumArgument.of(Attribute.class, "attribute"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(sender, b -> b.removeAttributeModifier(c.<Attribute>get("attribute")));
        });

    final var cAttributeClear = cAttribute.literal("clear")
        .meta(CommandMeta.DESCRIPTION, "Clear the attributes.")
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
        .permission(Permission.ENCHANTMENT);

    final var cEnchantmentAdd = cEnchantment.literal("add")
        .meta(CommandMeta.DESCRIPTION, "Add an enchantment.")
        .argument(EnchantmentArgument.of("type"))
        .argument(IntegerArgument.<CommandSender>builder("level").withMin(0).withMax(255))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(
              sender,
              b -> b.addEnchant(c.get("type"), c.<Integer>get("level"))
          );
        });

    final var cEnchantmentRemove = cEnchantment.literal("remove")
        .meta(CommandMeta.DESCRIPTION, "Remove an enchantment.")
        .argument(EnchantmentArgument.of("type"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(sender, b -> b.removeEnchant(c.get("type")));
        });

    final var cEnchantmentClear = cEnchantment.literal("clear")
        .meta(CommandMeta.DESCRIPTION, "Clear the enchantments.")
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
        .permission(Permission.FLAGS);

    final var cFlagsAdd = cFlags.literal("add")
        .meta(CommandMeta.DESCRIPTION, "Add a flag.")
        .argument(EnumArgument.of(ItemFlag.class, "flag"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(sender, b -> b.addFlag(c.<ItemFlag>get("flag")));
        });

    final var cFlagsRemove = cFlags.literal("remove")
        .meta(CommandMeta.DESCRIPTION, "Remove a flag.")
        .argument(EnumArgument.of(ItemFlag.class, "flag"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(sender, b -> b.removeFlag(c.<ItemFlag>get("flag")));
        });

    final var cFlagsClear = cFlags.literal("clear")
        .meta(CommandMeta.DESCRIPTION, "Clear the flags.")
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
        .permission(Permission.LORE);

    final var cLoreAdd = cLore.literal("add")
        .meta(CommandMeta.DESCRIPTION, "Add a line of lore.")
        .argument(StringArgument.optional("text", StringArgument.StringMode.GREEDY))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(sender, b -> {
            final List<Component> lore = Optional
                .ofNullable(b.lore())
                .map(ArrayList::new)
                .orElse(new ArrayList<>());

            lore.add(this.userService.formatWithUserFormat(c.getOptional("text"), sender));
            return b.lore(lore);
          });
        });

    final var cLoreSet = cLore.literal("set")
        .meta(CommandMeta.DESCRIPTION, "Set a line of lore.")
        .argument(IntegerArgument.<CommandSender>builder("index").withMin(0))
        .argument(StringArgument.optional("text", StringArgument.StringMode.GREEDY))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(sender, b -> {
            final @Nullable List<Component> lore = b.lore();

            final int line = c.get("index");
            if (lore == null || lore.size() <= line) {
              sender.sendMessage(this.langConfig.c(NodePath.path("error", "out-of-bounds")));
              return null;
            }

            lore.set(line, this.userService.formatWithUserFormat(c.getOptional("text"), sender));
            return b.lore(lore);
          });
        });

    final var cLoreRemove = cLore.literal("remove")
        .meta(CommandMeta.DESCRIPTION, "Remove a line of lore.")
        .argument(IntegerArgument.<CommandSender>builder("index").withMin(0))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          HeldItemModifier.modify(sender, b -> {
            final @Nullable List<Component> lore = b.lore();

            final int line = c.get("index");
            if (lore == null || lore.size() <= line) {
              sender.sendMessage(this.langConfig.c(NodePath.path("error", "out-of-bounds")));
              return null;
            }

            lore.remove(line);
            return b.lore(lore);
          });
        });

    final var cLoreClear = cLore.literal("clear")
        .meta(CommandMeta.DESCRIPTION, "Clear the lore.")
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
