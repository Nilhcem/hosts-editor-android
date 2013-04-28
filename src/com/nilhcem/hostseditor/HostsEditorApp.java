package com.nilhcem.hostseditor;

import com.nilhcem.hostseditor.core.HostsModule;

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
		mObjectGraph = ObjectGraph.create(new HostsModule());
	}

	public ObjectGraph getObjectGraph() {
		return mObjectGraph;
	}
}
