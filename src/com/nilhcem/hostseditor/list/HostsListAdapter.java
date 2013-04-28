package com.nilhcem.hostseditor.list;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.Views;

import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.model.Host;
import com.nilhcem.hostseditor.utils.ThreadPreconditions;

public class HostsListAdapter extends BaseAdapter {
	private List<Host> mHosts = Collections.emptyList();
	private final LayoutInflater mInflater;

	public HostsListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
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
		ViewHolder holder;

		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(R.layout.hosts_list_item, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}

		Host host = getItem(position);
		holder.mIp.setText(host.getIp());
		holder.mHost.setText(host.getHostName());

		return convertView;
	}

	static class ViewHolder {
		@InjectView(R.id.hostItemIp)
		TextView mIp;
		@InjectView(R.id.hostItemHostname)
		TextView mHost;

		public ViewHolder(View view) {
			Views.inject(this, view);
		}
	}
}
