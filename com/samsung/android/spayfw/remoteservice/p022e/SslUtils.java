package com.samsung.android.spayfw.remoteservice.p022e;

import android.content.Context;
import android.service.tima.ITimaService;
import com.android.org.conscrypt.TrustManagerImpl;
import com.samsung.android.spayfw.cncc.SpayCNCCX509KeyManager;
import com.samsung.android.spayfw.cncc.SpayDRKManager;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.p008e.ClientCertificateManagerWrapper;
import com.samsung.android.spayfw.p008e.DcmKeyManagerWrapper;
import com.samsung.android.spayfw.p008e.p010b.Platformutils;
import com.samsung.android.spayfw.utils.DBUtils;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;

/* renamed from: com.samsung.android.spayfw.remoteservice.e.c */
public class SslUtils {
    private static final boolean Bb;
    private static SSLContext Bc;
    private static String Bd;

    static {
        boolean z = true;
        if (!Utils.fN()) {
            z = false;
        }
        Bb = z;
        Bc = null;
        Bd = BuildConfig.FLAVOR;
    }

    public static synchronized SSLContext m1190M(Context context) {
        SSLContext sSLContext;
        synchronized (SslUtils.class) {
            if (Bc == null) {
                Bc = SslUtils.m1192O(context);
            }
            sSLContext = Bc;
        }
        return sSLContext;
    }

    public static synchronized String m1191N(Context context) {
        String str = null;
        synchronized (SslUtils.class) {
            if (Bd != null && !Bd.isEmpty()) {
                str = Bd;
            } else if (SpayDRKManager.isSupported(context)) {
                r2 = SpayDRKManager.getDeviceRootKeyUID(context);
                if (r2 == null) {
                    Log.m286e("SslUtils", "uid null");
                } else {
                    Log.m285d("SslUtils", "uid = " + r2);
                    r0 = r2.split(":");
                    r2 = new StringBuilder();
                    r2.append(r0[1]).append(r0[2]).append(r0[3]).append(r0[4]);
                    Bd = r2.substring(2);
                    Log.m285d("SslUtils", "mDrkUid = " + Bd);
                    str = Bd;
                }
            } else {
                if (Bb) {
                    ClientCertificateManagerWrapper.fg();
                    Log.m285d("SslUtils", "continue getDrkUid");
                }
                try {
                    X509KeyManager P = SslUtils.m1193P(context);
                    if (P == null) {
                        Log.m286e("SslUtils", "keyManager null");
                        str = Bd;
                    } else {
                        r2 = P.chooseClientAlias(null, null, null);
                        if (r2 == null) {
                            Log.m286e("SslUtils", "alias null");
                            str = Bd;
                        } else {
                            Log.m285d("SslUtils", "obtained certificate chain alias " + r2);
                            X509Certificate[] certificateChain = P.getCertificateChain(r2);
                            if (certificateChain == null || certificateChain.length == 0) {
                                Log.m286e("SslUtils", "certificate chain returned null");
                                str = Bd;
                            } else {
                                str = SslUtils.m1194u(certificateChain[0].getIssuerX500Principal().getName(), "UID");
                                Log.m285d("SslUtils", "uid = " + str);
                                r0 = str.split(":");
                                r2 = new StringBuilder();
                                r2.append(r0[1]).append(r0[2]).append(r0[3]).append(r0[4]);
                                Bd = r2.substring(2);
                                Log.m285d("SslUtils", "mDrkUid = " + Bd);
                                Log.m285d("SslUtils", "no catch exception");
                                str = Bd;
                            }
                        }
                    }
                } catch (Throwable e) {
                    Log.m284c("SslUtils", e.getMessage(), e);
                }
            }
        }
        return str;
    }

    private static final void fb() {
        try {
            ITimaService fF = DBUtils.fF();
            for (Method name : fF.getClass().getMethods()) {
                Log.m285d("SslUtils", "Method : " + name.getName());
            }
            fF.getClass().getMethod("DCMSelfCheck", new Class[0]).invoke(fF, new Object[0]);
        } catch (Throwable e) {
            Log.m284c("SslUtils", e.getMessage(), e);
        }
    }

