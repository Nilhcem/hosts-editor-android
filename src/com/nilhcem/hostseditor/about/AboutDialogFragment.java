package com.nilhcem.hostseditor.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.InjectView;
import butterknife.Views;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.nilhcem.hostseditor.R;

public class AboutDialogFragment extends SherlockDialogFragment implements View.OnClickListener {
	public static final String TAG = "AboutDialogFragment";
	private static final String GITHUB_URL = "https://github.com/Nilhcem/hosts-editor-android";

	@InjectView(R.id.aboutGitHub) Button mGitHubBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.about_dialog, container, false);
		Views.inject(this, view);
		getDialog().setTitle(R.string.about_title);

		mGitHubBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL));
		startActivity(browserIntent);
	}
}
