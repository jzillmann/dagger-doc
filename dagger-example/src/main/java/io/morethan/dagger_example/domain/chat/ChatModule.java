package io.morethan.dagger_example.domain.chat;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.morethan.dagger_example.persistence.PersistenceModule;
import io.morethan.dagger_example.persistence.PersistenceService;
import io.morethan.dagger_example.server.ServerEndpoint;
import io.morethan.daggerdoc.ModuleDoc;

@Module(includes = { PersistenceModule.class })
@ModuleDoc(category = "Domain Layer")
public class ChatModule {

    @Provides
    @Singleton
    ChatManager chatManager(PersistenceService persistenceService) {
        return new ChatManager(persistenceService);
    }

    @Provides
    @Singleton
    @IntoSet
    ServerEndpoint sendChatMessageEndpoint(ChatManager chatManager) {
        return new SendChatMessageEndpoint(chatManager);
    }
}
