package xyz.tehbrian.iteminator.command;

import broccolai.corn.paper.item.PaperItemBuilder;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.BooleanArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.parsers.EnchantmentArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.iteminator.FormatUtil;
import xyz.tehbrian.iteminator.Iteminator;
import xyz.tehbrian.iteminator.Permissions;
import xyz.tehbrian.iteminator.config.LangConfig;
import xyz.tehbrian.iteminator.user.UserService;

import java.util.ArrayList;
import java.util.List;
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
                .handler(c -> {
                    c.getSender().sendMessage(this.langConfig.c(NodePath.path("main")));
                });

        final var cReload = cMain.literal("reload")
                .meta(CommandMeta.DESCRIPTION, "Reloads the plugin's config.")
                .permission(Permissions.RELOAD)
                .handler(c -> {
                    if (this.iteminator.loadConfiguration()) {
                        c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "successful")));
                    } else {
                        c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "unsuccessful")));
                    }
                });

        final var cName = cMain.literal("name")
                .meta(CommandMeta.DESCRIPTION, "Sets the name.")
                .senderType(Player.class)
                .argument(StringArgument.greedy("text"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.name(this.translateWithUserFormat(c.get("text"), sender)));
                });

        final var cAmount = cMain.literal("amount")
                .meta(CommandMeta.DESCRIPTION, "Sets the amount.")
                .senderType(Player.class)
                .argument(IntegerArgument.<CommandSender>newBuilder("amount").withMin(0).withMax(127))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.amount(c.get("amount")));
                });

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
                            sender.sendMessage(this.langConfig.c(NodePath.path("todo")));
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
                            sender.sendMessage(this.langConfig.c(NodePath.path("todo")));
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
                    this.modify(sender, b -> b.lore(new ArrayList<>()));
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
                    this.modify(sender, b -> b.addEnchant(c.get("type"), c.get("level")));
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
                    this.modify(sender, b -> {
                        return b/*.enchants(Map.of()) blocked due to BROCOLI*/;
                    });
                });

        final var cUnbreakable = cMain.literal("unbreakable")
                .meta(CommandMeta.DESCRIPTION, "Set whether the item is unbreakable.")
                .senderType(Player.class)
                .argument(BooleanArgument.of("boolean"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    this.modify(sender, b -> b.unbreakable(c.get("boolean")));
                });

        commandManager.command(cMain)
                .command(cReload)
                .command(cName)
                .command(cAmount)
                .command(cLoreAdd)
                .command(cLoreSet)
                .command(cLoreRemove)
                .command(cLoreClear)
                .command(cEnchantmentAdd)
                .command(cEnchantmentRemove)
                .command(cEnchantmentClear)
                .command(cUnbreakable);
    }

    private @NonNull Component translateWithUserFormat(final @NonNull String string, final @NonNull Player player) {
        return switch (this.userService.getUser(player.getUniqueId()).formattingType()) {
            case LEGACY -> FormatUtil.legacy(string);
            case MINI_MESSAGE -> FormatUtil.miniMessage(string);
        };
    }

    /**
     * Applies the given operator to the item in the player's main hand.
     * If the operator returns null, nothing will happen to the item.
     *
     * @param player   which player to target
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
     * @param player   which player to target
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
