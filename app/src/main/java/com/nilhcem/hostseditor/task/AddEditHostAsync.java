package com.nilhcem.hostseditor.task;

import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.core.Host;
import timber.log.Timber;

import java.util.List;

/**
 * AsyncTask that inserts or edits an host entry and triggers a {@code TaskCompletedEvent} event.
 */
public class AddEditHostAsync extends GenericTaskAsync {

    @Override
    protected void process(Host... params) {
        Timber.d("Add/Edit host");
        Host host = params[0];
        Host original = params[1];

        List<Host> hosts = mHostsManager.getHosts(false);
        if (original == null) {
            hosts.add(host);
        } else {
            hosts.get(hosts.indexOf(original)).merge(host);
        }
    }

    @Override
    protected int getLoadingMsgRes() {
        return mFlagLoadingMsg ? R.string.loading_add : R.string.loading_edit;
    }
}
