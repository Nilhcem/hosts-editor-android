package com.nilhcem.hostseditor.ui.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.nilhcem.hostseditor.core.Host;
import com.nilhcem.hostseditor.core.util.Compatibility;
import com.nilhcem.hostseditor.core.util.ThreadPreconditions;
import com.nilhcem.hostseditor.ui.widget.CheckableHostItem;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class ListHostsAdapter extends BaseAdapter implements Filterable {

    private final ListHostsSearchFilter mSearchFilter;

    private List<Host> mHosts = Collections.emptyList();
    private Context mAppContext;

    private int mIpMinWidth;
    private int mIpMaxWidth;

    @Inject public ListHostsAdapter(ListHostsSearchFilter searchFilter) {
        mSearchFilter = searchFilter;
    }

    public void init(Context appContext) {
        mAppContext = appContext;
    }

    public void updateHosts(List<Host> hosts) {
        ThreadPreconditions.checkOnMainThread();
        if (hosts == null) {
            mHosts = Collections.emptyList();
        } else {
            mHosts = hosts;
        }
        notifyDataSetChanged();
    }

    public void computeViewWidths(Context context) {
        // The IP column can be very large, especially when it holds ipv6 with 39 chars.
        // Its size has to be generated programmatically, as it should fit with the device's width.
        // A tablet can afford having a large column but not a phone.

        int screenWidth = Compatibility.getScreenDimensions(context).x;
        Timber.d("Screen width: %d", screenWidth);

        // 1: Compute minimum width.
        // Min width must be between [100dp, 160dp]. If possible, 30% of screen width.
        int minWidth = screenWidth * 30 / 100;
        int minRange = Compatibility.convertDpToIntPixel(100f, context);
        int maxRange = Compatibility.convertDpToIntPixel(160f, context);

        if (minWidth < minRange) {
            minWidth = minRange;
        }
        if (minWidth > maxRange) {
            minWidth = maxRange;
        }

        // 2: Compute maximum width, usually 35% of screen width.
        int maxWidth = screenWidth * 35 / 100;
        if (maxWidth < minWidth) {
            maxWidth = minWidth;
        }

        Timber.d("Min width: %d - Max width: %d", minWidth, maxWidth);
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
        CheckableHostItem view;

        if (convertView == null) {
            view = new CheckableHostItem(mAppContext);
        } else {
            view = (CheckableHostItem) convertView;
        }

        Host host = getItem(position);
        view.init(host, mIpMinWidth, mIpMaxWidth);
        return view;
    }

    @Override
    public Filter getFilter() {
        return mSearchFilter;
    }
}
