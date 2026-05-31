package dev.tehbrian.iteminator;

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

import static org.incendo.cloud.execution.ExecutionCoordinator.simpleCoordinator;
import static org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper.simpleSenderMapper;

public final class Iteminator extends TehPlugin {

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
			this.disableSelf();
			this.getSLF4JLogger().error("Printing stack trace. Please send this to the developers", e);
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
