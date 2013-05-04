package com.nilhcem.hostseditor.task;

import android.util.Log;

import com.nilhcem.hostseditor.model.Host;

/**
 * AsyncTask that inserts an host entry and triggers a {@code TaskCompletedEvent} event.
 */
public class AddHostAsync extends GenericTaskAsync {
	public static final String TAG = "AddHostAsync";

	@Override
	protected void process(Host... params) {
		Log.d(TAG, "Add host");
		Host host = params[0];
		mHostsManager.getHosts(false).add(host);
	}
}
