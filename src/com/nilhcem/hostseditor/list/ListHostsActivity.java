package com.nilhcem.hostseditor.list;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.InjectView;
import butterknife.Views;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.addedit.AddEditHostActivity;
import com.nilhcem.hostseditor.bus.event.LoadingEvent;
import com.nilhcem.hostseditor.bus.event.StartAddEditActivityEvent;
import com.nilhcem.hostseditor.core.BaseActivity;
import com.nilhcem.hostseditor.core.HostsManager;
import com.nilhcem.hostseditor.model.Host;
import com.squareup.otto.Subscribe;

public class ListHostsActivity extends BaseActivity {
	private static final int REQUESTCODE_ADDHOST_ACTIVITY = 1;
	private static final String INSTANCE_STATE_LOADING = "loading";

	@Inject HostsManager mHostsManager;
	@InjectView(R.id.listLoading) ProgressBar mProgressBar;
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
			onLoadingEvent(new LoadingEvent(true));
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

		if (requestCode == REQUESTCODE_ADDHOST_ACTIVITY) {
			if (resultCode == RESULT_OK) {
				onLoadingEvent(new LoadingEvent(true));
				Host modified = data.getParcelableExtra(AddEditHostActivity.EXTRA_HOST_MODIFIED);
				Host original = data.getParcelableExtra(AddEditHostActivity.EXTRA_HOST_ORIGINAL);
				mFragment.addHost(new Host[] { modified, original });
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(INSTANCE_STATE_LOADING, mProgressBar.getVisibility() == View.VISIBLE);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		boolean isLoading = savedInstanceState.getBoolean(INSTANCE_STATE_LOADING, true);
		if (isLoading) {
			onLoadingEvent(new LoadingEvent(true));
		}
	}

	@Subscribe
	public void onStartAddEditActivityEvent(StartAddEditActivityEvent event) {
		Intent intent = new Intent(this, AddEditHostActivity.class);
		intent.putExtra(AddEditHostActivity.EXTRA_HOST_ORIGINAL, event.getHost());
		startActivityForResult(intent, REQUESTCODE_ADDHOST_ACTIVITY);
	}

	@Subscribe
	public void onLoadingEvent(LoadingEvent event) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		if (event.isLoading()) {
			mProgressBar.setVisibility(View.VISIBLE);
			ft.hide(mFragment);
		} else {
			mProgressBar.setVisibility(View.GONE);
			ft.show(mFragment);
		}
		ft.commit();
	}
}
