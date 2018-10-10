package io.morethan.dagger_example.service;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.morethan.dagger_example.server.ServerService;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    @IntoSet
    ServerService serviceX() {
        return new ServerService("X");
    }

    @Provides
    @Singleton
    @IntoSet
    ServerService serviceY() {
        return new ServerService("Y");
    }

}
