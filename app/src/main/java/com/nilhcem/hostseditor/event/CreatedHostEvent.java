package com.nilhcem.hostseditor.event;

import com.nilhcem.hostseditor.core.Host;

public class CreatedHostEvent {

    /**
     * Host (add mode), or the modified version of the Host entry (edit mode).
     */
    public final Host editedHost;

    /**
     * Original version of the Host entry (edit mode) or {@code null} (add mode).
     */
    public final Host originalHost;

    public CreatedHostEvent(Host original, Host edited) {
        originalHost = original;
        editedHost = edited;
    }
}
