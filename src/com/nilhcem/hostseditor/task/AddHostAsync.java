package com.nilhcem.hostseditor.task;

import javax.inject.Inject;

import android.os.AsyncTask;
import android.util.Log;

import com.nilhcem.hostseditor.bus.event.AddedHostEvent;
import com.nilhcem.hostseditor.core.HostsManager;
import com.nilhcem.hostseditor.model.Host;
import com.squareup.otto.Bus;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

/**
 * AsyncTask that inserts an host entry and triggers a {@code AddedHostEvent} event.
 */
public class AddHostAsync extends AsyncTask<Host, Void, Void> {
	private static final String TAG = "AddHostAsync";
	private static final String MOUNT_TYPE_RO = "ro";
	private static final String MOUNT_TYPE_RW = "rw";

	@Inject
	Bus mBus;
	@Inject
	HostsManager mHostsManager;

	@Override
	protected Void doInBackground(Host... params) {
		Log.i(TAG, "Add host");

		Host host = params[0];
		if (RootTools.isAccessGiven()) {
			RootTools.remount(HostsManager.HOSTS_FILE, MOUNT_TYPE_RW);
			try {
				CommandCapture command = new CommandCapture(0, "echo \"" + host.toString() + "\" >> " + HostsManager.HOSTS_FILE);
				try {
					RootTools.getShell(true).add(command).waitForFinish();
					mHostsManager.getHosts(false).add(host);
				} catch (Exception e) {
					Log.e(TAG, "Error adding host!", e);
				}
			} finally {
				RootTools.remount(HostsManager.HOSTS_FILE, MOUNT_TYPE_RO);
			}
		} else {
			Log.e(TAG, "Impossible to be root!");
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		mBus.post(new AddedHostEvent());
	}
}
