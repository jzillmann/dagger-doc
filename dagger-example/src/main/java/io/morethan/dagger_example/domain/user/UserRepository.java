package io.morethan.dagger_example.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.morethan.dagger_example.persistence.PersistenceService;

public class UserRepository {

    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);
    private final PersistenceService _persistenceService;

    public UserRepository(PersistenceService persistenceService) {
        _persistenceService = persistenceService;
    }

    public void persist(String user) {
        _persistenceService.persist(user);
        LOG.info("Persisted user {}", user);
    }

}
