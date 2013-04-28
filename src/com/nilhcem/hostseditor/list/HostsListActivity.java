package com.nilhcem.hostseditor.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.add.AddHostActivity;
import com.nilhcem.hostseditor.model.Host;

public class HostsListActivity extends SherlockFragmentActivity {
	private static final int REQUESTCODE_ADDHOST_ACTIVITY = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			Fragment fragment = new HostsListFragment();
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(android.R.id.content, fragment);
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
				// TODO
			}
		}
	}
}
