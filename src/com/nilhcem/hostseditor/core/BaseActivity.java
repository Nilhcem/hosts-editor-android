package com.nilhcem.hostseditor.core;

import javax.inject.Inject;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nilhcem.hostseditor.HostsEditorApp;
import com.squareup.otto.Bus;

/**
 * All activities should extend this for dependency injection.
 */
public class BaseActivity extends SherlockFragmentActivity {

	@Inject protected Bus mBus;

	@Override
	protected void onCreate(Bundle state) {
		super.onCreate(state);

		// Android constructs Activity instances so we must find the ObjectGraph instance and inject this.
		((HostsEditorApp) getApplication()).getObjectGraph().inject(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mBus.register(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mBus.unregister(this);
	}
}
