package com.samsung.contextservice.p029b;

import com.samsung.android.spayfw.p002b.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

/* renamed from: com.samsung.contextservice.b.b */
public final class CSlog {
    private static volatile boolean HJ;

    static {
        HJ = Config.HK;
    }

    private static boolean m1407c(String str, int i) {
        if (str.length() > 28 - "CTX_".length()) {
            if (!HJ) {
                Log.m285d("CTX_CS", str);
            }
            str.substring(0, 28 - "CTX_".length());
        }
        if (i == 5 || i == 2 || !HJ) {
            return true;
        }
        return false;
    }

    private static void m1400a(int i, String str, String str2) {
        if (CSlog.m1407c(str, i)) {
            CSlog.m1404b(i, str, str2);
        }
    }

    private static void m1401a(int i, String str, Throwable th, String str2) {
        if (CSlog.m1407c(str, i)) {
            CSlog.m1405b(i, str, th, str2);
        }
    }

    private static void m1402a(StringWriter stringWriter, Throwable th) {
        th.printStackTrace(new PrintWriter(stringWriter));
        stringWriter.flush();
    }

    private static void m1404b(int i, String str, String str2) {
        String str3 = "CTX_" + str;
        switch (i) {
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                Log.m286e(str3, str2);
            case F2m.PPB /*3*/:
                Log.m290w(str3, str2);
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                Log.m289v(str3, str2);
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                Log.m287i(str3, str2);
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                Log.m285d(str3, str2);
            default:
        }
    }

    private static void m1405b(int i, String str, Throwable th, String str2) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.append(str2);
        stringWriter.append('\n');
        CSlog.m1402a(stringWriter, th);
        CSlog.m1404b(i, str, stringWriter.toString());
    }

    public static void m1409e(String str, String str2) {
        CSlog.m1400a(2, str, str2);
    }

    public static void m1410i(String str, String str2) {
        CSlog.m1400a(5, str, str2);
    }

    public static void m1408d(String str, String str2) {
        CSlog.m1400a(6, str, str2);
    }

    public static void m1406c(String str, String str2, Throwable th) {
        CSlog.m1401a(2, str, th, str2);
    }

    public static void m1403a(String str, String str2, Throwable th) {
        CSlog.m1401a(6, str, th, str2);
    }
}
