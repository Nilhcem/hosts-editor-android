package com.nilhcem.hostseditor.event;

public class TaskCompletedEvent {

    public final String tag;
    public final boolean isSuccessful;

    public TaskCompletedEvent(String pTag, boolean pIsSuccessful) {
        tag = pTag;
        isSuccessful = pIsSuccessful;
    }
}
