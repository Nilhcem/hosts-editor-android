package com.nilhcem.hostseditor.ui;

import android.app.Activity;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nilhcem.hostseditor.HostsEditorApplication;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * All fragments should extend this for dependency injection.
 */
public class BaseFragment extends SherlockFragment {

    @Inject protected Bus mBus;

    protected SherlockFragmentActivity mActivity;
    protected HostsEditorApplication mApp;

    @Override
    public void onAttach(Activity activity) {
        if (!(activity instanceof SherlockFragmentActivity)) {
            throw new UnsupportedOperationException("Activity must be a SherlockFragmentActivity");
        }

        super.onAttach(activity);
        mActivity = (SherlockFragmentActivity) activity;
        mApp = (HostsEditorApplication) activity.getApplication();
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
        HostsEditorApplication.get(mActivity).inject(this);
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
