package com.samsung.android.spayfw.p002b;

import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayfw.b.c */
public final class Log {
    private static ArrayList<Logger> oJ;

    static {
        oJ = new ArrayList();
    }

    public static synchronized boolean m282a(Logger logger) {
        boolean z = false;
        synchronized (Log.class) {
            if (logger != null) {
                if (!oJ.contains(logger)) {
                    z = oJ.add(logger);
                }
            }
        }
        return z;
    }

    public static synchronized Logger an(String str) {
        Logger logger;
        synchronized (Log.class) {
            if (str == null) {
                logger = null;
            } else {
                Iterator it = oJ.iterator();
                while (it.hasNext()) {
                    logger = (Logger) it.next();
                    if (logger.oK.equals(str)) {
                        break;
                    }
                }
                logger = null;
            }
        }
        return logger;
    }

    public static void m289v(String str, String str2) {
        Iterator it = oJ.iterator();
        while (it.hasNext()) {
            ((Logger) it.next()).m271a(3, str, str2);
        }
    }

    public static void m285d(String str, String str2) {
        Iterator it = oJ.iterator();
        while (it.hasNext()) {
            ((Logger) it.next()).m271a(2, str, str2);
        }
    }

    public static void m287i(String str, String str2) {
        Iterator it = oJ.iterator();
        while (it.hasNext()) {
            ((Logger) it.next()).m271a(4, str, str2);
        }
    }

    public static void m290w(String str, String str2) {
        Iterator it = oJ.iterator();
        while (it.hasNext()) {
            ((Logger) it.next()).m271a(5, str, str2);
        }
    }

    public static void m286e(String str, String str2) {
        Iterator it = oJ.iterator();
        while (it.hasNext()) {
            ((Logger) it.next()).m271a(6, str, str2);
        }
    }

    public static void m288m(String str, String str2) {
        Iterator it = oJ.iterator();
        while (it.hasNext()) {
            ((Logger) it.next()).m271a(1, str, str2);
        }
    }

    public static void m281a(String str, String str2, Throwable th) {
        Iterator it = oJ.iterator();
        while (it.hasNext()) {
            ((Logger) it.next()).m271a(6, str, str2 + '\n' + Log.getStackTraceString(th));
        }
    }

    public static void m283b(String str, String str2, Throwable th) {
        Iterator it = oJ.iterator();
        while (it.hasNext()) {
            ((Logger) it.next()).m271a(6, str, str2 + '\n' + Log.getStackTraceString(th));
        }
    }

    public static void m284c(String str, String str2, Throwable th) {
        Iterator it = oJ.iterator();
        while (it.hasNext()) {
            ((Logger) it.next()).m271a(6, str, str2 + '\n' + Log.getStackTraceString(th));
        }
    }

    public static String getStackTraceString(Throwable th) {
        if (th == null) {
            return BuildConfig.FLAVOR;
        }
        for (Throwable th2 = th; th2 != null; th2 = th2.getCause()) {
            if (th2 instanceof UnknownHostException) {
                return BuildConfig.FLAVOR;
            }
        }
        Writer stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static String m280I(int i) {
        switch (i) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return "S";
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return "D";
            case F2m.PPB /*3*/:
                return "V";
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return "I";
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return "W";
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                return "E";
            default:
                return PaymentFramework.CARD_TYPE_UNKNOWN;
        }
    }
}
