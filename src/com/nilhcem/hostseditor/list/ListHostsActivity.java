package com.nilhcem.hostseditor.list;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.Views;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.addedit.AddEditHostActivity;
import com.nilhcem.hostseditor.bus.event.LoadingEvent;
import com.nilhcem.hostseditor.bus.event.StartAddEditActivityEvent;
import com.nilhcem.hostseditor.core.BaseActivity;
import com.nilhcem.hostseditor.core.Host;
import com.nilhcem.hostseditor.core.HostsManager;
import com.squareup.otto.Subscribe;

public class ListHostsActivity extends BaseActivity {
	private static final int REQUESTCODE_ADDEDIT_ACTIVITY = 1;
	private static final String TAG = "ListHostsActivity";
	private static final String INSTANCE_STATE_LOADING = "loading";
	private static final String INSTANCE_STATE_LOADING_MESSAGE = "loadingMessage";

	@Inject HostsManager mHostsManager;
	@InjectView(R.id.listLoading) ProgressBar mProgressBar;
	@InjectView(R.id.listLoadingMsg) TextView mLoadingMsg;
	private ListHostsFragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_hosts_layout);
		Views.inject(this);

		FragmentManager fragmentMngr = getSupportFragmentManager();
		mFragment = (ListHostsFragment) fragmentMngr.findFragmentById(R.id.listHostsFragment);
		mFragment.computeViewWidths();

		if (savedInstanceState == null) {
			onLoadingEvent(new LoadingEvent(true, R.string.loading_hosts));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_add_host:
				mBus.post(new StartAddEditActivityEvent(null));
				return true;
			case R.id.action_reload_hosts:
				mFragment.refreshHosts(true);
				return true;
			case R.id.action_select_all:
				mFragment.selectAll();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.d(TAG, "Activity result received");
		if (requestCode == REQUESTCODE_ADDEDIT_ACTIVITY) {
			if (resultCode == RESULT_OK) {
				Host modified = data.getParcelableExtra(AddEditHostActivity.EXTRA_HOST_MODIFIED);
				Host original = data.getParcelableExtra(AddEditHostActivity.EXTRA_HOST_ORIGINAL);

				boolean addMode = (original == null);
				onLoadingEvent(new LoadingEvent(true, addMode ? R.string.loading_add : R.string.loading_edit));
				mFragment.addEditHost(addMode, new Host[] { modified, original });
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(INSTANCE_STATE_LOADING, mProgressBar.getVisibility() == View.VISIBLE);
		outState.putString(INSTANCE_STATE_LOADING_MESSAGE, mLoadingMsg.getText().toString());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		boolean isLoading = savedInstanceState.getBoolean(INSTANCE_STATE_LOADING, true);
		if (isLoading) {
			String loadingMsg = savedInstanceState.getString(INSTANCE_STATE_LOADING_MESSAGE);
			if (loadingMsg == null) {
				loadingMsg = "";
			}
			onLoadingEvent(new LoadingEvent(isLoading, loadingMsg));
		}
	}

	@Subscribe
	public void onStartAddEditActivityEvent(StartAddEditActivityEvent event) {
		Log.d(TAG, "Ready to start AddEditActivity");
		Intent intent = new Intent(this, AddEditHostActivity.class);
		intent.putExtra(AddEditHostActivity.EXTRA_HOST_ORIGINAL, event.getHost());
		startActivityForResult(intent, REQUESTCODE_ADDEDIT_ACTIVITY);
	}

	@Subscribe
	public void onLoadingEvent(LoadingEvent event) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		mLoadingMsg.setText(event.getMessage(this));
		if (event.isLoading()) {
			Log.d(TAG, "Start loading");
			mProgressBar.setVisibility(View.VISIBLE);
			mLoadingMsg.setVisibility(View.VISIBLE);
			ft.hide(mFragment);
		} else {
			Log.d(TAG, "Stop loading");
			mProgressBar.setVisibility(View.GONE);
			mLoadingMsg.setVisibility(View.GONE);
			ft.show(mFragment);
		}
		ft.commit();
	}
}
