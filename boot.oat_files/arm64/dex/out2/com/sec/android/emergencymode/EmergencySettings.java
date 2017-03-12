package com.sec.android.emergencymode;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.PhoneNumberUtils;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephony.Stub;

public class EmergencySettings {
    private static final String TAG = "EmergencySettings";

    public static java.lang.String getEmergencyNumber(android.content.ContentResolver r16, java.lang.String r17) {
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
        if (r16 == 0) goto L_0x0004;
    L_0x0002:
        if (r17 != 0) goto L_0x0006;
    L_0x0004:
        r15 = 0;
    L_0x0005:
        return r15;
    L_0x0006:
        r15 = 0;
        r8 = 0;
        r11 = 0;
        r12 = 1;
        r14 = 0;
        r10 = 0;
    L_0x000c:
        r1 = com.samsung.android.telephony.MultiSimManager.getSimSlotCount();	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        if (r10 >= r1) goto L_0x002a;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
    L_0x0012:
        if (r14 == 0) goto L_0x001b;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
    L_0x0014:
        r1 = "";	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        if (r14 == r1) goto L_0x001b;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
    L_0x0018:
        r1 = 1;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        if (r12 != r1) goto L_0x001f;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
    L_0x001b:
        r14 = com.samsung.android.telephony.MultiSimManager.getNetworkOperator(r10);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
    L_0x001f:
        r12 = com.samsung.android.telephony.MultiSimManager.getSimState(r10);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r1 = 5;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        if (r12 != r1) goto L_0x0027;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
    L_0x0026:
        r11 = 1;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
    L_0x0027:
        r10 = r10 + 1;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        goto L_0x000c;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
    L_0x002a:
        r1 = 0;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = 3;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r13 = r14.substring(r1, r2);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r1 = "EmergencySettings";	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2.<init>();	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r3 = "getEmergencyNumber requested Country : ";	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = r2.append(r13);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r3 = " sim ready = ";	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = r2.append(r11);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        com.sec.android.emergencymode.Elog.d(r1, r2);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r1.<init>();	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = "mcc='";	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r1 = r1.append(r13);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = "'";	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r4 = r1.toString();	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = com.sec.android.emergencymode.EmergencyConstants.URI_ECCLIST;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r3 = 0;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r5 = 0;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r6 = 0;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r1 = r16;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r8 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        if (r8 == 0) goto L_0x008f;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
    L_0x0078:
        r1 = r8.getCount();	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        if (r1 <= 0) goto L_0x008f;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
    L_0x007e:
        r8.moveToFirst();	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r0 = r17;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r1 = r8.getColumnIndex(r0);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r1 = r8.getString(r1);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r15 = makeEmergencyNumber(r1, r11);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
    L_0x008f:
        if (r8 == 0) goto L_0x0094;
    L_0x0091:
        r8.close();
    L_0x0094:
        if (r15 != 0) goto L_0x0005;
    L_0x0096:
        r1 = "EmergencySettings";
        r2 = "getEmergencyNumber not found emergency number!";
        com.sec.android.emergencymode.Elog.d(r1, r2);
        r1 = "ro.csc.country_code";
        r7 = android.os.SystemProperties.get(r1);
        r1 = "China";
        r1 = r1.equalsIgnoreCase(r7);
        if (r1 == 0) goto L_0x00af;
    L_0x00ab:
        r15 = "119";
        goto L_0x0005;
    L_0x00af:
        r15 = "911";
        goto L_0x0005;
    L_0x00b3:
        r9 = move-exception;
        r1 = "EmergencySettings";	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2.<init>();	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r3 = "Exception ";	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = r2.append(r9);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        com.sec.android.emergencymode.Elog.d(r1, r2);	 Catch:{ Exception -> 0x00b3, all -> 0x00f0 }
        if (r8 == 0) goto L_0x00d1;
    L_0x00ce:
        r8.close();
    L_0x00d1:
        if (r15 != 0) goto L_0x0005;
    L_0x00d3:
        r1 = "EmergencySettings";
        r2 = "getEmergencyNumber not found emergency number!";
        com.sec.android.emergencymode.Elog.d(r1, r2);
        r1 = "ro.csc.country_code";
        r7 = android.os.SystemProperties.get(r1);
        r1 = "China";
        r1 = r1.equalsIgnoreCase(r7);
        if (r1 == 0) goto L_0x00ec;
    L_0x00e8:
        r15 = "119";
        goto L_0x0005;
    L_0x00ec:
        r15 = "911";
        goto L_0x0005;
    L_0x00f0:
        r1 = move-exception;
        if (r8 == 0) goto L_0x00f6;
    L_0x00f3:
        r8.close();
    L_0x00f6:
        if (r15 != 0) goto L_0x010f;
    L_0x00f8:
        r2 = "EmergencySettings";
        r3 = "getEmergencyNumber not found emergency number!";
        com.sec.android.emergencymode.Elog.d(r2, r3);
        r2 = "ro.csc.country_code";
        r7 = android.os.SystemProperties.get(r2);
        r2 = "China";
        r2 = r2.equalsIgnoreCase(r7);
        if (r2 == 0) goto L_0x0110;
    L_0x010d:
        r15 = "119";
    L_0x010f:
        throw r1;
    L_0x0110:
        r15 = "911";
        goto L_0x010f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.emergencymode.EmergencySettings.getEmergencyNumber(android.content.ContentResolver, java.lang.String):java.lang.String");
    }

