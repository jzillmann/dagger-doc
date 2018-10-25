package io.morethan.dagger_example.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractIdleService;

public class MailService extends AbstractIdleService {

    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    @Override
    protected void startUp() throws Exception {
        LOG.info("Start mail service");
    }

    @Override
    protected void shutDown() throws Exception {
        LOG.info("Stop mail service");
    }

    public void sendMail(String text) {
        LOG.info("Send mail:" + text);
    }

}
