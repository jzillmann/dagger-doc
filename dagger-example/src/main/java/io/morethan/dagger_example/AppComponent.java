package io.morethan.dagger_example;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import io.morethan.dagger_example.server.ServerModule.ServerPort;

@Component(modules = { AppModule.class })
@Singleton
public interface AppComponent {

    App app();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder port(@ServerPort int port);

        AppComponent build();
    }
}
