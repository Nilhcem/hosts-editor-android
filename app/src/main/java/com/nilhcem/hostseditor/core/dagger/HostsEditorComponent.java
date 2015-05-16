package com.nilhcem.hostseditor.core.dagger;

import com.nilhcem.hostseditor.task.AddEditHostAsync;
import com.nilhcem.hostseditor.task.ListHostsAsync;
import com.nilhcem.hostseditor.task.RemoveHostsAsync;
import com.nilhcem.hostseditor.task.ToggleHostsAsync;
import com.nilhcem.hostseditor.ui.addedit.AddEditHostActivity;
import com.nilhcem.hostseditor.ui.addedit.AddEditHostFragment;
import com.nilhcem.hostseditor.ui.list.ListHostsActivity;
import com.nilhcem.hostseditor.ui.list.ListHostsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                HostsEditorModule.class
        }
)
public interface HostsEditorComponent {

    void inject(AddEditHostActivity activity);

    void inject(AddEditHostAsync async);

    void inject(AddEditHostFragment fragment);

    void inject(ListHostsActivity activity);

    void inject(ListHostsAsync async);

    void inject(ListHostsFragment fragment);

    void inject(RemoveHostsAsync async);

    void inject(ToggleHostsAsync async);
}
