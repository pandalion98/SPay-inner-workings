package android.mtp;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Audio.Playlists;
import android.provider.MediaStore.Audio.Playlists.Members;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Video.VideoColumns;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Calendar;

public class MTPJNIInterface {
    static final int CONNECTED = 3;
    static final int DOWNLOADING = 4;
    private static final int GETTHUMBNAILTIME = 15000000;
    private static final int MICRO_HEIGH = 256;
    private static final int MICRO_WIDTH = 256;
    private static String MTP_DEBUG_LEVEL = null;
    private static final int MTP_MSGID_DEVICE_LOG = 5;
    private static final int MTP_MSGID_SIDESYNC_START = 7;
    static boolean RegisterBroadcast = false;
    static boolean RegisterBroadcast1 = false;
    static final int SENDING = 5;
    static final int STORAGE_INFO_EVENT = 26;
    static final int SYNCHRONIZING = 10;
    static final String TAG = "MTPJNIInterface";
    static final int USB_REMOVED = 19;
    static final int USB_TETHERING_ENABLED = 14;
    static ContentResolver cr;
    public static int gadgetResetStatus = 0;
    private static boolean isMtpLogOn;
    private static Handler mHandler;
    private static Context mcontext;
    private static MTPJNIInterface mtpJNIInterface = new MTPJNIInterface();
    public static boolean objectEventReceived = false;
    private static int scannerStatus = 0;
    private String DeviceName = null;
    private String album = ProxyInfo.LOCAL_EXCL_LIST;
    private String artist = ProxyInfo.LOCAL_EXCL_LIST;
    private String composer = ProxyInfo.LOCAL_EXCL_LIST;
    private String creationDate = ProxyInfo.LOCAL_EXCL_LIST;
    private String dateTaken = ProxyInfo.LOCAL_EXCL_LIST;
    private String description = ProxyInfo.LOCAL_EXCL_LIST;
    private String displayname = ProxyInfo.LOCAL_EXCL_LIST;
    private long duration = 0;
    private String filename = ProxyInfo.LOCAL_EXCL_LIST;
    private String genreName = ProxyInfo.LOCAL_EXCL_LIST;
    private long height = 0;
    private String id = ProxyInfo.LOCAL_EXCL_LIST;
    private String language = ProxyInfo.LOCAL_EXCL_LIST;
    private String latitude = ProxyInfo.LOCAL_EXCL_LIST;
    private String longitude = ProxyInfo.LOCAL_EXCL_LIST;
    private final BroadcastReceiver mMtpReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                Log.e(MTPJNIInterface.TAG, "In MTPJNIINterface onReceive:" + action);
                String path;
                if (action.equals("com.android.MTP.OBJECT_ADDED")) {
                    MTPJNIInterface.objectEventReceived = true;
                    path = intent.getStringExtra("Path");
                    if (path != null) {
                        Log.e(MTPJNIInterface.TAG, "***** file path of  sendObjectAdded ");
                        MTPJNIInterface.MTP_LOG_PRINT(MTPJNIInterface.TAG, "***** file path of  sendObjectAdded = " + path);
                        MTPJNIInterface.this.sendObjectAdded(path);
                        return;
                    }
                    Log.e(MTPJNIInterface.TAG, "sendObjectAdded path is null ");
                    return;
                } else if (action.equals("com.android.MTP.OBJECT_REMOVED")) {
                    MTPJNIInterface.objectEventReceived = true;
                    path = intent.getStringExtra("Path");
                    if (path != null) {
                        Log.e(MTPJNIInterface.TAG, "***** file path of  sendObjectRemoved ");
                        MTPJNIInterface.MTP_LOG_PRINT(MTPJNIInterface.TAG, "***** file path of  sendObjectRemoved = " + path);
                        MTPJNIInterface.this.sendObjectRemoved(path);
                        return;
                    }
                    Log.e(MTPJNIInterface.TAG, "sendObjectRemoved path is null ");
                    return;
                } else if (action.equals("com.android.MTP.OBJECT_PROP_CHANGED")) {
                    Uri uri = intent.getData();
                    if (uri != null) {
                        path = uri.getPath();
                        if (path != null) {
                            Log.e(MTPJNIInterface.TAG, "***** file path of OBJECT_PROP_CHANGED ");
                            MTPJNIInterface.MTP_LOG_PRINT(MTPJNIInterface.TAG, "***** file path of OBJECT_PROP_CHANGED = " + path);
                            MTPJNIInterface.this.sendObjectPropChanged(path);
                            return;
                        }
                        Log.e(MTPJNIInterface.TAG, "OBJECT_PROP_CHANGED path is null ");
                        return;
                    }
                    Log.e(MTPJNIInterface.TAG, "uri object is null ");
                    path = intent.getStringExtra("Path");
                    if (path != null) {
                        Log.e(MTPJNIInterface.TAG, "***** file path of  OBJECT_PROP_CHANGED ");
                        MTPJNIInterface.MTP_LOG_PRINT(MTPJNIInterface.TAG, "***** file path of  OBJECT_PROP_CHANGED = " + path);
                        MTPJNIInterface.this.sendObjectPropChanged(path);
                        return;
                    }
                    Log.e(MTPJNIInterface.TAG, "OBJECT_PROP_CHANGED path is null ");
                    return;
                } else if (action.equals(Intent.ACTION_BATTERY_CHANGED) && intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == 0 && MTPJNIInterface.mHandler != null) {
                    Message msg = MTPJNIInterface.mHandler.obtainMessage();
                    msg.what = 19;
                    MTPJNIInterface.mHandler.sendMessage(msg);
                    return;
                } else {
                    return;
                }
            }
            Log.e(MTPJNIInterface.TAG, "In MTPJNIINterface intent is coming null:");
        }
    };
    private final BroadcastReceiver mMtpReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                Log.e(MTPJNIInterface.TAG, "In MTPJNIINterface onReceive:" + action);
                if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED) && MTPJNIInterface.objectEventReceived) {
                    MTPJNIInterface.objectEventReceived = false;
                    MTPJNIInterface.this.notifyMTPStack(26);
                    Log.e(MTPJNIInterface.TAG, "storageinfo changed event sent to stack");
                    return;
                }
                return;
            }
            Log.e(MTPJNIInterface.TAG, "In MTPJNIINterface intent is coming null::");
        }
    };
    private String mimeType = ProxyInfo.LOCAL_EXCL_LIST;
    private String modificationDate = ProxyInfo.LOCAL_EXCL_LIST;
    private String path = ProxyInfo.LOCAL_EXCL_LIST;
    private String resolution = ProxyInfo.LOCAL_EXCL_LIST;
    private String size = ProxyInfo.LOCAL_EXCL_LIST;
    private String title = ProxyInfo.LOCAL_EXCL_LIST;
    private long width = 0;
    private int year = 0;

    public static final class MusicPlaylist {
        public static final String AUDIO_DATA_HASHCODE = "audio_data_hashcode";
        public static final String AUDIO_MUSIC_PLAYLISTS_MAP = "audio_playlists_map";
        public static final String AUTHORITY = "media";
        private static final String CONTENT_AUTHORITY_SLASH = "content://media/";
        public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");

        public static Uri getContentUri(String volumeName) {
            return Uri.parse(CONTENT_AUTHORITY_SLASH + volumeName + "/audio/playlists");
        }

        public static Uri getMembersContentUri(String volumeName, long playlistId) {
            return Uri.parse(CONTENT_AUTHORITY_SLASH + volumeName + "/audio/playlists/" + playlistId + "/members");
        }

        public static Uri getMembersContentUri(String volumeName) {
            return Uri.parse(CONTENT_AUTHORITY_SLASH + volumeName + "/audio/playlists/members");
        }
    }

    private native void alertNotiHandler(int i);

    private native void enumerateMtp(int i, int i2);

    private native void objectAdded(String str);

    private native void objectPlaAdded(String str);

    private native void objectPlaRemoved(String str);

    private native void objectPropChanged(String str);

    private native void objectRemoved(String str);

    private native void setCryptionKey(String str);

    private native void testMtpCommands(int i, int i2);

    static {
        MTP_DEBUG_LEVEL = ProxyInfo.LOCAL_EXCL_LIST;
        isMtpLogOn = false;
        String debugLevel = SystemProperties.get("ro.debug_level", "Unknown");
        String debugType = SystemProperties.get("ro.build.type", "Unknown");
        String countryCode = SystemProperties.get("ro.csc.country_code", "Unknown");
        if (Context.USER_SERVICE.equals(debugType) && "KOREA".equals(countryCode)) {
            MTP_DEBUG_LEVEL = "0x494d";
        } else {
            MTP_DEBUG_LEVEL = "0x4948";
        }
        if (debugLevel.equals(MTP_DEBUG_LEVEL)) {
            isMtpLogOn = true;
        } else {
            isMtpLogOn = false;
        }
        try {
            System.loadLibrary("mtp_samsung_jni");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "WARNING: Could not load libmtp__samsung_jni.so");
        }
    }

    private MTPJNIInterface() {
    }

    public static synchronized MTPJNIInterface getInstance() {
        MTPJNIInterface mTPJNIInterface;
        synchronized (MTPJNIInterface.class) {
            mTPJNIInterface = mtpJNIInterface;
        }
        return mTPJNIInterface;
    }

    public void setContext(Context context) {
        mcontext = context;
        cr = mcontext.getContentResolver();
        if (!RegisterBroadcast) {
            registerBroadCastRec();
        }
        if (!RegisterBroadcast1) {
            registerBroadCastRec1();
        }
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public void mtpCommands(int opCode, int transactionId) {
        Log.e(TAG, "MTP in testMtpCommands of MTPJNIInterface: code = " + opCode + "transactionId = " + transactionId);
        alertNotiHandler(2);
    }

    private void registerBroadCastRec() {
        Log.e(TAG, "< MTP > Registering BroadCast receiver :::::");
        IntentFilter lIntentFilter = new IntentFilter();
        lIntentFilter.addAction("com.android.MTP.OBJECT_ADDED");
        lIntentFilter.addAction("com.android.MTP.OBJECT_PROP_CHANGED");
        lIntentFilter.addAction("com.android.MTP.OBJECT_REMOVED");
        mcontext.registerReceiver(this.mMtpReceiver, lIntentFilter);
        RegisterBroadcast = true;
    }

    private void registerBroadCastRec1() {
        Log.e(TAG, "< MTP > Registering BroadCast receiver :::::::");
        IntentFilter lIntentFilter = new IntentFilter();
        lIntentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        lIntentFilter.addDataScheme(ContentResolver.SCHEME_FILE);
        mcontext.registerReceiver(this.mMtpReceiver1, lIntentFilter);
        RegisterBroadcast1 = true;
    }

    public void notifyMTPStack(int noti) {
        Log.e(TAG, "noti = " + noti);
        if (3 == noti && mcontext != null) {
            if (RegisterBroadcast) {
                mcontext.unregisterReceiver(this.mMtpReceiver);
                RegisterBroadcast = false;
            }
            if (RegisterBroadcast1) {
                mcontext.unregisterReceiver(this.mMtpReceiver1);
                RegisterBroadcast1 = false;
            }
        }
        alertNotiHandler(noti);
    }

    public String getdeviceName(int temp) {
        this.DeviceName = System.getString(mcontext.getContentResolver(), Global.DEVICE_NAME);
        if (this.DeviceName == null) {
            Log.e(TAG, "DeviceName is Null in System");
            this.DeviceName = Global.getString(mcontext.getContentResolver(), Global.DEVICE_NAME);
            if (this.DeviceName == null) {
                Log.e(TAG, "DeviceName is Null in Global");
                this.DeviceName = "Default";
            }
        }
        Log.e(TAG, "DeviceName is " + this.DeviceName);
        return this.DeviceName;
    }

    public void setDeviceName(byte[] name) {
        try {
            String strName = new String(name, "utf-8");
            Log.e(TAG, "Modify device_name to " + strName);
            if (System.getString(mcontext.getContentResolver(), Global.DEVICE_NAME) == null) {
                Log.e(TAG, "DeviceName is Null in System");
                Global.putString(mcontext.getContentResolver(), Global.DEVICE_NAME, strName);
                return;
            }
            System.putString(mcontext.getContentResolver(), Global.DEVICE_NAME, strName);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    public String getValueSettingDB(String strItemName) {
        String strVal = System.getString(mcontext.getContentResolver(), strItemName);
        if (strVal == null) {
            strVal = Global.getString(mcontext.getContentResolver(), strItemName);
        }
        Log.e(TAG, "Value of " + strItemName + " is " + strVal);
        return strVal;
    }

    public void setMediaScannerStatus(int status) {
        Log.e(TAG, "setting Media scanner status" + status);
        scannerStatus = status;
        Log.e(TAG, "After setting Media scanner status" + status);
    }

    public void sendObjectAdded(String path) {
        if (!path.endsWith(".pla")) {
            objectAdded(path);
        }
    }

    public void sendObjectPropChanged(String path) {
        if (!path.endsWith(".pla")) {
            objectPropChanged(path);
        }
    }

    public void sendObjectRemoved(String path) {
        if (!path.endsWith(".pla")) {
            objectRemoved(path);
        }
    }

    public int getMediaScannerStatus(int temp) {
        Log.e(TAG, "Getting media scanner status" + scannerStatus);
        return scannerStatus;
    }

    public int getGadgetResetStatus(int temp) {
        Log.e(TAG, "Getting gadget reset status" + gadgetResetStatus);
        return gadgetResetStatus;
    }

    public void getMtpEnumerate() {
        MTP_LOG_PRINT(TAG, "MTP in GetEnumerateMtp");
        enumerateMtp(1, 1);
    }

    public void sendPlaAdded(String playlist_path) {
        Log.e(TAG, "In sendPlaAdded playlist file path");
        MTP_LOG_PRINT(TAG, "In sendPlaAdded playlist file path is" + playlist_path);
        objectPlaAdded(playlist_path);
    }

    public void sendPlaRemoved(String playlist_path) {
        if (!playlist_path.contains(".pla")) {
            playlist_path = playlist_path.concat(".pla");
            Log.e(TAG, "sendPlaRemoved is added .pla ");
            MTP_LOG_PRINT(TAG, "sendPlaRemoved is added .pla " + playlist_path);
        }
        if (new File(playlist_path).exists()) {
            Log.e(TAG, "calling objectPlaRemoved from sendPlaRemoved ");
            objectPlaRemoved(playlist_path);
            return;
        }
        Log.e(TAG, "playlist file does not exist");
    }

    public void SetCryptionKey(String key) {
        Log.e(TAG, "Getting CryptionKey from JAVA");
        setCryptionKey(key);
    }

    public int GetBatteryLevel() {
        Log.e(TAG, "Getting battery Level from JAVA");
        return 10;
    }

    public void UpdateMediaDB(String filename) {
        MTP_LOG_PRINT(TAG, "Filename got from JNI " + filename);
        String contentName = filename;
        MTP_LOG_PRINT(TAG, "ContentName " + contentName);
        mcontext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(contentName))));
    }

    public void updateUiState(int notificationType) {
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage();
            msg.what = notificationType;
            mHandler.sendMessage(msg);
        }
    }

    public int deletePlaylist(String playlistName) {
        Log.e(TAG, "Playlist to be deleted");
        MTP_LOG_PRINT(TAG, "Playlist to be deleted  :" + playlistName);
        Uri playlistUri = Playlists.EXTERNAL_CONTENT_URI;
        Uri playlistFileUri = Files.getContentUri("external");
        Cursor cur = mcontext.getContentResolver().query(playlistUri, null, "name=\"" + playlistName + "\"", null, null);
        if (cur == null) {
            Log.e(TAG, "Cur is null");
            return -1;
        }
        int count = cur.getCount();
        Log.e(TAG, "Count" + count);
        if (count > 0) {
            cur.moveToFirst();
            int ret = mcontext.getContentResolver().delete(playlistFileUri, "_data=\"" + cur.getString(cur.getColumnIndex("_data")) + ".pla\"", null);
            Log.e(TAG, "Files Count" + ret);
            if (ret == 0) {
                Log.e(TAG, "Deletion unsuccessful" + ret);
            } else {
                Log.e(TAG, "Deletion successful" + ret);
            }
        }
        cur.close();
        return 1;
    }

    public int addPlaylist(String playlistName, String playlistPath) {
        MTP_LOG_PRINT(TAG, "Playlistname :" + playlistName);
        MTP_LOG_PRINT(TAG, "Playlist path:" + playlistPath);
        String playlistPathWithoutPla = playlistPath.substring(0, playlistPath.lastIndexOf("/")) + "/" + playlistName;
        Uri playlistUri = MusicPlaylist.EXTERNAL_CONTENT_URI;
        Uri playlistFileUri = Files.getContentUri("external");
        ContentValues values = new ContentValues();
        values.put("name", playlistName);
        values.put("_data", playlistPathWithoutPla);
        ContentValues valuesFile = new ContentValues();
        valuesFile.put("_data", playlistPathWithoutPla);
        ContentValues contentValues = valuesFile;
        contentValues.put("media_type", "4");
        contentValues = valuesFile;
        contentValues.put(FileColumns.FORMAT, "47621");
        Cursor cur = mcontext.getContentResolver().query(playlistUri, null, "name=\"" + playlistName + "\"", null, null);
        if (cur == null) {
            Log.e(TAG, "Cur is null");
            return -1;
        }
        int count = cur.getCount();
        Log.e(TAG, "Count" + count);
        int rowID;
        if (count == 0) {
            cur.close();
            Uri newUri = mcontext.getContentResolver().insert(playlistUri, values);
            if (newUri == null) {
                Log.e(TAG, "playlist Insertion failure");
                return 0;
            }
            Log.e(TAG, "The New URI " + newUri.toString());
            rowID = Integer.parseInt((String) newUri.getPathSegments().get(3));
            cur = mcontext.getContentResolver().query(playlistFileUri, null, "_data=\"" + playlistPathWithoutPla + "\"", null, null);
            if (cur == null) {
                Log.e(TAG, "Cur is null");
                return rowID;
            }
            count = cur.getCount();
            Log.e(TAG, "Count" + count);
            if (count > 0) {
                Log.e(TAG, "noOfRowsUpdated = " + mcontext.getContentResolver().update(playlistFileUri, valuesFile, "_data=\"" + playlistPathWithoutPla + "\"", null));
            } else if (count == 0 && mcontext.getContentResolver().insert(playlistFileUri, valuesFile) == null) {
                Log.e(TAG, "playlistFile Insertion failure");
            }
            cur.close();
            return rowID;
        }
        cur.moveToFirst();
        rowID = cur.getInt(cur.getColumnIndex("_id"));
        cur.close();
        return rowID;
    }

    public int getExternalStorageStatus(String mountPath) {
        String status = null;
        StorageManager storageManager = (StorageManager) mcontext.getSystemService(Context.STORAGE_SERVICE);
        if (mountPath != null) {
            try {
                status = storageManager.getVolumeState(mountPath);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Exception is coming while getting the Mount status");
                return 0;
            }
        }
        if (status == null) {
            return 0;
        }
        Log.e(TAG, "Status for mount/Unmount :" + status);
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "SDcard is  available");
            return 1;
        }
        Log.e(TAG, "SDcard is not available");
        return 0;
    }

    public int addtoPlaylist(String playlistName, String filename, int order) {
        MTP_LOG_PRINT(TAG, "Playlistname :" + playlistName);
        MTP_LOG_PRINT(TAG, "Filename: " + filename);
        Uri playlistUri = MusicPlaylist.EXTERNAL_CONTENT_URI;
        new ContentValues().put("name", playlistName);
        Cursor cur = mcontext.getContentResolver().query(playlistUri, null, "name=\"" + playlistName + "\"", null, null);
        if (cur == null) {
            Log.e(TAG, "Cur is null");
            return -1;
        }
        int count = cur.getCount();
        Log.e(TAG, "Count" + count);
        if (count == 0) {
            cur.close();
            return -1;
        }
        cur.moveToFirst();
        int rowID = cur.getInt(cur.getColumnIndex("_id"));
        cur.close();
        Log.e(TAG, "Playlist rowID" + rowID);
        ContentValues audioMAPValues = new ContentValues();
        Uri audioUri = Media.EXTERNAL_CONTENT_URI;
        Log.e(TAG, "URI for the audio DB is " + audioUri.toString());
        Cursor c1 = mcontext.getContentResolver().query(audioUri, null, "_data=\"" + filename + "\"", null, null);
        if (c1 == null) {
            Log.e(TAG, "c1 Cur is null");
            cur.close();
            return -1;
        }
        count = c1.getCount();
        Log.e(TAG, "Data from the audio count" + count);
        if (count == 0) {
            ContentValues values = new ContentValues();
            values.put("_data", filename);
            values.put(AudioColumns.IS_MUSIC, Integer.valueOf(1));
            values.put(AudioColumns.IS_RINGTONE, Integer.valueOf(0));
            values.put(AudioColumns.IS_ALARM, Integer.valueOf(0));
            values.put(AudioColumns.IS_NOTIFICATION, Integer.valueOf(0));
            audioUri = Media.EXTERNAL_CONTENT_URI;
            Log.e(TAG, "The audio URI " + audioUri.toString());
            Uri newUri = mcontext.getContentResolver().insert(audioUri, values);
            values.clear();
            values.put("date_modified", Integer.valueOf(0));
            if (newUri == null) {
                Log.e(TAG, "Insertion failure");
                cur.close();
                c1.close();
                return -1;
            }
            Log.e(TAG, "The New URI " + newUri.toString());
            mcontext.getContentResolver().update(newUri, values, null, null);
        }
        if (count == 0) {
            audioMAPValues = new ContentValues();
            audioUri = Media.EXTERNAL_CONTENT_URI;
            Log.e(TAG, "URI for the audio DB is " + audioUri.toString());
            c1.close();
            c1 = mcontext.getContentResolver().query(audioUri, null, "_data=\"" + filename + "\"", null, null);
            if (c1 == null) {
                Log.e(TAG, "Cur is null");
                cur.close();
                return -1;
            }
            count = c1.getCount();
            Log.e(TAG, "Data from the audio count" + count);
        }
        boolean add_in_playlist = true;
        if (count > 0) {
            c1.moveToFirst();
            int audioID = c1.getInt(c1.getColumnIndex("_id"));
            Cursor cur2 = mcontext.getContentResolver().query(Members.getContentUri("external", (long) rowID), null, "audio_id=\"" + audioID + "\"", null, null);
            if (cur2 == null) {
                Log.e(TAG, "Cursor cur2 is null");
                c1.close();
                return -1;
            }
            if (cur2.getCount() != 0) {
                add_in_playlist = false;
            }
            cur2.close();
        }
        if (count > 0) {
            c1.moveToFirst();
            audioID = c1.getInt(c1.getColumnIndex("_id"));
            Log.e(TAG, "Row ID for audio file" + rowID);
            c1.close();
            audioMAPValues.put("audio_id", Integer.valueOf(audioID));
            audioMAPValues.put("play_order", Integer.valueOf(order + 1));
            Log.e(TAG, "Playlist ID" + rowID);
            audioMAPValues.put(Members.PLAYLIST_ID, Integer.valueOf(rowID));
            if (add_in_playlist) {
                try {
                    Uri playlist_map = MusicPlaylist.getMembersContentUri("external", (long) rowID);
                    Log.e(TAG, "Audio map URI" + playlist_map.toString());
                    Log.e(TAG, "The New URI for the audio map" + mcontext.getContentResolver().insert(playlist_map, audioMAPValues).toString());
                } catch (Exception e) {
                    Log.e(TAG, "Exception" + e.getMessage());
                }
            }
            return 1;
        }
        c1.close();
        return -1;
    }

    public int getPlaylistId(String playlistName) {
        Cursor cur = mcontext.getContentResolver().query(MusicPlaylist.EXTERNAL_CONTENT_URI, null, "name=\"" + playlistName + "\"", null, null);
        if (cur == null) {
            Log.e(TAG, "Cur is null");
            return -1;
        }
        int rowID;
        int count = cur.getCount();
        Log.e(TAG, "Count" + count);
        if (count == 0) {
            rowID = -1;
        } else {
            cur.moveToFirst();
            rowID = cur.getInt(cur.getColumnIndex("_id"));
        }
        cur.close();
        Log.e(TAG, "Playlist ID" + rowID);
        return rowID;
    }

    public void deletefromMediaPlayer(String filename) {
        Log.e(TAG, "Row deleted for Audio" + mcontext.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data=\"" + filename + "\"", null));
        Log.e(TAG, "Row deleted Vedio" + mcontext.getContentResolver().delete(Video.Media.EXTERNAL_CONTENT_URI, "_data=\"" + filename + "\"", null));
        Log.e(TAG, "Row deleted Vedio from Gallary DB" + mcontext.getContentResolver().delete(Video.Media.EXTERNAL_CONTENT_URI, "_data=\"" + filename + "\"", null));
        Log.e(TAG, "Row deleted Image" + mcontext.getContentResolver().delete(Images.Media.EXTERNAL_CONTENT_URI, "_data=\"" + filename + "\"", null));
        Log.e(TAG, "Row deleted Vedio from Gallary DB" + mcontext.getContentResolver().delete(Images.Media.EXTERNAL_CONTENT_URI, "_data=\"" + filename + "\"", null));
    }

    private Bitmap rotate(Bitmap b, int degrees) {
        if (degrees == 0 || b == null) {
            return b;
        }
        Matrix m = new Matrix();
        m.setRotate((float) degrees, ((float) b.getWidth()) / 2.0f, ((float) b.getHeight()) / 2.0f);
        try {
            Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
            if (b.equals(b2)) {
                return b;
            }
            b.recycle();
            return b2;
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
            return b;
        }
    }

    private Bitmap getSampleSizeBitmap(int targetWidthHeight, String path) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        if (targetWidthHeight != -1) {
            options.inSampleSize = computeSampleSize(options, targetWidthHeight);
        }
        bm = BitmapFactory.decodeFile(path, options);
        int degree = getExifOrientation(path);
        if (degree != 0) {
            return rotate(bm, degree);
        }
        return bm;
    }

    private int getExifOrientation(String filepath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
        }
        if (exif == null) {
            return 0;
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
        if (orientation == -1) {
            return 0;
        }
        switch (orientation) {
            case 3:
                return 180;
            case 6:
                return 90;
            case 8:
                return 270;
            default:
                return 0;
        }
    }

    private int computeSampleSize(Options options, int target) {
        int w = options.outWidth;
        int h = options.outHeight;
        int candidate = Math.max(w / target, h / target);
        if (candidate == 0) {
            return 1;
        }
        if (candidate > 1 && w > target && w / candidate < target) {
            candidate--;
        }
        if (candidate > 1 && h > target && h / candidate < target) {
            candidate--;
        }
        return candidate;
    }

    private Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, boolean scaleUp) {
        if (source == null) {
            return null;
        }
        float scale;
        Bitmap b1;
        if (source.getWidth() < source.getHeight()) {
            scale = ((float) targetWidth) / ((float) source.getWidth());
        } else {
            scale = ((float) targetHeight) / ((float) source.getHeight());
        }
        if (scaler != null) {
            scaler.setScale(scale, scale);
            b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler, true);
        } else {
            b1 = source;
        }
        Bitmap b2 = Bitmap.createBitmap(b1, Math.max(0, b1.getWidth() - targetWidth) / 2, Math.max(0, b1.getHeight() - targetHeight) / 2, targetWidth, targetHeight);
        if (!b1.equals(source)) {
            return b2;
        }
        b1.recycle();
        return b2;
    }

    private Bitmap getImageThumbBitmap(String path) {
        Matrix m = new Matrix();
        Bitmap tempB = null;
        Bitmap b = null;
        try {
            tempB = getSampleSizeBitmap(1024, path);
            b = transform(m, tempB, 256, 256, true);
            if (tempB != null) {
                tempB.recycle();
            }
        } catch (Exception e) {
            if (tempB != null) {
                tempB.recycle();
            }
            e.printStackTrace();
        }
        return b;
    }

    private Bitmap getVideoThumbBitmap(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            float scale;
            retriever.setDataSource(path);
            retriever.setVideoSize(256, 256, false, true);
            Bitmap bitmap = retriever.getFrameAtTime(15000000);
            if (bitmap.getWidth() < bitmap.getHeight()) {
                scale = 256.0f / ((float) bitmap.getWidth());
            } else {
                scale = 256.0f / ((float) bitmap.getHeight());
            }
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            retriever.release();
            return transform(matrix, bitmap, 256, 256, false);
        } catch (RuntimeException e) {
            e.printStackTrace();
            retriever.release();
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getThumbnail(java.lang.String r19, int r20) {
        /*
        r18 = this;
        r17 = 0;
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r2.<init>();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = mcontext;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = r4.getFilesDir();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = r4.getParent();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r2 = r2.append(r4);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = "/test_thumb_img.jpg";
        r2 = r2.append(r4);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r16 = r2.toString();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r2 = 1;
        r0 = r20;
        if (r0 != r2) goto L_0x013d;
    L_0x0024:
        r2 = "MTPJNIInterface";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4.<init>();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r5 = "inside getThumbnail for images for ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r0 = r19;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        MTP_LOG_PRINT(r2, r4);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r15 = r18.getImageThumbBitmap(r19);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        if (r15 == 0) goto L_0x0090;
    L_0x0045:
        r9 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r9.<init>();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r2 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = 60;
        r15.compress(r2, r4, r9);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r8 = r9.toByteArray();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r0 = r8.length;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r17 = r0;
        r13 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r0 = r16;
        r13.<init>(r0);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r13.write(r8);	 Catch:{ Exception -> 0x0084, OutOfMemoryError -> 0x00de }
        r13.close();	 Catch:{ Exception -> 0x0084, OutOfMemoryError -> 0x00de }
    L_0x0065:
        r15.recycle();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
    L_0x0068:
        r2 = "MTPJNIInterface";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4.<init>();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r5 = "image's thumb size is ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r0 = r17;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        MTP_LOG_PRINT(r2, r4);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
    L_0x0083:
        return r17;
    L_0x0084:
        r12 = move-exception;
        r13.close();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        goto L_0x0065;
    L_0x0089:
        r12 = move-exception;
        r12.printStackTrace();
        r17 = 0;
        goto L_0x0083;
    L_0x0090:
        r2 = "MTPJNIInterface";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4.<init>();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r5 = "Image's bitmap is coming null try from MediaStore's API ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r0 = r19;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        android.util.Log.e(r2, r4);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r3 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r2 = mcontext;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r2 = r2.getContentResolver();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = 0;
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r5.<init>();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r6 = "_data =\"";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r0 = r19;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r6 = "\"";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r6 = 0;
        r7 = 0;
        r11 = r2.query(r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        if (r11 != 0) goto L_0x00e2;
    L_0x00d6:
        r2 = "MTPJNIInterface";
        r4 = "Cur is null";
        android.util.Log.e(r2, r4);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        goto L_0x0083;
    L_0x00de:
        r12 = move-exception;
        r17 = 0;
        goto L_0x0083;
    L_0x00e2:
        r10 = r11.getCount();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r14 = 0;
        if (r10 != 0) goto L_0x00f4;
    L_0x00e9:
        r2 = "MTPJNIInterface";
        r4 = "Count is zero";
        android.util.Log.e(r2, r4);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r11.close();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        goto L_0x0083;
    L_0x00f4:
        r11.moveToFirst();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r2 = "_id";
        r2 = r11.getColumnIndex(r2);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r14 = r11.getInt(r2);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r11.close();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r2 = mcontext;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r2 = r2.getContentResolver();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = (long) r14;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r6 = 1;
        r7 = 0;
        r15 = android.provider.MediaStore.Images.Thumbnails.getThumbnail(r2, r4, r6, r7);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        if (r15 == 0) goto L_0x0068;
    L_0x0113:
        r9 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r9.<init>();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r2 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = 60;
        r15.compress(r2, r4, r9);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r8 = r9.toByteArray();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r0 = r8.length;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r17 = r0;
        r13 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r0 = r16;
        r13.<init>(r0);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r13.write(r8);	 Catch:{ Exception -> 0x0138, OutOfMemoryError -> 0x00de }
        r13.close();	 Catch:{ Exception -> 0x0138, OutOfMemoryError -> 0x00de }
    L_0x0133:
        r15.recycle();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        goto L_0x0068;
    L_0x0138:
        r12 = move-exception;
        r13.close();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        goto L_0x0133;
    L_0x013d:
        r2 = 2;
        r0 = r20;
        if (r0 != r2) goto L_0x0083;
    L_0x0142:
        r15 = r18.getVideoThumbBitmap(r19);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        if (r15 == 0) goto L_0x0189;
    L_0x0148:
        r9 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r9.<init>();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r2 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = 60;
        r15.compress(r2, r4, r9);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r8 = r9.toByteArray();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r0 = r8.length;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r17 = r0;
        r2 = "MTPJNIInterface";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4.<init>();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r5 = "Videos's thumbnail size is ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r5 = r8.length;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        MTP_LOG_PRINT(r2, r4);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r13 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r0 = r16;
        r13.<init>(r0);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        r13.write(r8);	 Catch:{ Exception -> 0x0183, OutOfMemoryError -> 0x00de }
        r13.close();	 Catch:{ Exception -> 0x0183, OutOfMemoryError -> 0x00de }
        goto L_0x0083;
    L_0x0183:
        r12 = move-exception;
        r13.close();	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        goto L_0x0083;
    L_0x0189:
        r2 = "MTPJNIInterface";
        r4 = "Video's bitmap is coming null";
        android.util.Log.e(r2, r4);	 Catch:{ Exception -> 0x0089, OutOfMemoryError -> 0x00de }
        goto L_0x0083;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MTPJNIInterface.getThumbnail(java.lang.String, int):int");
    }

    public int getObjectHandle(String filePath, int type) {
        Uri FileUri = Uri.parse(("content://" + "media" + "/") + "external" + "/file");
        Cursor cur = null;
        int objectHandle = 0;
        MTP_LOG_PRINT(TAG, "filePath is " + filePath);
        MTP_LOG_PRINT(TAG, "type is " + type);
        if (type == 1) {
            try {
                cur = mcontext.getContentResolver().query(FileUri, null, "_data=\"" + filePath + "\"", null, null);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            if (cur == null) {
                Log.e(TAG, "Cur is null");
                return -1;
            }
            int count = cur.getCount();
            if (count == 0) {
                MTP_LOG_PRINT(TAG, "objectHandle Count" + count);
                cur.close();
                return -1;
            }
            cur.moveToFirst();
            objectHandle = cur.getInt(cur.getColumnIndex("_id"));
            cur.close();
            MTP_LOG_PRINT(TAG, "objectHandle is " + objectHandle);
        } else if (type == 2) {
            Uri newUri = null;
            ContentValues valuesFile = new ContentValues();
            valuesFile.put("_data", filePath);
            try {
                newUri = mcontext.getContentResolver().insert(FileUri, valuesFile);
            } catch (Exception e2) {
                Log.e(TAG, e2.getMessage());
            }
            if (newUri == null) {
                Log.e(TAG, "Insertion failure");
                return -1;
            }
            Log.e(TAG, "The New URI " + newUri.toString());
            objectHandle = (int) ContentUris.parseId(newUri);
            MTP_LOG_PRINT(TAG, "objectHandle is " + objectHandle);
        }
        return objectHandle;
    }

    public String noOfSongsInPlaylist(String playlistName) {
        Cursor cur = mcontext.getContentResolver().query(MusicPlaylist.EXTERNAL_CONTENT_URI, null, "name=\"" + playlistName + "\"", null, null);
        if (cur == null) {
            Log.e(TAG, "Cur is null");
            return null;
        }
        String noOfSongs = null;
        if (cur.getCount() == 0) {
            cur.close();
            Log.e(TAG, "MTPJNIInterface , Playlist is not present in media DB");
            MTP_LOG_PRINT(TAG, "MTPJNIInterface , Playlist" + playlistName + " is not present in media DB");
            return null;
        }
        cur.moveToFirst();
        int rowID = cur.getInt(cur.getColumnIndex("_id"));
        cur.close();
        cur = mcontext.getContentResolver().query(MusicPlaylist.getMembersContentUri("external", (long) rowID), null, "playlist_id=\"" + rowID + "\"", null, null);
        int count1 = cur.getCount();
        cur.close();
        if (count1 != 0) {
            Log.e(TAG, "MTPJNIInterface , no of songs in playlist  is " + count1);
            MTP_LOG_PRINT(TAG, "MTPJNIInterface , no of songs in playlist" + playlistName + " is " + count1);
            noOfSongs = Integer.toString(count1);
        }
        return noOfSongs;
    }

    public int songPresentCheck(String filename) {
        Cursor cur = mcontext.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, "_data=\"" + filename + "\"", null, null);
        if (cur == null) {
            Log.e(TAG, "Cur is null");
            return -1;
        }
        int count = cur.getCount();
        cur.close();
        if (count > 0) {
            return 1;
        }
        return 0;
    }

    public String songsPathOfPlaylist(String playlistname) {
        int rowID = getPlaylistId(playlistname);
        Uri playlist_map = MusicPlaylist.getMembersContentUri("external", (long) rowID);
        Cursor cur = mcontext.getContentResolver().query(playlist_map, new String[]{"audio_id"}, "playlist_id=\"" + rowID + "\"", null, null);
        if (cur == null) {
            Log.e(TAG, "Cur is null");
            return null;
        }
        int i = 0;
        int count = cur.getCount();
        int[] audioId = new int[count];
        cur.moveToFirst();
        while (i < count) {
            try {
                audioId[i] = Integer.parseInt(cur.getString(cur.getColumnIndex("audio_id")));
                cur.moveToNext();
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                cur.close();
                return null;
            }
        }
        cur.close();
        Uri audioUri = Media.EXTERNAL_CONTENT_URI;
        i = 0;
        String songsPath = ProxyInfo.LOCAL_EXCL_LIST;
        while (i < count) {
            cur = mcontext.getContentResolver().query(audioUri, new String[]{"_data"}, "_id=\"" + audioId[i] + "\"", null, null);
            if (cur == null) {
                Log.e(TAG, "Cur is null but continue since checking for audio index");
                i++;
            } else {
                cur.moveToFirst();
                songsPath = songsPath.concat(cur.getString(cur.getColumnIndex("_data"))).concat("|");
                cur.close();
                i++;
            }
        }
        Log.e(TAG, "MTPJNIInterface,pipe seperated Path of playlist song ");
        MTP_LOG_PRINT(TAG, "MTPJNIInterface,pipe seperated Path of playlist song is " + songsPath);
        return songsPath;
    }

    public void renamePlaylist(String playlistOldName, String playlistNewName, String playlistNewPath) {
        File file;
        Uri playlistUri;
        ContentValues values;
        int noOfRowsUpdated;
        long dateModified = 0;
        MTP_LOG_PRINT(TAG, "playlistOldName :" + playlistOldName);
        MTP_LOG_PRINT(TAG, "playlistNewName: " + playlistNewName);
        MTP_LOG_PRINT(TAG, "playlistNewPath: " + playlistNewPath);
        String playlistPathWithoutPla = playlistNewPath.substring(0, playlistNewPath.lastIndexOf("/")) + "/" + playlistNewName;
        try {
            File file2 = new File(playlistNewPath);
            try {
                dateModified = file2.lastModified() / 1000;
                file = file2;
            } catch (Exception e) {
                file = file2;
                Log.e(TAG, "exception in getting last modified ");
                Log.e(TAG, "In updatePlaylistPath dateModified is = " + dateModified);
                playlistUri = MusicPlaylist.EXTERNAL_CONTENT_URI;
                values = new ContentValues();
                values.put("name", playlistNewName);
                values.put("_data", playlistPathWithoutPla);
                values.put("date_modified", Long.valueOf(dateModified));
                noOfRowsUpdated = mcontext.getContentResolver().update(playlistUri, values, "name=\"" + playlistOldName + "\"", null);
                Log.e(TAG, "noOfRowsUpdated = " + noOfRowsUpdated);
                if (noOfRowsUpdated == 1) {
                    Log.e(TAG, "Playlist Rename failed");
                }
                Log.e(TAG, "Playlist Renamed");
                MTP_LOG_PRINT(TAG, "playlist " + playlistOldName + " is renamed to " + playlistNewName);
                return;
            }
        } catch (Exception e2) {
            Log.e(TAG, "exception in getting last modified ");
            Log.e(TAG, "In updatePlaylistPath dateModified is = " + dateModified);
            playlistUri = MusicPlaylist.EXTERNAL_CONTENT_URI;
            values = new ContentValues();
            values.put("name", playlistNewName);
            values.put("_data", playlistPathWithoutPla);
            values.put("date_modified", Long.valueOf(dateModified));
            noOfRowsUpdated = mcontext.getContentResolver().update(playlistUri, values, "name=\"" + playlistOldName + "\"", null);
            Log.e(TAG, "noOfRowsUpdated = " + noOfRowsUpdated);
            if (noOfRowsUpdated == 1) {
                Log.e(TAG, "Playlist Renamed");
                MTP_LOG_PRINT(TAG, "playlist " + playlistOldName + " is renamed to " + playlistNewName);
                return;
            }
            Log.e(TAG, "Playlist Rename failed");
        }
        Log.e(TAG, "In updatePlaylistPath dateModified is = " + dateModified);
        playlistUri = MusicPlaylist.EXTERNAL_CONTENT_URI;
        values = new ContentValues();
        values.put("name", playlistNewName);
        values.put("_data", playlistPathWithoutPla);
        values.put("date_modified", Long.valueOf(dateModified));
        noOfRowsUpdated = mcontext.getContentResolver().update(playlistUri, values, "name=\"" + playlistOldName + "\"", null);
        Log.e(TAG, "noOfRowsUpdated = " + noOfRowsUpdated);
        if (noOfRowsUpdated == 1) {
            Log.e(TAG, "Playlist Renamed");
            MTP_LOG_PRINT(TAG, "playlist " + playlistOldName + " is renamed to " + playlistNewName);
            return;
        }
        Log.e(TAG, "Playlist Rename failed");
    }

    public void deleteAllItemofPlaylist(String playlistName) {
        MTP_LOG_PRINT(TAG, "playlistName :" + playlistName);
        Cursor cur = mcontext.getContentResolver().query(MusicPlaylist.EXTERNAL_CONTENT_URI, null, "name=\"" + playlistName + "\"", null, null);
        if (cur == null) {
            Log.e(TAG, "Cur is null");
            return;
        }
        int count = cur.getCount();
        Log.e(TAG, "Count" + count);
        if (count == 0) {
            cur.close();
            Log.e(TAG, "playlist is not present");
            MTP_LOG_PRINT(TAG, "playlist " + playlistName + " is not present");
            return;
        }
        cur.moveToFirst();
        int rowID = cur.getInt(cur.getColumnIndex("_id"));
        cur.close();
        int noOfSongs = mcontext.getContentResolver().delete(MusicPlaylist.getMembersContentUri("external", (long) rowID), "playlist_id=\"" + rowID + "\"", null);
        Log.e(TAG, "No of songs of playlistName deleted");
        MTP_LOG_PRINT(TAG, "No of songs of playlistName deleted=" + noOfSongs);
    }

    public String getPathFromObjecthandle(int objectHandle) {
        String filePath = null;
        Cursor cur = null;
        try {
            cur = mcontext.getContentResolver().query(Uri.parse(("content://" + "media" + "/") + "external" + "/file"), null, "_id=\"" + objectHandle + "\"", null, null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        if (cur == null) {
            Log.e(TAG, "Cur is null");
            return null;
        }
        int count = cur.getCount();
        if (count == 0) {
            Log.e(TAG, "objectHandle Count" + count);
        } else {
            cur.moveToFirst();
            filePath = cur.getString(cur.getColumnIndex("_data"));
            Log.e(TAG, " filePath is" + filePath);
        }
        cur.close();
        return filePath;
    }

    public Object[] GetMediaObject(String filename) {
        Uri audioURI = Media.EXTERNAL_CONTENT_URI;
        Uri videoURI = Video.Media.EXTERNAL_CONTENT_URI;
        String path = filename;
        Cursor cursor = mcontext.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, null, "_data =\"" + path + "\"", null, null);
        if (cursor == null) {
            Log.e(TAG, "cursor is null for image");
            return null;
        }
        int objectCount = cursor.getCount();
        if (objectCount == 0) {
            cursor.close();
            cursor = mcontext.getContentResolver().query(audioURI, null, "_data =\"" + path + "\"", null, null);
            if (cursor == null) {
                Log.e(TAG, "cursor is null for audio ");
                return null;
            }
            objectCount = cursor.getCount();
            if (objectCount == 0) {
                cursor.close();
                cursor = mcontext.getContentResolver().query(videoURI, null, "_data =\"" + path + "\"", null, null);
                if (cursor == null) {
                    Log.e(TAG, "cursor is null for video ");
                    return null;
                }
                objectCount = cursor.getCount();
                if (objectCount == 0) {
                    cursor.close();
                    MTP_LOG_PRINT(TAG, "objectCount is zero ");
                    return null;
                }
            }
        }
        Object[] objectArray = new MediaObject[objectCount];
        getColumnData(cursor, objectArray);
        cursor.close();
        return objectArray;
    }

    public void getColumnData(Cursor cur, MediaObject[] objectArray) {
        int mimeTypeEnum = 3;
        if (cur.moveToFirst()) {
            int titleColumn = cur.getColumnIndex("title");
            int dataColumn = cur.getColumnIndex("_data");
            int filenameColumn = cur.getColumnIndex("_display_name");
            int sizeColumn = cur.getColumnIndex("_size");
            int mimeColumn = cur.getColumnIndex("mime_type");
            int dateCreatedColumn = cur.getColumnIndex("date_added");
            int dateModifiedColumn = cur.getColumnIndex("date_modified");
            int idColumn = cur.getColumnIndex("date_modified");
            int displayColumn = cur.getColumnIndex("_display_name");
            int durationColumn_audio = cur.getColumnIndex("duration");
            int albumColumn_audio = cur.getColumnIndex("album");
            int artistColumn_audio = cur.getColumnIndex("artist");
            int composerColumn_audio = cur.getColumnIndex(AudioColumns.COMPOSER);
            int yearColumn_audio = cur.getColumnIndex("year");
            int durationColumn_video = cur.getColumnIndex("duration");
            int albumColumn_video = cur.getColumnIndex("album");
            int descriptionColumn_video = cur.getColumnIndex("description");
            int latitudeColumn_video = cur.getColumnIndex("latitude");
            int longitudeCoulmn_video = cur.getColumnIndex("longitude");
            int languageColumn_video = cur.getColumnIndex("language");
            int artistColumn_video = cur.getColumnIndex("artist");
            int dateTakenColumn_video = cur.getColumnIndex("datetaken");
            int dateTakenColumn_image = cur.getColumnIndex("datetaken");
            int resolustion_video = cur.getColumnIndex(VideoColumns.RESOLUTION);
            try {
                this.id = cur.getString(idColumn);
                this.filename = cur.getString(filenameColumn);
                this.title = cur.getString(titleColumn);
                this.path = cur.getString(dataColumn);
                this.size = cur.getString(sizeColumn);
                this.mimeType = cur.getString(mimeColumn);
                this.creationDate = cur.getString(dateCreatedColumn);
                this.modificationDate = cur.getString(dateModifiedColumn);
                if (this.mimeType.contains("image")) {
                    mimeTypeEnum = 2;
                    this.dateTaken = cur.getString(dateTakenColumn_image);
                    this.width = cur.getLong(cur.getColumnIndex("width"));
                    this.height = cur.getLong(cur.getColumnIndex("height"));
                } else if (this.mimeType.contains(Context.AUDIO_SERVICE)) {
                    mimeTypeEnum = 0;
                    this.displayname = cur.getString(displayColumn);
                    this.album = cur.getString(albumColumn_audio);
                    this.duration = cur.getLong(durationColumn_audio);
                    this.artist = cur.getString(artistColumn_audio);
                    this.composer = cur.getString(composerColumn_audio);
                    this.year = cur.getInt(yearColumn_audio);
                    this.genreName = cur.getString(cur.getColumnIndex("genre_name"));
                } else if (this.mimeType.contains("video")) {
                    mimeTypeEnum = 1;
                    this.dateTaken = cur.getString(dateTakenColumn_video);
                    this.album = cur.getString(albumColumn_video);
                    this.duration = cur.getLong(durationColumn_video);
                    this.artist = cur.getString(artistColumn_video);
                    this.description = cur.getString(descriptionColumn_video);
                    this.longitude = cur.getString(longitudeCoulmn_video);
                    this.latitude = cur.getString(latitudeColumn_video);
                    this.language = cur.getString(languageColumn_video);
                    this.width = cur.getLong(cur.getColumnIndex("width"));
                    this.height = cur.getLong(cur.getColumnIndex("height"));
                    this.resolution = cur.getString(resolustion_video);
                    if (this.resolution != null && (this.width == 0 || this.height == 0)) {
                        String[] sRes = this.resolution.split("x");
                        this.width = (long) Integer.parseInt(sRes[0]);
                        this.height = (long) Integer.parseInt(sRes[1]);
                    }
                }
                objectArray[0] = new MediaObject(this.filename, this.album, this.artist, this.composer, this.creationDate, this.description, this.duration, this.id, this.language, this.latitude, this.longitude, mimeTypeEnum, this.modificationDate, this.path, this.size, this.title, this.year, this.genreName, this.width, this.height, this.dateTaken);
                int index = 0 + 1;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private String getTimeToString() {
        Calendar cal = Calendar.getInstance();
        String year = new DecimalFormat("0000").format((long) cal.get(1));
        String month = new DecimalFormat("00").format((long) (cal.get(2) + 1));
        String day = new DecimalFormat("00").format((long) cal.get(5));
        String hour = new DecimalFormat("00").format((long) cal.get(11));
        return year + month + day + hour + new DecimalFormat("00").format((long) cal.get(12));
    }

    private int DoShellCmd(String cmd) {
        Log.i(TAG, "DoShellCmd : " + cmd);
        String[] shell_command = new String[]{"/system/bin/sh", "-c", cmd};
        try {
            Log.i(TAG, "exec command");
            Runtime.getRuntime().exec(shell_command).waitFor();
            Log.i(TAG, "exec done");
            Log.i(TAG, "DoShellCmd done");
            return 1;
        } catch (IOException e) {
            Log.e(TAG, "DoShellCmd - IOException");
            return -1;
        } catch (SecurityException e2) {
            Log.e(TAG, "DoShellCmd - SecurityException");
            return -1;
        } catch (InterruptedException e3) {
            e3.printStackTrace();
            return -1;
        }
    }

    public int sendkiesmessage(int MsgId, int MsgVal, String MsgStr) {
        Log.e(TAG, "sendkiesmessage: MsgId=>" + MsgId + "MsgVal=>" + MsgVal + " MsgStr=>" + MsgStr);
        switch (MsgId) {
            case 5:
                String logPath = "/storage/emulated/0/DeviceLogFile";
                final int logLevel = MsgVal;
                new Thread(new Runnable() {
                    public void run() {
                        Log.i(MTPJNIInterface.TAG, "run dumpstate");
                        File dataLogDirectory = new File("/storage/emulated/0/DeviceLogFile");
                        String sDate = MTPJNIInterface.this.getTimeToString();
                        if (!dataLogDirectory.exists()) {
                            dataLogDirectory.mkdir();
                        }
                        if (logLevel == 0) {
                            MTPJNIInterface.this.DoShellCmd("bugreport > /storage/emulated/0/DeviceLogFile/.bugReport_" + sDate + ".log");
                            new File("/storage/emulated/0/DeviceLogFile/.bugReport_" + sDate + ".log").renameTo(new File("/storage/emulated/0/DeviceLogFile/bugReport_" + sDate + ".log"));
                            MTPJNIInterface.this.sendObjectPropChanged("/storage/emulated/0/DeviceLogFile/bugReport_" + sDate + ".log");
                        } else if (logLevel == 1) {
                            MTPJNIInterface.this.DoShellCmd("logcat -v threadtime -d -f /storage/emulated/0/DeviceLogFile/logcat_" + sDate + ".log");
                        }
                    }
                }).start();
                break;
            case 7:
                Log.e(TAG, "send Broadcast MTP_MSGID_SIDESYNC_START");
                if (Secure.getInt(mcontext.getContentResolver(), Secure.USER_SETUP_COMPLETE, 1) != 0) {
                    String APP_STORE = "http://apps.samsung.com/appquery/appDetail.as?appId=";
                    String APP_STORE1 = "samsungapps://ProductDetail/";
                    String SIDESYNC_PACKAGENAME = "com.sec.android.sidesync30";
                    String SIDESYNC_URL = "http://apps.samsung.com/appquery/appDetail.as?appId=com.sec.android.sidesync30";
                    if (MsgVal != 0) {
                        if (MsgVal != 1) {
                            Log.i(TAG, "Not supported sidesync30 device.");
                            break;
                        }
                        Log.i(TAG, "Installed sidesync30, so start sidesync.");
                        mcontext.sendBroadcast(new Intent("com.intent.action.MTP_SIDESYNC_START"));
                        break;
                    }
                    Log.i(TAG, "Not Installed sidesync30, so start App Store.");
                    Intent activityIntent = new Intent();
                    activityIntent.setFlags(276824064);
                    activityIntent.setAction("android.intent.action.VIEW");
                    activityIntent.setData(Uri.parse(SIDESYNC_URL));
                    if (activityIntent.resolveActivity(mcontext.getPackageManager()) != null) {
                        try {
                            mcontext.startActivity(activityIntent);
                            break;
                        } catch (ActivityNotFoundException e) {
                            Log.e(TAG, "Activity not found!");
                            break;
                        }
                    }
                }
                Log.d(TAG, "setup wizard is not finished, so return.");
                return 0;
                break;
            default:
                Intent intent = new Intent("com.intent.action.KIES_MTP_MESSAGE");
                intent.putExtra("MsgId", MsgId);
                intent.putExtra("MsgVal", MsgVal);
                intent.putExtra("MsgStr", MsgStr);
                mcontext.sendBroadcast(intent);
                break;
        }
        return 1;
    }

    public static void MTP_LOG_PRINT(String tag, String msg) {
        if (isMtpLogOn) {
            Log.d(tag, msg);
        }
    }
}
