package io.morethan.dagger_example;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.morethan.dagger_example.exec.ExecutionModule;
import io.morethan.dagger_example.exec.ExecutionService;
import io.morethan.dagger_example.mail.MailModule;
import io.morethan.dagger_example.mail.MailService;
import io.morethan.dagger_example.persistence.PersistenceModule;
import io.morethan.dagger_example.persistence.PersistenceService;
import io.morethan.dagger_example.server.Server;
import io.morethan.dagger_example.server.ServerModule;
import io.morethan.daggerdoc.ModuleDoc;

@Module(includes = {
        ServerModule.class,
        PersistenceModule.class,
        ExecutionModule.class,
        MailModule.class
})
@ModuleDoc(category = "App Layer")
public class AppModule {

    @Provides
    @Singleton
    LifeCycleController app(
            Server server,
            PersistenceService persistenceService,
            ExecutionService executionService,
            MailService mailService) {
        return new LifeCycleController(server, persistenceService, executionService, mailService);
    }

}
