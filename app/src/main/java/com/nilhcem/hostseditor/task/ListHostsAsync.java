package com.nilhcem.hostseditor.task;

import android.os.AsyncTask;

import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.core.Host;
import com.nilhcem.hostseditor.core.HostsManager;
import com.nilhcem.hostseditor.event.LoadingEvent;
import com.nilhcem.hostseditor.event.RefreshHostsEvent;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * AsyncTask that gets all valid hosts and triggers a {@code RefreshHostEvent} event.
 */
public class ListHostsAsync extends AsyncTask<Boolean, Void, List<Host>> {

    @Inject Bus mBus;
    @Inject HostsManager mHostsManager;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mBus.post(new LoadingEvent(true, R.string.loading_hosts));
    }

    @Override
    protected List<Host> doInBackground(Boolean... params) {
        Boolean forceRefresh = params[0];
        if (forceRefresh == null) {
            forceRefresh = false;
        }

        List<Host> allHosts = mHostsManager.getHosts(forceRefresh);

        // Filter to get only valid hosts
        List<Host> validHosts = new ArrayList<Host>();
        for (Host host : allHosts) {
            if (host.isValid()) {
                validHosts.add(host);
            }
        }
        return validHosts;
    }

    @Override
    protected void onPostExecute(List<Host> result) {
        super.onPostExecute(result);
        mBus.post(new RefreshHostsEvent(result));
    }
}
