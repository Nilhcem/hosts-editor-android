package com.nilhcem.hostseditor.task;

import android.util.Log;

import com.nilhcem.hostseditor.model.Host;

/**
 * AsyncTask that toggles one or many host entries and triggers a {@code TaskCompletedEvent} event.
 */
public class ToggleHostsAsync extends GenericTaskAsync {
	public static final String TAG = "ToggleHostsAsync";

	@Override
	protected void process(Host... params) {
		Log.d(TAG, "Toggle hosts");
		int nbHosts = params.length;

		for (int i = 0; i < nbHosts; i++) {
			params[i].toggleComment();
		}
	}
}
