/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.content.pm.PackageInfo
 *  android.content.pm.PackageManager
 *  android.content.res.Configuration
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.HandlerThread
 *  android.os.Looper
 *  android.os.Message
 *  android.os.ParcelFileDescriptor
 *  android.os.ParcelFileDescriptor$AutoCloseOutputStream
 *  android.os.Process
 *  android.os.RemoteException
 *  android.service.tima.ITimaService
 *  java.io.File
 *  java.io.FileDescriptor
 *  java.io.FileOutputStream
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.Thread
 *  java.lang.Thread$UncaughtExceptionHandler
 *  java.lang.Throwable
 *  java.text.DecimalFormat
 *  java.util.ArrayList
 *  java.util.Calendar
 *  java.util.List
 */
package com.samsung.android.spayfw.core;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.HandlerThread;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.service.tima.ITimaService;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.cncc.CNCCTAController;
import com.samsung.android.spayfw.core.retry.b;
import com.samsung.android.spayfw.core.retry.d;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.storage.ServerCertsStorage;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import com.samsung.android.spayfw.utils.IOExceptionHandler;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.android.spaytzsvc.api.PaymentTZServiceIF;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAException;
import com.visa.tainterface.VisaTAController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaymentFrameworkApp
extends Application {
    private static PaymentFrameworkApp jA;
    private static i jB;
    private static i jC;
    private static int jD;
    private static int jE;
    private static int jF;
    private static int jG;
    protected static final List<TAController> jz;
    private String jH;
    private String jI;
    private TokenRecordStorage jJ;

    static {
        jz = new ArrayList();
        jD = 0;
        jF = jE = 1;
        jG = 0;
    }

    public static void a(Intent intent) {
        if (intent != null) {
            jA.sendBroadcast(intent, "com.samsung.android.spayfw.permission.UPDATE_NOTIFICATION");
            Log.i("PaymentFrameworkApp", "sent broadcast: action: " + intent.getAction() + " type: " + intent.getStringExtra("notiType"));
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static void a(final ParcelFileDescriptor parcelFileDescriptor, String string, ICommonCallback iCommonCallback) {
        block6 : {
            try {
                IOExceptionHandler.a(new IOExceptionHandler.a<ParcelFileDescriptor.AutoCloseOutputStream>(){

                    /*
                     * Loose catch block
                     * Enabled aggressive block sorting
                     * Enabled unnecessary exception pruning
                     * Enabled aggressive exception aggregation
                     * Lifted jumps to return sites
                     */
                    public void a(ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream) {
                        com.samsung.android.spayfw.b.a a2;
                        String string5;
                        String string2;
                        String string3;
                        String string;
                        int n2;
                        String string4;
                        block7 : {
                            string3 = Build.BRAND;
                            string = Build.MANUFACTURER;
                            string2 = Build.MODEL;
                            string5 = Build.VERSION.SDK;
                            PackageManager packageManager = jA.getPackageManager();
                            string4 = packageManager.getPackageInfo((String)PaymentFrameworkApp.aH().getPackageName(), (int)64).versionName;
                            n2 = packageManager.getPackageInfo((String)PaymentFrameworkApp.aH().getPackageName(), (int)64).versionCode;
                            break block7;
                            {
                                catch (Exception exception) {}
                            }
                            catch (Exception exception) {
                                string4 = "";
                                Exception exception2 = exception;
                                exception2.printStackTrace();
                                n2 = 0;
                            }
                        }
                        String string6 = "Brand: " + string3 + "\nManufacturer: " + string + "\nModel: " + string2 + "\nAPI Version: " + string5 + "\nApplication Version Name: " + string4 + "\nApplication Version Code: " + n2 + "\nBuild CL : " + "3392039" + "\nFlavour : " + "" + "\nWallet Id : " + e.h((Context)jA).getConfig("CONFIG_WALLET_ID") + "\nDevice Id : " + DeviceInfo.getDeviceId((Context)jA) + "\n";
                        try {
                            autoCloseOutputStream.write("---------------------------------------------------------------\n".getBytes());
                            autoCloseOutputStream.write(string6.getBytes());
                            autoCloseOutputStream.write("---------------------------------------------------------------\n".getBytes());
                        }
                        catch (IOException iOException) {
                            iOException.printStackTrace();
                        }
                        if ((a2 = (com.samsung.android.spayfw.b.a) Log.an("File-Logger")) != null) {
                            a2.a((FileOutputStream)autoCloseOutputStream);
                            return;
                        }
                        Log.e("PaymentFrameworkApp", "No FILE LOGGER.");
                    }

                    public ParcelFileDescriptor.AutoCloseOutputStream aI() {
                        return new ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor);
                    }

                    @Override
                    public /* synthetic */ Object aJ() {
                        return this.aI();
                    }

                    public void b(ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream) {
                        autoCloseOutputStream.flush();
                    }

                    @Override
                    public void c(ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream) {
                        autoCloseOutputStream.getFD().sync();
                    }

                    @Override
                    public /* synthetic */ void c(Object object) {
                        this.d((ParcelFileDescriptor.AutoCloseOutputStream)object);
                    }

                    @Override
                    public void d(ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream) {
                        autoCloseOutputStream.close();
                    }

                    @Override
                    public /* synthetic */ void d(Object object) {
                        this.c((ParcelFileDescriptor.AutoCloseOutputStream)object);
                    }

                    @Override
                    public /* synthetic */ void e(Object object) {
                        this.b((ParcelFileDescriptor.AutoCloseOutputStream)object);
                    }

                    @Override
                    public /* synthetic */ void f(Object object) {
                        this.a((ParcelFileDescriptor.AutoCloseOutputStream)object);
                    }
                }, true);
            }
            catch (Exception exception) {
                Log.c("PaymentFrameworkApp", exception.getMessage(), exception);
                if (iCommonCallback == null) break block6;
                try {
                    iCommonCallback.onFail(null, -1);
                }
                catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }
        }
        if (iCommonCallback == null) return;
        try {
            iCommonCallback.onSuccess(string);
            return;
        }
        catch (RemoteException remoteException) {
            Log.c("PaymentFrameworkApp", remoteException.getMessage(), remoteException);
            return;
        }
    }

    public static void a(Class<?> class_) {
        jA.getPackageManager().setComponentEnabledSetting(new ComponentName((Context)jA, class_), 2, 1);
    }

    public static i aA() {
        return jC;
    }

    public static PaymentFrameworkApp aB() {
        return jA;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void aD() {
        try {
            if (!this.getDatabasePath("spayfw_enc.db").exists()) {
                com.samsung.android.spayfw.fraud.a a2 = com.samsung.android.spayfw.fraud.a.x((Context)this);
                if (a2 != null) {
                    if (PaymentFrameworkApp.c(FactoryResetDetector.class) != 0) {
                        Log.d("PaymentFrameworkApp", "Fraud: add a framework reset record");
                        a2.W("framework_reset");
                        return;
                    }
                    Log.d("PaymentFrameworkApp", "Fraud: add a factory reset record");
                    a2.W("factory_reset");
                    return;
                }
                Log.d("PaymentFrameworkApp", "FraudCollector: addFDeviceRecord cannot get data");
                return;
            }
        }
        catch (Exception exception) {
            Log.e("PaymentFrameworkApp", "cannot add factory reset record");
            return;
        }
        Log.d("PaymentFrameworkApp", "FraudCollector: DB Exists");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void aE() {
        String string;
        DecimalFormat decimalFormat;
        block5 : {
            block4 : {
                String string2 = e.h((Context)jA).getConfig("CONFIG_PF_INSTANCE_ID");
                Log.d("PaymentFrameworkApp", "Instance ID : " + string2);
                if (string2 != null && !string2.isEmpty()) break block4;
                Log.i("PaymentFrameworkApp", "Generating Instance ID");
                decimalFormat = new DecimalFormat("000");
                string = com.samsung.android.spayfw.remoteservice.e.c.N((Context)jA);
                if (string != null) break block5;
            }
            return;
        }
        com.samsung.android.spayfw.fraud.b b2 = new com.samsung.android.spayfw.fraud.b((Context)jA);
        try {
            String string3;
            int n2 = b2.C(1000000);
            Log.d("PaymentFrameworkApp", "resetCount : " + n2);
            string = string3 = string + decimalFormat.format((long)n2);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        int n3 = Calendar.getInstance().get(6);
        Log.d("PaymentFrameworkApp", "dayOfYear : " + n3);
        String string4 = string + decimalFormat.format((long)n3);
        Log.d("PaymentFrameworkApp", "Instance ID : " + string4);
        e.h((Context)jA).setConfig("CONFIG_PF_INSTANCE_ID", string4);
    }

    private void aF() {
        SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences("PFInitState", 0);
        if (sharedPreferences.getBoolean("PFInitState", false)) {
            Log.d("PaymentFrameworkApp", "PF has already been initialized");
            return;
        }
        Log.i("PaymentFrameworkApp", "PF init state shared pref is false");
        Log.i("PaymentFrameworkApp", "DB File Exists?" + this.getDatabasePath("spayfw_enc.db").exists());
        if (!this.getDatabasePath("spayfw_enc.db").exists()) {
            Log.i("PaymentFrameworkApp", "SPayfw db file does not exist. Delete Pin data");
            SpayTuiTAController.getInstance().deletePin();
        }
        Log.d("PaymentFrameworkApp", "PF init state shared pref set to true");
        sharedPreferences.edit().putBoolean("PFInitState", true).apply();
    }

    public static boolean ax() {
        return TokenRecordStorage.ae((Context)jA).fu() > 0 || e.h((Context)jA).getConfig("CONFIG_JWT_TOKEN") != null || e.h((Context)jA).getConfig("CONFIG_WALLET_ID") != null;
    }

    public static int ay() {
        return jG;
    }

    public static i az() {
        return jB;
    }

    public static void b(Class<?> class_) {
        jA.getPackageManager().setComponentEnabledSetting(new ComponentName((Context)jA, class_), 1, 1);
    }

    public static int c(Class<?> class_) {
        return jA.getPackageManager().getComponentEnabledSetting(new ComponentName((Context)jA, class_));
    }

    public static void d(Class<?> class_) {
        jA.getPackageManager().setComponentEnabledSetting(new ComponentName((Context)jA, class_), 0, 1);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void prepare() {
        String string;
        com.samsung.android.spayfw.storage.models.a a2;
        int n2 = 0;
        List<String> list = this.jJ.fv();
        if (list == null || list.isEmpty()) {
            Log.e("PaymentFrameworkApp", "Ids are null");
            return;
        }
        Log.d("PaymentFrameworkApp", "prepare:  number of Enrollments: " + list.size());
        String string2 = e.h((Context)jA).getConfig("CONFIG_USER_ID");
        if (string2 == null && (a2 = this.jJ.bp((String)list.get(0))) != null && a2.getUserId() != null) {
            string = a2.getUserId();
            Log.i("PaymentFrameworkApp", "Migrate User Id");
            e.h((Context)jA).setConfig("CONFIG_USER_ID", string);
        } else {
            string = string2;
        }
        if (string == null) {
            Log.e("PaymentFrameworkApp", "user id is null");
            return;
        }
        com.samsung.android.spayfw.core.a a3 = com.samsung.android.spayfw.core.a.a((Context)jA, string);
        while (n2 < list.size()) {
            c c2;
            com.samsung.android.spayfw.storage.models.a a4 = this.jJ.bp((String)list.get(n2));
            try {
                c c3;
                c2 = c3 = m.a((Context)jA, a4);
            }
            catch (TAException tAException) {
                Log.c("PaymentFrameworkApp", tAException.getMessage(), (Throwable)((Object)tAException));
                if (tAException.getErrorCode() == 2) {
                    jG = -42;
                    c2 = null;
                }
                jG = -41;
                c2 = null;
            }
            if (c2 == null || c2.ad() == null) {
                Log.e("PaymentFrameworkApp", "unable to create card " + (String)list.get(n2));
            } else {
                c2.ad().setPaymentFrameworkRequester(new a());
                c2.ad().setEnrollmentId(c2.getEnrollmentId());
                if (c2.ac().aQ() != null) {
                    c2.ad().setProviderTokenKey(c2.ac().aQ());
                    c2.ad().setPFTokenStatus(c2.ac().getTokenStatus());
                } else if (c2.ac().getTokenId() != null) {
                    f f2 = new f(null);
                    f2.setTrTokenId(c2.ac().getTokenId());
                    c2.ad().setProviderTokenKey(f2);
                }
                a3.a(c2);
                ServerCertsStorage serverCertsStorage = ServerCertsStorage.ad((Context)jA);
                List<CertificateInfo> list2 = serverCertsStorage != null ? serverCertsStorage.a(ServerCertsStorage.ServerCertsDb.ServerCertsColumn.Cg, c2.getCardBrand()) : null;
                if (list2 != null && !list2.isEmpty()) {
                    Log.d("PaymentFrameworkApp", "setServerCertificates for : " + c2.getCardBrand());
                    c2.ad().setServerCertificates((CertificateInfo[])list2.toArray((Object[])new CertificateInfo[list2.size()]));
                } else {
                    Log.e("PaymentFrameworkApp", "No certs stored for current card");
                }
                jB.post(new Runnable(){

                    public void run() {
                        c2.ad().setupReplenishAlarm(c2);
                    }
                });
                Log.i("PaymentFrameworkApp", "prepare: card added");
            }
            ++n2;
        }
        return;
    }

    public List<TAController> aC() {
        return jz;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public boolean init() {
        block36 : {
            var1_1 = true;
            var31_2 = this;
            // MONITORENTER : var31_2
            Log.d("PaymentFrameworkApp", "initPF: PF state: " + PaymentFrameworkApp.jF);
            if (PaymentFrameworkApp.jF == PaymentFrameworkApp.jD) {
                Log.i("PaymentFrameworkApp", "PF init already done");
                // MONITOREXIT : var31_2
                return var1_1;
            }
            var5_3 = com.samsung.android.spayfw.a.b.myUserId();
            if (var5_3 != com.samsung.android.spayfw.a.b.USER_OWNER) {
                Log.e("PaymentFrameworkApp", "initPF: application only allowed on USER_OWNER but current user id :" + var5_3);
                return false;
            }
            Log.i("PaymentFrameworkApp", "Build CL: 3392039");
            Log.i("PaymentFrameworkApp", "Build Flavor: ");
            Log.d("PaymentFrameworkApp", "Initializing TZ interface!");
            var6_4 = PaymentTZServiceIF.getInstance();
            if (CNCCTAController.isSupported((Context)PaymentFrameworkApp.jA)) {
                PaymentFrameworkApp.jz.add((Object)CNCCTAController.createOnlyInstance((Context)PaymentFrameworkApp.jA));
            }
            PaymentFrameworkApp.jz.add((Object)VisaTAController.bv((Context)PaymentFrameworkApp.jA));
            PaymentFrameworkApp.jz.add((Object)McTAController.createOnlyInstance((Context)PaymentFrameworkApp.jA));
            if (com.samsung.android.spayfw.utils.h.fS()) {
                PaymentFrameworkApp.jz.add((Object)AmexTAController.C((Context)PaymentFrameworkApp.jA));
            } else {
                PaymentFrameworkApp.jz.add((Object)com.samsung.android.spayfw.payprovider.amexv2.tzsvc.c.D((Context)PaymentFrameworkApp.jA));
            }
            PaymentFrameworkApp.jz.add((Object)PlccTAController.createOnlyInstance((Context)PaymentFrameworkApp.jA));
            PaymentFrameworkApp.jz.add((Object)SpayTuiTAController.createOnlyInstance((Context)PaymentFrameworkApp.jA));
            PaymentFrameworkApp.jz.add((Object)com.samsung.android.spayfw.payprovider.discover.tzsvc.b.E((Context)PaymentFrameworkApp.jA));
            PaymentFrameworkApp.jz.add((Object)com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.c.F((Context)PaymentFrameworkApp.jA));
            SpayTuiTAController.getInstance().setTAs(PaymentFrameworkApp.jz);
            if (!com.samsung.android.spayfw.utils.h.fR()) {
                this.aF();
            }
            Log.d("PaymentFrameworkApp", "initPF: Create efs directory based on UID = " + Process.myUid());
            if (Process.myUid() == 1000) {
                var27_5 = TAController.getEfsDirectory();
                Log.d("PaymentFrameworkApp", "initPF: path = " + var27_5);
                var28_6 = new File(var27_5);
                if (!var28_6.exists() && !var28_6.mkdir()) {
                    Log.e("PaymentFrameworkApp", "initPF: Error creating efs directory for PF!");
                    return false;
                }
                if (!var28_6.canWrite() || !var28_6.canRead()) {
                    Log.e("PaymentFrameworkApp", "initPF: Cannot read or write to efs directory! No permissions!");
                    return false;
                }
            }
            if (!var6_4.init(PaymentFrameworkApp.jz)) {
                Log.e("PaymentFrameworkApp", "initPF:Payment Service Init failed");
                return false;
            }
            Log.d("PaymentFrameworkApp", "initPF:Payment Service Version : " + var6_4.getVersion());
            Log.d("PaymentFrameworkApp", "initPF: load/unload tui TA to create/write pin random if necessary");
            try {
                var25_10 = SpayTuiTAController.createOnlyInstance(this.getApplicationContext());
                if (var25_10 != null) {
                    var25_10.loadTA();
                    var25_10.unloadTA();
                }
            }
            catch (TAException var14_13) {
                Log.c("PaymentFrameworkApp", var14_13.getMessage(), (Throwable)var14_13);
            }
            Log.d("PaymentFrameworkApp", "initPF: Initializing tima keystore! needed to get db key ");
            var15_11 = com.samsung.android.spayfw.utils.c.fF();
            if (var15_11 == null || !"3.0".equals((Object)var15_11.getTimaVersion())) ** GOTO lbl77
            Log.d("PaymentFrameworkApp", "initPF: - Tima Version : 3.0");
            var16_12 = var15_11.KeyStore3_init();
            Log.i("PaymentFrameworkApp", "initPF: timaStatus = " + var16_12);
            if (var16_12 == 0) {
                Log.i("PaymentFrameworkApp", "Device Integrity Verification success");
                if (!com.samsung.android.spayfw.utils.h.fM()) {
                    PaymentFrameworkApp.jG = -42;
                    Log.e("PaymentFrameworkApp", "initPF:security patch update date verification failed");
                    return false;
                }
            } else {
                Log.e("PaymentFrameworkApp", "Device Integrity Verification failed");
                if (var16_12 == 65548 || var16_12 == 65549 || var16_12 == 65550) {
                    Log.e("PaymentFrameworkApp", "Device Integrity Compromised");
                    return false;
                } else {
                    Log.e("PaymentFrameworkApp", "Unable to establish communication with TZ. Kill PF");
                    Process.killProcess((int)Process.myPid());
                }
                return false;
lbl77: // 1 sources:
                Log.e("PaymentFrameworkApp", "initPF:unable to get timaService instance");
                return false;
            }
            if (!CNCCTAController.bringup((Context)PaymentFrameworkApp.jA)) {
                Log.e("PaymentFrameworkApp", "initPF: Error - CNCC Bringup failed");
                return false;
            }
            this.aD();
            this.jJ = TokenRecordStorage.ae((Context)PaymentFrameworkApp.jA);
            if (this.jJ != null) {
                Log.i("PaymentFrameworkApp", "initPF:token count:" + this.jJ.fu());
            }
            Log.d("PaymentFrameworkApp", "initPF: Building ConfigurationManager config cache! ");
            e.h((Context)PaymentFrameworkApp.jA).ae();
            var17_14 = new HandlerThread("PaymentFramework");
            Log.d("PaymentFrameworkApp", "initPF:PF service worker thread is started");
            var17_14.start();
            PaymentFrameworkApp.jB = new i((Context)PaymentFrameworkApp.jA, var17_14.getLooper());
            var18_15 = new HandlerThread("restoreHandler");
            Log.d("PaymentFrameworkApp", "initPF:PF service worker thread for RESTORE_HANDLER is started");
            var18_15.start();
            PaymentFrameworkApp.jC = new i((Context)PaymentFrameworkApp.jA, var18_15.getLooper());
            this.jH = SpayTuiTAController.getInstance().getScreenDensity();
            this.jI = SpayTuiTAController.getInstance().getLocale();
            if (this.jJ != null && this.jJ.fu() > 0) {
                this.prepare();
            }
            if (!com.samsung.android.spayfw.utils.h.fR()) break block36;
            FactoryResetDetector.disable();
            ** GOTO lbl122
        }
        com.samsung.android.spayfw.e.c.ar((Context)PaymentFrameworkApp.jA);
        com.samsung.android.spayfw.e.c.fi();
        ** try [egrp 7[TRYBLOCK] [62 : 1092->1162)] { 
lbl108: // 1 sources:
        ** GOTO lbl111
        {
            catch (Exception var19_18) {
                Log.e("PaymentFrameworkApp", var19_18.getMessage());
            }
lbl111: // 2 sources:
            var20_17 = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            PaymentFrameworkApp.jA.registerReceiver((BroadcastReceiver)new b(), var20_17);
            d.e(true);
            Log.d("PaymentFrameworkApp", "initPF: Prepare MST pay config data");
            h.k((Context)PaymentFrameworkApp.jA);
            BinAttribute.init((Context)PaymentFrameworkApp.jA);
            ** GOTO lbl122
        }
lbl118: // 2 sources:
        catch (Exception var3_7) {
            block37 : {
                var1_1 = false;
                var4_8 = var3_7;
                break block37;
lbl122: // 2 sources:
                if (com.samsung.android.spayfw.utils.h.fQ()) {
                    com.samsung.android.spayfw.utils.d.ag((Context)PaymentFrameworkApp.jA).fJ();
                }
                PaymentFrameworkApp.jF = PaymentFrameworkApp.jD;
                try {
                    if ("US".equalsIgnoreCase(com.samsung.android.spayfw.utils.h.fP())) {
                        var22_16 = new Intent();
                        var22_16.setClassName((Context)PaymentFrameworkApp.jA, "com.samsung.contextservice.system.ContextService");
                        this.startService(var22_16);
                        Log.d("PaymentFrameworkApp", "start CTX service");
                    } else {
                        Log.d("PaymentFrameworkApp", "CTX service will not start");
                    }
                    com.samsung.android.spayfw.utils.e.a(this.getApplicationContext(), 10);
                    this.aE();
                    Log.i("PaymentFrameworkApp", "initPF: success without any error");
                    return var1_1;
                }
                catch (Exception var4_9) {}
            }
            Log.e("PaymentFrameworkApp", "initPF: failed with exception");
            var4_8.printStackTrace();
            return var1_1;
        }
    }

    public boolean isReady() {
        Log.d("PaymentFrameworkApp", "PFState: " + jF);
        return jF == jD;
    }

    public void onConfigurationChanged(Configuration configuration) {
        Log.d("PaymentFrameworkApp", "onConfigurationChanged");
        super.onConfigurationChanged(configuration);
        String string = SpayTuiTAController.getInstance().getScreenDensity();
        String string2 = SpayTuiTAController.getInstance().getLocale();
        Log.d("PaymentFrameworkApp", "original screenDpi " + this.jH);
        Log.d("PaymentFrameworkApp", "new screenDpi " + string);
        if (!string.equals((Object)this.jH)) {
            Log.d("PaymentFrameworkApp", "onConfigurationChanged. Screen DPI changed Killing PF! " + Process.myPid());
            Process.killProcess((int)Process.myPid());
        }
        Log.d("PaymentFrameworkApp", "original Locale " + this.jI);
        Log.d("PaymentFrameworkApp", "new Locale " + string2);
        if (!string2.equals((Object)this.jI)) {
            Log.d("PaymentFrameworkApp", "onConfigurationChanged. Locale changed Killing PF! " + Process.myPid());
            Process.killProcess((int)Process.myPid());
        }
    }

    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new n());
        Log.a(new com.samsung.android.spayfw.b.b("Console-Logger"));
        Log.a(new com.samsung.android.spayfw.b.a(this.getApplicationContext(), "File-Logger"));
        Log.d("PaymentFrameworkApp", "PaymentFrameworkApp: onCreate Start");
        jA = this;
        com.samsung.android.spayfw.utils.c.setContext((Context)jA);
        this.init();
        Log.d("PaymentFrameworkApp", "PaymentFrameworkApp : onCreate End");
    }

    public static class a
    implements k {
        @Override
        public void a(f f2) {
            if (jB != null) {
                Message message = j.a(11, f2.getTrTokenId(), null);
                jB.a(message);
                return;
            }
            Log.e("PaymentFrameworkApp", "HANDLER IS NOT INITIAILIZED");
        }

        @Override
        public void b(f f2) {
            Intent intent = new Intent("com.samsung.android.spayfw.action.notification");
            intent.putExtra("notiType", "payReadyStateUpdate");
            intent.putExtra("tokenId", f2.getTrTokenId());
            PaymentFrameworkApp.a(intent);
        }
    }

}

