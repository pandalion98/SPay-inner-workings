package com.android.server.backup;

import android.app.IWallpaperManager;
import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FullBackup;
import android.app.backup.FullBackupDataOutput;
import android.app.backup.WallpaperBackupHelper;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import java.io.File;
import java.io.IOException;

public class SystemBackupAgent extends BackupAgentHelper {
    private static final String NOTIFICATION_HELPER = "notifications";
    private static final String PREFERRED_HELPER = "preferred_activities";
    private static final String SYNC_SETTINGS_HELPER = "account_sync_settings";
    private static final String TAG = "SystemBackupAgent";
    private static final String WALLPAPER_HELPER = "wallpaper";
    private static final String WALLPAPER_IMAGE = WallpaperBackupHelper.WALLPAPER_IMAGE;
    private static final String WALLPAPER_IMAGE_DIR = Environment.getUserSystemDirectory(0).getAbsolutePath();
    private static final String WALLPAPER_IMAGE_FILENAME = "wallpaper";
    private static final String WALLPAPER_IMAGE_KEY = "/data/data/com.android.settings/files/wallpaper";
    private static final String WALLPAPER_INFO = WallpaperBackupHelper.WALLPAPER_INFO;
    private static final String WALLPAPER_INFO_DIR = Environment.getUserSystemDirectory(0).getAbsolutePath();
    private static final String WALLPAPER_INFO_FILENAME = "wallpaper_info.xml";
    private static final String WALLPAPER_INFO_KEY = "/data/system/wallpaper_info.xml";

    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        RemoteException re;
        IWallpaperManager wallpaper = (IWallpaperManager) ServiceManager.getService("wallpaper");
        String[] files = new String[]{WALLPAPER_IMAGE, WALLPAPER_INFO};
        String[] keys = new String[]{WALLPAPER_IMAGE_KEY, WALLPAPER_INFO_KEY};
        if (wallpaper != null) {
            try {
                String wallpaperName = wallpaper.getName();
                if (wallpaperName != null && wallpaperName.length() > 0) {
                    String[] files2 = new String[]{WALLPAPER_INFO};
                    try {
                        keys = new String[]{WALLPAPER_INFO_KEY};
                        files = files2;
                    } catch (RemoteException e) {
                        re = e;
                        files = files2;
                        Slog.e(TAG, "Couldn't get wallpaper name\n" + re);
                        addHelper("wallpaper", new WallpaperBackupHelper(this, files, keys));
                        addHelper(SYNC_SETTINGS_HELPER, new AccountSyncSettingsBackupHelper(this));
                        addHelper(PREFERRED_HELPER, new PreferredActivityBackupHelper());
                        addHelper(NOTIFICATION_HELPER, new NotificationBackupHelper(this));
                        super.onBackup(oldState, data, newState);
                    }
                }
            } catch (RemoteException e2) {
                re = e2;
                Slog.e(TAG, "Couldn't get wallpaper name\n" + re);
                addHelper("wallpaper", new WallpaperBackupHelper(this, files, keys));
                addHelper(SYNC_SETTINGS_HELPER, new AccountSyncSettingsBackupHelper(this));
                addHelper(PREFERRED_HELPER, new PreferredActivityBackupHelper());
                addHelper(NOTIFICATION_HELPER, new NotificationBackupHelper(this));
                super.onBackup(oldState, data, newState);
            }
        }
        addHelper("wallpaper", new WallpaperBackupHelper(this, files, keys));
        addHelper(SYNC_SETTINGS_HELPER, new AccountSyncSettingsBackupHelper(this));
        addHelper(PREFERRED_HELPER, new PreferredActivityBackupHelper());
        addHelper(NOTIFICATION_HELPER, new NotificationBackupHelper(this));
        super.onBackup(oldState, data, newState);
    }

    public void onFullBackup(FullBackupDataOutput data) throws IOException {
        fullWallpaperBackup(data);
    }

    private void fullWallpaperBackup(FullBackupDataOutput output) {
        FullBackup.backupToTar(getPackageName(), "r", null, WALLPAPER_INFO_DIR, WALLPAPER_INFO, output);
        FullBackup.backupToTar(getPackageName(), "r", null, WALLPAPER_IMAGE_DIR, WALLPAPER_IMAGE, output);
    }

    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        addHelper("wallpaper", new WallpaperBackupHelper(this, new String[]{WALLPAPER_IMAGE, WALLPAPER_INFO}, new String[]{WALLPAPER_IMAGE_KEY, WALLPAPER_INFO_KEY}));
        addHelper("system_files", new WallpaperBackupHelper(this, new String[]{WALLPAPER_IMAGE}, new String[]{WALLPAPER_IMAGE_KEY}));
        addHelper(SYNC_SETTINGS_HELPER, new AccountSyncSettingsBackupHelper(this));
        addHelper(PREFERRED_HELPER, new PreferredActivityBackupHelper());
        addHelper(NOTIFICATION_HELPER, new NotificationBackupHelper(this));
        try {
            super.onRestore(data, appVersionCode, newState);
            IWallpaperManager wallpaper = (IWallpaperManager) ServiceManager.getService("wallpaper");
            if (wallpaper != null) {
                try {
                    wallpaper.settingsRestored();
                } catch (RemoteException re) {
                    Slog.e(TAG, "Couldn't restore settings\n" + re);
                }
            }
        } catch (IOException ex) {
            Slog.d(TAG, "restore failed", ex);
            new File(WALLPAPER_IMAGE).delete();
            new File(WALLPAPER_INFO).delete();
        }
    }

    public void onRestoreFile(ParcelFileDescriptor data, long size, int type, String domain, String path, long mode, long mtime) throws IOException {
        Slog.i(TAG, "Restoring file domain=" + domain + " path=" + path);
        boolean restoredWallpaper = false;
        File outFile = null;
        if (domain.equals("r")) {
            if (path.equals(WALLPAPER_INFO_FILENAME)) {
                outFile = new File(WALLPAPER_INFO);
                restoredWallpaper = true;
            } else {
                if (path.equals("wallpaper")) {
                    outFile = new File(WALLPAPER_IMAGE);
                    restoredWallpaper = true;
                }
            }
        }
        if (outFile == null) {
            try {
                Slog.w(TAG, "Skipping unrecognized system file: [ " + domain + " : " + path + " ]");
            } catch (IOException e) {
                if (restoredWallpaper) {
                    new File(WALLPAPER_IMAGE).delete();
                    new File(WALLPAPER_INFO).delete();
                    return;
                }
                return;
            }
        }
        FullBackup.restoreFile(data, size, type, mode, mtime, outFile);
        if (restoredWallpaper) {
            IWallpaperManager wallpaper = (IWallpaperManager) ServiceManager.getService("wallpaper");
            if (wallpaper != null) {
                try {
                    wallpaper.settingsRestored();
                } catch (RemoteException re) {
                    Slog.e(TAG, "Couldn't restore settings\n" + re);
                }
            }
        }
    }
}
