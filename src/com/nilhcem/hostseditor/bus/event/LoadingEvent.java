package com.nilhcem.hostseditor.bus.event;

public class LoadingEvent {
	private boolean mIsLoading;

	public LoadingEvent(boolean isLoading) {
		mIsLoading = isLoading;
	}

	public boolean isLoading() {
		return mIsLoading;
	}
}
