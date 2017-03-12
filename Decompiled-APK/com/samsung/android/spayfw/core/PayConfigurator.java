package com.samsung.android.spayfw.core;

import android.os.Bundle;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntryItem;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.math.linearalgebra.Matrix;

/* renamed from: com.samsung.android.spayfw.core.h */
public class PayConfigurator {
    private static Map<String, PayConfigurator> jv;
    private static int jw;
    private static int jx;

    /* renamed from: com.samsung.android.spayfw.core.h.a */
    private static class PayConfigurator {
        int delay;
        Bundle extraParams;
        int jy;
        int leadingZeros;
        boolean reverse;
        int track;
        int trailingZeros;

        public PayConfigurator() {
            this.track = 0;
            this.jy = 0;
            this.delay = 0;
            this.leadingZeros = 0;
            this.trailingZeros = 0;
            this.reverse = false;
            this.extraParams = new Bundle();
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.h.b */
    private static class PayConfigurator {
        PayConfig js;
        int jt;
        String mstSequenceId;

        private PayConfigurator() {
        }
    }

    static {
        jv = new HashMap();
        jw = -1;
        jx = 1;
    }

    public static PayConfig m612b(String str, String str2) {
        if (str == null || str.isEmpty() || str2 == null || str2.isEmpty()) {
            Log.m286e("PayConfigurator", "getPayConfigByType: configType or cardBrand is null or empty!");
            return null;
        } else if (jv == null || jv.get(str) == null) {
            Log.m286e("PayConfigurator", "getPayConfigByType: mTrackDataHash=null or value=null for key = " + str);
            return null;
        } else {
            String str3 = str2 + "_" + str;
            Log.m287i("PayConfigurator", "getPayConfigByType: cardBrandConfigType : " + str3);
            if (jv.containsKey(str3)) {
                return ((PayConfigurator) jv.get(str3)).js;
            }
            return ((PayConfigurator) jv.get(str)).js;
        }
    }

    public static int m613c(String str, String str2) {
        if (str == null || str.isEmpty() || str2 == null || str2.isEmpty()) {
            Log.m286e("PayConfigurator", "getMstTransmitTimeForType: configType or cardBrand is null or empty!");
            return -1;
        } else if (jv == null || jv.get(str) == null) {
            Log.m286e("PayConfigurator", "getMstTransmitTimeForType: mTrackDataHash=null or value=null for key = " + str);
            return -1;
        } else {
            String str3 = str2 + "_" + str;
            if (jv.containsKey(str3)) {
                return ((PayConfigurator) jv.get(str3)).jt;
            }
            return ((PayConfigurator) jv.get(str)).jt;
        }
    }

    public static String m615f(String str, String str2) {
        if (str == null || str.isEmpty() || str2 == null || str2.isEmpty()) {
            Log.m286e("PayConfigurator", "getMstSequenceIdForType: configType or cardBrand is null or empty!");
            return null;
        } else if (jv == null || jv.get(str) == null) {
            Log.m286e("PayConfigurator", "getMstSequenceIdForType: mTrackDataHash=null or value=null for key = " + str);
            return null;
        } else {
            String str3 = str2 + "_" + str;
            if (jv.containsKey(str3)) {
                return ((PayConfigurator) jv.get(str3)).mstSequenceId;
            }
            return ((PayConfigurator) jv.get(str)).mstSequenceId;
        }
    }

    public static int av() {
        return jw;
    }

    public static int aw() {
        return jx;
    }

    public static String[] m609C(String str) {
        if (str == null) {
            return null;
        }
        String[] split = str.replaceAll("\\t\\n\\r\\f", BuildConfig.FLAVOR).trim().split(";");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
        }
        Log.m285d("PayConfigurator", "getSanitizedSequence: sequence.arr = " + Arrays.toString(split) + "; len = " + split.length);
        return split;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.samsung.android.spayfw.appinterface.PayConfig m614c(java.lang.String[] r8) {
        /*
        r1 = 0;
        r3 = new com.samsung.android.spayfw.appinterface.PayConfig;
        r3.<init>();
        r4 = new java.util.ArrayList;
        r4.<init>();
        r0 = new com.samsung.android.spayfw.appinterface.MstPayConfig;
        r0.<init>();
        r0.setMstPayConfigEntry(r4);
        r2 = 2;
        r3.setPayType(r2);
        r3.setMstPayConfig(r0);
        r0 = 1500; // 0x5dc float:2.102E-42 double:7.41E-321;
        r3.setPayIdleTime(r0);
        r0 = r1;
    L_0x0020:
        r2 = r8.length;
        if (r2 <= r0) goto L_0x009a;
    L_0x0023:
        r5 = new java.util.ArrayList;
        r5.<init>();
        r2 = r0;
    L_0x0029:
        r0 = "PayConfigurator";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "getPayConfig: i = ";
        r6 = r6.append(r7);
        r6 = r6.append(r2);
        r7 = "; mMstSequence = ";
        r6 = r6.append(r7);
        r7 = r8[r2];
        r6 = r6.append(r7);
        r6 = r6.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r6);
        r0 = r2 + 1;
        r2 = r8[r2];
        r2 = com.samsung.android.spayfw.core.PayConfigurator.m610D(r2);
        r6 = new com.samsung.android.spayfw.appinterface.MstPayConfigEntryItem;
        r6.<init>();
        r7 = r2.reverse;
        if (r7 != 0) goto L_0x0095;
    L_0x005e:
        r6.setDirection(r1);
    L_0x0061:
        r7 = r2.track;
        r6.setTrackIndex(r7);
        r7 = r2.leadingZeros;
        r6.setLeadingZeros(r7);
        r7 = r2.trailingZeros;
        r6.setTrailingZeros(r7);
        r7 = r2.extraParams;
        r6.setExtraParams(r7);
        r5.add(r6);
        r6 = r2.delay;
        if (r6 != 0) goto L_0x007f;
    L_0x007c:
        r6 = r8.length;
        if (r6 > r0) goto L_0x009b;
    L_0x007f:
        r6 = new com.samsung.android.spayfw.appinterface.MstPayConfigEntry;
        r6.<init>();
        r7 = r2.delay;
        r6.setDelayBetweenRepeat(r7);
        r2 = r2.jy;
        r6.setBaudRate(r2);
        r6.setMstPayConfigEntry(r5);
        r4.add(r6);
        goto L_0x0020;
    L_0x0095:
        r7 = 1;
        r6.setDirection(r7);
        goto L_0x0061;
    L_0x009a:
        return r3;
    L_0x009b:
        r2 = r0;
        goto L_0x0029;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.h.c(java.lang.String[]):com.samsung.android.spayfw.appinterface.PayConfig");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void m616k(android.content.Context r15) {
        /*
        r2 = 0;
        r0 = "PayConfigurator";
        r1 = "preparePayConfigData: Entering function ";
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);
        r1 = "PayConfig.xml";
        r4 = com.samsung.android.spayfw.utils.Utils.fP();
        r0 = "PayConfigurator";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "CC2 : ";
        r3 = r3.append(r5);
        r3 = r3.append(r4);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r3);
        r0 = r15.getAssets();	 Catch:{ IOException -> 0x006d }
        r3 = "";
        r5 = r0.list(r3);	 Catch:{ IOException -> 0x006d }
        r6 = r5.length;	 Catch:{ IOException -> 0x006d }
        r0 = 0;
        r3 = r0;
    L_0x0033:
        if (r3 >= r6) goto L_0x0077;
    L_0x0035:
        r0 = r5[r3];	 Catch:{ IOException -> 0x006d }
        r7 = "PayConfigurator";
        r8 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x006d }
        r8.<init>();	 Catch:{ IOException -> 0x006d }
        r9 = "asset : ";
        r8 = r8.append(r9);	 Catch:{ IOException -> 0x006d }
        r8 = r8.append(r0);	 Catch:{ IOException -> 0x006d }
        r8 = r8.toString();	 Catch:{ IOException -> 0x006d }
        com.samsung.android.spayfw.p002b.Log.m285d(r7, r8);	 Catch:{ IOException -> 0x006d }
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x006d }
        r7.<init>();	 Catch:{ IOException -> 0x006d }
        r7 = r7.append(r4);	 Catch:{ IOException -> 0x006d }
        r8 = ".xml";
        r7 = r7.append(r8);	 Catch:{ IOException -> 0x006d }
        r7 = r7.toString();	 Catch:{ IOException -> 0x006d }
        r7 = r0.endsWith(r7);	 Catch:{ IOException -> 0x006d }
        if (r7 == 0) goto L_0x030b;
    L_0x0068:
        r1 = r3 + 1;
        r3 = r1;
        r1 = r0;
        goto L_0x0033;
    L_0x006d:
        r0 = move-exception;
        r3 = "PayConfigurator";
        r4 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r3, r4, r0);
    L_0x0077:
        r0 = "PayConfigurator";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "payConfigFileName : ";
        r3 = r3.append(r4);
        r3 = r3.append(r1);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r3);
        r0 = r15.getAssets();	 Catch:{ XmlPullParserException -> 0x0146, IOException -> 0x02e3, Exception -> 0x02f4 }
        r7 = r0.open(r1);	 Catch:{ XmlPullParserException -> 0x0146, IOException -> 0x02e3, Exception -> 0x02f4 }
        r8 = 0;
        r0 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r9 = r0.newPullParser();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = 0;
        r9.setInput(r7, r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = r9.getEventType();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r3 = r2;
        r4 = r2;
        r5 = r2;
        r6 = r2;
    L_0x00ac:
        r1 = 1;
        if (r0 == r1) goto L_0x02d4;
    L_0x00af:
        switch(r0) {
            case 2: goto L_0x00bf;
            case 3: goto L_0x00b2;
            case 4: goto L_0x01e3;
            default: goto L_0x00b2;
        };	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
    L_0x00b2:
        r1 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r6;
    L_0x00b6:
        r0 = r9.next();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r6 = r5;
        r5 = r4;
        r4 = r3;
        r3 = r1;
        goto L_0x00ac;
    L_0x00bf:
        r6 = r9.getName();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = "PayConfigurator";
        r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1.<init>();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = "preparePayConfigData: start_tag -> tag = ";
        r1 = r1.append(r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r1.append(r6);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r1.toString();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = r9.getAttributeCount();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = "PayConfigurator";
        r10 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10.<init>();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r11 = "preparePayConfigData: start_tag -> attrCount = ";
        r10 = r10.append(r11);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = r10.append(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = r0.toString();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        if (r6 == 0) goto L_0x0151;
    L_0x00f9:
        r0 = "mstRetryType";
        r0 = r6.equals(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        if (r0 == 0) goto L_0x0151;
    L_0x0101:
        r0 = 0;
        r1 = "value";
        r0 = r9.getAttributeValue(r0, r1);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = "PayConfigurator";
        r10 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10.<init>();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r11 = "preparePayConfigData: start_tag -> attrVal = ";
        r10 = r10.append(r11);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = r10.append(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = r10.toString();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        if (r0 == 0) goto L_0x012e;
    L_0x0122:
        r1 = r0.isEmpty();	 Catch:{ NumberFormatException -> 0x0133 }
        if (r1 != 0) goto L_0x012e;
    L_0x0128:
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x0133 }
        jx = r0;	 Catch:{ NumberFormatException -> 0x0133 }
    L_0x012e:
        r1 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r6;
        goto L_0x00b6;
    L_0x0133:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        goto L_0x012e;
    L_0x0138:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x013a }
    L_0x013a:
        r1 = move-exception;
        r14 = r1;
        r1 = r0;
        r0 = r14;
    L_0x013e:
        if (r7 == 0) goto L_0x0145;
    L_0x0140:
        if (r1 == 0) goto L_0x0306;
    L_0x0142:
        r7.close();	 Catch:{ Throwable -> 0x0300 }
    L_0x0145:
        throw r0;	 Catch:{ XmlPullParserException -> 0x0146, IOException -> 0x02e3, Exception -> 0x02f4 }
    L_0x0146:
        r0 = move-exception;
        r1 = "PayConfigurator";
        r2 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);
    L_0x0150:
        return;
    L_0x0151:
        if (r6 == 0) goto L_0x0196;
    L_0x0153:
        r0 = "retryDelay";
        r0 = r6.equals(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        if (r0 == 0) goto L_0x0196;
    L_0x015b:
        r0 = 0;
        r1 = "value";
        r0 = r9.getAttributeValue(r0, r1);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = "PayConfigurator";
        r10 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10.<init>();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r11 = "preparePayConfigData: start_tag -> attrVal = ";
        r10 = r10.append(r11);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = r10.append(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = r10.toString();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        if (r0 == 0) goto L_0x0188;
    L_0x017c:
        r1 = r0.isEmpty();	 Catch:{ NumberFormatException -> 0x018e }
        if (r1 != 0) goto L_0x0188;
    L_0x0182:
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x018e }
        jw = r0;	 Catch:{ NumberFormatException -> 0x018e }
    L_0x0188:
        r1 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r6;
        goto L_0x00b6;
    L_0x018e:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        goto L_0x0188;
    L_0x0193:
        r0 = move-exception;
        r1 = r2;
        goto L_0x013e;
    L_0x0196:
        r0 = 0;
        r1 = "transmitTime";
        r5 = r9.getAttributeValue(r0, r1);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = 0;
        r1 = "idleTime";
        r4 = r9.getAttributeValue(r0, r1);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = 0;
        r1 = "mstSequenceId";
        r3 = r9.getAttributeValue(r0, r1);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = "PayConfigurator";
        r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1.<init>();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = "preparePayConfigData: start_tag -> transmitTime = ";
        r1 = r1.append(r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r1.append(r5);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = "; idleTime = ";
        r1 = r1.append(r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r1.append(r4);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = "; ";
        r1 = r1.append(r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = "mstSequenceId = ";
        r1 = r1.append(r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r1.append(r3);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r1.toString();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r6;
        goto L_0x00b6;
    L_0x01e3:
        r0 = "PayConfigurator";
        r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1.<init>();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = "preparePayConfigData: text -> tag = ";
        r1 = r1.append(r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r1.append(r6);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = "; transmitTime";
        r1 = r1.append(r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = " = ";
        r1 = r1.append(r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r1.append(r5);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = "; idleTime = ";
        r1 = r1.append(r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r1.append(r4);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r1.toString();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        if (r6 == 0) goto L_0x00b2;
    L_0x0217:
        r0 = "retryDelay";
        r0 = r6.equals(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        if (r0 != 0) goto L_0x00b2;
    L_0x021f:
        r0 = r9.getText();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = "PayConfigurator";
        r10 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10.<init>();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r11 = "preparePayConfigData: seq = ";
        r10 = r10.append(r11);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = r10.append(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10 = r10.toString();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = com.samsung.android.spayfw.core.PayConfigurator.m609C(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        if (r0 == 0) goto L_0x00b2;
    L_0x0241:
        r1 = r0.length;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        if (r1 <= 0) goto L_0x00b2;
    L_0x0244:
        r10 = new com.samsung.android.spayfw.core.h$b;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = 0;
        r10.<init>();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = com.samsung.android.spayfw.core.PayConfigurator.m614c(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10.js = r0;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = -1;
        if (r5 == 0) goto L_0x025d;
    L_0x0253:
        r1 = r5.isEmpty();	 Catch:{ NumberFormatException -> 0x02af }
        if (r1 != 0) goto L_0x025d;
    L_0x0259:
        r0 = java.lang.Integer.parseInt(r5);	 Catch:{ NumberFormatException -> 0x02af }
    L_0x025d:
        r1 = "PayConfigurator";
        r11 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r11.<init>();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r12 = "preparePayConfigData: transmitTime = ";
        r11 = r11.append(r12);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r11 = r11.append(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r11 = r11.toString();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r11);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r10.jt = r0;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = 1500; // 0x5dc float:2.102E-42 double:7.41E-321;
        if (r4 == 0) goto L_0x0285;
    L_0x027b:
        r1 = r4.isEmpty();	 Catch:{ NumberFormatException -> 0x02cf }
        if (r1 != 0) goto L_0x0285;
    L_0x0281:
        r0 = java.lang.Integer.parseInt(r4);	 Catch:{ NumberFormatException -> 0x02cf }
    L_0x0285:
        r1 = "PayConfigurator";
        r11 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r11.<init>();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r12 = "preparePayConfigData: idleTime = ";
        r11 = r11.append(r12);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r11 = r11.append(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r11 = r11.toString();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r11);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r10.js;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        if (r1 == 0) goto L_0x02a6;
    L_0x02a1:
        r1 = r10.js;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1.setPayIdleTime(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
    L_0x02a6:
        r10.mstSequenceId = r3;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = jv;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0.put(r6, r10);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        goto L_0x00b2;
    L_0x02af:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r1 = r10.js;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        if (r1 == 0) goto L_0x025d;
    L_0x02b7:
        r0 = r10.js;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = r0.getMstPayConfig();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = r0.getMstPayConfigEntry();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = r0.size();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = (double) r0;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r12 = 4608308318706860032; // 0x3ff4000000000000 float:0.0 double:1.25;
        r0 = r0 * r12;
        r0 = java.lang.Math.ceil(r0);	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        r0 = (int) r0;	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        goto L_0x025d;
    L_0x02cf:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ Throwable -> 0x0138, all -> 0x0193 }
        goto L_0x0285;
    L_0x02d4:
        if (r7 == 0) goto L_0x0150;
    L_0x02d6:
        if (r2 == 0) goto L_0x02ef;
    L_0x02d8:
        r7.close();	 Catch:{ Throwable -> 0x02dd }
        goto L_0x0150;
    L_0x02dd:
        r0 = move-exception;
        r8.addSuppressed(r0);	 Catch:{ XmlPullParserException -> 0x0146, IOException -> 0x02e3, Exception -> 0x02f4 }
        goto L_0x0150;
    L_0x02e3:
        r0 = move-exception;
        r1 = "PayConfigurator";
        r2 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);
        goto L_0x0150;
    L_0x02ef:
        r7.close();	 Catch:{ XmlPullParserException -> 0x0146, IOException -> 0x02e3, Exception -> 0x02f4 }
        goto L_0x0150;
    L_0x02f4:
        r0 = move-exception;
        r1 = "PayConfigurator";
        r2 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);
        goto L_0x0150;
    L_0x0300:
        r2 = move-exception;
        r1.addSuppressed(r2);	 Catch:{ XmlPullParserException -> 0x0146, IOException -> 0x02e3, Exception -> 0x02f4 }
        goto L_0x0145;
    L_0x0306:
        r7.close();	 Catch:{ XmlPullParserException -> 0x0146, IOException -> 0x02e3, Exception -> 0x02f4 }
        goto L_0x0145;
    L_0x030b:
        r0 = r1;
        goto L_0x0068;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.h.k(android.content.Context):void");
    }

    private static PayConfigurator m610D(String str) {
        String substring = str.substring(0, str.length() - 1);
        Log.m285d("PayConfigurator", "mstConfig :" + substring);
        PayConfigurator payConfigurator = new PayConfigurator();
        String[] split = substring.split(",");
        for (String substring2 : split) {
            String substring22 = substring22.substring(1);
            switch (substring22.charAt(0)) {
                case CipherSuite.TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA /*68*/:
                    payConfigurator.delay = Integer.parseInt(substring22.substring(1));
                    break;
                case EACTags.CERTIFICATE_HOLDER_AUTHORIZATION_TEMPLATE /*76*/:
                    if (substring22.charAt(1) == Matrix.MATRIX_TYPE_ZERO) {
                        payConfigurator.leadingZeros = Integer.parseInt(substring22.substring(2));
                    }
                    if (substring22.charAt(1) != 'P') {
                        if (substring22.charAt(1) != 'N') {
                            if (substring22.charAt(1) != 'D') {
                                break;
                            }
                            payConfigurator.extraParams.putInt(MstPayConfigEntryItem.EXTRA_PARAMS_KEY_DATA_LENGTH, Integer.parseInt(substring22.substring(2)));
                            Log.m285d("PayConfigurator", "Data Length:" + Integer.parseInt(substring22.substring(2)));
                            break;
                        }
                        payConfigurator.extraParams.putInt(MstPayConfigEntryItem.EXTRA_PARAMS_KEY_NAME_LENGTH, Integer.parseInt(substring22.substring(2)));
                        Log.m285d("PayConfigurator", "Name Length:" + Integer.parseInt(substring22.substring(2)));
                        break;
                    }
                    payConfigurator.extraParams.putInt(MstPayConfigEntryItem.EXTRA_PARAMS_KEY_PAN_LENGTH, Integer.parseInt(substring22.substring(2)));
                    Log.m285d("PayConfigurator", "PAN Length:" + Integer.parseInt(substring22.substring(2)));
                    break;
                case EACTags.COMMAND_TO_PERFORM /*82*/:
                    payConfigurator.reverse = true;
                    break;
                case EACTags.OFFSET_DATA_OBJECT /*84*/:
                    if (substring22.charAt(1) != Matrix.MATRIX_TYPE_ZERO) {
                        break;
                    }
                    payConfigurator.trailingZeros = Integer.parseInt(substring22.substring(2));
                    break;
                case 'r':
                    payConfigurator.jy = Integer.parseInt(substring22.substring(1));
                    break;
                case 't':
                    payConfigurator.track = PayConfigurator.m611E(substring22);
                    Log.m285d("PayConfigurator", "Track: " + payConfigurator.track);
                    break;
                default:
                    break;
            }
        }
        return payConfigurator;
    }

    private static int m611E(String str) {
        if (str.length() <= 2 || str.charAt(1) != LLVARUtil.PLAIN_TEXT) {
            return Integer.parseInt(str.substring(1));
        }
        Object obj = -1;
        switch (str.hashCode()) {
            case 113105:
                if (str.equals("t1n")) {
                    obj = null;
                    break;
                }
                break;
        }
        switch (obj) {
            case ECCurve.COORD_AFFINE /*0*/:
                return GF2Field.MASK;
            default:
                Log.m290w("PayConfigurator", "getTrackValue: incorrect track, defaulting to track 1 : " + str);
                return 1;
        }
    }
}
