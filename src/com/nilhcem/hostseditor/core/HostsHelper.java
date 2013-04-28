package com.nilhcem.hostseditor.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.nilhcem.hostseditor.model.Host;

@Singleton
public class HostsHelper {
	private static final String TAG = "HostsHelper";
	private static final String HOSTS_FILE = "/etc/hosts";

	private List<Host> mHosts = null; // Do not access this field directly even in the same class, use getAllHosts() instead.

	public List<Host> getValidHosts() {
		List<Host> hosts = new ArrayList<Host>();

		for (Host host : getAllHosts()) {
			if (host.isValid()) {
				hosts.add(host);
			}
		}
		return hosts;
	}

	public void addHost(Host host) {
		// TODO
	}

	// Must be in an async call
	private List<Host> getAllHosts() {
		if (mHosts == null) {
			mHosts = new ArrayList<Host>();

			try {
				List<String> lines = Files.readLines(new File(HOSTS_FILE), Charsets.UTF_8);
				for (String line : lines) {
					Host host = Host.fromString(line);
					if (host != null) {
						mHosts.add(host);
					}
				}
			} catch (IOException e) {
				Log.e(TAG, "I/O error while opening hosts file", e);
			}
		}
		return mHosts;
	}
}