    private EmergencySettings() {
    }

    public static void put(ContentResolver resolver, String pref, Object value) {
        if (resolver != null) {
            resolver.delete(EmergencyConstants.URI_PREFSETTINGS, "pref='" + pref + "'", null);
            ContentValues values = new ContentValues();
            values.put(EmergencyConstants.PREF, pref);
            values.put(EmergencyConstants.VALUE, String.valueOf(value));
            Uri resultUri = resolver.insert(EmergencyConstants.URI_PREFSETTINGS, values);
        }
    }

    private static String get(ContentResolver resolver, String pref) {
        String str = null;
        if (!(resolver == null || pref == null)) {
            str = null;
            Cursor cursor = null;
            try {
                String selection = "pref='" + pref + "'";
                cursor = resolver.query(EmergencyConstants.URI_PREFSETTINGS, null, selection, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    str = cursor.getString(cursor.getColumnIndex(EmergencyConstants.VALUE));
                    cursor.close();
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                Elog.d(TAG, "Exception " + e);
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return str;
    }

    public static int getInt(ContentResolver resolver, String pref, int defaultValue) {
        try {
            defaultValue = Integer.parseInt(get(resolver, pref));
        } catch (Exception e) {
            Elog.d(TAG, "Exception " + e);
        }
        return defaultValue;
    }

    public static boolean getBoolean(ContentResolver resolver, String pref, boolean defaultValue) {
        String ret = get(resolver, pref);
        return ret == null ? defaultValue : Boolean.parseBoolean(ret);
    }

    public static String getString(ContentResolver resolver, String pref, String defaultValue) {
        String ret = get(resolver, pref);
        return ret == null ? defaultValue : ret;
    }

    public static long getLong(ContentResolver resolver, String pref, long defaultValue) {
        try {
            defaultValue = Long.parseLong(get(resolver, pref));
        } catch (Exception e) {
            Elog.d(TAG, "Exception " + e);
        }
        return defaultValue;
    }

    public static double getDouble(ContentResolver resolver, String pref, double defaultValue) {
        try {
            defaultValue = Double.parseDouble(get(resolver, pref));
        } catch (Exception e) {
            Elog.d(TAG, "Exception " + e);
        }
        return defaultValue;
    }

    private static String makeEmergencyNumber(String number, boolean isReady) {
        if (PhoneNumberUtils.isEmergencyNumber(number)) {
            Elog.d(TAG, "This is Emergency number");
            return number;
        } else if (isReady && isPossibleNormalCall()) {
            Elog.d(TAG, "SIM Ready, not emergency number.");
            return number;
        } else {
            Elog.d(TAG, "SIM Ready = " + isReady + ", default emergency number.");
            return null;
        }
    }

    private static boolean isPossibleNormalCall() {
        try {
            ITelephony phone = Stub.asInterface(ServiceManager.checkService("phone"));
            if (phone == null) {
                return false;
            }
            int serviceState = phone.getServiceState();
            Elog.d(TAG, "serviceState : " + serviceState);
            if (serviceState == 0) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Elog.d(TAG, "Failed to clear missed calls notification due to remote exception");
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        } catch (Throwable th) {
            return false;
        }
    }
}
