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
        this.mIsLoading = isLoading;
        this.mMessageRes = messageRes;
        this.mMessage = null;
    }

    public LoadingEvent(boolean isLoading, String message) {
        this.mIsLoading = isLoading;
        this.mMessage = message;
        this.mMessageRes = 0;
    }

    public boolean isLoading() {
        return this.mIsLoading;
    }

    public String getMessage(Context context) {
        if (this.mMessage == null) {
            return context.getString(mMessageRes);
        }
        return this.mMessage;
    }
}
