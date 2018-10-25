package io.morethan.dagger_example.server;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import dagger.Module;
import dagger.Provides;
import io.morethan.daggerdoc.ModuleDoc;

@Module
@ModuleDoc(category = "UI Access Layer")
public class ServerModule {

    private static final Logger LOG = LoggerFactory.getLogger(ServerModule.class);

    @Provides
    @Singleton
    public Server server(@ServerPort int port, Set<ServerEndpoint> endpoints) {
        ImmutableMap.Builder<String, ServerEndpoint> endpointMap = ImmutableMap.builder();
        LOG.info("Registering endpoints");
        for (ServerEndpoint endpoint : endpoints) {
            LOG.info("  - '{}' ", endpoint.type());
            endpointMap.put(endpoint.type(), endpoint);
        }
        return new Server(port, endpointMap.build());
    }

    @Qualifier
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface ServerPort {

    }
}
