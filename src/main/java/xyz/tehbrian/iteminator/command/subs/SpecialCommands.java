package xyz.tehbrian.iteminator.command.subs;

import broccolai.corn.paper.item.AbstractPaperItemBuilder;
import broccolai.corn.paper.item.special.ArmorStandBuilder;
import broccolai.corn.paper.item.special.AxolotlBucketBuilder;
import broccolai.corn.paper.item.special.BannerBuilder;
import broccolai.corn.paper.item.special.BookBuilder;
import broccolai.corn.paper.item.special.DamageableBuilder;
import broccolai.corn.paper.item.special.EnchantmentStorageBuilder;
import broccolai.corn.paper.item.special.FireworkBuilder;
import broccolai.corn.paper.item.special.LeatherArmorBuilder;
import broccolai.corn.paper.item.special.MapBuilder;
import broccolai.corn.paper.item.special.PotionBuilder;
import broccolai.corn.paper.item.special.RepairableBuilder;
import broccolai.corn.paper.item.special.SuspiciousStewBuilder;
import broccolai.corn.paper.item.special.TropicalFishBucketBuilder;
import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.BooleanArgument;
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
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
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
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.iteminator.Iteminator;
import xyz.tehbrian.iteminator.Permissions;
import xyz.tehbrian.iteminator.command.ModernEnchantment;
import xyz.tehbrian.iteminator.command.ModernPotionEffectType;
import xyz.tehbrian.iteminator.command.ModernPotionType;
import xyz.tehbrian.iteminator.config.LangConfig;
import xyz.tehbrian.iteminator.user.UserService;
import xyz.tehbrian.iteminator.util.HeldItemModifier;
import xyz.tehbrian.iteminator.util.ItemMetaRequiredTypes;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class SpecialCommands {

  private final Iteminator iteminator;
  private final UserService userService;
  private final LangConfig langConfig;

  @Inject
  public SpecialCommands(
      final Iteminator iteminator,
      final UserService userService,
      final LangConfig langConfig
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
      final PaperCommandManager<CommandSender> commandManager,
      final Command.Builder<CommandSender> parent
  ) {
    final var sArmorStand = parent.literal("armor-stand")
        .meta(CommandMeta.DESCRIPTION, "Commands for Armor Stands.")
        .permission(Permissions.ARMOR_STAND);

    final var sArmorStandShowArms = sArmorStand.literal("show-arms")
        .meta(CommandMeta.DESCRIPTION, "Set the show arms flag.")
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

    commandManager
        .command(sArmorStandShowArms)
        .command(sArmorStandInvisible)
        .command(sArmorStandMarker)
        .command(sArmorStandNoBasePlate)
        .command(sArmorStandSmall);

    final var sAxolotlBucket = parent.literal("axolotl-bucket")
        .meta(CommandMeta.DESCRIPTION, "Commands for Axolotl Buckets.")
        .permission(Permissions.AXOLOTL_BUCKET);

    final var sAxolotlBucketVariant = sAxolotlBucket.literal("variant")
        .meta(CommandMeta.DESCRIPTION, "Set the variant.")
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

    final var sBannerPattern = sBanner.literal("pattern")
        .meta(CommandMeta.DESCRIPTION, "Modify the patterns.");

    final var sBannerPatternAdd = sBannerPattern.literal("add")
        .meta(CommandMeta.DESCRIPTION, "Add a pattern.")
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

    final var sBannerPatternSet = sBannerPattern.literal("set")
        .meta(CommandMeta.DESCRIPTION, "Set a pattern.")
        .argument(IntegerArgument.<CommandSender>builder("index").withMin(0))
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

    final var sBannerPatternRemove = sBannerPattern.literal("remove")
        .meta(CommandMeta.DESCRIPTION, "Remove a pattern.")
        .argument(IntegerArgument.<CommandSender>builder("index").withMin(0))
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

    final var sBannerPatternClear = sBannerPattern.literal("clear")
        .meta(CommandMeta.DESCRIPTION, "Clear the patterns.")
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifySpecial(
              sender,
              b -> b.patterns(List.of()),
              BannerBuilder::of,
              BannerMeta.class
          );
        });

    commandManager
        .command(sBannerPatternAdd)
        .command(sBannerPatternSet)
        .command(sBannerPatternRemove)
        .command(sBannerPatternClear);

    final var sBook = parent.literal("book")
        .meta(CommandMeta.DESCRIPTION, "Commands for Books.")
        .permission(Permissions.BOOK);

    final var sBookTitle = sBook.literal("title")
        .meta(CommandMeta.DESCRIPTION, "Set the title. Pass nothing to reset.")
        .argument(StringArgument.optional("text", StringArgument.StringMode.GREEDY))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifySpecial(
              sender,
              b -> c.<String>getOptional("text")
                  .map(s -> b.title(this.userService.formatWithUserFormat(s, sender)))
                  .orElseGet(() -> b.title(null)),
              BookBuilder::of,
              BookMeta.class
          );
        });

    final var sBookAuthor = sBook.literal("author")
        .meta(CommandMeta.DESCRIPTION, "Set the author. Pass nothing to reset.")
        .argument(StringArgument.optional("text", StringArgument.StringMode.GREEDY))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifySpecial(
              sender,
              b -> c.<String>getOptional("text")
                  .map(s -> b.author(this.userService.formatWithUserFormat(s, sender)))
                  .orElseGet(() -> b.author(null)),
              BookBuilder::of,
              BookMeta.class
          );
        });

    final var sBookGeneration = sBook.literal("generation")
        .meta(CommandMeta.DESCRIPTION, "Set the generation.")
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

    commandManager
        .command(sBookTitle)
        .command(sBookAuthor)
        .command(sBookGeneration)
        .command(sBookEditable);

    final var sDamageable = parent.literal("damage") // abnormal name because ends in "able"
        .meta(CommandMeta.DESCRIPTION, "Commands for Damageable items.")
        .permission(Permissions.DAMAGEABLE);

    final var sDamageableSet = sDamageable
        .meta(CommandMeta.DESCRIPTION, "Sets the damage.")
        .argument(IntegerArgument.<CommandSender>builder("damage").asOptionalWithDefault(0).withMin(0))
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
        .argument(EnumArgument.of(ModernEnchantment.class, "type"))
        .argument(IntegerArgument.<CommandSender>builder("level").withMin(0).withMax(255))
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
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifySpecial(
              sender,
              b -> b.storedEnchants(Map.of()),
              EnchantmentStorageBuilder::of,
              EnchantmentStorageMeta.class
          );
        });

    commandManager
        .command(sEnchantmentStorageAdd)
        .command(sEnchantmentStorageRemove)
        .command(sEnchantmentStorageClear);

