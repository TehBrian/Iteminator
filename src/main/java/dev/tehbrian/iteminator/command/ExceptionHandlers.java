package dev.tehbrian.iteminator.command;

import com.google.inject.Inject;
import dev.tehbrian.iteminator.config.LangConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.util.ComponentMessageThrowable;
import org.incendo.cloud.exception.ArgumentParseException;
import org.incendo.cloud.exception.CommandExecutionException;
import org.incendo.cloud.exception.InvalidCommandSenderException;
import org.incendo.cloud.exception.InvalidSyntaxException;
import org.incendo.cloud.exception.NoPermissionException;
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler.MessageFactory;
import org.incendo.cloud.paper.util.sender.Source;
import org.slf4j.Logger;
import org.spongepowered.configurate.NodePath;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;

/**
 * Exception handlers for use with {@link org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler}.
 */
public final class ExceptionHandlers {

	private static final Component NULL = Component.text("null");
	private static final Pattern SPECIAL_CHARACTERS = Pattern.compile("[^\\s\\w\\-]");

	private final LangConfig langConfig;
	private final Logger logger;

	@Inject
	public ExceptionHandlers(
			final LangConfig langConfig,
			final Logger logger
	) {
		this.langConfig = langConfig;
		this.logger = logger;
	}

	private static Component getMessage(final Throwable throwable) {
		final Component msg = ComponentMessageThrowable.getOrConvertMessage(throwable);
		return msg == null ? NULL : msg;
	}

	public MessageFactory<Source, InvalidSyntaxException> invalidSyntax() {
		return (a, e) -> this.langConfig.c(
				NodePath.path("error", "invalid-syntax"),
				Placeholder.component(
						"syntax",
						this.highlightSpecial(Component.text(String.format("/%s", e.exception().correctSyntax())))
				)
		);
	}

	public MessageFactory<Source, InvalidCommandSenderException> invalidSender() {
		return (a, e) -> this.langConfig.c(
				NodePath.path("error", "invalid-sender"),
				Placeholder.unparsed(
						"type",
						String.join(", ", e.exception().requiredSenderTypes().stream().map(Object::toString).toList())
				)
		);
	}

	public MessageFactory<Source, NoPermissionException> noPermission() {
		return (a, e) -> this.langConfig.c(NodePath.path("error", "no-permission"));
	}

	public MessageFactory<Source, ArgumentParseException> argumentParsing() {
		return (a, e) -> this.langConfig.c(
				NodePath.path("error", "argument-parse"),
				Placeholder.component("cause", getMessage(e.exception().getCause()))
		);
	}

	public MessageFactory<Source, CommandExecutionException> commandExecution() {
		return (a, e) -> {
			final Throwable cause = e.exception().getCause();
			this.logger.warn("An exception occurred during command execution", cause);

			final StringWriter writer = new StringWriter();
			cause.printStackTrace(new PrintWriter(writer));
			final String stackTrace = writer.toString().replace("\t", "    ");

			final HoverEvent<Component> hover = HoverEvent.showText(
					this.langConfig.c(
							NodePath.path("error", "command-execution-hover"),
							TagResolver.resolver(
									Placeholder.component("cause", getMessage(cause)),
									Placeholder.unparsed("stacktrace", stackTrace)
							)
					)
			);

			final ClickEvent click = ClickEvent.copyToClipboard(stackTrace);

			return this.langConfig.c(NodePath.path("error", "command-execution"))
					.hoverEvent(hover)
					.clickEvent(click);
		};
	}

	private Component highlightSpecial(final Component component) {
		final TextColor specialColor = this.langConfig.color(NodePath.path("error", "invalid-syntax-special-color"));
		return component.replaceText(config -> {
			config.match(SPECIAL_CHARACTERS);
			config.replacement(match -> match.color(specialColor));
		});
	}

}
