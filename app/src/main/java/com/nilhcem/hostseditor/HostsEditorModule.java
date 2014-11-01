package com.nilhcem.hostseditor;

import com.nilhcem.hostseditor.task.AddEditHostAsync;
import com.nilhcem.hostseditor.task.ListHostsAsync;
import com.nilhcem.hostseditor.task.RemoveHostsAsync;
import com.nilhcem.hostseditor.task.ToggleHostsAsync;
import com.nilhcem.hostseditor.ui.addedit.AddEditHostActivity;
import com.nilhcem.hostseditor.ui.addedit.AddEditHostFragment;
import com.nilhcem.hostseditor.ui.list.ListHostsActivity;
import com.nilhcem.hostseditor.ui.list.ListHostsFragment;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
