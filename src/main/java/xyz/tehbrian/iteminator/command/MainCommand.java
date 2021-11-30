package xyz.tehbrian.iteminator.command;

import broccolai.corn.paper.item.PaperItemBuilder;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.iteminator.FormatUtil;
import xyz.tehbrian.iteminator.Iteminator;
import xyz.tehbrian.iteminator.Permissions;
import xyz.tehbrian.iteminator.config.LangConfig;
import xyz.tehbrian.iteminator.user.UserService;

import java.util.function.UnaryOperator;

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
        final var main = commandManager.commandBuilder("iteminator", ArgumentDescription.of("The main command for Iteminator."))
                .handler(c -> {
                    c.getSender().sendMessage(this.langConfig.c(NodePath.path("main")));
                });

        final var reload = main.literal("reload")
                .meta(CommandMeta.DESCRIPTION, "Reloads the plugin's config.")
                .permission(Permissions.RELOAD)
                .handler(c -> {
                    if (this.iteminator.loadConfiguration()) {
                        c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "successful")));
                    } else {
                        c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "unsuccessful")));
                    }
                });

        final var name = main.literal("name")
                .meta(CommandMeta.DESCRIPTION, "Sets the name.")
                .argument(StringArgument.greedy("name"))
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();

                    this.replaceItemInMainHand(
                            i -> PaperItemBuilder.of(i).name(this.translateWithUserFormat(c.get("name"), sender)).build(),
                            sender
                    );
                });

        final var amount = main.literal("amount")
                .meta(CommandMeta.DESCRIPTION, "Sets the amount.")
                .argument(IntegerArgument.of("amount"))
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();

                    this.replaceItemInMainHand(
                            i -> PaperItemBuilder.of(i).amount(c.get("amount")).build(),
                            sender
                    );
                });

        commandManager.command(main)
                .command(reload)
                .command(name)
                .command(amount);
    }

    private @NonNull Component translateWithUserFormat(final @NonNull String string, final @NonNull Player player) {
        return switch (this.userService.getUser(player.getUniqueId()).formattingType()) {
            case LEGACY -> FormatUtil.legacy(string);
            case MINI_MESSAGE -> FormatUtil.miniMessage(string);
        };
    }

    private void replaceItemInMainHand(final @NonNull UnaryOperator<@NonNull ItemStack> operator, final @NonNull Player player) {
        final @NonNull PlayerInventory inventory = player.getInventory();
        final @NonNull ItemStack item = inventory.getItemInMainHand();
        if (item.getItemMeta() == null) {
            return;
        }
        inventory.setItemInMainHand(operator.apply(item));
    }

}
