package com.nilhcem.hostseditor.core;

import javax.inject.Inject;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((HostsEditorApp) getSherlockActivity().getApplication()).getObjectGraph().inject(this);
	}
}
