package com.nilhcem.hostseditor;

import javax.inject.Singleton;

import com.nilhcem.hostseditor.core.HostsHelper;
import com.nilhcem.hostseditor.list.HostsListFragment;

import dagger.Module;
import dagger.Provides;

@Module(entryPoints = {
		HostsListFragment.class
})
public class HostsEditorModule {

	@Provides @Singleton HostsHelper provideHostsHelper() {
		return new HostsHelper();
	}
}
