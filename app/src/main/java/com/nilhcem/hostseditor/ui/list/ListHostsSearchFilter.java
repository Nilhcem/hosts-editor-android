package com.nilhcem.hostseditor.ui.list;

import android.text.TextUtils;
import android.widget.Filter;
import com.nilhcem.hostseditor.event.RefreshHostsEvent;
import com.nilhcem.hostseditor.core.Host;
import com.nilhcem.hostseditor.core.HostsManager;
import com.nilhcem.hostseditor.core.util.Log;
import com.squareup.otto.Bus;

import javax.inject.Inject;
import java.util.List;

class ListHostsSearchFilter extends Filter {

    private static final String TAG = ListHostsSearchFilter.class.getSimpleName();

    @Inject Bus mBus;
    @Inject HostsManager mHostsManager;

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (!TextUtils.isEmpty(constraint)) {
            Log.d(TAG, "Publishing result for: %s", constraint);
        }
        mBus.post(new RefreshHostsEvent((List<Host>) results.values));
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        if (!TextUtils.isEmpty(constraint)) {
            Log.d(TAG, "Perform filtering for: %s", constraint);
        }
        FilterResults results = new FilterResults();
        results.values = mHostsManager.filterHosts(constraint);
        return results;
    }
}
