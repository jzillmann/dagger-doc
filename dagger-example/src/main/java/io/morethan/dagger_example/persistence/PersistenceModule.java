package io.morethan.dagger_example.persistence;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.morethan.daggerdoc.ModuleDoc;

@Module
@ModuleDoc(category = "Infrastructure Layer")
public class PersistenceModule {

    @Provides
    @Singleton
    static PersistenceService persistenceService() {
        return new PersistenceService();
    }

}
