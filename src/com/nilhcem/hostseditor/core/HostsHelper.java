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

	public List<Host> getValidHosts() {
		return getHosts(true);
	}

	// Must be in an async call
	private List<Host> getHosts(boolean validHostsOnly) {
		List<Host> hosts = new ArrayList<Host>();

		try {
			List<String> lines = Files.readLines(new File(HOSTS_FILE), Charsets.UTF_8);
			for (String line : lines) {
				Host host = Host.fromString(line);
				if (host != null && (!validHostsOnly || host.isValid())) {
					hosts.add(host);
				}
			}
		} catch (IOException e) {
			Log.e(TAG, "I/O error while opening hosts file", e);
		}
		return hosts;
	}
}
