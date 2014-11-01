package com.nilhcem.hostseditor.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nilhcem.hostseditor.R;
import com.nilhcem.hostseditor.core.Host;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Custom component that implements the Checkable interface.
 *
 * @see "http://www.marvinlabs.com/2010/10/29/custom-listview-ability-check-items/"
 */
public class CheckableHostItem extends RelativeLayout implements Checkable {

    @InjectView(R.id.hostItemIp) TextView mIp;
    @InjectView(R.id.hostItemHostname) TextView mHostname;
    @InjectView(R.id.hostItemComment) TextView mComment;
    @InjectView(R.id.hostItemCheckbox) InertCheckBox mCheckbox;

    private int mTextColor;
    private int mCommentColor;

    public CheckableHostItem(Context context) {
        super(context);
        initLayout(context);
    }

    private void initLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.checkable_host_item, this, true);
        ButterKnife.inject(this, view);

        Resources res = context.getResources();
        mTextColor = res.getColor(R.color.list_hosts_entry);
        mCommentColor = res.getColor(R.color.list_hosts_comment);
    }

    public void init(Host host, int ipMinWidth, int ipMaxWidth) {
        String ip = String.format(Locale.US, "%s%s", (host.isCommented() ? Host.STR_COMMENT : ""), host.getIp());

        int textColor;
        if (host.isCommented()) {
            textColor = mCommentColor;
        } else {
            textColor = mTextColor;
        }

        mIp.setText(ip);
        mIp.setTextColor(textColor);
        mIp.setMinimumWidth(ipMinWidth);
        mIp.setMaxWidth(ipMaxWidth);
        mHostname.setText(host.getHostName());
        mHostname.setTextColor(textColor);
        mCheckbox.setChecked(false);

        String comment = host.getComment();
        if (TextUtils.isEmpty(comment)) {
            mComment.setVisibility(View.GONE);
        } else {
            mComment.setText(String.format(Locale.US, "%s%s", Host.STR_COMMENT, comment));
            mComment.setVisibility(View.VISIBLE);
        }
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
