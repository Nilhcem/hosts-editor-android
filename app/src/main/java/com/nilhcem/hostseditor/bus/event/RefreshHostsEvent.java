package com.nilhcem.hostseditor.bus.event;

import java.util.List;

import com.nilhcem.hostseditor.core.Host;

/**
 * Sent to the bus when hosts need to be refreshed.
 */
public class RefreshHostsEvent {

	public final List<Host> hosts;

	public RefreshHostsEvent(List<Host> hosts) {
		this.hosts = hosts;
	}
}
