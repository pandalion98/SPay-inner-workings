package com.samsung.android.spayfw.utils;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class GLDManager {
    private static GLDManager CW;
    private Context mContext;

    private static class AddressEntry {
        String port;
        String url;

        private AddressEntry() {
        }
    }

    private static class Entry {
        PfEntry PF;

        private Entry() {
        }
    }

    private static class GldResponse {
        Entry DEV;
        Entry PRD;
        Entry PRE;
        Entry STG;

        private GldResponse() {
        }
    }

    private static class PfEntry {
        AddressEntry pri;
        AddressEntry sec;

        private PfEntry() {
        }
    }

    public static synchronized GLDManager af(Context context) {
        GLDManager gLDManager;
        synchronized (GLDManager.class) {
            if (CW == null) {
                CW = new GLDManager(context);
            }
            gLDManager = CW;
        }
        return gLDManager;
    }

    private static final String m1259d(Context context, String str) {
        return context.getSharedPreferences("GLDPrefs", 0).getString(str, null);
    }

    private static final void m1258a(Context context, String str, String str2) {
        Editor edit = context.getSharedPreferences("GLDPrefs", 0).edit();
        if (str2 != null) {
            edit.putString(str, str2).commit();
        } else {
            edit.remove(str).commit();
        }
    }

    private GLDManager(Context context) {
        this.mContext = context;
    }

    public final synchronized void fG() {
        if (m1259d(this.mContext, "TrUrl1") != null) {
            m1258a(this.mContext, "TrUrl1", null);
        } else if (m1259d(this.mContext, "TrUrl2") != null) {
            m1258a(this.mContext, "TrUrl2", null);
        }
    }

    public final synchronized String by(String str) {
        String d;
        d = m1259d(this.mContext, "TrUrl1");
        if (d == null) {
            d = m1259d(this.mContext, "TrUrl2");
            if (d == null) {
                d = m1260e(this.mContext, str);
            }
        }
        return d;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String m1260e(android.content.Context r12, java.lang.String r13) {
        /*
        r11 = this;
        r0 = 0;
        r1 = 0;
        r2 = com.samsung.android.spayfw.utils.Utils.fP();
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "https://gld.push.samsungosp.com:443/gld/sPay?iso=";
        r3 = r3.append(r4);
        r2 = r3.append(r2);
        r5 = r2.toString();
        r2 = "GLDManager";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "GLD Server : ";
        r3 = r3.append(r4);
        r3 = r3.append(r5);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r2, r3);
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r4 = r0;
    L_0x0037:
        r0 = 2;
        if (r4 > r0) goto L_0x00c8;
    L_0x003a:
        r0 = new java.net.URL;	 Catch:{ Exception -> 0x0180 }
        r0.<init>(r5);	 Catch:{ Exception -> 0x0180 }
        r2 = r0.getProtocol();	 Catch:{ Exception -> 0x0180 }
        r2 = r2.toLowerCase();	 Catch:{ Exception -> 0x0180 }
        r3 = "https";
        r2 = r2.equals(r3);	 Catch:{ Exception -> 0x0180 }
        if (r2 == 0) goto L_0x0150;
    L_0x004f:
        r2 = "TLS";
        r2 = javax.net.ssl.SSLContext.getInstance(r2);	 Catch:{ Exception -> 0x0180 }
        r3 = 0;
        r7 = 0;
        r8 = new java.security.SecureRandom;	 Catch:{ Exception -> 0x0180 }
        r8.<init>();	 Catch:{ Exception -> 0x0180 }
        r2.init(r3, r7, r8);	 Catch:{ Exception -> 0x0180 }
        r0 = r0.openConnection();	 Catch:{ Exception -> 0x0180 }
        r0 = (javax.net.ssl.HttpsURLConnection) r0;	 Catch:{ Exception -> 0x0180 }
        r3 = "GET";
        r0.setRequestMethod(r3);	 Catch:{ Exception -> 0x0180 }
        r2 = r2.getSocketFactory();	 Catch:{ Exception -> 0x0180 }
        r0.setSSLSocketFactory(r2);	 Catch:{ Exception -> 0x0180 }
        r3 = r0;
    L_0x0072:
        if (r3 == 0) goto L_0x01bf;
    L_0x0074:
        r0 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r3.setConnectTimeout(r0);	 Catch:{ Exception -> 0x0180 }
        r0 = 0;
        r3.setUseCaches(r0);	 Catch:{ Exception -> 0x0180 }
        r0 = r3.getResponseCode();	 Catch:{ Exception -> 0x0180 }
        r2 = "GLDManager";
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0180 }
        r7.<init>();	 Catch:{ Exception -> 0x0180 }
        r8 = "Status Code : ";
        r7 = r7.append(r8);	 Catch:{ Exception -> 0x0180 }
        r0 = r7.append(r0);	 Catch:{ Exception -> 0x0180 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0180 }
        com.samsung.android.spayfw.p002b.Log.m285d(r2, r0);	 Catch:{ Exception -> 0x0180 }
        r0 = r3.getResponseCode();	 Catch:{ Exception -> 0x0180 }
        r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r0 != r2) goto L_0x00c5;
    L_0x00a1:
        r7 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x017a }
        r0 = r3.getInputStream();	 Catch:{ Exception -> 0x017a }
        r7.<init>(r0);	 Catch:{ Exception -> 0x017a }
        r0 = 0;
        r8 = new java.io.BufferedReader;	 Catch:{ Throwable -> 0x016c, all -> 0x019a }
        r8.<init>(r7);	 Catch:{ Throwable -> 0x016c, all -> 0x019a }
        r2 = 0;
    L_0x00b1:
        r9 = r8.readLine();	 Catch:{ Throwable -> 0x015e, all -> 0x021f }
        if (r9 != 0) goto L_0x0159;
    L_0x00b7:
        if (r8 == 0) goto L_0x00be;
    L_0x00b9:
        if (r1 == 0) goto L_0x019d;
    L_0x00bb:
        r8.close();	 Catch:{ Throwable -> 0x0194, all -> 0x019a }
    L_0x00be:
        if (r7 == 0) goto L_0x00c5;
    L_0x00c0:
        if (r1 == 0) goto L_0x01b1;
    L_0x00c2:
        r7.close();	 Catch:{ Throwable -> 0x01ab }
    L_0x00c5:
        r3.disconnect();	 Catch:{ Exception -> 0x0180 }
    L_0x00c8:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r2 = "http://abc.com/gld?";
        r0 = r0.append(r2);
        r2 = r6.toString();
        r0 = r0.append(r2);
        r0 = r0.toString();
        r0 = android.net.Uri.parse(r0);
        r2 = "serverUrl";
        r0 = r0.getQueryParameter(r2);	 Catch:{ Exception -> 0x01e7 }
        r2 = "UTF-8";
        r0 = java.net.URLDecoder.decode(r0, r2);	 Catch:{ Exception -> 0x01e7 }
        r2 = "GLDManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01e7 }
        r3.<init>();	 Catch:{ Exception -> 0x01e7 }
        r4 = "Server Url : ";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x01e7 }
        r3 = r3.append(r0);	 Catch:{ Exception -> 0x01e7 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01e7 }
        com.samsung.android.spayfw.p002b.Log.m285d(r2, r3);	 Catch:{ Exception -> 0x01e7 }
        r2 = new com.google.gson.GsonBuilder;	 Catch:{ Exception -> 0x01e7 }
        r2.<init>();	 Catch:{ Exception -> 0x01e7 }
        r2 = r2.disableHtmlEscaping();	 Catch:{ Exception -> 0x01e7 }
        r2 = r2.create();	 Catch:{ Exception -> 0x01e7 }
        r3 = com.samsung.android.spayfw.utils.GLDManager.GldResponse.class;
        r0 = r2.fromJson(r0, r3);	 Catch:{ Exception -> 0x01e7 }
        r0 = (com.samsung.android.spayfw.utils.GLDManager.GldResponse) r0;	 Catch:{ Exception -> 0x01e7 }
        r2 = "GLDManager";
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01e7 }
        r3.<init>();	 Catch:{ Exception -> 0x01e7 }
        r4 = "GldResponse : ";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x01e7 }
        r4 = r0.toString();	 Catch:{ Exception -> 0x01e7 }
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x01e7 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01e7 }
        com.samsung.android.spayfw.p002b.Log.m285d(r2, r3);	 Catch:{ Exception -> 0x01e7 }
        r2 = "PROD";
        r2 = r13.equals(r2);	 Catch:{ Exception -> 0x01e7 }
        if (r2 == 0) goto L_0x01d6;
    L_0x0140:
        r0 = r0.PRD;	 Catch:{ Exception -> 0x01e7 }
        r0 = r0.PF;	 Catch:{ Exception -> 0x01e7 }
        r11.m1257a(r12, r0);	 Catch:{ Exception -> 0x01e7 }
    L_0x0147:
        r0 = "TrUrl1";
        r0 = m1259d(r12, r0);
        if (r0 == 0) goto L_0x0215;
    L_0x014f:
        return r0;
    L_0x0150:
        r0 = r0.openConnection();	 Catch:{ Exception -> 0x0180 }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x0180 }
        r3 = r0;
        goto L_0x0072;
    L_0x0159:
        r6.append(r9);	 Catch:{ Throwable -> 0x015e, all -> 0x021f }
        goto L_0x00b1;
    L_0x015e:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0160 }
    L_0x0160:
        r2 = move-exception;
        r10 = r2;
        r2 = r0;
        r0 = r10;
    L_0x0164:
        if (r8 == 0) goto L_0x016b;
    L_0x0166:
        if (r2 == 0) goto L_0x01a7;
    L_0x0168:
        r8.close();	 Catch:{ Throwable -> 0x01a2, all -> 0x019a }
    L_0x016b:
        throw r0;	 Catch:{ Throwable -> 0x016c, all -> 0x019a }
    L_0x016c:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x016e }
    L_0x016e:
        r2 = move-exception;
        r10 = r2;
        r2 = r0;
        r0 = r10;
    L_0x0172:
        if (r7 == 0) goto L_0x0179;
    L_0x0174:
        if (r2 == 0) goto L_0x01bb;
    L_0x0176:
        r7.close();	 Catch:{ Throwable -> 0x01b6 }
    L_0x0179:
        throw r0;	 Catch:{ Exception -> 0x017a }
    L_0x017a:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ Exception -> 0x0180 }
        goto L_0x00c5;
    L_0x0180:
        r0 = move-exception;
        r2 = "GLDManager";
        r3 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r3, r0);
        r2 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        java.lang.Thread.sleep(r2);	 Catch:{ InterruptedException -> 0x01d1 }
    L_0x018f:
        r0 = r4 + 1;
        r4 = r0;
        goto L_0x0037;
    L_0x0194:
        r8 = move-exception;
        r2.addSuppressed(r8);	 Catch:{ Throwable -> 0x016c, all -> 0x019a }
        goto L_0x00be;
    L_0x019a:
        r0 = move-exception;
        r2 = r1;
        goto L_0x0172;
    L_0x019d:
        r8.close();	 Catch:{ Throwable -> 0x016c, all -> 0x019a }
        goto L_0x00be;
    L_0x01a2:
        r8 = move-exception;
        r2.addSuppressed(r8);	 Catch:{ Throwable -> 0x016c, all -> 0x019a }
        goto L_0x016b;
    L_0x01a7:
        r8.close();	 Catch:{ Throwable -> 0x016c, all -> 0x019a }
        goto L_0x016b;
    L_0x01ab:
        r2 = move-exception;
        r0.addSuppressed(r2);	 Catch:{ Exception -> 0x017a }
        goto L_0x00c5;
    L_0x01b1:
        r7.close();	 Catch:{ Exception -> 0x017a }
        goto L_0x00c5;
    L_0x01b6:
        r7 = move-exception;
        r2.addSuppressed(r7);	 Catch:{ Exception -> 0x017a }
        goto L_0x0179;
    L_0x01bb:
        r7.close();	 Catch:{ Exception -> 0x017a }
        goto L_0x0179;
    L_0x01bf:
        r0 = "GLDManager";
        r2 = "No Connection to GLD";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ Exception -> 0x0180 }
        r2 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        java.lang.Thread.sleep(r2);	 Catch:{ InterruptedException -> 0x01cc }
        goto L_0x018f;
    L_0x01cc:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ Exception -> 0x0180 }
        goto L_0x018f;
    L_0x01d1:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x018f;
    L_0x01d6:
        r2 = "STG";
        r2 = r13.equals(r2);	 Catch:{ Exception -> 0x01e7 }
        if (r2 == 0) goto L_0x01f3;
    L_0x01de:
        r0 = r0.STG;	 Catch:{ Exception -> 0x01e7 }
        r0 = r0.PF;	 Catch:{ Exception -> 0x01e7 }
        r11.m1257a(r12, r0);	 Catch:{ Exception -> 0x01e7 }
        goto L_0x0147;
    L_0x01e7:
        r0 = move-exception;
        r2 = "GLDManager";
        r0 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r0);
        goto L_0x0147;
    L_0x01f3:
        r2 = "PRE";
        r2 = r13.equals(r2);	 Catch:{ Exception -> 0x01e7 }
        if (r2 == 0) goto L_0x0204;
    L_0x01fb:
        r0 = r0.PRE;	 Catch:{ Exception -> 0x01e7 }
        r0 = r0.PF;	 Catch:{ Exception -> 0x01e7 }
        r11.m1257a(r12, r0);	 Catch:{ Exception -> 0x01e7 }
        goto L_0x0147;
    L_0x0204:
        r2 = "DEV";
        r2 = r13.equals(r2);	 Catch:{ Exception -> 0x01e7 }
        if (r2 == 0) goto L_0x0147;
    L_0x020c:
        r0 = r0.DEV;	 Catch:{ Exception -> 0x01e7 }
        r0 = r0.PF;	 Catch:{ Exception -> 0x01e7 }
        r11.m1257a(r12, r0);	 Catch:{ Exception -> 0x01e7 }
        goto L_0x0147;
    L_0x0215:
        r0 = "GLDManager";
        r2 = "fetchTokenRequesterUrl returns null";
        com.samsung.android.spayfw.p002b.Log.m290w(r0, r2);
        r0 = r1;
        goto L_0x014f;
    L_0x021f:
        r0 = move-exception;
        r2 = r1;
        goto L_0x0164;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.utils.GLDManager.e(android.content.Context, java.lang.String):java.lang.String");
    }

    private void m1257a(Context context, PfEntry pfEntry) {
        if (pfEntry.pri.port.equals("443")) {
            StringBuilder stringBuilder = new StringBuilder("https://");
            stringBuilder.append(pfEntry.pri.url).append(":").append(pfEntry.pri.port);
            m1258a(context, "TrUrl1", stringBuilder.toString());
        }
        if (pfEntry.sec.port.equals("443")) {
            stringBuilder = new StringBuilder("https://");
            stringBuilder.append(pfEntry.sec.url).append(":").append(pfEntry.sec.port);
            m1258a(context, "TrUrl2", stringBuilder.toString());
        }
    }
}
