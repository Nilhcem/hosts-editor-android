package com.nilhcem.hostseditor.event;

import com.nilhcem.hostseditor.core.Host;

public class StartAddEditActivityEvent {

    /**
     * The Host entry to modify (edit mode), or {@code null} (add mode).
     */
    public final Host host;

    public StartAddEditActivityEvent(Host addEditHost) {
        host = addEditHost;
    }
}
