package xyz.tehbrian.iteminator.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.captions.Caption;
import cloud.commandframework.captions.CaptionVariable;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import cloud.commandframework.exceptions.parsing.ParserException;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiFunction;

/**
 * cloud argument type that parses Bukkit {@link PotionEffectType}s
 *
 * @param <C> Command sender type
 */
public class PotionEffectTypeArgument<C> extends CommandArgument<C, PotionEffectType> {

    protected PotionEffectTypeArgument(
            final boolean required,
            final @NonNull String name,
            final @NonNull String defaultValue,
            final @Nullable BiFunction<@NonNull CommandContext<C>, @NonNull String,
                    @NonNull List<@NonNull String>> suggestionsProvider,
            final @NonNull ArgumentDescription defaultDescription
    ) {
        super(
                required,
                name,
                new PotionEffectTypeParser<>(),
                defaultValue,
                PotionEffectType.class,
                suggestionsProvider,
                defaultDescription
        );
    }

    /**
     * Create a new builder
     *
     * @param name Name of the argument
     * @param <C>  Command sender type
     * @return Created builder
     */
    public static <C> PotionEffectTypeArgument.@NonNull Builder<C> newBuilder(final @NonNull String name) {
        return new PotionEffectTypeArgument.Builder<>(name);
    }

    /**
     * Create a new required argument
     *
     * @param name Argument name
     * @param <C>  Command sender type
     * @return Created argument
     */
    public static <C> @NonNull CommandArgument<C, PotionEffectType> of(final @NonNull String name) {
        return PotionEffectTypeArgument.<C>newBuilder(name).asRequired().build();
    }

    /**
     * Create a new optional argument
     *
     * @param name Argument name
     * @param <C>  Command sender type
     * @return Created argument
     */
    public static <C> @NonNull CommandArgument<C, PotionEffectType> optional(final @NonNull String name) {
        return PotionEffectTypeArgument.<C>newBuilder(name).asOptional().build();
    }

    /**
     * Create a new optional argument with a default value
     *
     * @param name             Argument name
     * @param potionEffectType Default value
     * @param <C>              Command sender type
     * @return Created argument
     */
    public static <C> @NonNull CommandArgument<C, PotionEffectType> optional(
            final @NonNull String name,
            final @NonNull PotionEffectType potionEffectType
    ) {
        return PotionEffectTypeArgument.<C>newBuilder(name).asOptionalWithDefault(potionEffectType.getName()).build();
    }

    public static final class Builder<C> extends CommandArgument.Builder<C, PotionEffectType> {

        private Builder(final @NonNull String name) {
            super(PotionEffectType.class, name);
        }

        @Override
        public @NonNull CommandArgument<C, PotionEffectType> build() {
            return new PotionEffectTypeArgument<>(
                    this.isRequired(),
                    this.getName(),
                    this.getDefaultValue(),
                    this.getSuggestionsProvider(),
                    this.getDefaultDescription()
            );
        }

    }

    public static final class PotionEffectTypeParser<C> implements ArgumentParser<C, PotionEffectType> {

        @Override
        public @NonNull ArgumentParseResult<PotionEffectType> parse(
                final @NonNull CommandContext<C> commandContext,
                final @NonNull Queue<@NonNull String> inputQueue
        ) {
            final String input = inputQueue.peek();
            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(
                        PotionEffectTypeParser.class,
                        commandContext
                ));
            }

            final PotionEffectType potionEffectType = PotionEffectType.getByName(input);
            if (potionEffectType == null) {
                return ArgumentParseResult.failure(new PotionEffectTypeParseException(input, commandContext));
            }
            inputQueue.remove();
            return ArgumentParseResult.success(potionEffectType);
        }

        @Override
        public @NonNull List<@NonNull String> suggestions(
                final @NonNull CommandContext<C> commandContext,
                final @NonNull String input
        ) {
            final List<String> completions = new ArrayList<>();
            for (final PotionEffectType value : PotionEffectType.values()) {
                completions.add(value.getName());
            }
            return completions;
        }

    }


    public static final class PotionEffectTypeParseException extends ParserException {

        private static final long serialVersionUID = -4591509689484989301L;
        private final String input;

        /**
         * Construct a new PotionEffectTypeParseException
         *
         * @param input   Input
         * @param context Command context
         */
        public PotionEffectTypeParseException(
                final @NonNull String input,
                final @NonNull CommandContext<?> context
        ) {
            super(
                    PotionEffectTypeParser.class,
                    context,
                    Caption.of("argument.parse.failure.potioneffecttype"), // TODO currently doesn't resolve - put in BukkitCaptionKeys if pring to cloud
                    CaptionVariable.of("input", input)
            );
            this.input = input;
        }

        /**
         * Get the input
         *
         * @return Input
         */
        public @NonNull String getInput() {
            return this.input;
        }

    }

}
