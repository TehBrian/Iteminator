package dev.tehbrian.iteminator.config;

import dev.tehbrian.agna.configurate.ConfigurateWrapper;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;

public class HoconConfigurateWrapper extends ConfigurateWrapper<HoconConfigurationLoader> {

	/**
	 * @param filePath the file path for the config
	 */
	public HoconConfigurateWrapper(final Path filePath) {
		super(
				filePath, HoconConfigurationLoader.builder()
						.path(filePath)
						.build()
		);
	}

	/**
	 * @param filePath the file path for the config
	 * @param loader   the loader
	 */
	public HoconConfigurateWrapper(final Path filePath, final HoconConfigurationLoader loader) {
		super(filePath, loader);
	}

}
