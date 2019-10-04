/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Class
 *  java.lang.ClassNotFoundException
 *  java.lang.Exception
 *  java.lang.IllegalAccessException
 *  java.lang.NoSuchFieldException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Field
 */
package com.samsung.contextservice.server;

import android.content.Context;
import com.samsung.contextservice.a.c;
import com.samsung.contextservice.b.b;
import com.samsung.contextservice.server.models.CtxPolicy;
import com.samsung.contextservice.server.models.PolicyResponseData;
import java.lang.reflect.Field;

public class j {
    private static j Ha;
    private static CtxPolicy Hb;
    private static Object lock;
    public final String GZ;

    static {
        lock = new Object();
        Hb = CtxPolicy.getDefault();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public j(Context context) {
        String string;
        block9 : {
            String string2;
            block8 : {
                string = "PROD";
                if (context != null) {
                    try {
                        string2 = (String)j.g(context, "ENVIRONMENT");
                        if (string2 == null) {
                            string2 = "PROD";
                        }
                        break block8;
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        break block9;
                    }
                }
                string2 = string;
            }
            string = string2;
        }
        if (context != null) {
            try {
                PolicyResponseData policyResponseData = new c(context).bI("ALL_POLICIES");
                if (policyResponseData != null) {
                    j.a(policyResponseData.getPolicyObject());
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        this.GZ = string;
        b.d("ServerConfig", "Build: " + j.gA());
    }

    public static void a(CtxPolicy ctxPolicy) {
        if (ctxPolicy != null) {
            Hb = ctxPolicy;
            b.d("ServerConfig", "update local policy to " + Hb.toString());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static j az(Context context) {
        Object object;
        Object object2 = object = lock;
        synchronized (object2) {
            if (Ha == null) {
                Ha = new j(context);
            }
            return Ha;
        }
    }

    private static Object g(Context context, String string) {
        try {
            Object object = Class.forName((String)(context.getPackageName() + ".BuildConfig")).getField(string).get(null);
            return object;
        }
        catch (ClassNotFoundException classNotFoundException) {
            b.e("ServerConfig", "Use default config");
            return null;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            b.e("ServerConfig", "Use default config");
            return null;
        }
        catch (IllegalAccessException illegalAccessException) {
            b.e("ServerConfig", "Use default config");
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String gA() {
        Object object;
        Object object2 = object = lock;
        synchronized (object2) {
            if (Ha != null) return j.Ha.GZ;
            return "PROD";
        }
    }

    public static CtxPolicy gB() {
        return Hb;
    }
}

