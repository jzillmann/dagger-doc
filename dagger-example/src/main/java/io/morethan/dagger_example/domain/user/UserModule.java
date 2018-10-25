package io.morethan.dagger_example.domain.user;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.morethan.dagger_example.mail.MailModule;
import io.morethan.dagger_example.mail.MailService;
import io.morethan.dagger_example.persistence.PersistenceModule;
import io.morethan.dagger_example.persistence.PersistenceService;
import io.morethan.dagger_example.server.ServerEndpoint;
import io.morethan.daggerdoc.ModuleDoc;

@Module(includes = { PersistenceModule.class, MailModule.class })
@ModuleDoc(category = "Domain Layer")
public class UserModule {

    @Provides
    @Singleton
    static UserRepository userRepository(PersistenceService persistenceService) {
        return new UserRepository(persistenceService);
    }

    @Provides
    @Singleton
    static RegistrationManager registrationManager(UserRepository userRepository, MailService mailService) {
        return new RegistrationManager(userRepository, mailService);
    }

    @Provides
    @Singleton
    @IntoSet
    static ServerEndpoint registrationEndpoint(RegistrationManager registrationManager) {
        return new RegistrationEndpoint(registrationManager);
    }
}
