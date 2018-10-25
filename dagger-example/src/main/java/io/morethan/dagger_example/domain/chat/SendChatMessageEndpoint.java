package io.morethan.dagger_example.domain.chat;

import java.util.Map;

import io.morethan.dagger_example.server.ServerEndpoint;

public class SendChatMessageEndpoint extends ServerEndpoint {

    private final ChatManager _chatManager;

    public SendChatMessageEndpoint(ChatManager chatManager) {
        _chatManager = chatManager;
    }

    @Override
    public void call(Map<String, String> parameters) {
        _chatManager.sendMessage(parameters.get("user"), parameters.get("content"));
    }

}
