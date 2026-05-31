package dev.tehbrian.iteminator.command.subs;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import com.google.inject.Inject;
import dev.tehbrian.iteminator.Iteminator;
import dev.tehbrian.iteminator.Permission;
import dev.tehbrian.iteminator.config.LangConfig;
import dev.tehbrian.iteminator.user.UserService;
import dev.tehbrian.iteminator.util.HeldItemModifier;
import dev.tehbrian.iteminator.util.ItemMetaRequiredTypes;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.potion.SuspiciousEffectEntry;
import io.papermc.paper.registry.RegistryKey;
import love.broccolai.corn.minecraft.item.AbstractItemBuilder;
import love.broccolai.corn.minecraft.item.special.ArmorStandBuilder;
import love.broccolai.corn.minecraft.item.special.AxolotlBucketBuilder;
import love.broccolai.corn.minecraft.item.special.BannerBuilder;
import love.broccolai.corn.minecraft.item.special.BookBuilder;
import love.broccolai.corn.minecraft.item.special.DamageableBuilder;
import love.broccolai.corn.minecraft.item.special.EnchantmentStorageBuilder;
import love.broccolai.corn.minecraft.item.special.FireworkBuilder;
import love.broccolai.corn.minecraft.item.special.LeatherArmorBuilder;
import love.broccolai.corn.minecraft.item.special.MapBuilder;
import love.broccolai.corn.minecraft.item.special.PotionBuilder;
import love.broccolai.corn.minecraft.item.special.RepairableBuilder;
import love.broccolai.corn.minecraft.item.special.SuspiciousStewBuilder;
import love.broccolai.corn.minecraft.item.special.TropicalFishBucketBuilder;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.Command;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.spongepowered.configurate.NodePath;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static dev.tehbrian.iteminator.command.subs.CommonCommands.sender;
import static org.incendo.cloud.bukkit.parser.EnchantmentParser.enchantmentParser;
import static org.incendo.cloud.component.DefaultValue.constant;
import static org.incendo.cloud.description.Description.description;
import static org.incendo.cloud.paper.parser.RegistryEntryParser.registryEntryParser;
import static org.incendo.cloud.parser.standard.BooleanParser.booleanParser;
import static org.incendo.cloud.parser.standard.EnumParser.enumParser;
import static org.incendo.cloud.parser.standard.IntegerParser.integerParser;
import static org.incendo.cloud.parser.standard.StringParser.greedyStringParser;
import static org.incendo.cloud.suggestion.SuggestionProvider.blockingStrings;

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
			final PaperCommandManager<Source> commandManager,
			final Command.Builder<PlayerSource> parent
	) {
		final var sArmorStand = parent.literal("armor-stand")
				.commandDescription(description("Commands for Armor Stands."))
				.permission(Permission.ARMOR_STAND);

		final var sArmorStandShowArms = sArmorStand.literal("show-arms")
				.commandDescription(description("Set the show arms flag."))
				.required("value", booleanParser())
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.showArms(c.get("value")),
						ArmorStandBuilder::armorStandBuilder,
						ArmorStandMeta.class
				));

		final var sArmorStandInvisible = sArmorStand.literal("invisible")
				.commandDescription(description("Set the invisible flag."))
				.required("value", booleanParser())
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.invisible(c.get("value")),
						ArmorStandBuilder::armorStandBuilder,
						ArmorStandMeta.class
				));

		final var sArmorStandMarker = sArmorStand.literal("marker")
				.commandDescription(description("Set the marker flag."))
				.required("value", booleanParser())
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.marker(c.get("value")),
						ArmorStandBuilder::armorStandBuilder,
						ArmorStandMeta.class
				));

		final var sArmorStandNoBasePlate = sArmorStand.literal("no-base-plate")
				.commandDescription(description("Set the no base plate flag."))
				.required("value", booleanParser())
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.noBasePlate((c.get("value"))),
						ArmorStandBuilder::armorStandBuilder,
						ArmorStandMeta.class
				));

		final var sArmorStandSmall = sArmorStand.literal("small")
				.commandDescription(description("Set the small flag."))
				.required("value", booleanParser())
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.small(c.get("value")),
						ArmorStandBuilder::armorStandBuilder,
						ArmorStandMeta.class
				));

		commandManager
				.command(sArmorStandShowArms)
				.command(sArmorStandInvisible)
				.command(sArmorStandMarker)
				.command(sArmorStandNoBasePlate)
				.command(sArmorStandSmall);

		final var sAxolotlBucket = parent.literal("axolotl-bucket")
				.commandDescription(description("Commands for Axolotl Buckets."))
				.permission(Permission.AXOLOTL_BUCKET);

		final var sAxolotlBucketVariant = sAxolotlBucket.literal("variant")
				.commandDescription(description("Set the variant."))
				.required("variant", enumParser(Axolotl.Variant.class))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.variant(c.get("variant")),
						AxolotlBucketBuilder::axolotlBucketBuilder,
						AxolotlBucketMeta.class
				));

		commandManager.command(sAxolotlBucketVariant);

		final var sBanner = parent.literal("banner")
				.commandDescription(description("Commands for Banners."))
				.permission(Permission.BANNER);

		final var sBannerPattern = sBanner.literal("pattern")
				.commandDescription(description("Modify the patterns."));

		final var sBannerPatternAdd = sBannerPattern.literal("add")
				.commandDescription(description("Add a pattern."))
				.required("color", enumParser(DyeColor.class))
				.required("type", registryEntryParser(RegistryKey.BANNER_PATTERN, TypeToken.get(PatternType.class)))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.addPattern(new Pattern(c.get("color"), c.get("type"))),
						BannerBuilder::bannerBuilder,
						BannerMeta.class
				));

		final var sBannerPatternSet = sBannerPattern.literal("set")
				.commandDescription(description("Set a pattern."))
				.required("index", integerParser(0))
				.required("color", enumParser(DyeColor.class))
				.required("type", registryEntryParser(RegistryKey.BANNER_PATTERN, TypeToken.get(PatternType.class)))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> {
							final int index = c.<Integer>get("index");
							if (b.patterns().size() <= index) {
								sender(c).sendMessage(this.langConfig.c(NodePath.path("error", "out-of-bounds")));
								return null;
							}

							return b.pattern(index, new Pattern(c.get("color"), c.get("type")));
						},
						BannerBuilder::bannerBuilder,
						BannerMeta.class
				));

		final var sBannerPatternRemove = sBannerPattern.literal("remove")
				.commandDescription(description("Remove a pattern."))
				.required("index", integerParser(0))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> {
							final int index = c.<Integer>get("index");
							if (b.patterns().size() <= index) {
								sender(c).sendMessage(this.langConfig.c(NodePath.path("error", "out-of-bounds")));
								return null;
							}

							return b.removePattern(index);
						},
						BannerBuilder::bannerBuilder,
						BannerMeta.class
				));

		final var sBannerPatternClear = sBannerPattern.literal("clear")
				.commandDescription(description("Clear the patterns."))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.patterns(List.of()),
						BannerBuilder::bannerBuilder,
						BannerMeta.class
				));

		commandManager
				.command(sBannerPatternAdd)
				.command(sBannerPatternSet)
				.command(sBannerPatternRemove)
				.command(sBannerPatternClear);

		final var sBook = parent.literal("book")
				.commandDescription(description("Commands for Books."))
				.permission(Permission.BOOK);

		final var sBookTitle = sBook.literal("title")
				.commandDescription(description("Set the title. Pass nothing to reset."))
				.optional("text", greedyStringParser())
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> c.<String>optional("text")
								.map(s -> b.title(this.userService.formatWithUserFormat(s, sender(c))))
								.orElseGet(() -> b.title(null)),
						BookBuilder::bookBuilder,
						BookMeta.class
				));

		final var sBookAuthor = sBook.literal("author")
				.commandDescription(description("Set the author. Pass nothing to reset."))
				.optional("text", greedyStringParser())
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> c.<String>optional("text")
								.map(s -> b.author(this.userService.formatWithUserFormat(s, sender(c))))
								.orElseGet(() -> b.author(null)),
						BookBuilder::bookBuilder,
						BookMeta.class
				));

		final var sBookGeneration = sBook.literal("generation")
				.commandDescription(description("Set the generation."))
				.required("generation", enumParser(BookMeta.Generation.class))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.generation(c.get("generation")),
						BookBuilder::bookBuilder,
						BookMeta.class
				));

		final var sBookEditable = sBook.literal("editable")
				.commandDescription(description("Set whether the book is editable."))
				.required("value", booleanParser())
				.handler(c -> {
					this.modifySpecial(
							sender(c),
							b -> {
								if (c.get("value")) {
									return b.material(Material.WRITABLE_BOOK);
								} else {
									return b.material(Material.WRITTEN_BOOK);
								}
							},
							BookBuilder::bookBuilder,
							BookMeta.class
					);
				});

		commandManager
				.command(sBookTitle)
				.command(sBookAuthor)
				.command(sBookGeneration)
				.command(sBookEditable);

		final var sDamageable = parent.literal("damage") // abnormal name because ends in "able"
				.commandDescription(description("Commands for Damageable items."))
				.permission(Permission.DAMAGEABLE);

		final var sDamageableSet = sDamageable
				.commandDescription(description("Sets the damage."))
				.optional("damage", integerParser(0), constant(0))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.damage(c.get("damage")),
						DamageableBuilder::damageableBuilder,
						Damageable.class
				));

		commandManager.command(sDamageableSet);

		final var sEnchantmentStorage = parent.literal("enchantment-storage")
				.commandDescription(description("Commands for Enchantment Storages."))
				.permission(Permission.ENCHANTMENT_STORAGE);

		final var sEnchantmentStorageAdd = sEnchantmentStorage.literal("add")
				.commandDescription(description("Add a stored enchantment."))
				.required("type", enchantmentParser())
				.required("level", integerParser(0, 255))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.addStoredEnchant(c.get("type"), c.<Integer>get("level")),
						EnchantmentStorageBuilder::enchantmentStorageBuilder,
						EnchantmentStorageMeta.class
				));

		final var sEnchantmentStorageRemove = sEnchantmentStorage.literal("remove")
				.commandDescription(description("Remove a stored enchantment."))
				.required("type", enchantmentParser())
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.removeStoredEnchant(c.get("type")),
						EnchantmentStorageBuilder::enchantmentStorageBuilder,
						EnchantmentStorageMeta.class
				));

		final var sEnchantmentStorageClear = sEnchantmentStorage.literal("clear")
				.commandDescription(description("Clear the stored enchantments."))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.storedEnchants(Map.of()),
						EnchantmentStorageBuilder::enchantmentStorageBuilder,
						EnchantmentStorageMeta.class
				));

		commandManager
				.command(sEnchantmentStorageAdd)
				.command(sEnchantmentStorageRemove)
				.command(sEnchantmentStorageClear);

