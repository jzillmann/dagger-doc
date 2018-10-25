package io.morethan.dagger_example.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractIdleService;

import io.morethan.dagger_example.mail.MailService;

public class ExecutionService extends AbstractIdleService {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionService.class);

    private final MailService _mailService;

    public ExecutionService(MailService mailService) {
        _mailService = mailService;
    }

    @Override
    protected void startUp() throws Exception {
        LOG.info("Start execution service");
    }

    @Override
    protected void shutDown() throws Exception {
        LOG.info("Stop execution service");
    }

    public void execute(Runnable runnable) {
        runnable.run();
        _mailService.sendMail(runnable + " executed!");
    }
}
