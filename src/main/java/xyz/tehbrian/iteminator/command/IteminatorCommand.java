package xyz.tehbrian.iteminator.command;

import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.tehbrian.iteminator.command.subs.CommonCommands;
import xyz.tehbrian.iteminator.command.subs.MetaCommands;
import xyz.tehbrian.iteminator.command.subs.SpecialCommands;

public final class IteminatorCommand extends PaperCloudCommand<CommandSender> {

  private final MetaCommands metaCommands;
  private final CommonCommands commonCommands;
  private final SpecialCommands specialCommands;

  @Inject
  public IteminatorCommand(
      final MetaCommands metaCommands,
      final CommonCommands commonCommands,
      final SpecialCommands specialCommands
  ) {
    this.metaCommands = metaCommands;
    this.commonCommands = commonCommands;
    this.specialCommands = specialCommands;
  }

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var cMain = commandManager.commandBuilder("iteminator", "ia")
        .meta(CommandMeta.DESCRIPTION, "The main command for Iteminator.");

    this.metaCommands.registerMeta(commandManager, cMain);

    final var cCommon = cMain
        .senderType(Player.class);
    this.commonCommands.registerCommon(commandManager, cCommon);

    final var cSpecial = cMain.literal("special", "s")
        .meta(CommandMeta.DESCRIPTION, "Commands special to specific item types.")
        .senderType(Player.class);
    this.specialCommands.registerSpecial(commandManager, cSpecial);
  }

}
