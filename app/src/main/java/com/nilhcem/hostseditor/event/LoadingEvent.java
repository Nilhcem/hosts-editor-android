package com.nilhcem.hostseditor.event;

import android.content.Context;

public class LoadingEvent {

    private final boolean mIsLoading;
    private final int mMessageRes;
    private final String mMessage;

    public LoadingEvent() {
        this(false, "");
    }

    public LoadingEvent(boolean isLoading, int messageRes) {
        mIsLoading = isLoading;
        mMessageRes = messageRes;
        mMessage = null;
    }

    public LoadingEvent(boolean isLoading, String message) {
        mIsLoading = isLoading;
        mMessage = message;
        mMessageRes = 0;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public String getMessage(Context context) {
        if (mMessage == null) {
            return context.getString(mMessageRes);
        }
        return mMessage;
    }
}
