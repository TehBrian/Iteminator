package xyz.tehbrian.iteminator.command;

import broccolai.corn.paper.item.AbstractPaperItemBuilder;
import broccolai.corn.paper.item.PaperItemBuilder;
import broccolai.corn.paper.item.special.ArmorStandBuilder;
import broccolai.corn.paper.item.special.AxolotlBucketBuilder;
import broccolai.corn.paper.item.special.TropicalFishBucketBuilder;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.parsers.EnchantmentArgument;
import cloud.commandframework.bukkit.parsers.MaterialArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.iteminator.Iteminator;
import xyz.tehbrian.iteminator.config.LangConfig;
import xyz.tehbrian.iteminator.user.User;
import xyz.tehbrian.iteminator.user.UserService;
import xyz.tehbrian.iteminator.util.FormatUtil;
import xyz.tehbrian.iteminator.util.ItemMetaToRequiredTypes;
import xyz.tehbrian.iteminator.util.Permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class MainCommand extends PaperCloudCommand<CommandSender> {

    private final Iteminator iteminator;
    private final UserService userService;
    private final LangConfig langConfig;

    @Inject
    public MainCommand(
            final @NonNull Iteminator iteminator,
            final @NonNull UserService userService,
            final @NonNull LangConfig langConfig
    ) {
        this.iteminator = iteminator;
        this.userService = userService;
        this.langConfig = langConfig;
    }

    @Override
    public void register(final @NonNull PaperCommandManager<CommandSender> commandManager) {
        final var cMain = commandManager.commandBuilder("iteminator", ArgumentDescription.of("The main command for Iteminator."))
                .handler(c -> c.getSender().sendMessage(this.langConfig.c(NodePath.path("main"))));

        final var cReload = cMain.literal("reload")
                .meta(CommandMeta.DESCRIPTION, "Reload the plugin's config.")
                .permission(Permissions.RELOAD)
                .handler(c -> {
                    if (this.iteminator.loadConfiguration()) {
                        c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "success")));
                    } else {
                        c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "fail")));
                    }
                });

        final var cFormat = cMain.literal("format", ArgumentDescription.of("Toggle your ability to format text."))
                .permission(Permissions.FORMAT)
                .senderType(Player.class)
                .handler(c -> {
                    final Player sender = (Player) c.getSender();
                    if (this.userService.getUser(sender).toggleFormatEnabled()) {
                        sender.sendMessage(this.langConfig.c(NodePath.path("format", "enable")));
                    } else {
                        sender.sendMessage(this.langConfig.c(NodePath.path("format", "disable")));
                    }
                });

        final var cFormatFormattingType = cFormat
                .argument(EnumArgument.of(User.FormattingType.class, "formatting_type"))
                .handler(c -> {
                    final @NonNull Player player = (Player) c.getSender();
                    final User.@NonNull FormattingType formattingType = c.get("formatting_type");

                    this.userService.getUser(player).formattingType(formattingType);
                    player.sendMessage(this.langConfig.c(
                            NodePath.path("format", "set"),
                            TemplateResolver.templates(Template.template("formatting_type", formattingType.toString()))
                    ));
                });

        commandManager.command(cMain)
                .command(cReload)
                .command(cFormat)
                .command(cFormatFormattingType);

        final var cAmount = cMain.literal("amount")
                .meta(CommandMeta.DESCRIPTION, "Set the amount.")
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("amount").withMin(0).withMax(127))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.amount(c.get("amount")));
                });

        final var cMaterial = cMain.literal("material")
                .meta(CommandMeta.DESCRIPTION, "Set the material.")
                .senderType(Player.class)
                .argument(MaterialArgument.of("material"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.material(c.get("material")));
                });

        final var cName = cMain.literal("name")
                .meta(CommandMeta.DESCRIPTION, "Set the name.")
                .senderType(Player.class)
                .argument(StringArgument.greedy("text"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.name(this.translateWithUserFormat(c.get("text"), sender)));
                });

        final var cUnbreakable = cMain.literal("unbreakable")
                .meta(CommandMeta.DESCRIPTION, "Set the unbreakable flag.")
                .senderType(Player.class)
                .argument(BooleanArgument.of("boolean"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.unbreakable(c.get("boolean")));
                });

        commandManager.command(cAmount)
                .command(cMaterial)
                .command(cName)
                .command(cUnbreakable);

        final var cLore = cMain.literal("lore")
                .meta(CommandMeta.DESCRIPTION, "Lore-related commands.");

        final var cLoreAdd = cLore.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add a line of lore.")
                .senderType(Player.class)
                .argument(StringArgument.greedy("text"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> {
                        final @NonNull List<Component> lore = Optional
                                .ofNullable(b.lore())
                                .map(ArrayList::new)
                                .orElse(new ArrayList<>());

                        lore.add(this.translateWithUserFormat(c.get("text"), sender));
                        return b.lore(lore);
                    });
                });

        final var cLoreSet = cLore.literal("set")
                .meta(CommandMeta.DESCRIPTION, "Set a specific line of lore.")
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("line").withMin(0))
                .argument(StringArgument.greedy("text"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> {
                        final @Nullable List<Component> lore = b.lore();

                        final int line = c.get("line");
                        if (lore == null || lore.size() <= line) {
                            sender.sendMessage(this.langConfig.c(NodePath.path("wrong_line")));
                            return null;
                        }

                        lore.set(line, this.translateWithUserFormat(c.get("text"), sender));
                        return b.lore(lore);
                    });
                });

        final var cLoreRemove = cLore.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove a specific line of lore.")
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("line").withMin(0))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> {
                        final @Nullable List<Component> lore = b.lore();

                        final int line = c.get("line");
                        if (lore == null || lore.size() <= line) {
                            sender.sendMessage(this.langConfig.c(NodePath.path("wrong_line")));
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
                    this.modify(sender, b -> b.lore((List<Component>) null)); // TODO: replace with null once corn pr gets merged
                });

        final var cEnchantment = cMain.literal("enchantment")
                .meta(CommandMeta.DESCRIPTION, "Enchantment-related commands.");

        final var cEnchantmentAdd = cEnchantment.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add an enchantment.")
                .senderType(Player.class)
                .argument(EnchantmentArgument.of("type"))
                .argument(IntegerArgument.<CommandSender>newBuilder("level").withMin(0).withMax(255))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.addEnchant(c.get("type"), c.<Integer>get("level")));
                });

        final var cEnchantmentRemove = cEnchantment.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove an enchantment.")
                .senderType(Player.class)
                .argument(EnchantmentArgument.of("type"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.removeEnchant(c.<Enchantment>get("type")));
                });

        final var cEnchantmentClear = cEnchantment.literal("clear")
                .meta(CommandMeta.DESCRIPTION, "Clear the enchantments.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.enchants(null));
                });

        final var cFlags = cMain.literal("flags")
                .meta(CommandMeta.DESCRIPTION, "Flag-related commands.");

        final var cFlagsAdd = cFlags.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add a flag.")
                .senderType(Player.class)
                .argument(EnumArgument.of(ItemFlag.class, "flag"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.addFlag(c.<ItemFlag>get("flag")));
                });

        final var cFlagsRemove = cFlags.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove a flag.")
                .senderType(Player.class)
                .argument(EnumArgument.of(ItemFlag.class, "flag"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.removeFlag(c.<ItemFlag>get("flag")));
                });

        final var cFlagsClear = cFlags.literal("clear")
                .meta(CommandMeta.DESCRIPTION, "Clear the flags.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.flags(null));
                });

        commandManager
                .command(cLoreAdd)
                .command(cLoreSet)
                .command(cLoreRemove)
                .command(cLoreClear)
                .command(cEnchantmentAdd)
                .command(cEnchantmentRemove)
                .command(cEnchantmentClear)
                .command(cFlagsAdd)
                .command(cFlagsRemove)
                .command(cFlagsClear);

        final var cSpecial = cMain.literal("special")
                .meta(CommandMeta.DESCRIPTION, "Commands special to a specific item type.");

        final var sTropicalFishBucket = cSpecial.literal("tropical-fish-bucket")
                .meta(CommandMeta.DESCRIPTION, "Commands for Tropical Fish Buckets.");

        final var sTropicalFishBucketPattern = sTropicalFishBucket.literal("pattern")
                .meta(CommandMeta.DESCRIPTION, "Set the pattern.")
                .senderType(Player.class)
                .argument(EnumArgument.of(TropicalFish.Pattern.class, "pattern"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.pattern(c.get("pattern")),
                            TropicalFishBucketBuilder::of,
                            TropicalFishBucketMeta.class
                    );
                });

        final var sTropicalFishBucketBodyColor = sTropicalFishBucket.literal("body-color")
                .meta(CommandMeta.DESCRIPTION, "Set the body color.")
                .senderType(Player.class)
                .argument(EnumArgument.of(DyeColor.class, "body_color"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.bodyColor(c.get("body_color")),
                            TropicalFishBucketBuilder::of,
                            TropicalFishBucketMeta.class
                    );
                });

        final var sTropicalFishBucketPatternColor = sTropicalFishBucket.literal("pattern-color")
                .meta(CommandMeta.DESCRIPTION, "Set the pattern color.")
                .senderType(Player.class)
                .argument(EnumArgument.of(DyeColor.class, "pattern_color"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.patternColor(c.get("pattern_color")),
                            TropicalFishBucketBuilder::of,
                            TropicalFishBucketMeta.class
                    );
                });

        commandManager.command(sTropicalFishBucketPattern)
                .command(sTropicalFishBucketBodyColor)
                .command(sTropicalFishBucketPatternColor);

        final var sArmorStand = cSpecial.literal("armor-stand")
                .meta(CommandMeta.DESCRIPTION, "Commands for Armor Stands.");

        final var sArmorStandShowArms = sArmorStand.literal("show-arms")
                .meta(CommandMeta.DESCRIPTION, "Set the show arms flag.")
                .senderType(Player.class)
                .argument(BooleanArgument.of("boolean"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.showArms(c.get("boolean")),
                            ArmorStandBuilder::of,
                            ArmorStandMeta.class
                    );
                });

        final var sArmorStandInvisible = sArmorStand.literal("invisible")
                .meta(CommandMeta.DESCRIPTION, "Set the invisible flag.")
                .senderType(Player.class)
                .argument(BooleanArgument.of("boolean"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.invisible(c.get("boolean")),
                            ArmorStandBuilder::of,
                            ArmorStandMeta.class
                    );
                });

        final var sArmorStandMarker = sArmorStand.literal("marker")
                .meta(CommandMeta.DESCRIPTION, "Set the marker flag.")
                .senderType(Player.class)
                .argument(BooleanArgument.of("boolean"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.marker(c.get("boolean")),
                            ArmorStandBuilder::of,
                            ArmorStandMeta.class
                    );
                });

        final var sArmorStandNoBasePlate = sArmorStand.literal("no-base-plate")
                .meta(CommandMeta.DESCRIPTION, "Set the no base plate flag.")
                .senderType(Player.class)
                .argument(BooleanArgument.of("boolean"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.noBasePlate((c.get("boolean"))),
                            ArmorStandBuilder::of,
                            ArmorStandMeta.class
                    );
                });

        final var sArmorStandSmall = sArmorStand.literal("small")
                .meta(CommandMeta.DESCRIPTION, "Set the small flag.")
                .senderType(Player.class)
                .argument(BooleanArgument.of("boolean"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.small(c.get("boolean")),
                            ArmorStandBuilder::of,
                            ArmorStandMeta.class
                    );
                });

        // TODO: these commands are borken for some reason
        commandManager.command(sArmorStandShowArms)
                .command(sArmorStandInvisible)
                .command(sArmorStandMarker)
                .command(sArmorStandNoBasePlate)
                .command(sArmorStandSmall);

        final var sAxolotlBucket = cSpecial.literal("axolotl-bucket")
                .meta(CommandMeta.DESCRIPTION, "Commands for Axolotl Buckets.");

        final var sAxolotlBucketVariant = sAxolotlBucket.literal("variant")
                .meta(CommandMeta.DESCRIPTION, "Set the variant.")
                .senderType(Player.class)
                .argument(EnumArgument.of(Axolotl.Variant.class, "variant"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.variant(c.<Axolotl.Variant>get("variant")),
                            AxolotlBucketBuilder::of,
                            AxolotlBucketMeta.class
                    );
                });

        commandManager.command(sAxolotlBucketVariant);
    }

    private @NonNull Component translateWithUserFormat(final @NonNull String string, final @NonNull Player player) {
        final @NonNull User user = this.userService.getUser(player.getUniqueId());
        if (!user.formatEnabled()) {
            return FormatUtil.plain(string);
        }

        return switch (user.formattingType()) {
            case LEGACY -> FormatUtil.legacy(string);
            case MINI_MESSAGE -> FormatUtil.miniMessage(string);
        };
    }

    private @NonNull Component generateWrongTypeMessage(final List<Material> requiredTypes) {
        return this.langConfig.c(
                NodePath.path("wrong_type"),
                TemplateResolver.templates(Template.template(
                        "type", String.join(", ", requiredTypes.stream().map(Enum::toString).toList())
                ))
        );
    }

    /**
     * I am most certainly going to the deepest, darkest level of programmer hell
     * for this terribleness. At least I don't have to re-type the same thing
     * everywhere! \_o¬o_/
     *
     * @param player   the player to target
     * @param operator the operator to apply to the item in the main hand
     * @param metaType the meta type to get the required types from
     * @param <T>      the builder type
     */
    private <T extends AbstractPaperItemBuilder<?, ?>> void modifySpecial(
            final @NonNull Player player, final @NonNull Function<@NonNull T, @Nullable T> operator,
            final Function<ItemStack, T> builderCreator, final Class<? extends ItemMeta> metaType
    ) {
        modifyItem(player, i -> {
            final @NonNull T builder;
            try {
                builder = builderCreator.apply(i);
            } catch (final IllegalArgumentException e) {
                // TODO: handle null better
                final @NonNull List<Material> requiredTypes =
                        Objects.requireNonNull(ItemMetaToRequiredTypes.get(metaType));
                player.sendMessage(this.generateWrongTypeMessage(requiredTypes));
                return null;
            }
            final @Nullable T modifiedBuilder = operator.apply(builder);
            if (modifiedBuilder == null) {
                return null;
            }
            return modifiedBuilder.build();
        });
    }

    /**
     * Applies the given operator to the item in the player's main hand.
     * If the operator returns null, nothing will happen to the item.
     *
     * @param player   the player to target
     * @param operator the operator to apply to the item in the main hand
     */
    private void modify(
            final @NonNull Player player, final @NonNull Function<@NonNull PaperItemBuilder, @Nullable PaperItemBuilder> operator
    ) {
        modifyItem(player, i -> {
            final @Nullable PaperItemBuilder modifiedBuilder = operator.apply(PaperItemBuilder.of(i));
            if (modifiedBuilder == null) {
                return null;
            }
            return modifiedBuilder.build();
        });
    }

    /**
     * Applies the given operator to the item in the player's main hand.
     * If the operator returns null, nothing will happen to the item.
     *
     * @param player   the player to target
     * @param operator the operator to apply to the item in the main hand
     */
    private void modifyItem(
            final @NonNull Player player, final @NonNull Function<@NonNull ItemStack, @Nullable ItemStack> operator
    ) {
        final @NonNull PlayerInventory inventory = player.getInventory();
        final @NonNull ItemStack item = inventory.getItemInMainHand();
        if (item.getItemMeta() == null) { // it's air and therefore cannot be modified
            return;
        }
        final var modifiedItem = operator.apply(item);
        if (modifiedItem == null) {
            return;
        }
        inventory.setItemInMainHand(modifiedItem);
    }

}
