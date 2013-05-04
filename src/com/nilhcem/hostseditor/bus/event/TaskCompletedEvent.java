package com.nilhcem.hostseditor.bus.event;

public class TaskCompletedEvent {
	private String mTag;
	private boolean mSuccess;

	public TaskCompletedEvent(String tag, boolean success) {
		mTag = tag;
		mSuccess = success;
	}

	public boolean isSuccessful() {
		return mSuccess;
	}

	public String getTag() {
		return mTag;
	}
}
