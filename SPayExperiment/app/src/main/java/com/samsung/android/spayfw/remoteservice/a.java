/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.os.Build
 *  android.os.Bundle
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
package com.samsung.android.spayfw.remoteservice;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.core.e;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.e.c;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.utils.GLDManager;
import com.samsung.android.spayfw.utils.h;
import java.io.File;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.UUID;
import javax.net.ssl.SSLSocketFactory;

public abstract class a
extends Client {
    protected final Context mContext;

    protected a(Context context, String string) {
        super(string, new b(), new a(context));
        this.mContext = context;
    }

    public com.samsung.android.spayfw.remoteservice.b a(int n2, String string, Bundle bundle, String string2) {
        com.samsung.android.spayfw.remoteservice.b b2 = new com.samsung.android.spayfw.remoteservice.b(this, n2, string, bundle, string2);
        this.a(b2);
        return b2;
    }

    public void a(Request request) {
        com.samsung.android.spayfw.b.c.i("CommonClient", "initializeRequest : Environment - PROD");
        com.samsung.android.spayfw.b.c.i("CommonClient", "Build CL : 3392039");
        request.setUserAgent(h.ai(this.mContext));
        request.addHeader("Authorization", "Bearer " + e.h(this.mContext).getConfig("CONFIG_JWT_TOKEN"));
        request.addHeader("x-smps-dmid", e.h(this.mContext).getConfig("CONFIG_WALLET_ID"));
        request.addHeader("x-smps-mid", e.h(this.mContext).getConfig("CONFIG_USER_ID"));
        request.addHeader("x-smps-cc2", h.fP());
        request.addHeader("x-smps-did", h.ah(this.mContext));
        request.addHeader("PF-Version", h.getPackageVersion(this.mContext, this.mContext.getPackageName()));
        request.addHeader("PF-Version-Code", h.f(this.mContext, this.mContext.getPackageName()) + "");
        request.addHeader("Device-Did", h.ah(this.mContext));
        request.addHeader("Spay-Version", h.getPackageVersion(this.mContext, "com.samsung.android.spay"));
        request.addHeader("x-smps-model-id", Build.MODEL);
        request.addHeader("x-smps-mcc", DeviceInfo.getMcc(this.mContext));
        request.addHeader("x-smps-mnc", DeviceInfo.getMnc(this.mContext));
        request.addHeader("x-smps-dt", "01");
        request.addHeader("x-smps-lang", this.mContext.getResources().getConfiguration().locale.getLanguage());
        request.addHeader("x-smps-sales-cd", h.getSalesCode());
        request.addHeader("PF-Instance-Id", e.h(this.mContext).getConfig("CONFIG_PF_INSTANCE_ID"));
    }

    @Override
    public File aU(String string) {
        return this.mContext.getDir(string, 0);
    }

    @Override
    public String bc(String string) {
        return com.samsung.android.spayfw.utils.b.bx(string);
    }

    @Override
    public Client.HttpRequest bd(String string) {
        return com.samsung.android.spayfw.remoteservice.b.a.a(string, com.samsung.android.spayfw.remoteservice.e.b.a(c.M(this.mContext).getSocketFactory()));
    }

    @Override
    protected boolean eQ() {
        return h.ak(this.mContext);
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
            com.samsung.android.spayfw.b.c.i("CommonClient", "Request UUID " + uUID.toString());
            stringBuilder.append("-").append(uUID.toString());
        }
        if (string3 != null) {
            com.samsung.android.spayfw.b.c.i("CommonClient", "Request Timestamp " + string3);
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
            return GLDManager.af(this.mContext).by("PROD");
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

