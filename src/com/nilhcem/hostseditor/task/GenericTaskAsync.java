package com.nilhcem.hostseditor.task;

import javax.inject.Inject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nilhcem.hostseditor.bus.event.LoadingEvent;
import com.nilhcem.hostseditor.bus.event.TaskCompletedEvent;
import com.nilhcem.hostseditor.core.HostsManager;
import com.nilhcem.hostseditor.model.Host;
import com.squareup.otto.Bus;

public abstract class GenericTaskAsync extends AsyncTask<Host, Void, Void> {

	@Inject
	Bus mBus;
	@Inject
	HostsManager mHostsManager;

	private Context mAppContext;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mBus.post(new LoadingEvent(true));
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

	public void setAppContext(Context appContext) {
		mAppContext = appContext;
	}

	/**
	 * This method should edit the main {@code List<Host>}.
	 * @param params selected Hosts from the main ListView.
	 */
	protected abstract void process(Host... params);
}
