package xyz.tehbrian.iteminator;

import cloud.commandframework.minecraft.extras.AudienceProvider;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.tehbrian.tehlib.core.configurate.Config;
import dev.tehbrian.tehlib.paper.TehPlugin;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import xyz.tehbrian.iteminator.command.CommandService;
import xyz.tehbrian.iteminator.command.IteminatorCommand;
import xyz.tehbrian.iteminator.config.LangConfig;
import xyz.tehbrian.iteminator.inject.PluginModule;
import xyz.tehbrian.iteminator.inject.SingletonModule;

import java.util.List;

public final class Iteminator extends TehPlugin {

    private @MonotonicNonNull Injector injector;

    @Override
    public void onEnable() {
        try {
            this.injector = Guice.createInjector(
                    new PluginModule(this),
                    new SingletonModule()
            );
        } catch (final Exception e) {
            this.getLog4JLogger().error("Something went wrong while creating the Guice injector.");
            this.getLog4JLogger().error("Disabling plugin.");
            this.setEnabled(false);
            this.getLog4JLogger().error("Printing stack trace, please send this to the developers:", e);
            return;
        }

        if (!this.loadConfiguration()) {
            this.disableSelf();
            return;
        }
        if (!this.setupCommands()) {
            this.disableSelf();
            return;
        }

        this.getLog4JLogger().info("Successfully started the plugin.");
    }

    /**
     * @return whether it was successful
     */
    public boolean loadConfiguration() {
        this.saveResourceSilently("lang.yml");

        final List<Config> configsToLoad = List.of(
                this.injector.getInstance(LangConfig.class)
        );

        for (final Config config : configsToLoad) {
            try {
                config.load();
            } catch (final ConfigurateException e) {
                this.getLog4JLogger().error("Exception caught during config load for {}", config.configurateWrapper().filePath());
                this.getLog4JLogger().error("Please check your config.");
                this.getLog4JLogger().error("Printing stack trace:", e);
                return false;
            }
        }

        this.getLog4JLogger().info("Successfully loaded configuration.");
        return true;
    }

    /**
     * @return whether it was successful
     */
    private boolean setupCommands() {
        final @NonNull CommandService commandService = this.injector.getInstance(CommandService.class);
        try {
            commandService.init();
        } catch (final Exception e) {
            this.getLog4JLogger().error("Failed to create the CommandManager.");
            this.getLog4JLogger().error("Printing stack trace, please send this to the developers:", e);
            return false;
        }

        final @Nullable PaperCommandManager<CommandSender> commandManager = commandService.get();
        if (commandManager == null) {
            this.getLog4JLogger().error("The CommandService was null after initialization!");
            return false;
        }

        new MinecraftExceptionHandler<CommandSender>()
                .withArgumentParsingHandler()
                .withInvalidSenderHandler()
                .withInvalidSyntaxHandler()
                .withNoPermissionHandler()
                .withCommandExecutionHandler()
                .apply(commandManager, AudienceProvider.nativeAudience());

        this.injector.getInstance(IteminatorCommand.class).register(commandManager);

        return true;
    }

}
