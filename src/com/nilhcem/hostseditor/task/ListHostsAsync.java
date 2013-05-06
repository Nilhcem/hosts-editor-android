package com.nilhcem.hostseditor.task;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import android.os.AsyncTask;

import com.nilhcem.hostseditor.bus.event.LoadingEvent;
import com.nilhcem.hostseditor.bus.event.RefreshHostsEvent;
import com.nilhcem.hostseditor.core.Host;
import com.nilhcem.hostseditor.core.HostsManager;
import com.squareup.otto.Bus;

/**
 * AsyncTask that gets all valid hosts and triggers a {@code RefreshHostEvent} event.
 */
public class ListHostsAsync extends AsyncTask<Boolean, Void, List<Host>> {
	@Inject Bus mBus;
	@Inject HostsManager mHostsManager;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mBus.post(new LoadingEvent(true));
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
