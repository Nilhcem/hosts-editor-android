package com.nilhcem.hostseditor.add;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.nilhcem.hostseditor.core.BaseActivity;
import com.nilhcem.hostseditor.list.HostsListActivity;
import com.nilhcem.hostseditor.model.Host;
import com.squareup.otto.Subscribe;

public class AddHostActivity extends BaseActivity {
	public static final String EXTRA_HOST = "host";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Init ActionBar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		// Add fragment
		if (savedInstanceState == null) {
			Fragment fragment = new AddHostFragment();
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(android.R.id.content, fragment);
			ft.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent intent = new Intent(this, HostsListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Subscribe
	public void onValidHostFromFragment(Host host) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra(EXTRA_HOST, host);
		setResult(RESULT_OK, returnIntent);
		finish();
	}
}
