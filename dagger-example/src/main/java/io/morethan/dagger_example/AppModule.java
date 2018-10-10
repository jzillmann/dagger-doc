package io.morethan.dagger_example;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.morethan.dagger_example.server.Server;
import io.morethan.dagger_example.server.ServerModule;
import io.morethan.dagger_example.server.ServerService;
import io.morethan.dagger_example.service.ServiceModule;

@Module(includes = { ServerModule.class, ServiceModule.class })
public class AppModule {

    @Provides
    @Singleton
    @IntoSet
    ServerService extraService() {
        return new ServerService("extraService");
    }

    @Provides
    @Singleton
    App app(Server server) {
        return new App(server);
    }
}
