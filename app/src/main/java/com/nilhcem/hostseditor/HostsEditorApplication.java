package com.nilhcem.hostseditor;

import android.app.Application;
import android.content.Context;
import dagger.ObjectGraph;

/**
 * Creates and provides access to Dagger's {@link ObjectGraph} instance.
 */
public class HostsEditorApplication extends Application {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        mObjectGraph = ObjectGraph.create(new HostsEditorModule());
    }

    public void inject(Object target) {
        mObjectGraph.inject(target);
    }

    public <T> T get(Class<T> type) {
        return mObjectGraph.get(type);
    }

    public static HostsEditorApplication get(Context context) {
        return (HostsEditorApplication) context.getApplicationContext();
    }
}
