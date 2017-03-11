package com.samsung.android.spayfw.core;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.HandlerThread;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.os.Process;
import android.os.RemoteException;
import android.service.tima.ITimaService;
import com.google.android.gms.location.LocationStatusCodes;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.cncc.CNCCTAController;
import com.samsung.android.spayfw.core.retry.NetworkEventReceiver;
import com.samsung.android.spayfw.core.retry.RetryRequester;
import com.samsung.android.spayfw.fraud.FraudDataCollector;
import com.samsung.android.spayfw.fraud.FraudDataProvider;
import com.samsung.android.spayfw.p001a.UserHandleAdapter;
import com.samsung.android.spayfw.p002b.BufferedFileLogger;
import com.samsung.android.spayfw.p002b.ConsoleLogger;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.p008e.NfcControllerWrapper;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAController;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.GlobalMembershipTAController;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.p022e.SslUtils;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.storage.ServerCertsStorage;
import com.samsung.android.spayfw.storage.ServerCertsStorage.ServerCertsDb.ServerCertsColumn;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spayfw.utils.DBUtils;
import com.samsung.android.spayfw.utils.GoogleApiHelper;
import com.samsung.android.spayfw.utils.IOExceptionHandler;
import com.samsung.android.spayfw.utils.IOExceptionHandler.C0403a;
import com.samsung.android.spayfw.utils.RequestManager;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.android.spaytzsvc.api.PaymentTZServiceIF;
import com.samsung.android.spaytzsvc.api.TACommands.Init;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.android.visasdk.facade.VisaPaymentSDKImpl;
import com.visa.tainterface.VisaTAController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaymentFrameworkApp extends Application {
    private static PaymentFrameworkApp jA;
    private static PaymentFrameworkHandler jB;
    private static PaymentFrameworkHandler jC;
    private static int jD;
    private static int jE;
    private static int jF;
    private static int jG;
    protected static final List<TAController> jz;
    private String jH;
    private String jI;
    private TokenRecordStorage jJ;

    /* renamed from: com.samsung.android.spayfw.core.PaymentFrameworkApp.1 */
    static class C04071 extends C0403a<AutoCloseOutputStream> {
        final /* synthetic */ ParcelFileDescriptor jK;

        C04071(ParcelFileDescriptor parcelFileDescriptor) {
            this.jK = parcelFileDescriptor;
        }

        public /* synthetic */ Object aJ() {
            return aI();
        }

        public /* synthetic */ void m306c(Object obj) {
            m307d((AutoCloseOutputStream) obj);
        }

        public /* synthetic */ void m308d(Object obj) {
            m305c((AutoCloseOutputStream) obj);
        }

        public /* synthetic */ void m309e(Object obj) {
            m304b((AutoCloseOutputStream) obj);
        }

        public /* synthetic */ void m310f(Object obj) {
            m303a((AutoCloseOutputStream) obj);
        }

        public AutoCloseOutputStream aI() {
            return new AutoCloseOutputStream(this.jK);
        }

        public void m303a(AutoCloseOutputStream autoCloseOutputStream) {
            String str;
            Exception e;
            String str2;
            BufferedFileLogger bufferedFileLogger;
            String str3 = Build.BRAND;
            String str4 = Build.MANUFACTURER;
            String str5 = Build.MODEL;
            String str6 = VERSION.SDK;
            String str7 = BuildConfig.FLAVOR;
            int i = 0;
            PackageManager packageManager = PaymentFrameworkApp.jA.getPackageManager();
            try {
                str = packageManager.getPackageInfo(PaymentFrameworkApp.jA.getPackageName(), 64).versionName;
                try {
                    i = packageManager.getPackageInfo(PaymentFrameworkApp.jA.getPackageName(), 64).versionCode;
                } catch (Exception e2) {
                    e = e2;
                    e.printStackTrace();
                    str2 = "Brand: " + str3 + "\nManufacturer: " + str4 + "\nModel: " + str5 + "\nAPI Version: " + str6 + "\nApplication Version Name: " + str + "\nApplication Version Code: " + i + "\nBuild CL : " + "3392039" + "\nFlavour : " + BuildConfig.FLAVOR + "\nWallet Id : " + ConfigurationManager.m581h(PaymentFrameworkApp.jA).getConfig(PaymentFramework.CONFIG_WALLET_ID) + "\nDevice Id : " + DeviceInfo.getDeviceId(PaymentFrameworkApp.jA) + "\n";
                    str = "---------------------------------------------------------------\n";
                    autoCloseOutputStream.write("---------------------------------------------------------------\n".getBytes());
                    autoCloseOutputStream.write(str2.getBytes());
                    autoCloseOutputStream.write("---------------------------------------------------------------\n".getBytes());
                    bufferedFileLogger = (BufferedFileLogger) Log.an(VisaPaymentSDKImpl.FILE_LOGGER);
                    if (bufferedFileLogger != null) {
                        Log.m286e("PaymentFrameworkApp", "No FILE LOGGER.");
                    } else {
                        bufferedFileLogger.m277a((FileOutputStream) autoCloseOutputStream);
                    }
                }
            } catch (Exception e3) {
                Exception exception = e3;
                str = str7;
                e = exception;
                e.printStackTrace();
                str2 = "Brand: " + str3 + "\nManufacturer: " + str4 + "\nModel: " + str5 + "\nAPI Version: " + str6 + "\nApplication Version Name: " + str + "\nApplication Version Code: " + i + "\nBuild CL : " + "3392039" + "\nFlavour : " + BuildConfig.FLAVOR + "\nWallet Id : " + ConfigurationManager.m581h(PaymentFrameworkApp.jA).getConfig(PaymentFramework.CONFIG_WALLET_ID) + "\nDevice Id : " + DeviceInfo.getDeviceId(PaymentFrameworkApp.jA) + "\n";
                str = "---------------------------------------------------------------\n";
                autoCloseOutputStream.write("---------------------------------------------------------------\n".getBytes());
                autoCloseOutputStream.write(str2.getBytes());
                autoCloseOutputStream.write("---------------------------------------------------------------\n".getBytes());
                bufferedFileLogger = (BufferedFileLogger) Log.an(VisaPaymentSDKImpl.FILE_LOGGER);
                if (bufferedFileLogger != null) {
                    bufferedFileLogger.m277a((FileOutputStream) autoCloseOutputStream);
                } else {
                    Log.m286e("PaymentFrameworkApp", "No FILE LOGGER.");
                }
            }
            str2 = "Brand: " + str3 + "\nManufacturer: " + str4 + "\nModel: " + str5 + "\nAPI Version: " + str6 + "\nApplication Version Name: " + str + "\nApplication Version Code: " + i + "\nBuild CL : " + "3392039" + "\nFlavour : " + BuildConfig.FLAVOR + "\nWallet Id : " + ConfigurationManager.m581h(PaymentFrameworkApp.jA).getConfig(PaymentFramework.CONFIG_WALLET_ID) + "\nDevice Id : " + DeviceInfo.getDeviceId(PaymentFrameworkApp.jA) + "\n";
            str = "---------------------------------------------------------------\n";
            try {
                autoCloseOutputStream.write("---------------------------------------------------------------\n".getBytes());
                autoCloseOutputStream.write(str2.getBytes());
                autoCloseOutputStream.write("---------------------------------------------------------------\n".getBytes());
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            bufferedFileLogger = (BufferedFileLogger) Log.an(VisaPaymentSDKImpl.FILE_LOGGER);
            if (bufferedFileLogger != null) {
                bufferedFileLogger.m277a((FileOutputStream) autoCloseOutputStream);
            } else {
                Log.m286e("PaymentFrameworkApp", "No FILE LOGGER.");
            }
        }

        public void m304b(AutoCloseOutputStream autoCloseOutputStream) {
            autoCloseOutputStream.flush();
        }

        public void m305c(AutoCloseOutputStream autoCloseOutputStream) {
            autoCloseOutputStream.getFD().sync();
        }

        public void m307d(AutoCloseOutputStream autoCloseOutputStream) {
            autoCloseOutputStream.close();
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.PaymentFrameworkApp.2 */
    class C04082 implements Runnable {
        final /* synthetic */ Card jL;
        final /* synthetic */ PaymentFrameworkApp jM;

        C04082(PaymentFrameworkApp paymentFrameworkApp, Card card) {
            this.jM = paymentFrameworkApp;
            this.jL = card;
        }

        public void run() {
            this.jL.ad().setupReplenishAlarm(this.jL);
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.PaymentFrameworkApp.a */
    public static class C0409a implements PaymentFrameworkRequester {
        public void m313a(ProviderTokenKey providerTokenKey) {
            if (PaymentFrameworkApp.jB != null) {
                PaymentFrameworkApp.jB.m618a(PaymentFrameworkMessage.m620a(11, providerTokenKey.getTrTokenId(), null));
                return;
            }
            Log.m286e("PaymentFrameworkApp", "HANDLER IS NOT INITIAILIZED");
        }

        public void m314b(ProviderTokenKey providerTokenKey) {
            Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
            intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_UPDATE_PAYREADY_STATE);
            intent.putExtra(PaymentFramework.EXTRA_TOKEN_ID, providerTokenKey.getTrTokenId());
            PaymentFrameworkApp.m315a(intent);
        }
    }

    static {
        jz = new ArrayList();
        jD = 0;
        jE = 1;
        jF = jE;
        jG = 0;
    }

    public static boolean ax() {
        return (TokenRecordStorage.ae(jA).fu() <= 0 && ConfigurationManager.m581h(jA).getConfig(PaymentFramework.CONFIG_JWT_TOKEN) == null && ConfigurationManager.m581h(jA).getConfig(PaymentFramework.CONFIG_WALLET_ID) == null) ? false : true;
    }

    public static void m317a(Class<?> cls) {
        jA.getPackageManager().setComponentEnabledSetting(new ComponentName(jA, cls), 2, 1);
    }

    public static void m318b(Class<?> cls) {
        jA.getPackageManager().setComponentEnabledSetting(new ComponentName(jA, cls), 1, 1);
    }

    public static int m319c(Class<?> cls) {
        return jA.getPackageManager().getComponentEnabledSetting(new ComponentName(jA, cls));
    }

    public static void m320d(Class<?> cls) {
        jA.getPackageManager().setComponentEnabledSetting(new ComponentName(jA, cls), 0, 1);
    }

    public static int ay() {
        return jG;
    }

    public static PaymentFrameworkHandler az() {
        return jB;
    }

    public static PaymentFrameworkHandler aA() {
        return jC;
    }

    public static PaymentFrameworkApp aB() {
        return jA;
    }

    public static void m315a(Intent intent) {
        if (intent != null) {
            jA.sendBroadcast(intent, "com.samsung.android.spayfw.permission.UPDATE_NOTIFICATION");
            Log.m287i("PaymentFrameworkApp", "sent broadcast: action: " + intent.getAction() + " type: " + intent.getStringExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE));
        }
    }

    public static void m316a(ParcelFileDescriptor parcelFileDescriptor, String str, ICommonCallback iCommonCallback) {
        try {
            IOExceptionHandler.m1262a(new C04071(parcelFileDescriptor), true);
        } catch (Throwable e) {
            Log.m284c("PaymentFrameworkApp", e.getMessage(), e);
            if (iCommonCallback != null) {
                try {
                    iCommonCallback.onFail(null, -1);
                } catch (RemoteException e2) {
                    e2.printStackTrace();
                }
            }
        }
        if (iCommonCallback != null) {
            try {
                iCommonCallback.onSuccess(str);
            } catch (Throwable e3) {
                Log.m284c("PaymentFrameworkApp", e3.getMessage(), e3);
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new SpayUncaughtExceptionHandler());
        Log.m282a(new ConsoleLogger(VisaPaymentSDKImpl.CONSOLE_LOGGER));
        Log.m282a(new BufferedFileLogger(getApplicationContext(), VisaPaymentSDKImpl.FILE_LOGGER));
        Log.m285d("PaymentFrameworkApp", "PaymentFrameworkApp: onCreate Start");
        jA = this;
        DBUtils.setContext(jA);
        init();
        Log.m285d("PaymentFrameworkApp", "PaymentFrameworkApp : onCreate End");
    }

    public List<TAController> aC() {
        return jz;
    }

    public boolean isReady() {
        Log.m285d("PaymentFrameworkApp", "PFState: " + jF);
        return jF == jD;
    }

    public synchronized boolean init() {
        Exception exception;
        boolean z = true;
        synchronized (this) {
            Log.m285d("PaymentFrameworkApp", "initPF: PF state: " + jF);
            if (jF == jD) {
                Log.m287i("PaymentFrameworkApp", "PF init already done");
            } else {
                int myUserId = UserHandleAdapter.myUserId();
                if (myUserId != UserHandleAdapter.USER_OWNER) {
                    Log.m286e("PaymentFrameworkApp", "initPF: application only allowed on USER_OWNER but current user id :" + myUserId);
                    z = false;
                } else {
                    Log.m287i("PaymentFrameworkApp", "Build CL: 3392039");
                    Log.m287i("PaymentFrameworkApp", "Build Flavor: ");
                    Log.m285d("PaymentFrameworkApp", "Initializing TZ interface!");
                    PaymentTZServiceIF instance = PaymentTZServiceIF.getInstance();
                    if (CNCCTAController.isSupported(jA)) {
                        jz.add(CNCCTAController.createOnlyInstance(jA));
                    }
                    jz.add(VisaTAController.bv(jA));
                    jz.add(McTAController.createOnlyInstance(jA));
                    if (Utils.fS()) {
                        jz.add(AmexTAController.m780C(jA));
                    } else {
                        jz.add(com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexTAController.m810D(jA));
                    }
                    jz.add(PlccTAController.createOnlyInstance(jA));
                    jz.add(SpayTuiTAController.createOnlyInstance(jA));
                    jz.add(DcTAController.m1039E(jA));
                    jz.add(GlobalMembershipTAController.m1066F(jA));
                    SpayTuiTAController.getInstance().setTAs(jz);
                    if (!Utils.fR()) {
                        aF();
                    }
                    Log.m285d("PaymentFrameworkApp", "initPF: Create efs directory based on UID = " + Process.myUid());
                    if (Process.myUid() == LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
                        String efsDirectory = TAController.getEfsDirectory();
                        Log.m285d("PaymentFrameworkApp", "initPF: path = " + efsDirectory);
                        File file = new File(efsDirectory);
                        if (file.exists() || file.mkdir()) {
                            try {
                                if (!(file.canWrite() && file.canRead())) {
                                    Log.m286e("PaymentFrameworkApp", "initPF: Cannot read or write to efs directory! No permissions!");
                                    z = false;
                                }
                            } catch (Exception e) {
                                Exception exception2 = e;
                                z = false;
                                exception = exception2;
                                Log.m286e("PaymentFrameworkApp", "initPF: failed with exception");
                                exception.printStackTrace();
                                return z;
                            }
                        }
                        Log.m286e("PaymentFrameworkApp", "initPF: Error creating efs directory for PF!");
                        z = false;
                    }
                    if (instance.init(jz)) {
                        Log.m285d("PaymentFrameworkApp", "initPF:Payment Service Version : " + instance.getVersion());
                        Log.m285d("PaymentFrameworkApp", "initPF: load/unload tui TA to create/write pin random if necessary");
                        try {
                            SpayTuiTAController createOnlyInstance = SpayTuiTAController.createOnlyInstance(getApplicationContext());
                            if (createOnlyInstance != null) {
                                createOnlyInstance.loadTA();
                                createOnlyInstance.unloadTA();
                            }
                        } catch (Throwable e2) {
                            Log.m284c("PaymentFrameworkApp", e2.getMessage(), e2);
                        }
                        Log.m285d("PaymentFrameworkApp", "initPF: Initializing tima keystore! needed to get db key ");
                        ITimaService fF = DBUtils.fF();
                        if (fF == null || !"3.0".equals(fF.getTimaVersion())) {
                            Log.m286e("PaymentFrameworkApp", "initPF:unable to get timaService instance");
                            z = false;
                        } else {
                            Log.m285d("PaymentFrameworkApp", "initPF: - Tima Version : 3.0");
                            myUserId = fF.KeyStore3_init();
                            Log.m287i("PaymentFrameworkApp", "initPF: timaStatus = " + myUserId);
                            if (myUserId == 0) {
                                Log.m287i("PaymentFrameworkApp", "Device Integrity Verification success");
                                if (!Utils.fM()) {
                                    jG = -42;
                                    Log.m286e("PaymentFrameworkApp", "initPF:security patch update date verification failed");
                                    z = false;
                                } else if (CNCCTAController.bringup(jA)) {
                                    aD();
                                    this.jJ = TokenRecordStorage.ae(jA);
                                    if (this.jJ != null) {
                                        Log.m287i("PaymentFrameworkApp", "initPF:token count:" + this.jJ.fu());
                                    }
                                    Log.m285d("PaymentFrameworkApp", "initPF: Building ConfigurationManager config cache! ");
                                    ConfigurationManager.m581h(jA).ae();
                                    HandlerThread handlerThread = new HandlerThread("PaymentFramework");
                                    Log.m285d("PaymentFrameworkApp", "initPF:PF service worker thread is started");
                                    handlerThread.start();
                                    jB = new PaymentFrameworkHandler(jA, handlerThread.getLooper());
                                    handlerThread = new HandlerThread("restoreHandler");
                                    Log.m285d("PaymentFrameworkApp", "initPF:PF service worker thread for RESTORE_HANDLER is started");
                                    handlerThread.start();
                                    jC = new PaymentFrameworkHandler(jA, handlerThread.getLooper());
                                    this.jH = SpayTuiTAController.getInstance().getScreenDensity();
                                    this.jI = SpayTuiTAController.getInstance().getLocale();
                                    if (this.jJ != null && this.jJ.fu() > 0) {
                                        prepare();
                                    }
                                    if (Utils.fR()) {
                                        FactoryResetDetector.disable();
                                    } else {
                                        try {
                                            NfcControllerWrapper.ar(jA);
                                            NfcControllerWrapper.fi();
                                        } catch (Exception e3) {
                                            Log.m286e("PaymentFrameworkApp", e3.getMessage());
                                        }
                                        jA.registerReceiver(new NetworkEventReceiver(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                                        RetryRequester.m673e(true);
                                        Log.m285d("PaymentFrameworkApp", "initPF: Prepare MST pay config data");
                                        PayConfigurator.m616k(jA);
                                        BinAttribute.init(jA);
                                    }
                                    if (Utils.fQ()) {
                                        GoogleApiHelper.ag(jA).fJ();
                                    }
                                    jF = jD;
                                    try {
                                        if ("US".equalsIgnoreCase(Utils.fP())) {
                                            Intent intent = new Intent();
                                            intent.setClassName(jA, "com.samsung.contextservice.system.ContextService");
                                            startService(intent);
                                            Log.m285d("PaymentFrameworkApp", "start CTX service");
                                        } else {
                                            Log.m285d("PaymentFrameworkApp", "CTX service will not start");
                                        }
                                        RequestManager.m1267a(getApplicationContext(), Integer.valueOf(10));
                                        aE();
                                        Log.m287i("PaymentFrameworkApp", "initPF: success without any error");
                                    } catch (Exception e4) {
                                        exception = e4;
                                        Log.m286e("PaymentFrameworkApp", "initPF: failed with exception");
                                        exception.printStackTrace();
                                        return z;
                                    }
                                } else {
                                    Log.m286e("PaymentFrameworkApp", "initPF: Error - CNCC Bringup failed");
                                    z = false;
                                }
                            } else {
                                Log.m286e("PaymentFrameworkApp", "Device Integrity Verification failed");
                                if (myUserId == Init.TZ_COMMON_INIT_ERROR_TAMPER_FUSE_FAIL || myUserId == Init.TZ_COMMON_INIT_MSR_MISMATCH || myUserId == Init.TZ_COMMON_INIT_MSR_MODIFIED) {
                                    Log.m286e("PaymentFrameworkApp", "Device Integrity Compromised");
                                } else {
                                    Log.m286e("PaymentFrameworkApp", "Unable to establish communication with TZ. Kill PF");
                                    Process.killProcess(Process.myPid());
                                }
                                z = false;
                            }
                        }
                    } else {
                        Log.m286e("PaymentFrameworkApp", "initPF:Payment Service Init failed");
                        z = false;
                    }
                }
            }
        }
        return z;
    }

    public void onConfigurationChanged(Configuration configuration) {
        Log.m285d("PaymentFrameworkApp", "onConfigurationChanged");
        super.onConfigurationChanged(configuration);
        String screenDensity = SpayTuiTAController.getInstance().getScreenDensity();
        String locale = SpayTuiTAController.getInstance().getLocale();
        Log.m285d("PaymentFrameworkApp", "original screenDpi " + this.jH);
        Log.m285d("PaymentFrameworkApp", "new screenDpi " + screenDensity);
        if (!screenDensity.equals(this.jH)) {
            Log.m285d("PaymentFrameworkApp", "onConfigurationChanged. Screen DPI changed Killing PF! " + Process.myPid());
            Process.killProcess(Process.myPid());
        }
        Log.m285d("PaymentFrameworkApp", "original Locale " + this.jI);
        Log.m285d("PaymentFrameworkApp", "new Locale " + locale);
        if (!locale.equals(this.jI)) {
            Log.m285d("PaymentFrameworkApp", "onConfigurationChanged. Locale changed Killing PF! " + Process.myPid());
            Process.killProcess(Process.myPid());
        }
    }

    private void prepare() {
        int i = 0;
        List fv = this.jJ.fv();
        if (fv == null || fv.isEmpty()) {
            Log.m286e("PaymentFrameworkApp", "Ids are null");
            return;
        }
        String userId;
        Card a;
        Log.m285d("PaymentFrameworkApp", "prepare:  number of Enrollments: " + fv.size());
        String config = ConfigurationManager.m581h(jA).getConfig(PaymentFramework.CONFIG_USER_ID);
        if (config == null) {
            TokenRecord bp = this.jJ.bp((String) fv.get(0));
            if (!(bp == null || bp.getUserId() == null)) {
                userId = bp.getUserId();
                Log.m287i("PaymentFrameworkApp", "Migrate User Id");
                ConfigurationManager.m581h(jA).setConfig(PaymentFramework.CONFIG_USER_ID, userId);
                if (userId != null) {
                    Log.m286e("PaymentFrameworkApp", "user id is null");
                }
                Account a2 = Account.m551a(jA, userId);
                while (i < fv.size()) {
                    try {
                        a = ResponseDataBuilder.m633a(jA, this.jJ.bp((String) fv.get(i)));
                    } catch (Throwable e) {
                        Log.m284c("PaymentFrameworkApp", e.getMessage(), e);
                        if (e.getErrorCode() == 2) {
                            jG = -42;
                            a = null;
                        } else {
                            jG = -41;
                            a = null;
                        }
                    }
                    if (a != null || a.ad() == null) {
                        Log.m286e("PaymentFrameworkApp", "unable to create card " + ((String) fv.get(i)));
                    } else {
                        List a3;
                        a.ad().setPaymentFrameworkRequester(new C0409a());
                        a.ad().setEnrollmentId(a.getEnrollmentId());
                        if (a.ac().aQ() != null) {
                            a.ad().setProviderTokenKey(a.ac().aQ());
                            a.ad().setPFTokenStatus(a.ac().getTokenStatus());
                        } else if (a.ac().getTokenId() != null) {
                            ProviderTokenKey providerTokenKey = new ProviderTokenKey(null);
                            providerTokenKey.setTrTokenId(a.ac().getTokenId());
                            a.ad().setProviderTokenKey(providerTokenKey);
                        }
                        a2.m556a(a);
                        ServerCertsStorage ad = ServerCertsStorage.ad(jA);
                        if (ad != null) {
                            a3 = ad.m1220a(ServerCertsColumn.CARD_TYPE, a.getCardBrand());
                        } else {
                            a3 = null;
                        }
                        if (a3 == null || a3.isEmpty()) {
                            Log.m286e("PaymentFrameworkApp", "No certs stored for current card");
                        } else {
                            Log.m285d("PaymentFrameworkApp", "setServerCertificates for : " + a.getCardBrand());
                            a.ad().setServerCertificates((CertificateInfo[]) a3.toArray(new CertificateInfo[a3.size()]));
                        }
                        jB.post(new C04082(this, a));
                        Log.m287i("PaymentFrameworkApp", "prepare: card added");
                    }
                    i++;
                }
                return;
            }
        }
        userId = config;
        if (userId != null) {
            Account a22 = Account.m551a(jA, userId);
            while (i < fv.size()) {
                a = ResponseDataBuilder.m633a(jA, this.jJ.bp((String) fv.get(i)));
                if (a != null) {
                }
                Log.m286e("PaymentFrameworkApp", "unable to create card " + ((String) fv.get(i)));
                i++;
            }
            return;
        }
        Log.m286e("PaymentFrameworkApp", "user id is null");
    }

    private void aD() {
        try {
            if (getDatabasePath("spayfw_enc.db").exists()) {
                Log.m285d("PaymentFrameworkApp", "FraudCollector: DB Exists");
                return;
            }
            FraudDataCollector x = FraudDataCollector.m718x(this);
            if (x == null) {
                Log.m285d("PaymentFrameworkApp", "FraudCollector: addFDeviceRecord cannot get data");
            } else if (m319c(FactoryResetDetector.class) != 0) {
                Log.m285d("PaymentFrameworkApp", "Fraud: add a framework reset record");
                x.m719W("framework_reset");
            } else {
                Log.m285d("PaymentFrameworkApp", "Fraud: add a factory reset record");
                x.m719W("factory_reset");
            }
        } catch (Exception e) {
            Log.m286e("PaymentFrameworkApp", "cannot add factory reset record");
        }
    }

    private void aE() {
        String config = ConfigurationManager.m581h(jA).getConfig(PaymentFramework.CONFIG_PF_INSTANCE_ID);
        Log.m285d("PaymentFrameworkApp", "Instance ID : " + config);
        if (config == null || config.isEmpty()) {
            Log.m287i("PaymentFrameworkApp", "Generating Instance ID");
            DecimalFormat decimalFormat = new DecimalFormat("000");
            config = SslUtils.m1191N(jA);
            if (config != null) {
                int C;
                try {
                    C = new FraudDataProvider(jA).m740C(1000000);
                    Log.m285d("PaymentFrameworkApp", "resetCount : " + C);
                    config = config + decimalFormat.format((long) C);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                C = Calendar.getInstance().get(6);
                Log.m285d("PaymentFrameworkApp", "dayOfYear : " + C);
                config = config + decimalFormat.format((long) C);
                Log.m285d("PaymentFrameworkApp", "Instance ID : " + config);
                ConfigurationManager.m581h(jA).setConfig(PaymentFramework.CONFIG_PF_INSTANCE_ID, config);
            }
        }
    }

    private void aF() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PFInitState", 0);
        if (sharedPreferences.getBoolean("PFInitState", false)) {
            Log.m285d("PaymentFrameworkApp", "PF has already been initialized");
            return;
        }
        Log.m287i("PaymentFrameworkApp", "PF init state shared pref is false");
        Log.m287i("PaymentFrameworkApp", "DB File Exists?" + getDatabasePath("spayfw_enc.db").exists());
        if (!getDatabasePath("spayfw_enc.db").exists()) {
            Log.m287i("PaymentFrameworkApp", "SPayfw db file does not exist. Delete Pin data");
            SpayTuiTAController.getInstance().deletePin();
        }
        Log.m285d("PaymentFrameworkApp", "PF init state shared pref set to true");
        sharedPreferences.edit().putBoolean("PFInitState", true).apply();
    }
}
