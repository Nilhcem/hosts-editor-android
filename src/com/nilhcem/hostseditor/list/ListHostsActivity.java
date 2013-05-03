package com.nilhcem.hostseditor.list;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.add.AddHostActivity;
import com.nilhcem.hostseditor.core.BaseActivity;
import com.nilhcem.hostseditor.core.HostsManager;
import com.nilhcem.hostseditor.model.Host;

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
				Intent intent = new Intent(this, AddHostActivity.class);
				startActivityForResult(intent, REQUESTCODE_ADDHOST_ACTIVITY);
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
				Host host = data.getParcelableExtra(AddHostActivity.EXTRA_HOST);
				mFragment.addHost(host);
			}
		}
	}
}
