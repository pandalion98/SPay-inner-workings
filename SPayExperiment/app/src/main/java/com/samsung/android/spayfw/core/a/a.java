/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.location.Location
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.Looper
 *  android.os.Message
 *  com.google.android.gms.maps.model.RuntimeRemoteException
 *  java.lang.Class
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.samsung.android.spayfw.core.a;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.samsung.android.analytics.sdk.AnalyticContext;
import com.samsung.android.analytics.sdk.AnalyticEvent;
import com.samsung.android.spayfw.core.a.o;
import com.samsung.android.spayfw.e.d;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class a
extends o {
    private static Handler handler;
    private static HandlerThread km;
    private static a kq;
    private boolean kn = true;
    private com.samsung.android.spayfw.remoteservice.a.a ko = null;
    private com.samsung.android.spayfw.storage.a kp = null;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private a(Context context) {
        super(context);
        this.ko = com.samsung.android.spayfw.remoteservice.a.a.H(context);
        try {
            this.kp = com.samsung.android.spayfw.storage.a.aa(context);
        }
        catch (RuntimeRemoteException runtimeRemoteException) {
            runtimeRemoteException.printStackTrace();
            com.samsung.android.spayfw.b.c.e("AnalyticsFrameworkProcessor", "AnalyticsReportCache threw exception and failed to store or upload events");
        }
        km = new HandlerThread("AnalyticsReportThread");
        km.start();
        handler = new Handler(km.getLooper()){

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public void handleMessage(Message message) {
                if (message == null) return;
                switch (message.what) {
                    default: {
                        return;
                    }
                    case 1: {
                        com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "ACTION_UPLOAD_EVENT");
                        a a2 = null;
                        if (message != null) {
                            a2 = (a)message.obj;
                        }
                        com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "Entered AnalyticsReporter: lEvent " + a2);
                        a.this.a(a2);
                        return;
                    }
                    case 2: 
                }
                com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "ACTION_FLUSH_EVENTS_FROM_STORAGE");
                a.this.aU();
            }
        };
    }

    private void aU() {
        com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "flushEventsFromStorage");
    }

    private void b(AnalyticEvent analyticEvent) {
        com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "updateLocationInfo");
        Location location = DeviceInfo.getGoogleLocation();
        if (location != null) {
            c c2 = new c(String.valueOf((double)location.getLatitude()), String.valueOf((double)location.getLongitude()), null, location.getProvider(), String.valueOf((double)location.getAltitude()));
            c2.setAccuracy(String.valueOf((float)location.getAccuracy()));
            c2.setTime(String.valueOf((long)location.getTime()));
            analyticEvent.a(AnalyticEvent.Field.fy, c2.toJsonString());
            com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "location = " + c2.toJsonString());
        }
    }

    private boolean c(AnalyticEvent analyticEvent) {
        return analyticEvent.L().equals((Object)AnalyticEvent.Type.hr) || analyticEvent.L().equals((Object)AnalyticEvent.Type.hA);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean d(AnalyticEvent var1_1) {
        var2_2 = true;
        switch (2.kt[var1_1.L().ordinal()]) {
            case 9: 
            case 10: 
            case 11: 
            case 12: {
                var4_4 = var1_1.getValue(AnalyticEvent.Field.fq.getString());
                if (var4_4 != null && var4_4.equals((Object)AnalyticEvent.Data.ce.toString())) {
                    return var2_2;
                }
            }
            default: {
                ** GOTO lbl21
            }
            case 13: 
            case 14: {
                ** break;
            }
            case 8: {
                var5_3 = var1_1.getValue(AnalyticEvent.Field.fc.getString());
                if (var5_3 != null) {
                    if (var5_3.equals((Object)AnalyticEvent.Data.cw.getString()) != false) return var2_2;
                    if (var5_3.equals((Object)AnalyticEvent.Data.cy.getString())) {
                        return var2_2;
                    }
                }
                ** GOTO lbl21
            }
lbl18: // 1 sources:
            var3_5 = var1_1.getValue(AnalyticEvent.Field.gk.getString());
            if (var3_5 != null && var3_5.equals((Object)AnalyticEvent.Data.dY.toString())) {
                return var2_2;
            }
lbl21: // 4 sources:
            var2_2 = false;
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
        }
        return var2_2;
    }

    public static final a l(Context context) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            if (kq == null) {
                kq = new a(context);
            }
            a a2 = kq;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return a2;
        }
    }

    public void a(AnalyticEvent analyticEvent, AnalyticContext analyticContext) {
        com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "process");
        String string = d.get("ro.build.PDA");
        com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "BuildNumber :" + string);
        analyticContext.n(string);
        Context context = this.mContext;
        String string2 = null;
        if (context != null) {
            string2 = DeviceInfo.getDeviceId(this.mContext);
        }
        com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "fetched DID:" + string2);
        analyticContext.m(string2);
        if (this.c(analyticEvent) || this.d(analyticEvent)) {
            this.b(analyticEvent);
            com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "locationUpdatedEvent" + analyticEvent);
        }
        this.kp.b(analyticEvent, analyticContext);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void a(a a2) {
        a a3 = this;
        synchronized (a3) {
            com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "Sending analytic event Report");
            if (a2 == null) {
                com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "Json Report Data Empty");
            } else {
                if (a2 instanceof b) {
                    com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "Report GSIMData := " + a2);
                } else {
                    com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "Report Data := " + a2);
                }
                if (com.samsung.android.spayfw.remoteservice.tokenrequester.a.fc()) {
                    com.samsung.android.spayfw.b.c.e("AnalyticsFrameworkProcessor", " server is UnAvailable Now, reports should remain in the cache");
                }
            }
            return;
        }
    }

    public void a(List<AnalyticEvent> list, AnalyticContext analyticContext) {
        com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "process-event list");
        String string = d.get("ro.build.PDA");
        com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "BuildNumber :" + string);
        analyticContext.n(string);
        Context context = this.mContext;
        String string2 = null;
        if (context != null) {
            string2 = DeviceInfo.getDeviceId(this.mContext);
        }
        com.samsung.android.spayfw.b.c.d("AnalyticsFrameworkProcessor", "fetched DID:" + string2);
        analyticContext.m(string2);
        Message message = new Message();
        message.obj = new a(list, analyticContext);
        message.what = 1;
        handler.sendMessage(message);
    }

    public static class com.samsung.android.spayfw.core.a.a$a {
        public a ku = null;

        public com.samsung.android.spayfw.core.a.a$a(List<AnalyticEvent> list, AnalyticContext analyticContext) {
            this.ku.kv.kx.kz.id = analyticContext.K();
            this.ku.kv.kx.kz.model = analyticContext.E();
            this.ku.kv.kx.kz.kG.build = analyticContext.H();
            this.ku.kv.kx.kz.kG.name = analyticContext.G();
            this.ku.kv.kx.kz.kG.version = analyticContext.F();
            this.ku.kv.kx.kA.id = analyticContext.J();
            this.ku.kv.kx.kB.id = analyticContext.I();
            this.ku.kv.kx.kC.id = analyticContext.D();
            a.a.b b2 = new a.a.b();
            if (list != null) {
                for (AnalyticEvent analyticEvent : list) {
                    b2.category = analyticEvent.L().getString();
                    ContentValues contentValues = analyticEvent.M();
                    if (contentValues != null) {
                        Set set = contentValues.valueSet();
                        if (set != null) {
                            for (Map.Entry entry : set) {
                                a.a.b.a a2 = new a.a.b.a();
                                a2.key = (String)entry.getKey();
                                a2.value = (String)entry.getValue();
                                b2.kH.add((Object)a2);
                            }
                            continue;
                        }
                        com.samsung.android.spayfw.b.c.e("AnalyticsFrameworkProcessor", "content values are empty (lSet is null)");
                        continue;
                    }
                    com.samsung.android.spayfw.b.c.e("AnalyticsFrameworkProcessor", "content values are empty");
                }
            } else {
                com.samsung.android.spayfw.b.c.e("AnalyticsFrameworkProcessor", "given list of events are null");
            }
            this.ku.kv.ky.add((Object)b2);
        }

        public String toString() {
            return "{\"data\":" + this.ku + '}';
        }

        public static class com.samsung.android.spayfw.core.a.a$a$a {
            public a kv;

            public String toString() {
                return "{\"app\":" + this.kv + '}';
            }

            private static class com.samsung.android.spayfw.core.a.a$a$a$a {
                public a kx;
                public ArrayList<b> ky;

                public String toString() {
                    return "{\"context\":" + this.kx.toString() + ", \"events\":" + this.ky.toString() + '}';
                }

                public static class com.samsung.android.spayfw.core.a.a$a$a$a$a {
                    public c kA;
                    public d kB;
                    public b kC;
                    public a kz;

                    public String toString() {
                        return "{\"device\":" + this.kz.toString() + ", \"user\":" + this.kA.toString() + ", \"wallet\":" + this.kB.toString() + ", \"session\":" + this.kC.toString() + '}';
                    }

                    public static class com.samsung.android.spayfw.core.a.a$a$a$a$a$a {
                        public String id;
                        public String kD;
                        public String kE;
                        public String kF;
                        public a kG;
                        public String model;
                        public String pfVersion;

                        public String toString() {
                            return "{\"id\":\"" + this.id + '\"' + ", \"model\":\"" + this.model + '\"' + ", \"appVersion\":\"" + this.kD + '\"' + ", \"appPkgName\":\"" + this.kE + '\"' + ", \"specVersion\":\"" + this.kF + '\"' + ", \"pfVersion\":\"" + this.pfVersion + '\"' + ", \"os\":" + this.kG + '}';
                        }

                        public static class a {
                            public String build;
                            public String name;
                            public String version;

                            public String toString() {
                                return "{\"build\":\"" + this.build + '\"' + ", \"name\":\"" + this.name + '\"' + ", \"version\":\"" + this.version + '\"' + '}';
                            }
                        }

                    }

                    public static class b {
                        public String id;

                        public String toString() {
                            return "{\"id\":\"" + this.id + '\"' + '}';
                        }
                    }

                    public static class c {
                        public String id;

                        public String toString() {
                            return "{\"id\":\"" + this.id + '\"' + '}';
                        }
                    }

                    public static class d {
                        public String id;

                        public String toString() {
                            return "{\"id\":\"" + this.id + '\"' + '}';
                        }
                    }

                }

                public static class b {
                    public String category = null;
                    public String id = null;
                    public ArrayList<a> kH = new ArrayList();
                    public String timestamp = null;

                    public String toString() {
                        return "{\"category\":\"" + this.category + '\"' + ", \"id\":\"" + this.id + '\"' + ", \"timestamp\":\"" + this.timestamp + '\"' + ", \"fields\":" + this.kH + '}';
                    }

                    public static class a {
                        public String key = null;
                        public String value = null;

                        public String toString() {
                            return "{\"key\":\"" + this.key + '\"' + ", \"value\":\"" + this.value + '\"' + '}';
                        }
                    }

                }

            }

        }

    }

    public static class b
    extends a {
    }

    public static class c {
        private String accuracy;
        private String altitude;
        private String latitude;
        private String longitude;
        private String provider;
        private String time;
        private String timezone;

        public c(String string, String string2, String string3, String string4, String string5) {
            this.latitude = string;
            this.longitude = string2;
            this.timezone = string3;
            this.provider = string4;
            this.altitude = string5;
        }

        public void setAccuracy(String string) {
            this.accuracy = string;
        }

        public void setTime(String string) {
            this.time = string;
        }

        public String toJsonString() {
            return "{\"latitude\":\"" + this.latitude + "\"" + ",\"longitude\":\"" + this.longitude + "\"" + ",\"altitude\":\"" + this.altitude + "\"" + ",\"timezone\":\"" + this.timezone + "\"" + ",\"provider\":\"" + this.provider + "\"" + ",\"accuracy\":\"" + this.accuracy + "\"" + ",\"time\":\"" + this.time + "\"" + '}';
        }
    }

}

