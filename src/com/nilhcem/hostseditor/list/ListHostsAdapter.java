package com.nilhcem.hostseditor.list;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

import com.nilhcem.hostseditor.model.Host;
import com.nilhcem.hostseditor.util.ThreadPreconditions;
import com.nilhcem.hostseditor.widget.CheckableHostItem;

public class ListHostsAdapter extends BaseAdapter {
	private List<Host> mHosts = Collections.emptyList();
	private Context mAppContext;

	public ListHostsAdapter(Context context) {
		mAppContext = context;
	}

	public void updateHosts(List<Host> hosts) {
		ThreadPreconditions.checkOnMainThread();
		mHosts = hosts;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mHosts.size();
	}

	@Override
	public Host getItem(int position) {
		return mHosts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new CheckableHostItem(mAppContext);
			convertView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}

		Host host = getItem(position);
		((CheckableHostItem) convertView).init(host);
		return convertView;
	}
}
