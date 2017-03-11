package com.samsung.contextservice.server;

import android.content.Context;
import com.samsung.contextservice.p028a.PolicyDao;
import com.samsung.contextservice.p029b.CSlog;
import com.samsung.contextservice.server.models.CtxPolicy;
import com.samsung.contextservice.server.models.PolicyResponseData;

/* renamed from: com.samsung.contextservice.server.j */
public class ServerConfig {
    private static ServerConfig Ha;
    private static CtxPolicy Hb;
    private static Object lock;
    public final String GZ;

    static {
        lock = new Object();
        Hb = CtxPolicy.getDefault();
    }

    public static ServerConfig az(Context context) {
        synchronized (lock) {
            if (Ha == null) {
                Ha = new ServerConfig(context);
            }
        }
        return Ha;
    }

    public static String gA() {
        String str;
        synchronized (lock) {
            if (Ha == null) {
                str = "PROD";
            } else {
                str = Ha.GZ;
            }
        }
        return str;
    }

    public ServerConfig(Context context) {
        String str;
        Exception exception;
        PolicyResponseData bI;
        String str2 = "PROD";
        if (context != null) {
            try {
                str = (String) ServerConfig.m1458g(context, "ENVIRONMENT");
                if (str == null) {
                    try {
                        str = "PROD";
                    } catch (Exception e) {
                        Exception exception2 = e;
                        str2 = str;
                        exception = exception2;
                        exception.printStackTrace();
                        if (context != null) {
                            try {
                                bI = new PolicyDao(context).bI("ALL_POLICIES");
                                if (bI != null) {
                                    ServerConfig.m1457a(bI.getPolicyObject());
                                }
                            } catch (Exception exception3) {
                                exception3.printStackTrace();
                            }
                        }
                        this.GZ = str2;
                        CSlog.m1408d("ServerConfig", "Build: " + ServerConfig.gA());
                    }
                }
            } catch (Exception e2) {
                exception3 = e2;
                exception3.printStackTrace();
                if (context != null) {
                    bI = new PolicyDao(context).bI("ALL_POLICIES");
                    if (bI != null) {
                        ServerConfig.m1457a(bI.getPolicyObject());
                    }
                }
                this.GZ = str2;
                CSlog.m1408d("ServerConfig", "Build: " + ServerConfig.gA());
            }
        }
        str = str2;
        str2 = str;
        if (context != null) {
            bI = new PolicyDao(context).bI("ALL_POLICIES");
            if (bI != null) {
                ServerConfig.m1457a(bI.getPolicyObject());
            }
        }
        this.GZ = str2;
        CSlog.m1408d("ServerConfig", "Build: " + ServerConfig.gA());
    }

    private static Object m1458g(Context context, String str) {
        Object obj = null;
        try {
            obj = Class.forName(context.getPackageName() + ".BuildConfig").getField(str).get(null);
        } catch (ClassNotFoundException e) {
            CSlog.m1409e("ServerConfig", "Use default config");
        } catch (NoSuchFieldException e2) {
            CSlog.m1409e("ServerConfig", "Use default config");
        } catch (IllegalAccessException e3) {
            CSlog.m1409e("ServerConfig", "Use default config");
        }
        return obj;
    }

    public static void m1457a(CtxPolicy ctxPolicy) {
        if (ctxPolicy != null) {
            Hb = ctxPolicy;
            CSlog.m1408d("ServerConfig", "update local policy to " + Hb.toString());
        }
    }

    public static CtxPolicy gB() {
        return Hb;
    }
}
