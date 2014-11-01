package com.nilhcem.hostseditor.ui.list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.core.Host;
import com.nilhcem.hostseditor.event.LoadingEvent;
import com.nilhcem.hostseditor.event.RefreshHostsEvent;
import com.nilhcem.hostseditor.event.StartAddEditActivityEvent;
import com.nilhcem.hostseditor.event.TaskCompletedEvent;
import com.nilhcem.hostseditor.task.AddEditHostAsync;
import com.nilhcem.hostseditor.task.GenericTaskAsync;
import com.nilhcem.hostseditor.task.ListHostsAsync;
import com.nilhcem.hostseditor.task.RemoveHostsAsync;
import com.nilhcem.hostseditor.task.ToggleHostsAsync;
import com.nilhcem.hostseditor.ui.BaseFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

public class ListHostsFragment extends BaseFragment implements OnItemClickListener, OnItemLongClickListener {

    @Inject ListHostsAdapter mAdapter;
    @InjectView(R.id.listHosts) ListView mListView;

    private ActionMode mMode;
    private MenuItem mEditMenuItem;
    private Dialog mDisplayedDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter.init(mActivity.getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean firstCall = (mListView == null);

        View view = inflater.inflate(R.layout.list_hosts_fragment, container, false);
        ButterKnife.inject(this, view);

        mListView.setAdapter(mAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setFastScrollEnabled(true);
        mListView.setItemsCanFocus(false);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        View emptyLayout = view.findViewById(R.id.listEmptyLayout);
        if (emptyLayout != null) {
            mListView.setEmptyView(emptyLayout);
        }

        mAdapter.computeViewWidths(mActivity);

        if (firstCall) {
            refreshHosts(false);
        }
        return view;
    }

    @Override
    public void onPause() {
        finishActionMode();
        if (mDisplayedDialog != null) {
            mDisplayedDialog.dismiss();
            mDisplayedDialog = null;
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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
        Timber.d("Task %s finished", task.tag);
        if (task.isSuccessful) {
            refreshHosts(false);
        } else {
            displayErrorDialog();
        }
    }

    @Subscribe
    public void onHostsRefreshed(RefreshHostsEvent hosts) {
        Timber.d("Refreshing listview");
        finishActionMode();
        mAdapter.updateHosts(hosts.hosts);
        mBus.post(new LoadingEvent());
    }

    public void addEditHost(boolean addMode, Host[] hosts) {
        runGenericTask(AddEditHostAsync.class, hosts, addMode);
    }

    public void refreshHosts(boolean forceRefresh) {
        mApp.get(ListHostsAsync.class).execute(forceRefresh);
    }

    public void selectAll() {
        int nbElem = mAdapter.getCount();

        if (nbElem > 0) {
            for (int i = 0; i < nbElem; i++) {
                mListView.setItemChecked(i, true);
            }
            displayActionMode(nbElem);
        }
    }

    public void filterList(String filter) {
        mAdapter.getFilter().filter(filter);
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
                    displayDeleteConfirmationDialog(selectedItems);
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
            for (int i = 0; i < len; i++) {
                if (checked.get(i)) {
                    items.add(mAdapter.getItem(i));
                }
            }
            return items.toArray(new Host[items.size()]);
        }
    }

    private void displayDeleteConfirmationDialog(final Host[] selectedItems) {
        mDisplayedDialog = new AlertDialog.Builder(mActivity)
                .setTitle(R.string.delete_dialog_title)
                .setMessage(getResources().getQuantityText(R.plurals.delete_dialog_content, selectedItems.length))
                .setPositiveButton(R.string.delete_dialog_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runGenericTask(RemoveHostsAsync.class, selectedItems);
                    }
                })
                .setNegativeButton(R.string.delete_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .create();
        mDisplayedDialog.show();
    }

    private void displayErrorDialog() {
        mBus.post(new LoadingEvent());
        mDisplayedDialog = new AlertDialog.Builder(mActivity)
                .setTitle(R.string.list_error_title)
                .setMessage(R.string.list_error_content)
                .setNeutralButton(R.string.list_error_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refreshHosts(true);
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        refreshHosts(true);
                    }
                })
                .create();
        mDisplayedDialog.show();
    }

    private void runGenericTask(Class<? extends GenericTaskAsync> clazz, Host[] hosts) {
        runGenericTask(clazz, hosts, hosts.length == 1);
    }

    private void runGenericTask(Class<? extends GenericTaskAsync> clazz, Host[] hosts, boolean flagMsg) {
        GenericTaskAsync task = mApp.get(clazz);
        task.init(mActivity.getApplicationContext(), flagMsg);
        task.execute(hosts);
    }

    private void finishActionMode() {
        if (mMode != null) {
            mMode.finish();
        }
    }
}