//    final var sFireworkEffect = parent.literal("firework-effect")
//        .commandDescription(description("Commands for Firework Effects."))
//        .permission(Permissions.FIREWORK_EFFECT);
//
//    final var sFireworkEffectFlicker = sFireworkEffect.literal("flicker")
//        .commandDescription(description("Set whether the effect should flicker."))
//        .required("value", booleanParser())
//        .handler(c -> {
//          final var sender = c.sender().source();
//          this.modifySpecial(
//              sender,
//              b -> b.fireworkEffect(
//                  this.fireworkEffectBuilder(b.fireworkEffect())
//                      .flicker(c.<Boolean>get("boolean")).build()
//              ),
//              FireworkEffectBuilder::fireworkEffectBuilder,
//              FireworkEffectMeta.class
//          );
//        });
//
//    final var sFireworkEffectTrail = sFireworkEffect.literal("trail")
//        .commandDescription(description("Set whether the effect should trail."))
//        .required("value", booleanParser())
//        .handler(c -> {
//          final var sender = c.sender().source();
//          this.modifySpecial(
//              sender,
//              b -> b.fireworkEffect(
//                  this.fireworkEffectBuilder(b.fireworkEffect())
//                      .trail(c.<Boolean>get("boolean")).build()
//              ),
//              FireworkEffectBuilder::fireworkEffectBuilder,
//              FireworkEffectMeta.class
//          );
//        });
//
//    final var sFireworkEffectType = sFireworkEffect.literal("type")
//        .commandDescription(description("Set the effect type."))
//        .argument(EnumArgument.of(FireworkEffect.Type.class, "type"))
//        .handler(c -> {
//          final var sender = c.sender().source();
//          this.modifySpecial(
//              sender,
//              b -> b.fireworkEffect(
//                  this.fireworkEffectBuilder(b.fireworkEffect())
//                      .with(c.get("type")).build()
//              ),
//              FireworkEffectBuilder::fireworkEffectBuilder,
//              FireworkEffectMeta.class
//          );
//        });
//
//    final var sFireworkEffectColor = sFireworkEffect.literal("color");
//
//    final var sFireworkEffectColorAdd = sFireworkEffectColor.literal("add")
//        .commandDescription(description("Add a color."))
//        .argument(IntegerArgument.<CommandSender>builder("red").withMin(0).withMax(255))
//        .argument(IntegerArgument.<CommandSender>builder("blue").withMin(0).withMax(255))
//        .argument(IntegerArgument.<CommandSender>builder("green").withMin(0).withMax(255))
//        .handler(c -> {
//          final var sender = c.sender().source();
//          this.modifySpecial(
//              sender,
//              b -> b.fireworkEffect(this.fireworkEffectBuilder(b.fireworkEffect())
//                  .withColor(Color.fromRGB(
//                      c.<Integer>get("red"),
//                      c.<Integer>get("green"),
//                      c.<Integer>get("blue")
//                  )).build()
//              ),
//              FireworkEffectBuilder::fireworkEffectBuilder,
//              FireworkEffectMeta.class
//          );
//        });
//
//    final var sFireworkEffectFadeColor = sFireworkEffect.literal("fade-color");
//
//    final var sFireworkEffectFadeColorAdd = sFireworkEffectFadeColor.literal("add")
//        .commandDescription(description("Add a fade color."))
//        .argument(IntegerArgument.<CommandSender>builder("red").withMin(0).withMax(255))
//        .argument(IntegerArgument.<CommandSender>builder("blue").withMin(0).withMax(255))
//        .argument(IntegerArgument.<CommandSender>builder("green").withMin(0).withMax(255))
//        .handler(c -> {
//          final var sender = c.sender().source();
//          this.modifySpecial(
//              sender,
//              b -> b.fireworkEffect(this.fireworkEffectBuilder(b.fireworkEffect())
//                  .withFade(Color.fromRGB(
//                      c.<Integer>get("red"),
//                      c.<Integer>get("green"),
//                      c.<Integer>get("blue")
//                  )).build()
//              ),
//              FireworkEffectBuilder::fireworkEffectBuilder,
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
				.commandDescription(description("Commands for Fireworks."))
				.permission(Permission.FIREWORK);

		final var sFireworkPower = sFirework.literal("power")
				.commandDescription(description("Set the power."))
				.required("power", integerParser(0, 127))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.power(c.<Integer>get("power")),
						FireworkBuilder::fireworkBuilder,
						FireworkMeta.class
				));

		commandManager.command(sFireworkPower);

