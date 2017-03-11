package com.samsung.android.spaytzsvc.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.spay.CertInfo;
import android.spay.ITAController;
import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.google.android.gms.location.LocationStatusCodes;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.TACommands.Init.Request;
import com.samsung.android.spaytzsvc.api.TACommands.Init.Response;
import com.samsung.android.spaytzsvc.api.TACommands.LoadPinRandom;
import com.samsung.android.spaytzsvc.api.TACommands.MoveServiceKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class TAController implements IPaymentSvcDeathReceiver {
    public static final int CMD_ABORT_MST = 3;
    public static final int CMD_MOVE_SEC_OS_CORE0 = 6;
    public static final int CMD_MOVE_SEC_OS_CORE4 = 5;
    public static final int CMD_MST_OFF = 2;
    public static final int CMD_MST_ON = 1;
    public static final int CMD_RESET_MST = 4;
    public static final boolean DEBUG;
    private static final String PF_EFS_ROOT_DIR_SPAY_UID = "/efs/pfw_data";
    private static final String PF_EFS_ROOT_DIR_SYSTEM_UID = "/efs/prov_data/pfw_data";
    private static final String TAG = "TAController";
    private static final int TA_LOAD_RETRY_COUNT = 5;
    private static final boolean bQC;
    private boolean bLoaded;
    private boolean bMeasurementVerified;
    private Context mContext;
    protected ITAController mPaymentHandle;
    protected TAInfo mTAInfo;

    static {
        DEBUG = "eng".equals(Build.TYPE);
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
    }

    protected TAController(Context context, TAInfo tAInfo) {
        this.bLoaded = DEBUG;
        this.bMeasurementVerified = DEBUG;
        this.mContext = context;
        this.mTAInfo = tAInfo;
    }

    protected boolean init() {
        PaymentTZServiceIF instance = PaymentTZServiceIF.getInstance();
        this.mPaymentHandle = instance.getTAController(this.mTAInfo.getTAType());
        instance.registerForDisconnection(this);
        prepareTA();
        return true;
    }

    public TAInfo getTAInfo() {
        return this.mTAInfo;
    }

    public static boolean isChipSetQC() {
        return bQC;
    }

    public Context getContext() {
        return this.mContext;
    }

    public static String getEfsDirectory() {
        if (DEBUG) {
            Log.m285d(TAG, "getEfsDirectory: my UID = " + Process.myUid());
        }
        if (Process.myUid() == LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
            return PF_EFS_ROOT_DIR_SYSTEM_UID;
        }
        return PF_EFS_ROOT_DIR_SPAY_UID;
    }

    protected String findTAByDeviceModel(AssetManager assetManager) {
        return this.mTAInfo.getTAPath();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.content.res.AssetFileDescriptor getTaFd() {
        /*
        r9 = this;
        r0 = 0;
        r1 = r9.mContext;
        r2 = r1.getAssets();
        if (r2 != 0) goto L_0x000a;
    L_0x0009:
        return r0;
    L_0x000a:
        r1 = r9.shouldLoadTAFromSystem();
        if (r1 != 0) goto L_0x0009;
    L_0x0010:
        r1 = DEBUG;	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        if (r1 == 0) goto L_0x0032;
    L_0x0014:
        r1 = "TAController";
        r3 = new java.lang.StringBuilder;	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r3.<init>();	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r4 = "getTA: ";
        r3 = r3.append(r4);	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r4 = r9.mTAInfo;	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r4 = r4.getTAFileName();	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r3 = r3.append(r4);	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r3 = r3.toString();	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r3);	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
    L_0x0032:
        r1 = DEBUG;	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        if (r1 == 0) goto L_0x0061;
    L_0x0036:
        r1 = com.samsung.android.spaytzsvc.api.TAInfo.getTARootDir();	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r3 = r2.list(r1);	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        if (r3 == 0) goto L_0x0061;
    L_0x0040:
        r4 = r3.length;	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r1 = 0;
    L_0x0042:
        if (r1 >= r4) goto L_0x0061;
    L_0x0044:
        r5 = r3[r1];	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r6 = "TAController";
        r7 = new java.lang.StringBuilder;	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r7.<init>();	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r8 = "File : ";
        r7 = r7.append(r8);	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r5 = r7.append(r5);	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r5 = r5.toString();	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        com.samsung.android.spayfw.p002b.Log.m285d(r6, r5);	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r1 = r1 + 1;
        goto L_0x0042;
    L_0x0061:
        r1 = r9.findTAByDeviceModel(r2);	 Catch:{ FileNotFoundException -> 0x00b1, Exception -> 0x00a4 }
        r0 = r2.openFd(r1);	 Catch:{ FileNotFoundException -> 0x0086, Exception -> 0x00a4 }
        r2 = DEBUG;	 Catch:{ FileNotFoundException -> 0x0086, Exception -> 0x00a4 }
        if (r2 == 0) goto L_0x0009;
    L_0x006d:
        r2 = "TAController";
        r3 = new java.lang.StringBuilder;	 Catch:{ FileNotFoundException -> 0x0086, Exception -> 0x00a4 }
        r3.<init>();	 Catch:{ FileNotFoundException -> 0x0086, Exception -> 0x00a4 }
        r4 = "Found TA file: ";
        r3 = r3.append(r4);	 Catch:{ FileNotFoundException -> 0x0086, Exception -> 0x00a4 }
        r3 = r3.append(r1);	 Catch:{ FileNotFoundException -> 0x0086, Exception -> 0x00a4 }
        r3 = r3.toString();	 Catch:{ FileNotFoundException -> 0x0086, Exception -> 0x00a4 }
        com.samsung.android.spayfw.p002b.Log.m285d(r2, r3);	 Catch:{ FileNotFoundException -> 0x0086, Exception -> 0x00a4 }
        goto L_0x0009;
    L_0x0086:
        r2 = move-exception;
    L_0x0087:
        r2 = "TAController";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a1 }
        r3.<init>();	 Catch:{ all -> 0x00a1 }
        r4 = "TA file not found: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x00a1 }
        r1 = r3.append(r1);	 Catch:{ all -> 0x00a1 }
        r1 = r1.toString();	 Catch:{ all -> 0x00a1 }
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r1);	 Catch:{ all -> 0x00a1 }
        goto L_0x0009;
    L_0x00a1:
        r1 = move-exception;
        goto L_0x0009;
    L_0x00a4:
        r1 = move-exception;
        r2 = "TAController";
        r3 = "general exception";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);	 Catch:{ all -> 0x00a1 }
        r1.printStackTrace();	 Catch:{ all -> 0x00a1 }
        goto L_0x0009;
    L_0x00b1:
        r1 = move-exception;
        r1 = r0;
        goto L_0x0087;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytzsvc.api.TAController.getTaFd():android.content.res.AssetFileDescriptor");
    }

    private boolean shouldLoadTAFromSystem() {
        if (DEBUG) {
            SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("shared_preferences_test", CMD_RESET_MST);
            int i = sharedPreferences.getInt(this.mTAInfo.getTAFileName(), LocationStatusCodes.GEOFENCE_NOT_AVAILABLE);
            Log.m285d(TAG, "fromSystem=" + i);
            if (i == CMD_MST_ON) {
                return true;
            }
            if (i == LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
                Editor edit = sharedPreferences.edit();
                edit.putInt(this.mTAInfo.getTAFileName(), 0);
                edit.apply();
            }
        }
        if (this.mTAInfo.isLoadFromSystem()) {
            return true;
        }
        return DEBUG;
    }

    public synchronized boolean loadTA() {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Exception block dominator not found, method:com.samsung.android.spaytzsvc.api.TAController.loadTA():boolean. bs: [B:27:0x0059, B:97:0x0158]
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:86)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:57)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
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
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytzsvc.api.TAController.loadTA():boolean");
    }

    private ParcelFileDescriptor createDummyFD() {
        try {
            return this.mContext.getAssets().openFd(this.mTAInfo.getDummyTAPath()).getParcelFileDescriptor();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void unloadTA() {
        if (this.mPaymentHandle == null) {
            Log.m286e(TAG, "unloadTA: mPaymentHandle is null");
        } else if (this.bLoaded) {
            try {
                this.mPaymentHandle.unloadTA();
                this.bLoaded = DEBUG;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.m286e(TAG, "TA is never loaded. Unload is noop");
        }
    }

    public synchronized boolean isTALoaded() {
        return this.bLoaded;
    }

    boolean prepareTA() {
        if (isAndroidMAndAbove() || this.bMeasurementVerified) {
            return true;
        }
        if (!loadTA()) {
            Log.m286e(TAG, "Error: loadTA failed");
            return DEBUG;
        } else if (initTA()) {
            unloadTA();
            return true;
        } else {
            Log.m286e(TAG, "Error: initTA failed");
            return DEBUG;
        }
    }

    public synchronized TACommandResponse executeNoLoad(TACommandRequest tACommandRequest) {
        TACommandResponse tACommandResponse = null;
        synchronized (this) {
            if (isTALoaded()) {
                try {
                    if (this.mPaymentHandle != null) {
                        tACommandResponse = this.mPaymentHandle.processTACommand(tACommandRequest);
                        if (tACommandResponse != null && DEBUG) {
                            Log.m285d(TAG, "executeNoLoad: Response Code = " + tACommandResponse.mResponseCode);
                            Log.m285d(TAG, "executeNoLoad: Error Message = " + tACommandResponse.mErrorMsg);
                            Log.m285d(TAG, "executeNoLoad: Response Len = " + tACommandResponse.mResponse.length + " Buf = " + Arrays.toString(tACommandResponse.mResponse));
                        }
                    } else {
                        Log.m286e(TAG, "executeNoLoad: mPaymentHandle is null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.m286e(TAG, "TA has to be loaded before calling executeNoLoad");
            }
        }
        return tACommandResponse;
    }

    public synchronized TACommandResponse loadExecuteUnload(TACommandRequest tACommandRequest) {
        Exception exception;
        TACommandResponse tACommandResponse;
        Exception exception2;
        TACommandResponse tACommandResponse2 = null;
        synchronized (this) {
            if (loadTA()) {
                try {
                    if (this.mPaymentHandle != null) {
                        tACommandResponse2 = this.mPaymentHandle.processTACommand(tACommandRequest);
                        if (tACommandResponse2 != null) {
                            try {
                                if (DEBUG) {
                                    Log.m285d(TAG, "execute: Response Code = " + tACommandResponse2.mResponseCode);
                                    Log.m285d(TAG, "execute: Error Message = " + tACommandResponse2.mErrorMsg);
                                    Log.m285d(TAG, "execute: Response Len = " + tACommandResponse2.mResponse.length + " Buf = " + Arrays.toString(tACommandResponse2.mResponse));
                                }
                            } catch (Exception e) {
                                exception = e;
                                tACommandResponse = tACommandResponse2;
                                exception2 = exception;
                                exception2.printStackTrace();
                                tACommandResponse2 = tACommandResponse;
                                unloadTA();
                                return tACommandResponse2;
                            }
                        }
                    }
                    Log.m286e(TAG, "execute: mPaymentHandle is null");
                } catch (Exception e2) {
                    exception = e2;
                    tACommandResponse = null;
                    exception2 = exception;
                    exception2.printStackTrace();
                    tACommandResponse2 = tACommandResponse;
                    unloadTA();
                    return tACommandResponse2;
                }
                unloadTA();
            } else {
                Log.m286e(TAG, "TA Loading failed");
            }
        }
        return tACommandResponse2;
    }

    public void onDisconnected() {
        this.mPaymentHandle = null;
    }

    public boolean usesPinRandom() {
        return this.mTAInfo.usesPinRandom();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.spay.CertInfo getCertInfo() {
        /*
        r5 = this;
        r1 = 0;
        r2 = com.samsung.android.spaytzsvc.api.TAController.class;
        monitor-enter(r2);
        r0 = r5.mPaymentHandle;	 Catch:{ Exception -> 0x0031 }
        if (r0 == 0) goto L_0x0027;
    L_0x0008:
        r0 = r5.mPaymentHandle;	 Catch:{ Exception -> 0x0031 }
        r0 = r0.getClass();	 Catch:{ Exception -> 0x0031 }
        r3 = "getCertInfo";
        r4 = 0;
        r0 = r0.getMethod(r3, r4);	 Catch:{ Exception -> 0x0031 }
        r3 = "TAController";
        r4 = "Using getCertInfo API";
        com.samsung.android.spayfw.p002b.Log.m285d(r3, r4);	 Catch:{ Exception -> 0x0031 }
        r3 = r5.mPaymentHandle;	 Catch:{ Exception -> 0x0031 }
        r4 = 0;
        r0 = r0.invoke(r3, r4);	 Catch:{ Exception -> 0x0031 }
        r0 = (android.spay.CertInfo) r0;	 Catch:{ Exception -> 0x0031 }
        monitor-exit(r2);	 Catch:{ all -> 0x0036 }
    L_0x0026:
        return r0;
    L_0x0027:
        r0 = "TAController";
        r3 = "mPaymentHandle is null";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r3);	 Catch:{ Exception -> 0x0031 }
    L_0x002e:
        monitor-exit(r2);	 Catch:{ all -> 0x0036 }
        r0 = r1;
        goto L_0x0026;
    L_0x0031:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0036 }
        goto L_0x002e;
    L_0x0036:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0036 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytzsvc.api.TAController.getCertInfo():android.spay.CertInfo");
    }

    public boolean isDeviceCertificateMigratable() {
        try {
            Class[] clsArr = new Class[CMD_MST_ON];
            clsArr[0] = List.class;
            if (this.mPaymentHandle.getClass().getMethod("checkCertInfo", clsArr) != null) {
                return true;
            }
            Log.m285d(TAG, "API checkCertInfo Not found");
            return DEBUG;
        } catch (NoSuchMethodException e) {
            Log.m285d(TAG, "API checkCertInfo Not found");
            return DEBUG;
        } catch (Exception e2) {
            e2.printStackTrace();
            return DEBUG;
        }
    }

    public CertInfo checkCertInfo(List<String> list) {
        try {
            if (this.mPaymentHandle != null) {
                Class[] clsArr = new Class[CMD_MST_ON];
                clsArr[0] = List.class;
                Method method = this.mPaymentHandle.getClass().getMethod("checkCertInfo", clsArr);
                Log.m285d(TAG, "Using checkCertInfo API");
                ITAController iTAController = this.mPaymentHandle;
                Object[] objArr = new Object[CMD_MST_ON];
                objArr[0] = list;
                return (CertInfo) method.invoke(iTAController, objArr);
            }
            Log.m286e(TAG, "mPaymentHandle is null");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean clearDeviceCertificates(String str) {
        try {
            if (this.mPaymentHandle == null) {
                Log.m286e(TAG, "mPaymentHandle is null");
                return DEBUG;
            } else if (str != null) {
                Class[] clsArr = new Class[CMD_MST_ON];
                clsArr[0] = String.class;
                r0 = this.mPaymentHandle.getClass().getMethod("clearDeviceCertificates", clsArr);
                Log.m287i(TAG, "Using NEW clearDeviceCertificates API");
                ITAController iTAController = this.mPaymentHandle;
                Object[] objArr = new Object[CMD_MST_ON];
                objArr[0] = str;
                return ((Boolean) r0.invoke(iTAController, objArr)).booleanValue();
            } else {
                r0 = this.mPaymentHandle.getClass().getMethod("clearDeviceCertificates", new Class[0]);
                Log.m287i(TAG, "Using OLD clearDeviceCertificates API");
                return ((Boolean) r0.invoke(this.mPaymentHandle, new Object[0])).booleanValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean copyMctoRst() {
        try {
            if (this.mPaymentHandle != null) {
                Method method = this.mPaymentHandle.getClass().getMethod("copyMctoRst", new Class[0]);
                Log.m285d(TAG, "Using copyMctoRst API");
                return ((Boolean) method.invoke(this.mPaymentHandle, new Object[0])).booleanValue();
            }
            Log.m286e(TAG, "mPaymentHandle is null");
            return DEBUG;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setCertInfo(CertInfo certInfo) {
        try {
            if (this.mPaymentHandle != null) {
                Class[] clsArr = new Class[CMD_MST_ON];
                clsArr[0] = CertInfo.class;
                Method method = this.mPaymentHandle.getClass().getMethod("setCertInfo", clsArr);
                Log.m285d(TAG, "Using setCertInfo API");
                ITAController iTAController = this.mPaymentHandle;
                Object[] objArr = new Object[CMD_MST_ON];
                objArr[0] = certInfo;
                return ((Boolean) method.invoke(iTAController, objArr)).booleanValue();
            }
            Log.m286e(TAG, "mPaymentHandle is null");
            return DEBUG;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean makeSystemCall(int i) {
        try {
            if (this.mPaymentHandle != null) {
                return this.mPaymentHandle.makeSystemCall(i);
            }
            Log.m286e(TAG, "mPaymentHandle is null");
            return DEBUG;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public synchronized void abortMstTransmission() {
        if (DEBUG) {
            Log.m285d(TAG, "abortMstTransmission is called");
        }
        if (!makeSystemCall(CMD_ABORT_MST)) {
            Log.m286e(TAG, "abortMstTransmission: Failed to abort MST");
        }
    }

    protected void dumpHex(String str, String str2, byte[] bArr) {
        if (bArr != null) {
            StringBuilder stringBuilder = new StringBuilder();
            int length = bArr.length;
            for (int i = 0; i < length; i += CMD_MST_ON) {
                Object[] objArr = new Object[CMD_MST_ON];
                objArr[0] = Byte.valueOf(bArr[i]);
                stringBuilder.append(String.format("%02X ", objArr));
            }
            Log.m285d(str, str2 + " " + stringBuilder.toString());
        }
    }

    protected synchronized void resetMstFlag() {
        if (DEBUG) {
            Log.m285d(TAG, "resetMstFlag is called");
        }
        if (!makeSystemCall(CMD_RESET_MST)) {
            Log.m286e(TAG, "resetMstFlag: Failed to reset MST flag");
        }
    }

    public synchronized void moveSecOsToCore4() {
        if (!bQC) {
            if (DEBUG) {
                Log.m285d(TAG, "moveSecOsToCore4 is called");
            }
            if (!makeSystemCall(TA_LOAD_RETRY_COUNT)) {
                Log.m286e(TAG, "moveSecOsToCore4: Failed to move sec OS to core2");
            }
        } else if (DEBUG) {
            Log.m285d(TAG, "moveSecOsToCore4: QSEE do not need core migration");
        }
    }

    public synchronized void moveSecOsToDefaultCore() {
        if (!bQC) {
            if (DEBUG) {
                Log.m285d(TAG, "moveSecOsToDefaultCore is called");
            }
            if (!makeSystemCall(CMD_MOVE_SEC_OS_CORE0)) {
                Log.m286e(TAG, "moveSecOsToDefaultCore: Failed to move sec OS to core0");
            }
        } else if (DEBUG) {
            Log.m285d(TAG, "moveSecOsToDefaultCore: QSEE do not need core migration");
        }
    }

    public boolean initTA() {
        try {
            initTA(0, TACommands.TL_MAGIC_NUM);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return DEBUG;
        }
    }

    private void initTA(int i, byte[] bArr) {
        if (!this.bMeasurementVerified) {
            if (DEBUG) {
                Log.m285d(TAG, "Calling initTA");
            }
            TACommandResponse executeNoLoad = executeNoLoad(new Request(null));
            if (executeNoLoad == null) {
                Log.m286e(TAG, "Error: execute failed");
                throw new TAException("Error: executeNoLoad failed", CMD_MST_ON);
            }
            Response response = new Response(executeNoLoad);
            if (response.mRetVal.result.get() == 65547) {
                if (DEBUG) {
                    Log.m285d(TAG, "ReInitializing TA - Reason : Received Error Code TZ_COMMON_INIT_UNINITIALIZED_SECURE_MEM");
                }
                executeNoLoad = executeNoLoad(new Request(PaymentTZServiceIF.getInstance().getMeasurementFile()));
                if (executeNoLoad == null) {
                    Log.m286e(TAG, "Error: execute failed");
                    throw new TAException("Error: executeNoLoad failed", CMD_MST_ON);
                }
                response = new Response(executeNoLoad);
            }
            long j = response.mRetVal.result.get();
            if (j != 0) {
                Log.m286e(TAG, "Error: initTA failed");
                if (j == 65548 || j == 65549 || j == 65550) {
                    throw new TAException("Error: initTA failed", CMD_MST_OFF);
                }
                throw new TAException("Error: initTA failed", CMD_MST_ON);
            }
            if (DEBUG) {
                Log.m285d(TAG, "initTA called Successfully");
            }
            this.bMeasurementVerified = true;
        } else if (DEBUG) {
            Log.m285d(TAG, "initTA need not be called as measurement is already verified");
        }
    }

    public byte[] decapsulateAndWrap(byte[] bArr) {
        TACommandResponse executeNoLoad = executeNoLoad(new MoveServiceKey.Request(bArr));
        if (executeNoLoad == null) {
            Log.m286e(TAG, "Error: execute failed");
            throw new TAException("Error: executeNoLoad failed", CMD_MST_ON);
        }
        MoveServiceKey.Response response = new MoveServiceKey.Response(executeNoLoad);
        if (response.mRetVal.return_code.get() != 0) {
            Log.m285d(TAG, "Error: decapsulateAndWrap failed - response.mRetVal = " + response.mErrorMsg);
            throw new TAException("Error: decapsulateAndWrap failed" + response.mErrorMsg, CMD_RESET_MST);
        }
        if (DEBUG) {
            Log.m285d(TAG, "decapsulateAndWrap called Successfully");
        }
        return response.mRetVal.wrapped_msg.getData();
    }

    private int loadPinRandom() {
        Exception e;
        Throwable th;
        if (usesPinRandom()) {
            File file = new File(getEfsDirectory(), this.mTAInfo.getPinRandomFileName());
            if (file.isFile()) {
                byte[] bArr = new byte[((int) file.length())];
                FileInputStream fileInputStream;
                try {
                    fileInputStream = new FileInputStream(file);
                    try {
                        int length = (int) file.length();
                        int i = 0;
                        while (length > 0) {
                            int read = fileInputStream.read(bArr, i, length);
                            i += read;
                            length -= read;
                        }
                        TACommandResponse executeNoLoad = executeNoLoad(new LoadPinRandom.Request(bArr));
                        if (executeNoLoad == null) {
                            Log.m286e(TAG, "Error: execute failed");
                            throw new TAException("Error: executeNoLoad failed", CMD_MST_ON);
                        }
                        if (new LoadPinRandom.Response(executeNoLoad).mRetVal.result.get() != 0) {
                            Log.m286e(TAG, "Load PIN random failed for TA " + this.mTAInfo);
                        } else if (DEBUG) {
                            Log.m285d(TAG, "Load PIN random succeeded for TA " + this.mTAInfo);
                        }
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                    } catch (Exception e3) {
                        e = e3;
                        try {
                            Log.m286e(TAG, "Cannot load PIN random for TA " + this.mTAInfo);
                            e.printStackTrace();
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e22) {
                                    e22.printStackTrace();
                                }
                            }
                            return 0;
                        } catch (Throwable th2) {
                            th = th2;
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e4) {
                                    e4.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    }
                } catch (Exception e5) {
                    e = e5;
                    fileInputStream = null;
                    Log.m286e(TAG, "Cannot load PIN random for TA " + this.mTAInfo);
                    e.printStackTrace();
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    return 0;
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream = null;
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    throw th;
                }
            }
            Log.m286e(TAG, "Cannot load PIN random for TA " + this.mTAInfo + ", file " + file.getAbsolutePath() + " not found");
        } else {
            Log.m287i(TAG, "No need to load PIN random for TA " + this.mTAInfo);
        }
        return 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int setupPinRandom(byte[] r10) {
        /*
        r9 = this;
        r0 = 0;
        r1 = -1;
        r2 = bQC;
        if (r2 != 0) goto L_0x0007;
    L_0x0006:
        return r0;
    L_0x0007:
        r2 = r9.usesPinRandom();
        if (r2 == 0) goto L_0x00fa;
    L_0x000d:
        r2 = new com.samsung.android.spaytzsvc.api.TACommands$SetupPinRandom$Request;
        r2.<init>(r10);
        r2 = r9.executeNoLoad(r2);
        if (r2 != 0) goto L_0x0021;
    L_0x0018:
        r0 = "TAController";
        r2 = "Error: execute failed";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);
        r0 = r1;
        goto L_0x0006;
    L_0x0021:
        r4 = new com.samsung.android.spaytzsvc.api.TACommands$SetupPinRandom$Response;
        r4.<init>(r2);
        r2 = r4.mRetVal;
        r2 = r2.result;
        r2 = r2.get();
        r6 = 0;
        r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r2 == 0) goto L_0x0050;
    L_0x0034:
        r0 = "TAController";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Setup PIN random failed for TA ";
        r2 = r2.append(r3);
        r3 = r9.mTAInfo;
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);
        r0 = r1;
        goto L_0x0006;
    L_0x0050:
        r2 = r9.mTAInfo;
        r2 = r2.getPinRandomFileName();
        r3 = new java.io.File;
        r5 = getEfsDirectory();
        r3.<init>(r5, r2);
        r2 = DEBUG;
        if (r2 == 0) goto L_0x008b;
    L_0x0063:
        r2 = "TAController";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Setup PIN random succeeded for TA ";
        r5 = r5.append(r6);
        r6 = r9.mTAInfo;
        r5 = r5.append(r6);
        r6 = ". Write to file ";
        r5 = r5.append(r6);
        r6 = r3.getName();
        r5 = r5.append(r6);
        r5 = r5.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r2, r5);
    L_0x008b:
        r5 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x00d3 }
        r5.<init>(r3);	 Catch:{ Exception -> 0x00d3 }
        r3 = 0;
        r2 = DEBUG;	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        if (r2 == 0) goto L_0x00b6;
    L_0x0095:
        r2 = "TAController";
        r6 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        r6.<init>();	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        r7 = "File Write - Length = ";
        r6 = r6.append(r7);	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        r7 = r4.mRetVal;	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        r7 = r7.pin_random_so;	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        r7 = r7.getData();	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        r7 = r7.length;	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        r6 = r6.append(r7);	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        r6 = r6.toString();	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        com.samsung.android.spayfw.p002b.Log.m285d(r2, r6);	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
    L_0x00b6:
        r2 = r4.mRetVal;	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        r2 = r2.pin_random_so;	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        r2 = r2.getData();	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        r5.write(r2);	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        r5.close();	 Catch:{ Throwable -> 0x00e3, all -> 0x0116 }
        if (r5 == 0) goto L_0x0006;
    L_0x00c6:
        if (r3 == 0) goto L_0x00de;
    L_0x00c8:
        r5.close();	 Catch:{ Throwable -> 0x00cd }
        goto L_0x0006;
    L_0x00cd:
        r2 = move-exception;
        r3.addSuppressed(r2);	 Catch:{ Exception -> 0x00d3 }
        goto L_0x0006;
    L_0x00d3:
        r2 = move-exception;
        r3 = DEBUG;
        if (r3 == 0) goto L_0x0006;
    L_0x00d8:
        r2.printStackTrace();
        r0 = r1;
        goto L_0x0006;
    L_0x00de:
        r5.close();	 Catch:{ Exception -> 0x00d3 }
        goto L_0x0006;
    L_0x00e3:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x00e5 }
    L_0x00e5:
        r3 = move-exception;
        r8 = r3;
        r3 = r2;
        r2 = r8;
    L_0x00e9:
        if (r5 == 0) goto L_0x00f0;
    L_0x00eb:
        if (r3 == 0) goto L_0x00f6;
    L_0x00ed:
        r5.close();	 Catch:{ Throwable -> 0x00f1 }
    L_0x00f0:
        throw r2;	 Catch:{ Exception -> 0x00d3 }
    L_0x00f1:
        r4 = move-exception;
        r3.addSuppressed(r4);	 Catch:{ Exception -> 0x00d3 }
        goto L_0x00f0;
    L_0x00f6:
        r5.close();	 Catch:{ Exception -> 0x00d3 }
        goto L_0x00f0;
    L_0x00fa:
        r1 = "TAController";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "No need to setup PIN random for TA ";
        r2 = r2.append(r3);
        r3 = r9.mTAInfo;
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r1, r2);
        goto L_0x0006;
    L_0x0116:
        r2 = move-exception;
        goto L_0x00e9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spaytzsvc.api.TAController.setupPinRandom(byte[]):int");
    }

    private static final boolean isAndroidMAndAbove() {
        if (VERSION.SDK_INT <= 22) {
            return DEBUG;
        }
        Log.m285d(TAG, "You are using Android Version " + VERSION.SDK_INT);
        return true;
    }
}
