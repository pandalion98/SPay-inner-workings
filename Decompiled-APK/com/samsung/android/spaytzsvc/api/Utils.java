package com.samsung.android.spaytzsvc.api;

import com.samsung.android.spayfw.p002b.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {
    private static final String TAG = "spaytzsvc.api.Utils";

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] readFile(java.lang.String r9) {
        /*
        r2 = 0;
        r0 = new java.io.File;
        r0.<init>(r9);
        r1 = "spaytzsvc.api.Utils";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "In readFile - Path ";
        r3 = r3.append(r4);
        r3 = r3.append(r9);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r3);
        r1 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x0064, all -> 0x0079 }
        r1.<init>(r0);	 Catch:{ Exception -> 0x0064, all -> 0x0079 }
        r3 = "spaytzsvc.api.Utils";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x008f, all -> 0x008a }
        r4.<init>();	 Catch:{ Exception -> 0x008f, all -> 0x008a }
        r5 = "File Read - Length = ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x008f, all -> 0x008a }
        r6 = r0.length();	 Catch:{ Exception -> 0x008f, all -> 0x008a }
        r4 = r4.append(r6);	 Catch:{ Exception -> 0x008f, all -> 0x008a }
        r4 = r4.toString();	 Catch:{ Exception -> 0x008f, all -> 0x008a }
        com.samsung.android.spayfw.p002b.Log.m285d(r3, r4);	 Catch:{ Exception -> 0x008f, all -> 0x008a }
        r4 = r0.length();	 Catch:{ Exception -> 0x008f, all -> 0x008a }
        r0 = (int) r4;	 Catch:{ Exception -> 0x008f, all -> 0x008a }
        r3 = new byte[r0];	 Catch:{ Exception -> 0x008f, all -> 0x008a }
        r0 = r1.read(r3);	 Catch:{ Exception -> 0x0095, all -> 0x008a }
        r4 = r3.length;	 Catch:{ Exception -> 0x0095, all -> 0x008a }
        if (r0 == r4) goto L_0x009a;
    L_0x004d:
        r0 = "spaytzsvc.api.Utils";
        r4 = "File Read Failed";
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r4);	 Catch:{ Exception -> 0x0095, all -> 0x008a }
        r0 = r2;
    L_0x0055:
        if (r1 == 0) goto L_0x005a;
    L_0x0057:
        r1.close();	 Catch:{ IOException -> 0x005b }
    L_0x005a:
        return r0;
    L_0x005b:
        r1 = move-exception;
        r1 = "spaytzsvc.api.Utils";
        r2 = "Error closing InputStream";
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
        goto L_0x005a;
    L_0x0064:
        r0 = move-exception;
        r1 = r0;
        r0 = r2;
    L_0x0067:
        r1.printStackTrace();	 Catch:{ all -> 0x008c }
        if (r2 == 0) goto L_0x005a;
    L_0x006c:
        r2.close();	 Catch:{ IOException -> 0x0070 }
        goto L_0x005a;
    L_0x0070:
        r1 = move-exception;
        r1 = "spaytzsvc.api.Utils";
        r2 = "Error closing InputStream";
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
        goto L_0x005a;
    L_0x0079:
        r0 = move-exception;
        r1 = r2;
    L_0x007b:
        if (r1 == 0) goto L_0x0080;
    L_0x007d:
        r1.close();	 Catch:{ IOException -> 0x0081 }
    L_0x0080:
        throw r0;
    L_0x0081:
        r1 = move-exception;
        r1 = "spaytzsvc.api.Utils";
        r2 = "Error closing InputStream";
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
        goto L_0x0080;
    L_0x008a:
        r0 = move-exception;
        goto L_0x007b;
    L_0x008c:
        r0 = move-exception;
        r1 = r2;
        goto L_0x007b;
    L_0x008f:
        r0 = move-exception;
        r8 = r0;
        r0 = r2;
        r2 = r1;
        r1 = r8;
        goto L_0x0067;
    L_0x0095:
        r0 = move-exception;
        r2 = r1;
        r1 = r0;
        r0 = r3;
        goto L_0x0067;
    L_0x009a:
        r0 = r3;
        goto L_0x0055;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytzsvc.api.Utils.readFile(java.lang.String):byte[]");
    }

    public static boolean writeFile(byte[] bArr, String str) {
        FileOutputStream fileOutputStream;
        Exception e;
        Throwable th;
        boolean z = false;
        Log.m285d(TAG, "Out writeFile - Path " + str);
        File file = new File(str);
        File parentFile = file.getParentFile();
        if (!(parentFile == null || parentFile.exists())) {
            boolean mkdirs = parentFile.mkdirs();
            if (mkdirs) {
                z = mkdirs;
            } else {
                Log.m286e(TAG, "Error: mkdirs fail for " + parentFile.getAbsolutePath());
                return z;
            }
        }
        try {
            fileOutputStream = new FileOutputStream(file);
            try {
                Log.m285d(TAG, "File Write - Length = " + bArr.length);
                fileOutputStream.write(bArr);
                z = true;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e2) {
                        Log.m285d(TAG, "Error closing OutputStream");
                    }
                }
            } catch (Exception e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e4) {
                            Log.m285d(TAG, "Error closing OutputStream");
                        }
                    }
                    return z;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e5) {
                            Log.m285d(TAG, "Error closing OutputStream");
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e6) {
            e = e6;
            fileOutputStream = null;
            e.printStackTrace();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            return z;
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
        return z;
    }
}
