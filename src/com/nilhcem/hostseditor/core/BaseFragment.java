package com.nilhcem.hostseditor.core;

import javax.inject.Inject;

import android.app.Activity;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;
import com.nilhcem.hostseditor.HostsEditorApp;
import com.squareup.otto.Bus;

/**
* All fragments should extend this for dependency injection.
*/
public class BaseFragment extends SherlockFragment {
	@Inject
	protected Bus mBus;

	protected Activity mActivity;
	protected HostsEditorApp mApp;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		mApp = (HostsEditorApp) activity.getApplication();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mApp = null;
		mActivity = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		// Android constructs Fragment instances so we must find the ObjectGraph instance and inject this.
		mApp.getObjectGraph().inject(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		mBus.register(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		mBus.unregister(this);
	}
}