    private static SSLContext m1192O(Context context) {
        Throwable th;
        Throwable th2;
        Throwable th3;
        Throwable th4 = null;
        KeyManager[] keyManagerArr = new KeyManager[]{SslUtils.m1193P(context)};
        SSLContext instance;
        try {
            InputStream open = context.getAssets().open("server.bks");
            Throwable th5 = null;
            try {
                TrustManager[] trustManagerArr;
                KeyStore instance2 = KeyStore.getInstance("BKS");
                if (instance2 != null) {
                    instance2.load(open, null);
                    trustManagerArr = new TrustManager[]{new TrustManagerImpl(instance2)};
                } else {
                    trustManagerArr = null;
                }
                instance = SSLContext.getInstance("TLS");
                try {
                    instance.init(keyManagerArr, trustManagerArr, null);
                    Log.m285d("SslUtils", "SSL Context initialization done!");
                    if (open != null) {
                        if (null != null) {
                            try {
                                open.close();
                            } catch (Throwable th42) {
                                try {
                                    th5.addSuppressed(th42);
                                } catch (Exception e) {
                                    th42 = e;
                                    Log.m284c("SslUtils", th42.getMessage(), th42);
                                    Log.m286e("SslUtils", "SSL Context initialization failure");
                                    Log.m286e("SslUtils", "Shutting Down");
                                    System.exit(-1);
                                    return instance;
                                }
                            }
                        }
                        open.close();
                    }
                    return instance;
                } catch (Throwable th32) {
                    th = th32;
                    Object obj = instance;
                    th2 = th;
                    if (open != null) {
                        if (th42 == null) {
                            try {
                                open.close();
                            } catch (Throwable th6) {
                                th42.addSuppressed(th6);
                            }
                        } else {
                            open.close();
                        }
                    }
                    try {
                        throw th2;
                    } catch (Throwable th22) {
                        th42 = th22;
                        Object obj2 = th32;
                        Log.m284c("SslUtils", th42.getMessage(), th42);
                        Log.m286e("SslUtils", "SSL Context initialization failure");
                        Log.m286e("SslUtils", "Shutting Down");
                        System.exit(-1);
                        return instance;
                    }
                }
            } catch (Throwable th7) {
                th22 = th7;
                th32 = null;
                if (open != null) {
                    if (th42 == null) {
                        open.close();
                    } else {
                        open.close();
                    }
                }
                throw th22;
            }
        } catch (Throwable th222) {
            th = th222;
            instance = null;
            th42 = th;
            Log.m284c("SslUtils", th42.getMessage(), th42);
            Log.m286e("SslUtils", "SSL Context initialization failure");
            Log.m286e("SslUtils", "Shutting Down");
            System.exit(-1);
            return instance;
        }
    }

    private static String m1194u(String str, String str2) {
        for (String str3 : str.split(",")) {
            if (str3.contains(str2)) {
                String[] split = str3.trim().split("=");
                if (split[1] != null) {
                    return split[1].trim();
                }
            }
        }
        return BuildConfig.FLAVOR;
    }

    private static X509KeyManager m1193P(Context context) {
        boolean isSupported = SpayCNCCX509KeyManager.isSupported(context);
        if (Platformutils.fT()) {
            Log.m286e("SslUtils", "sem device -  useSPayCNCC = false");
            isSupported = false;
        }
        if (isSupported) {
            Log.m285d("SslUtils", "Using CNCC Flow for SSL Communication");
            return new SpayCNCCX509KeyManager(context);
        } else if (Bb || !Utils.fO()) {
            Log.m286e("SslUtils", "CcmKeyManager init ");
            ClientCertificateManagerWrapper.fg();
            return new CcmKeyManager();
        } else {
            Log.m286e("SslUtils", "DcmKeyManager init ");
            SslUtils.fb();
            return DcmKeyManagerWrapper.fh();
        }
    }
}
