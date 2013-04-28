package com.nilhcem.hostseditor;


import dagger.ObjectGraph;
import android.app.Application;

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
