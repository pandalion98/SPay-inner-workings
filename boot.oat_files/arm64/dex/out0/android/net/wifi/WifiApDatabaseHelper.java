package android.net.wifi;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioParameter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.RouteInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;
import android.view.DisplayAdjustments;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WifiApDatabaseHelper extends SQLiteOpenHelper {
    private static final String ACCEPT = "accept";
    private static final String DES = " -d ";
    private static final String ID = "id";
    private static final String INPUT = " -i ";
    private static final String IP = "ip";
    private static final String IPTABLES_A = "iptables -A FORWARD ";
    private static final String IPTABLES_D = "iptables -D FORWARD ";
    private static final String IPTABLES_GREP = "iptables -nvx -L FORWARD | grep ";
    private static final String IPTABLES_REJECT = " -j REJECT";
    private static final String LIMIT_DATA = "limit_data";
    private static final String MAC = "mac";
    private static final String NAME = "wifiapdata.db";
    private static final String OUTPUT = " -o ";
    private static final String PATH = "/data/misc/wifi_hostapd";
    private static final String REJECT = "reject";
    private static final String REMAIN = "remain";
    private static final String SRC = " -s ";
    private static final String TABLE_NAME = "wifiapdata";
    private static final String TAG = "WifiApDatabaseHelper";
    private static final String TEMP_USAGE = "temp_usage";
    private static final String USAGE = "usage";
    private static final int VERSION = 1;
    private static final String WIFIAPDATA_TABLE_CREATE = "CREATE TABLE wifiapdata (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, mac STRING NOT NULL , ip STRING NOT NULL , usage STRING NOT NULL , limit_data STRING NOT NULL , temp_usage STRING NOT NULL , remain STRING NOT NULL , accept STRING , reject STRING)";
    private static final int WIFI_AP_DATA_MONITOR_MS = 1000;
    private Context mContext;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(WifiApDatabaseHelper.TAG, "action : " + action);
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                int networkType = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, 0);
                if (networkType == 0) {
                    String iface = WifiApDatabaseHelper.this.getMobileIfaceName(((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getLinkProperties(networkType));
                    if (iface == null) {
                        return;
                    }
                    if (WifiApDatabaseHelper.this.mobileInterface == null) {
                        WifiApDatabaseHelper.this.mobileInterface = iface;
                        WifiApDatabaseHelper.this.checkWifiApList();
                        return;
                    } else if (!iface.equals(WifiApDatabaseHelper.this.mobileInterface)) {
                        WifiApDatabaseHelper.this.clearWifiApList(false);
                        WifiApDatabaseHelper.this.checkWifiApList();
                        return;
                    } else {
                        return;
                    }
                }
                WifiApDatabaseHelper.this.mWifiApDataHandler.pause();
            } else if (action.equals(WifiManager.WIFI_AP_STATE_CHANGED_ACTION)) {
                if (intent.getIntExtra("wifi_state", 14) == 11) {
                    Log.d(WifiApDatabaseHelper.TAG, "WIFI AP disabled");
                    WifiApDatabaseHelper.this.clearWifiApList(true);
                }
            } else if (action.equals(WifiManager.WIFI_AP_STA_STATUS_CHANGED_ACTION)) {
                String type = intent.getStringExtra("EVENT");
                String macList = ((WifiManager) context.getSystemService("wifi")).getWifiApStaList();
                Log.d(WifiApDatabaseHelper.TAG, "type : " + type);
                if ("sta_leave".equals(type)) {
                    String mac = intent.getStringExtra("MAC").toUpperCase();
                    Log.d(WifiApDatabaseHelper.TAG, "leave : " + mac);
                    WifiApDatabaseHelper.this.removeStaFromApList(mac);
                }
            }
        }
    };
    private final WifiApDataHandler mWifiApDataHandler;
    private List<String> mWifiApList = new ArrayList();
    private String mobileInterface = null;

    private static class WifiApDataBaseContext extends Context {
        Context mContext;
        private String mDbPath;

        WifiApDataBaseContext(Context base, String path) {
            this.mContext = base;
            this.mDbPath = path;
        }

        public AssetManager getAssets() {
            return this.mContext.getAssets();
        }

        public Resources getResources() {
            return this.mContext.getResources();
        }

        public PackageManager getPackageManager() {
            return this.mContext.getPackageManager();
        }

        public ContentResolver getContentResolver() {
            return this.mContext.getContentResolver();
        }

        public Looper getMainLooper() {
            return this.mContext.getMainLooper();
        }

        public Context getApplicationContext() {
            return this.mContext.getApplicationContext();
        }

        public void setTheme(int resid) {
            this.mContext.setTheme(resid);
        }

        public int getThemeResId() {
            return this.mContext.getThemeResId();
        }

        public Theme getTheme() {
            return this.mContext.getTheme();
        }

        public ClassLoader getClassLoader() {
            return this.mContext.getClassLoader();
        }

        public String getPackageName() {
            return this.mContext.getPackageName();
        }

        public String getBasePackageName() {
            return this.mContext.getBasePackageName();
        }

        public String getOpPackageName() {
            return this.mContext.getOpPackageName();
        }

        public ApplicationInfo getApplicationInfo() {
            return this.mContext.getApplicationInfo();
        }

        public String getPackageResourcePath() {
            return this.mContext.getPackageResourcePath();
        }

        public String getPackageCodePath() {
            return this.mContext.getPackageCodePath();
        }

        public File getSharedPrefsFile(String name) {
            return this.mContext.getSharedPrefsFile(name);
        }

        public SharedPreferences getSharedPreferences(String name, int mode) {
            return this.mContext.getSharedPreferences(name, mode);
        }

        public FileInputStream openFileInput(String name) throws FileNotFoundException {
            return this.mContext.openFileInput(name);
        }

        public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
            return this.mContext.openFileOutput(name, mode);
        }

        public boolean deleteFile(String name) {
            return this.mContext.deleteFile(name);
        }

        public File getFilesDir() {
            return this.mContext.getFilesDir();
        }

        public File getNoBackupFilesDir() {
            return this.mContext.getNoBackupFilesDir();
        }

        public File getExternalFilesDir(String type) {
            return this.mContext.getExternalFilesDir(type);
        }

        public File[] getExternalFilesDirs(String type) {
            return this.mContext.getExternalFilesDirs(type);
        }

        public File getObbDir() {
            return this.mContext.getObbDir();
        }

        public File[] getObbDirs() {
            return this.mContext.getObbDirs();
        }

        public File getCacheDir() {
            return this.mContext.getCacheDir();
        }

        public File getCodeCacheDir() {
            return this.mContext.getCodeCacheDir();
        }

        public File getExternalCacheDir() {
            return this.mContext.getExternalCacheDir();
        }

        public File[] getExternalCacheDirs() {
            return this.mContext.getExternalCacheDirs();
        }

        public File[] getExternalMediaDirs() {
            return this.mContext.getExternalMediaDirs();
        }

        public File getFileStreamPath(String name) {
            return this.mContext.getFileStreamPath(name);
        }

        public String[] fileList() {
            return this.mContext.fileList();
        }

        public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory) {
            return this.mContext.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(), mode, factory);
        }

        public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory, DatabaseErrorHandler errorHandler) {
            return this.mContext.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(), mode, factory, errorHandler);
        }

        public boolean deleteDatabase(String name) {
            return this.mContext.deleteDatabase(name);
        }

        public File getDatabasePath(String name) {
            File file = new File(this.mDbPath + File.separator + name);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            return file;
        }

        public String[] databaseList() {
            return this.mContext.databaseList();
        }

        @Deprecated
        public Drawable getWallpaper() {
            return this.mContext.getWallpaper();
        }

        @Deprecated
        public Drawable peekWallpaper() {
            return this.mContext.peekWallpaper();
        }

        @Deprecated
        public int getWallpaperDesiredMinimumWidth() {
            return this.mContext.getWallpaperDesiredMinimumWidth();
        }

        @Deprecated
        public int getWallpaperDesiredMinimumHeight() {
            return this.mContext.getWallpaperDesiredMinimumHeight();
        }

        @Deprecated
        public void setWallpaper(Bitmap bitmap) throws IOException {
            this.mContext.setWallpaper(bitmap);
        }

        @Deprecated
        public void setWallpaper(InputStream data) throws IOException {
            this.mContext.setWallpaper(data);
        }

        @Deprecated
        public void clearWallpaper() throws IOException {
            this.mContext.clearWallpaper();
        }

        public void startActivity(Intent intent) {
            this.mContext.startActivity(intent);
        }

        public void startActivityAsUser(Intent intent, UserHandle user) {
            this.mContext.startActivityAsUser(intent, user);
        }

        public void startActivity(Intent intent, Bundle options) {
            this.mContext.startActivity(intent, options);
        }

        public void startActivityAsUser(Intent intent, Bundle options, UserHandle user) {
            this.mContext.startActivityAsUser(intent, options, user);
        }

        public void startActivities(Intent[] intents) {
            this.mContext.startActivities(intents);
        }

        public void startActivitiesAsUser(Intent[] intents, Bundle options, UserHandle userHandle) {
            this.mContext.startActivitiesAsUser(intents, options, userHandle);
        }

        public void startActivities(Intent[] intents, Bundle options) {
            this.mContext.startActivities(intents, options);
        }

        public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws SendIntentException {
            this.mContext.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags);
        }

        public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
            this.mContext.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        }

        public void sendBroadcast(Intent intent) {
            this.mContext.sendBroadcast(intent);
        }

        public void sendBroadcast(Intent intent, String receiverPermission) {
            this.mContext.sendBroadcast(intent, receiverPermission);
        }

        public void sendBroadcastMultiplePermissions(Intent intent, String[] receiverPermissions) {
            this.mContext.sendBroadcastMultiplePermissions(intent, receiverPermissions);
        }

        public void sendBroadcastMultiplePermissionsAsUser(Intent intent, String[] receiverPermissions, UserHandle user) {
            this.mContext.sendBroadcastMultiplePermissionsAsUser(intent, receiverPermissions, user);
        }

        public void sendBroadcast(Intent intent, String receiverPermission, Bundle options) {
            this.mContext.sendBroadcast(intent, receiverPermission, options);
        }

        public void sendBroadcast(Intent intent, String receiverPermission, int appOp) {
            this.mContext.sendBroadcast(intent, receiverPermission, appOp);
        }

        public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
            this.mContext.sendOrderedBroadcast(intent, receiverPermission);
        }

        public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
            this.mContext.sendOrderedBroadcast(intent, receiverPermission, resultReceiver, scheduler, initialCode, initialData, initialExtras);
        }

        public void sendOrderedBroadcast(Intent intent, String receiverPermission, Bundle options, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
            this.mContext.sendOrderedBroadcast(intent, receiverPermission, options, resultReceiver, scheduler, initialCode, initialData, initialExtras);
        }

        public void sendOrderedBroadcast(Intent intent, String receiverPermission, int appOp, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
            this.mContext.sendOrderedBroadcast(intent, receiverPermission, appOp, resultReceiver, scheduler, initialCode, initialData, initialExtras);
        }

        public void sendBroadcastAsUser(Intent intent, UserHandle user) {
            this.mContext.sendBroadcastAsUser(intent, user);
        }

        public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission) {
            this.mContext.sendBroadcastAsUser(intent, user, receiverPermission);
        }

        public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, int appOp) {
            this.mContext.sendBroadcastAsUser(intent, user, receiverPermission, appOp);
        }

        public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
            this.mContext.sendOrderedBroadcastAsUser(intent, user, receiverPermission, resultReceiver, scheduler, initialCode, initialData, initialExtras);
        }

        public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, int appOp, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
            this.mContext.sendOrderedBroadcastAsUser(intent, user, receiverPermission, appOp, resultReceiver, scheduler, initialCode, initialData, initialExtras);
        }

        public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, int appOp, Bundle options, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
            this.mContext.sendOrderedBroadcastAsUser(intent, user, receiverPermission, appOp, options, resultReceiver, scheduler, initialCode, initialData, initialExtras);
        }

        public void sendStickyBroadcast(Intent intent) {
            this.mContext.sendStickyBroadcast(intent);
        }

        public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
            this.mContext.sendStickyOrderedBroadcast(intent, resultReceiver, scheduler, initialCode, initialData, initialExtras);
        }

        public void removeStickyBroadcast(Intent intent) {
            this.mContext.removeStickyBroadcast(intent);
        }

        public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {
            this.mContext.sendStickyBroadcastAsUser(intent, user);
        }

        public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
            this.mContext.sendStickyOrderedBroadcastAsUser(intent, user, resultReceiver, scheduler, initialCode, initialData, initialExtras);
        }

        public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {
            this.mContext.removeStickyBroadcastAsUser(intent, user);
        }

        public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
            return this.mContext.registerReceiver(receiver, filter);
        }

        public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
            return this.mContext.registerReceiver(receiver, filter, broadcastPermission, scheduler);
        }

        public Intent registerReceiverAsUser(BroadcastReceiver receiver, UserHandle user, IntentFilter filter, String broadcastPermission, Handler scheduler) {
            return this.mContext.registerReceiverAsUser(receiver, user, filter, broadcastPermission, scheduler);
        }

        public void unregisterReceiver(BroadcastReceiver receiver) {
            this.mContext.unregisterReceiver(receiver);
        }

        public ComponentName startService(Intent service) {
            return this.mContext.startService(service);
        }

        public boolean stopService(Intent service) {
            return this.mContext.stopService(service);
        }

        public ComponentName startServiceAsUser(Intent service, UserHandle user) {
            return this.mContext.startServiceAsUser(service, user);
        }

        public boolean stopServiceAsUser(Intent service, UserHandle user) {
            return this.mContext.stopServiceAsUser(service, user);
        }

        public boolean bindService(Intent service, ServiceConnection conn, int flags) {
            return this.mContext.bindService(service, conn, flags);
        }

        public boolean bindServiceAsUser(Intent service, ServiceConnection conn, int flags, UserHandle user) {
            return this.mContext.bindServiceAsUser(service, conn, flags, user);
        }

        public void unbindService(ServiceConnection conn) {
            this.mContext.unbindService(conn);
        }

        public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
            return this.mContext.startInstrumentation(className, profileFile, arguments);
        }

        public Object getSystemService(String name) {
            return this.mContext.getSystemService(name);
        }

        public String getSystemServiceName(Class<?> serviceClass) {
            return this.mContext.getSystemServiceName(serviceClass);
        }

        public int checkPermission(String permission, int pid, int uid) {
            return this.mContext.checkPermission(permission, pid, uid);
        }

        public int checkPermission(String permission, int pid, int uid, IBinder callerToken) {
            return this.mContext.checkPermission(permission, pid, uid);
        }

        public int checkCallingPermission(String permission) {
            return this.mContext.checkCallingPermission(permission);
        }

        public int checkCallingOrSelfPermission(String permission) {
            return this.mContext.checkCallingOrSelfPermission(permission);
        }

        public int checkSelfPermission(String permission) {
            return this.mContext.checkSelfPermission(permission);
        }

        public void enforcePermission(String permission, int pid, int uid, String message) {
            this.mContext.enforcePermission(permission, pid, uid, message);
        }

        public void enforceCallingPermission(String permission, String message) {
            this.mContext.enforceCallingPermission(permission, message);
        }

        public void enforceCallingOrSelfPermission(String permission, String message) {
            this.mContext.enforceCallingOrSelfPermission(permission, message);
        }

        public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
            this.mContext.grantUriPermission(toPackage, uri, modeFlags);
        }

        public void revokeUriPermission(Uri uri, int modeFlags) {
            this.mContext.revokeUriPermission(uri, modeFlags);
        }

        public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
            return this.mContext.checkUriPermission(uri, pid, uid, modeFlags);
        }

        public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags, IBinder callerToken) {
            return this.mContext.checkUriPermission(uri, pid, uid, modeFlags);
        }

        public int checkCallingUriPermission(Uri uri, int modeFlags) {
            return this.mContext.checkCallingUriPermission(uri, modeFlags);
        }

        public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
            return this.mContext.checkCallingOrSelfUriPermission(uri, modeFlags);
        }

        public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {
            return this.mContext.checkUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags);
        }

        public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {
            this.mContext.enforceUriPermission(uri, pid, uid, modeFlags, message);
        }

        public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {
            this.mContext.enforceCallingUriPermission(uri, modeFlags, message);
        }

        public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {
            this.mContext.enforceCallingOrSelfUriPermission(uri, modeFlags, message);
        }

        public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) {
            this.mContext.enforceUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags, message);
        }

        public Context createApplicationContext(ApplicationInfo application, int flags) throws NameNotFoundException {
            return this.mContext.createApplicationContext(application, flags);
        }

        public Context createPackageContext(String packageName, int flags) throws NameNotFoundException {
            return this.mContext.createPackageContext(packageName, flags);
        }

        public Context createPackageContextAsUser(String packageName, int flags, UserHandle user) throws NameNotFoundException {
            return this.mContext.createPackageContextAsUser(packageName, flags, user);
        }

        public Context createPackageContext(String packageName, ClassLoader baseLoader, int flags) throws NameNotFoundException {
            return this.mContext.createPackageContext(packageName, baseLoader, flags);
        }

        public Context createPackageContextAsUser(String packageName, ClassLoader baseLoader, int flags, UserHandle user) throws NameNotFoundException {
            return this.mContext.createPackageContextAsUser(packageName, baseLoader, flags, user);
        }

        public Context createConfigurationContext(Configuration overrideConfiguration) {
            return this.mContext.createConfigurationContext(overrideConfiguration);
        }

        public Context createDisplayContext(Display display) {
            return this.mContext.createDisplayContext(display);
        }

        public int getDisplayId() {
            return this.mContext.getDisplayId();
        }

        public void setDisplay(Display display) {
            this.mContext.setDisplay(display);
        }

        public boolean getShrinkRequested() {
            return this.mContext.getShrinkRequested();
        }

        public boolean isRestricted() {
            return this.mContext.isRestricted();
        }

        public DisplayAdjustments getDisplayAdjustments(int displayId) {
            return this.mContext.getDisplayAdjustments(displayId);
        }

        public File getDir(String name, int mode) {
            return this.mContext.getDir(name, mode);
        }

        public int getUserId() {
            return this.mContext.getUserId();
        }

        public final Context getOuterContext() {
            return this.mContext.getOuterContext();
        }
    }

    private class WifiApDataHandler extends Handler {
        private boolean isRunning = false;

        WifiApDataHandler() {
        }

        void resume() {
            Log.d(WifiApDatabaseHelper.TAG, "WifiApDataHandler resume, isRunning : " + this.isRunning);
            this.isRunning = true;
            if (!hasMessages(0)) {
                sendEmptyMessage(0);
            }
        }

        void pause() {
            Log.d(WifiApDatabaseHelper.TAG, "WifiApDataHandler pause, isRunning : " + this.isRunning);
            this.isRunning = false;
            removeMessages(0);
        }

        boolean getRunning() {
            return this.isRunning;
        }

        public void handleMessage(Message message) {
            Log.d(WifiApDatabaseHelper.TAG, "WifiApDataHandler handleMessage size : " + WifiApDatabaseHelper.this.mWifiApList.size());
            boolean flag = false;
            if (WifiApDatabaseHelper.this.mWifiApList.size() > 0) {
                for (String mac : WifiApDatabaseHelper.this.mWifiApList) {
                    if (!WifiApDatabaseHelper.this.skipGetUsageData(mac)) {
                        flag = true;
                        String usage = WifiApDatabaseHelper.this.getStaUsageData(mac);
                        if (usage != null) {
                            WifiApDatabaseHelper.this.modifyUsageData(mac, usage);
                        }
                    }
                }
                if (flag) {
                    sendEmptyMessageDelayed(0, 1000);
                    return;
                }
                return;
            }
            this.isRunning = false;
        }
    }

    public static class WifiApDataInfo {
        private String ZERO = "0";
        private String mAccept;
        private String mIp;
        private String mLimit;
        private String mMac;
        private String mReject;
        private String mRemain;
        private String mTemp_usage;
        private String mUsage;

        public WifiApDataInfo(String mac, String ip, String limit) {
            this.mMac = mac;
            this.mIp = ip;
            this.mUsage = this.ZERO;
            this.mLimit = limit;
            this.mTemp_usage = this.ZERO;
            this.mRemain = limit;
            this.mAccept = AudioParameter.AUDIO_PARAMETER_VALUE_false;
            this.mReject = AudioParameter.AUDIO_PARAMETER_VALUE_false;
        }

        public WifiApDataInfo(String mac, String ip, String usage, String limit, String temp_usage, String remain, String accept, String reject) {
            this.mMac = mac;
            this.mIp = ip;
            this.mUsage = usage;
            this.mLimit = limit;
            this.mTemp_usage = temp_usage;
            this.mRemain = remain;
            this.mAccept = accept;
            this.mReject = reject;
        }

        public String getMac() {
            return this.mMac;
        }

        public String getIp() {
            return this.mIp;
        }

        public String getUsage() {
            return this.mUsage;
        }

        public String getLimit() {
            return this.mLimit;
        }

        public String getTempUsage() {
            return this.mTemp_usage;
        }

        public String getRemain() {
            return this.mRemain;
        }

        public boolean getAccept() {
            if (AudioParameter.AUDIO_PARAMETER_VALUE_true.equals(this.mAccept)) {
                return true;
            }
            return false;
        }

        public boolean getReject() {
            if (AudioParameter.AUDIO_PARAMETER_VALUE_true.equals(this.mReject)) {
                return true;
            }
            return false;
        }
    }

    public void setDatabaseDefaultValue() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x008b in list [B:15:0x0088]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r12 = this;
        r10 = new android.content.ContentValues;
        r10.<init>();
        r1 = "usage";
        r2 = "0";
        r10.put(r1, r2);
        r0 = r12.getReadableDatabase();
        r9 = 0;
        r1 = "wifiapdata";	 Catch:{ all -> 0x007f }
        r2 = 9;	 Catch:{ all -> 0x007f }
        r2 = new java.lang.String[r2];	 Catch:{ all -> 0x007f }
        r3 = 0;	 Catch:{ all -> 0x007f }
        r4 = "id";	 Catch:{ all -> 0x007f }
        r2[r3] = r4;	 Catch:{ all -> 0x007f }
        r3 = 1;	 Catch:{ all -> 0x007f }
        r4 = "mac";	 Catch:{ all -> 0x007f }
        r2[r3] = r4;	 Catch:{ all -> 0x007f }
        r3 = 2;	 Catch:{ all -> 0x007f }
        r4 = "ip";	 Catch:{ all -> 0x007f }
        r2[r3] = r4;	 Catch:{ all -> 0x007f }
        r3 = 3;	 Catch:{ all -> 0x007f }
        r4 = "usage";	 Catch:{ all -> 0x007f }
        r2[r3] = r4;	 Catch:{ all -> 0x007f }
        r3 = 4;	 Catch:{ all -> 0x007f }
        r4 = "limit_data";	 Catch:{ all -> 0x007f }
        r2[r3] = r4;	 Catch:{ all -> 0x007f }
        r3 = 5;	 Catch:{ all -> 0x007f }
        r4 = "temp_usage";	 Catch:{ all -> 0x007f }
        r2[r3] = r4;	 Catch:{ all -> 0x007f }
        r3 = 6;	 Catch:{ all -> 0x007f }
        r4 = "remain";	 Catch:{ all -> 0x007f }
        r2[r3] = r4;	 Catch:{ all -> 0x007f }
        r3 = 7;	 Catch:{ all -> 0x007f }
        r4 = "accept";	 Catch:{ all -> 0x007f }
        r2[r3] = r4;	 Catch:{ all -> 0x007f }
        r3 = 8;	 Catch:{ all -> 0x007f }
        r4 = "reject";	 Catch:{ all -> 0x007f }
        r2[r3] = r4;	 Catch:{ all -> 0x007f }
        r3 = 0;	 Catch:{ all -> 0x007f }
        r4 = 0;	 Catch:{ all -> 0x007f }
        r5 = 0;	 Catch:{ all -> 0x007f }
        r6 = 0;	 Catch:{ all -> 0x007f }
        r7 = 0;	 Catch:{ all -> 0x007f }
        r9 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ all -> 0x007f }
        if (r9 == 0) goto L_0x0086;	 Catch:{ all -> 0x007f }
    L_0x005a:
        r1 = r9.getCount();	 Catch:{ all -> 0x007f }
        if (r1 == 0) goto L_0x0086;	 Catch:{ all -> 0x007f }
    L_0x0060:
        r1 = r9.moveToNext();	 Catch:{ all -> 0x007f }
        if (r1 == 0) goto L_0x0086;	 Catch:{ all -> 0x007f }
    L_0x0066:
        r1 = 1;	 Catch:{ all -> 0x007f }
        r11 = r9.getString(r1);	 Catch:{ all -> 0x007f }
        r1 = 1;	 Catch:{ all -> 0x007f }
        r8 = new java.lang.String[r1];	 Catch:{ all -> 0x007f }
        r1 = 0;	 Catch:{ all -> 0x007f }
        r2 = java.lang.String.valueOf(r11);	 Catch:{ all -> 0x007f }
        r8[r1] = r2;	 Catch:{ all -> 0x007f }
        r1 = "wifiapdata";	 Catch:{ all -> 0x007f }
        r2 = "mac=?";	 Catch:{ all -> 0x007f }
        r0.update(r1, r10, r2, r8);	 Catch:{ all -> 0x007f }
        goto L_0x0060;
    L_0x007f:
        r1 = move-exception;
        if (r9 == 0) goto L_0x0085;
    L_0x0082:
        r9.close();
    L_0x0085:
        throw r1;
    L_0x0086:
        if (r9 == 0) goto L_0x008b;
    L_0x0088:
        r9.close();
    L_0x008b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiApDatabaseHelper.setDatabaseDefaultValue():void");
    }

    public WifiApDatabaseHelper(Context context) {
        super(new WifiApDataBaseContext(context, PATH), NAME, null, 1);
        this.mContext = context;
        this.mWifiApDataHandler = new WifiApDataHandler();
        registerForBroadcasts();
        setDatabaseDefaultValue();
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WIFIAPDATA_TABLE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public String getMobileIfaceName(LinkProperties linkProperties) {
        if (linkProperties == null) {
            return null;
        }
        String iface;
        RouteInfo ipv4Default = RouteInfo.selectBestRoute(linkProperties.getAllRoutes(), Inet4Address.ANY);
        if (ipv4Default != null) {
            iface = ipv4Default.getInterface();
        } else {
            iface = linkProperties.getInterfaceName();
        }
        return iface;
    }

    private void registerForBroadcasts() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_AP_STA_STATUS_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_AP_STATE_CHANGED_ACTION);
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
    }

    public void addList(String mac, String ip) {
        Log.d(TAG, "addList List size : " + this.mWifiApList.size() + ", mac : " + mac + ", ip : " + ip);
        if (!ip.equals(getDataInfoFromDb(mac).getIp())) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("ip", ip);
            db.update(TABLE_NAME, values, "mac=?", new String[]{String.valueOf(mac)});
        }
        this.mWifiApList.add(mac);
        if (this.mobileInterface != null) {
            monitorStaData(mac);
            this.mWifiApDataHandler.resume();
        }
    }

    public boolean isWifiApListContain(String mac) {
        if (this.mWifiApList.size() <= 0 || !this.mWifiApList.contains(mac)) {
            return false;
        }
        return true;
    }

    public boolean isWifiApDbContain(String mac) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(TABLE_NAME, new String[]{"id", MAC, "ip", USAGE, LIMIT_DATA, TEMP_USAGE, REMAIN, ACCEPT, REJECT}, "mac=?", new String[]{String.valueOf(mac)}, null, null, null);
            if (((cursor.getCount() != 0 ? 1 : 0) & (cursor != null ? 1 : 0)) != 0) {
                result = true;
            }
            if (cursor != null) {
                cursor.close();
            }
            return result;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public long insert(WifiApDataInfo info) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MAC, info.getMac());
        values.put("ip", info.getIp());
        values.put(USAGE, info.getUsage());
        values.put(LIMIT_DATA, info.getLimit());
        values.put(TEMP_USAGE, info.getTempUsage());
        values.put(REMAIN, info.getRemain());
        values.put(ACCEPT, Boolean.valueOf(info.getAccept()));
        values.put(REJECT, Boolean.valueOf(info.getReject()));
        long row = db.insert(TABLE_NAME, null, values);
        addList(info.getMac(), info.getIp());
        return row;
    }

    public void modifyLimtData(String mac, String limit) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USAGE, "0");
        cv.put(LIMIT_DATA, limit);
        cv.put(TEMP_USAGE, "0");
        cv.put(REMAIN, limit);
        db.update(TABLE_NAME, cv, "mac=?", new String[]{String.valueOf(mac)});
        if (this.mobileInterface != null) {
            clearIptablesCmd(mac);
            monitorStaData(mac);
            this.mWifiApDataHandler.resume();
        }
    }

    public void modifyUsageData(String mac, String data) {
        WifiApDataInfo mWifiApDataInfo = getDataInfoFromDb(mac);
        String ip = mWifiApDataInfo.getIp();
        BigDecimal limit = new BigDecimal(mWifiApDataInfo.getLimit());
        BigDecimal preRemain = new BigDecimal(mWifiApDataInfo.getRemain());
        String remain = null;
        BigDecimal temp_usage = new BigDecimal(mWifiApDataInfo.getTempUsage());
        BigDecimal usage = new BigDecimal(mWifiApDataInfo.getUsage());
        BigDecimal ZERO = new BigDecimal("0");
        Log.d(TAG, "modifyUsageData mac : " + mac + ", ip : " + ip + ", limit : " + limit.toString() + ", preRemain : " + preRemain.toString() + ", temp_usage : " + temp_usage.toString() + ", usage : " + usage.toString());
        BigDecimal bigDecimal;
        if (usage.compareTo(ZERO) == 0 && temp_usage.compareTo(ZERO) == 0) {
            bigDecimal = new BigDecimal(data);
            temp_usage = bigDecimal;
            remain = addAndSubtract(limit.toString(), temp_usage.toString(), false);
        } else if (usage.compareTo(ZERO) == 1 && temp_usage.compareTo(ZERO) == 1) {
            BigDecimal sub = new BigDecimal(addAndSubtract(data, usage.toString(), false));
            if (sub.compareTo(preRemain) >= 0) {
                usage = ZERO;
                temp_usage = ZERO;
                remain = ZERO.toString();
                if (setDataMonitor(ip, false) && rejectStaData(ip, true)) {
                    updateMonitorAndReject(mac, false, true);
                }
                this.mContext.sendBroadcast(new Intent("com.samsung.android.net.wifi.ap.STA_DATA"));
            } else {
                bigDecimal = new BigDecimal(data);
                bigDecimal = new BigDecimal(addAndSubtract(temp_usage.toString(), sub.toString(), true));
                remain = addAndSubtract(preRemain.toString(), sub.toString(), false);
                temp_usage = bigDecimal;
            }
        } else if (usage.compareTo(ZERO) == 0 && temp_usage.compareTo(ZERO) == 1) {
            bigDecimal = new BigDecimal(addAndSubtract(data, temp_usage.toString(), true));
            if (bigDecimal.compareTo(limit) >= 0) {
                usage = ZERO;
                temp_usage = ZERO;
                remain = ZERO.toString();
                if (setDataMonitor(ip, false) && rejectStaData(ip, true)) {
                    updateMonitorAndReject(mac, false, true);
                }
                this.mContext.sendBroadcast(new Intent("com.samsung.android.net.wifi.ap.STA_DATA"));
            } else {
                bigDecimal = new BigDecimal(data);
                remain = addAndSubtract(limit.toString(), bigDecimal.toString(), false);
                temp_usage = bigDecimal;
            }
        }
        ContentValues cv = new ContentValues();
        cv.put(USAGE, usage.toString());
        cv.put(TEMP_USAGE, temp_usage.toString());
        cv.put(REMAIN, remain);
        getReadableDatabase().update(TABLE_NAME, cv, "mac=?", new String[]{String.valueOf(mac)});
    }

    public void updateMonitorAndReject(String mac, boolean accept, boolean reject) {
        ContentValues cv = new ContentValues();
        cv.put(ACCEPT, accept ? AudioParameter.AUDIO_PARAMETER_VALUE_true : AudioParameter.AUDIO_PARAMETER_VALUE_false);
        cv.put(REJECT, reject ? AudioParameter.AUDIO_PARAMETER_VALUE_true : AudioParameter.AUDIO_PARAMETER_VALUE_false);
        getReadableDatabase().update(TABLE_NAME, cv, "mac=?", new String[]{String.valueOf(mac)});
    }

    public WifiApDataInfo getDataInfoFromDb(String mac) {
        Cursor cursor = null;
        SQLiteDatabase db = getReadableDatabase();
        String[] args = new String[]{String.valueOf(mac)};
        try {
            WifiApDataInfo result;
            cursor = db.query(TABLE_NAME, new String[]{"id", MAC, "ip", USAGE, LIMIT_DATA, TEMP_USAGE, REMAIN, ACCEPT, REJECT}, "mac=?", args, null, null, null);
            if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst()) {
                result = null;
            } else {
                result = new WifiApDataInfo(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
            }
            if (cursor != null) {
                cursor.close();
            }
            return result;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String addAndSubtract(String v1, String v2, boolean add) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        if (add) {
            return b1.add(b2).toString();
        }
        return b1.subtract(b2).toString();
    }

    public void checkWifiApList() {
        if (this.mWifiApList.size() > 0) {
            for (String mac : this.mWifiApList) {
                monitorStaData(mac);
            }
            this.mWifiApDataHandler.resume();
        }
    }

    public void monitorStaData(String mac) {
        WifiApDataInfo mWifiApDataInfo = getDataInfoFromDb(mac);
        String ip = mWifiApDataInfo.getIp();
        BigDecimal remain = new BigDecimal(mWifiApDataInfo.getRemain());
        BigDecimal ZERO = new BigDecimal("0");
        Log.d(TAG, "monitorStaData mac : " + mac + ", ip : " + ip + ", accept : " + mWifiApDataInfo.getAccept() + ", reject : " + mWifiApDataInfo.getReject() + ", remain : " + remain.toString());
        if (mWifiApDataInfo.getReject() || remain.compareTo(ZERO) == 0) {
            if (rejectStaData(ip, true)) {
                updateMonitorAndReject(mac, false, true);
            }
        } else if (!mWifiApDataInfo.getReject() && setDataMonitor(ip, true)) {
            updateMonitorAndReject(mac, true, false);
        }
    }

    public void clearIptablesCmd(String mac) {
        WifiApDataInfo mWifiApDataInfo = getDataInfoFromDb(mac);
        String ip = mWifiApDataInfo.getIp();
        Log.d(TAG, "monitorStaData mac : " + mac + ", ip : " + ip + ", accept : " + mWifiApDataInfo.getAccept() + ", reject : " + mWifiApDataInfo.getReject());
        if (!mWifiApDataInfo.getAccept() || mWifiApDataInfo.getReject()) {
            if (!mWifiApDataInfo.getAccept() && mWifiApDataInfo.getReject() && rejectStaData(ip, false)) {
                updateMonitorAndReject(mac, false, false);
            }
        } else if (setDataMonitor(ip, false)) {
            updateMonitorAndReject(mac, false, false);
        }
    }

    public void clearWifiApList(boolean enable) {
        Log.d(TAG, "clearWifiApList List size : " + this.mWifiApList.size());
        if (this.mWifiApList.size() > 0) {
            Iterator it = this.mWifiApList.iterator();
            while (it.hasNext()) {
                String mMac = (String) it.next();
                Log.d(TAG, "mMac : " + mMac);
                clearIptablesCmd(mMac);
                if (enable) {
                    setUsageToZero(mMac);
                    it.remove();
                }
            }
        }
        this.mWifiApDataHandler.pause();
    }

    public void removeStaFromApList(String mac) {
        Log.d(TAG, "removeStaFromApList mac : " + mac + ", List size : " + this.mWifiApList.size());
        if (this.mWifiApList.size() > 0) {
            Iterator it = this.mWifiApList.iterator();
            while (it.hasNext()) {
                String mMac = (String) it.next();
                Log.d(TAG, "mMac : " + mMac);
                if (mMac.equals(mac)) {
                    clearIptablesCmd(mac);
                    setUsageToZero(mac);
                    it.remove();
                    return;
                }
            }
        }
    }

    public void setUsageToZero(String mac) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USAGE, "0");
        db.update(TABLE_NAME, cv, "mac=?", new String[]{String.valueOf(mac)});
    }

    public boolean execIptables(String input_cmd, String output_cmd) {
        try {
            Log.d(TAG, "input_cmd : " + input_cmd);
            Process p = Runtime.getRuntime().exec(input_cmd);
            Log.d(TAG, "output_cmd : " + output_cmd);
            p = Runtime.getRuntime().exec(output_cmd);
            try {
                p.waitFor();
                p.destroy();
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public boolean setDataMonitor(String ip, boolean enable) {
        Log.d(TAG, "setDataMonitor ip : " + ip + ", enable : " + enable + ", mobileInterface : " + this.mobileInterface);
        if (this.mobileInterface == null) {
            return false;
        }
        String input_cmd;
        String output_cmd;
        if (enable) {
            input_cmd = "iptables -A FORWARD  -s " + ip + OUTPUT + this.mobileInterface;
            output_cmd = "iptables -A FORWARD  -d " + ip + INPUT + this.mobileInterface;
        } else {
            input_cmd = "iptables -D FORWARD  -s " + ip + OUTPUT + this.mobileInterface;
            output_cmd = "iptables -D FORWARD  -d " + ip + INPUT + this.mobileInterface;
        }
        return execIptables(input_cmd, output_cmd);
    }

    public boolean rejectStaData(String ip, boolean enable) {
        Log.d(TAG, "rejectStaData ip : " + ip + ", enable : " + enable + ", mobileInterface : " + this.mobileInterface);
        if (this.mobileInterface == null) {
            return false;
        }
        String input_cmd;
        String output_cmd;
        if (enable) {
            input_cmd = "iptables -A FORWARD  -s " + ip + OUTPUT + this.mobileInterface + IPTABLES_REJECT;
            output_cmd = "iptables -A FORWARD  -d " + ip + INPUT + this.mobileInterface + IPTABLES_REJECT;
        } else {
            input_cmd = "iptables -D FORWARD  -s " + ip + OUTPUT + this.mobileInterface + IPTABLES_REJECT;
            output_cmd = "iptables -D FORWARD  -d " + ip + INPUT + this.mobileInterface + IPTABLES_REJECT;
        }
        return execIptables(input_cmd, output_cmd);
    }

    public boolean skipGetUsageData(String mac) {
        if (this.mobileInterface == null) {
            return true;
        }
        WifiApDataInfo mWifiApDataInfo = getDataInfoFromDb(mac);
        if (mWifiApDataInfo == null) {
            return true;
        }
        if (mWifiApDataInfo.getAccept() || !mWifiApDataInfo.getReject()) {
            return false;
        }
        return true;
    }

    public String getStaUsageData(String mac) {
        IOException e;
        Throwable th;
        DataInputStream input = null;
        long data = 0;
        String[] cmd = new String[]{"/system/bin/sh", "-c", IPTABLES_GREP + getDataInfoFromDb(mac).getIp()};
        try {
            Log.d(TAG, "cmd = " + IPTABLES_GREP + getDataInfoFromDb(mac).getIp());
            Process p = Runtime.getRuntime().exec(cmd);
            DataInputStream input2 = new DataInputStream(p.getInputStream());
            while (true) {
                try {
                    String line = input2.readLine();
                    if (line != null) {
                        Log.d(TAG, "line : " + line);
                        data += Long.parseLong(line.trim().split("\\s+")[1]);
                    } else {
                        try {
                            break;
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                            if (input2 != null) {
                                try {
                                    input2.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                    input = input2;
                                    return null;
                                }
                            }
                            input = input2;
                            return null;
                        }
                    }
                } catch (IOException e4) {
                    e3 = e4;
                    input = input2;
                } catch (Throwable th2) {
                    th = th2;
                    input = input2;
                }
            }
            p.waitFor();
            p.destroy();
            if (input2 != null) {
                try {
                    input2.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                    input = input2;
                    return null;
                }
            }
            input = input2;
            return Long.toString(data);
        } catch (IOException e5) {
            e32 = e5;
            try {
                e32.printStackTrace();
                if (input == null) {
                    return null;
                }
                try {
                    input.close();
                    return null;
                } catch (IOException e322) {
                    e322.printStackTrace();
                    return null;
                }
            } catch (Throwable th3) {
                th = th3;
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e3222) {
                        e3222.printStackTrace();
                        return null;
                    }
                }
                throw th;
            }
        }
    }
}
