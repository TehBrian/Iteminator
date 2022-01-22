package xyz.tehbrian.iteminator.command.subs;

import broccolai.corn.paper.item.AbstractPaperItemBuilder;
import broccolai.corn.paper.item.special.ArmorStandBuilder;
import broccolai.corn.paper.item.special.AxolotlBucketBuilder;
import broccolai.corn.paper.item.special.BannerBuilder;
import broccolai.corn.paper.item.special.BookBuilder;
import broccolai.corn.paper.item.special.DamageableBuilder;
import broccolai.corn.paper.item.special.EnchantmentStorageBuilder;
import broccolai.corn.paper.item.special.LeatherArmorBuilder;
import broccolai.corn.paper.item.special.MapBuilder;
import broccolai.corn.paper.item.special.PotionBuilder;
import broccolai.corn.paper.item.special.SuspiciousStewBuilder;
import broccolai.corn.paper.item.special.TropicalFishBucketBuilder;
import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import com.google.inject.Inject;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.iteminator.Iteminator;
import xyz.tehbrian.iteminator.command.LowerBooleanArgument;
import xyz.tehbrian.iteminator.command.ModernEnchantment;
import xyz.tehbrian.iteminator.command.ModernPotionEffectType;
import xyz.tehbrian.iteminator.command.ModernPotionType;
import xyz.tehbrian.iteminator.config.LangConfig;
import xyz.tehbrian.iteminator.user.UserService;
import xyz.tehbrian.iteminator.util.HeldItemModifier;
import xyz.tehbrian.iteminator.util.ItemMetaRequiredTypes;
import xyz.tehbrian.iteminator.util.Permissions;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class SpecialCommands {

    private final Iteminator iteminator;
    private final UserService userService;
    private final LangConfig langConfig;

    /**
     * @param iteminator  injected
     * @param userService injected
     * @param langConfig  injected
     */
    @Inject
    public SpecialCommands(
            final @NonNull Iteminator iteminator,
            final @NonNull UserService userService,
            final @NonNull LangConfig langConfig
    ) {
        this.iteminator = iteminator;
        this.userService = userService;
        this.langConfig = langConfig;
    }

    /**
     * @param commandManager the manager to register the commands to
     * @param parent         the command to register the subcommands under
     */
    public void registerSpecial(
            final @NonNull PaperCommandManager<CommandSender> commandManager,
            final Command.@NonNull Builder<CommandSender> parent
    ) {
        final var sArmorStand = parent.literal("armor-stand")
                .meta(CommandMeta.DESCRIPTION, "Commands for Armor Stands.")
                .permission(Permissions.ARMOR_STAND);

        final var sArmorStandShowArms = sArmorStand.literal("show-arms")
                .meta(CommandMeta.DESCRIPTION, "Set the show arms flag.")
                .senderType(Player.class)
                .argument(LowerBooleanArgument.of("boolean"))
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
                .argument(LowerBooleanArgument.of("boolean"))
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
                .argument(LowerBooleanArgument.of("boolean"))
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
                .argument(LowerBooleanArgument.of("boolean"))
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
                .argument(LowerBooleanArgument.of("boolean"))
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

        final var sAxolotlBucket = parent.literal("axolotl-bucket")
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

        final var sBanner = parent.literal("banner")
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

        final var sBook = parent.literal("book")
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
                                return text
                                        .map(s -> b.name(this.userService.formatWithUserFormat(s, sender)))
                                        .orElseGet(() -> b.name(null));
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
                                return text.map(s -> b.author(this.userService.formatWithUserFormat(s, sender))).orElseGet(() -> b.author(
                                        null));
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
                .argument(LowerBooleanArgument.of("boolean"))
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

        final var sDamageable = parent.literal("damage") // abnormal name because ends in "able"
                .meta(CommandMeta.DESCRIPTION, "Commands for Damageable items.")
                .permission(Permissions.DAMAGEABLE);

        final var sDamageableSet = sDamageable
                .meta(CommandMeta.DESCRIPTION, "Sets the damage.")
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("damage").asOptionalWithDefault(0).withMin(0))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.damage(c.get("damage")),
                            DamageableBuilder::of,
                            Damageable.class
                    );
                });

        commandManager.command(sDamageableSet);

        final var sEnchantmentStorage = parent.literal("enchantment-storage")
                .meta(CommandMeta.DESCRIPTION, "Commands for Enchantment Storages.")
                .permission(Permissions.ENCHANTMENT_STORAGE);

        final var sEnchantmentStorageAdd = sEnchantmentStorage.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add a stored enchantment.")
                .senderType(Player.class)
                .argument(EnumArgument.of(ModernEnchantment.class, "type"))
                .argument(IntegerArgument.<CommandSender>newBuilder("level").withMin(0).withMax(255))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.addStoredEnchant(c.<ModernEnchantment>get("type").unwrap(), c.<Integer>get("level")),
                            EnchantmentStorageBuilder::of,
                            EnchantmentStorageMeta.class
                    );
                });

        final var sEnchantmentStorageRemove = sEnchantmentStorage.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove a stored enchantment.")
                .senderType(Player.class)
                .argument(EnumArgument.of(ModernEnchantment.class, "type"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.removeStoredEnchant(c.<ModernEnchantment>get("type").unwrap()),
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

        final var sLeatherArmor = parent.literal("leather-armor")
                .meta(CommandMeta.DESCRIPTION, "Commands for Leather Armor.")
                .permission(Permissions.LEATHER_ARMOR);

        final var sLeatherArmorColor = sLeatherArmor.literal("color");

        final var sLeatherArmorSet = sLeatherArmorColor
                .meta(CommandMeta.DESCRIPTION, "Set the armor's color.")
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
                            LeatherArmorBuilder::of,
                            LeatherArmorMeta.class
                    );
                });

        final var sLeatherArmorReset = sLeatherArmorColor
                .meta(CommandMeta.DESCRIPTION, "Reset the armor's color.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.color(null),
                            LeatherArmorBuilder::of,
                            LeatherArmorMeta.class
                    );
                });

        commandManager.command(sLeatherArmorSet)
                .command(sLeatherArmorReset);

        final var sMap = parent.literal("map")
                .meta(CommandMeta.DESCRIPTION, "Commands for Maps.")
                .permission(Permissions.MAP);

        final var sMapScaling = sMap.literal("scaling")
                .meta(CommandMeta.DESCRIPTION, "Set whether the map is scaling.")
                .senderType(Player.class)
                .argument(LowerBooleanArgument.of("scaling"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.scaling(c.<Boolean>get("scaling")),
                            MapBuilder::of,
                            MapMeta.class
                    );
                });

        final var sMapLocationName = sMap.literal("location-name")
                .meta(CommandMeta.DESCRIPTION, "Set the map's location name.")
                .senderType(Player.class)
                .argument(StringArgument.optional("name"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.locationName(c.<String>getOptional("name").orElse(null)),
                            MapBuilder::of,
                            MapMeta.class
                    );
                });

        final var sMapColor = sMap.literal("color");

        final var sMapColorSet = sMapColor
                .meta(CommandMeta.DESCRIPTION, "Set the map's color.")
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
                            MapBuilder::of,
                            MapMeta.class
                    );
                });

        final var sMapColorReset = sMapColor
                .meta(CommandMeta.DESCRIPTION, "Reset the map's color.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.color(null),
                            MapBuilder::of,
                            MapMeta.class
                    );
                });

        final var sMapView = sMap.literal("view");

        final var sMapViewCenterX = sMapView.literal("center-x")
                .meta(CommandMeta.DESCRIPTION, "Set the map view's center x.")
                .senderType(Player.class)
                .argument(IntegerArgument.of("x"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final @Nullable MapView view = b.mapView();
                                if (view != null) {
                                    view.setCenterX(c.<Integer>get("x"));
                                    b.mapView(view);
                                }
                                return b;
                            },
                            MapBuilder::of,
                            MapMeta.class
                    );
                });

        final var sMapViewCenterZ = sMapView.literal("center-z")
                .meta(CommandMeta.DESCRIPTION, "Set the map view's center z.")
                .senderType(Player.class)
                .argument(IntegerArgument.of("z"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final @Nullable MapView view = b.mapView();
                                if (view != null) {
                                    view.setCenterZ(c.<Integer>get("z"));
                                    b.mapView(view);
                                }
                                return b;
                            },
                            MapBuilder::of,
                            MapMeta.class
                    );
                });

        final var sMapViewScale = sMapView.literal("scale")
                .meta(CommandMeta.DESCRIPTION, "Set the map view's scale.")
                .senderType(Player.class)
                .argument(EnumArgument.of(MapView.Scale.class, "scale"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final @Nullable MapView view = b.mapView();
                                if (view != null) {
                                    view.setScale(c.get("scale"));
                                    b.mapView(view);
                                }
                                return b;
                            },
                            MapBuilder::of,
                            MapMeta.class
                    );
                });

        final var sMapViewLocked = sMapView.literal("locked")
                .meta(CommandMeta.DESCRIPTION, "Set whether the map view is locked.")
                .senderType(Player.class)
                .argument(LowerBooleanArgument.of("boolean"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final @Nullable MapView view = b.mapView();
                                if (view != null) {
                                    view.setLocked(c.<Boolean>get("boolean"));
                                    b.mapView(view);
                                }
                                return b;
                            },
                            MapBuilder::of,
                            MapMeta.class
                    );
                });

        final var sMapViewTrackingPosition = sMapView.literal("tracking-position")
                .meta(CommandMeta.DESCRIPTION, "Set whether the map view shows a position cursor.")
                .senderType(Player.class)
                .argument(LowerBooleanArgument.of("boolean"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final @Nullable MapView view = b.mapView();
                                if (view != null) {
                                    view.setTrackingPosition(c.<Boolean>get("boolean"));
                                    b.mapView(view);
                                }
                                return b;
                            },
                            MapBuilder::of,
                            MapMeta.class
                    );
                });

        final var sMapViewUnlimitedTracking = sMapView.literal("unlimited-tracking")
                .meta(CommandMeta.DESCRIPTION, "Set whether the map view shows off-screen cursors.")
                .senderType(Player.class)
                .argument(LowerBooleanArgument.of("boolean"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final @Nullable MapView view = b.mapView();
                                if (view != null) {
                                    view.setUnlimitedTracking(c.<Boolean>get("boolean"));
                                    b.mapView(view);
                                }
                                return b;
                            },
                            MapBuilder::of,
                            MapMeta.class
                    );
                });

        commandManager.command(sMapScaling)
                .command(sMapLocationName)
                .command(sMapColorSet)
                .command(sMapColorReset)
                .command(sMapViewCenterX)
                .command(sMapViewCenterZ)
                .command(sMapViewScale)
                .command(sMapViewLocked)
                .command(sMapViewTrackingPosition)
                .command(sMapViewUnlimitedTracking);

        final var sPotion = parent.literal("potion")
                .meta(CommandMeta.DESCRIPTION, "Commands for Potions.")
                .permission(Permissions.POTION);

        final var sPotionEffect = sPotion.literal("effect")
                .meta(CommandMeta.DESCRIPTION, "Modify the custom effects.");

        final var sPotionEffectAdd = sPotionEffect.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add a custom effect.")
                .senderType(Player.class)
                .argument(EnumArgument.of(ModernPotionEffectType.class, "type"))
                .argument(IntegerArgument.<CommandSender>newBuilder("duration").withMin(0))
                .argument(IntegerArgument.<CommandSender>newBuilder("amplifier").withMin(0).withMax(64))
                .argument(LowerBooleanArgument.optional("ambient", true))
                .argument(LowerBooleanArgument.optional("particles", true))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final var potionEffect = new PotionEffect(
                                        c.<ModernPotionEffectType>get("type").unwrap(),
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
                .argument(EnumArgument.of(ModernPotionEffectType.class, "type"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.removeCustomEffect(c.<ModernPotionEffectType>get("type").unwrap()),
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
                .argument(EnumArgument.of(ModernPotionType.class, "type"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.basePotionData(new PotionData(c.<ModernPotionType>get("type").unwrap())),
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

        final var sSkull = parent.literal("skull")
                .meta(CommandMeta.DESCRIPTION, "Commands for Skulls.")
                .permission(Permissions.SKULL);

        final var sSkullName = sSkull.literal("name")
                .meta(CommandMeta.DESCRIPTION, "Set the owning player by name.")
                .senderType(Player.class)
                .argument(StringArgument.<CommandSender>newBuilder("name")
                        .withSuggestionsProvider((c, s) -> this.iteminator.getServer()
                                .getOnlinePlayers().stream().map(Player::getName).toList()))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    HeldItemModifier.modifyItemStack(
                            sender,
                            i -> {
                                if (i.getItemMeta() instanceof SkullMeta skullMeta) {
                                    // fuk th polic
                                    // i spent like 50 trillion hours fiddling with
                                    // get offline player and calling mojang's api, and
                                    // you know what actually worked? a deprecated method.
                                    //noinspection deprecation
                                    skullMeta.setOwner(c.get("name"));
                                    i.setItemMeta(skullMeta);
                                } else {
                                    this.sendWrongTypeMessage(sender, SkullMeta.class);
                                }
                                return i;
                            }
                    );
                });

        commandManager.command(sSkullName);

        final var sSuspiciousStew = parent.literal("suspicious-stew")
                .meta(CommandMeta.DESCRIPTION, "Commands for Suspicious Stews.")
                .permission(Permissions.SUSPICIOUS_STEW);

        final var sSuspiciousStewAdd = sSuspiciousStew.literal("add")
                .meta(CommandMeta.DESCRIPTION, "Add a custom effect.")
                .senderType(Player.class)
                .argument(EnumArgument.of(ModernPotionEffectType.class, "type"))
                .argument(IntegerArgument.<CommandSender>newBuilder("duration").withMin(0))
                .argument(IntegerArgument.<CommandSender>newBuilder("amplifier").withMin(0).withMax(64))
                .argument(LowerBooleanArgument.optional("ambient", true))
                .argument(LowerBooleanArgument.optional("particles", true))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> {
                                final var potionEffect = new PotionEffect(
                                        c.<ModernPotionEffectType>get("type").unwrap(),
                                        c.<Integer>get("duration"),
                                        c.<Integer>get("amplifier"),
                                        c.<Boolean>get("ambient"),
                                        c.<Boolean>get("particles"),
                                        c.<Boolean>get("particles") // in testing, icon and particles are equivalent
                                );

                                return b.addCustomEffect(potionEffect, true);
                            },
                            SuspiciousStewBuilder::of,
                            SuspiciousStewMeta.class
                    );
                });

        final var sSuspiciousStewRemove = sSuspiciousStew.literal("remove")
                .meta(CommandMeta.DESCRIPTION, "Remove a custom effect.")
                .senderType(Player.class)
                .argument(EnumArgument.of(ModernPotionEffectType.class, "type"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.removeCustomEffect(c.<ModernPotionEffectType>get("type").unwrap()),
                            SuspiciousStewBuilder::of,
                            SuspiciousStewMeta.class
                    );
                });

        final var sSuspiciousStewClear = sSuspiciousStew.literal("clear")
                .meta(CommandMeta.DESCRIPTION, "Clear the custom effects.")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modifySpecial(
                            sender,
                            b -> b.customEffects(null),
                            SuspiciousStewBuilder::of,
                            SuspiciousStewMeta.class
                    );
                });

        commandManager.command(sSuspiciousStewAdd)
                .command(sSuspiciousStewRemove)
                .command(sSuspiciousStewClear);

        final var sTropicalFishBucket = parent.literal("tropical-fish-bucket")
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

    private @NonNull Component generateWrongTypeMessage(final List<Material> requiredTypes) {
        final Component types = Component.join(
                JoinConfiguration.separators(Component.text(", "), Component.text(", or ")),
                requiredTypes.stream().map(Component::translatable).toList()
        );

        return this.langConfig.c(
                NodePath.path("wrong-type"),
                PlaceholderResolver.placeholders(Placeholder.component("type", types))
        );
    }

    private void sendWrongTypeMessage(final Audience audience, final Class<? extends ItemMeta> metaType) {
        // TODO: handle null better
        final @NonNull List<Material> requiredTypes =
                Objects.requireNonNull(ItemMetaRequiredTypes.get(metaType));
        audience.sendMessage(this.generateWrongTypeMessage(requiredTypes));
    }

    /**
     * @param player         the player to target
     * @param operator       the operator to apply to the item in the main hand
     * @param builderCreator a function that creates the builder
     * @param metaType       the meta type to get the required types from
     * @param <T>            the builder type
     */
    private <T extends AbstractPaperItemBuilder<T, ?>> void modifySpecial(
            final @NonNull Player player, final @NonNull Function<@NonNull T, @Nullable T> operator,
            final Function<ItemStack, T> builderCreator, final Class<? extends ItemMeta> metaType
    ) {
        try {
            HeldItemModifier.modifySpecial(player, operator, builderCreator);
        } catch (final IllegalArgumentException e) {
            this.sendWrongTypeMessage(player, metaType);
        }
    }

}
