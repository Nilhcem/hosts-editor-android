package com.nilhcem.hostseditor.bus.event;

import android.content.Context;

public class LoadingEvent {

	private boolean mIsLoading;
	private int mMessageRes;
	private String mMessage;

	public LoadingEvent(boolean isLoading) {
		this(isLoading, "");
	}

	public LoadingEvent(boolean isLoading, int messageRes) {
		mIsLoading = isLoading;
		mMessageRes = messageRes;
	}

	public LoadingEvent(boolean isLoading, String message) {
		mIsLoading = isLoading;
		mMessage = message;
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
