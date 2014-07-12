package com.nilhcem.hostseditor.bus;

import com.nilhcem.hostseditor.addedit.AddEditHostActivity;
import com.nilhcem.hostseditor.addedit.AddEditHostFragment;
import com.nilhcem.hostseditor.list.ListHostsActivity;
import com.nilhcem.hostseditor.list.ListHostsFragment;
import com.nilhcem.hostseditor.task.AddEditHostAsync;
import com.nilhcem.hostseditor.task.ListHostsAsync;
import com.nilhcem.hostseditor.task.RemoveHostsAsync;
import com.nilhcem.hostseditor.task.ToggleHostsAsync;
import com.squareup.otto.Bus;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(
        injects = {
                AddEditHostActivity.class,
                AddEditHostAsync.class,
                AddEditHostFragment.class,
                ListHostsActivity.class,
                ListHostsAsync.class,
                ListHostsFragment.class,
                RemoveHostsAsync.class,
                ToggleHostsAsync.class
        }
)
public class HostsEditorModule {

    @Provides @Singleton Bus provideBus() {
        return new Bus();
    }
}
