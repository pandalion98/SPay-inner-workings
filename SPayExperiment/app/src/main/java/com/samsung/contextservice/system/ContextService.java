/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.app.Service
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Binder
 *  android.os.IBinder
 *  java.lang.Exception
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.contextservice.system;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.samsung.contextclient.a;
import com.samsung.contextclient.a.b;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextservice.system.ManagerHub;
import com.samsung.contextservice.system.a;
import com.samsung.contextservice.system.c;
import java.util.ArrayList;
import java.util.List;

public class ContextService
extends Service {
    private a Hr = null;
    private final a.a Hs = new a.a(){

        private String e(int n2, int n3) {
            return String.valueOf((int)n2) + String.valueOf((int)n3);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public List<Poi> a(Location location, double d2) {
            c c2;
            com.samsung.contextservice.b.b.d("CtxService", "findPoiByLocation()");
            if (location == null) {
                return null;
            }
            try {
                c2 = c.aG(ContextService.this.getApplicationContext());
                if (c2 == null) return null;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
            return c2.a(location.getLatitude(), location.getLongitude(), d2, 500.0);
        }

        @Override
        public void a(String string, int n2, String string2) {
            com.samsung.contextservice.b.b.d("CtxService", "sendContextData()");
        }

        @Override
        public void a(List<Poi> list, com.samsung.contextclient.a.a a2) {
            com.samsung.contextservice.b.b.d("CtxService", "registerPoi()");
            int n2 = Binder.getCallingPid();
            int n3 = Binder.getCallingUid();
            String string = this.e(n3, n2);
            com.samsung.contextservice.b.b.d("CtxService", "callingpid=" + n2 + ", callingUID=" + n3);
            if (ContextService.this.Hr != null) {
                ContextService.this.Hr.a(n2, string, (ArrayList<Poi>)((ArrayList)list), (b)a2);
                return;
            }
            com.samsung.contextservice.b.b.e("CtxService", "Cannot register new poi");
        }

        @Override
        public void d(List<Poi> list) {
            com.samsung.contextservice.b.b.d("CtxService", "addPoi()");
            int n2 = Binder.getCallingPid();
            int n3 = Binder.getCallingUid();
            this.e(n3, n2);
            com.samsung.contextservice.b.b.d("CtxService", "callingpid=" + n2 + ", callingUID=" + n3);
            if (ContextService.this.Hr != null) {
                ContextService.this.Hr.a(n2, list);
                return;
            }
            com.samsung.contextservice.b.b.e("CtxService", "Cannot register new poi");
        }

        @Override
        public void e(List<Poi> list) {
            com.samsung.contextservice.b.b.d("CtxService", "removePoi()");
            int n2 = Binder.getCallingPid();
            int n3 = Binder.getCallingUid();
            this.e(n3, n2);
            com.samsung.contextservice.b.b.d("CtxService", "callingpid=" + n2 + ", callingUID=" + n3);
            if (ContextService.this.Hr != null) {
                ContextService.this.Hr.b(n3, list);
                return;
            }
            com.samsung.contextservice.b.b.e("CtxService", "Cannot register new poi");
        }

        @Override
        public void f(List<Poi> list) {
            com.samsung.contextservice.b.b.d("CtxService", "addPersistentPoi()");
            int n2 = Binder.getCallingPid();
            int n3 = Binder.getCallingUid();
            this.e(n3, n2);
            com.samsung.contextservice.b.b.d("CtxService", "callingpid=" + n2 + ", callingUID=" + n3);
            if (ContextService.this.Hr != null) {
                ContextService.this.Hr.a(n3, (ArrayList<Poi>)((ArrayList)list));
                return;
            }
            com.samsung.contextservice.b.b.e("CtxService", "Cannot register new poi");
        }

        @Override
        public void g(List<Poi> list) {
            com.samsung.contextservice.b.b.d("CtxService", "removePersistentPoi()");
            int n2 = Binder.getCallingPid();
            int n3 = Binder.getCallingUid();
            this.e(n3, n2);
            com.samsung.contextservice.b.b.d("CtxService", "callingpid=" + n2 + ", callingUID=" + n3);
            if (ContextService.this.Hr != null) {
                ContextService.this.Hr.b(n3, (ArrayList<Poi>)((ArrayList)list));
                return;
            }
            com.samsung.contextservice.b.b.e("CtxService", "Cannot register new poi");
        }

        @Override
        public void gs() {
            com.samsung.contextservice.b.b.d("CtxService", "unregisterPoi()");
            int n2 = Binder.getCallingPid();
            if (ContextService.this.Hr != null) {
                ContextService.this.Hr.X(n2);
                return;
            }
            com.samsung.contextservice.b.b.e("CtxService", "Cannot register new poi");
        }
    };

    public IBinder onBind(Intent intent) {
        com.samsung.contextservice.b.b.d("CtxService", "onBinder()");
        String string = intent.getStringExtra("myname");
        com.samsung.contextservice.b.b.d("CtxService", string + " is connected");
        return this.Hs;
    }

    public void onCreate() {
        super.onCreate();
        com.samsung.contextservice.b.b.i("CtxService", "context service is started");
        try {
            ManagerHub.aE(this.getApplicationContext());
            this.Hr = (a)ManagerHub.Z(1);
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            ManagerHub.onDestroy();
            this.sendBroadcast(new Intent("YouWillNeverKillMe"));
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    public int onStartCommand(Intent intent, int n2, int n3) {
        com.samsung.contextservice.b.b.d("CtxService", "onStartCommand");
        return 1;
    }

}