//    final var sFireworkEffect = parent.literal("firework-effect")
//        .meta(CommandMeta.DESCRIPTION, "Commands for Firework Effects.")
//        .permission(Permissions.FIREWORK_EFFECT);
//
//    final var sFireworkEffectFlicker = sFireworkEffect.literal("flicker")
//        .meta(CommandMeta.DESCRIPTION, "Set whether the effect should flicker.")
//        .argument(BooleanArgument.of("boolean"))
//        .handler(c -> {
//          final var sender = (Player) c.getSender();
//          this.modifySpecial(
//              sender,
//              b -> b.fireworkEffect(
//                  this.fireworkEffectBuilder(b.fireworkEffect())
//                      .flicker(c.<Boolean>get("boolean")).build()
//              ),
//              FireworkEffectBuilder::of,
//              FireworkEffectMeta.class
//          );
//        });
//
//    final var sFireworkEffectTrail = sFireworkEffect.literal("trail")
//        .meta(CommandMeta.DESCRIPTION, "Set whether the effect should trail.")
//        .argument(BooleanArgument.of("boolean"))
//        .handler(c -> {
//          final var sender = (Player) c.getSender();
//          this.modifySpecial(
//              sender,
//              b -> b.fireworkEffect(
//                  this.fireworkEffectBuilder(b.fireworkEffect())
//                      .trail(c.<Boolean>get("boolean")).build()
//              ),
//              FireworkEffectBuilder::of,
//              FireworkEffectMeta.class
//          );
//        });
//
//    final var sFireworkEffectType = sFireworkEffect.literal("type")
//        .meta(CommandMeta.DESCRIPTION, "Set the effect type.")
//        .argument(EnumArgument.of(FireworkEffect.Type.class, "type"))
//        .handler(c -> {
//          final var sender = (Player) c.getSender();
//          this.modifySpecial(
//              sender,
//              b -> b.fireworkEffect(
//                  this.fireworkEffectBuilder(b.fireworkEffect())
//                      .with(c.get("type")).build()
//              ),
//              FireworkEffectBuilder::of,
//              FireworkEffectMeta.class
//          );
//        });
//
//    final var sFireworkEffectColor = sFireworkEffect.literal("color");
//
//    final var sFireworkEffectColorAdd = sFireworkEffectColor.literal("add")
//        .meta(CommandMeta.DESCRIPTION, "Add a color.")
//        .argument(IntegerArgument.<CommandSender>builder("red").withMin(0).withMax(255))
//        .argument(IntegerArgument.<CommandSender>builder("blue").withMin(0).withMax(255))
//        .argument(IntegerArgument.<CommandSender>builder("green").withMin(0).withMax(255))
//        .handler(c -> {
//          final var sender = (Player) c.getSender();
//          this.modifySpecial(
//              sender,
//              b -> b.fireworkEffect(this.fireworkEffectBuilder(b.fireworkEffect())
//                  .withColor(Color.fromRGB(
//                      c.<Integer>get("red"),
//                      c.<Integer>get("green"),
//                      c.<Integer>get("blue")
//                  )).build()
//              ),
//              FireworkEffectBuilder::of,
//              FireworkEffectMeta.class
//          );
//        });
//
//    final var sFireworkEffectFadeColor = sFireworkEffect.literal("fade-color");
//
//    final var sFireworkEffectFadeColorAdd = sFireworkEffectFadeColor.literal("add")
//        .meta(CommandMeta.DESCRIPTION, "Add a fade color.")
//        .argument(IntegerArgument.<CommandSender>builder("red").withMin(0).withMax(255))
//        .argument(IntegerArgument.<CommandSender>builder("blue").withMin(0).withMax(255))
//        .argument(IntegerArgument.<CommandSender>builder("green").withMin(0).withMax(255))
//        .handler(c -> {
//          final var sender = (Player) c.getSender();
//          this.modifySpecial(
//              sender,
//              b -> b.fireworkEffect(this.fireworkEffectBuilder(b.fireworkEffect())
//                  .withFade(Color.fromRGB(
//                      c.<Integer>get("red"),
//                      c.<Integer>get("green"),
//                      c.<Integer>get("blue")
//                  )).build()
//              ),
//              FireworkEffectBuilder::of,
//              FireworkEffectMeta.class
//          );
//        });
//
//    commandManager
//        .command(sFireworkEffectFlicker)
//        .command(sFireworkEffectTrail)
//        .command(sFireworkEffectType)
//        .command(sFireworkEffectColorAdd)
//        .command(sFireworkEffectFadeColorAdd);

    final var sFirework = parent.literal("firework")
        .meta(CommandMeta.DESCRIPTION, "Commands for Fireworks.")
        .permission(Permissions.FIREWORK);

    final var sFireworkPower = sFirework.literal("power")
        .meta(CommandMeta.DESCRIPTION, "Set the power.")
        .argument(IntegerArgument.<CommandSender>builder("power").withMin(0).withMax(127))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifySpecial(
              sender,
              b -> b.power(c.<Integer>get("power")),
              FireworkBuilder::of,
              FireworkMeta.class
          );
        });

    commandManager.command(sFireworkPower);

