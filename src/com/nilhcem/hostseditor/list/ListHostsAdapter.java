package com.nilhcem.hostseditor.list;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.nilhcem.hostseditor.core.Host;
import com.nilhcem.hostseditor.util.Compatibility;
import com.nilhcem.hostseditor.util.Log;
import com.nilhcem.hostseditor.util.ThreadPreconditions;
import com.nilhcem.hostseditor.widget.CheckableHostItem;

public class ListHostsAdapter extends BaseAdapter implements Filterable {
	private static final String TAG = "ListHostsAdapter";

	private List<Host> mHosts = Collections.emptyList();
	private Context mAppContext;
	@Inject ListHostsSearchFilter mSearchFilter;

	private int mIpMinWidth;
	private int mIpMaxWidth;

	public void init(Context appContext) {
		mAppContext = appContext;
	}

	public void updateHosts(List<Host> hosts) {
		ThreadPreconditions.checkOnMainThread();
		mHosts = hosts;
		notifyDataSetChanged();
	}

	public void computeViewWidths(Context context) {
		// The IP column can be very large, especially when it holds ipv6 with 39 chars.
		// Its size has to be generated programmatically, as it should fit with the device's width.
		// A tablet can afford having a large column but not a phone.

		int screenWidth = Compatibility.getScreenDimensions(context).x;
		Log.d(TAG, "Screen width: %d", screenWidth);

		// Step 1: Compute minimum width.
		// Min width must be between [100dp, 160dp]. If possible, 30% of screen width.
		int minWidth = screenWidth * 30 / 100;
		int minRange = Math.round(Compatibility.convertDpToPixel(100f, context));
		int maxRange = Math.round(Compatibility.convertDpToPixel(160f, context));

		if (minWidth < minRange) {
			minWidth = minRange;
		}
		if (minWidth > maxRange) {
			minWidth = maxRange;
		}

		// Step 2: Compute maximum width, usually 35% of screen width.
		int maxWidth = screenWidth * 35 / 100;
		if (maxWidth < minWidth) {
			maxWidth = minWidth;
		}

		Log.d(TAG, "Min width: %d - Max width: %d", minWidth, maxWidth);
		mIpMinWidth = minWidth;
		mIpMaxWidth = maxWidth;
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
		((CheckableHostItem) convertView).init(host, mIpMinWidth, mIpMaxWidth);
		return convertView;
	}

	@Override
	public Filter getFilter() {
		return mSearchFilter;
	}
}
