package com.nilhcem.hostseditor.addedit;

import java.util.regex.Pattern;

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
import com.nilhcem.hostseditor.bus.event.CreatedHostEvent;
import com.nilhcem.hostseditor.core.BaseFragment;
import com.nilhcem.hostseditor.model.Host;

public class AddEditHostFragment extends BaseFragment implements OnClickListener {
	public static final String TAG = "AddHostFragment";
	private static final Pattern HOSTNAME_INVALID_CHARS_PATTERN = Pattern.compile("^.*[#'\",\\\\]+.*$");

	private Host mInitialHost; // "edit mode" only - null for "add mode"
	private AlertDialog mErrorAlert;

	@InjectView(R.id.addEditHostIp) EditText mIp;
	@InjectView(R.id.addEditHostName) EditText mHostName;
	@InjectView(R.id.addEditHostButton) Button mButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_edit_host_layout, container, false);
		Views.inject(this, view);
		mButton.setOnClickListener(this);

		if (mInitialHost == null) {
			mButton.setText(R.string.add_host_title);
		} else {
			mIp.setText(mInitialHost.getIp());
			mHostName.setText(mInitialHost.getHostName());
			mButton.setText(R.string.edit_host_title);
		}
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
		if (v.getId() == R.id.addEditHostButton) {
			String ip = mIp.getText().toString();
			String hostname = mHostName.getText().toString();
			int error = checkFormErrors(ip, hostname);

			if (error == 0) {
				Host edited = new Host(ip, hostname, null, false, true);
				mBus.post(new CreatedHostEvent(mInitialHost, edited));
			} else {
				mErrorAlert = new AlertDialog.Builder(getSherlockActivity())
					.setTitle(R.string.add_edit_error_title)
					.setMessage(error)
					.setCancelable(true)
					.create();
				mErrorAlert.show();
			}
		}
	}

	public void setHostToEdit(Host toEdit) {
		mInitialHost = toEdit;
	}

	private int checkFormErrors(String ip, String hostname) {
		int error = 0;

		if (TextUtils.isEmpty(hostname) || HOSTNAME_INVALID_CHARS_PATTERN.matcher(hostname).matches()) {
			error = R.string.add_edit_error_hostname;
		}
		if (TextUtils.isEmpty(ip) || !InetAddresses.isInetAddress(ip)) {
			error = R.string.add_edit_error_ip;
		}
		return error;
	}
}