//    final var sItemFrame = parent.literal("item-frame")
//        .commandDescription(description("Commands for Item Frames.")))
//        .permission(Permission.ITEM_FRAME);
//
//    final var sItemFrameInvisible = sItemFrame.literal("invisible")
//        .commandDescription(description("Set the invisible flag.")))
//        .required("value", booleanParser())
//        .handler(c -> HeldItemModifier.modifyItemStack(
//            sender(c),
//            i -> {
//              final var meta = i.getItemMeta();
//              if (meta instanceof final ItemFrameMeta itemFrame) {
//                itemFrame.setInvisible(c.get("value"));
//                i.setItemMeta(itemFrame);
//                return i;
//              } else {
//                sender(c).sendMessage(this.generateWrongTypeMessage(List.of(
//                    Material.ITEM_FRAME,
//                    Material.GLOW_ITEM_FRAME
//                )));
//                return null;
//              }
//            }
//        ));
//
//    final var sItemFrameFixed = sItemFrame.literal("fixed")
//        .commandDescription(description("Set the fixed flag.")))
//        .required("value", booleanParser())
//        .handler(c -> HeldItemModifier.modifyItemStack(
//            sender(c),
//            i -> {
//              final var meta = i.getItemMeta();
//              if (meta instanceof final ItemFrameMeta itemFrame) {
//                itemFrame.setFixed(c.get("value"));
//                i.setItemMeta(itemFrame);
//                return i;
//              } else {
//                sender(c).sendMessage(this.generateWrongTypeMessage(List.of(
//                    Material.ITEM_FRAME,
//                    Material.GLOW_ITEM_FRAME
//                )));
//                return null;
//              }
//            }
//        ));
//
//    commandManager
//        .command(sItemFrameInvisible)
//        .command(sItemFrameFixed);

		final var sLeatherArmor = parent.literal("leather-armor")
				.commandDescription(description("Commands for Leather Armor."))
				.permission(Permission.LEATHER_ARMOR);

		final var sLeatherArmorColor = sLeatherArmor.literal("color");

		final var sLeatherArmorSet = sLeatherArmorColor
				.commandDescription(description("Set the armor's color."))
				.required("red", integerParser(0, 255))
				.required("blue", integerParser(0, 255))
				.required("green", integerParser(0, 255))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.color(Color.fromRGB(
								c.<Integer>get("red"),
								c.<Integer>get("green"),
								c.<Integer>get("blue")
						)),
						LeatherArmorBuilder::leatherArmorBuilder,
						LeatherArmorMeta.class
				));

		final var sLeatherArmorReset = sLeatherArmorColor
				.commandDescription(description("Reset the armor's color."))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.color(null),
						LeatherArmorBuilder::leatherArmorBuilder,
						LeatherArmorMeta.class
				));

		commandManager
				.command(sLeatherArmorSet)
				.command(sLeatherArmorReset);

		final var sMap = parent.literal("map")
				.commandDescription(description("Commands for Maps."))
				.permission(Permission.MAP);

		final var sMapScaling = sMap.literal("scaling")
				.commandDescription(description("Set whether the map is scaling."))
				.required("scaling", booleanParser())
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.scaling(c.<Boolean>get("scaling")),
						MapBuilder::mapBuilder,
						MapMeta.class
				));

		final var sMapColor = sMap.literal("color");

		final var sMapColorSet = sMapColor
				.commandDescription(description("Set the map's color."))
				.required("red", integerParser(0, 255))
				.required("blue", integerParser(0, 255))
				.required("green", integerParser(0, 255))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.color(Color.fromRGB(
								c.<Integer>get("red"),
								c.<Integer>get("green"),
								c.<Integer>get("blue")
						)),
						MapBuilder::mapBuilder,
						MapMeta.class
				));

		final var sMapColorReset = sMapColor
				.commandDescription(description("Reset the map's color."))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.color(null),
						MapBuilder::mapBuilder,
						MapMeta.class
				));

		final var sMapView = sMap.literal("view");

		final var sMapViewCenterX = sMapView.literal("center-x")
				.commandDescription(description("Set the map view's center x."))
				.required("x", integerParser())
				.handler(c -> this.modifyMapView(
						sender(c),
						v -> {
							v.setCenterX(c.<Integer>get("x"));
							return v;
						}
				));

		final var sMapViewCenterZ = sMapView.literal("center-z")
				.commandDescription(description("Set the map view's center z."))
				.required("z", integerParser())
				.handler(c -> this.modifyMapView(
						sender(c),
						v -> {
							v.setCenterZ(c.<Integer>get("z"));
							return v;
						}
				));

		final var sMapViewScale = sMapView.literal("scale")
				.commandDescription(description("Set the map view's scale."))
				.required("scale", enumParser(MapView.Scale.class))
				.handler(c -> this.modifyMapView(
						sender(c),
						v -> {
							v.setScale(c.get("scale"));
							return v;
						}
				));

		final var sMapViewLocked = sMapView.literal("locked")
				.commandDescription(description("Set whether the map view is locked."))
				.required("value", booleanParser())
				.handler(c -> {
					final var sender = c.sender().source();
					this.modifyMapView(
							sender,
							v -> {
								v.setLocked(c.<Boolean>get("boolean"));
								return v;
							}
					);
				});

		final var sMapViewTrackingPosition = sMapView.literal("tracking-position")
				.commandDescription(description("Set whether the map view shows a position cursor."))
				.required("value", booleanParser())
				.handler(c -> {
					final var sender = c.sender().source();
					this.modifyMapView(
							sender,
							v -> {
								v.setTrackingPosition(c.<Boolean>get("boolean"));
								return v;
							}
					);
				});

		final var sMapViewUnlimitedTracking = sMapView.literal("unlimited-tracking")
				.commandDescription(description("Set whether the map view shows off-screen cursors."))
				.required("value", booleanParser())
				.handler(c -> {
					final var sender = c.sender().source();
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
				.command(sMapColorSet)
				.command(sMapColorReset)
				.command(sMapViewCenterX)
				.command(sMapViewCenterZ)
				.command(sMapViewScale)
				.command(sMapViewLocked)
				.command(sMapViewTrackingPosition)
				.command(sMapViewUnlimitedTracking);

		final var sPotion = parent.literal("potion")
				.commandDescription(description("Commands for Potions."))
				.permission(Permission.POTION);

		final var sPotionEffect = sPotion.literal("effect")
				.commandDescription(description("Modify the custom effects."));

		final var sPotionEffectAdd = sPotionEffect.literal("add")
				.commandDescription(description("Add a custom effect."))
				.required("type", registryEntryParser(RegistryKey.MOB_EFFECT, TypeToken.get(PotionEffectType.class)))
				.required("duration", integerParser(0))
				.required("amplifier", integerParser(0, 255))
				.optional("ambient", booleanParser(), constant(false))
				.optional("particles", booleanParser(), constant(true))
				.optional("icon", booleanParser(), constant(true))
				.handler(c -> {
					this.modifySpecial(
							sender(c),
							b -> {
								final var potionEffect = new PotionEffect(
										c.get("type"),
										c.<Integer>get("duration"),
										c.<Integer>get("amplifier"),
										c.<Boolean>get("ambient"),
										c.<Boolean>get("particles"),
										c.<Boolean>get("icon")
								);

								return b.addCustomEffect(potionEffect, true);
							},
							PotionBuilder::potionBuilder,
							PotionMeta.class
					);
				});

		final var sPotionEffectRemove = sPotionEffect.literal("remove")
				.commandDescription(description("Remove a custom effect."))
				.required("type", registryEntryParser(RegistryKey.POTION, TypeToken.get(PotionType.class)))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.removeCustomEffect(c.get("type")),
						PotionBuilder::potionBuilder,
						PotionMeta.class
				));

		final var sPotionEffectClear = sPotionEffect.literal("clear")
				.commandDescription(description("Clear the custom effects."))
				.handler(c -> {
					this.modifySpecial(
							sender(c),
							b -> b.customEffects(null),
							PotionBuilder::potionBuilder,
							PotionMeta.class
					);
				});

		final var sPotionColor = sPotion.literal("color");

		final var sPotionColorSet = sPotionColor
				.commandDescription(description("Set the potion's color."))
				.required("red", integerParser(0, 255))
				.required("blue", integerParser(0, 255))
				.required("green", integerParser(0, 255))
				.handler(c -> {
					this.modifySpecial(
							sender(c),
							b -> b.color(Color.fromRGB(
									c.<Integer>get("red"),
									c.<Integer>get("green"),
									c.<Integer>get("blue")
							)),
							PotionBuilder::potionBuilder,
							PotionMeta.class
					);
				});

		final var sPotionColorReset = sPotionColor
				.commandDescription(description("Reset the potion's color."))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.color(null),
						PotionBuilder::potionBuilder,
						PotionMeta.class
				));

		final var sPotionType = sPotion.literal("type")
				.commandDescription(description("Set the potion type."))
				.required("type", enumParser(PotionType.class))
				.handler(c -> this.modifySpecial(
						sender(c),
						b -> b.basePotionType(c.get("type")),
						PotionBuilder::potionBuilder,
						PotionMeta.class
				));

		commandManager
				.command(sPotionEffectAdd)
				.command(sPotionEffectRemove)
				.command(sPotionEffectClear)
				.command(sPotionColorSet)
				.command(sPotionColorReset)
				.command(sPotionType);

		final var sRepairable = parent.literal("repair") // abnormal name because ends in "able"
				.commandDescription(description("Commands for Repairable items."))
				.permission(Permission.REPAIRABLE);

		final var sRepairableCostSet = sRepairable.literal("cost")
				.commandDescription(description("Set the repair cost."))
				.optional("cost", integerParser(), constant(0))
				.handler(c -> {
					this.modifySpecial(
							sender(c),
							b -> b.repairCost(c.get("cost")),
							RepairableBuilder::repairableBuilder,
							Repairable.class
					);
				});

		commandManager.command(sRepairableCostSet);

		final var sSkull = parent.literal("skull")
				.commandDescription(description("Commands for Skulls."))
				.permission(Permission.SKULL);

		final var sSkullName = sSkull.literal("name")
				.commandDescription(description("Set the owning player. Pass nothing to reset."))
				.optional(
						"name", greedyStringParser(),
						blockingStrings((c, i) ->
								this.iteminator.getServer().getOnlinePlayers().stream().map(Player::getName).toList())
				)
				.handler(c -> {
					final var sender = c.sender().source();
					HeldItemModifier.modifyItemStack(
							sender,
							i -> {
								if (i.getItemMeta() instanceof final SkullMeta skullMeta) {
									// fuk th polic
									// i spent like 50 trillion hours fiddling with
									// get offline player and calling mojang's api, and
									// you know what actually worked? a deprecated method.
									//noinspection deprecation
									skullMeta.setOwner(c.<String>optional("name").orElse(null));
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
				.commandDescription(description("Commands for Suspicious Stews."))
				.permission(Permission.SUSPICIOUS_STEW);

		final var sSuspiciousStewEffect = sSuspiciousStew.literal("effect")
				.commandDescription(description("Modify the custom effects."));

		final var sSuspiciousStewEffectAdd = sSuspiciousStewEffect.literal("add")
				.commandDescription(description("Add a custom effect."))
				.required("type", registryEntryParser(RegistryKey.MOB_EFFECT, TypeToken.get(PotionEffectType.class)))
				.required("duration", integerParser(0))
				.required("amplifier", integerParser(0, 255))
				.optional("ambient", booleanParser(), constant(true))
				.optional("particles", booleanParser(), constant(true))
				.optional("icon", booleanParser(), constant(true))
				.handler(c -> {
					this.modifySpecial(
							sender(c),
							b -> b.addCustomEffect(
									SuspiciousEffectEntry.create(
											c.get("type"),
											c.<Integer>get("duration")
									), true
							),
							SuspiciousStewBuilder::suspiciousStewBuilder,
							SuspiciousStewMeta.class
					);
				});

		final var sSuspiciousStewEffectRemove = sSuspiciousStewEffect.literal("remove")
				.commandDescription(description("Remove a custom effect."))
				.required("type", registryEntryParser(RegistryKey.MOB_EFFECT, TypeToken.get(PotionEffectType.class)))
				.handler(c -> {
					this.modifySpecial(
							sender(c),
							b -> b.removeCustomEffect(c.<PotionEffectType>get("type")),
							SuspiciousStewBuilder::suspiciousStewBuilder,
							SuspiciousStewMeta.class
					);
				});

		final var sSuspiciousStewEffectClear = sSuspiciousStewEffect.literal("clear")
				.commandDescription(description("Clear the custom effects."))
				.handler(c -> {
					this.modifySpecial(
							sender(c),
							b -> b.customEffects(null),
							SuspiciousStewBuilder::suspiciousStewBuilder,
							SuspiciousStewMeta.class
					);
				});

		commandManager
				.command(sSuspiciousStewEffectAdd)
				.command(sSuspiciousStewEffectRemove)
				.command(sSuspiciousStewEffectClear);

		final var sTropicalFishBucket = parent.literal("tropical-fish-bucket")
				.commandDescription(description("Commands for Tropical Fish Buckets."))
				.permission(Permission.TROPICAL_FISH_BUCKET);

		final var sTropicalFishBucketPattern = sTropicalFishBucket.literal("pattern")
				.commandDescription(description("Set the pattern."))
				.required("pattern", enumParser(TropicalFish.Pattern.class))
				.handler(c -> {
					this.modifySpecial(
							sender(c),
							b -> b.pattern(c.get("pattern")),
							TropicalFishBucketBuilder::tropicalFishBucketBuilder,
							TropicalFishBucketMeta.class
					);
				});

		final var sTropicalFishBucketBodyColor = sTropicalFishBucket.literal("body-color")
				.commandDescription(description("Set the body color."))
				.required("body_color", enumParser(DyeColor.class))
				.handler(c -> {
					this.modifySpecial(
							sender(c),
							b -> b.bodyColor(c.get("body_color")),
							TropicalFishBucketBuilder::tropicalFishBucketBuilder,
							TropicalFishBucketMeta.class
					);
				});

		final var sTropicalFishBucketPatternColor = sTropicalFishBucket.literal("pattern-color")
				.commandDescription(description("Set the pattern color."))
				.required("pattern_color", enumParser(DyeColor.class))
				.handler(c -> {
					this.modifySpecial(
							sender(c),
							b -> b.patternColor(c.get("pattern_color")),
							TropicalFishBucketBuilder::tropicalFishBucketBuilder,
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
				NodePath.path("error", "wrong-type"),
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
	private <T extends AbstractItemBuilder<T, ?>> void modifySpecial(
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
				MapBuilder::mapBuilder,
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
