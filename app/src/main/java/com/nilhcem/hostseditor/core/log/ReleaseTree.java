package com.nilhcem.hostseditor.core.log;

import android.util.Log;

import timber.log.Timber;

public class ReleaseTree extends Timber.Tree {

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        // Only log ERROR, and WTF.
        if (priority > Log.WARN) {
            Log.println(priority, tag, message);
        }
    }
}
