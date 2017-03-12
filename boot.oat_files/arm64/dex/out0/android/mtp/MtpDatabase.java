package android.mtp;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IContentProvider;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScanner;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Playlists;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class MtpDatabase {
    static final int[] AUDIO_PROPERTIES = new int[]{MtpConstants.PROPERTY_STORAGE_ID, MtpConstants.PROPERTY_OBJECT_FORMAT, MtpConstants.PROPERTY_PROTECTION_STATUS, MtpConstants.PROPERTY_OBJECT_SIZE, MtpConstants.PROPERTY_OBJECT_FILE_NAME, MtpConstants.PROPERTY_DATE_MODIFIED, MtpConstants.PROPERTY_PARENT_OBJECT, MtpConstants.PROPERTY_PERSISTENT_UID, MtpConstants.PROPERTY_NAME, MtpConstants.PROPERTY_DISPLAY_NAME, MtpConstants.PROPERTY_DATE_ADDED, MtpConstants.PROPERTY_ARTIST, MtpConstants.PROPERTY_ALBUM_NAME, MtpConstants.PROPERTY_ALBUM_ARTIST, MtpConstants.PROPERTY_TRACK, MtpConstants.PROPERTY_ORIGINAL_RELEASE_DATE, MtpConstants.PROPERTY_DURATION, MtpConstants.PROPERTY_GENRE, MtpConstants.PROPERTY_COMPOSER, MtpConstants.PROPERTY_AUDIO_WAVE_CODEC, MtpConstants.PROPERTY_BITRATE_TYPE, MtpConstants.PROPERTY_AUDIO_BITRATE, MtpConstants.PROPERTY_NUMBER_OF_CHANNELS, MtpConstants.PROPERTY_SAMPLE_RATE};
    private static final int DEVICE_PROPERTIES_DATABASE_VERSION = 1;
    static final int[] FILE_PROPERTIES = new int[]{MtpConstants.PROPERTY_STORAGE_ID, MtpConstants.PROPERTY_OBJECT_FORMAT, MtpConstants.PROPERTY_PROTECTION_STATUS, MtpConstants.PROPERTY_OBJECT_SIZE, MtpConstants.PROPERTY_OBJECT_FILE_NAME, MtpConstants.PROPERTY_DATE_MODIFIED, MtpConstants.PROPERTY_PARENT_OBJECT, MtpConstants.PROPERTY_PERSISTENT_UID, MtpConstants.PROPERTY_NAME, MtpConstants.PROPERTY_DISPLAY_NAME, MtpConstants.PROPERTY_DATE_ADDED};
    private static final String FORMAT_PARENT_WHERE = "format=? AND parent=?";
    private static final String[] FORMAT_PROJECTION = new String[]{"_id", FileColumns.FORMAT};
    private static final String FORMAT_WHERE = "format=?";
    private static final String[] ID_PROJECTION = new String[]{"_id"};
    private static final String ID_WHERE = "_id=?";
    static final int[] IMAGE_PROPERTIES = new int[]{MtpConstants.PROPERTY_STORAGE_ID, MtpConstants.PROPERTY_OBJECT_FORMAT, MtpConstants.PROPERTY_PROTECTION_STATUS, MtpConstants.PROPERTY_OBJECT_SIZE, MtpConstants.PROPERTY_OBJECT_FILE_NAME, MtpConstants.PROPERTY_DATE_MODIFIED, MtpConstants.PROPERTY_PARENT_OBJECT, MtpConstants.PROPERTY_PERSISTENT_UID, MtpConstants.PROPERTY_NAME, MtpConstants.PROPERTY_DISPLAY_NAME, MtpConstants.PROPERTY_DATE_ADDED, MtpConstants.PROPERTY_DESCRIPTION};
    private static final String[] OBJECT_INFO_PROJECTION = new String[]{"_id", FileColumns.STORAGE_ID, FileColumns.FORMAT, "parent", "_data", "date_added", "date_modified"};
    private static final String PARENT_WHERE = "parent=?";
    private static final String[] PATH_FORMAT_PROJECTION = new String[]{"_id", "_data", FileColumns.FORMAT};
    private static final String[] PATH_PROJECTION = new String[]{"_id", "_data"};
    private static final String PATH_WHERE = "_data=?";
    private static final String STORAGE_FORMAT_PARENT_WHERE = "storage_id=? AND format=? AND parent=?";
    private static final String STORAGE_FORMAT_WHERE = "storage_id=? AND format=?";
    private static final String STORAGE_PARENT_WHERE = "storage_id=? AND parent=?";
    private static final String STORAGE_WHERE = "storage_id=?";
    private static final String TAG = "MtpDatabase";
    static final int[] VIDEO_PROPERTIES = new int[]{MtpConstants.PROPERTY_STORAGE_ID, MtpConstants.PROPERTY_OBJECT_FORMAT, MtpConstants.PROPERTY_PROTECTION_STATUS, MtpConstants.PROPERTY_OBJECT_SIZE, MtpConstants.PROPERTY_OBJECT_FILE_NAME, MtpConstants.PROPERTY_DATE_MODIFIED, MtpConstants.PROPERTY_PARENT_OBJECT, MtpConstants.PROPERTY_PERSISTENT_UID, MtpConstants.PROPERTY_NAME, MtpConstants.PROPERTY_DISPLAY_NAME, MtpConstants.PROPERTY_DATE_ADDED, MtpConstants.PROPERTY_ARTIST, MtpConstants.PROPERTY_ALBUM_NAME, MtpConstants.PROPERTY_DURATION, MtpConstants.PROPERTY_DESCRIPTION};
    private int mBatteryLevel;
    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                MtpDatabase.this.mBatteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int newLevel = intent.getIntExtra("level", 0);
                if (newLevel != MtpDatabase.this.mBatteryLevel) {
                    MtpDatabase.this.mBatteryLevel = newLevel;
                    if (MtpDatabase.this.mServer != null) {
                        MtpDatabase.this.mServer.sendDevicePropertyChanged(MtpConstants.DEVICE_PROPERTY_BATTERY_LEVEL);
                    }
                }
            }
        }
    };
    private int mBatteryScale;
    private final Context mContext;
    private boolean mDatabaseModified;
    private SharedPreferences mDeviceProperties;
    private final IContentProvider mMediaProvider;
    private final MediaScanner mMediaScanner;
    private final String mMediaStoragePath;
    private long mNativeContext;
    private final Uri mObjectsUri;
    private final String mPackageName;
    private final HashMap<Integer, MtpPropertyGroup> mPropertyGroupsByFormat = new HashMap();
    private final HashMap<Integer, MtpPropertyGroup> mPropertyGroupsByProperty = new HashMap();
    private MtpServer mServer;
    private final HashMap<String, MtpStorage> mStorageMap = new HashMap();
    private final String[] mSubDirectories;
    private String mSubDirectoriesWhere;
    private String[] mSubDirectoriesWhereArgs;
    private final String mVolumeName;

    private int getObjectFilePath(int r13, char[] r14, long[] r15) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(Unknown Source)
	at java.util.HashMap$KeyIterator.next(Unknown Source)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:80)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r12 = this;
        r11 = 8193; // 0x2001 float:1.1481E-41 double:4.048E-320;
        r3 = 1;
        r2 = 0;
        if (r13 != 0) goto L_0x0023;
    L_0x0006:
        r0 = r12.mMediaStoragePath;
        r1 = r12.mMediaStoragePath;
        r1 = r1.length();
        r0.getChars(r2, r1, r14, r2);
        r0 = r12.mMediaStoragePath;
        r0 = r0.length();
        r14[r0] = r2;
        r0 = 0;
        r15[r2] = r0;
        r0 = 12289; // 0x3001 float:1.722E-41 double:6.0716E-320;
        r15[r3] = r0;
        r0 = r11;
    L_0x0022:
        return r0;
    L_0x0023:
        r8 = 0;
        r0 = r12.mMediaProvider;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r1 = r12.mPackageName;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r2 = r12.mObjectsUri;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r3 = PATH_FORMAT_PROJECTION;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r4 = "_id=?";	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r5 = 1;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r5 = new java.lang.String[r5];	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r6 = 0;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r7 = java.lang.Integer.toString(r13);	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r5[r6] = r7;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r6 = 0;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r7 = 0;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        if (r8 == 0) goto L_0x0076;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
    L_0x0040:
        r0 = r8.moveToNext();	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        if (r0 == 0) goto L_0x0076;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
    L_0x0046:
        r0 = 1;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r10 = r8.getString(r0);	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r0 = 0;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r1 = r10.length();	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r2 = 0;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r10.getChars(r0, r1, r14, r2);	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r0 = r10.length();	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r1 = 0;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r14[r0] = r1;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r0 = 0;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r1 = new java.io.File;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r1.<init>(r10);	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r2 = r1.length();	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r15[r0] = r2;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r0 = 1;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r1 = 2;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r2 = r8.getLong(r1);	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r15[r0] = r2;	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        if (r8 == 0) goto L_0x0074;
    L_0x0071:
        r8.close();
    L_0x0074:
        r0 = r11;
        goto L_0x0022;
    L_0x0076:
        r0 = 8201; // 0x2009 float:1.1492E-41 double:4.052E-320;
        if (r8 == 0) goto L_0x0022;
    L_0x007a:
        r8.close();
        goto L_0x0022;
    L_0x007e:
        r9 = move-exception;
        r0 = "MtpDatabase";	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r1 = "RemoteException in getObjectFilePath";	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        android.util.Log.e(r0, r1, r9);	 Catch:{ RemoteException -> 0x007e, all -> 0x008e }
        r0 = 8194; // 0x2002 float:1.1482E-41 double:4.0484E-320;
        if (r8 == 0) goto L_0x0022;
    L_0x008a:
        r8.close();
        goto L_0x0022;
    L_0x008e:
        r0 = move-exception;
        if (r8 == 0) goto L_0x0094;
    L_0x0091:
        r8.close();
    L_0x0094:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpDatabase.getObjectFilePath(int, char[], long[]):int");
    }

    private int getObjectFormat(int r12) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x002e in list [B:9:0x002b]
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
        r11 = this;
        r10 = -1;
        r8 = 0;
        r0 = r11.mMediaProvider;	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r1 = r11.mPackageName;	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r2 = r11.mObjectsUri;	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r3 = FORMAT_PROJECTION;	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r4 = "_id=?";	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r5 = 1;	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r5 = new java.lang.String[r5];	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r6 = 0;	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r7 = java.lang.Integer.toString(r12);	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r5[r6] = r7;	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r6 = 0;	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r7 = 0;	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        if (r8 == 0) goto L_0x002f;	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
    L_0x001e:
        r0 = r8.moveToNext();	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        if (r0 == 0) goto L_0x002f;	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
    L_0x0024:
        r0 = 1;	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r0 = r8.getInt(r0);	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        if (r8 == 0) goto L_0x002e;
    L_0x002b:
        r8.close();
    L_0x002e:
        return r0;
    L_0x002f:
        if (r8 == 0) goto L_0x0034;
    L_0x0031:
        r8.close();
    L_0x0034:
        r0 = r10;
        goto L_0x002e;
    L_0x0036:
        r9 = move-exception;
        r0 = "MtpDatabase";	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        r1 = "RemoteException in getObjectFilePath";	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        android.util.Log.e(r0, r1, r9);	 Catch:{ RemoteException -> 0x0036, all -> 0x0045 }
        if (r8 == 0) goto L_0x0043;
    L_0x0040:
        r8.close();
    L_0x0043:
        r0 = r10;
        goto L_0x002e;
    L_0x0045:
        r0 = move-exception;
        if (r8 == 0) goto L_0x004b;
    L_0x0048:
        r8.close();
    L_0x004b:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpDatabase.getObjectFormat(int):int");
    }

    private final native void native_finalize();

    private final native void native_setup();

    private int renameFile(int r19, java.lang.String r20) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(Unknown Source)
	at java.util.HashMap$KeyIterator.next(Unknown Source)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:80)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r18 = this;
        r9 = 0;
        r15 = 0;
        r1 = 1;
        r6 = new java.lang.String[r1];
        r1 = 0;
        r2 = java.lang.Integer.toString(r19);
        r6[r1] = r2;
        r0 = r18;	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r1 = r0.mMediaProvider;	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r0 = r18;	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r2 = r0.mPackageName;	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r0 = r18;	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r3 = r0.mObjectsUri;	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r4 = PATH_PROJECTION;	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r5 = "_id=?";	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r7 = 0;	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r8 = 0;	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r9 = r1.query(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        if (r9 == 0) goto L_0x002f;	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
    L_0x0024:
        r1 = r9.moveToNext();	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        if (r1 == 0) goto L_0x002f;	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
    L_0x002a:
        r1 = 1;	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r15 = r9.getString(r1);	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
    L_0x002f:
        if (r9 == 0) goto L_0x0034;
    L_0x0031:
        r9.close();
    L_0x0034:
        if (r15 != 0) goto L_0x0050;
    L_0x0036:
        r1 = 8201; // 0x2009 float:1.1492E-41 double:4.052E-320;
    L_0x0038:
        return r1;
    L_0x0039:
        r10 = move-exception;
        r1 = "MtpDatabase";	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r2 = "RemoteException in getObjectFilePath";	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        android.util.Log.e(r1, r2, r10);	 Catch:{ RemoteException -> 0x0039, all -> 0x0049 }
        r1 = 8194; // 0x2002 float:1.1482E-41 double:4.0484E-320;
        if (r9 == 0) goto L_0x0038;
    L_0x0045:
        r9.close();
        goto L_0x0038;
    L_0x0049:
        r1 = move-exception;
        if (r9 == 0) goto L_0x004f;
    L_0x004c:
        r9.close();
    L_0x004f:
        throw r1;
    L_0x0050:
        r0 = r18;
        r1 = r0.isStorageSubDirectory(r15);
        if (r1 == 0) goto L_0x005b;
    L_0x0058:
        r1 = 8205; // 0x200d float:1.1498E-41 double:4.054E-320;
        goto L_0x0038;
    L_0x005b:
        r14 = new java.io.File;
        r14.<init>(r15);
        r1 = 47;
        r11 = r15.lastIndexOf(r1);
        r1 = 1;
        if (r11 > r1) goto L_0x006c;
    L_0x0069:
        r1 = 8194; // 0x2002 float:1.1482E-41 double:4.0484E-320;
        goto L_0x0038;
    L_0x006c:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = 0;
        r3 = r11 + 1;
        r2 = r15.substring(r2, r3);
        r1 = r1.append(r2);
        r0 = r20;
        r1 = r1.append(r0);
        r13 = r1.toString();
        r12 = new java.io.File;
        r12.<init>(r13);
        r16 = r14.renameTo(r12);
        if (r16 != 0) goto L_0x00be;
    L_0x0091:
        r1 = "MtpDatabase";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "renaming ";
        r2 = r2.append(r3);
        r2 = r2.append(r15);
        r3 = " to ";
        r2 = r2.append(r3);
        r2 = r2.append(r13);
        r3 = " failed";
        r2 = r2.append(r3);
        r2 = r2.toString();
        android.util.Log.w(r1, r2);
        r1 = 8194; // 0x2002 float:1.1482E-41 double:4.0484E-320;
        goto L_0x0038;
    L_0x00be:
        r4 = new android.content.ContentValues;
        r4.<init>();
        r1 = "_data";
        r4.put(r1, r13);
        r17 = 0;
        r0 = r18;	 Catch:{ RemoteException -> 0x0107 }
        r1 = r0.mMediaProvider;	 Catch:{ RemoteException -> 0x0107 }
        r0 = r18;	 Catch:{ RemoteException -> 0x0107 }
        r2 = r0.mPackageName;	 Catch:{ RemoteException -> 0x0107 }
        r0 = r18;	 Catch:{ RemoteException -> 0x0107 }
        r3 = r0.mObjectsUri;	 Catch:{ RemoteException -> 0x0107 }
        r5 = "_id=?";	 Catch:{ RemoteException -> 0x0107 }
        r17 = r1.update(r2, r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x0107 }
    L_0x00dc:
        if (r17 != 0) goto L_0x0110;
    L_0x00de:
        r1 = "MtpDatabase";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Unable to update path for ";
        r2 = r2.append(r3);
        r2 = r2.append(r15);
        r3 = " to ";
        r2 = r2.append(r3);
        r2 = r2.append(r13);
        r2 = r2.toString();
        android.util.Log.e(r1, r2);
        r12.renameTo(r14);
        r1 = 8194; // 0x2002 float:1.1482E-41 double:4.0484E-320;
        goto L_0x0038;
    L_0x0107:
        r10 = move-exception;
        r1 = "MtpDatabase";
        r2 = "RemoteException in mMediaProvider.update";
        android.util.Log.e(r1, r2, r10);
        goto L_0x00dc;
    L_0x0110:
        r1 = r12.isDirectory();
        if (r1 == 0) goto L_0x0157;
    L_0x0116:
        r1 = r14.getName();
        r2 = ".";
        r1 = r1.startsWith(r2);
        if (r1 == 0) goto L_0x0139;
    L_0x0122:
        r1 = ".";
        r1 = r13.startsWith(r1);
        if (r1 != 0) goto L_0x0139;
    L_0x012a:
        r0 = r18;	 Catch:{ RemoteException -> 0x013d }
        r1 = r0.mMediaProvider;	 Catch:{ RemoteException -> 0x013d }
        r0 = r18;	 Catch:{ RemoteException -> 0x013d }
        r2 = r0.mPackageName;	 Catch:{ RemoteException -> 0x013d }
        r3 = "unhide";	 Catch:{ RemoteException -> 0x013d }
        r5 = 0;	 Catch:{ RemoteException -> 0x013d }
        r1.call(r2, r3, r13, r5);	 Catch:{ RemoteException -> 0x013d }
    L_0x0139:
        r1 = 8193; // 0x2001 float:1.1481E-41 double:4.048E-320;
        goto L_0x0038;
    L_0x013d:
        r10 = move-exception;
        r1 = "MtpDatabase";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "failed to unhide/rescan for ";
        r2 = r2.append(r3);
        r2 = r2.append(r13);
        r2 = r2.toString();
        android.util.Log.e(r1, r2);
        goto L_0x0139;
    L_0x0157:
        r1 = r14.getName();
        r2 = java.util.Locale.US;
        r1 = r1.toLowerCase(r2);
        r2 = ".nomedia";
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x0139;
    L_0x0169:
        r1 = java.util.Locale.US;
        r1 = r13.toLowerCase(r1);
        r2 = ".nomedia";
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x0139;
    L_0x0177:
        r0 = r18;	 Catch:{ RemoteException -> 0x018b }
        r1 = r0.mMediaProvider;	 Catch:{ RemoteException -> 0x018b }
        r0 = r18;	 Catch:{ RemoteException -> 0x018b }
        r2 = r0.mPackageName;	 Catch:{ RemoteException -> 0x018b }
        r3 = "unhide";	 Catch:{ RemoteException -> 0x018b }
        r5 = r14.getParent();	 Catch:{ RemoteException -> 0x018b }
        r7 = 0;	 Catch:{ RemoteException -> 0x018b }
        r1.call(r2, r3, r5, r7);	 Catch:{ RemoteException -> 0x018b }
        goto L_0x0139;
    L_0x018b:
        r10 = move-exception;
        r1 = "MtpDatabase";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "failed to unhide/rescan for ";
        r2 = r2.append(r3);
        r2 = r2.append(r13);
        r2 = r2.toString();
        android.util.Log.e(r1, r2);
        goto L_0x0139;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpDatabase.renameFile(int, java.lang.String):int");
    }

    static {
        System.loadLibrary("media_jni");
    }

    public MtpDatabase(Context context, String volumeName, String storagePath, String[] subDirectories) {
        native_setup();
        this.mContext = context;
        this.mPackageName = context.getPackageName();
        this.mMediaProvider = context.getContentResolver().acquireProvider("media");
        this.mVolumeName = volumeName;
        this.mMediaStoragePath = storagePath;
        this.mObjectsUri = Files.getMtpObjectsUri(volumeName);
        this.mMediaScanner = new MediaScanner(context);
        this.mSubDirectories = subDirectories;
        if (subDirectories != null) {
            int i;
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            for (i = 0; i < count; i++) {
                builder.append("_data=? OR _data LIKE ?");
                if (i != count - 1) {
                    builder.append(" OR ");
                }
            }
            builder.append(")");
            this.mSubDirectoriesWhere = builder.toString();
            this.mSubDirectoriesWhereArgs = new String[(count * 2)];
            int j = 0;
            for (String path : subDirectories) {
                int i2 = j + 1;
                this.mSubDirectoriesWhereArgs[j] = path;
                j = i2 + 1;
                this.mSubDirectoriesWhereArgs[i2] = path + "/%";
            }
        }
        Locale locale = context.getResources().getConfiguration().locale;
        if (locale != null) {
            String language = locale.getLanguage();
            String country = locale.getCountry();
            if (language != null) {
                if (country != null) {
                    this.mMediaScanner.setLocale(language + "_" + country);
                } else {
                    this.mMediaScanner.setLocale(language);
                }
            }
        }
        initDeviceProperties(context);
    }

    public void setServer(MtpServer server) {
        this.mServer = server;
        try {
            this.mContext.unregisterReceiver(this.mBatteryReceiver);
        } catch (IllegalArgumentException e) {
        }
        if (server != null) {
            this.mContext.registerReceiver(this.mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }
    }

    protected void finalize() throws Throwable {
        try {
            native_finalize();
        } finally {
            super.finalize();
        }
    }

    public void addStorage(MtpStorage storage) {
        this.mStorageMap.put(storage.getPath(), storage);
    }

    public void removeStorage(MtpStorage storage) {
        this.mStorageMap.remove(storage.getPath());
    }

    private void initDeviceProperties(Context context) {
        String devicePropertiesName = "device-properties";
        this.mDeviceProperties = context.getSharedPreferences("device-properties", 0);
        if (context.getDatabasePath("device-properties").exists()) {
            SQLiteDatabase db = null;
            Cursor c = null;
            Editor e;
            try {
                db = context.openOrCreateDatabase("device-properties", 0, null);
                if (db != null) {
                    c = db.query("properties", new String[]{"_id", "code", "value"}, null, null, null, null, null);
                    if (c != null) {
                        e = this.mDeviceProperties.edit();
                        while (c.moveToNext()) {
                            e.putString(c.getString(1), c.getString(2));
                        }
                        e.commit();
                    }
                }
                if (c != null) {
                    c.close();
                }
                if (db != null) {
                    db.close();
                }
            } catch (Editor e2) {
                Log.e(TAG, "failed to migrate device properties", e2);
                context.deleteDatabase("device-properties");
            } finally {
                if (c != null) {
                    c.close();
                }
                if (db != null) {
                    db.close();
                }
            }
            context.deleteDatabase("device-properties");
        }
    }

    private boolean inStorageSubDirectory(String path) {
        if (this.mSubDirectories == null) {
            return true;
        }
        if (path == null) {
            return false;
        }
        boolean allowed = false;
        int pathLength = path.length();
        for (int i = 0; i < this.mSubDirectories.length && !allowed; i++) {
            String subdir = this.mSubDirectories[i];
            int subdirLength = subdir.length();
            if (subdirLength < pathLength && path.charAt(subdirLength) == '/' && path.startsWith(subdir)) {
                allowed = true;
            }
        }
        return allowed;
    }

    private boolean isStorageSubDirectory(String path) {
        if (this.mSubDirectories == null) {
            return false;
        }
        for (Object equals : this.mSubDirectories) {
            if (path.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    private boolean inStorageRoot(String path) {
        try {
            String canonical = new File(path).getCanonicalPath();
            for (String root : this.mStorageMap.keySet()) {
                if (canonical.startsWith(root)) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    private int beginSendObject(String path, int format, int parent, int storageId, long size, long modified) {
        if (!inStorageRoot(path)) {
            Log.e(TAG, "attempt to put file outside of storage area: " + path);
            return -1;
        } else if (!inStorageSubDirectory(path)) {
            return -1;
        } else {
            if (path != null) {
                Cursor c = null;
                try {
                    c = this.mMediaProvider.query(this.mPackageName, this.mObjectsUri, ID_PROJECTION, PATH_WHERE, new String[]{path}, null, null);
                    if (c != null && c.getCount() > 0) {
                        Log.w(TAG, "file already exists in beginSendObject: " + path);
                        if (c == null) {
                            return -1;
                        }
                        c.close();
                        return -1;
                    } else if (c != null) {
                        c.close();
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in beginSendObject", e);
                    if (c != null) {
                        c.close();
                    }
                } catch (Throwable th) {
                    if (c != null) {
                        c.close();
                    }
                }
            }
            this.mDatabaseModified = true;
            ContentValues values = new ContentValues();
            values.put("_data", path);
            values.put(FileColumns.FORMAT, Integer.valueOf(format));
            values.put("parent", Integer.valueOf(parent));
            values.put(FileColumns.STORAGE_ID, Integer.valueOf(storageId));
            values.put("_size", Long.valueOf(size));
            values.put("date_modified", Long.valueOf(modified));
            try {
                Uri uri = this.mMediaProvider.insert(this.mPackageName, this.mObjectsUri, values);
                if (uri != null) {
                    return Integer.parseInt((String) uri.getPathSegments().get(2));
                }
                return -1;
            } catch (RemoteException e2) {
                Log.e(TAG, "RemoteException in beginSendObject", e2);
                return -1;
            }
        }
    }

    private void endSendObject(String path, int handle, int format, boolean succeeded) {
        if (!succeeded) {
            deleteFile(handle);
        } else if (format == MtpConstants.FORMAT_ABSTRACT_AV_PLAYLIST) {
            String name = path;
            int lastSlash = name.lastIndexOf(47);
            if (lastSlash >= 0) {
                name = name.substring(lastSlash + 1);
            }
            if (name.endsWith(".pla")) {
                name = name.substring(0, name.length() - 4);
            }
            ContentValues values = new ContentValues(1);
            values.put("_data", path);
            values.put("name", name);
            values.put(FileColumns.FORMAT, Integer.valueOf(format));
            values.put("date_modified", Long.valueOf(System.currentTimeMillis() / 1000));
            values.put(MediaColumns.MEDIA_SCANNER_NEW_OBJECT_ID, Integer.valueOf(handle));
            try {
                this.mMediaProvider.insert(this.mPackageName, Playlists.EXTERNAL_CONTENT_URI, values);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in endSendObject", e);
            }
        } else {
            this.mMediaScanner.scanMtpFile(path, this.mVolumeName, handle, format);
        }
    }

    private Cursor createObjectQuery(int storageID, int format, int parent) throws RemoteException {
        String where;
        String[] whereArgs;
        if (storageID == -1) {
            if (format == 0) {
                if (parent == 0) {
                    where = null;
                    whereArgs = null;
                } else {
                    if (parent == -1) {
                        parent = 0;
                    }
                    where = PARENT_WHERE;
                    whereArgs = new String[]{Integer.toString(parent)};
                }
            } else if (parent == 0) {
                where = FORMAT_WHERE;
                whereArgs = new String[]{Integer.toString(format)};
            } else {
                if (parent == -1) {
                    parent = 0;
                }
                where = FORMAT_PARENT_WHERE;
                whereArgs = new String[]{Integer.toString(format), Integer.toString(parent)};
            }
        } else if (format == 0) {
            if (parent == 0) {
                where = STORAGE_WHERE;
                whereArgs = new String[]{Integer.toString(storageID)};
            } else {
                if (parent == -1) {
                    parent = 0;
                }
                where = STORAGE_PARENT_WHERE;
                whereArgs = new String[]{Integer.toString(storageID), Integer.toString(parent)};
            }
        } else if (parent == 0) {
            where = STORAGE_FORMAT_WHERE;
            whereArgs = new String[]{Integer.toString(storageID), Integer.toString(format)};
        } else {
            if (parent == -1) {
                parent = 0;
            }
            where = STORAGE_FORMAT_PARENT_WHERE;
            whereArgs = new String[]{Integer.toString(storageID), Integer.toString(format), Integer.toString(parent)};
        }
        if (this.mSubDirectoriesWhere != null) {
            if (where == null) {
                where = this.mSubDirectoriesWhere;
                whereArgs = this.mSubDirectoriesWhereArgs;
            } else {
                where = where + " AND " + this.mSubDirectoriesWhere;
                String[] newWhereArgs = new String[(whereArgs.length + this.mSubDirectoriesWhereArgs.length)];
                int i = 0;
                while (i < whereArgs.length) {
                    newWhereArgs[i] = whereArgs[i];
                    i++;
                }
                for (String str : this.mSubDirectoriesWhereArgs) {
                    newWhereArgs[i] = str;
                    i++;
                }
                whereArgs = newWhereArgs;
            }
        }
        return this.mMediaProvider.query(this.mPackageName, this.mObjectsUri, ID_PROJECTION, where, whereArgs, null, null);
    }

    private int[] getObjectList(int storageID, int format, int parent) {
        Cursor c = null;
        try {
            c = createObjectQuery(storageID, format, parent);
            if (c == null) {
                if (c != null) {
                    c.close();
                }
                return null;
            }
            int count = c.getCount();
            if (count > 0) {
                int[] result = new int[count];
                for (int i = 0; i < count; i++) {
                    c.moveToNext();
                    result[i] = c.getInt(0);
                }
                if (c == null) {
                    return result;
                }
                c.close();
                return result;
            }
            if (c != null) {
                c.close();
            }
            return null;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getObjectList", e);
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
    }

    private int getNumObjects(int storageID, int format, int parent) {
        Cursor c = null;
        try {
            c = createObjectQuery(storageID, format, parent);
            if (c != null) {
                int count = c.getCount();
                if (c == null) {
                    return count;
                }
                c.close();
                return count;
            }
            if (c != null) {
                c.close();
            }
            return -1;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getNumObjects", e);
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
    }

    private int[] getSupportedPlaybackFormats() {
        return new int[]{12288, 12289, 12292, 12293, 12296, 12297, 12299, MtpConstants.FORMAT_EXIF_JPEG, MtpConstants.FORMAT_TIFF_EP, MtpConstants.FORMAT_BMP, MtpConstants.FORMAT_GIF, MtpConstants.FORMAT_JFIF, MtpConstants.FORMAT_PNG, MtpConstants.FORMAT_TIFF, MtpConstants.FORMAT_WMA, MtpConstants.FORMAT_OGG, MtpConstants.FORMAT_AAC, MtpConstants.FORMAT_MP4_CONTAINER, MtpConstants.FORMAT_MP2, MtpConstants.FORMAT_3GP_CONTAINER, MtpConstants.FORMAT_ABSTRACT_AV_PLAYLIST, MtpConstants.FORMAT_WPL_PLAYLIST, MtpConstants.FORMAT_M3U_PLAYLIST, MtpConstants.FORMAT_PLS_PLAYLIST, MtpConstants.FORMAT_XML_DOCUMENT, MtpConstants.FORMAT_FLAC};
    }

    private int[] getSupportedCaptureFormats() {
        return null;
    }

    private int[] getSupportedObjectProperties(int format) {
        switch (format) {
            case 12296:
            case 12297:
            case MtpConstants.FORMAT_WMA /*47361*/:
            case MtpConstants.FORMAT_OGG /*47362*/:
            case MtpConstants.FORMAT_AAC /*47363*/:
                return AUDIO_PROPERTIES;
            case 12299:
            case MtpConstants.FORMAT_WMV /*47489*/:
            case MtpConstants.FORMAT_3GP_CONTAINER /*47492*/:
                return VIDEO_PROPERTIES;
            case MtpConstants.FORMAT_EXIF_JPEG /*14337*/:
            case MtpConstants.FORMAT_BMP /*14340*/:
            case MtpConstants.FORMAT_GIF /*14343*/:
            case MtpConstants.FORMAT_PNG /*14347*/:
                return IMAGE_PROPERTIES;
            default:
                return FILE_PROPERTIES;
        }
    }

    private int[] getSupportedDeviceProperties() {
        return new int[]{MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER, MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME, MtpConstants.DEVICE_PROPERTY_IMAGE_SIZE, MtpConstants.DEVICE_PROPERTY_BATTERY_LEVEL};
    }

    private MtpPropertyList getObjectPropertyList(long handle, int format, long property, int groupCode, int depth) {
        if (groupCode != 0) {
            return new MtpPropertyList(0, MtpConstants.RESPONSE_SPECIFICATION_BY_GROUP_UNSUPPORTED);
        }
        MtpPropertyGroup propertyGroup;
        if (property == 4294967295L) {
            if (format == 0 && handle > 0) {
                format = getObjectFormat((int) handle);
            }
            propertyGroup = (MtpPropertyGroup) this.mPropertyGroupsByFormat.get(Integer.valueOf(format));
            if (propertyGroup == null) {
                propertyGroup = new MtpPropertyGroup(this, this.mMediaProvider, this.mPackageName, this.mVolumeName, getSupportedObjectProperties(format));
                this.mPropertyGroupsByFormat.put(new Integer(format), propertyGroup);
            }
        } else {
            propertyGroup = (MtpPropertyGroup) this.mPropertyGroupsByProperty.get(Long.valueOf(property));
            if (propertyGroup == null) {
                propertyGroup = new MtpPropertyGroup(this, this.mMediaProvider, this.mPackageName, this.mVolumeName, new int[]{(int) property});
                this.mPropertyGroupsByProperty.put(new Integer((int) property), propertyGroup);
            }
        }
        return propertyGroup.getPropertyList((int) handle, format, depth);
    }

    private int setObjectProperty(int handle, int property, long intValue, String stringValue) {
        switch (property) {
            case MtpConstants.PROPERTY_OBJECT_FILE_NAME /*56327*/:
                return renameFile(handle, stringValue);
            default:
                return MtpConstants.RESPONSE_OBJECT_PROP_NOT_SUPPORTED;
        }
    }

    private int getDeviceProperty(int property, long[] outIntValue, char[] outStringValue) {
        switch (property) {
            case MtpConstants.DEVICE_PROPERTY_IMAGE_SIZE /*20483*/:
                Display display = ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                String imageSize = Integer.toString(display.getMaximumSizeDimension()) + "x" + Integer.toString(display.getMaximumSizeDimension());
                imageSize.getChars(0, imageSize.length(), outStringValue, 0);
                outStringValue[imageSize.length()] = '\u0000';
                return MtpConstants.RESPONSE_OK;
            case MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER /*54273*/:
            case MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME /*54274*/:
                String value = this.mDeviceProperties.getString(Integer.toString(property), ProxyInfo.LOCAL_EXCL_LIST);
                int length = value.length();
                if (length > 255) {
                    length = 255;
                }
                value.getChars(0, length, outStringValue, 0);
                outStringValue[length] = '\u0000';
                return MtpConstants.RESPONSE_OK;
            default:
                return MtpConstants.RESPONSE_DEVICE_PROP_NOT_SUPPORTED;
        }
    }

    private int setDeviceProperty(int property, long intValue, String stringValue) {
        switch (property) {
            case MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER /*54273*/:
            case MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME /*54274*/:
                Editor e = this.mDeviceProperties.edit();
                e.putString(Integer.toString(property), stringValue);
                return e.commit() ? MtpConstants.RESPONSE_OK : 8194;
            default:
                return MtpConstants.RESPONSE_DEVICE_PROP_NOT_SUPPORTED;
        }
    }

    private boolean getObjectInfo(int handle, int[] outStorageFormatParent, char[] outName, long[] outCreatedModified) {
        Cursor c = null;
        try {
            c = this.mMediaProvider.query(this.mPackageName, this.mObjectsUri, OBJECT_INFO_PROJECTION, ID_WHERE, new String[]{Integer.toString(handle)}, null, null);
            if (c == null || !c.moveToNext()) {
                if (c != null) {
                    c.close();
                }
                return false;
            }
            outStorageFormatParent[0] = c.getInt(1);
            outStorageFormatParent[1] = c.getInt(2);
            outStorageFormatParent[2] = c.getInt(3);
            String path = c.getString(4);
            int lastSlash = path.lastIndexOf(47);
            int start = lastSlash >= 0 ? lastSlash + 1 : 0;
            int end = path.length();
            if (end - start > 255) {
                end = start + 255;
            }
            path.getChars(start, end, outName, 0);
            outName[end - start] = '\u0000';
            outCreatedModified[0] = c.getLong(5);
            outCreatedModified[1] = c.getLong(6);
            if (outCreatedModified[0] == 0) {
                outCreatedModified[0] = outCreatedModified[1];
            }
            if (c == null) {
                return true;
            }
            c.close();
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getObjectInfo", e);
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
    }

    private int deleteFile(int handle) {
        this.mDatabaseModified = true;
        Cursor c = null;
        int i;
        try {
            c = this.mMediaProvider.query(this.mPackageName, this.mObjectsUri, PATH_FORMAT_PROJECTION, ID_WHERE, new String[]{Integer.toString(handle)}, null, null);
            if (c == null || !c.moveToNext()) {
                i = MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;
                if (c != null) {
                    c.close();
                }
                return i;
            }
            String path = c.getString(1);
            int format = c.getInt(2);
            if (path == null || format == 0) {
                i = 8194;
                if (c != null) {
                    c.close();
                }
                return i;
            }
            if (isStorageSubDirectory(path)) {
                i = MtpConstants.RESPONSE_OBJECT_WRITE_PROTECTED;
                if (c != null) {
                    c.close();
                }
            } else {
                if (format == 12289) {
                    this.mMediaProvider.delete(this.mPackageName, Files.getMtpObjectsUri(this.mVolumeName), "_data LIKE ?1 AND lower(substr(_data,1,?2))=lower(?3)", new String[]{path + "/%", Integer.toString(path.length() + 1), path + "/"});
                }
                if (this.mMediaProvider.delete(this.mPackageName, Files.getMtpObjectsUri(this.mVolumeName, (long) handle), null, null) > 0) {
                    if (format != 12289 && path.toLowerCase(Locale.US).endsWith("/.nomedia")) {
                        try {
                            this.mMediaProvider.call(this.mPackageName, MediaStore.UNHIDE_CALL, path.substring(0, path.lastIndexOf("/")), null);
                        } catch (RemoteException e) {
                            Log.e(TAG, "failed to unhide/rescan for " + path);
                        }
                    }
                    i = MtpConstants.RESPONSE_OK;
                    if (c != null) {
                        c.close();
                    }
                } else {
                    i = MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;
                    if (c != null) {
                        c.close();
                    }
                }
            }
            return i;
        } catch (RemoteException e2) {
            Log.e(TAG, "RemoteException in deleteFile", e2);
            i = 8194;
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
    }

    private int[] getObjectReferences(int handle) {
        Cursor c = null;
        try {
            c = this.mMediaProvider.query(this.mPackageName, Files.getMtpReferencesUri(this.mVolumeName, (long) handle), ID_PROJECTION, null, null, null, null);
            if (c == null) {
                if (c != null) {
                    c.close();
                }
                return null;
            }
            int count = c.getCount();
            if (count > 0) {
                int[] result = new int[count];
                for (int i = 0; i < count; i++) {
                    c.moveToNext();
                    result[i] = c.getInt(0);
                }
                if (c == null) {
                    return result;
                }
                c.close();
                return result;
            }
            if (c != null) {
                c.close();
            }
            return null;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getObjectList", e);
            if (c != null) {
                c.close();
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
    }

    private int setObjectReferences(int handle, int[] references) {
        this.mDatabaseModified = true;
        Uri uri = Files.getMtpReferencesUri(this.mVolumeName, (long) handle);
        int count = references.length;
        ContentValues[] valuesList = new ContentValues[count];
        for (int i = 0; i < count; i++) {
            ContentValues values = new ContentValues();
            values.put("_id", Integer.valueOf(references[i]));
            valuesList[i] = values;
        }
        try {
            if (this.mMediaProvider.bulkInsert(this.mPackageName, uri, valuesList) > 0) {
                return MtpConstants.RESPONSE_OK;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in setObjectReferences", e);
        }
        return 8194;
    }

    private void sessionStarted() {
        this.mDatabaseModified = false;
    }

    private void sessionEnded() {
        if (this.mDatabaseModified) {
            this.mContext.sendBroadcast(new Intent(MediaStore.ACTION_MTP_SESSION_END));
            this.mDatabaseModified = false;
        }
    }
}
