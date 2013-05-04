package com.nilhcem.hostseditor.task;

import java.util.List;

import android.util.Log;

import com.nilhcem.hostseditor.model.Host;

/**
 * AsyncTask that removes one or many host entries and triggers a {@code TaskCompletedEvent} event.
 */
public class RemoveHostsAsync extends GenericTaskAsync {
	public static final String TAG = "RemoveHostsAsync";

	@Override
	protected void process(Host... params) {
		Log.d(TAG, "Remove hosts");
		int nbHosts = params.length;

		List<Host> hosts = mHostsManager.getHosts(false);
		for (int i = 0; i < nbHosts; i++) {
			hosts.remove(params[i]);
		}
	}
}
