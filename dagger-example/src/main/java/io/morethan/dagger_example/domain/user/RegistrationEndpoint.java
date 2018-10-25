package io.morethan.dagger_example.domain.user;

import java.util.Map;

import io.morethan.dagger_example.server.ServerEndpoint;

public class RegistrationEndpoint extends ServerEndpoint {

    private final RegistrationManager _registrationManager;

    public RegistrationEndpoint(RegistrationManager registrationManager) {
        _registrationManager = registrationManager;
    }

    @Override
    public void call(Map<String, String> parameters) {
        _registrationManager.registerUser(parameters.get("user"));
    }

}
