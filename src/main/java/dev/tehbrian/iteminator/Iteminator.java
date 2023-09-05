package dev.tehbrian.iteminator;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.AudienceProvider;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.tehbrian.iteminator.command.ExceptionHandlers;
import dev.tehbrian.iteminator.command.IteminatorCommand;
import dev.tehbrian.iteminator.config.LangConfig;
import dev.tehbrian.iteminator.inject.PluginModule;
import dev.tehbrian.iteminator.inject.SingletonModule;
import dev.tehbrian.tehlib.configurate.Config;
import dev.tehbrian.tehlib.paper.TehPlugin;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.spongepowered.configurate.ConfigurateException;

import java.util.List;
import java.util.function.Function;

public final class Iteminator extends TehPlugin {

  private @MonotonicNonNull PaperCommandManager<CommandSender> commandManager;
  private @MonotonicNonNull Injector injector;

  @Override
  public void onEnable() {
    try {
      this.injector = Guice.createInjector(
          new PluginModule(this),
          new SingletonModule()
      );
    } catch (final Exception e) {
      this.getSLF4JLogger().error("Something went wrong while creating the Guice injector.");
      this.getSLF4JLogger().error("Disabling plugin.");
      this.disableSelf();
      this.getSLF4JLogger().error("Printing stack trace, please send this to the developers:", e);
      return;
    }

    if (!this.loadConfiguration()) {
      this.disableSelf();
      return;
    }
    if (!this.setupCommands()) {
      this.disableSelf();
    }
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
        this.getSLF4JLogger().error(
            "Exception caught during config load for {}",
            config.configurateWrapper().filePath()
        );
        this.getSLF4JLogger().error("Please check your config.");
        this.getSLF4JLogger().error("Printing stack trace:", e);
        return false;
      }
    }

    this.getSLF4JLogger().info("Successfully loaded configuration.");
    return true;
  }

  /**
   * @return whether it was successful
   */
  private boolean setupCommands() {
    if (this.commandManager != null) {
      throw new IllegalStateException("The CommandManager is already instantiated.");
    }

    try {
      this.commandManager = new PaperCommandManager<>(
          this,
          CommandExecutionCoordinator.simpleCoordinator(),
          Function.identity(),
          Function.identity()
      );
    } catch (final Exception e) {
      this.getSLF4JLogger().error("Failed to create the CommandManager.");
      this.getSLF4JLogger().error("Printing stack trace, please send this to the developers:", e);
      return false;
    }

    new MinecraftExceptionHandler<CommandSender>()
        .withHandler(MinecraftExceptionHandler.ExceptionType.ARGUMENT_PARSING, ExceptionHandlers.ARGUMENT_PARSE)
        .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SENDER, ExceptionHandlers.INVALID_SENDER)
        .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SYNTAX, ExceptionHandlers.INVALID_SYNTAX)
        .withHandler(MinecraftExceptionHandler.ExceptionType.NO_PERMISSION, ExceptionHandlers.NO_PERMISSION)
        .withHandler(MinecraftExceptionHandler.ExceptionType.COMMAND_EXECUTION, ExceptionHandlers.COMMAND_EXECUTION)
        .apply(this.commandManager, AudienceProvider.nativeAudience());

    this.injector.getInstance(IteminatorCommand.class).register(this.commandManager);

    return true;
  }

}
