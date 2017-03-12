package com.android.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.IPackageManager.Stub;
import android.os.Build;
import android.os.Debug;
import android.os.DropBoxManager;
import android.os.FileObserver;
import android.os.FileUtils;
import android.os.RecoverySystem;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Downloads;
import android.text.format.Time;
import android.util.Slog;
import com.android.internal.telephony.SmsConstants;
import com.samsung.android.cocktailbar.AbsCocktailLoadablePanel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class BootReceiver extends BroadcastReceiver {
    private static final int LAST_KMSG_SIZE = 131072;
    private static final int LOG_SIZE = (SystemProperties.getInt("ro.debuggable", 0) == 1 ? 98304 : 65536);
    private static final String OLD_UPDATER_CLASS = "com.google.android.systemupdater.SystemUpdateReceiver";
    private static final String OLD_UPDATER_PACKAGE = "com.google.android.systemupdater";
    private static final String PROC_RESET_RESON = "/proc/reset_reason";
    private static final String TAG = "BootReceiver";
    private static final File TOMBSTONE_DIR = new File("/data/tombstones");
    private static FileObserver sTombstoneObserver = null;

    private static class SaveLastkmsg extends Thread {
        private static final String KERNEL_VERSION = "/proc/version";
        private static final String LAST_KMSG = "/proc/last_kmsg";
        private static final String LAST_KMSG_SAVE = "/data/log/dumpstate_lastkmsg";
        private static final int MAX_LAST_KMSG = 5;

        private SaveLastkmsg() {
        }

        private void trimLastKmsg() {
            File logFolder = new File("/data/log");
            File[] listOfAllFiles = logFolder.listFiles();
            List<File> listOfLastkmsg = new ArrayList();
            try {
                if (logFolder.exists()) {
                    for (File file : listOfAllFiles) {
                        String fname = file.getName();
                        if (file.isFile() && fname.startsWith("dumpstate_lastkmsg")) {
                            listOfLastkmsg.add(file);
                        }
                    }
                    Collections.sort(listOfLastkmsg, new Comparator<File>() {
                        public int compare(File o1, File o2) {
                            return Long.valueOf(o1.lastModified()).compareTo(Long.valueOf(o2.lastModified()));
                        }
                    });
                    Slog.i(BootReceiver.TAG, "trimLastKmsg - Num of existing listOfLastkmsg is " + listOfLastkmsg.size());
                    while (listOfLastkmsg.size() >= 5) {
                        Slog.i(BootReceiver.TAG, "trimLastKmsg - Delete file" + ((File) listOfLastkmsg.get(0)).getName());
                        ((File) listOfLastkmsg.get(0)).delete();
                        listOfLastkmsg.remove(0);
                    }
                    return;
                }
                logFolder.mkdir();
            } catch (Exception e) {
                Slog.e(BootReceiver.TAG, "trimLastKmsg error" + e);
            }
        }

        private void logLastKmsg() {
            IOException e;
            Throwable th;
            File lk_proc = new File(LAST_KMSG);
            File version_proc = new File(KERNEL_VERSION);
            byte[] buffer = new byte[2048];
            FileInputStream fin = null;
            FileInputStream ver_fin = null;
            FileOutputStream fout = null;
            GZIPOutputStream gout = null;
            Time time = new Time();
            Slog.i(BootReceiver.TAG, "logLastKmsg - Start");
            if (lk_proc.isFile()) {
                time.setToNow();
                File lk_save = new File("/data/log/dumpstate_lastkmsg_" + time.format("%Y%m%d_%H%M%S") + ".log.gz");
                try {
                    String line = "========================================================\n";
                    String text = "== dumpstate lastkmsg : " + time.format("%Y-%m-%d %H:%M:%S") + "\n";
                    Slog.i(BootReceiver.TAG, "logLastKmsg - New filename is " + lk_save.getName());
                    FileInputStream fin2 = new FileInputStream(lk_proc);
                    try {
                        FileOutputStream fout2 = new FileOutputStream(lk_save);
                        try {
                            GZIPOutputStream gout2 = new GZIPOutputStream(fout2);
                            try {
                                int len;
                                gout2.write(line.getBytes());
                                gout2.write(text.getBytes());
                                gout2.write(line.getBytes());
                                gout2.write("\n[Kernel version]: ".getBytes());
                                if (version_proc.isFile()) {
                                    FileInputStream ver_fin2 = new FileInputStream(version_proc);
                                    while (true) {
                                        try {
                                            len = ver_fin2.read(buffer);
                                            if (len <= 0) {
                                                break;
                                            }
                                            gout2.write(buffer, 0, len);
                                        } catch (IOException e2) {
                                            e = e2;
                                            gout = gout2;
                                            fout = fout2;
                                            ver_fin = ver_fin2;
                                            fin = fin2;
                                        } catch (Throwable th2) {
                                            th = th2;
                                            gout = gout2;
                                            fout = fout2;
                                            ver_fin = ver_fin2;
                                            fin = fin2;
                                        }
                                    }
                                    ver_fin = ver_fin2;
                                } else {
                                    gout2.write(SmsConstants.FORMAT_UNKNOWN.getBytes());
                                }
                                gout2.write("[Build Fingerprint]: ".getBytes());
                                gout2.write((SystemProperties.get("ro.build.fingerprint", SmsConstants.FORMAT_UNKNOWN) + "\n\n").getBytes());
                                while (true) {
                                    len = fin2.read(buffer);
                                    if (len <= 0) {
                                        break;
                                    }
                                    gout2.write(buffer, 0, len);
                                }
                                if (fin2 != null) {
                                    try {
                                        fin2.close();
                                    } catch (IOException e3) {
                                    }
                                }
                                if (ver_fin != null) {
                                    try {
                                        ver_fin.close();
                                    } catch (IOException e4) {
                                    }
                                }
                                if (gout2 != null) {
                                    try {
                                        gout2.close();
                                    } catch (IOException e5) {
                                    }
                                }
                                if (fout2 != null) {
                                    try {
                                        fout2.close();
                                    } catch (IOException e6) {
                                        gout = gout2;
                                        fout = fout2;
                                        fin = fin2;
                                    }
                                }
                                gout = gout2;
                                fout = fout2;
                                fin = fin2;
                            } catch (IOException e7) {
                                e = e7;
                                gout = gout2;
                                fout = fout2;
                                fin = fin2;
                                try {
                                    Slog.e(BootReceiver.TAG, "logLastKmsg - File copy error" + e);
                                    if (fin != null) {
                                        try {
                                            fin.close();
                                        } catch (IOException e8) {
                                        }
                                    }
                                    if (ver_fin != null) {
                                        try {
                                            ver_fin.close();
                                        } catch (IOException e9) {
                                        }
                                    }
                                    if (gout != null) {
                                        try {
                                            gout.close();
                                        } catch (IOException e10) {
                                        }
                                    }
                                    if (fout != null) {
                                        try {
                                            fout.close();
                                        } catch (IOException e11) {
                                        }
                                    }
                                    Slog.i(BootReceiver.TAG, "logLastKmsg - Save Complete");
                                } catch (Throwable th3) {
                                    th = th3;
                                    if (fin != null) {
                                        try {
                                            fin.close();
                                        } catch (IOException e12) {
                                        }
                                    }
                                    if (ver_fin != null) {
                                        try {
                                            ver_fin.close();
                                        } catch (IOException e13) {
                                        }
                                    }
                                    if (gout != null) {
                                        try {
                                            gout.close();
                                        } catch (IOException e14) {
                                        }
                                    }
                                    if (fout != null) {
                                        try {
                                            fout.close();
                                        } catch (IOException e15) {
                                        }
                                    }
                                    throw th;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                gout = gout2;
                                fout = fout2;
                                fin = fin2;
                                if (fin != null) {
                                    fin.close();
                                }
                                if (ver_fin != null) {
                                    ver_fin.close();
                                }
                                if (gout != null) {
                                    gout.close();
                                }
                                if (fout != null) {
                                    fout.close();
                                }
                                throw th;
                            }
                        } catch (IOException e16) {
                            e = e16;
                            fout = fout2;
                            fin = fin2;
                            Slog.e(BootReceiver.TAG, "logLastKmsg - File copy error" + e);
                            if (fin != null) {
                                fin.close();
                            }
                            if (ver_fin != null) {
                                ver_fin.close();
                            }
                            if (gout != null) {
                                gout.close();
                            }
                            if (fout != null) {
                                fout.close();
                            }
                            Slog.i(BootReceiver.TAG, "logLastKmsg - Save Complete");
                        } catch (Throwable th5) {
                            th = th5;
                            fout = fout2;
                            fin = fin2;
                            if (fin != null) {
                                fin.close();
                            }
                            if (ver_fin != null) {
                                ver_fin.close();
                            }
                            if (gout != null) {
                                gout.close();
                            }
                            if (fout != null) {
                                fout.close();
                            }
                            throw th;
                        }
                    } catch (IOException e17) {
                        e = e17;
                        fin = fin2;
                        Slog.e(BootReceiver.TAG, "logLastKmsg - File copy error" + e);
                        if (fin != null) {
                            fin.close();
                        }
                        if (ver_fin != null) {
                            ver_fin.close();
                        }
                        if (gout != null) {
                            gout.close();
                        }
                        if (fout != null) {
                            fout.close();
                        }
                        Slog.i(BootReceiver.TAG, "logLastKmsg - Save Complete");
                    } catch (Throwable th6) {
                        th = th6;
                        fin = fin2;
                        if (fin != null) {
                            fin.close();
                        }
                        if (ver_fin != null) {
                            ver_fin.close();
                        }
                        if (gout != null) {
                            gout.close();
                        }
                        if (fout != null) {
                            fout.close();
                        }
                        throw th;
                    }
                } catch (IOException e18) {
                    e = e18;
                    Slog.e(BootReceiver.TAG, "logLastKmsg - File copy error" + e);
                    if (fin != null) {
                        fin.close();
                    }
                    if (ver_fin != null) {
                        ver_fin.close();
                    }
                    if (gout != null) {
                        gout.close();
                    }
                    if (fout != null) {
                        fout.close();
                    }
                    Slog.i(BootReceiver.TAG, "logLastKmsg - Save Complete");
                }
                Slog.i(BootReceiver.TAG, "logLastKmsg - Save Complete");
            }
        }

        public void run() {
            trimLastKmsg();
            logLastKmsg();
        }
    }

    public void onReceive(final Context context, Intent intent) {
        try {
            if (intent.getBooleanExtra("from_quickboot", false)) {
                return;
            }
        } catch (Exception e) {
            Slog.e(TAG, "[from_quickboot]:", e);
        }
        new Thread() {
            public void run() {
                try {
                    BootReceiver.this.logBootEvents(context);
                } catch (Exception e) {
                    Slog.e(BootReceiver.TAG, "Can't log boot events", e);
                }
                boolean onlyCore = false;
                try {
                    onlyCore = Stub.asInterface(ServiceManager.getService(AbsCocktailLoadablePanel.PACKAGE_NAME)).isOnlyCoreApps();
                } catch (RemoteException e2) {
                }
                if (!onlyCore) {
                    try {
                        BootReceiver.this.removeOldUpdatePackages(context);
                    } catch (Exception e3) {
                        Slog.e(BootReceiver.TAG, "Can't remove old update packages", e3);
                    }
                }
            }
        }.start();
    }

    private void logResetReson() {
        File file = new File(PROC_RESET_RESON);
        if (file.isFile()) {
            String resetString = null;
            try {
                resetString = FileUtils.readTextFile(file, 1024, "\n");
            } catch (IOException e) {
                Slog.e("Reset_Reason", "readTextFile error" + e);
            }
            Slog.d("Reset_Reason", "resetString = " + resetString);
            if (resetString != null && resetString.length() >= 2) {
                resetString = resetString.substring(0, 2);
                Debug.dumpResetReason(resetString, "RR");
            }
            if ("KP".equals(resetString) || "PP".equals(resetString) || "DP".equals(resetString)) {
                new SaveLastkmsg().start();
            }
        }
    }

    private void removeOldUpdatePackages(Context context) {
        Downloads.removeAllDownloadsByPackage(context, OLD_UPDATER_PACKAGE, OLD_UPDATER_CLASS);
    }

    private void logBootEvents(Context ctx) throws IOException {
        DropBoxManager db = (DropBoxManager) ctx.getSystemService("dropbox");
        SharedPreferences prefs = ctx.getSharedPreferences("log_files", 0);
        String headers = "Build: " + Build.FINGERPRINT + "\n" + "Hardware: " + Build.BOARD + "\n" + "Revision: " + SystemProperties.get("ro.revision", "") + "\n" + "Bootloader: " + Build.BOOTLOADER + "\n" + "Radio: " + Build.RADIO + "\n" + "Kernel: " + FileUtils.readTextFile(new File("/proc/version"), 1024, "...\n") + "\n";
        String bootReason = SystemProperties.get("ro.boot.bootreason", null);
        String recovery = RecoverySystem.handleAftermath();
        if (!(recovery == null || db == null)) {
            db.addText("SYSTEM_RECOVERY_LOG", headers + recovery);
        }
        String lastKmsgFooter = "";
        if (bootReason != null) {
            lastKmsgFooter = "\n" + "Boot info:\n" + "Last boot reason: " + bootReason + "\n";
        }
        if (SystemProperties.getLong("ro.runtime.firstboot", 0) == 0) {
            if (!("encrypted".equals(SystemProperties.get("ro.crypto.state")) && "trigger_restart_min_framework".equals(SystemProperties.get("vold.decrypt")))) {
                SystemProperties.set("ro.runtime.firstboot", Long.toString(System.currentTimeMillis()));
            }
            if (db != null) {
                db.addText("SYSTEM_BOOT", headers);
            }
            logResetReson();
            addFileWithFootersToDropBox(db, prefs, headers, lastKmsgFooter, "/proc/last_kmsg", -LOG_SIZE, "SYSTEM_LAST_KMSG");
            addFileWithFootersToDropBox(db, prefs, headers, lastKmsgFooter, "/sys/fs/pstore/console-ramoops", -LOG_SIZE, "SYSTEM_LAST_KMSG");
            addFileToDropBox(db, prefs, headers, "/cache/recovery/log", -LOG_SIZE, "SYSTEM_RECOVERY_LOG");
            addFileToDropBox(db, prefs, headers, "/cache/recovery/last_kmsg", -LOG_SIZE, "SYSTEM_RECOVERY_KMSG");
            addAuditErrorsToDropBox(db, prefs, headers, -LOG_SIZE, "SYSTEM_AUDIT");
            addFsckErrorsToDropBox(db, prefs, headers, -LOG_SIZE, "SYSTEM_FSCK");
        } else if (db != null) {
            db.addText("SYSTEM_RESTART", headers);
        }
        File[] tombstoneFiles = TOMBSTONE_DIR.listFiles();
        int i = 0;
        while (tombstoneFiles != null && i < tombstoneFiles.length) {
            if (tombstoneFiles[i].isFile()) {
                addFileToDropBox(db, prefs, headers, tombstoneFiles[i].getPath(), LOG_SIZE, "SYSTEM_TOMBSTONE");
            }
            i++;
        }
        final DropBoxManager dropBoxManager = db;
        final SharedPreferences sharedPreferences = prefs;
        final String str = headers;
        sTombstoneObserver = new FileObserver(TOMBSTONE_DIR.getPath(), 8) {
            public void onEvent(int event, String path) {
                try {
                    File file = new File(BootReceiver.TOMBSTONE_DIR, path);
                    if (file.isFile()) {
                        BootReceiver.addFileToDropBox(dropBoxManager, sharedPreferences, str, file.getPath(), BootReceiver.LOG_SIZE, "SYSTEM_TOMBSTONE");
                    }
                } catch (IOException e) {
                    Slog.e(BootReceiver.TAG, "Can't log tombstone", e);
                }
            }
        };
        sTombstoneObserver.startWatching();
    }

    private static void addFileToDropBox(DropBoxManager db, SharedPreferences prefs, String headers, String filename, int maxSize, String tag) throws IOException {
        addFileWithFootersToDropBox(db, prefs, headers, "", filename, maxSize, tag);
    }

    private static void addFileWithFootersToDropBox(DropBoxManager db, SharedPreferences prefs, String headers, String footers, String filename, int maxSize, String tag) throws IOException {
        if (db != null && db.isTagEnabled(tag)) {
            File file = new File(filename);
            if (!file.isDirectory()) {
                long fileTime = file.lastModified();
                if (fileTime > 0) {
                    if (prefs != null) {
                        if (prefs.getLong(filename, 0) != fileTime) {
                            prefs.edit().putLong(filename, fileTime).apply();
                        } else {
                            return;
                        }
                    }
                    Slog.i(TAG, "Copying " + filename + " to DropBox (" + tag + ")");
                    db.addText(tag, headers + FileUtils.readTextFile(file, maxSize, "[[TRUNCATED]]\n") + footers);
                }
            }
        }
    }

    private static void addAuditErrorsToDropBox(DropBoxManager db, SharedPreferences prefs, String headers, int maxSize, String tag) throws IOException {
        if (db != null && db.isTagEnabled(tag)) {
            Slog.i(TAG, "Copying audit failures to DropBox");
            File file = new File("/proc/last_kmsg");
            long fileTime = file.lastModified();
            if (fileTime <= 0) {
                file = new File("/sys/fs/pstore/console-ramoops");
                fileTime = file.lastModified();
            }
            if (fileTime > 0) {
                if (prefs != null) {
                    if (prefs.getLong(tag, 0) != fileTime) {
                        prefs.edit().putLong(tag, fileTime).apply();
                    } else {
                        return;
                    }
                }
                String log = FileUtils.readTextFile(file, maxSize, "[[TRUNCATED]]\n");
                StringBuilder sb = new StringBuilder();
                for (String line : log.split("\n")) {
                    if (line.contains("audit")) {
                        sb.append(line + "\n");
                    }
                }
                Slog.i(TAG, "Copied " + sb.toString().length() + " worth of audits to DropBox");
                db.addText(tag, headers + sb.toString());
            }
        }
    }

    private static void addFsckErrorsToDropBox(DropBoxManager db, SharedPreferences prefs, String headers, int maxSize, String tag) throws IOException {
        boolean upload_needed = false;
        if (db != null && db.isTagEnabled(tag)) {
            Slog.i(TAG, "Checking for fsck errors");
            File file = new File("/dev/fscklogs/log");
            if (file.lastModified() > 0) {
                String log = FileUtils.readTextFile(file, maxSize, "[[TRUNCATED]]\n");
                StringBuilder sb = new StringBuilder();
                for (String line : log.split("\n")) {
                    if (line.contains("FILE SYSTEM WAS MODIFIED")) {
                        upload_needed = true;
                        break;
                    }
                }
                if (upload_needed) {
                    addFileToDropBox(db, prefs, headers, "/dev/fscklogs/log", maxSize, tag);
                }
                file.delete();
            }
        }
    }
}
