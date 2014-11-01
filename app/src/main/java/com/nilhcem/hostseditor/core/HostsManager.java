package com.nilhcem.hostseditor.core;

import android.content.Context;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

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

import timber.log.Timber;

@Singleton
public class HostsManager {

    private static final String UTF_8 = "UTF-8";
    private static final String HOSTS_FILE_NAME = "hosts";
    private static final String HOSTS_FILE_PATH = "/system/etc/" + HOSTS_FILE_NAME;

    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    private static final String MOUNT_TYPE_RO = "ro";
    private static final String MOUNT_TYPE_RW = "rw";
    private static final String COMMAND_RM = "rm -f";
    private static final String COMMAND_CHOWN = "chown 0:0";
    private static final String COMMAND_CHMOD_644 = "chmod 644";

    // Do not access this field directly even in the same class, use getAllHosts() instead.
    private List<Host> mHosts;

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

            LineIterator it = null;
            try {
                it = FileUtils.lineIterator(new File(HOSTS_FILE_PATH), UTF_8);
                while (it.hasNext()) {
                    Host host = Host.fromString(it.nextLine());
                    if (host != null) {
                        mHosts.add(host);
                    }
                }
            } catch (IOException e) {
                Timber.e(e, "I/O error while opening hosts file");
            } finally {
                if (it != null) {
                    LineIterator.closeQuietly(it);
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
            Timber.w("Can't get root access");
            return false;
        }

        // Step 1: Create temporary hosts file in /data/data/project_package/files/hosts
        if (!createTempHostsFile(appContext)) {
            Timber.w("Can't create temporary hosts file");
            return false;
        }

        String tmpFile = String.format(Locale.US, "%s/%s", appContext.getFilesDir().getAbsolutePath(), HOSTS_FILE_NAME);
        String backupFile = String.format(Locale.US, "%s.bak", tmpFile);

        // Step 2: Get canonical path for /etc/hosts (it could be a symbolic link)
        String hostsFilePath = HOSTS_FILE_PATH;
        File hostsFile = new File(HOSTS_FILE_PATH);
        if (hostsFile.exists()) {
            try {
                if (FileUtils.isSymlink(hostsFile)) {
                    hostsFilePath = hostsFile.getCanonicalPath();
                }
            } catch (IOException e1) {
                Timber.e(e1, "");
            }
        } else {
            Timber.w("Hosts file was not found in filesystem");
        }

        try {
            // Step 3: Create backup of current hosts file (if any)
            RootTools.remount(hostsFilePath, MOUNT_TYPE_RW);
            runRootCommand(COMMAND_RM, backupFile);
            RootTools.copyFile(hostsFilePath, backupFile, false, true);

            // Step 4: Replace hosts file with generated file
            runRootCommand(COMMAND_RM, hostsFilePath);
            RootTools.copyFile(tmpFile, hostsFilePath, false, true);

            // Step 5: Give proper rights
            runRootCommand(COMMAND_CHOWN, hostsFilePath);
            runRootCommand(COMMAND_CHMOD_644, hostsFilePath);

            // Step 6: Delete local file
            appContext.deleteFile(HOSTS_FILE_NAME);
        } catch (Exception e) {
            Timber.e(e, "");
            return false;
        } finally {
            RootTools.remount(hostsFilePath, MOUNT_TYPE_RO);
        }
        return true;
    }

    /**
     * Returns a list of hosts matching the constraint parameter.
     */
    public List<Host> filterHosts(CharSequence constraint) {
        List<Host> all = getHosts(false);
        List<Host> hosts = new ArrayList<Host>();

        for (Host host : all) {
            if (host.isValid()) {
                if (host.getIp().contains(constraint)
                        || host.getHostName().contains(constraint)
                        || (host.getComment() != null && host.getComment().contains(constraint))) {
                    hosts.add(host);
                }
            }
        }
        return hosts;
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
            Timber.e(e, "");
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Timber.e(e, "Error while closing writer");
                }
            }
        }
        return true;
    }

    /**
     * Executes a single argument root command.
     * <p><b>Must be in an async call.</b></p>
     *
     * @param command   a command, ie {@code "rm -f"}, {@code "chmod 644"}...
     * @param uniqueArg the unique argument for the command, usually the file name
     */
    private void runRootCommand(String command, String uniqueArg) throws IOException, TimeoutException, RootDeniedException {
        CommandCapture cmd = new CommandCapture(0, false, String.format(Locale.US, "%s %s", command, uniqueArg));
        RootTools.getShell(true).add(cmd);
    }
}
