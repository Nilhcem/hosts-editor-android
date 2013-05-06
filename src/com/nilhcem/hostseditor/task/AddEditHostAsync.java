package com.nilhcem.hostseditor.task;

import java.util.List;

import android.util.Log;

import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.core.Host;

/**
 * AsyncTask that inserts or edits an host entry and triggers a {@code TaskCompletedEvent} event.
 */
public class AddEditHostAsync extends GenericTaskAsync {
	public static final String TAG = "AddEditHostAsync";

	@Override
	protected void process(Host... params) {
		Log.d(TAG, "Add/Edit host");
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
