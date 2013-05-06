package com.nilhcem.hostseditor.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;
import javax.inject.Singleton;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.nilhcem.hostseditor.model.Host;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

@Singleton
public class HostsManager {
	private static final String TAG = "HostsManager";

	private static final String HOSTS_FILE_NAME = "hosts";
	private static final String HOSTS_FILE_PATH = "/system/etc/" + HOSTS_FILE_NAME;

	private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
	private static final String MOUNT_TYPE_RO = "ro";
	private static final String MOUNT_TYPE_RW = "rw";
	private static final String COMMAND_RM = "rm -f";
	private static final String COMMAND_CHOWN = "chown 0:0";
	private static final String COMMAND_CHMOD_644 = "chmod 644";

	// Do not access this field directly even in the same class, use getAllHosts() instead.
	private List<Host> mHosts = null;

	@Inject
	public HostsManager() {
	}

	/**
	 * Gets all host entries from hosts file.
	 * <p><b>Must be in an async call.</b></p>
	 *
	 * @param forceRefresh if we want to force using the hosts file (not the cache)
	 * @return a list of host entries
	 */
	public synchronized List<Host> getHosts(boolean forceRefresh) {
		if (mHosts == null || forceRefresh) {
			mHosts = Collections.synchronizedList(new ArrayList<Host>());

			BufferedReader reader = null;
			try {
				reader = Files.newReader(new File(HOSTS_FILE_PATH), Charsets.UTF_8);

				String line;
				while ((line = reader.readLine()) != null) {
					Host host = Host.fromString(line);
					if (host != null) {
						mHosts.add(host);
					}
				}
			} catch (IOException e) {
				Log.e(TAG, "I/O error while opening hosts file", e);
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					Log.e(TAG, "Error while closing reader", e);
				}
			}
		}
		return mHosts;
	}

	/**
	 * Saves new hosts file and creates a backup of previous file.
	 * <p><b>Must be in an async call.</b></p>
	 *
	 * @param appContext application context
	 * @return {@code true} if everything was working as expected, or {@code false} otherwise
	 */
	public synchronized boolean saveHosts(Context appContext) {
		if (!RootTools.isAccessGiven()) {
			Log.w(TAG, "Can't get root access");
			return false;
		}

		// Step 1: Create temporary hosts file in /data/data/project_package/files/hosts
		if (!createTempHostsFile(appContext)) {
			Log.w(TAG, "Can't create temporary hosts file");
			return false;
		}

		String tmpFile = String.format(Locale.US, "%s/%s", appContext.getFilesDir().getAbsolutePath(), HOSTS_FILE_NAME);
		String backupFile = String.format(Locale.US, "%s.bak", tmpFile);
		try {
			// Step 2: Create backup of current hosts file (if any)
			RootTools.remount(HOSTS_FILE_PATH, MOUNT_TYPE_RW);
			runRootCommand(COMMAND_RM, backupFile);
			RootTools.copyFile(HOSTS_FILE_PATH, backupFile, false, true);

			// Step 3: Replace hosts file with generated file
			runRootCommand(COMMAND_RM, HOSTS_FILE_PATH);
			RootTools.copyFile(tmpFile, HOSTS_FILE_PATH, false, true);

			// Step 4: Give proper rights
			runRootCommand(COMMAND_CHOWN, HOSTS_FILE_PATH);
			runRootCommand(COMMAND_CHMOD_644, HOSTS_FILE_PATH);

			// Step 5: Delete local file
			appContext.deleteFile(HOSTS_FILE_NAME);
		} catch (Exception e) {
			Log.e(TAG, "", e);
			return false;
		} finally {
			RootTools.remount(HOSTS_FILE_PATH, MOUNT_TYPE_RO);
		}
		return true;
	}

	/**
	 * Creates a temporary hosts file in {@code /data/data/project_package/files/hosts}.
	 * <p><b>Must be in an async call.</b></p>
	 *
	 * @param appContext application context
	 * @return {@code true} if the temp file was created, or {@code false} otherwise
	 */
	private boolean createTempHostsFile(Context appContext) {
		OutputStreamWriter writer = null;
		try {
			FileOutputStream out = appContext.openFileOutput(HOSTS_FILE_NAME, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);

			for (Host host : getHosts(false)) {
				writer.append(host.toString()).append(LINE_SEPARATOR);
			}
			writer.flush();
		} catch (IOException e) {
			Log.e(TAG, "", e);
			return false;
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					Log.e(TAG, "Error while closing writer", e);
				}
			}
		}
		return true;
	}

	/**
	 * Executes a single argument root command.
	 * <p><b>Must be in an async call.</b></p>
	 *
	 * @param command a command, ie {@code "rm -f"}, {@code "chmod 644"}...
	 * @param uniqueArg the unique argument for the command, usually the file name
	 */
	private void runRootCommand(String command, String uniqueArg) throws InterruptedException, IOException, TimeoutException, RootDeniedException {
		CommandCapture cmd = new CommandCapture(0, String.format(Locale.US, "%s %s", command, uniqueArg));
		RootTools.getShell(true).add(cmd).waitForFinish();
	}
}
