package dev.tehbrian.iteminator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.tehbrian.agna.paper.UpdateChecker;
import dev.tehbrian.agna.paper.configurate.ConfigLoader;
import dev.tehbrian.agna.paper.configurate.ConfigLoader.Loadable;
import dev.tehbrian.iteminator.command.ExceptionHandlers;
import dev.tehbrian.iteminator.command.IteminatorCommand;
import dev.tehbrian.iteminator.config.LangConfig;
import dev.tehbrian.iteminator.inject.PluginModule;
import dev.tehbrian.iteminator.inject.SingletonModule;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.incendo.cloud.exception.ArgumentParseException;
import org.incendo.cloud.exception.CommandExecutionException;
import org.incendo.cloud.exception.InvalidCommandSenderException;
import org.incendo.cloud.exception.InvalidSyntaxException;
import org.incendo.cloud.exception.NoPermissionException;
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;

import java.util.List;

import static dev.tehbrian.agna.paper.PluginUtils.disableSelf;
import static org.incendo.cloud.execution.ExecutionCoordinator.simpleCoordinator;
import static org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper.simpleSenderMapper;

public final class Iteminator extends JavaPlugin {

	private static final int BSTATS_PLUGIN_ID = 31731;

	private @MonotonicNonNull PaperCommandManager<Source> commandManager;
	private @MonotonicNonNull Injector injector;

	@Override
	public void onEnable() {
		try {
			this.injector = Guice.createInjector(
					new PluginModule(this),
					new SingletonModule()
			);
		} catch (final Exception e) {
			this.getSLF4JLogger().error("Something went wrong while creating the injector. Disabling plugin");
			disableSelf(this);
			this.getSLF4JLogger().error("Printing stack trace. Please send this to the developers", e);
			return;
		}

		if (!this.loadConfiguration()) {
			disableSelf(this);
			return;
		}

		if (!this.setupCommands()) {
			disableSelf(this);
			return;
		}

		// initialize bStats.
		Metrics _ = new Metrics(this, BSTATS_PLUGIN_ID);

		new UpdateChecker(this, "iteminator").checkForUpdates();
	}

	/**
	 * @return whether it was successful
	 */
	public boolean loadConfiguration() {
		return new ConfigLoader(this).load(List.of(
				Loadable.ofVersioned("lang.hocon", this.injector.getInstance(LangConfig.class), 1)
		));
	}

	/**
	 * @return whether it was successful
	 */
	private boolean setupCommands() {
		if (this.commandManager != null) {
			throw new IllegalStateException("The CommandManager is already instantiated");
		}

		this.commandManager = PaperCommandManager
				.builder(simpleSenderMapper())
				.executionCoordinator(simpleCoordinator())
				.buildOnEnable(this);

		final ExceptionHandlers exceptionHandlers = this.injector.getInstance(ExceptionHandlers.class);

		MinecraftExceptionHandler.create(Source::source)
				.handler(ArgumentParseException.class, exceptionHandlers.argumentParsing())
				.handler(InvalidCommandSenderException.class, exceptionHandlers.invalidSender())
				.handler(InvalidSyntaxException.class, exceptionHandlers.invalidSyntax())
				.handler(NoPermissionException.class, exceptionHandlers.noPermission())
				.handler(CommandExecutionException.class, exceptionHandlers.commandExecution())
				.registerTo(this.commandManager);

		this.injector.getInstance(IteminatorCommand.class).register(this.commandManager);

		return true;
	}

}
