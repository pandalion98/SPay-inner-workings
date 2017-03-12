package android.media;

import android.Manifest.permission;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Process;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Media;
import android.provider.Settings.System;
import android.util.Log;
import android.util.Slog;
import com.android.internal.database.SortCursor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RingtoneManager {
    public static final String ACTION_RINGTONE_PICKER = "android.intent.action.RINGTONE_PICKER";
    protected static String EXTERNAL_PATH = null;
    public static final String EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS = "android.intent.extra.ringtone.AUDIO_ATTRIBUTES_FLAGS";
    public static final String EXTRA_RINGTONE_DEFAULT_URI = "android.intent.extra.ringtone.DEFAULT_URI";
    public static final String EXTRA_RINGTONE_EXISTING_URI = "android.intent.extra.ringtone.EXISTING_URI";
    @Deprecated
    public static final String EXTRA_RINGTONE_INCLUDE_DRM = "android.intent.extra.ringtone.INCLUDE_DRM";
    public static final String EXTRA_RINGTONE_PICKED_URI = "android.intent.extra.ringtone.PICKED_URI";
    public static final String EXTRA_RINGTONE_SHOW_DEFAULT = "android.intent.extra.ringtone.SHOW_DEFAULT";
    public static final String EXTRA_RINGTONE_SHOW_SILENT = "android.intent.extra.ringtone.SHOW_SILENT";
    public static final String EXTRA_RINGTONE_TITLE = "android.intent.extra.ringtone.TITLE";
    public static final String EXTRA_RINGTONE_TYPE = "android.intent.extra.ringtone.TYPE";
    public static final int ID_COLUMN_INDEX = 0;
    private static final String[] INTERNAL_COLUMNS = new String[]{"_id", "title", "\"" + Media.INTERNAL_CONTENT_URI + "\"", "title_key"};
    private static final String[] MEDIA_COLUMNS = new String[]{"_id", "title", "\"" + Media.EXTERNAL_CONTENT_URI + "\"", "title_key"};
    private static final String TAG = "RingtoneManager";
    public static final int TITLE_COLUMN_INDEX = 1;
    public static final int TYPE_ALARM = 4;
    public static final int TYPE_ALL = 7;
    public static final int TYPE_NOTIFICATION = 2;
    public static final int TYPE_NOTIFICATION_2 = 256;
    public static final int TYPE_RINGFORME = 32;
    public static final int TYPE_RINGTONE = 1;
    public static final int TYPE_RINGTONE_2 = 128;
    public static final int URI_COLUMN_INDEX = 2;
    private static HashMap<String, Boolean> mCachedUriPath = new HashMap(1);
    private static Uri mDefaultAlarmUri = null;
    private static Uri mDefaultNotificationUri = null;
    private static Uri mDefaultRingtone2Uri = null;
    private static Uri mDefaultRingtoneUri = null;
    private Activity mActivity;
    private Context mContext;
    private Cursor mCursor;
    private final List<String> mFilterColumns = new ArrayList();
    private Ringtone mPreviousRingtone;
    private boolean mStopPreviousRingtone = true;
    private int mType = 1;

    private static boolean checkDefaultRingtoneProperUri(android.content.Context r24, int r25) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r19 = getDefaultSoundUriField(r25);
        r18 = getDefaultSoundConstantPathField(r25);
        if (r19 == 0) goto L_0x000c;
    L_0x000a:
        if (r18 != 0) goto L_0x000e;
    L_0x000c:
        r4 = 1;
    L_0x000d:
        return r4;
    L_0x000e:
        r0 = r24;
        r1 = r19;
        r22 = getStringForCurrentUser(r0, r1);
        r0 = r24;
        r1 = r18;
        r17 = getStringForCurrentUser(r0, r1);
        if (r22 != 0) goto L_0x0022;
    L_0x0020:
        r4 = 1;
        goto L_0x000d;
    L_0x0022:
        r4 = android.provider.MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        r4 = r4.toString();
        r0 = r22;
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x004c;
    L_0x0030:
        r4 = "RingtoneManager";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "checkDefaultRingtoneProperUri : InternalMediaAudio uriString not null type:";
        r6 = r6.append(r7);
        r0 = r25;
        r6 = r6.append(r0);
        r6 = r6.toString();
        android.util.Log.d(r4, r6);
        r4 = 1;
        goto L_0x000d;
    L_0x004c:
        if (r17 != 0) goto L_0x0057;
    L_0x004e:
        r4 = "RingtoneManager";
        r6 = "checkDefaultRingtoneProperUri : Settings of SoundConstantPath null";
        android.util.Log.d(r4, r6);
        r4 = 1;
        goto L_0x000d;
    L_0x0057:
        r3 = android.net.Uri.parse(r22);
        r12 = 0;
        r13 = 0;
        r2 = getCurrentProfileContentResolver(r24);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = 1;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = new java.lang.String[r4];	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = 0;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r7 = "_data";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4[r6] = r7;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r5 = 0;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = 0;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r7 = 0;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r12 = r2.query(r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        if (r12 == 0) goto L_0x00a9;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
    L_0x0072:
        r4 = r12.getCount();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = 1;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        if (r4 != r6) goto L_0x00a9;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
    L_0x0079:
        r12.moveToFirst();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = "_data";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = r12.getColumnIndexOrThrow(r4);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r20 = r12.getString(r4);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r0 = r20;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r1 = r17;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = r0.equals(r1);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        if (r4 == 0) goto L_0x00a9;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
    L_0x0090:
        r4 = "RingtoneManager";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = "checkDefaultRingtoneProperUri : path and URI match to each other ";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        android.util.Log.d(r4, r6);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r0 = r17;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        setCacheUri(r3, r0);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = 1;
        if (r12 == 0) goto L_0x00a2;
    L_0x009f:
        r12.close();
    L_0x00a2:
        if (r13 == 0) goto L_0x000d;
    L_0x00a4:
        r13.close();
        goto L_0x000d;
    L_0x00a9:
        r4 = "RingtoneManager";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = "checkDefaultRingtoneProperUri : patch and URI DONT match";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        android.util.Log.e(r4, r6);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r5 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = 1;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = new java.lang.String[r4];	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = 0;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r7 = "_id";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6[r4] = r7;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r7 = "_data=?";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = 1;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r8 = new java.lang.String[r4];	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = 0;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r8[r4] = r17;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r9 = 0;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = r2;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r13 = r4.query(r5, r6, r7, r8, r9);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        if (r13 == 0) goto L_0x019b;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
    L_0x00ca:
        r4 = r13.getCount();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = 1;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        if (r4 != r6) goto L_0x019b;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
    L_0x00d1:
        r13.moveToFirst();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = "_id";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = r13.getColumnIndexOrThrow(r4);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r10 = r13.getLong(r4);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = "RingtoneManager";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6.<init>();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r7 = "checkDefaultRingtoneProperUri : we've found same path and another index :  ";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.append(r10);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        android.util.Log.e(r4, r6);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r21 = android.content.ContentUris.withAppendedId(r4, r10);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r0 = r21;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r1 = r17;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        setCacheUri(r0, r1);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = r24.getContentResolver();	 Catch:{ SecurityException -> 0x013b }
        r6 = getDefaultSoundUriField(r25);	 Catch:{ SecurityException -> 0x013b }
        r7 = r21.toString();	 Catch:{ SecurityException -> 0x013b }
        r8 = -2;	 Catch:{ SecurityException -> 0x013b }
        android.provider.Settings.System.putStringForUser(r4, r6, r7, r8);	 Catch:{ SecurityException -> 0x013b }
    L_0x0113:
        r23 = new android.content.ContentValues;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r23.<init>();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = "is_ringtone";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = 1;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = java.lang.Boolean.valueOf(r6);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r0 = r23;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r0.put(r4, r6);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = 0;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = 0;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r0 = r21;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r1 = r23;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r2.update(r0, r1, r4, r6);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = 1;
        if (r12 == 0) goto L_0x0134;
    L_0x0131:
        r12.close();
    L_0x0134:
        if (r13 == 0) goto L_0x000d;
    L_0x0136:
        r13.close();
        goto L_0x000d;
    L_0x013b:
        r15 = move-exception;
        r4 = r24.getContentResolver();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = getDefaultSoundUriField(r25);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r7 = r21.toString();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        android.provider.Settings.System.putString(r4, r6, r7);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        goto L_0x0113;
    L_0x014c:
        r16 = move-exception;
        r4 = "RingtoneManager";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6.<init>();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r7 = "checkDefaultRingtoneProperUri : Excepiton : ";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r0 = r16;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.append(r0);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        android.util.Log.e(r4, r6);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = "RingtoneManager";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6.<init>();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r7 = "uriString : ";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r0 = r22;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.append(r0);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r7 = ", pathString : ";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r0 = r17;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.append(r0);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        android.util.Log.e(r4, r6);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        if (r12 == 0) goto L_0x0193;
    L_0x0190:
        r12.close();
    L_0x0193:
        if (r13 == 0) goto L_0x0198;
    L_0x0195:
        r13.close();
    L_0x0198:
        r4 = 0;
        goto L_0x000d;
    L_0x019b:
        r14 = getDefaultSoundUri(r24, r25);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r4 = "RingtoneManager";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6.<init>();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r7 = "checkDefaultRingtoneProperUri : we havn't found same path. default Uri : ";	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.append(r14);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        android.util.Log.e(r4, r6);	 Catch:{ Exception -> 0x014c, all -> 0x01c2 }
        if (r12 == 0) goto L_0x01bc;
    L_0x01b9:
        r12.close();
    L_0x01bc:
        if (r13 == 0) goto L_0x0198;
    L_0x01be:
        r13.close();
        goto L_0x0198;
    L_0x01c2:
        r4 = move-exception;
        if (r12 == 0) goto L_0x01c8;
    L_0x01c5:
        r12.close();
    L_0x01c8:
        if (r13 == 0) goto L_0x01cd;
    L_0x01ca:
        r13.close();
    L_0x01cd:
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.RingtoneManager.checkDefaultRingtoneProperUri(android.content.Context, int):boolean");
    }

    public static void setActualDefaultRingtoneUri(android.content.Context r11, int r12, android.net.Uri r13, int r14) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r10 = 1;
        r3 = 0;
        r2 = 0;
        r8 = getSettingForType(r12);
        if (r8 != 0) goto L_0x000a;
    L_0x0009:
        return;
    L_0x000a:
        if (r13 != 0) goto L_0x0014;
    L_0x000c:
        r0 = r11.getContentResolver();
        android.provider.Settings.System.putStringForUser(r0, r8, r2, r14);
        goto L_0x0009;
    L_0x0014:
        r0 = r13.toString();
        r1 = android.provider.MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        r1 = r1.toString();
        r0 = r0.startsWith(r1);
        if (r0 != 0) goto L_0x0034;
    L_0x0024:
        r0 = r13.toString();
        r1 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        r1 = r1.toString();
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x0009;
    L_0x0034:
        r0 = r11.getContentResolver();
        r1 = r13.toString();
        android.provider.Settings.System.putStringForUser(r0, r8, r1, r14);
        r0 = "ringtone";
        r0 = r8.equals(r0);
        if (r0 == 0) goto L_0x007b;
    L_0x0048:
        r0 = r11.getContentResolver();
        r1 = "recommendation_time";
        android.provider.Settings.System.putIntForUser(r0, r1, r3, r14);
    L_0x0052:
        r6 = 0;
        r9 = 0;
        r0 = r13.toString();
        r1 = android.provider.MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        r1 = r1.toString();
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x008f;
    L_0x0064:
        r0 = r12 & 32;
        if (r0 != 0) goto L_0x0009;
    L_0x0068:
        r0 = r11.getContentResolver();
        r1 = getDefaultSoundConstantPathField(r12);
        android.provider.Settings.System.putStringForUser(r0, r1, r2, r14);
        r0 = "RingtoneManager";
        r1 = "RingtoneConstantPath: for internal is null...";
        android.util.Log.d(r0, r1);
        goto L_0x0009;
    L_0x007b:
        r0 = "ringtone_2";
        r0 = r8.equals(r0);
        if (r0 == 0) goto L_0x0052;
    L_0x0084:
        r0 = r11.getContentResolver();
        r1 = "recommendation_time_2";
        android.provider.Settings.System.putIntForUser(r0, r1, r3, r14);
        goto L_0x0052;
    L_0x008f:
        r0 = r11.getContentResolver();	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r1 = 1;	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r2 = new java.lang.String[r1];	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r1 = 0;	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r3 = "_data";	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r2[r1] = r3;	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r3 = 0;	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r4 = 0;	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r5 = 0;	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r1 = r13;	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r6 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        if (r6 == 0) goto L_0x00cb;	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
    L_0x00a5:
        r0 = r6.getCount();	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        if (r0 != r10) goto L_0x00cb;	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
    L_0x00ab:
        r6.moveToFirst();	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r0 = "_data";	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r0 = r6.getColumnIndexOrThrow(r0);	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r9 = r6.getString(r0);	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r0 = r11.getContentResolver();	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r1 = getDefaultSoundConstantPathField(r12);	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        android.provider.Settings.System.putStringForUser(r0, r1, r9, r14);	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r0 = "RingtoneManager";	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r1 = "setActualDefaultRingtoneUri: SetDefaultRingtone success...";	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        android.util.Log.d(r0, r1);	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
    L_0x00cb:
        if (r6 == 0) goto L_0x0009;
    L_0x00cd:
        r6.close();
        goto L_0x0009;
    L_0x00d2:
        r7 = move-exception;
        r0 = "RingtoneManager";	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r1.<init>();	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r2 = "setActualDefaultRingtoneUri : Excepiton : ";	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r1 = r1.append(r7);	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        android.util.Log.e(r0, r1);	 Catch:{ Exception -> 0x00d2, all -> 0x00f3 }
        if (r6 == 0) goto L_0x0009;
    L_0x00ee:
        r6.close();
        goto L_0x0009;
    L_0x00f3:
        r0 = move-exception;
        if (r6 == 0) goto L_0x00f9;
    L_0x00f6:
        r6.close();
    L_0x00f9:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.RingtoneManager.setActualDefaultRingtoneUri(android.content.Context, int, android.net.Uri, int):void");
    }

    public RingtoneManager(Activity activity) {
        this.mActivity = activity;
        this.mContext = activity;
        setType(this.mType);
    }

    public RingtoneManager(Context context) {
        this.mContext = context;
        setType(this.mType);
    }

    public void setType(int type) {
        if (this.mCursor != null) {
            throw new IllegalStateException("Setting filter columns should be done before querying for ringtones.");
        }
        this.mType = type;
        if ((type & 5) != 0) {
            type |= 5;
        }
        setFilterColumnsList(type);
    }

    public int inferStreamType() {
        switch (this.mType) {
            case 2:
            case 256:
                return 5;
            case 4:
                return 4;
            default:
                return 2;
        }
    }

    public void setStopPreviousRingtone(boolean stopPreviousRingtone) {
        this.mStopPreviousRingtone = stopPreviousRingtone;
    }

    public boolean getStopPreviousRingtone() {
        return this.mStopPreviousRingtone;
    }

    public void stopPreviousRingtone() {
        if (this.mPreviousRingtone != null) {
            this.mPreviousRingtone.stop();
        }
    }

    @Deprecated
    public boolean getIncludeDrm() {
        return false;
    }

    @Deprecated
    public void setIncludeDrm(boolean includeDrm) {
        if (includeDrm) {
            Log.w(TAG, "setIncludeDrm no longer supported");
        }
    }

    public Cursor getCursor() {
        if (this.mCursor != null && !this.mCursor.isClosed() && this.mCursor.requery()) {
            return this.mCursor;
        }
        Cursor internalCursor = getInternalRingtones();
        Cursor mediaCursor = getMediaRingtones();
        Cursor sortCursor = new SortCursor(new Cursor[]{internalCursor, mediaCursor}, "title_key");
        this.mCursor = sortCursor;
        return sortCursor;
    }

    public Ringtone getRingtone(int position) {
        if (this.mStopPreviousRingtone && this.mPreviousRingtone != null) {
            this.mPreviousRingtone.stop();
        }
        this.mPreviousRingtone = getRingtone(this.mContext, getRingtoneUri(position), inferStreamType());
        return this.mPreviousRingtone;
    }

    public Uri getRingtoneUri(int position) {
        Uri uri = null;
        try {
            if (!(this.mCursor == null || this.mCursor.isClosed() || !this.mCursor.moveToPosition(position))) {
                uri = getUriFromCursor(this.mCursor);
            }
        } catch (IllegalStateException e) {
        }
        return uri;
    }

    private static Uri getUriFromCursor(Cursor cursor) {
        try {
            return ContentUris.withAppendedId(Uri.parse(cursor.getString(2)), cursor.getLong(0));
        } catch (Exception e) {
            return null;
        }
    }

    public int getRingtonePosition(Uri ringtoneUri) {
        if (ringtoneUri == null) {
            return -1;
        }
        Cursor cursor = getCursor();
        int cursorCount = cursor.getCount();
        if (!cursor.moveToFirst()) {
            return -1;
        }
        Uri currentUri = null;
        String previousUriString = null;
        int i = 0;
        while (i < cursorCount) {
            try {
                String uriString = cursor.getString(2);
                if (currentUri == null || !uriString.equals(previousUriString)) {
                    currentUri = Uri.parse(uriString);
                }
                if (ringtoneUri.equals(ContentUris.withAppendedId(currentUri, cursor.getLong(0)))) {
                    return i;
                }
                cursor.move(1);
                previousUriString = uriString;
                i++;
            } catch (Exception e) {
                Log.e(TAG, "Ringtone Picker index exceeded(Cursor Count=" + cursorCount + ")");
                return -1;
            }
        }
        return -1;
    }

    public static Uri getValidRingtoneUri(Context context) {
        RingtoneManager rm = new RingtoneManager(context);
        Uri uri = getValidRingtoneUriFromCursorAndClose(context, rm.getInternalRingtones());
        if (uri == null) {
            return getValidRingtoneUriFromCursorAndClose(context, rm.getMediaRingtones());
        }
        return uri;
    }

    private static Uri getValidRingtoneUriFromCursorAndClose(Context context, Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        Uri uri = null;
        if (cursor.moveToFirst()) {
            uri = getUriFromCursor(cursor);
        }
        cursor.close();
        return uri;
    }

    private Cursor getInternalRingtones() {
        List<String> mThemeColumns = new ArrayList();
        mThemeColumns.clear();
        if (!((this.mType & 1) == 0 && (this.mType & 128) == 0)) {
            mThemeColumns.add(AudioColumns.IS_RINGTONE_THEME);
        }
        if (!((this.mType & 2) == 0 && (this.mType & 256) == 0)) {
            mThemeColumns.add(AudioColumns.IS_NOTIFICATION_THEME);
        }
        if ((this.mType & 4) != 0) {
            mThemeColumns.add(AudioColumns.IS_ALARM_THEME);
        }
        Cursor internal = query(Media.INTERNAL_CONTENT_URI, INTERNAL_COLUMNS, constructBooleanTrueWhereClause(this.mFilterColumns), null, "title_key");
        Cursor theme = query(Media.INTERNAL_CONTENT_URI, INTERNAL_COLUMNS, constructBooleanTrueWhereClauseTheme(mThemeColumns), null, "title_key");
        return new MergeCursor(new Cursor[]{internal, theme});
    }

    private Cursor getMediaRingtones() {
        if (this.mContext.checkPermission(permission.READ_EXTERNAL_STORAGE, Process.myPid(), Process.myUid()) != 0) {
            Log.w(TAG, "No READ_EXTERNAL_STORAGE permission, ignoring ringtones on ext storage");
            return null;
        }
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED) || status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return query(Media.EXTERNAL_CONTENT_URI, MEDIA_COLUMNS, constructBooleanTrueWhereClause(this.mFilterColumns), null, "title_key");
        }
        return null;
    }

    private void setFilterColumnsList(int type) {
        List<String> columns = this.mFilterColumns;
        columns.clear();
        if (!((type & 1) == 0 && (type & 128) == 0)) {
            columns.add(AudioColumns.IS_RINGTONE);
        }
        if (!((type & 2) == 0 && (type & 256) == 0)) {
            columns.add(AudioColumns.IS_NOTIFICATION);
        }
        if ((type & 4) != 0) {
            columns.add(AudioColumns.IS_ALARM);
        }
    }

    private static String constructBooleanTrueWhereClause(List<String> columns) {
        if (columns == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = columns.size() - 1; i >= 0; i--) {
            sb.append((String) columns.get(i)).append("=1 or ");
        }
        if (columns.size() > 0) {
            sb.setLength(sb.length() - 4);
        }
        sb.append(")");
        sb.append(" and ");
        sb.append("_size");
        sb.append("!=0");
        sb.append(" and ");
        sb.append(AudioColumns.IS_RINGTONE_THEME);
        sb.append("!=1");
        sb.append(" and ");
        sb.append(AudioColumns.IS_NOTIFICATION_THEME);
        sb.append("!=1");
        sb.append(" and ");
        sb.append(AudioColumns.IS_ALARM_THEME);
        sb.append("!=1");
        return sb.toString();
    }

    private static String constructBooleanTrueWhereClauseTheme(List<String> columns) {
        if (columns == null || columns.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = columns.size() - 1; i >= 0; i--) {
            sb.append((String) columns.get(i)).append("=1 or ");
        }
        if (columns.size() > 0) {
            sb.setLength(sb.length() - 4);
        }
        sb.append(")");
        sb.append(" and ");
        sb.append("_size");
        sb.append("!=0");
        return sb.toString();
    }

    private Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (this.mActivity != null) {
            return this.mActivity.managedQuery(uri, projection, selection, selectionArgs, sortOrder);
        }
        return this.mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    public static Ringtone getRingtone(Context context, Uri ringtoneUri) {
        return getRingtone(context, ringtoneUri, -1);
    }

    private static Ringtone getRingtone(Context context, Uri ringtoneUri, int streamType) {
        try {
            Ringtone r = new Ringtone(context, true);
            if (streamType >= 0) {
                r.setStreamType(streamType);
            }
            if (r.setUri(ringtoneUri)) {
                return r;
            }
            Log.w(TAG, "Failed to setUri " + ringtoneUri + ", return ringtone");
            return r;
        } catch (Exception ex) {
            Log.e(TAG, "Failed to open ringtone " + ringtoneUri + ": " + ex);
            return null;
        }
    }

    public Ringtone getRingtone(int position, int mSec) {
        if (this.mStopPreviousRingtone && this.mPreviousRingtone != null) {
            this.mPreviousRingtone.stop();
        }
        this.mPreviousRingtone = getRingtone(this.mContext, getRingtoneUri(position), inferStreamType(), mSec);
        return this.mPreviousRingtone;
    }

    private static Ringtone getRingtone(Context context, Uri ringtoneUri, int streamType, int mSec) {
        try {
            Ringtone r = new Ringtone(context, true);
            if (streamType >= 0) {
                r.setStreamType(streamType);
            }
            if (mSec >= 0) {
                r.setSecForSeek(mSec);
            }
            if (r.setUri(ringtoneUri)) {
                return r;
            }
            Log.w(TAG, "Failed to setUri " + ringtoneUri + ", return ringtone");
            return r;
        } catch (Exception ex) {
            Log.e(TAG, "Failed to open ringtone " + ringtoneUri + ": " + ex);
            return null;
        }
    }

    public static Ringtone getRingtone(Context context, int mSec, Uri ringtoneUri) {
        try {
            Ringtone r = new Ringtone(context, true);
            if (mSec >= 0) {
                r.setSecForSeek(mSec);
            }
            if (r.setUri(ringtoneUri)) {
                return r;
            }
            Log.w(TAG, "Failed to setUri " + ringtoneUri + ", return ringtone");
            return r;
        } catch (Exception ex) {
            Log.e(TAG, "Failed to open ringtone " + ringtoneUri + ": " + ex);
            return null;
        }
    }

    public static Uri getActualDefaultRingtoneUri(Context context, int type) {
        Log.d(TAG, "getActualDefaultRingtoneUri  type    :" + type);
        if (!checkDefaultRingtoneProperUri(context, type)) {
            Uri defaultSettingSound = getDefaultSettingSound(type);
            if (defaultSettingSound != null) {
                return defaultSettingSound;
            }
        }
        String setting = getSettingForType(type);
        if (setting == null) {
            return null;
        }
        String uriString = getStringForCurrentUser(context, setting);
        if (uriString != null) {
            return Uri.parse(uriString);
        }
        return null;
    }

    private static String getDefaultSoundConstantPathField(int type) {
        String constantPath = "_CONSTANT_PATH";
        switch (type) {
            case 1:
                return "ringtone" + constantPath;
            case 2:
                return "notification_sound" + constantPath;
            case 4:
                return "alarm_alert" + constantPath;
            case 128:
                return "ringtone_2" + constantPath;
            case 256:
                return "notification_sound_2" + constantPath;
            default:
                return null;
        }
    }

    private static String getDefaultSoundUriField(int type) {
        switch (type) {
            case 1:
                return "ringtone";
            case 2:
                return "notification_sound";
            case 4:
                return "alarm_alert";
            case 128:
                return "ringtone_2";
            case 256:
                return "notification_sound_2";
            default:
                return null;
        }
    }

    private static Uri getDefaultSettingSound(int type) {
        if ((type & 1) != 0) {
            return mDefaultRingtoneUri;
        }
        if ((type & 128) != 0) {
            return mDefaultRingtone2Uri;
        }
        if ((type & 2) != 0 || (type & 256) != 0) {
            return mDefaultNotificationUri;
        }
        if ((type & 4) != 0) {
            return mDefaultAlarmUri;
        }
        return null;
    }

    private static Uri getDefaultSoundUri(Context context, int type) {
        String defaultSoundTitle;
        List<String> columns = new ArrayList();
        boolean foundUri = false;
        if ((type & 1) != 0) {
            if (mDefaultRingtoneUri != null) {
                return mDefaultRingtoneUri;
            }
            defaultSoundTitle = SystemProperties.get("ro.config.ringtone");
            columns.add(AudioColumns.IS_RINGTONE);
        } else if ((type & 128) != 0) {
            if (mDefaultRingtone2Uri != null) {
                return mDefaultRingtone2Uri;
            }
            defaultSoundTitle = SystemProperties.get("ro.config.ringtone_2");
            if (defaultSoundTitle == null || defaultSoundTitle.trim().length() == 0) {
                Log.e(TAG, "ro.config.ringtone_2 is not set");
                defaultSoundTitle = SystemProperties.get("ro.config.ringtone");
            }
            columns.add(AudioColumns.IS_RINGTONE);
        } else if ((type & 2) == 0 && (type & 256) == 0) {
            if ((type & 4) == 0) {
                return null;
            }
            if (mDefaultAlarmUri != null) {
                return mDefaultAlarmUri;
            }
            defaultSoundTitle = SystemProperties.get("ro.config.alarm_alert");
            columns.add(AudioColumns.IS_RINGTONE);
            columns.add(AudioColumns.IS_ALARM);
        } else if (mDefaultNotificationUri != null) {
            return mDefaultNotificationUri;
        } else {
            defaultSoundTitle = SystemProperties.get("ro.config.notification_sound");
            columns.add(AudioColumns.IS_NOTIFICATION);
        }
        Log.d(TAG, "Default ringtone/notification sound is :" + defaultSoundTitle);
        Cursor internalCursor = null;
        Cursor dataCursor = null;
        String tempStrUri = null;
        String dataPath = null;
        try {
            internalCursor = context.getContentResolver().query(Media.INTERNAL_CONTENT_URI, INTERNAL_COLUMNS, constructBooleanTrueWhereClause(columns), null, "title_key");
            internalCursor.moveToFirst();
            while (!internalCursor.isAfterLast()) {
                tempStrUri = internalCursor.getString(2);
                tempStrUri = tempStrUri + "/" + internalCursor.getString(0);
                dataCursor = context.getContentResolver().query(Uri.parse(tempStrUri), new String[]{"_data"}, null, null, null);
                if (dataCursor != null) {
                    dataCursor.moveToFirst();
                    dataPath = dataCursor.getString(dataCursor.getColumnIndex("_data"));
                    dataCursor.close();
                    dataCursor = null;
                }
                if (dataPath == null || !dataPath.endsWith(defaultSoundTitle)) {
                    internalCursor.moveToNext();
                } else {
                    foundUri = true;
                    if ((type & 1) != 0) {
                        mDefaultRingtoneUri = Uri.parse(tempStrUri);
                    } else if ((type & 128) != 0) {
                        mDefaultRingtone2Uri = Uri.parse(tempStrUri);
                    } else if ((type & 2) != 0 || (type & 256) != 0) {
                        mDefaultNotificationUri = Uri.parse(tempStrUri);
                    } else if ((type & 4) != 0) {
                        mDefaultAlarmUri = Uri.parse(tempStrUri);
                    }
                    Log.d(TAG, "Default notification's uri found : " + tempStrUri);
                    if (dataCursor != null) {
                        dataCursor.close();
                    }
                    if (internalCursor != null) {
                        internalCursor.close();
                    }
                    if (foundUri) {
                        return null;
                    }
                    return Uri.parse(tempStrUri);
                }
            }
            Log.d(TAG, "Default notification's uri found : " + tempStrUri);
            if (dataCursor != null) {
                dataCursor.close();
            }
            if (internalCursor != null) {
                internalCursor.close();
            }
        } catch (Exception ex) {
            Log.e(TAG, "Can't read ro.config value", ex);
            if (dataCursor != null) {
                dataCursor.close();
            }
            if (internalCursor != null) {
                internalCursor.close();
            }
        } catch (Throwable th) {
            if (dataCursor != null) {
                dataCursor.close();
            }
            if (internalCursor != null) {
                internalCursor.close();
            }
        }
        if (foundUri) {
            return null;
        }
        return Uri.parse(tempStrUri);
    }

    protected static void clearCachedUri() {
        mCachedUriPath.clear();
    }

    protected static Boolean getCachedUri(Uri uri) {
        if (uri == null) {
            return null;
        }
        return (Boolean) mCachedUriPath.get(uri.toString());
    }

    protected static void setCacheUri(Uri uri, String path) {
        Log.d(TAG, "setCacheUri uri:" + uri + ", external path:" + EXTERNAL_PATH + ", path:" + path);
        mCachedUriPath.clear();
        if (uri != null && EXTERNAL_PATH != null && path != null) {
            mCachedUriPath.put(uri.toString(), Boolean.valueOf(path.startsWith(EXTERNAL_PATH)));
        }
    }

    public static void setActualDefaultRingtoneUri(Context context, int type, Uri ringtoneUri) {
        setActualDefaultRingtoneUri(context, type, ringtoneUri, UserHandle.getCallingUserId());
    }

    private static String getSettingForType(int type) {
        if ((type & 1) != 0) {
            return "ringtone";
        }
        if ((type & 2) != 0) {
            return "notification_sound";
        }
        if ((type & 4) != 0) {
            return "alarm_alert";
        }
        if ((type & 128) != 0) {
            return "ringtone_2";
        }
        if ((type & 256) != 0) {
            return "notification_sound_2";
        }
        return null;
    }

    public static int convertSettingForType(int type, int simId) {
        if (simId == 1) {
            if (type == 1) {
                return 128;
            }
            if (type == 2) {
                return 256;
            }
            return type;
        } else if (type == 128) {
            return 1;
        } else {
            if (type == 256) {
                return 2;
            }
            return type;
        }
    }

    public static boolean isDefault(Uri ringtoneUri) {
        return getDefaultType(ringtoneUri) != -1;
    }

    public static int getDefaultType(Uri defaultRingtoneUri) {
        if (defaultRingtoneUri == null) {
            return -1;
        }
        if (defaultRingtoneUri.equals(System.DEFAULT_RINGTONE_URI)) {
            return 1;
        }
        if (defaultRingtoneUri.equals(System.DEFAULT_NOTIFICATION_URI)) {
            return 2;
        }
        if (defaultRingtoneUri.equals(System.DEFAULT_ALARM_ALERT_URI)) {
            return 4;
        }
        if (defaultRingtoneUri.equals(System.DEFAULT_RINGTONE_URI_2)) {
            return 128;
        }
        if (defaultRingtoneUri.equals(System.DEFAULT_RINGTONE_URI_3)) {
            return 1;
        }
        if (defaultRingtoneUri.equals(System.DEFAULT_NOTIFICATION_URI_2)) {
            return 256;
        }
        return -1;
    }

    public static Uri getDefaultUri(int type) {
        if ((type & 1) != 0) {
            return System.DEFAULT_RINGTONE_URI;
        }
        if ((type & 2) != 0) {
            return System.DEFAULT_NOTIFICATION_URI;
        }
        if ((type & 4) != 0) {
            return System.DEFAULT_ALARM_ALERT_URI;
        }
        if ((type & 128) != 0) {
            return System.DEFAULT_RINGTONE_URI_2;
        }
        if ((type & 256) != 0) {
            return System.DEFAULT_NOTIFICATION_URI_2;
        }
        return null;
    }

    protected static ContentResolver getCurrentProfileContentResolver(Context context) {
        try {
            int currentUser = ActivityManager.getCurrentUser();
            int myUser = UserManager.get(context).getUserHandle();
            int profileGroupId = UserManager.get(context).getUserInfo(myUser).profileGroupId;
            Slog.d(TAG, "myUser=" + myUser + ", currentUser=" + currentUser);
            if (!(myUser == currentUser || currentUser == profileGroupId)) {
                try {
                    return context.createPackageContextAsUser(context.getPackageName(), 0, new UserHandle(currentUser)).getContentResolver();
                } catch (NameNotFoundException e) {
                    Slog.d(TAG, "Can't find self package", e);
                }
            }
        } catch (Exception e2) {
            Slog.d(TAG, "Can't get current user. return default user");
        }
        return context.getContentResolver();
    }

    private static String getStringForCurrentUser(Context context, String setting) {
        try {
            return System.getStringForUser(context.getContentResolver(), setting, -2);
        } catch (Exception e) {
            return System.getString(context.getContentResolver(), setting);
        }
    }
}
