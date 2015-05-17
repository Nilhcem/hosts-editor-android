package com.nilhcem.hostseditor.ui.addedit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.core.Host;
import com.nilhcem.hostseditor.event.CreatedHostEvent;
import com.nilhcem.hostseditor.ui.BaseActivity;
import com.nilhcem.hostseditor.ui.list.ListHostsActivity;
import com.squareup.otto.Subscribe;

public class AddEditHostActivity extends BaseActivity {

    public static final String EXTRA_HOST_ORIGINAL = "hostOriginal"; // useful in Edit mode, this is the original Host entry before being edited.
    public static final String EXTRA_HOST_MODIFIED = "hostModified"; // the new Host entry, after having been edited.

    private AddEditHostFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Host hostToEdit = getHostFromIntent();
        initActionBar(hostToEdit);

        // Add fragment
        FragmentManager fm = getSupportFragmentManager();
        mFragment = (AddEditHostFragment) fm.findFragmentByTag(AddEditHostFragment.TAG);
        if (mFragment == null) {
            mFragment = AddEditHostFragment.newInstance(hostToEdit);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(android.R.id.content, mFragment, AddEditHostFragment.TAG);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_add_rm_comment);
        if (mFragment.hasComment()) {
            item.setTitle(R.string.action_remove_comment);
        } else {
            item.setTitle(R.string.action_add_comment);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ListHostsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.action_add_rm_comment:
                mFragment.toggleCommentVisibility();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onHostCreatedFromFragment(CreatedHostEvent event) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_HOST_ORIGINAL, event.originalHost);
        returnIntent.putExtra(EXTRA_HOST_MODIFIED, event.editedHost);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private Host getHostFromIntent() {
        Host host = null;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            host = bundle.getParcelable(EXTRA_HOST_ORIGINAL);
        }
        return host;
    }

    private void initActionBar(Host hostToEdit) {
        int titleRes;
        if (hostToEdit == null) {
            titleRes = R.string.add_host_title;
        } else {
            titleRes = R.string.edit_host_title;
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(titleRes);
    }
}
