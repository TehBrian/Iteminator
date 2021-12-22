package xyz.tehbrian.iteminator.command;

import broccolai.corn.paper.item.AbstractPaperItemBuilder;
import broccolai.corn.paper.item.PaperItemBuilder;
import broccolai.corn.paper.item.special.ArmorStandBuilder;
import broccolai.corn.paper.item.special.AxolotlBucketBuilder;
import broccolai.corn.paper.item.special.BannerBuilder;
import broccolai.corn.paper.item.special.BookBuilder;
import broccolai.corn.paper.item.special.EnchantmentStorageBuilder;
import broccolai.corn.paper.item.special.PotionBuilder;
import broccolai.corn.paper.item.special.TropicalFishBucketBuilder;
import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.parsers.EnchantmentArgument;
import cloud.commandframework.bukkit.parsers.MaterialArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.minecraft.extras.AudienceProvider;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import cloud.commandframework.paper.PaperCommandManager;
import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
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
import java.util.Map;
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
        final var cMain = commandManager.commandBuilder("iteminator")
                .meta(CommandMeta.DESCRIPTION, "The main command for Iteminator.")
                .handler(c -> c.getSender().sendMessage(this.langConfig.c(NodePath.path("main"))));

        final var help = new MinecraftHelp<>(
                "/iteminator help",
                AudienceProvider.nativeAudience(), commandManager
        );

        commandManager.command(cMain.literal("help")
                .argument(StringArgument.optional("query", StringArgument.StringMode.GREEDY))
                .handler(context -> help.queryCommands(context.getOrDefault("query", ""), context.getSender()))
        );

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

        final var cFormat = cMain.literal("format")
                .meta(CommandMeta.DESCRIPTION, "Toggle your ability to format text.")
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
                .permission(Permissions.AMOUNT)
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("amount").withMin(0).withMax(127))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.amount(c.get("amount")));
                });

        final var cMaterial = cMain.literal("material")
                .meta(CommandMeta.DESCRIPTION, "Set the material.")
                .permission(Permissions.MATERIAL)
                .senderType(Player.class)
                .argument(MaterialArgument.of("material"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.material(c.get("material")));
                });

        final var cName = cMain.literal("name")
                .meta(CommandMeta.DESCRIPTION, "Set the name. Pass nothing to reset.")
                .permission(Permissions.NAME)
                .senderType(Player.class)
                .argument(StringArgument.optional("text", StringArgument.StringMode.GREEDY))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> {
                        final @NonNull Optional<String> text = c.getOptional("text");
                        if (text.isPresent()) {
                            return b.name(this.formatWithUserFormat(text.get(), sender));
                        } else {
                            return b.name(null);
                        }
                    });
                });

        final var cUnbreakable = cMain.literal("unbreakable")
                .meta(CommandMeta.DESCRIPTION, "Set the unbreakable flag.")
                .permission(Permissions.UNBREAKABLE)
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
                .meta(CommandMeta.DESCRIPTION, "Lore-related commands.")
                .permission(Permissions.LORE);

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

                        lore.add(this.formatWithUserFormat(c.get("text"), sender));
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
                    this.modify(sender, b -> {
                        final @Nullable List<Component> lore = b.lore();

                        final int line = c.get("index");
                        if (lore == null || lore.size() <= line) {
                            sender.sendMessage(this.langConfig.c(NodePath.path("out-of-bounds")));
                            return null;
                        }

                        lore.set(line, this.formatWithUserFormat(c.get("text"), sender));
                        return b.lore(lore);
                    });
                });

        final var cLoreRemove = cLore.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove a line of lore.")
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("index").withMin(0))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> {
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
                    this.modify(sender, b -> b.lore(null));
                });

        final var cEnchantment = cMain.literal("enchantment")
                .meta(CommandMeta.DESCRIPTION, "Enchantment-related commands.")
                .permission(Permissions.ENCHANTMENT);

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
                .meta(CommandMeta.DESCRIPTION, "Flag-related commands.")
                .permission(Permissions.FLAGS);

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

        final var sArmorStand = cSpecial.literal("armor-stand")
                .meta(CommandMeta.DESCRIPTION, "Commands for Armor Stands.")
                .permission(Permissions.ARMOR_STAND);

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

        // FIXME: these commands are broken when trying to set the flags to their default state
        commandManager.command(sArmorStandShowArms)
                .command(sArmorStandInvisible)
                .command(sArmorStandMarker)
                .command(sArmorStandNoBasePlate)
                .command(sArmorStandSmall);

        final var sAxolotlBucket = cSpecial.literal("axolotl-bucket")
                .meta(CommandMeta.DESCRIPTION, "Commands for Axolotl Buckets.")
                .permission(Permissions.AXOLOTL_BUCKET);

        final var sAxolotlBucketVariant = sAxolotlBucket.literal("variant")
                .meta(CommandMeta.DESCRIPTION, "Set the variant.")
                .senderType(Player.class)
                .argument(EnumArgument.of(Axolotl.Variant.class, "variant"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.variant(c.get("variant")),
                            AxolotlBucketBuilder::of,
                            AxolotlBucketMeta.class
                    );
                });

        commandManager.command(sAxolotlBucketVariant);

        final var sBanner = cSpecial.literal("banner")
                .meta(CommandMeta.DESCRIPTION, "Commands for Banners.")
                .permission(Permissions.BANNER);

        final var sBannerAdd = sBanner.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add a pattern.")
                .senderType(Player.class)
                .argument(EnumArgument.of(DyeColor.class, "color"))
                .argument(EnumArgument.of(PatternType.class, "type"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.addPattern(new Pattern(c.get("color"), c.get("type"))),
                            BannerBuilder::of,
                            BannerMeta.class
                    );
                });

        final var sBannerSet = sBanner.literal("set")
                .meta(CommandMeta.DESCRIPTION, "Set a pattern.")
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("index").withMin(0))
                .argument(EnumArgument.of(DyeColor.class, "color"))
                .argument(EnumArgument.of(PatternType.class, "type"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final int index = c.<Integer>get("index");
                                if (b.patterns().size() <= index) {
                                    sender.sendMessage(this.langConfig.c(NodePath.path("out-of-bounds")));
                                    return null;
                                }

                                return b.setPattern(index, new Pattern(c.get("color"), c.get("type")));
                            },
                            BannerBuilder::of,
                            BannerMeta.class
                    );
                });

        final var sBannerRemove = sBanner.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove a pattern.")
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("index").withMin(0))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final int index = c.<Integer>get("index");
                                if (b.patterns().size() <= index) {
                                    sender.sendMessage(this.langConfig.c(NodePath.path("out-of-bounds")));
                                    return null;
                                }

                                return b.removePattern(index);
                            },
                            BannerBuilder::of,
                            BannerMeta.class
                    );
                });

        final var sBannerClear = sBanner.literal("clear")
                .meta(CommandMeta.DESCRIPTION, "Clear the patterns.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.patterns(List.of()),
                            BannerBuilder::of,
                            BannerMeta.class
                    );
                });

        commandManager.command(sBannerAdd)
                .command(sBannerSet)
                .command(sBannerRemove)
                .command(sBannerClear);

        final var sBook = cSpecial.literal("book")
                .meta(CommandMeta.DESCRIPTION, "Commands for Books.")
                .permission(Permissions.BOOK);

        final var sBookTitle = sBook.literal("title")
                .meta(CommandMeta.DESCRIPTION, "Set the title. Pass nothing to reset.")
                .senderType(Player.class)
                .argument(StringArgument.optional("text", StringArgument.StringMode.GREEDY))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final @NonNull Optional<String> text = c.getOptional("text");
                                if (text.isPresent()) {
                                    return b.name(this.formatWithUserFormat(text.get(), sender));
                                } else {
                                    return b.name(null);
                                }
                            },
                            BookBuilder::of,
                            BookMeta.class
                    );
                });

        final var sBookAuthor = sBook.literal("author")
                .meta(CommandMeta.DESCRIPTION, "Set the author. Pass nothing to reset.")
                .senderType(Player.class)
                .argument(StringArgument.optional("text", StringArgument.StringMode.GREEDY))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final @NonNull Optional<String> text = c.getOptional("text");
                                if (text.isPresent()) {
                                    return b.author(this.formatWithUserFormat(text.get(), sender));
                                } else {
                                    return b.author(null);
                                }
                            },
                            BookBuilder::of,
                            BookMeta.class
                    );
                });

        final var sBookGeneration = sBook.literal("generation")
                .meta(CommandMeta.DESCRIPTION, "Set the generation.")
                .senderType(Player.class)
                .argument(EnumArgument.of(BookMeta.Generation.class, "generation"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.generation(c.get("generation")),
                            BookBuilder::of,
                            BookMeta.class
                    );
                });

        final var sBookEditable = sBook.literal("editable")
                .meta(CommandMeta.DESCRIPTION, "Set whether the book is editable.")
                .senderType(Player.class)
                .argument(BooleanArgument.of("boolean"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                if (c.get("boolean")) {
                                    return b.material(Material.WRITABLE_BOOK);
                                } else {
                                    return b.material(Material.WRITTEN_BOOK);
                                }
                            },
                            BookBuilder::of,
                            BookMeta.class
                    );
                });

        commandManager.command(sBookTitle)
                .command(sBookAuthor)
                .command(sBookGeneration)
                .command(sBookEditable);

        final var sEnchantmentStorage = cSpecial.literal("enchantment-storage")
                .meta(CommandMeta.DESCRIPTION, "Commands for Enchantment Storages.")
                .permission(Permissions.ENCHANTMENT_STORAGE);

        final var sEnchantmentStorageAdd = sEnchantmentStorage.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add a stored enchantment.")
                .senderType(Player.class)
                .argument(EnchantmentArgument.of("type"))
                .argument(IntegerArgument.<CommandSender>newBuilder("level").withMin(0).withMax(255))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.addStoredEnchant(c.get("type"), c.<Integer>get("level")),
                            EnchantmentStorageBuilder::of,
                            EnchantmentStorageMeta.class
                    );
                });

        final var sEnchantmentStorageRemove = sEnchantmentStorage.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove a stored enchantment.")
                .senderType(Player.class)
                .argument(EnchantmentArgument.of("type"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.removeStoredEnchant(c.<Enchantment>get("type")),
                            EnchantmentStorageBuilder::of,
                            EnchantmentStorageMeta.class
                    );
                });

        final var sEnchantmentStorageClear = sEnchantmentStorage.literal("clear")
                .meta(CommandMeta.DESCRIPTION, "Clear the stored enchantments.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.storedEnchants(Map.of()),
                            EnchantmentStorageBuilder::of,
                            EnchantmentStorageMeta.class
                    );
                });

        commandManager.command(sEnchantmentStorageAdd)
                .command(sEnchantmentStorageRemove)
                .command(sEnchantmentStorageClear);

        final var sPotion = cSpecial.literal("potion")
                .meta(CommandMeta.DESCRIPTION, "Commands for Potions.")
                .permission(Permissions.POTION);

        final var sPotionEffect = sPotion.literal("effect")
                .meta(CommandMeta.DESCRIPTION, "Modify the custom effects.");

        final var sPotionEffectAdd = sPotionEffect.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add a custom effect.")
                .senderType(Player.class)
                .argument(PotionEffectTypeArgument.of("type"))
                .argument(IntegerArgument.<CommandSender>newBuilder("duration").withMin(0))
                .argument(IntegerArgument.<CommandSender>newBuilder("amplifier").withMin(0).withMax(64))
                .argument(BooleanArgument.optional("ambient", true))
                .argument(BooleanArgument.optional("particles", true))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final var potionEffect = new PotionEffect(
                                        c.get("type"),
                                        c.<Integer>get("duration"),
                                        c.<Integer>get("amplifier"),
                                        c.<Boolean>get("ambient"),
                                        c.<Boolean>get("particles"),
                                        c.<Boolean>get("particles") // in testing, icon and particles are equivalent
                                );

                                return b.addCustomEffect(potionEffect, true);
                            },
                            PotionBuilder::of,
                            PotionMeta.class
                    );
                });

        final var sPotionEffectRemove = sPotionEffect.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove a custom effect.")
                .senderType(Player.class)
                .argument(PotionEffectTypeArgument.of("type"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.removeCustomEffect(c.<PotionEffectType>get("type")),
                            PotionBuilder::of,
                            PotionMeta.class
                    );
                });

        final var sPotionEffectClear = sPotionEffect.literal("clear")
                .meta(CommandMeta.DESCRIPTION, "Clear the custom effects.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.customEffects(null),
                            PotionBuilder::of,
                            PotionMeta.class
                    );
                });

        final var sPotionColor = sPotion.literal("color");

        final var sPotionColorSet = sPotionColor
                .meta(CommandMeta.DESCRIPTION, "Set the potion's color.")
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("red").withMin(0).withMax(255))
                .argument(IntegerArgument.<CommandSender>newBuilder("blue").withMin(0).withMax(255))
                .argument(IntegerArgument.<CommandSender>newBuilder("green").withMin(0).withMax(255))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.color(Color.fromRGB(
                                    c.<Integer>get("red"),
                                    c.<Integer>get("green"),
                                    c.<Integer>get("blue")
                            )),
                            PotionBuilder::of,
                            PotionMeta.class
                    );
                });

        final var sPotionColorReset = sPotionColor
                .meta(CommandMeta.DESCRIPTION, "Reset the potion's color.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.color(null),
                            PotionBuilder::of,
                            PotionMeta.class
                    );
                });

        final var sPotionType = sPotion.literal("type")
                .meta(CommandMeta.DESCRIPTION, "Set the potion type.")
                .senderType(Player.class)
                .argument(EnumArgument.of(PotionType.class, "type"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.basePotionData(new PotionData(c.get("type"))),
                            PotionBuilder::of,
                            PotionMeta.class
                    );
                });

        commandManager.command(sPotionEffectAdd)
                .command(sPotionEffectRemove)
                .command(sPotionEffectClear)
                .command(sPotionColorSet)
                .command(sPotionColorReset)
                .command(sPotionType);

        final var sTropicalFishBucket = cSpecial.literal("tropical-fish-bucket")
                .meta(CommandMeta.DESCRIPTION, "Commands for Tropical Fish Buckets.")
                .permission(Permissions.TROPICAL_FISH_BUCKET);

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
    }

    private @NonNull Component formatWithUserFormat(final @NonNull String string, final @NonNull Player player) {
        final @NonNull User user = this.userService.getUser(player.getUniqueId());

        if (player.hasPermission(Permissions.FORMAT) && user.formatEnabled()) {
            if (user.formattingType() == User.FormattingType.LEGACY && player.hasPermission(Permissions.LEGACY)) {
                return FormatUtil.legacy(string);
            } else if (user.formattingType() == User.FormattingType.MINI_MESSAGE && player.hasPermission(Permissions.MINI_MESSAGE)) {
                return FormatUtil.miniMessage(string);
            }
        }

        return FormatUtil.plain(string);
    }

    private @NonNull Component generateWrongTypeMessage(final List<Material> requiredTypes) {
        return this.langConfig.c(
                NodePath.path("wrong-type"),
                TemplateResolver.templates(Template.template(
                        "type", Component.join(
                                JoinConfiguration.separators(Component.text(", "), Component.text(", or ")),
                                requiredTypes.stream().map(Component::translatable).toList()
                        )
                ))
        );
    }

    /**
     * I am most certainly going to the deepest, darkest level of programmer hell
     * for this terribleness. At least I don't have to re-type the same thing
     * everywhere! \_o¬o_/
     *
     * @param player         the player to target
     * @param operator       the operator to apply to the item in the main hand
     * @param builderCreator a function that creates the builder
     * @param metaType       the meta type to get the required types from
     * @param <T>            the builder type
     */
    private <T extends AbstractPaperItemBuilder<?, ?>> void modifySpecial(
            final @NonNull Player player, final @NonNull Function<@NonNull T, @Nullable T> operator,
            final Function<ItemStack, T> builderCreator, final Class<? extends ItemMeta> metaType
    ) {
        this.modifyItem(player, i -> {
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
        this.modifyItem(player, i -> {
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
