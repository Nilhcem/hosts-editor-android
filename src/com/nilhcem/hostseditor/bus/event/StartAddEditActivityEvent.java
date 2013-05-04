package com.nilhcem.hostseditor.bus.event;

import com.nilhcem.hostseditor.model.Host;

public class StartAddEditActivityEvent {
	private final Host mHost;

	public StartAddEditActivityEvent(Host host) {
		mHost = host;
	}

	/**
	 * Returns the Host entry to modify (edit mode), or {@code null} (add mode).
	 */
	public Host getHost() {
		return mHost;
	}
}
