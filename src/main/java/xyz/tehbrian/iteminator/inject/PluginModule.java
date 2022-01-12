package xyz.tehbrian.iteminator.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.iteminator.Iteminator;

import java.nio.file.Path;

@SuppressWarnings("unused")
public final class PluginModule extends AbstractModule {

    private final Iteminator iteminator;

    /**
     * @param iteminator Iteminator reference
     */
    public PluginModule(final @NonNull Iteminator iteminator) {
        this.iteminator = iteminator;
    }

    @Override
    protected void configure() {
        this.bind(Iteminator.class).toInstance(this.iteminator);
        this.bind(JavaPlugin.class).toInstance(this.iteminator);
    }

    /**
     * @return the plugin's Log4J logger
     */
    @Provides
    public @NonNull Logger provideLog4JLogger() {
        return this.iteminator.getLog4JLogger();
    }

    /**
     * @return the data folder
     */
    @Provides
    @Named("dataFolder")
    public @NonNull Path provideDataFolder() {
        return this.iteminator.getDataFolder().toPath();
    }

}
