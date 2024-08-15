package dev.tehbrian.iteminator;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.AudienceProvider;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.minecraft.extras.TestCommand;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.tehbrian.iteminator.command.ExceptionHandlers;
import dev.tehbrian.iteminator.command.IteminatorCommand;
import dev.tehbrian.iteminator.config.LangConfig;
import dev.tehbrian.iteminator.inject.PluginModule;
import dev.tehbrian.iteminator.inject.SingletonModule;
import dev.tehbrian.tehlib.paper.TehPlugin;
import dev.tehbrian.tehlib.paper.configurate.ConfigLoader;
import dev.tehbrian.tehlib.paper.configurate.ConfigLoader.Loadable;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

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
    return new ConfigLoader(this).load(List.of(
        Loadable.of("lang.yml", this.injector.getInstance(LangConfig.class))
    ));
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

    final ExceptionHandlers exceptionHandlers = this.injector.getInstance(ExceptionHandlers.class);

    new MinecraftExceptionHandler<CommandSender>()
        .withHandler(MinecraftExceptionHandler.ExceptionType.ARGUMENT_PARSING, exceptionHandlers.argumentParsing())
        .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SENDER, exceptionHandlers.invalidSender())
        .withHandler(MinecraftExceptionHandler.ExceptionType.INVALID_SYNTAX, exceptionHandlers.invalidSyntax())
        .withHandler(MinecraftExceptionHandler.ExceptionType.NO_PERMISSION, exceptionHandlers.noPermission())
        .withHandler(MinecraftExceptionHandler.ExceptionType.COMMAND_EXECUTION, exceptionHandlers.commandExecution())
        .apply(this.commandManager, AudienceProvider.nativeAudience());

    this.injector.getInstance(IteminatorCommand.class).register(this.commandManager);

    return true;
  }

}
