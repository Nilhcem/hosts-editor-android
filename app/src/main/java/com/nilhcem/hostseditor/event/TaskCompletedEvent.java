package com.nilhcem.hostseditor.event;

public class TaskCompletedEvent {

    public final String tag;
    public final boolean isSuccessful;

    public TaskCompletedEvent(String pTag, boolean pIsSuccessful) {
        this.tag = pTag;
        this.isSuccessful = pIsSuccessful;
    }
}
