package xyz.tehbrian.iteminator.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.iteminator.command.CommandService;
import xyz.tehbrian.iteminator.config.LangConfig;
import xyz.tehbrian.iteminator.user.UserService;

public class SingletonModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(CommandService.class).asEagerSingleton();
        this.bind(UserService.class).asEagerSingleton();
        this.bind(LangConfig.class).asEagerSingleton();
    }

}
