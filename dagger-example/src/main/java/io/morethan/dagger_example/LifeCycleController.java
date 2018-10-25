package io.morethan.dagger_example;

import com.google.common.util.concurrent.AbstractIdleService;

import io.morethan.dagger_example.exec.ExecutionService;
import io.morethan.dagger_example.mail.MailService;
import io.morethan.dagger_example.persistence.PersistenceService;
import io.morethan.dagger_example.server.Server;

public class LifeCycleController extends AbstractIdleService {

    private final Server _server;
    private final PersistenceService _persistenceService;
    private final ExecutionService _executionService;
    private final MailService _mailService;

    public LifeCycleController(Server server, PersistenceService persistenceService, ExecutionService executionService, MailService mailService) {
        _server = server;
        _persistenceService = persistenceService;
        _executionService = executionService;
        _mailService = mailService;
    }

    @Override
    protected void startUp() throws Exception {
        _mailService.startAsync().awaitRunning();
        _persistenceService.startAsync().awaitRunning();
        _executionService.startAsync().awaitRunning();
        _server.startAsync().awaitRunning();
    }

    @Override
    protected void shutDown() throws Exception {
        _server.stopAsync().awaitTerminated();
        _persistenceService.stopAsync().awaitTerminated();
        _executionService.stopAsync().awaitTerminated();
        _mailService.stopAsync().awaitTerminated();
    }

}
