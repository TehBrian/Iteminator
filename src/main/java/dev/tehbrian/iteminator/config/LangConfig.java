package dev.tehbrian.iteminator.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.agna.paper.configurate.AbstractLangConfig;
import dev.tehbrian.agna.paper.configurate.NoSuchValueInConfigException;
import net.kyori.adventure.text.format.TextColor;
import org.spongepowered.configurate.NodePath;

import java.nio.file.Path;

public final class LangConfig extends AbstractLangConfig<HoconConfigurateWrapper> {

	/**
	 * @param dataFolder the data folder
	 */
	@Inject
	public LangConfig(final @Named("dataFolder") Path dataFolder) {
		super(new HoconConfigurateWrapper(dataFolder.resolve("lang.hocon")));
	}

	public TextColor color(final NodePath path) throws NoSuchValueInConfigException {
		return this.c(path).color();
	}

}
