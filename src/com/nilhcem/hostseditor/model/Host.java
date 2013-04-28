package com.nilhcem.hostseditor.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.net.InetAddresses;

import android.text.TextUtils;

public class Host {
	private static final String STR_COMMENT = "#";
	private static final String STR_SEPARATOR = "\\t";
	private static final String HOST_PATTERN_STR = "^\\s*(" + STR_COMMENT + "?)\\s*(\\S*)\\s*(.*)$";
	private static final Pattern HOST_PATTERN = Pattern.compile(HOST_PATTERN_STR);

	private final String mIp;
	private final String mHostName;
	private final boolean mIsCommented;
	private final boolean mIsValid;

	public Host(String ip, String hostName, boolean isCommented, boolean isValid) {
		mIp = ip;
		mHostName = hostName;
		mIsCommented = isCommented;
		mIsValid = isValid;
	}

	public String getIp() {
		return mIp;
	}

	public String getHostName() {
		return mHostName;
	}

	public boolean isValid() {
		return mIsValid;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (mIsCommented) {
			sb.append(STR_COMMENT);
		}
		if (mIp != null) {
			sb.append(mIp).append(STR_SEPARATOR);
		}
		if (mHostName != null) {
			sb.append(mHostName);
		}
		return sb.toString();
	}

	public static Host fromString(String line) {
		Matcher matcher = HOST_PATTERN.matcher(line);
		String ip = null;
		String hostname = null;
		boolean isCommented = false;
		boolean isValid = false;

		if (matcher.find()) {
			isCommented = !TextUtils.isEmpty(matcher.group(1));
			ip = matcher.group(2);
			hostname = matcher.group(3);

			isValid = !TextUtils.isEmpty(ip) && !TextUtils.isEmpty(hostname)
					&& InetAddresses.isInetAddress(ip);
		}
		return new Host(ip, hostname, isCommented, isValid);
	}
}
