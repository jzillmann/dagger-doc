package io.morethan.dagger_example.domain.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.morethan.dagger_example.persistence.PersistenceService;

public class ChatManager {

    private static final Logger LOG = LoggerFactory.getLogger(ChatManager.class);

    private final PersistenceService _persistenceService;

    public ChatManager(PersistenceService persistenceService) {
        _persistenceService = persistenceService;
    }

    public void sendMessage(String user, String content) {
        LOG.info("Sending message to user {}: {}", user, content);
        _persistenceService.persist(new ChatMessage(user, content));
    }
}
