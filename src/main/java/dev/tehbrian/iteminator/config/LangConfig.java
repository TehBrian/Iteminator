package dev.tehbrian.iteminator.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.tehlib.paper.configurate.AbstractLangConfig;
import dev.tehbrian.tehlib.paper.configurate.NoSuchValueInConfigException;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.spongepowered.configurate.NodePath;

import java.nio.file.Path;

public final class LangConfig extends AbstractLangConfig<YamlConfigurateWrapper> {

  /**
   * @param dataFolder the data folder
   */
  @Inject
  public LangConfig(final @Named("dataFolder") Path dataFolder) {
    super(new YamlConfigurateWrapper(dataFolder.resolve("lang.yml")));
  }

  public TextColor color(final NodePath path) throws NoSuchValueInConfigException {
    return this.c(path, TagResolver.empty()).color();
  }

}
