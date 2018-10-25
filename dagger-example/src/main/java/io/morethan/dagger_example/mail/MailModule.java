package io.morethan.dagger_example.mail;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.morethan.daggerdoc.ModuleDoc;

@Module
@ModuleDoc(category = "Infrastructure Layer")
public class MailModule {

    @Provides
    @Singleton
    static MailService mailService() {
        return new MailService();
    }

}
