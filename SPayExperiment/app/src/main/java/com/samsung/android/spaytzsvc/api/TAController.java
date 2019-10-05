/*
 * Decompiled with CFR 0.0.
 *
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.content.res.AssetFileDescriptor
 *  android.content.res.AssetManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.ParcelFileDescriptor
 *  android.os.Process
 *  android.os.RemoteException
 *  android.spay.CertInfo
 *  android.spay.ITAController
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  java.io.File
 *  java.io.FileInputStream
 *  java.io.FileNotFoundException
 *  java.io.IOException
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.Throwable
 *  java.lang.reflect.Method
 *  java.util.Arrays
 *  java.util.List
 */
package com.samsung.android.spaytzsvc.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.spay.CertInfo;
import android.spay.ITAController;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;

import com.samsung.android.spayfw.b.c;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.IPaymentSvcDeathReceiver;
import com.samsung.android.spaytzsvc.api.PaymentTZServiceIF;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAException;
import com.samsung.android.spaytzsvc.api.TAInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javolution.io.Struct;

public class TAController
        implements IPaymentSvcDeathReceiver {
    public static final int CMD_ABORT_MST = 3;
    public static final int CMD_MOVE_SEC_OS_CORE0 = 6;
    public static final int CMD_MOVE_SEC_OS_CORE4 = 5;
    public static final int CMD_MST_OFF = 2;
    public static final int CMD_MST_ON = 1;
    public static final int CMD_RESET_MST = 4;
    public static final boolean DEBUG = "eng".equals((Object) Build.TYPE);
    private static final String PF_EFS_ROOT_DIR_SPAY_UID = "/efs/pfw_data";
    private static final String PF_EFS_ROOT_DIR_SYSTEM_UID = "/efs/prov_data/pfw_data";
    private static final String TAG = "TAController";
    private static final int TA_LOAD_RETRY_COUNT = 5;
    private static final boolean bQC;
    private boolean bLoaded = false;
    private boolean bMeasurementVerified = false;
    private Context mContext;
    protected ITAController mPaymentHandle;
    protected TAInfo mTAInfo;

    static {
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
    }

    protected TAController(Context context, TAInfo tAInfo) {
        this.mContext = context;
        this.mTAInfo = tAInfo;
    }

    private ParcelFileDescriptor createDummyFD() {
        try {
            ParcelFileDescriptor parcelFileDescriptor = this.mContext.getAssets().openFd(this.mTAInfo.getDummyTAPath()).getParcelFileDescriptor();
            return parcelFileDescriptor;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String getEfsDirectory() {
        if (DEBUG) {
            c.d(TAG, "getEfsDirectory: my UID = " + Process.myUid());
        }
        if (Process.myUid() == 1000) {
            return PF_EFS_ROOT_DIR_SYSTEM_UID;
        }
        return PF_EFS_ROOT_DIR_SPAY_UID;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private AssetFileDescriptor getTaFd() {
        AssetManager var1_1 = this.mContext.getAssets();
        AssetFileDescriptor taFileDescriptor = null;
        boolean var3_3 = this.shouldLoadTAFromSystem();
        taFileDescriptor = null;
        try {
            if (TAController.DEBUG) {
                c.d("TAController", "getTA: " + this.mTAInfo.getTAFileName());
            }
            boolean var8_4 = TAController.DEBUG;
            taFileDescriptor = null;
            if (var8_4) {
                String[] var9_5 = var1_1.list(TAInfo.getTARootDir());
                taFileDescriptor = null;
                if (var9_5 != null) {
                    int var10_6 = var9_5.length;
                    int var11_7 = 0;
                    do {
                        taFileDescriptor = null;
                        if (var11_7 >= var10_6) break;
                        String var12_8 = var9_5[var11_7];
                        c.d("TAController", "File : " + var12_8);
                        ++var11_7;
                    } while (true);
                }
            }
            try {
                String var5_10 = this.findTAByDeviceModel(var1_1);
                try {
                    taFileDescriptor = var1_1.openFd(var5_10);
                    c.d("TAController", "Found TA file: " + var5_10);
                    return taFileDescriptor;
                } catch (FileNotFoundException var14_11) {
                    c.e("TAController", "TA file not found: " + var5_10);
                    return taFileDescriptor;
                }
            } catch (Exception var7_12) {
                c.e("TAController", "general exception");
                var7_12.printStackTrace();
                return taFileDescriptor;
            }
        } catch (Throwable var6_14) {
        }

        return taFileDescriptor;
    }

    private void initTA(int n2, byte[] arrby) throws TAException {
        TACommandResponse tACommandResponse;
        long l2;
        if (this.bMeasurementVerified) {
            if (DEBUG) {
                c.d(TAG, "initTA need not be called as measurement is already verified");
            }
            return;
        }
        if (DEBUG) {
            c.d(TAG, "Calling initTA");
        }
        if ((tACommandResponse = this.executeNoLoad(new TACommands.Init.Request(null))) == null) {
            c.e(TAG, "Error: execute failed");
            throw new TAException("Error: executeNoLoad failed", 1);
        }
        TACommands.Init.Response response = new TACommands.Init.Response(tACommandResponse);
        if (response.mRetVal.result.get() == 65547L) {
            TACommandResponse tACommandResponse2;
            if (DEBUG) {
                c.d(TAG, "ReInitializing TA - Reason : Received Error Code TZ_COMMON_INIT_UNINITIALIZED_SECURE_MEM");
            }
            if ((tACommandResponse2 = this.executeNoLoad(new TACommands.Init.Request(PaymentTZServiceIF.getInstance().getMeasurementFile()))) == null) {
                c.e(TAG, "Error: execute failed");
                throw new TAException("Error: executeNoLoad failed", 1);
            }
            response = new TACommands.Init.Response(tACommandResponse2);
        }
        if ((l2 = response.mRetVal.result.get()) != 0L) {
            c.e(TAG, "Error: initTA failed");
            if (l2 == 65548L || l2 == 65549L || l2 == 65550L) {
                throw new TAException("Error: initTA failed", 2);
            }
            throw new TAException("Error: initTA failed", 1);
        }
        if (DEBUG) {
            c.d(TAG, "initTA called Successfully");
        }
        this.bMeasurementVerified = true;
    }

    private static final boolean isAndroidMAndAbove() {
        if (Build.VERSION.SDK_INT > 22) {
            c.d(TAG, "You are using Android Version " + Build.VERSION.SDK_INT);
            return true;
        }
        return false;
    }

    public static boolean isChipSetQC() {
        return bQC;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    // WARNING: Hand-reconstruction unverified.
    private int loadPinRandom() throws FileNotFoundException {
        if (!this.usesPinRandom()) {
            c.i("TAController", "No need to load PIN random for TA " + this.mTAInfo);
            return 0;
        }
        String randPinFileName = this.mTAInfo.getPinRandomFileName();
        File pinFile = new File(TAController.getEfsDirectory(), randPinFileName);
        if (!pinFile.isFile()) {
            c.e("TAController", "Cannot load PIN random for TA " + this.mTAInfo + ", file " + pinFile.getAbsolutePath() + " not found");
            return 0;
        }
        byte[] pinFileReadBuffer = new byte[(int) pinFile.length()];

        FileInputStream pinFis = null;
        TACommandResponse taCommandResponse = new TACommandResponse();

        try {
            pinFis = new FileInputStream(pinFile);

            int curReadOffset = 0;
            int bytesRead;
            for (int readLength = (int) pinFile.length();
                 readLength > 0;
                 curReadOffset += bytesRead, readLength -= bytesRead) {
                bytesRead = pinFis.read(pinFileReadBuffer, curReadOffset, readLength);
            }

            taCommandResponse =
                    this.executeNoLoad(new TACommands.LoadPinRandom.Request(pinFileReadBuffer));
            if (taCommandResponse != null) {
                c.e("TAController", "Error: execute failed");
                throw new TAException("Error: executeNoLoad failed", 1);
            }

        } catch (FileNotFoundException e) {
            c.e("TAController", "Cannot load PIN random for TA " + this.mTAInfo);
            e.printStackTrace();

            if (pinFis == null) return 0;

            try {
                pinFis.close();
                return 0;
            } catch (IOException var8_16) {
                var8_16.printStackTrace();
                return 0;
            }
        } catch (IOException e) {
            if (new TACommands.LoadPinRandom.Response(taCommandResponse).mRetVal.result.get() == 0L) {
                c.e("TAController", "Load PIN random failed for TA " + this.mTAInfo);
                return 0;
            }

            if (TAController.DEBUG) {
                c.d("TAController", "Load PIN random succeeded for TA " + this.mTAInfo);
            }


            try {
                pinFis.close();
                return 0;
            } catch (IOException var13_12) {
                var13_12.printStackTrace();
                return 0;
            }

        } catch (TAException e) {

        }

        return 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean shouldLoadTAFromSystem() {
        block6:
        {
            block5:
            {
                block4:
                {
                    if (!DEBUG) break block4;
                    SharedPreferences sharedPreferences =
                            this.mContext.getSharedPreferences(
                                    "shared_preferences_test", Context.MODE_MULTI_PROCESS);
                    int n2 = sharedPreferences.getInt(this.mTAInfo.getTAFileName(), 1000);
                    c.d(TAG, "fromSystem=" + n2);
                    if (n2 == 1) break block5;
                    if (n2 == 1000) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(this.mTAInfo.getTAFileName(), 0);
                        editor.apply();
                    }
                }
                if (!this.mTAInfo.isLoadFromSystem()) break block6;
            }
            return true;
        }
        return false;
    }

    public void abortMstTransmission() {
        TAController tAController = this;
        synchronized (tAController) {
            if (DEBUG) {
                c.d(TAG, "abortMstTransmission is called");
            }
            if (!this.makeSystemCall(3)) {
                c.e(TAG, "abortMstTransmission: Failed to abort MST");
            }
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public CertInfo checkCertInfo(List<String> list) {
        try {
            if (this.mPaymentHandle != null) {
                Method method = this.mPaymentHandle.getClass().getMethod("checkCertInfo", new Class[]{List.class});
                c.d(TAG, "Using checkCertInfo API");
                return (CertInfo) method.invoke((Object) this.mPaymentHandle, new Object[]{list});
            }
            c.e(TAG, "mPaymentHandle is null");
            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean clearDeviceCertificates(String string) {
        try {
            if (this.mPaymentHandle == null) {
                c.e(TAG, "mPaymentHandle is null");
                return false;
            }
            if (string != null) {
                Method method = this.mPaymentHandle.getClass().getMethod("clearDeviceCertificates", new Class[]{String.class});
                c.i(TAG, "Using NEW clearDeviceCertificates API");
                return (Boolean) method.invoke((Object) this.mPaymentHandle, new Object[]{string});
            }
            Method method = this.mPaymentHandle.getClass().getMethod("clearDeviceCertificates", new Class[0]);
            c.i(TAG, "Using OLD clearDeviceCertificates API");
            return (Boolean) method.invoke((Object) this.mPaymentHandle, new Object[0]);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean copyMctoRst() {
        try {
            if (this.mPaymentHandle != null) {
                Method method = this.mPaymentHandle.getClass().getMethod("copyMctoRst", new Class[0]);
                c.d(TAG, "Using copyMctoRst API");
                return (Boolean) method.invoke((Object) this.mPaymentHandle, new Object[0]);
            }
            c.e(TAG, "mPaymentHandle is null");
            return false;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public byte[] decapsulateAndWrap(byte[] arrby) throws TAException {
        TACommandResponse tACommandResponse = this.executeNoLoad(new TACommands.MoveServiceKey.Request(arrby));
        if (tACommandResponse == null) {
            c.e(TAG, "Error: execute failed");
            throw new TAException("Error: executeNoLoad failed", 1);
        }
        TACommands.MoveServiceKey.Response response = new TACommands.MoveServiceKey.Response(tACommandResponse);
        if (response.mRetVal.return_code.get() != 0L) {
            c.d(TAG, "Error: decapsulateAndWrap failed - response.mRetVal = " + response.mErrorMsg);
            throw new TAException("Error: decapsulateAndWrap failed" + response.mErrorMsg, 4);
        }
        if (DEBUG) {
            c.d(TAG, "decapsulateAndWrap called Successfully");
        }
        return response.mRetVal.wrapped_msg.getData();
    }

    protected void dumpHex(String string, String string2, byte[] arrby) {
        if (arrby == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (byte by : arrby) {
            Object[] arrobject = new Object[]{by};
            stringBuilder.append(String.format((String) "%02X ", (Object[]) arrobject));
        }
        c.d(string, string2 + " " + stringBuilder.toString());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public TACommandResponse executeNoLoad(TACommandRequest tACommandRequest) {
        TACommandResponse tACommandResponse = null;
        TAController tAController = this;
        synchronized (tAController) {
            if (!this.isTALoaded()) {
                c.e(TAG, "TA has to be loaded before calling executeNoLoad");
            } else {
                try {
                    ITAController iTAController = this.mPaymentHandle;
                    tACommandResponse = null;
                    if (iTAController != null) {
                        tACommandResponse = this.mPaymentHandle.processTACommand(tACommandRequest);
                        if (tACommandResponse == null) return tACommandResponse;
                        if (!DEBUG) return tACommandResponse;
                        c.d(TAG, "executeNoLoad: Response Code = " + tACommandResponse.mResponseCode);
                        c.d(TAG, "executeNoLoad: Error Message = " + tACommandResponse.mErrorMsg);
                        c.d(TAG, "executeNoLoad: Response Len = " + tACommandResponse.mResponse.length + " Buf = " + Arrays.toString((byte[]) tACommandResponse.mResponse));
                    }
                    c.e(TAG, "executeNoLoad: mPaymentHandle is null");
                    return null;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            return tACommandResponse;
        }
    }

    protected String findTAByDeviceModel(AssetManager assetManager) {
        return this.mTAInfo.getTAPath();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public CertInfo getCertInfo() {
        Class<TAController> class_ = TAController.class;
        synchronized (TAController.class) {
            try {
                if (this.mPaymentHandle != null) {
                    Method method = this.mPaymentHandle.getClass().getMethod("getCertInfo", null);
                    c.d(TAG, "Using getCertInfo API");
                    return (CertInfo) method.invoke((Object) this.mPaymentHandle, null);
                }
                c.e(TAG, "mPaymentHandle is null");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return null;
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public TAInfo getTAInfo() {
        return this.mTAInfo;
    }

    protected boolean init() {
        PaymentTZServiceIF paymentTZServiceIF = PaymentTZServiceIF.getInstance();
        this.mPaymentHandle = paymentTZServiceIF.getTAController(this.mTAInfo.getTAType());
        paymentTZServiceIF.registerForDisconnection(this);
        this.prepareTA();
        return true;
    }

    public boolean initTA() {
        try {
            this.initTA(0, TACommands.TL_MAGIC_NUM);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean isDeviceCertificateMigratable() {
        try {
            if (this.mPaymentHandle.getClass().getMethod("checkCertInfo", new Class[]{List.class}) == null) {
                c.d(TAG, "API checkCertInfo Not found");
                return false;
            }
            return true;
        } catch (NoSuchMethodException noSuchMethodException) {
            c.d(TAG, "API checkCertInfo Not found");
            return false;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean isTALoaded() {
        TAController tAController = this;
        synchronized (tAController) {
            boolean bl = this.bLoaded;
            return bl;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public TACommandResponse loadExecuteUnload(TACommandRequest tACommandRequest) throws RemoteException {
        TACommandResponse tACommandResponse;

        tACommandResponse = null;
        TAController tAController = this;
        // MONITORENTER : tAController

        if (!this.loadTA()) {
            c.e(TAG, "TA Loading failed");
            // MONITOREXIT : tAController
            return tACommandResponse;
        }

        if (this.mPaymentHandle == null) {
            c.e(TAG, "execute: mPaymentHandle is null");
        }

        tACommandResponse = this.mPaymentHandle.processTACommand(tACommandRequest);
        if (tACommandResponse != null && DEBUG) {
            c.d(TAG, "execute: Response Code = " + tACommandResponse.mResponseCode);
            c.d(TAG, "execute: Error Message = " + tACommandResponse.mErrorMsg);
            c.d(TAG, "execute: Response Len = " + tACommandResponse.mResponse.length + " Buf = " + Arrays.toString((byte[]) tACommandResponse.mResponse));
        }


        this.unloadTA();
        return tACommandResponse;
    }

    /*
     * Exception decompiling
     */
    public boolean loadTA() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [53[FORLOOP]], but top level block is 13[TRYBLOCK]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean makeSystemCall(int n2) {
        try {
            if (this.mPaymentHandle != null) {
                return this.mPaymentHandle.makeSystemCall(n2);
            }
            c.e(TAG, "mPaymentHandle is null");
            return false;
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void moveSecOsToCore4() {
        TAController tAController = this;
        synchronized (tAController) {
            if (bQC) {
                if (DEBUG) {
                    c.d(TAG, "moveSecOsToCore4: QSEE do not need core migration");
                }
            } else {
                if (DEBUG) {
                    c.d(TAG, "moveSecOsToCore4 is called");
                }
                if (!this.makeSystemCall(5)) {
                    c.e(TAG, "moveSecOsToCore4: Failed to move sec OS to core2");
                }
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void moveSecOsToDefaultCore() {
        TAController tAController = this;
        synchronized (tAController) {
            if (bQC) {
                if (DEBUG) {
                    c.d(TAG, "moveSecOsToDefaultCore: QSEE do not need core migration");
                }
            } else {
                if (DEBUG) {
                    c.d(TAG, "moveSecOsToDefaultCore is called");
                }
                if (!this.makeSystemCall(6)) {
                    c.e(TAG, "moveSecOsToDefaultCore: Failed to move sec OS to core0");
                }
            }
            return;
        }
    }

    @Override
    public void onDisconnected() {
        this.mPaymentHandle = null;
    }

    /*
     * Enabled aggressive block sorting
     */
    boolean prepareTA() {
        if (TAController.isAndroidMAndAbove() || this.bMeasurementVerified) {
            return true;
        }
        if (!this.loadTA()) {
            c.e(TAG, "Error: loadTA failed");
            return false;
        }
        if (!this.initTA()) {
            c.e(TAG, "Error: initTA failed");
            return false;
        }
        this.unloadTA();
        return true;
    }

    protected void resetMstFlag() {
        TAController tAController = this;
        synchronized (tAController) {
            if (DEBUG) {
                c.d(TAG, "resetMstFlag is called");
            }
            if (!this.makeSystemCall(4)) {
                c.e(TAG, "resetMstFlag: Failed to reset MST flag");
            }
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean setCertInfo(CertInfo certInfo) {
        try {
            if (this.mPaymentHandle != null) {
                Method method = this.mPaymentHandle.getClass().getMethod("setCertInfo", new Class[]{CertInfo.class});
                c.d(TAG, "Using setCertInfo API");
                return (Boolean) method.invoke((Object) this.mPaymentHandle, new Object[]{certInfo});
            }
            c.e(TAG, "mPaymentHandle is null");
            return false;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    /*
     * Exception decompiling
     */
    public int setupPinRandom(byte[] var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
        // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void unloadTA() {
        TAController tAController = this;
        synchronized (tAController) {
            if (this.mPaymentHandle == null) {
                c.e(TAG, "unloadTA: mPaymentHandle is null");
            } else if (!this.bLoaded) {
                c.e(TAG, "TA is never loaded. Unload is noop");
            } else {
                try {
                    this.mPaymentHandle.unloadTA();
                    this.bLoaded = false;
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }
            return;
        }
    }

    public boolean usesPinRandom() {
        return this.mTAInfo.usesPinRandom();
    }
}

