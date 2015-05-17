package com.nilhcem.hostseditor.core.dagger;

import com.nilhcem.hostseditor.task.GenericTaskAsync;
import com.nilhcem.hostseditor.task.ListHostsAsync;
import com.nilhcem.hostseditor.ui.BaseActivity;
import com.nilhcem.hostseditor.ui.addedit.AddEditHostFragment;
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

    void inject(BaseActivity activity);

    void inject(GenericTaskAsync async);

    void inject(AddEditHostFragment fragment);

    void inject(ListHostsAsync async);

    void inject(ListHostsFragment fragment);
}
