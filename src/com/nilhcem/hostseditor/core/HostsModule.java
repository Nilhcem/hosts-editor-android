package com.nilhcem.hostseditor.core;

import javax.inject.Singleton;

import com.nilhcem.hostseditor.list.HostsListFragment;

import dagger.Module;
import dagger.Provides;

@Module(entryPoints = {
		HostsListFragment.class
})
public class HostsModule {

	@Provides @Singleton HostsHelper provideHostsHelper() {
		return new HostsHelper();
	}
}
