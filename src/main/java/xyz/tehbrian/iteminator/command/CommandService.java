package xyz.tehbrian.iteminator.command;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudService;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.iteminator.Iteminator;

import java.util.function.Function;

public class CommandService extends PaperCloudService<CommandSender> {

    private final Iteminator iteminator;

    /**
     * @param iteminator injected
     */
    @Inject
    public CommandService(
            final @NonNull Iteminator iteminator
    ) {
        this.iteminator = iteminator;
    }

    /**
     * Instantiates {@link #commandManager}.
     *
     * @throws IllegalStateException if {@link #commandManager} is already instantiated
     * @throws Exception             if something goes wrong during instantiation
     */
    public void init() throws Exception {
        if (this.commandManager != null) {
            throw new IllegalStateException("The CommandManager is already instantiated.");
        }

        this.commandManager = new PaperCommandManager<>(
                this.iteminator,
                CommandExecutionCoordinator.simpleCoordinator(),
                Function.identity(),
                Function.identity()
        );
    }

}
