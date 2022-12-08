package xyz.tehbrian.iteminator.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import xyz.tehbrian.iteminator.Iteminator;

import java.nio.file.Path;

@SuppressWarnings("unused")
public final class PluginModule extends AbstractModule {

  private final Iteminator iteminator;

  public PluginModule(final Iteminator iteminator) {
    this.iteminator = iteminator;
  }

  @Override
  protected void configure() {
    this.bind(Iteminator.class).toInstance(this.iteminator);
    this.bind(JavaPlugin.class).toInstance(this.iteminator);
  }

  /**
   * @return the plugin's SLF4J logger
   */
  @Provides
  public Logger provideSLF4JLogger() {
    return this.iteminator.getSLF4JLogger();
  }

  /**
   * @return the plugin's data folder
   */
  @Provides
  @Named("dataFolder")
  public Path provideDataFolder() {
    return this.iteminator.getDataFolder().toPath();
  }

}
