package com.nilhcem.hostseditor.bus.event;

import java.util.List;

import com.nilhcem.hostseditor.model.Host;

/**
 * Sent to the bus when hosts need to be refreshed.
 */
public class RefreshHostsEvent {
	private List<Host> mHosts;

	public RefreshHostsEvent(List<Host> hosts) {
		mHosts = hosts;
	}

	public List<Host> get() {
		return mHosts;
	}
}
