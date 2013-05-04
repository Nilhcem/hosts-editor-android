package com.nilhcem.hostseditor.bus.event;

import com.nilhcem.hostseditor.model.Host;

public class CreatedHostEvent {
	private Host mEdited;
	private Host mOriginal;

	public CreatedHostEvent(Host original, Host edited) {
		mOriginal = original;
		mEdited = edited;
	}

	/**
	 * Returns the host (add mode), or the modified version of the Host entry (edit mode).
	 */
	public Host getHost() {
		return mEdited;
	}

	/**
	 * Returns the original version of the Host entry (edit mode) or {@code null} (add mode)
	 */
	public Host getOriginal() {
		return mOriginal;
	}
}
