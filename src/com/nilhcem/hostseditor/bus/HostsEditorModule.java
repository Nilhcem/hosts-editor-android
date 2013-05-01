package com.nilhcem.hostseditor.bus;

import javax.inject.Singleton;

import com.nilhcem.hostseditor.add.AddHostActivity;
import com.nilhcem.hostseditor.add.AddHostFragment;
import com.nilhcem.hostseditor.core.HostsManager;
import com.nilhcem.hostseditor.list.ListHostsActivity;
import com.nilhcem.hostseditor.list.ListHostsFragment;
import com.nilhcem.hostseditor.task.AddHostAsync;
import com.nilhcem.hostseditor.task.ListHostsAsync;
import com.squareup.otto.Bus;

import dagger.Module;
import dagger.Provides;

@Module(
	entryPoints = {
		AddHostActivity.class,
		AddHostAsync.class,
		AddHostFragment.class,
		ListHostsActivity.class,
		ListHostsAsync.class,
		ListHostsFragment.class
	}
)
public class HostsEditorModule {

	@Provides @Singleton Bus provideBus() {
		return new Bus();
	}

	@Provides @Singleton HostsManager provideHostsManager() {
		return new HostsManager();
	}
}
