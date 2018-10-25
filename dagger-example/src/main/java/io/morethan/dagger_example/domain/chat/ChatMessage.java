package io.morethan.dagger_example.domain.chat;

import com.google.common.base.MoreObjects;

public class ChatMessage {

    private final String _to;
    private final String _content;

    public ChatMessage(String to, String content) {
        _to = to;
        _content = content;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("to", _to)
                .addValue(_content)
                .toString();
    }

}
