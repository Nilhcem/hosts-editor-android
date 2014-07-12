package com.nilhcem.hostseditor.core.logging;

import timber.log.Timber;

public class ReleaseLogTree extends Timber.DebugTree {

    @Override
    public void d(String message, Object... args) {
        // Do not log
    }

    @Override
    public void d(Throwable t, String message, Object... args) {
        // Do not log
    }

    @Override
    public void i(String message, Object... args) {
        // Do not log
    }

    @Override
    public void i(Throwable t, String message, Object... args) {
        // Do not log
    }

    @Override
    public void w(String message, Object... args) {
        // Do not log
    }

    @Override
    public void w(Throwable t, String message, Object... args) {
        // Do not log
    }
}
