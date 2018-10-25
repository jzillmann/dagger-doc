package io.morethan.dagger_example;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;

import io.morethan.dagger_example.domain.user.RegistrationEndpoint;

public class AppTest {

    @Test
    void testApp() throws Exception {
        AppComponent appComponent = DaggerAppComponent.builder()
                .port(8080)
                .build();

        appComponent.lifeCycleController().startAsync().awaitRunning();
        appComponent.server().call(RegistrationEndpoint.class.getSimpleName(), ImmutableMap.of("user", "Greg"));
        appComponent.lifeCycleController().stopAsync().awaitTerminated();
    }

}
