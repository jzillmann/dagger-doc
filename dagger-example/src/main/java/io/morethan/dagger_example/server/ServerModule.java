package io.morethan.dagger_example.server;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServerModule {

    @Provides
    @Singleton
    public Server server(@ServerPort int port, Set<ServerService> services) {
        Server myServer = new Server(port);
        for (ServerService service : services) {
            myServer.add(service);
        }
        return myServer;
    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface ServerPort {

    }
}
