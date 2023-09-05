package dev.tehbrian.iteminator.command;

import cloud.commandframework.exceptions.InvalidCommandSenderException;
import cloud.commandframework.exceptions.InvalidSyntaxException;
import dev.tehbrian.iteminator.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.ComponentMessageThrowable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Exception handlers that return Iteminator-styled components for use with
 * {@link cloud.commandframework.minecraft.extras.MinecraftExceptionHandler}
 */
public final class ExceptionHandlers {

  private static final Component PREFIX = Component.text("I| ").color(Colors.DARK_BLUE);

  public static final Function<Exception, Component> INVALID_SYNTAX =
      e -> PREFIX
          .append(Component.text("Invalid command syntax. Correct syntax is: ", Colors.RED))
          .append(highlightSpecial(Component.text(
              String.format("/%s", ((InvalidSyntaxException) e).getCorrectSyntax()),
              Colors.LIGHT_GRAY
          )));

  public static final Function<Exception, Component> INVALID_SENDER =
      e -> PREFIX
          .append(Component.text("Invalid command sender. You must be of type ", Colors.RED))
          .append(Component.text(
              ((InvalidCommandSenderException) e).getRequiredSender().getSimpleName(),
              Colors.LIGHT_GRAY
          ))
          .append(Component.text(" to run this command.", Colors.RED));

  public static final Function<Exception, Component> NO_PERMISSION =
      e -> PREFIX
          .append(Component.text("You don't have permission to do that.", Colors.RED));

  public static final Function<Exception, Component> ARGUMENT_PARSE =
      e -> PREFIX
          .append(Component.text("Invalid command argument: ", Colors.RED))
          .append(getMessage(e.getCause()).colorIfAbsent(Colors.LIGHT_GRAY));

  public static final Function<Exception, Component> COMMAND_EXECUTION =
      e -> {
        final Throwable cause = e.getCause();
        cause.printStackTrace();

        final StringWriter writer = new StringWriter();
        cause.printStackTrace(new PrintWriter(writer));
        final String stackTrace = writer.toString().replaceAll("\t", "    ");
        final HoverEvent<Component> hover = HoverEvent.showText(
            Component.text()
                .append(getMessage(cause))
                .append(Component.newline())
                .append(Component.text(stackTrace))
                .append(Component.newline())
                .append(Component.text("Click to copy", Colors.LIGHT_GRAY, TextDecoration.ITALIC))
        );
        final ClickEvent click = ClickEvent.copyToClipboard(stackTrace);
        return PREFIX.append(Component.text()
            .content("An internal error occurred while attempting to perform this command.")
            .color(Colors.RED)
            .hoverEvent(hover)
            .clickEvent(click)
            .build());
      };

  private ExceptionHandlers() {
  }

  private static final Component NULL = Component.text("null");

  private static Component getMessage(final Throwable throwable) {
    final Component msg = ComponentMessageThrowable.getOrConvertMessage(throwable);
    return msg == null ? NULL : msg;
  }

  private static final Pattern SPECIAL_CHARACTERS = Pattern.compile("[^\\s\\w\\-]");

  private static Component highlightSpecial(final Component component) {
    return component.replaceText(config -> {
      config.match(SPECIAL_CHARACTERS);
      config.replacement(match -> match.color(Colors.WHITE));
    });
  }

}
