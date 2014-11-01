package com.nilhcem.hostseditor.task;

import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.core.Host;

import java.util.List;

import timber.log.Timber;

/**
 * AsyncTask that removes one or many host entries and triggers a {@code TaskCompletedEvent} event.
 */
public class RemoveHostsAsync extends GenericTaskAsync {

    @Override
    protected void process(Host... params) {
        Timber.d("Remove hosts");

        List<Host> hosts = mHostsManager.getHosts(false);
        for (Host host : params) {
            hosts.remove(host);
        }
    }

    @Override
    protected int getLoadingMsgRes() {
        return mFlagLoadingMsg ? R.string.loading_remove_single : R.string.loading_remove_multiple;
    }
}
