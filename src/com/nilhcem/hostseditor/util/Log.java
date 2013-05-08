package com.nilhcem.hostseditor.util;

import java.util.IllegalFormatException;
import java.util.Locale;

public final class Log {
	private static final int LOG_LEVEL = 4; // 1: error, 2: warn, 3: info, 4: debug

	private static final boolean LOG_ERROR = LOG_LEVEL > 0;
	private static final boolean LOG_WARN = LOG_LEVEL > 1;
	private static final boolean LOG_INFO = LOG_LEVEL > 2;
	private static final boolean LOG_DEBUG = LOG_LEVEL > 3;

	private Log() {
		throw new UnsupportedOperationException();
	}

	public static void d(String tag, String msgFormat, Object... args) {
		if (LOG_DEBUG) {
			try {
				android.util.Log.d(tag, String.format(Locale.US, msgFormat, args));
			} catch (NullPointerException e) {
				// Do nothing
			} catch (IllegalFormatException e) {
				android.util.Log.d(tag, msgFormat);
			}
		}
	}

	public static void w(String tag, String msgFormat, Object... args) {
		if (LOG_WARN) {
			try {
				android.util.Log.w(tag, String.format(Locale.US, msgFormat, args));
			} catch (NullPointerException e) {
				// Do nothing
			} catch (IllegalFormatException e) {
				android.util.Log.w(tag, msgFormat);
			}
		}
	}

	public static void i(String tag, String msgFormat, Object... args) {
		if (LOG_INFO) {
			try {
				android.util.Log.i(tag, String.format(Locale.US, msgFormat, args));
			} catch (NullPointerException e) {
				// Do nothing
			} catch (IllegalFormatException e) {
				android.util.Log.i(tag, msgFormat);
			}
		}
	}

	public static void e(String tag, Throwable t) {
		if (LOG_ERROR) {
			android.util.Log.e(tag, "", t);
		}
	}

	public static void e(String tag, String msgFormat, Object... args) {
		if (LOG_ERROR) {
			try {
				android.util.Log.e(tag, String.format(Locale.US, msgFormat, args));
			} catch (NullPointerException e) {
				// Do nothing
			} catch (IllegalFormatException e) {
				android.util.Log.e(tag, msgFormat);
			}
		}
	}

	public static void e(String tag, Throwable t, String msgFormat, Object... args) {
		if (LOG_ERROR) {
			try {
				android.util.Log.e(tag, String.format(Locale.US, msgFormat, args), t);
			} catch (NullPointerException e) {
				// Do nothing
			} catch (IllegalFormatException e) {
				android.util.Log.e(tag, msgFormat, t);
			}
		}
	}
}
