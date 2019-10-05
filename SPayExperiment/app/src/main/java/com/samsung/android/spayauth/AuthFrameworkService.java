/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RunningAppProcessInfo
 *  android.app.Service
 *  android.content.Context
 *  android.content.Intent
 *  android.hardware.input.InputManager
 *  android.os.Binder
 *  android.os.Build
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.IBinder$DeathRecipient
 *  android.os.RemoteException
 *  android.os.SystemClock
 *  android.util.Log
 *  android.view.InputEvent
 *  android.view.KeyEvent
 *  com.sec.enterprise.knox.seams.SEAMS
 *  java.lang.Class
 *  java.lang.IllegalAccessException
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.InterruptedException
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.InvocationTargetException
 *  java.lang.reflect.Method
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.android.spayauth;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.InputEvent;
import android.view.KeyEvent;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a.n;
import com.samsung.android.spayfw.utils.h;
import com.sec.enterprise.knox.seams.SEAMS;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class AuthFrameworkService
extends Service {
    private static final boolean DEBUG = h.DEBUG;
    private static final boolean bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
    private a iD = null;
    private final a.a iE = new a.a(){
        com.samsung.android.spayauth.a iA;

        private com.samsung.android.spayauth.a U() {
            if (this.iA == null) {
                this.iA = com.samsung.android.spayauth.a.b(AuthFrameworkService.this.mContext);
            }
            return this.iA;
        }

        private void g(int n2) {
            long l2 = SystemClock.uptimeMillis();
            try {
                InputManager inputManager = (InputManager)InputManager.class.getMethod("getInstance", new Class[0]).invoke(InputManager.class, new Object[0]);
                Class[] arrclass = new Class[]{InputEvent.class, Integer.TYPE};
                Method method = InputManager.class.getMethod("injectInputEvent", arrclass);
                KeyEvent keyEvent = new KeyEvent(l2, l2, 0, n2, 0, 0, -1, 0, 0, 257);
                Object[] arrobject = new Object[]{keyEvent, 2};
                method.invoke((Object)inputManager, arrobject);
                long l3 = SystemClock.uptimeMillis();
                KeyEvent keyEvent2 = new KeyEvent(l3, l3, 1, n2, 0, 0, -1, 0, 0, 257);
                Object[] arrobject2 = new Object[]{keyEvent2, 2};
                method.invoke((Object)inputManager, arrobject2);
                return;
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.e("AuthFrameworkService", "IllegalAccessException on injectInputEvent.");
                illegalAccessException.printStackTrace();
                return;
            }
            catch (IllegalArgumentException illegalArgumentException) {
                Log.e("AuthFrameworkService", "IllegalArgumentException on injectInputEvent.");
                illegalArgumentException.printStackTrace();
                return;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.e("AuthFrameworkService", "NoSuchMethodException on injectInputEvent.");
                noSuchMethodException.printStackTrace();
                return;
            }
            catch (InvocationTargetException invocationTargetException) {
                Log.e("AuthFrameworkService", "InvocationTargetException on injectInputEvent.");
                invocationTargetException.printStackTrace();
                return;
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int N() {
            if (!AuthFrameworkService.this.o("tppLoad")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.N();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int O() {
            if (!AuthFrameworkService.this.o("tppUnload")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.O();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int P() {
            if (!AuthFrameworkService.this.o("tppDeletePin")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.P();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int Q() {
            if (!AuthFrameworkService.this.o("tppCheckTuiSession")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.Q();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public byte[] R() {
            block3 : {
                block2 : {
                    if (!AuthFrameworkService.this.o("tppGetNonce")) break block2;
                    this.U();
                    if (this.iA != null) break block3;
                }
                return null;
            }
            return this.iA.R();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int S() {
            if (!AuthFrameworkService.this.o("tppClearState")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.S();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public int[] T() {
            block3 : {
                int[] arrn;
                block2 : {
                    arrn = new int[]{-40, 0};
                    if (!AuthFrameworkService.this.o("tppMerchantSecureTouch")) break block2;
                    this.U();
                    if (this.iA != null) break block3;
                }
                return arrn;
            }
            return this.iA.T();
        }

        @Override
        public void V() {
            if (!AuthFrameworkService.this.o("cancelTui")) {
                return;
            }
            long l2 = Binder.clearCallingIdentity();
            try {
                android.util.Log.d((String)"AuthFrameworkService", (String)"inside cancelTui method.");
                this.g(4);
                return;
            }
            finally {
                Binder.restoreCallingIdentity((long)l2);
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public int a(int n2, int[] arrn) {
            block3 : {
                block2 : {
                    if (!AuthFrameworkService.this.o("tppMerchantSecureInit")) break block2;
                    this.U();
                    if (this.iA != null) break block3;
                }
                return -40;
            }
            return this.iA.a(n2, arrn);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int a(String string, String string2, String string3, String string4, String string5, String string6, byte[] arrby) {
            if (!AuthFrameworkService.this.o("tppInAppConfirm")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.a(string, string2, string3, string4, string5, string6, arrby);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int a(boolean bl) {
            if (!AuthFrameworkService.this.o("tppResume")) {
                return -40;
            }
            if (!AuthFrameworkService.c(AuthFrameworkService.this.mContext)) {
                return 20481;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.a(bl);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int a(boolean bl, boolean bl2) {
            if (!AuthFrameworkService.this.o("tppVerifyPIN")) {
                return -40;
            }
            if (!AuthFrameworkService.c(AuthFrameworkService.this.mContext)) {
                return 20481;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.a(bl, bl2);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int a(byte[] arrby, int n2, int n3) {
            if (!AuthFrameworkService.this.o("tppSetPrompt")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n4 = 0;
            if (a2 == null) return n4;
            return this.iA.a(arrby, n2, n3);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public int a(byte[] arrby, int n2, int n3, int n4, int n5) {
            block3 : {
                block2 : {
                    if (!AuthFrameworkService.this.o("tppMerchantSendSecureImg")) break block2;
                    this.U();
                    if (this.iA != null) break block3;
                }
                return -40;
            }
            return this.iA.a(arrby, n2, n3, n4, n5);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public int a(byte[] arrby, int n2, int n3, int[] arrn) {
            block3 : {
                block2 : {
                    if (!AuthFrameworkService.this.o("tppMerchantSecureDisplay")) break block2;
                    this.U();
                    if (this.iA != null) break block3;
                }
                return -40;
            }
            return this.iA.a(arrby, n2, n3, arrn);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int a(byte[] arrby, byte[] arrby2, int n2, int n3) {
            if (!AuthFrameworkService.this.o("tppSetCancelBtnImg")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n4 = 0;
            if (a2 == null) return n4;
            return this.iA.a(arrby, arrby2, n2, n3);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int a(byte[] arrby, byte[] arrby2, int n2, int n3, int n4, int n5, int n6) {
            if (!AuthFrameworkService.this.o("tppSetPinBox")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n7 = 0;
            if (a2 == null) return n7;
            return this.iA.a(arrby, arrby2, n2, n3, n4, n5, n6);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int a(String[] arrstring) {
            if (!AuthFrameworkService.this.o("tppSetSecureModeText")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.a(arrstring);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public byte[] a(String string, byte[] arrby) {
            block3 : {
                block2 : {
                    if (!AuthFrameworkService.this.o("tppGetSecureResult")) break block2;
                    this.U();
                    if (this.iA != null) break block3;
                }
                return null;
            }
            return this.iA.a(string, arrby);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int b(byte[] arrby, int n2, int n3) {
            if (!AuthFrameworkService.this.o("tppSetBkImg")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n4 = 0;
            if (a2 == null) return n4;
            return this.iA.b(arrby, n2, n3);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int b(byte[] arrby, byte[] arrby2) {
            if (!AuthFrameworkService.this.o("tppSetupBIO")) {
                return -40;
            }
            if (!AuthFrameworkService.c(AuthFrameworkService.this.mContext)) {
                return 20481;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.b(arrby, arrby2);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int b(String[] arrstring) {
            if (!AuthFrameworkService.this.o("tppSetActionBarText")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.b(arrstring);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int c(byte[] arrby) {
            if (!AuthFrameworkService.this.o("tppSetupPIN")) {
                return -40;
            }
            if (!AuthFrameworkService.c(AuthFrameworkService.this.mContext)) {
                return 20481;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.c(arrby);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int d(byte[] arrby) {
            if (!AuthFrameworkService.this.o("tppPreloadFpSecureResult")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.d(arrby);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int e(byte[] arrby) {
            if (!AuthFrameworkService.this.o("tppUpdateFP")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.e(arrby);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int f(byte[] arrby) {
            if (!AuthFrameworkService.this.o("tppUpdateBIO")) {
                return -40;
            }
            this.U();
            com.samsung.android.spayauth.a a2 = this.iA;
            int n2 = 0;
            if (a2 == null) return n2;
            return this.iA.f(arrby);
        }
    };
    private Context mContext;
    private Handler mHandler;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static boolean c(Context context) {
        Class<AuthFrameworkService> class_ = AuthFrameworkService.class;
        synchronized (AuthFrameworkService.class) {
            boolean bl = AuthFrameworkService.d(context);
            if (bl) {
                return true;
            }
            AuthFrameworkService.e(context);
            try {
                AuthFrameworkService.class.wait(1000L);
                return AuthFrameworkService.d(context);
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            return AuthFrameworkService.d(context);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private static boolean d(Context context) {
        String string = bQC ? "com.qualcomm.qti.services.secureui" : "com.trustonic.tuiservice";
        Log.d("AuthFrameworkService", "tuiProcessName : " + string);
        List list = ((ActivityManager)context.getSystemService("activity")).getRunningAppProcesses();
        if (list != null) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                if (!((ActivityManager.RunningAppProcessInfo)iterator.next()).processName.startsWith(string)) continue;
                Log.d("AuthFrameworkService", "TUI Service Running");
                return true;
            }
        }
        Log.e("AuthFrameworkService", "TUI Service Not Running");
        return false;
    }

    private static void e(Context context) {
        Log.i("AuthFrameworkService", "startTuiService");
        if (bQC) {
            // empty if block
        }
        context.sendBroadcast(new Intent("com.trustonic.tuiservice.action.restart"));
    }

    private boolean o(String string) {
        SEAMS sEAMS = SEAMS.getInstance((Context)this.getApplicationContext());
        if (sEAMS == null) {
            Log.e("AuthFrameworkService", "AuthFrameworkService.checkCallerPermission(): SEAMS is null");
            return false;
        }
        if (sEAMS.isAuthorized(Binder.getCallingPid(), Binder.getCallingUid(), "PaymentFramework", string) == 0) {
            Log.d("AuthFrameworkService", "AuthFrameworkService.checkCallerPermission(): Access Granted");
            return true;
        }
        Log.e("AuthFrameworkService", "AuthFrameworkService.checkCallerPermission(): Access Denied");
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public IBinder onBind(Intent intent) {
        if (intent.getStringExtra("binder") != null && intent.getStringExtra("binder").equals((Object)"auth")) {
            IBinder iBinder;
            AuthFrameworkService.e(this.mContext);
            Bundle bundle = intent.getExtras();
            if (bundle != null && (iBinder = bundle.getBinder("deathDetectorBinder")) != null) {
                Log.d("AuthFrameworkService", "onBind: registering deathBinder : " + (Object)iBinder);
                try {
                    this.iD.a(iBinder);
                    iBinder.linkToDeath((IBinder.DeathRecipient)this.iD, 0);
                }
                catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }
            Log.i("AuthFrameworkService", "return auth binder");
            return this.iE;
        }
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.iD = new a();
        this.mHandler = PaymentFrameworkApp.az();
        this.mContext = this.getApplicationContext();
    }

    class a
    implements IBinder.DeathRecipient {
        private IBinder iG;

        a() {
        }

        public void a(IBinder iBinder) {
            this.iG = iBinder;
        }

        public void binderDied() {
            Log.e("AuthFrameworkService", "DeathRecipient: Error: Wallet App died, handle clean up");
            n.q(AuthFrameworkService.this.mContext).clearCard();
            if (this.iG != null) {
                this.iG.unlinkToDeath((IBinder.DeathRecipient)this, 0);
            }
            try {
                AuthFrameworkService.this.iE.O();
                return;
            }
            catch (RemoteException remoteException) {
                remoteException.printStackTrace();
                return;
            }
        }
    }

}