//        final var sItemFrame = parent.literal("item-frame")
//                .meta(CommandMeta.DESCRIPTION, "Commands for Item Frames.")
//                .permission(Permissions.ITEM_FRAME);
//
//        final var sItemFrameInvisible = sItemFrame.literal("invisible")
//                .meta(CommandMeta.DESCRIPTION, "Set the invisible flag.")
//                .argument(BooleanArgument.of("boolean"))
//                .handler(c -> {
//                    final var sender = (Player) c.getSender();
//                    HeldItemModifier.modifyItemStack(
//                            sender,
//                            i -> {
//                                final var builder = BlockStateBuilder.of(i);
//                                if (builder.blockState() instanceof ItemFrame frame) {
//                                    frame.setVisible(!c.<Boolean>get("boolean"));
//                                    return builder.blockState((BlockState) frame).build();
//                                } else {
//                                    sender.sendMessage(this.generateWrongTypeMessage(List.of(Material.ITEM_FRAME)));
//                                    return null;
//                                }
//                            }
//                    );
//                });
//
//        final var sItemFrameFixed = sItemFrame.literal("fixed")
//                .meta(CommandMeta.DESCRIPTION, "Set the fixed flag.")
//                .argument(BooleanArgument.of("boolean"))
//                .handler(c -> {
//                    final var sender = (Player) c.getSender();
//                    HeldItemModifier.modifyItemStack(
//                            sender,
//                            i -> {
//                                final var builder = BlockDataBuilder.of(i);
//                                if (builder.blockData(Material.ITEM_FRAME) instanceof ItemFrame frame) {
//                                    frame.setFixed(!c.<Boolean>get("boolean"));
//                                    return builder.blockData((BlockData) frame).build();
//                                } else {
//                                    sender.sendMessage(this.generateWrongTypeMessage(List.of(Material.ITEM_FRAME)));
//                                    return null;
//                                }
//                            }
//                    );
//                });
//
//        commandManager
//                .command(sItemFrameInvisible)
//                .command(sItemFrameFixed);

    final var sLeatherArmor = parent.literal("leather-armor")
        .meta(CommandMeta.DESCRIPTION, "Commands for Leather Armor.")
        .permission(Permissions.LEATHER_ARMOR);

    final var sLeatherArmorColor = sLeatherArmor.literal("color");

    final var sLeatherArmorSet = sLeatherArmorColor
        .meta(CommandMeta.DESCRIPTION, "Set the armor's color.")
        .argument(IntegerArgument.<CommandSender>builder("red").withMin(0).withMax(255))
        .argument(IntegerArgument.<CommandSender>builder("blue").withMin(0).withMax(255))
        .argument(IntegerArgument.<CommandSender>builder("green").withMin(0).withMax(255))
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
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifySpecial(
              sender,
              b -> b.color(null),
              LeatherArmorBuilder::of,
              LeatherArmorMeta.class
          );
        });

    commandManager
        .command(sLeatherArmorSet)
        .command(sLeatherArmorReset);

    final var sMap = parent.literal("map")
        .meta(CommandMeta.DESCRIPTION, "Commands for Maps.")
        .permission(Permissions.MAP);

    final var sMapScaling = sMap.literal("scaling")
        .meta(CommandMeta.DESCRIPTION, "Set whether the map is scaling.")
        .argument(BooleanArgument.of("scaling"))
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
        .meta(CommandMeta.DESCRIPTION, "Set the map's location name. Pass nothing to reset.")
        .argument(StringArgument.optional("text", StringArgument.StringMode.GREEDY))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifySpecial(
              sender,
              b -> b.locationName(c.<String>getOptional("text").orElse(null)),
              MapBuilder::of,
              MapMeta.class
          );
        });

    final var sMapColor = sMap.literal("color");

    final var sMapColorSet = sMapColor
        .meta(CommandMeta.DESCRIPTION, "Set the map's color.")
        .argument(IntegerArgument.<CommandSender>builder("red").withMin(0).withMax(255))
        .argument(IntegerArgument.<CommandSender>builder("blue").withMin(0).withMax(255))
        .argument(IntegerArgument.<CommandSender>builder("green").withMin(0).withMax(255))
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
        .argument(IntegerArgument.of("x"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifyMapView(
              sender,
              v -> {
                v.setCenterX(c.<Integer>get("x"));
                return v;
              }
          );
        });

    final var sMapViewCenterZ = sMapView.literal("center-z")
        .meta(CommandMeta.DESCRIPTION, "Set the map view's center z.")
        .argument(IntegerArgument.of("z"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifyMapView(
              sender,
              v -> {
                v.setCenterZ(c.<Integer>get("z"));
                return v;
              }
          );
        });

    final var sMapViewScale = sMapView.literal("scale")
        .meta(CommandMeta.DESCRIPTION, "Set the map view's scale.")
        .argument(EnumArgument.of(MapView.Scale.class, "scale"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifyMapView(
              sender,
              v -> {
                v.setScale(c.get("scale"));
                return v;
              }
          );
        });

    final var sMapViewLocked = sMapView.literal("locked")
        .meta(CommandMeta.DESCRIPTION, "Set whether the map view is locked.")
        .argument(BooleanArgument.of("boolean"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifyMapView(
              sender,
              v -> {
                v.setLocked(c.<Boolean>get("boolean"));
                return v;
              }
          );
        });

    final var sMapViewTrackingPosition = sMapView.literal("tracking-position")
        .meta(CommandMeta.DESCRIPTION, "Set whether the map view shows a position cursor.")
        .argument(BooleanArgument.of("boolean"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifyMapView(
              sender,
              v -> {
                v.setTrackingPosition(c.<Boolean>get("boolean"));
                return v;
              }
          );
        });

    final var sMapViewUnlimitedTracking = sMapView.literal("unlimited-tracking")
        .meta(CommandMeta.DESCRIPTION, "Set whether the map view shows off-screen cursors.")
        .argument(BooleanArgument.of("boolean"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifyMapView(
              sender,
              v -> {
                v.setUnlimitedTracking(c.<Boolean>get("boolean"));
                return v;
              }
          );
        });

    commandManager
        .command(sMapScaling)
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
        .argument(EnumArgument.of(ModernPotionEffectType.class, "type"))
        .argument(IntegerArgument.<CommandSender>builder("duration").withMin(0))
        .argument(IntegerArgument.<CommandSender>builder("amplifier").withMin(0).withMax(255))
        .argument(BooleanArgument.optional("ambient", true))
        .argument(BooleanArgument.optional("particles", true))
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
        .argument(IntegerArgument.<CommandSender>builder("red").withMin(0).withMax(255))
        .argument(IntegerArgument.<CommandSender>builder("blue").withMin(0).withMax(255))
        .argument(IntegerArgument.<CommandSender>builder("green").withMin(0).withMax(255))
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

    commandManager
        .command(sPotionEffectAdd)
        .command(sPotionEffectRemove)
        .command(sPotionEffectClear)
        .command(sPotionColorSet)
        .command(sPotionColorReset)
        .command(sPotionType);

    final var sRepairable = parent.literal("repair") // abnormal name because ends in "able"
        .meta(CommandMeta.DESCRIPTION, "Commands for Repairable items.")
        .permission(Permissions.REPAIRABLE);

    final var sRepairableCostSet = sRepairable.literal("cost")
        .meta(CommandMeta.DESCRIPTION, "Set the repair cost.")
        .argument(IntegerArgument.<CommandSender>builder("cost").asOptionalWithDefault(0))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifySpecial(
              sender,
              b -> b.repairCost(c.get("cost")),
              RepairableBuilder::of,
              Repairable.class
          );
        });

    commandManager.command(sRepairableCostSet);

    final var sSkull = parent.literal("skull")
        .meta(CommandMeta.DESCRIPTION, "Commands for Skulls.")
        .permission(Permissions.SKULL);

    final var sSkullName = sSkull.literal("name")
        .meta(CommandMeta.DESCRIPTION, "Set the owning player. Pass nothing to reset.")
        .argument(StringArgument.<CommandSender>builder("name")
            .asOptional()
            .withSuggestionsProvider((c, i) -> this.iteminator.getServer()
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
                  skullMeta.setOwner(c.<String>getOptional("name").orElse(null));
                  i.setItemMeta(skullMeta);
                  return i;
                } else {
                  this.sendWrongTypeMessage(sender, SkullMeta.class);
                  return null;
                }
              }
          );
        });

    commandManager.command(sSkullName);

    final var sSuspiciousStew = parent.literal("suspicious-stew")
        .meta(CommandMeta.DESCRIPTION, "Commands for Suspicious Stews.")
        .permission(Permissions.SUSPICIOUS_STEW);

    final var sSuspiciousStewEffect = sSuspiciousStew.literal("effect")
        .meta(CommandMeta.DESCRIPTION, "Modify the custom effects.");

    final var sSuspiciousStewEffectAdd = sSuspiciousStewEffect.literal("add")
        .meta(CommandMeta.DESCRIPTION, "Add a custom effect.")
        .argument(EnumArgument.of(ModernPotionEffectType.class, "type"))
        .argument(IntegerArgument.<CommandSender>builder("duration").withMin(0))
        .argument(IntegerArgument.<CommandSender>builder("amplifier").withMin(0).withMax(255))
        .argument(BooleanArgument.optional("ambient", true))
        .argument(BooleanArgument.optional("particles", true))
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

    final var sSuspiciousStewEffectRemove = sSuspiciousStewEffect.literal("remove")
        .meta(CommandMeta.DESCRIPTION, "Remove a custom effect.")
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

    final var sSuspiciousStewEffectClear = sSuspiciousStewEffect.literal("clear")
        .meta(CommandMeta.DESCRIPTION, "Clear the custom effects.")
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.modifySpecial(
              sender,
              b -> b.customEffects(null),
              SuspiciousStewBuilder::of,
              SuspiciousStewMeta.class
          );
        });

    commandManager
        .command(sSuspiciousStewEffectAdd)
        .command(sSuspiciousStewEffectRemove)
        .command(sSuspiciousStewEffectClear);

    final var sTropicalFishBucket = parent.literal("tropical-fish-bucket")
        .meta(CommandMeta.DESCRIPTION, "Commands for Tropical Fish Buckets.")
        .permission(Permissions.TROPICAL_FISH_BUCKET);

    final var sTropicalFishBucketPattern = sTropicalFishBucket.literal("pattern")
        .meta(CommandMeta.DESCRIPTION, "Set the pattern.")
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

    commandManager
        .command(sTropicalFishBucketPattern)
        .command(sTropicalFishBucketBodyColor)
        .command(sTropicalFishBucketPatternColor);
  }

  private Component generateWrongTypeMessage(final List<Material> requiredTypes) {
    final Component types = Component.join(
        JoinConfiguration.separators(Component.text(", "), Component.text(", or ")),
        requiredTypes.stream().map(Component::translatable).toList()
    );

    return this.langConfig.c(
        NodePath.path("wrong-type"),
        Placeholder.component("type", types)
    );
  }

  private void sendWrongTypeMessage(final Audience audience, final Class<? extends ItemMeta> metaType) {
    final List<Material> requiredTypes =
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
      final Player player, final Function<T, @Nullable T> operator,
      final Function<ItemStack, T> builderCreator, final Class<? extends ItemMeta> metaType
  ) {
    try {
      HeldItemModifier.modifySpecial(player, operator, builderCreator);
    } catch (final IllegalArgumentException e) {
      this.sendWrongTypeMessage(player, metaType);
    }
  }

  /**
   * @param player   the player to target
   * @param operator the operator to apply to the map in the main hand
   */
  private void modifyMapView(
      final Player player, final Function<MapView, @Nullable MapView> operator
  ) {
    this.modifySpecial(
        player,
        b -> {
          final @Nullable MapView view = b.mapView();
          if (view != null) {
            b.mapView(operator.apply(view));
          }
          return b;
        },
        MapBuilder::of,
        MapMeta.class
    );
  }

  /**
   * Creates a new {@code Builder} from the provided {@code FireworkEffect}.
   *
   * @param fireworkEffect the {@code FireworkEffect} to turn into a {@code Builder}
   * @return the {@code Builder}
   */
  private FireworkEffect.Builder fireworkEffectBuilder(final @Nullable FireworkEffect fireworkEffect) {
    if (fireworkEffect == null) {
      return FireworkEffect.builder();
    }
    return FireworkEffect.builder()
        .flicker(fireworkEffect.hasFlicker())
        .trail(fireworkEffect.hasTrail())
        .withColor(fireworkEffect.getColors())
        .withFade(fireworkEffect.getFadeColors())
        .with(fireworkEffect.getType());
  }

}
