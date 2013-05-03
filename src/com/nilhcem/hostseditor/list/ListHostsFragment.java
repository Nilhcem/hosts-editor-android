package com.nilhcem.hostseditor.list;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.bus.event.AddedHostEvent;
import com.nilhcem.hostseditor.bus.event.RefreshHostsEvent;
import com.nilhcem.hostseditor.core.BaseFragment;
import com.nilhcem.hostseditor.core.HostsManager;
import com.nilhcem.hostseditor.model.Host;
import com.nilhcem.hostseditor.task.AddHostAsync;
import com.nilhcem.hostseditor.task.ListHostsAsync;
import com.squareup.otto.Subscribe;

public class ListHostsFragment extends BaseFragment implements OnItemClickListener {
	static final String TAG = "ListHostsFragment";

	@Inject HostsManager mHostsManager;
	private ListHostsAdapter mAdapter;

	private ActionMode mMode;
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new ListHostsAdapter(mActivity.getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mListView == null) {
			mListView = new ListView(mActivity.getApplicationContext());
			mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			mListView.setFastScrollEnabled(true);
			mListView.setItemsCanFocus(false);
			mListView.setOnItemClickListener(this);
			refreshHosts(false);
		} else {
			// detach from container and return the same view
			((ViewGroup) mListView.getParent()).removeAllViews();
		}
		return mListView;
	}

	@Override
	public void onPause() {
		if (mMode != null) {
			mMode.finish();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int nbCheckedElements = 0;
		SparseBooleanArray checked = mListView.getCheckedItemPositions();
		for (int i = 0; i < checked.size(); i++) {
			if (checked.valueAt(i)) {
				nbCheckedElements++;
			}
		}

		if (nbCheckedElements > 0) {
			if (mMode == null) {
				mMode = mActivity.startActionMode(new ModeCallback());
			}
			mMode.setTitle(String.format(Locale.US, getString(R.string.add_host_menu_selected), nbCheckedElements));
		} else {
			if (mMode != null) {
				mMode.finish();
			}
		}
	}

	@Subscribe
	public void onHostAdded(AddedHostEvent event) {
		Log.d(TAG, "onHostAdded event");
		refreshHosts(false);
	}

	@Subscribe
	public void onRefreshHosts(RefreshHostsEvent hosts) {
		Log.d(TAG, "onRefreshHosts event");
		mAdapter.updateHosts(hosts.get());
		mListView.setAdapter(mAdapter);
	}

	public void addHost(Host host) {
		mApp.getObjectGraph().get(AddHostAsync.class).execute(host);
	}

	private void refreshHosts(boolean forceRefresh) {
		mAdapter.updateHosts(new ArrayList<Host>());
		mApp.getObjectGraph().get(ListHostsAsync.class).execute(forceRefresh);
	}

	private final class ModeCallback implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mActivity.getSupportMenuInflater();
			inflater.inflate(R.menu.hosts_list_contextual_actions, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			for (int i = 0; i < mListView.getAdapter().getCount(); i++) {
				mListView.setItemChecked(i, false);
			}
			mMode = null;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			mode.finish();
			return true;
		}
	};
}
