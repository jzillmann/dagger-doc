package io.morethan.dagger_example.exec;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.morethan.dagger_example.mail.MailModule;
import io.morethan.dagger_example.mail.MailService;
import io.morethan.daggerdoc.ModuleDoc;

@Module(includes = { MailModule.class })
@ModuleDoc(category = "Infrastructure Layer")
public class ExecutionModule {

    @Provides
    @Singleton
    static ExecutionService executionService(MailService mailService) {
        return new ExecutionService(mailService);
    }

}
