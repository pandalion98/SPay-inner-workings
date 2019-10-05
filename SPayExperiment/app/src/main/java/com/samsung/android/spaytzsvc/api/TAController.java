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

import com.samsung.android.spayfw.b.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

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
            Log.d(TAG, "getEfsDirectory: my UID = " + Process.myUid());
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
                Log.d("TAController", "getTA: " + this.mTAInfo.getTAFileName());
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
                        Log.d("TAController", "File : " + var12_8);
                        ++var11_7;
                    } while (true);
                }
            }
            try {
                String var5_10 = this.findTAByDeviceModel(var1_1);
                try {
                    taFileDescriptor = var1_1.openFd(var5_10);
                    Log.d("TAController", "Found TA file: " + var5_10);
                    return taFileDescriptor;
                } catch (FileNotFoundException var14_11) {
                    Log.e("TAController", "TA file not found: " + var5_10);
                    return taFileDescriptor;
                }
            } catch (Exception var7_12) {
                Log.e("TAController", "general exception");
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
                Log.d(TAG, "initTA need not be called as measurement is already verified");
            }
            return;
        }
        if (DEBUG) {
            Log.d(TAG, "Calling initTA");
        }
        if ((tACommandResponse = this.executeNoLoad(new TACommands.Init.Request(null))) == null) {
            Log.e(TAG, "Error: execute failed");
            throw new TAException("Error: executeNoLoad failed", 1);
        }
        TACommands.Init.Response response = new TACommands.Init.Response(tACommandResponse);
        if (response.mRetVal.result.get() == 65547L) {
            TACommandResponse tACommandResponse2;
            if (DEBUG) {
                Log.d(TAG, "ReInitializing TA - Reason : Received Error Code TZ_COMMON_INIT_UNINITIALIZED_SECURE_MEM");
            }
            if ((tACommandResponse2 = this.executeNoLoad(new TACommands.Init.Request(PaymentTZServiceIF.getInstance().getMeasurementFile()))) == null) {
                Log.e(TAG, "Error: execute failed");
                throw new TAException("Error: executeNoLoad failed", 1);
            }
            response = new TACommands.Init.Response(tACommandResponse2);
        }
        if ((l2 = response.mRetVal.result.get()) != 0L) {
            Log.e(TAG, "Error: initTA failed");
            if (l2 == 65548L || l2 == 65549L || l2 == 65550L) {
                throw new TAException("Error: initTA failed", 2);
            }
            throw new TAException("Error: initTA failed", 1);
        }
        if (DEBUG) {
            Log.d(TAG, "initTA called Successfully");
        }
        this.bMeasurementVerified = true;
    }

    private static final boolean isAndroidMAndAbove() {
        if (Build.VERSION.SDK_INT > 22) {
            Log.d(TAG, "You are using Android Version " + Build.VERSION.SDK_INT);
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
            Log.i("TAController", "No need to load PIN random for TA " + this.mTAInfo);
            return 0;
        }
        String randPinFileName = this.mTAInfo.getPinRandomFileName();
        File pinFile = new File(TAController.getEfsDirectory(), randPinFileName);
        if (!pinFile.isFile()) {
            Log.e("TAController", "Cannot load PIN random for TA " + this.mTAInfo + ", file " + pinFile.getAbsolutePath() + " not found");
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
                Log.e("TAController", "Error: execute failed");
                throw new TAException("Error: executeNoLoad failed", 1);
            }

        } catch (FileNotFoundException e) {
            Log.e("TAController", "Cannot load PIN random for TA " + this.mTAInfo);
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
                Log.e("TAController", "Load PIN random failed for TA " + this.mTAInfo);
                return 0;
            }

            if (TAController.DEBUG) {
                Log.d("TAController", "Load PIN random succeeded for TA " + this.mTAInfo);
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
                    Log.d(TAG, "fromSystem=" + n2);
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
                Log.d(TAG, "abortMstTransmission is called");
            }
            if (!this.makeSystemCall(3)) {
                Log.e(TAG, "abortMstTransmission: Failed to abort MST");
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
                Log.d(TAG, "Using checkCertInfo API");
                return (CertInfo) method.invoke((Object) this.mPaymentHandle, new Object[]{list});
            }
            Log.e(TAG, "mPaymentHandle is null");
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
                Log.e(TAG, "mPaymentHandle is null");
                return false;
            }
            if (string != null) {
                Method method = this.mPaymentHandle.getClass().getMethod("clearDeviceCertificates", new Class[]{String.class});
                Log.i(TAG, "Using NEW clearDeviceCertificates API");
                return (Boolean) method.invoke((Object) this.mPaymentHandle, new Object[]{string});
            }
            Method method = this.mPaymentHandle.getClass().getMethod("clearDeviceCertificates", new Class[0]);
            Log.i(TAG, "Using OLD clearDeviceCertificates API");
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
                Log.d(TAG, "Using copyMctoRst API");
                return (Boolean) method.invoke((Object) this.mPaymentHandle, new Object[0]);
            }
            Log.e(TAG, "mPaymentHandle is null");
            return false;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public byte[] decapsulateAndWrap(byte[] arrby) throws TAException {
        TACommandResponse tACommandResponse = this.executeNoLoad(new TACommands.MoveServiceKey.Request(arrby));
        if (tACommandResponse == null) {
            Log.e(TAG, "Error: execute failed");
            throw new TAException("Error: executeNoLoad failed", 1);
        }
        TACommands.MoveServiceKey.Response response = new TACommands.MoveServiceKey.Response(tACommandResponse);
        if (response.mRetVal.return_code.get() != 0L) {
            Log.d(TAG, "Error: decapsulateAndWrap failed - response.mRetVal = " + response.mErrorMsg);
            throw new TAException("Error: decapsulateAndWrap failed" + response.mErrorMsg, 4);
        }
        if (DEBUG) {
            Log.d(TAG, "decapsulateAndWrap called Successfully");
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
        Log.d(string, string2 + " " + stringBuilder.toString());
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
                Log.e(TAG, "TA has to be loaded before calling executeNoLoad");
            } else {
                try {
                    ITAController iTAController = this.mPaymentHandle;
                    tACommandResponse = null;
                    if (iTAController != null) {
                        tACommandResponse = this.mPaymentHandle.processTACommand(tACommandRequest);
                        if (tACommandResponse == null) return tACommandResponse;
                        if (!DEBUG) return tACommandResponse;
                        Log.d(TAG, "executeNoLoad: Response Code = " + tACommandResponse.mResponseCode);
                        Log.d(TAG, "executeNoLoad: Error Message = " + tACommandResponse.mErrorMsg);
                        Log.d(TAG, "executeNoLoad: Response Len = " + tACommandResponse.mResponse.length + " Buf = " + Arrays.toString((byte[]) tACommandResponse.mResponse));
                    }
                    Log.e(TAG, "executeNoLoad: mPaymentHandle is null");
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
                    Log.d(TAG, "Using getCertInfo API");
                    return (CertInfo) method.invoke((Object) this.mPaymentHandle, null);
                }
                Log.e(TAG, "mPaymentHandle is null");
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
                Log.d(TAG, "API checkCertInfo Not found");
                return false;
            }
            return true;
        } catch (NoSuchMethodException noSuchMethodException) {
            Log.d(TAG, "API checkCertInfo Not found");
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
            Log.e(TAG, "TA Loading failed");
            // MONITOREXIT : tAController
            return tACommandResponse;
        }

        if (this.mPaymentHandle == null) {
            Log.e(TAG, "execute: mPaymentHandle is null");
        }

        tACommandResponse = this.mPaymentHandle.processTACommand(tACommandRequest);
        if (tACommandResponse != null && DEBUG) {
            Log.d(TAG, "execute: Response Code = " + tACommandResponse.mResponseCode);
            Log.d(TAG, "execute: Error Message = " + tACommandResponse.mErrorMsg);
            Log.d(TAG, "execute: Response Len = " + tACommandResponse.mResponse.length + " Buf = " + Arrays.toString((byte[]) tACommandResponse.mResponse));
        }


        this.unloadTA();
        return tACommandResponse;
    }

    /*
     * Exception decompiling
     */
    public boolean loadTA() {
        TAController r12 = this;
        ITAController r0 = r12.mPaymentHandle;
        int r4 = 0, r8 = 1, r7 = 0;

        // TODO: Synchronize the entire thing. monitor-enter(r12);
        // if (r0 != 0) goto L_0x0013
        if (r0 == null) {
//            r0 = "TAController";	 Catch:{ all -> 0x0077 }
//            r1 = "loadTA: mPaymentHandle is null";	 Catch:{ all -> 0x0077 }
//            com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x0077 }
//            r0 = r7;
            String stringR0 = "TAController";
            String stringR1 = "loadTA: mPaymentHandle is null";
            Log.e(stringR0, stringR1);
            return false;
        } else {
            boolean boolean0 = r12.bLoaded;
            if (boolean0) { // L_0x001b
                String stringR0 = "TAController";
                String stringR1 = "loadTA: mPaymentHandle is null";
                Log.d(stringR0, stringR1);
                return true;
            } else { // L_0x0024
                AssetFileDescriptor taFd = r12.getTaFd();
                if (taFd != null) {
                    /*
                        r6 = r0.getParcelFileDescriptor();	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
                        r2 = r0.getStartOffset();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
                        r0 = r0.getLength();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
                        if (r6 == 0) goto L_0x009c;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
                    */

                    ParcelFileDescriptor pfd = taFd.getParcelFileDescriptor();
                    long startOffset = taFd.getStartOffset();
                    long taFdLength = taFd.getLength();

                    int r9 = (startOffset > r4 ? 1 : (startOffset == r4 ? 0 : -1));
                    int r9_1 = taFdLength > r4 ? 1 : (taFdLength == r4 ? 0 : -1);

                    if (pfd == null || r9 < 0) {
                        Log.e("TAController", "pfd is null");
                        return false;
                    } else {
                        if (r9_1 >= 0) {
                            boolean taFromSystem = r12.shouldLoadTAFromSystem();
                            if (taFromSystem) {
                                ParcelFileDescriptor dummyPfd = r12.createDummyFD();
                                r0 = r12.mPaymentHandle;
                                try {
                                    boolean loaded = r0.loadTA(dummyPfd, 0, 0);
                                    if (!loaded) {
                                        if (dummyPfd == null) {
                                            return false;
                                        } else {
                                            try {
                                                dummyPfd.close();
                                                return false;
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                return false;
                                            }
                                        }
                                    } else {
                                        r12.bLoaded = true;
                                        r12.loadPinRandom();
                                    }
                                } catch (RemoteException | FileNotFoundException e) {
                                    e.printStackTrace();
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            Log.e("TAController", "pfd is null");
                            return false;
                        }

                        try {
                            pfd.close();
                            return false;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                } else {
                    boolean taFromSystem = r12.shouldLoadTAFromSystem();
                    if (taFromSystem) {
                        ParcelFileDescriptor dummyPfd = r12.createDummyFD();
                        r0 = r12.mPaymentHandle;
                        try {
                            boolean loaded = r0.loadTA(dummyPfd, 0, 0);
                            if (!loaded) {
                                if (dummyPfd == null) {
                                    return false;
                                } else {
                                    try {
                                        dummyPfd.close();
                                        return false;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        return false;
                                    }
                                }
                            } else {
                                r12.bLoaded = true;
                                r12.loadPinRandom();
                                try {
                                    dummyPfd.close();
                                    return true;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return true;
                                }
                                //return true;
                            }
                        } catch (RemoteException | FileNotFoundException e) {
                            e.printStackTrace();
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }

        }




        /*
        r12 = this;
        r4 = 0;
        r8 = 1;
        r7 = 0;
        monitor-enter(r12);
        r0 = r12.mPaymentHandle;	 Catch:{ all -> 0x0077 }
        if (r0 != 0) goto L_0x0013;	 Catch:{ all -> 0x0077 }
    L_0x0009:
        r0 = "TAController";	 Catch:{ all -> 0x0077 }
        r1 = "loadTA: mPaymentHandle is null";	 Catch:{ all -> 0x0077 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x0077 }
        r0 = r7;
    L_0x0011:
        monitor-exit(r12);
        return r0;
    L_0x0013:
        r0 = r12.bLoaded;	 Catch:{ all -> 0x0077 }
        if (r0 == 0) goto L_0x0024;	 Catch:{ all -> 0x0077 }
    L_0x0017:
        r0 = DEBUG;	 Catch:{ all -> 0x0077 }
        if (r0 == 0) goto L_0x0022;	 Catch:{ all -> 0x0077 }
    L_0x001b:
        r0 = "TAController";	 Catch:{ all -> 0x0077 }
        r1 = "TA is already loaded";	 Catch:{ all -> 0x0077 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ all -> 0x0077 }
    L_0x0022:
        r0 = r8;
        goto L_0x0011;
    L_0x0024:
        r1 = 0;
        r0 = r12.getTaFd();	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        if (r0 != 0) goto L_0x0086;	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
    L_0x002b:
        r0 = r12.shouldLoadTAFromSystem();	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        if (r0 == 0) goto L_0x007a;	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
    L_0x0031:
        r0 = DEBUG;	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        if (r0 == 0) goto L_0x0055;	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
    L_0x0035:
        r0 = "TAController";	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        r2 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        r2.<init>();	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        r3 = "load TA ";	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        r3 = r12.mTAInfo;	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        r3 = " from system";	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        r2 = r2.append(r3);	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        r2 = r2.toString();	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
    L_0x0055:
        r1 = r12.createDummyFD();	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        r0 = r12.mPaymentHandle;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r2 = 0;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r4 = 0;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r0 = r0.loadTA(r1, r2, r4);	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        if (r0 != r8) goto L_0x007a;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
    L_0x0065:
    // TODO: Checkpoint.

        r0 = 1;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r12.bLoaded = r0;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r12.loadPinRandom();	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        if (r1 == 0) goto L_0x0070;
    L_0x006d:
        r1.close();	 Catch:{ IOException -> 0x0072 }
    L_0x0070:
        r0 = r8;
        goto L_0x0011;
    L_0x0072:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0077 }
        goto L_0x0070;
    L_0x0077:
        r0 = move-exception;
        monitor-exit(r12);
        throw r0;
    L_0x007a:
        if (r1 == 0) goto L_0x007f;
    L_0x007c:
        r1.close();	 Catch:{ IOException -> 0x0081 }
    L_0x007f:
        r0 = r7;
        goto L_0x0011;
    L_0x0081:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0077 }
        goto L_0x007f;
    L_0x0086:
        r6 = r0.getParcelFileDescriptor();	 Catch:{ RemoteException -> 0x0196, TAException -> 0x01a8, Exception -> 0x01b8, all -> 0x01cf }
        r2 = r0.getStartOffset();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r0 = r0.getLength();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        if (r6 == 0) goto L_0x009c;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
    L_0x0094:
        r9 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        if (r9 < 0) goto L_0x009c;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
    L_0x0098:
        r9 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        if (r9 >= 0) goto L_0x00b0;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
    L_0x009c:
        r0 = "TAController";	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r1 = "pfd is null";	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        if (r6 == 0) goto L_0x00a8;
    L_0x00a5:
        r6.close();	 Catch:{ IOException -> 0x00ab }
    L_0x00a8:
        r0 = r7;
        goto L_0x0011;
    L_0x00ab:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0077 }
        goto L_0x00a8;
    L_0x00b0:
        r9 = DEBUG;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        if (r9 == 0) goto L_0x00e4;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
    L_0x00b4:
        r9 = "TAController";	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r10 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r10.<init>();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r11 = "TA fd=";	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r10 = r10.append(r11);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r11 = r6.getFd();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r10 = r10.append(r11);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r11 = " offset=";	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r10 = r10.append(r11);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r10 = r10.append(r2);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r11 = " len=";	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r10 = r10.append(r11);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r10 = r10.append(r0);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r10 = r10.toString();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        com.samsung.android.spayfw.p002b.Log.m285d(r9, r10);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
    L_0x00e4:
        r9 = r12.shouldLoadTAFromSystem();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        if (r9 == 0) goto L_0x0131;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
    L_0x00ea:
        r0 = DEBUG;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        if (r0 == 0) goto L_0x010e;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
    L_0x00ee:
        r0 = "TAController";	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r1 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r1.<init>();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r2 = "load TA ";	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r1 = r1.append(r2);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r2 = r12.mTAInfo;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r1 = r1.append(r2);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r2 = " from system";	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r1 = r1.append(r2);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r1 = r1.toString();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
    L_0x010e:
        r1 = r12.createDummyFD();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r2 = r4;
    L_0x0113:
        r6 = r7;
        r0 = r7;
    L_0x0115:
        r9 = 5;
        if (r6 >= r9) goto L_0x0120;
    L_0x0118:
        r0 = r12.mPaymentHandle;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r0 = r0.loadTA(r1, r2, r4);	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        if (r0 != r8) goto L_0x0158;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
    L_0x0120:
        if (r0 != 0) goto L_0x0178;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
    L_0x0122:
        r0 = "TAController";	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r2 = "TA Load failed";	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        if (r1 == 0) goto L_0x012e;
    L_0x012b:
        r1.close();	 Catch:{ IOException -> 0x0173 }
    L_0x012e:
        r0 = r7;
        goto L_0x0011;
    L_0x0131:
        r4 = DEBUG;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        if (r4 == 0) goto L_0x0155;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
    L_0x0135:
        r4 = "TAController";	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r5 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r5.<init>();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r9 = "load TA ";	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r5 = r5.append(r9);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r9 = r12.mTAInfo;	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r5 = r5.append(r9);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r9 = " from app";	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r5 = r5.append(r9);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        r5 = r5.toString();	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
        com.samsung.android.spayfw.p002b.Log.m285d(r4, r5);	 Catch:{ RemoteException -> 0x01ee, TAException -> 0x01e9, Exception -> 0x01e4 }
    L_0x0155:
        r4 = r0;
        r1 = r6;
        goto L_0x0113;
    L_0x0158:
        r9 = "TAController";	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r10 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r10.<init>();	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r11 = "TA loading failure: ";	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r10 = r10.append(r11);	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r10 = r10.append(r6);	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r10 = r10.toString();	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        com.samsung.android.spayfw.p002b.Log.m286e(r9, r10);	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r6 = r6 + 1;
        goto L_0x0115;
    L_0x0173:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0077 }
        goto L_0x012e;
    L_0x0178:
        r0 = 1;
        r12.bLoaded = r0;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r0 = DEBUG;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        if (r0 == 0) goto L_0x0186;	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
    L_0x017f:
        r0 = "TAController";	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        r2 = "TA Loaded Successfully";	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
    L_0x0186:
        r12.loadPinRandom();	 Catch:{ RemoteException -> 0x01eb, TAException -> 0x01e6, Exception -> 0x01e1, all -> 0x01dc }
        if (r1 == 0) goto L_0x018e;
    L_0x018b:
        r1.close();	 Catch:{ IOException -> 0x0191 }
    L_0x018e:
        r0 = r8;
        goto L_0x0011;
    L_0x0191:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0077 }
        goto L_0x018e;
    L_0x0196:
        r0 = move-exception;
        r6 = r1;
    L_0x0198:
        r0.printStackTrace();	 Catch:{ all -> 0x01df }
        if (r6 == 0) goto L_0x01a0;
    L_0x019d:
        r6.close();	 Catch:{ IOException -> 0x01a3 }
    L_0x01a0:
        r0 = r7;
        goto L_0x0011;
    L_0x01a3:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0077 }
        goto L_0x01a0;
    L_0x01a8:
        r0 = move-exception;
        r6 = r1;
    L_0x01aa:
        r0.printStackTrace();	 Catch:{ all -> 0x01df }
        if (r6 == 0) goto L_0x01a0;
    L_0x01af:
        r6.close();	 Catch:{ IOException -> 0x01b3 }
        goto L_0x01a0;
    L_0x01b3:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0077 }
        goto L_0x01a0;
    L_0x01b8:
        r0 = move-exception;
        r6 = r1;
    L_0x01ba:
        r1 = "TAController";	 Catch:{ all -> 0x01df }
        r2 = "Generic exception";	 Catch:{ all -> 0x01df }
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r2);	 Catch:{ all -> 0x01df }
        r0.printStackTrace();	 Catch:{ all -> 0x01df }
        if (r6 == 0) goto L_0x01a0;
    L_0x01c6:
        r6.close();	 Catch:{ IOException -> 0x01ca }
        goto L_0x01a0;
    L_0x01ca:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0077 }
        goto L_0x01a0;
    L_0x01cf:
        r0 = move-exception;
        r6 = r1;
    L_0x01d1:
        if (r6 == 0) goto L_0x01d6;
    L_0x01d3:
        r6.close();	 Catch:{ IOException -> 0x01d7 }
    L_0x01d6:
        throw r0;	 Catch:{ all -> 0x0077 }
    L_0x01d7:
        r1 = move-exception;	 Catch:{ all -> 0x0077 }
        r1.printStackTrace();	 Catch:{ all -> 0x0077 }
        goto L_0x01d6;
    L_0x01dc:
        r0 = move-exception;
        r6 = r1;
        goto L_0x01d1;
    L_0x01df:
        r0 = move-exception;
        goto L_0x01d1;
    L_0x01e1:
        r0 = move-exception;
        r6 = r1;
        goto L_0x01ba;
    L_0x01e4:
        r0 = move-exception;
        goto L_0x01ba;
    L_0x01e6:
        r0 = move-exception;
        r6 = r1;
        goto L_0x01aa;
    L_0x01e9:
        r0 = move-exception;
        goto L_0x01aa;
    L_0x01eb:
        r0 = move-exception;
        r6 = r1;
        goto L_0x0198;
    L_0x01ee:
        r0 = move-exception;
        goto L_0x0198;
        */
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
            Log.e(TAG, "mPaymentHandle is null");
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
                    Log.d(TAG, "moveSecOsToCore4: QSEE do not need core migration");
                }
            } else {
                if (DEBUG) {
                    Log.d(TAG, "moveSecOsToCore4 is called");
                }
                if (!this.makeSystemCall(5)) {
                    Log.e(TAG, "moveSecOsToCore4: Failed to move sec OS to core2");
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
                    Log.d(TAG, "moveSecOsToDefaultCore: QSEE do not need core migration");
                }
            } else {
                if (DEBUG) {
                    Log.d(TAG, "moveSecOsToDefaultCore is called");
                }
                if (!this.makeSystemCall(6)) {
                    Log.e(TAG, "moveSecOsToDefaultCore: Failed to move sec OS to core0");
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
            Log.e(TAG, "Error: loadTA failed");
            return false;
        }
        if (!this.initTA()) {
            Log.e(TAG, "Error: initTA failed");
            return false;
        }
        this.unloadTA();
        return true;
    }

    protected void resetMstFlag() {
        TAController tAController = this;
        synchronized (tAController) {
            if (DEBUG) {
                Log.d(TAG, "resetMstFlag is called");
            }
            if (!this.makeSystemCall(4)) {
                Log.e(TAG, "resetMstFlag: Failed to reset MST flag");
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
                Log.d(TAG, "Using setCertInfo API");
                return (Boolean) method.invoke((Object) this.mPaymentHandle, new Object[]{certInfo});
            }
            Log.e(TAG, "mPaymentHandle is null");
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
                Log.e(TAG, "unloadTA: mPaymentHandle is null");
            } else if (!this.bLoaded) {
                Log.e(TAG, "TA is never loaded. Unload is noop");
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

