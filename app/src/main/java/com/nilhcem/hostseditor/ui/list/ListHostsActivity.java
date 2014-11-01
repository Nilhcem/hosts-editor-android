package com.nilhcem.hostseditor.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.core.Host;
import com.nilhcem.hostseditor.event.LoadingEvent;
import com.nilhcem.hostseditor.event.StartAddEditActivityEvent;
import com.nilhcem.hostseditor.ui.BaseActivity;
import com.nilhcem.hostseditor.ui.about.AboutDialogFragment;
import com.nilhcem.hostseditor.ui.addedit.AddEditHostActivity;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

public class ListHostsActivity extends BaseActivity {

    private static final int REQUESTCODE_ADDEDIT_ACTIVITY = 1;
    private static final String STR_EMPTY = "";
    private static final String INSTANCE_STATE_LOADING = "loading";
    private static final String INSTANCE_STATE_LOADING_MESSAGE = "loadingMessage";
    private static final String INSTANCE_STATE_SEARCH = "search";

    @InjectView(R.id.listLoading) ProgressBar mProgressBar;
    @InjectView(R.id.listLoadingMsg) TextView mLoadingMsg;

    private ListHostsFragment mFragment;
    private String mSearchQuery = STR_EMPTY;
    private MenuItem mSearchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_hosts_layout);
        ButterKnife.inject(this);

        FragmentManager fm = getSupportFragmentManager();
        mFragment = (ListHostsFragment) fm.findFragmentById(R.id.listHostsFragment);

        if (savedInstanceState == null) {
            onLoadingEvent(new LoadingEvent(true, R.string.loading_hosts));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.list_menu, menu);

        // Init search
        mSearchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearchItem.getActionView();
        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.action_search));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mSearchItem.collapseActionView();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Timber.d("Search query: %s", newText);
                    setSearchQuery(newText);
                    mFragment.filterList(newText);
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFragment.filterList(mSearchQuery);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (!TextUtils.isEmpty(mSearchQuery)) {
                    // Revert search
                    setSearchQuery(STR_EMPTY);
                    mFragment.filterList(mSearchQuery);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_SEARCH:
                mSearchItem.expandActionView();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_host:
                mBus.post(new StartAddEditActivityEvent(null));
                return true;
            case R.id.action_reload_hosts:
                mFragment.refreshHosts(true);
                return true;
            case R.id.action_select_all:
                mFragment.selectAll();
                return true;
            case R.id.action_about:
                displayAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Timber.d("Activity result received");
        if (requestCode == REQUESTCODE_ADDEDIT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                Host modified = data.getParcelableExtra(AddEditHostActivity.EXTRA_HOST_MODIFIED);
                Host original = data.getParcelableExtra(AddEditHostActivity.EXTRA_HOST_ORIGINAL);

                boolean addMode = (original == null);
                onLoadingEvent(new LoadingEvent(true, addMode ? R.string.loading_add : R.string.loading_edit));
                mFragment.addEditHost(addMode, new Host[]{modified, original});
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(INSTANCE_STATE_LOADING, mProgressBar.getVisibility() == View.VISIBLE);
        outState.putString(INSTANCE_STATE_LOADING_MESSAGE, mLoadingMsg.getText().toString());
        outState.putString(INSTANCE_STATE_SEARCH, mSearchQuery);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boolean isLoading = savedInstanceState.getBoolean(INSTANCE_STATE_LOADING, true);

        if (isLoading) {
            String loadingMsg = savedInstanceState.getString(INSTANCE_STATE_LOADING_MESSAGE);
            if (loadingMsg == null) {
                loadingMsg = STR_EMPTY;
            }
            onLoadingEvent(new LoadingEvent(true, loadingMsg));
        }
        setSearchQuery(savedInstanceState.getString(INSTANCE_STATE_SEARCH));
    }

    @Subscribe
    public void onStartAddEditActivityEvent(StartAddEditActivityEvent event) {
        Timber.d("Ready to start AddEditActivity");
        Intent intent = new Intent(this, AddEditHostActivity.class);
        intent.putExtra(AddEditHostActivity.EXTRA_HOST_ORIGINAL, event.host);
        startActivityForResult(intent, REQUESTCODE_ADDEDIT_ACTIVITY);
    }

    @Subscribe
    public void onLoadingEvent(LoadingEvent event) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        mLoadingMsg.setText(event.getMessage(this));
        if (event.isLoading()) {
            Timber.d("Start loading");
            if (mSearchItem != null) {
                setSearchQuery(STR_EMPTY);
                mSearchItem.collapseActionView();
            }

            mProgressBar.setVisibility(View.VISIBLE);
            mLoadingMsg.setVisibility(View.VISIBLE);
            ft.hide(mFragment);
        } else {
            Timber.d("Stop loading");
            mProgressBar.setVisibility(View.GONE);
            mLoadingMsg.setVisibility(View.GONE);
            ft.show(mFragment);
        }
        ft.commitAllowingStateLoss();
    }

    private void setSearchQuery(String searchQuery) {
        mSearchQuery = searchQuery;
        setActionBarTitle();
    }

    private void setActionBarTitle() {
        int titleRes;

        if (TextUtils.isEmpty(mSearchQuery)) {
            titleRes = R.string.app_name;
        } else {
            titleRes = R.string.action_search_results;
        }
        getSupportActionBar().setTitle(titleRes);
    }

    private void displayAboutDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AboutDialogFragment dialog = new AboutDialogFragment();
        dialog.show(fm, AboutDialogFragment.TAG);
    }
}
