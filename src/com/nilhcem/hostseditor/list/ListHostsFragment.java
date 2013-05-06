package com.nilhcem.hostseditor.list;

import java.util.ArrayList;
import java.util.List;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import butterknife.InjectView;
import butterknife.Views;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.bus.event.LoadingEvent;
import com.nilhcem.hostseditor.bus.event.RefreshHostsEvent;
import com.nilhcem.hostseditor.bus.event.StartAddEditActivityEvent;
import com.nilhcem.hostseditor.bus.event.TaskCompletedEvent;
import com.nilhcem.hostseditor.core.BaseFragment;
import com.nilhcem.hostseditor.core.HostsManager;
import com.nilhcem.hostseditor.model.Host;
import com.nilhcem.hostseditor.task.AddEditHostAsync;
import com.nilhcem.hostseditor.task.GenericTaskAsync;
import com.nilhcem.hostseditor.task.ListHostsAsync;
import com.nilhcem.hostseditor.task.RemoveHostsAsync;
import com.nilhcem.hostseditor.task.ToggleHostsAsync;
import com.squareup.otto.Subscribe;

public class ListHostsFragment extends BaseFragment implements OnItemClickListener, OnItemLongClickListener {
	private static final String TAG = "ListHostsFragment";

	@Inject HostsManager mHostsManager;
	@InjectView(R.id.listHosts) ListView mListView;
	private ListHostsAdapter mAdapter;

	private ActionMode mMode;
	private MenuItem mEditMenuItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new ListHostsAdapter(mActivity.getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		boolean firstCall = (mListView == null);

		View view = inflater.inflate(R.layout.list_hosts_fragment, container, false);
		Views.inject(this, view);

		mListView.setAdapter(mAdapter);
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mListView.setEmptyView(view.findViewById(R.id.listEmptyLayout));
		mListView.setFastScrollEnabled(true);
		mListView.setItemsCanFocus(false);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);

		if (firstCall) {
			refreshHosts(false);
		}
		return view;
	}

	@Override
	public void onPause() {
		finishActionMode();
		super.onPause();
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
		displayActionMode(nbCheckedElements);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if (mMode == null) {
			Host host = mAdapter.getItem(position);
			mBus.post(new StartAddEditActivityEvent(host));
			return true;
		}
		return false;
	}

	@Subscribe
	public void onTaskFinished(TaskCompletedEvent task) {
		Log.d(TAG, "Task " + task.getTag() + " finished");
		if (task.isSuccessful()) {
			refreshHosts(false);
		} else {
			// Display error message
			// Force reload
		}
	}

	@Subscribe
	public void onHostsRefreshed(RefreshHostsEvent hosts) {
		Log.d(TAG, "Refreshing listview");
		finishActionMode();
		mAdapter.updateHosts(hosts.get());
		mBus.post(new LoadingEvent(false));
	}

	public void addHost(Host[] hosts) {
		runGenericTask(AddEditHostAsync.class, hosts);
	}

	public void refreshHosts(boolean forceRefresh) {
		mApp.getObjectGraph().get(ListHostsAsync.class).execute(forceRefresh);
	}

	public void selectAll() {
		int nbElem = mAdapter.getCount();

		if (nbElem > 0) {
			for (int i = 0; i < nbElem; i++ ) {
				mListView.setItemChecked(i, true);
			}
			displayActionMode(nbElem);
		}
	}

	public void computeViewWidths() {
		mAdapter.computeViewWidths(mActivity);
	}

	private void displayActionMode(int nbCheckedElements) {
		if (nbCheckedElements > 0) {
			if (mMode == null) {
				mMode = mActivity.startActionMode(new ModeCallback());
			}

			if (mEditMenuItem != null) {
				mEditMenuItem.setVisible(nbCheckedElements == 1);
			}
			mMode.setTitle(String.format(Locale.US, getString(R.string.list_menu_selected), nbCheckedElements));
		} else {
			finishActionMode();
		}
	}

	private final class ModeCallback implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mActivity.getSupportMenuInflater();
			inflater.inflate(R.menu.list_contextual_actions, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			mEditMenuItem = menu.findItem(R.id.cab_action_edit);
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			for (int i = 0; i < mListView.getAdapter().getCount(); i++) {
				mListView.setItemChecked(i, false);
			}
			mMode = null;
			mEditMenuItem = null;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			Host[] selectedItems = getSelectedItems();

			switch (item.getItemId()) {
				case R.id.cab_action_edit:
					mBus.post(new StartAddEditActivityEvent(selectedItems[0]));
					break;
				case R.id.cab_action_delete:
					runGenericTask(RemoveHostsAsync.class, selectedItems);
					break;
				case R.id.cab_action_toggle:
					runGenericTask(ToggleHostsAsync.class, selectedItems);
					break;
				default:
					return false;
			}
			mode.finish();
			return true;
		}

		private Host[] getSelectedItems() {
			List<Host> items = new ArrayList<Host>();

			int len = mListView.getCount();
			SparseBooleanArray checked = mListView.getCheckedItemPositions();
			for (int i = 0; i < len ; i++) {
				if (checked.get(i)) {
					items.add(mAdapter.getItem(i));
				}
			}
			return items.toArray(new Host[items.size()]);
		}
	};

	private void runGenericTask(Class<? extends GenericTaskAsync> clazz, Host[] hosts) {
		GenericTaskAsync task = mApp.getObjectGraph().get(clazz);
		task.setAppContext(mActivity.getApplicationContext());
		task.execute(hosts);
	}

	private void finishActionMode() {
		if (mMode != null) {
			mMode.finish();
		}
	}
}
