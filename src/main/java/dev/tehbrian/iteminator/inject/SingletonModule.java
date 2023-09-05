package dev.tehbrian.iteminator.inject;

import com.google.inject.AbstractModule;
import dev.tehbrian.iteminator.config.LangConfig;
import dev.tehbrian.iteminator.user.UserService;

public final class SingletonModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(UserService.class).asEagerSingleton();
    this.bind(LangConfig.class).asEagerSingleton();
  }

}
