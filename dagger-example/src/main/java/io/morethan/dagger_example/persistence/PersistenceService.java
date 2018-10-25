package io.morethan.dagger_example.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractIdleService;

public class PersistenceService extends AbstractIdleService {

    private static final Logger LOG = LoggerFactory.getLogger(PersistenceService.class);

    @Override
    protected void startUp() throws Exception {
        LOG.info("Start persistence service");
    }

    @Override
    protected void shutDown() throws Exception {
        LOG.info("Stop persistence service");
    }

    public void persist(Object entity) {
        LOG.info("Persist entity of type " + entity.getClass().getSimpleName());
    }

}
