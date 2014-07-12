package com.nilhcem.hostseditor;

import android.app.Application;
import com.nilhcem.hostseditor.bus.HostsEditorModule;
import dagger.ObjectGraph;

/**
 * Creates and provides access to Dagger's {@link ObjectGraph} instance.
 */
public class HostsEditorApp extends Application {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        mObjectGraph = ObjectGraph.create(new HostsEditorModule());
    }

    public ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }
}
