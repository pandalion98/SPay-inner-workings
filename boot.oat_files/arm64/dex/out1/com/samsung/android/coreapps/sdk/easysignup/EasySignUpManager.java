package com.samsung.android.coreapps.sdk.easysignup;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Process;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.ims.ImsConferenceState;
import com.samsung.android.fingerprint.FingerprintManager;

public class EasySignUpManager {
    private static final String AUTHORITY = "com.samsung.android.coreapps.easysignup";
    private static final String AUTHORITY_PUBLIC = "com.samsung.android.coreapps.easysignup.public";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://com.samsung.android.coreapps.easysignup");
    private static final Uri BASE_CONTENT_URI_PUBLIC = Uri.parse("content://com.samsung.android.coreapps.easysignup.public");
    public static final String EASY_SIGNUP_ACCCOUNT_TYPE = "com.samsung.android.coreapps";
    public static final int SERVICE_ID_CONTACT = 0;
    public static final int SERVICE_ID_ESU = 4;
    public static final int SERVICE_ID_FREE_MESSAGE = 1;
    public static final int SERVICE_ID_RSHARE = 2;
    public static final int SERVICE_ID_SHOP = 3;
    public static final int SERVICE_OFF = 0;
    public static final int SERVICE_ON = 1;
    private static final String TAG = "EasySignUpManager_1.0.5";

