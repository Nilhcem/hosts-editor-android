package com.nilhcem.hostseditor;

import javax.inject.Singleton;

import com.nilhcem.hostseditor.add.AddHostActivity;
import com.nilhcem.hostseditor.add.AddHostFragment;
import com.nilhcem.hostseditor.core.HostsHelper;
import com.nilhcem.hostseditor.list.HostsListFragment;
import com.squareup.otto.Bus;

import dagger.Module;
import dagger.Provides;

@Module(
	entryPoints = {
		AddHostActivity.class,
		AddHostFragment.class,
		HostsListFragment.class
	}
)
public class HostsEditorModule {

	@Provides @Singleton Bus provideBus() {
		return new Bus();
	}

	@Provides @Singleton HostsHelper provideHostsHelper() {
		return new HostsHelper();
	}
}
