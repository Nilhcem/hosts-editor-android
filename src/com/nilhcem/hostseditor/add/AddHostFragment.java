package com.nilhcem.hostseditor.add;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.InjectView;
import butterknife.Views;

import com.google.common.net.InetAddresses;
import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.core.BaseFragment;
import com.nilhcem.hostseditor.model.Host;

public class AddHostFragment extends BaseFragment implements OnClickListener {
	private AlertDialog mErrorAlert;
	@InjectView(R.id.addHostIp) EditText mIp;
	@InjectView(R.id.addHostName) EditText mHostName;
	@InjectView(R.id.addHostButton) Button mButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_host_layout, container, false);
		Views.inject(this, view);
		mButton.setOnClickListener(this);
		return view;
	}

	@Override
	public void onStop() {
		if (mErrorAlert != null) {
			mErrorAlert.dismiss();
		}
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.addHostButton) {
			String ip = mIp.getText().toString();
			String hostname = mHostName.getText().toString();
			int error = checkFormErrors(ip, hostname);

			if (error == 0) {
				mBus.post(new Host(ip, hostname, false, true));
			} else {
				mErrorAlert = new AlertDialog.Builder(getSherlockActivity())
					.setTitle(R.string.add_host_error_title)
					.setMessage(error)
					.setCancelable(true)
					.create();
				mErrorAlert.show();
			}
		}
	}

	private int checkFormErrors(String ip, String hostname) {
		int error = 0;

		if (TextUtils.isEmpty(hostname)) {
			error = R.string.add_host_error_hostname;
		}
		if (TextUtils.isEmpty(ip) || !InetAddresses.isInetAddress(ip)) {
			error = R.string.add_host_error_ip;
		}
		return error;
	}
}
