package com.nilhcem.hostseditor.list;

import java.util.List;

import javax.inject.Inject;

import android.os.AsyncTask;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListFragment;
import com.nilhcem.hostseditor.HostsEditorApp;
import com.nilhcem.hostseditor.core.HostsHelper;
import com.nilhcem.hostseditor.model.Host;

public class HostsListFragment extends SherlockListFragment {

	@Inject HostsHelper mHostsHelper;
	private HostsListAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((HostsEditorApp) getSherlockActivity().getApplication()).getObjectGraph().inject(this);
		mAdapter = new HostsListAdapter(getSherlockActivity());
	}

	@Override
	public void onResume() {
		super.onResume();
		getListView().setFastScrollEnabled(true);
		new HostsGetterAsync().execute();
	}

	class HostsGetterAsync extends AsyncTask<Void, Void, List<Host>> {

		@Override
		protected List<Host> doInBackground(Void... params) {
			return mHostsHelper.getValidHosts();
		}

		@Override
		protected void onPostExecute(List<Host> result) {
			super.onPostExecute(result);
			mAdapter.updateHosts(result);
			setListAdapter(mAdapter);
		}
	}
}
