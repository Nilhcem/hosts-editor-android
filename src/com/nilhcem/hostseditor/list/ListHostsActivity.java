package com.nilhcem.hostseditor.list;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.addedit.AddEditHostActivity;
import com.nilhcem.hostseditor.bus.event.StartAddEditActivityEvent;
import com.nilhcem.hostseditor.core.BaseActivity;
import com.nilhcem.hostseditor.core.HostsManager;
import com.nilhcem.hostseditor.model.Host;
import com.squareup.otto.Subscribe;

public class ListHostsActivity extends BaseActivity {
	private static final int REQUESTCODE_ADDHOST_ACTIVITY = 1;

	@Inject HostsManager mHostsManager;
	private ListHostsFragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fragmentMngr = getSupportFragmentManager();
		mFragment = (ListHostsFragment) fragmentMngr.findFragmentByTag(ListHostsFragment.TAG);
		if (mFragment == null) {
			mFragment = new ListHostsFragment();
			FragmentTransaction ft = fragmentMngr.beginTransaction();
			ft.add(android.R.id.content, mFragment, ListHostsFragment.TAG);
			ft.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.hosts_list_menu, menu);
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
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUESTCODE_ADDHOST_ACTIVITY) {
			if (resultCode == RESULT_OK) {
				Host modified = data.getParcelableExtra(AddEditHostActivity.EXTRA_HOST_MODIFIED);
				Host original = data.getParcelableExtra(AddEditHostActivity.EXTRA_HOST_ORIGINAL);
				mFragment.addHost(new Host[] { modified, original });
			}
		}
	}

	@Subscribe
	public void onEditHostEvent(StartAddEditActivityEvent event) {
		Intent intent = new Intent(this, AddEditHostActivity.class);
		intent.putExtra(AddEditHostActivity.EXTRA_HOST_ORIGINAL, event.getHost());
		startActivityForResult(intent, REQUESTCODE_ADDHOST_ACTIVITY);
	}
}
