package com.nilhcem.hostseditor.task;

import javax.inject.Inject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nilhcem.hostseditor.bus.event.LoadingEvent;
import com.nilhcem.hostseditor.bus.event.TaskCompletedEvent;
import com.nilhcem.hostseditor.core.Host;
import com.nilhcem.hostseditor.core.HostsManager;
import com.squareup.otto.Bus;

public abstract class GenericTaskAsync extends AsyncTask<Host, Void, Void> {

	@Inject
	Bus mBus;
	@Inject
	HostsManager mHostsManager;

	private Context mAppContext;
	protected boolean mFlagLoadingMsg; // which loading message (between 2) to display: (singular/plural) - (add/edit).

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mBus.post(new LoadingEvent(true, getLoadingMsgRes()));
	}

	@Override
	protected Void doInBackground(Host... params) {
		process(params);
		if (!mHostsManager.saveHosts(mAppContext)) {
			cancel(false);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Log.d(getClass().getSimpleName(), "Task fully executed");
		mBus.post(new TaskCompletedEvent(getClass().getSimpleName(), true));
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		Log.w(getClass().getSimpleName(), "Task cancelled");
		mBus.post(new TaskCompletedEvent(getClass().getSimpleName(), false));
	}

	public void init(Context appContext, boolean flagMsg) {
		mAppContext = appContext;
		mFlagLoadingMsg = flagMsg;
	}

	/**
	 * This method should edit the main {@code List<Host>}.
	 * @param params selected Hosts from the main ListView.
	 */
	protected abstract void process(Host... params);
	protected abstract int getLoadingMsgRes();
}
