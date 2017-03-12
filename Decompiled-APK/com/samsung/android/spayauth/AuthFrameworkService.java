package com.samsung.android.spayauth;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import com.samsung.android.spayauth.sdk.IAuthFramework.IAuthFramework;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.p005a.PaymentProcessor;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytui.SpayTuiTAInfo;
import com.sec.enterprise.knox.seams.SEAMS;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AuthFrameworkService extends Service {
    private static final boolean DEBUG;
    private static final boolean bQC;
    private C0327a iD;
    private final IAuthFramework iE;
    private Context mContext;
    private Handler mHandler;

    /* renamed from: com.samsung.android.spayauth.AuthFrameworkService.1 */
    class C03261 extends IAuthFramework {
        AuthFramework iA;
        final /* synthetic */ AuthFrameworkService iF;

        C03261(AuthFrameworkService authFrameworkService) {
            this.iF = authFrameworkService;
        }

        private AuthFramework m195U() {
            if (this.iA == null) {
                this.iA = AuthFramework.m232b(this.iF.mContext);
            }
            return this.iA;
        }

        public int m197N() {
            if (!this.iF.m231o("tppLoad")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m233N();
            }
            return 0;
        }

        public int m198O() {
            if (!this.iF.m231o("tppUnload")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m234O();
            }
            return 0;
        }

        public int m214a(String[] strArr) {
            if (!this.iF.m231o("tppSetSecureModeText")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m249a(strArr);
            }
            return 0;
        }

        public int m213a(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, int i5) {
            if (!this.iF.m231o("tppSetPinBox")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m248a(bArr, bArr2, i, i2, i3, i4, i5);
            }
            return 0;
        }

        public int m209a(byte[] bArr, int i, int i2) {
            if (!this.iF.m231o("tppSetPrompt")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m244a(bArr, i, i2);
            }
            return 0;
        }

        public int m216b(byte[] bArr, int i, int i2) {
            if (!this.iF.m231o("tppSetBkImg")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m252b(bArr, i, i2);
            }
            return 0;
        }

        public int m212a(byte[] bArr, byte[] bArr2, int i, int i2) {
            if (!this.iF.m231o("tppSetCancelBtnImg")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m247a(bArr, bArr2, i, i2);
            }
            return 0;
        }

        public int m217b(byte[] bArr, byte[] bArr2) {
            if (!this.iF.m231o("tppSetupBIO")) {
                return -40;
            }
            if (!AuthFrameworkService.m227c(this.iF.mContext)) {
                return 20481;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m253b(bArr, bArr2);
            }
            return 0;
        }

        public int m219c(byte[] bArr) {
            if (!this.iF.m231o("tppSetupPIN")) {
                return -40;
            }
            if (!AuthFrameworkService.m227c(this.iF.mContext)) {
                return 20481;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m255c(bArr);
            }
            return 0;
        }

        public int m208a(boolean z, boolean z2) {
            if (!this.iF.m231o("tppVerifyPIN")) {
                return -40;
            }
            if (!AuthFrameworkService.m227c(this.iF.mContext)) {
                return 20481;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m243a(z, z2);
            }
            return 0;
        }

        public int m207a(boolean z) {
            if (!this.iF.m231o("tppResume")) {
                return -40;
            }
            if (!AuthFrameworkService.m227c(this.iF.mContext)) {
                return 20481;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m242a(z);
            }
            return 0;
        }

        public byte[] m215a(String str, byte[] bArr) {
            if (!this.iF.m231o("tppGetSecureResult")) {
                return null;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m250a(str, bArr);
            }
            return null;
        }

        public int m220d(byte[] bArr) {
            if (!this.iF.m231o("tppPreloadFpSecureResult")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m256d(bArr);
            }
            return 0;
        }

        public int m199P() {
            if (!this.iF.m231o("tppDeletePin")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m235P();
            }
            return 0;
        }

        public int m200Q() {
            if (!this.iF.m231o("tppCheckTuiSession")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m236Q();
            }
            return 0;
        }

        public int m218b(String[] strArr) {
            if (!this.iF.m231o("tppSetActionBarText")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m254b(strArr);
            }
            return 0;
        }

        public byte[] m201R() {
            if (!this.iF.m231o("tppGetNonce")) {
                return null;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m237R();
            }
            return null;
        }

        public int m202S() {
            if (!this.iF.m231o("tppClearState")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m238S();
            }
            return 0;
        }

        public int m221e(byte[] bArr) {
            if (!this.iF.m231o("tppUpdateFP")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m257e(bArr);
            }
            return 0;
        }

        public int m222f(byte[] bArr) {
            if (!this.iF.m231o("tppUpdateBIO")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m258f(bArr);
            }
            return 0;
        }

        public int m206a(String str, String str2, String str3, String str4, String str5, String str6, byte[] bArr) {
            if (!this.iF.m231o("tppInAppConfirm")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m241a(str, str2, str3, str4, str5, str6, bArr);
            }
            return 0;
        }

        public int m211a(byte[] bArr, int i, int i2, int[] iArr) {
            if (!this.iF.m231o("tppMerchantSecureDisplay")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m246a(bArr, i, i2, iArr);
            }
            return -40;
        }

        public int[] m203T() {
            int[] iArr = new int[]{-40, 0};
            if (!this.iF.m231o("tppMerchantSecureTouch")) {
                return iArr;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m239T();
            }
            return iArr;
        }

        public int m205a(int i, int[] iArr) {
            if (!this.iF.m231o("tppMerchantSecureInit")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m240a(i, iArr);
            }
            return -40;
        }

        public int m210a(byte[] bArr, int i, int i2, int i3, int i4) {
            if (!this.iF.m231o("tppMerchantSendSecureImg")) {
                return -40;
            }
            m195U();
            if (this.iA != null) {
                return this.iA.m245a(bArr, i, i2, i3, i4);
            }
            return -40;
        }

        public void m204V() {
            if (this.iF.m231o("cancelTui")) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    Log.d("AuthFrameworkService", "inside cancelTui method.");
                    m196g(4);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        private void m196g(int i) {
            long uptimeMillis = SystemClock.uptimeMillis();
            Class cls = InputManager.class;
            try {
                InputManager inputManager = (InputManager) cls.getMethod("getInstance", new Class[0]).invoke(cls, new Object[0]);
                Method method = cls.getMethod("injectInputEvent", new Class[]{InputEvent.class, Integer.TYPE});
                KeyEvent keyEvent = new KeyEvent(uptimeMillis, uptimeMillis, 0, i, 0, 0, -1, 0, 0, SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI);
                method.invoke(inputManager, new Object[]{keyEvent, Integer.valueOf(2)});
                uptimeMillis = SystemClock.uptimeMillis();
                keyEvent = new KeyEvent(uptimeMillis, uptimeMillis, 1, i, 0, 0, -1, 0, 0, SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI);
                method.invoke(inputManager, new Object[]{keyEvent, Integer.valueOf(2)});
            } catch (IllegalAccessException e) {
                com.samsung.android.spayfw.p002b.Log.m286e("AuthFrameworkService", "IllegalAccessException on injectInputEvent.");
                e.printStackTrace();
            } catch (IllegalArgumentException e2) {
                com.samsung.android.spayfw.p002b.Log.m286e("AuthFrameworkService", "IllegalArgumentException on injectInputEvent.");
                e2.printStackTrace();
            } catch (NoSuchMethodException e3) {
                com.samsung.android.spayfw.p002b.Log.m286e("AuthFrameworkService", "NoSuchMethodException on injectInputEvent.");
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                com.samsung.android.spayfw.p002b.Log.m286e("AuthFrameworkService", "InvocationTargetException on injectInputEvent.");
                e4.printStackTrace();
            }
        }
    }

    /* renamed from: com.samsung.android.spayauth.AuthFrameworkService.a */
    class C0327a implements DeathRecipient {
        final /* synthetic */ AuthFrameworkService iF;
        private IBinder iG;

        C0327a(AuthFrameworkService authFrameworkService) {
            this.iF = authFrameworkService;
        }

        public void m223a(IBinder iBinder) {
            this.iG = iBinder;
        }

        public void binderDied() {
            com.samsung.android.spayfw.p002b.Log.m286e("AuthFrameworkService", "DeathRecipient: Error: Wallet App died, handle clean up");
            PaymentProcessor.m470q(this.iF.mContext).clearCard();
            if (this.iG != null) {
                this.iG.unlinkToDeath(this, 0);
            }
            try {
                this.iF.iE.m170O();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public AuthFrameworkService() {
        this.iD = null;
        this.iE = new C03261(this);
    }

    static {
        DEBUG = Utils.DEBUG;
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
    }

    public void onCreate() {
        super.onCreate();
        this.iD = new C0327a(this);
        this.mHandler = PaymentFrameworkApp.az();
        this.mContext = getApplicationContext();
    }

    public IBinder onBind(Intent intent) {
        if (intent.getStringExtra("binder") == null || !intent.getStringExtra("binder").equals("auth")) {
            return null;
        }
        m229e(this.mContext);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            IBinder binder = extras.getBinder("deathDetectorBinder");
            if (binder != null) {
                com.samsung.android.spayfw.p002b.Log.m285d("AuthFrameworkService", "onBind: registering deathBinder : " + binder);
                try {
                    this.iD.m223a(binder);
                    binder.linkToDeath(this.iD, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        com.samsung.android.spayfw.p002b.Log.m287i("AuthFrameworkService", "return auth binder");
        return this.iE;
    }

    private boolean m231o(String str) {
        SEAMS instance = SEAMS.getInstance(getApplicationContext());
        String str2 = "PaymentFramework";
        if (instance == null) {
            com.samsung.android.spayfw.p002b.Log.m286e("AuthFrameworkService", "AuthFrameworkService.checkCallerPermission(): SEAMS is null");
            return false;
        } else if (instance.isAuthorized(Binder.getCallingPid(), Binder.getCallingUid(), str2, str) == 0) {
            com.samsung.android.spayfw.p002b.Log.m285d("AuthFrameworkService", "AuthFrameworkService.checkCallerPermission(): Access Granted");
            return true;
        } else {
            com.samsung.android.spayfw.p002b.Log.m286e("AuthFrameworkService", "AuthFrameworkService.checkCallerPermission(): Access Denied");
            return false;
        }
    }

    private static synchronized boolean m227c(Context context) {
        boolean z;
        synchronized (AuthFrameworkService.class) {
            if (m228d(context)) {
                z = true;
            } else {
                m229e(context);
                try {
                    AuthFrameworkService.class.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                z = m228d(context);
            }
        }
        return z;
    }

    private static boolean m228d(Context context) {
        String str;
        String str2 = "com.trustonic.tuiservice";
        if (bQC) {
            str = "com.qualcomm.qti.services.secureui";
        } else {
            str = str2;
        }
        com.samsung.android.spayfw.p002b.Log.m285d("AuthFrameworkService", "tuiProcessName : " + str);
        List<RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.processName.startsWith(str)) {
                    com.samsung.android.spayfw.p002b.Log.m285d("AuthFrameworkService", "TUI Service Running");
                    return true;
                }
            }
        }
        com.samsung.android.spayfw.p002b.Log.m286e("AuthFrameworkService", "TUI Service Not Running");
        return false;
    }

    private static void m229e(Context context) {
        com.samsung.android.spayfw.p002b.Log.m287i("AuthFrameworkService", "startTuiService");
        String str = "com.trustonic.tuiservice.action.restart";
        if (bQC) {
            context.sendBroadcast(new Intent(str));
        } else {
            context.sendBroadcast(new Intent(str));
        }
    }
}
