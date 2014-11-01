package com.nilhcem.hostseditor.core.util;

import android.os.Looper;

import com.nilhcem.hostseditor.BuildConfig;

public final class ThreadPreconditions {

    private ThreadPreconditions() {
        throw new UnsupportedOperationException();
    }

    public static void checkOnMainThread() {
        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                throw new IllegalStateException("This method should be called from the Main Thread");
            }
        }
    }
}
