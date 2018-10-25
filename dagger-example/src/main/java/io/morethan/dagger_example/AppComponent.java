package io.morethan.dagger_example;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import io.morethan.dagger_example.domain.chat.ChatModule;
import io.morethan.dagger_example.domain.user.UserModule;
import io.morethan.dagger_example.server.Server;
import io.morethan.dagger_example.server.ServerModule.ServerPort;

@Component(modules = {
        AppModule.class,
        UserModule.class,
        ChatModule.class
})
@Singleton
public interface AppComponent {

    LifeCycleController lifeCycleController();

    Server server();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder port(@ServerPort int port);

        AppComponent build();
    }
}
