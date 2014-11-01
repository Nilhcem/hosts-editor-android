package com.nilhcem.hostseditor.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.nilhcem.hostseditor.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutDialogFragment extends SherlockDialogFragment {

    public static final String TAG = AboutDialogFragment.class.getSimpleName();
    private static final String GITHUB_URL = "https://github.com/Nilhcem/hosts-editor-android";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_dialog, container, false);
        ButterKnife.inject(this, view);
        getDialog().setTitle(R.string.about_title);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.aboutGitHub)
    void openGitHubUrl() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL));
        startActivity(browserIntent);
    }
}
