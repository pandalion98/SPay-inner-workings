/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.os.Build
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.io.File
 *  java.lang.Class
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.reflect.Type
 *  java.util.Locale
 *  java.util.UUID
 *  javax.net.ssl.SSLSocketFactory
 */
package com.samsung.contextservice.server;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.utils.GLDManager;
import com.samsung.android.spayfw.utils.h;
import com.samsung.contextclient.data.Location;
import com.samsung.contextservice.b.e;
import com.samsung.contextservice.server.c;
import com.samsung.contextservice.server.g;
import com.samsung.contextservice.server.j;
import com.samsung.contextservice.server.k;
import com.samsung.contextservice.server.models.CacheRequestData;
import com.samsung.contextservice.server.models.DeviceInfo;
import com.samsung.contextservice.server.models.TriggerRequestData;
import java.io.File;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.UUID;
import javax.net.ssl.SSLSocketFactory;

public class d
extends Client {
    private static d GG;
    private final Context mContext;

    public d(Context context) {
        super("/cp/v1", new b(), new a(context));
        this.mContext = context;
    }

    public static d aw(Context context) {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            if (GG == null) {
                GG = new d(context);
            }
            d d2 = GG;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return d2;
        }
    }

    public c a(Location location) {
        c c2 = new c(this, new CacheRequestData(location));
        this.a(c2);
        return c2;
    }

    public k a(TriggerRequestData triggerRequestData) {
        k k2 = new k(this, triggerRequestData);
        this.a(k2);
        return k2;
    }

    public void a(Request request) {
        request.setUserAgent(h.ai(this.mContext));
        request.addHeader("Authorization", "Bearer " + e.aJ(this.mContext));
        request.addHeader("x-smps-dmid", e.aI(this.mContext));
        request.addHeader("x-smps-mid", e.aK(this.mContext));
        request.addHeader("x-smps-cc2", e.fP());
        request.addHeader("x-smps-did", e.ah(this.mContext));
        request.addHeader("PF-Version", e.getPackageVersion(this.mContext, this.mContext.getPackageName()));
        request.addHeader("Device-Did", DeviceInfo.getDeviceId(this.mContext));
        request.addHeader("Spay-Version", e.getPackageVersion(this.mContext, "com.samsung.android.spay"));
        request.addHeader("CTX-Version", "1.1.00");
        request.addHeader("x-smps-model-id", Build.MODEL);
        request.addHeader("x-smps-mcc", e.getMcc(this.mContext));
        request.addHeader("x-smps-mnc", e.getMnc(this.mContext));
        request.addHeader("x-smps-dt", "01");
        request.addHeader("x-smps-lang", this.mContext.getResources().getConfiguration().locale.getLanguage());
        request.addHeader("x-smps-sales-cd", h.getSalesCode());
    }

    @Override
    public File aU(String string) {
        return this.mContext.getDir(string, 0);
    }

    public g bL(String string) {
        g g2 = new g(this, string);
        this.a(g2);
        return g2;
    }

    @Override
    public String bc(String string) {
        return com.samsung.android.spayfw.utils.b.bx(string);
    }

    @Override
    public Client.HttpRequest bd(String string) {
        return com.samsung.android.spayfw.remoteservice.b.a.a(string, com.samsung.android.spayfw.remoteservice.e.b.a(com.samsung.android.spayfw.remoteservice.e.c.M(this.mContext).getSocketFactory()));
    }

    @Override
    public String getRequestId() {
        String string = Build.SERIAL;
        String string2 = DeviceInfo.getDeviceImei(this.mContext);
        UUID uUID = UUID.randomUUID();
        String string3 = Long.toString((long)System.currentTimeMillis());
        StringBuilder stringBuilder = new StringBuilder();
        if (string != null) {
            stringBuilder.append(string);
        }
        if (string2 != null) {
            stringBuilder.append("-").append(string2);
        }
        if (uUID != null) {
            com.samsung.contextservice.b.b.i("CtxRequesterClient", "Request UUID " + uUID.toString());
            stringBuilder.append("-").append(uUID.toString());
        }
        if (string3 != null) {
            com.samsung.contextservice.b.b.i("CtxRequesterClient", "Request Timestamp " + string3);
            stringBuilder.append("-").append(string3);
        }
        return stringBuilder.toString();
    }

    private static class a
    implements Client.b {
        private Context mContext;

        a(Context context) {
            this.mContext = context;
        }

        @Override
        public String eN() {
            return GLDManager.af(this.mContext).by(j.gA());
        }

        @Override
        public void eR() {
            GLDManager.af(this.mContext).fG();
        }
    }

    private static class b
    implements Client.a {
        final Gson sGson = new GsonBuilder().disableHtmlEscaping().create();

        private b() {
        }

        @Override
        public <Z> Z fromJson(String string, Class<Z> class_) {
            return (Z)this.sGson.fromJson(string, class_);
        }

        @Override
        public <Z> Z fromJson(String string, Type type) {
            return (Z)this.sGson.fromJson(string, type);
        }

        @Override
        public String toJson(Object object) {
            return this.sGson.toJson(object);
        }
    }

}

