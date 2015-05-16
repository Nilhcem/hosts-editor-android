package com.nilhcem.hostseditor;

import android.app.Application;
import android.content.Context;

import com.nilhcem.hostseditor.core.dagger.DaggerHostsEditorComponent;
import com.nilhcem.hostseditor.core.dagger.HostsEditorComponent;
import com.nilhcem.hostseditor.core.dagger.HostsEditorModule;
import com.nilhcem.hostseditor.core.log.ReleaseTree;

import timber.log.Timber;

public class HostsEditorApplication extends Application {

    private HostsEditorComponent mComponent;

    public static HostsEditorApplication get(Context context) {
        return (HostsEditorApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLogger();
        initGraph();
    }

    public HostsEditorComponent component() {
        return mComponent;
    }

    private void initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }
    }

    private void initGraph() {
        mComponent = DaggerHostsEditorComponent.builder()
                .hostsEditorModule(new HostsEditorModule())
                .build();
    }
}
