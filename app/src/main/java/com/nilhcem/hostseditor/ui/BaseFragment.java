package com.nilhcem.hostseditor.ui;

import android.app.Activity;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nilhcem.hostseditor.BuildConfig;
import com.nilhcem.hostseditor.HostsEditorApplication;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * All fragments should extend this for dependency injection.
 */
public class BaseFragment extends SherlockFragment {

    @Inject protected Bus mBus;

    protected HostsEditorApplication mApp;
    protected SherlockFragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        if (BuildConfig.DEBUG && !(activity instanceof SherlockFragmentActivity)) {
            throw new UnsupportedOperationException("Activity must be a SherlockFragmentActivity");
        }

        super.onAttach(activity);
        mApp = HostsEditorApplication.get(activity);
        mActivity = (SherlockFragmentActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
    }

    @Override
    public void onPause() {
        mBus.unregister(this);
        super.onPause();
    }

    @Override
    public void onDetach() {
        mApp = null;
        mActivity = null;
        super.onDetach();
    }
}