    public static boolean isJoined(android.content.Context r11, java.lang.String r12, int r13) {
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
        r10 = 0;
        r2 = 0;
        r0 = isAuth(r11, r12);
        if (r0 != 0) goto L_0x0022;
    L_0x0008:
        r0 = "EasySignUpManager_1.0.5";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "auth : false, join : ";
        r2 = r2.append(r3);
        r2 = r2.append(r13);
        r2 = r2.toString();
        android.util.Log.i(r0, r2);
        r0 = r10;
    L_0x0021:
        return r0;
    L_0x0022:
        r9 = "";
        r0 = BASE_CONTENT_URI_PUBLIC;
        r0 = r0.buildUpon();
        r3 = "join_sids";
        r0 = r0.appendPath(r3);
        r3 = "imsi";
        r0 = r0.appendQueryParameter(r3, r12);
        r1 = r0.build();
        r0 = r11.getContentResolver();
        r3 = r2;
        r4 = r2;
        r5 = r2;
        r6 = r0.query(r1, r2, r3, r4, r5);
        if (r6 == 0) goto L_0x0097;
    L_0x0047:
        r0 = r6.getCount();	 Catch:{ all -> 0x009e }
        if (r0 <= 0) goto L_0x0097;	 Catch:{ all -> 0x009e }
    L_0x004d:
        r0 = r6.moveToFirst();	 Catch:{ all -> 0x009e }
        if (r0 == 0) goto L_0x0097;	 Catch:{ all -> 0x009e }
    L_0x0053:
        r0 = "join_sids";	 Catch:{ all -> 0x009e }
        r0 = r6.getColumnIndex(r0);	 Catch:{ all -> 0x009e }
        r9 = r6.getString(r0);	 Catch:{ all -> 0x009e }
        r0 = "EasySignUpManager_1.0.5";	 Catch:{ all -> 0x009e }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x009e }
        r2.<init>();	 Catch:{ all -> 0x009e }
        r3 = "join : ";	 Catch:{ all -> 0x009e }
        r2 = r2.append(r3);	 Catch:{ all -> 0x009e }
        r2 = r2.append(r13);	 Catch:{ all -> 0x009e }
        r3 = ", sids : ";	 Catch:{ all -> 0x009e }
        r2 = r2.append(r3);	 Catch:{ all -> 0x009e }
        r2 = r2.append(r9);	 Catch:{ all -> 0x009e }
        r2 = r2.toString();	 Catch:{ all -> 0x009e }
        android.util.Log.i(r0, r2);	 Catch:{ all -> 0x009e }
        r8 = convertToIntArray(r9);	 Catch:{ all -> 0x009e }
        if (r8 == 0) goto L_0x0097;	 Catch:{ all -> 0x009e }
    L_0x0085:
        r7 = 0;	 Catch:{ all -> 0x009e }
    L_0x0086:
        r0 = r8.length;	 Catch:{ all -> 0x009e }
        if (r7 >= r0) goto L_0x0097;	 Catch:{ all -> 0x009e }
    L_0x0089:
        r0 = r8[r7];	 Catch:{ all -> 0x009e }
        if (r13 != r0) goto L_0x0094;
    L_0x008d:
        r0 = 1;
        if (r6 == 0) goto L_0x0021;
    L_0x0090:
        r6.close();
        goto L_0x0021;
    L_0x0094:
        r7 = r7 + 1;
        goto L_0x0086;
    L_0x0097:
        if (r6 == 0) goto L_0x009c;
    L_0x0099:
        r6.close();
    L_0x009c:
        r0 = r10;
        goto L_0x0021;
    L_0x009e:
        r0 = move-exception;
        if (r6 == 0) goto L_0x00a4;
    L_0x00a1:
        r6.close();
    L_0x00a4:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.coreapps.sdk.easysignup.EasySignUpManager.isJoined(android.content.Context, java.lang.String, int):boolean");
    }

    public static boolean isAuth(Context context) {
        boolean bIsAuth = false;
        Cursor cursor = context.getContentResolver().query(BASE_CONTENT_URI_PUBLIC.buildUpon().appendPath("is_auth").build(), null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.getCount() > 0) {
                    bIsAuth = true;
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        if (bIsAuth) {
            Log.d(TAG, "isAuth is true");
        } else {
            Log.d(TAG, "isAuth is false");
        }
        return bIsAuth;
    }

    public static boolean isAuth(Context context, String imsi) {
        boolean bIsAuth = false;
        Cursor cursor = context.getContentResolver().query(BASE_CONTENT_URI_PUBLIC.buildUpon().appendPath("is_auth").appendQueryParameter("imsi", imsi).build(), null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.getCount() > 0) {
                    bIsAuth = true;
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        if (bIsAuth) {
            Log.d(TAG, "isAuth regarding imsi is true");
        } else {
            Log.d(TAG, "isAuth regarding imsi is false");
        }
        return bIsAuth;
    }

    public static synchronized int getServiceStatus(Context context, int serviceId) {
        int status;
        synchronized (EasySignUpManager.class) {
            status = 0;
            if (isAuth(context)) {
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(BASE_CONTENT_URI_PUBLIC.buildUpon().appendPath("sids").build(), new String[]{"sids"}, null, null, null);
                    if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                        String sids = cursor.getString(cursor.getColumnIndex("sids"));
                        Log.d(TAG, "getServiceStatus - sids : " + sids);
                        int[] result = convertToIntArray(sids);
                        if (result != null) {
                            for (int i : result) {
                                if (serviceId == i) {
                                    status = 1;
                                }
                            }
                        }
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Throwable th) {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
            if (status == 1) {
                Log.d(TAG, "getServiceStatus : serviceId (" + serviceId + ") is ON");
            } else {
                Log.d(TAG, "getServiceStatus : serviceId (" + serviceId + ") is OFF");
            }
        }
        return status;
    }

    public static synchronized boolean isJoined(Context context, int serviceId) {
        boolean z;
        synchronized (EasySignUpManager.class) {
            if (isAuth(context)) {
                Cursor cursor = null;
                Uri uri = BASE_CONTENT_URI_PUBLIC.buildUpon().appendPath("join_sids").build();
                try {
                    cursor = context.getContentResolver().query(uri, new String[]{"join_sids"}, null, null, null);
                    if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                        String joinSids = cursor.getString(cursor.getColumnIndex("join_sids"));
                        Log.d(TAG, "getServiceStatus - join sids : " + joinSids);
                        int[] result = convertToIntArray(joinSids);
                        if (result != null) {
                            int j = 0;
                            while (j < result.length) {
                                if (serviceId == result[j]) {
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                    z = true;
                                } else {
                                    j++;
                                }
                            }
                        }
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                    z = false;
                } catch (Throwable th) {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else {
                Log.i(TAG, "auth : false, join : " + serviceId);
                z = false;
            }
        }
        return z;
    }

    public static boolean isSDKServiceEnable(Context context, String imsi, int serviceId) {
        String sids = "";
        Cursor cursor = context.getContentResolver().query(BASE_CONTENT_URI_PUBLIC.buildUpon().appendPath("sdk_sids").appendQueryParameter("imsi", imsi).build(), null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    sids = cursor.getString(0);
                    Log.i(TAG, "sdkService : " + serviceId + ", sidsList : " + sids);
                    int[] result = convertToIntArray(sids);
                    if (result != null) {
                        for (int i : result) {
                            if (serviceId == i) {
                                return true;
                            }
                        }
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }

    public static int getSupportedFeatures(Context context, int serviceId) {
        int features = -1;
        if (isOwner(context)) {
            Uri uri = Uri.withAppendedPath(Uri.withAppendedPath(BASE_CONTENT_URI_PUBLIC, "features"), String.valueOf(serviceId));
            Cursor cursor = context.getContentResolver().query(uri, new String[]{"features"}, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                        features = cursor.getInt(cursor.getColumnIndex("features"));
                    }
                } catch (Throwable th) {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d(TAG, "serviceId : " + serviceId + ", features : " + features);
        return features;
    }

    public static void serviceOn(Context context, int serviceId) {
        Log.d(TAG, "serviceOn - serviceId : " + serviceId);
        Intent intent = new Intent("com.samsung.android.coreapps.easysignup.ACTION_SERVICE_ON");
        intent.setPackage("com.samsung.android.coreapps");
        intent.setFlags(32);
        intent.putExtra("service_id", serviceId);
        context.sendBroadcast(intent);
    }

    public static void serviceOff(Context context, int serviceId) {
        Log.d(TAG, "serviceOff - serviceId : " + serviceId);
        Intent intent = new Intent("com.samsung.android.coreapps.easysignup.ACTION_SERVICE_OFF");
        intent.setPackage("com.samsung.android.coreapps");
        intent.setFlags(32);
        intent.putExtra("service_id", serviceId);
        context.sendBroadcast(intent);
    }

    public static String getMsisdn(Context context) {
        Log.d(TAG, "getMsisdn");
        String msisdn = null;
        Uri uri = BASE_CONTENT_URI.buildUpon().appendPath("auth").build();
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"msisdn"}, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    msisdn = cursor.getString(cursor.getColumnIndex("msisdn"));
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return msisdn;
    }

    public static String getMsisdn(Context context, String imsi) {
        Log.d(TAG, "getMsisdn with imsi");
        String msisdn = null;
        Uri uri = BASE_CONTENT_URI.buildUpon().appendPath("auth").appendQueryParameter("imsi", imsi).build();
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"msisdn"}, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    msisdn = cursor.getString(cursor.getColumnIndex("msisdn"));
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return msisdn;
    }

    private static int[] convertToIntArray(String str) {
        int[] result = null;
        if (!(TextUtils.isEmpty(str) || str.equals("[]"))) {
            String[] items = str.replaceAll("\\[", "").replaceAll("\\]", "").split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
            result = new int[items.length];
            for (int i = 0; i < items.length; i++) {
                try {
                    result[i] = Integer.parseInt(items[i]);
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
            }
        }
        return result;
    }

    private static boolean isOwner(Context context) {
        UserHandle userHandle = Process.myUserHandle();
        UserManager userMgr = (UserManager) context.getSystemService(ImsConferenceState.USER);
        if (userMgr == null) {
            return false;
        }
        long userSerialNum = userMgr.getSerialNumberForUser(userHandle);
        Log.d(TAG, "userSerialNumber = " + userSerialNum);
        if (0 == userSerialNum) {
            return true;
        }
        return false;
    }
}
