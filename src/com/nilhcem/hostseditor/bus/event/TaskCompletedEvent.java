package com.nilhcem.hostseditor.bus.event;

public class TaskCompletedEvent {

	public final String tag;
	public final boolean isSuccessful;

	public TaskCompletedEvent(String tag, boolean isSuccessful) {
		this.tag = tag;
		this.isSuccessful = isSuccessful;
	}
}
