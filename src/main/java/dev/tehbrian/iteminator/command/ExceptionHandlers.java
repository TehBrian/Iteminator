package dev.tehbrian.iteminator.command;

import cloud.commandframework.exceptions.InvalidCommandSenderException;
import cloud.commandframework.exceptions.InvalidSyntaxException;
import com.google.inject.Inject;
import dev.tehbrian.iteminator.config.LangConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.util.ComponentMessageThrowable;
import org.slf4j.Logger;
import org.spongepowered.configurate.NodePath;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Exception handlers for use with {@link cloud.commandframework.minecraft.extras.MinecraftExceptionHandler}.
 */
public final class ExceptionHandlers {

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

  public Function<Exception, Component> invalidSyntax() {
    return e -> this.langConfig.c(
        NodePath.path("error", "invalid-syntax"),
        Placeholder.component(
            "syntax",
            highlightSpecial(Component.text(String.format("/%s", ((InvalidSyntaxException) e).getCorrectSyntax())))
        )
    );
  }

  public Function<Exception, Component> invalidSender() {
    return e -> this.langConfig.c(
        NodePath.path("error", "invalid-sender"),
        Placeholder.unparsed(
            "type",
            ((InvalidCommandSenderException) e).getRequiredSender().getSimpleName()
        )
    );
  }

  public Function<Exception, Component> noPermission() {
    return e -> this.langConfig.c(NodePath.path("error", "no-permission"));
  }

  public Function<Exception, Component> argumentParsing() {
    return e -> this.langConfig.c(
        NodePath.path("error", "argument-parse"),
        Placeholder.component(
            "cause",
            getMessage(e.getCause())
        )
    );
  }

  public Function<Exception, Component> commandExecution() {
    return e -> {
      final Throwable cause = e.getCause();
      this.logger.warn("Uh oh.", cause);

      final StringWriter writer = new StringWriter();
      cause.printStackTrace(new PrintWriter(writer));
      final String stackTrace = writer.toString().replaceAll("\t", "    ");

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

  private static final Component NULL = Component.text("null");

  private static Component getMessage(final Throwable throwable) {
    final Component msg = ComponentMessageThrowable.getOrConvertMessage(throwable);
    return msg == null ? NULL : msg;
  }

  private static final Pattern SPECIAL_CHARACTERS = Pattern.compile("[^\\s\\w\\-]");

  private Component highlightSpecial(final Component component) {
    final TextColor specialColor = this.langConfig.color(NodePath.path("error", "invalid-syntax-special-color"));
    return component.replaceText(config -> {
      config.match(SPECIAL_CHARACTERS);
      config.replacement(match -> match.color(specialColor));
    });
  }

}
