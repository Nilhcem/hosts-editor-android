package com.nilhcem.hostseditor.list;

import java.util.ArrayList;

import javax.inject.Inject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nilhcem.hostseditor.bus.event.AddedHostEvent;
import com.nilhcem.hostseditor.bus.event.RefreshHostsEvent;
import com.nilhcem.hostseditor.core.BaseFragment;
import com.nilhcem.hostseditor.core.HostsManager;
import com.nilhcem.hostseditor.model.Host;
import com.nilhcem.hostseditor.task.AddHostAsync;
import com.nilhcem.hostseditor.task.ListHostsAsync;
import com.squareup.otto.Subscribe;

public class ListHostsFragment extends BaseFragment {
	static final String FRAGMENT_TAG = "ListHostsFragment";

	@Inject HostsManager mHostsManager;
	private ListHostsAdapter mAdapter;
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new ListHostsAdapter(mActivity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mListView = new ListView(mActivity.getApplicationContext());
		mListView.setFastScrollEnabled(true);
		return mListView;
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshHosts(false);
	}

	@Subscribe
	public void onRefreshHosts(RefreshHostsEvent hosts) {
		mAdapter.updateHosts(hosts.get());
		mListView.setAdapter(mAdapter);
	}

	@Subscribe
	public void onHostAdded(AddedHostEvent event) {
		refreshHosts(false);
	}

	private void refreshHosts(boolean forceRefresh) {
		mAdapter.updateHosts(new ArrayList<Host>());
		mApp.getObjectGraph().get(ListHostsAsync.class).execute(forceRefresh);
	}

	public void addHost(Host host) {
		mApp.getObjectGraph().get(AddHostAsync.class).execute(host);
	}
}
