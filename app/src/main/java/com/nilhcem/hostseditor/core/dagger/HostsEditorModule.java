package com.nilhcem.hostseditor.core.dagger;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class HostsEditorModule {

    @Provides @Singleton Bus provideBus() {
        return new Bus();
    }
}
