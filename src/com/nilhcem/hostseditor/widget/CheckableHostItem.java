package com.nilhcem.hostseditor.widget;

import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.Views;

import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.model.Host;

/**
 * Custom component that implements the Checkable interface.
 * @see {@link http://www.marvinlabs.com/2010/10/29/custom-listview-ability-check-items/}
 */
public class CheckableHostItem extends RelativeLayout implements Checkable {
	@InjectView(R.id.hostItemIp)
	TextView mIp;
	@InjectView(R.id.hostItemHostname)
	TextView mHostname;
	@InjectView(R.id.hostItemCheckbox)
	InertCheckBox mCheckbox;

	public CheckableHostItem(Context context) {
		super(context);
		initLayout(context);
	}

	public CheckableHostItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLayout(context);
	}

	public void initLayout(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.checkable_host_item, this, true);
		Views.inject(this, view);
	}

	public void init(Host host) {
		String ip = String.format(Locale.US, "%s%s", (host.isCommented() ? Host.STR_COMMENT : ""), host.getIp());

		int textColor;
		if (host.isCommented()) {
			textColor = 0xffb0b0b0;
		} else {
			textColor = 0xffffffff;
		}

		mIp.setText(ip);
		mIp.setTextColor(textColor);
		mHostname.setText(host.getHostName());
		mHostname.setTextColor(textColor);
		mCheckbox.setChecked(false);
	}

	@Override
	public boolean isChecked() {
		return mCheckbox.isChecked();
	}

	@Override
	public void setChecked(boolean checked) {
		mCheckbox.setChecked(checked);
	}

	@Override
	public void toggle() {
		mCheckbox.toggle();
	}
}
