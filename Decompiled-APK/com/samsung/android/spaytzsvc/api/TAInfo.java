package com.samsung.android.spaytzsvc.api;

import android.os.Build;
import android.spay.PaymentTZServiceConfig.TAConfig;
import com.google.android.gms.location.LocationStatusCodes;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.p008e.SystemPropertiesWrapper;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.TACommands.CommandsInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.File;
import java.lang.reflect.Constructor;

public class TAInfo {
    private static String DUMMY_QC_ROOT = null;
    private static final String DUMMY_TA_DIR = "ta";
    private static final String KB8890;
    private static final String LA8996;
    private static String PINRANDOMFILE_POSTFIX = null;
    private static final String PRO8976;
    public static final String SPAY_CHIPSET_TYPE_QC;
    public static final String SPAY_CIPSET_TYPE_TB;
    public static final String SPAY_TA_TECH_ESE = "ese";
    public static final String SPAY_TA_TECH_TEE = "tee";
    private static final String TAG = "TAInfo";
    private static final String TA_DIR;
    private static final boolean bQC;
    private static int mMinTypeIdx;
    private static final String modelNumber;
    private static final String modelNumberF;
    private TACommands mCommands;
    private String mLSIFileName;
    private boolean mLoadFromSystem;
    private String mProcess;
    private String mQcFileName;
    private String mRoot;
    private Class<?> mTAClass;
    private String mTATechnology;
    private int mType;
    private String mUUID;
    private boolean mUsesPinRandom;

    static {
        modelNumberF = Build.BOARD.replaceAll("[^\\d]*", BuildConfig.FLAVOR);
        modelNumber = modelNumberF.isEmpty() ? SystemPropertiesWrapper.get("ro.chipname", TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE).replaceAll("[^\\d]*", BuildConfig.FLAVOR) : modelNumberF;
        LA8996 = Build.MODEL.matches("(?i)[a-z\\d-]*SM-G(?:891A|93[0|5][ATPVRU08][ZLC\\d]*)") ? "8996" : "8996_LA2";
        KB8890 = Build.MODEL.matches("(?i)[a-z\\d-]*SM-G(?:93[0|5][FXWSKL][D\\d]*)") ? "8890" : "8890_T311";
        PRO8976 = Build.MODEL.matches("(?i)[a-z\\d-]*SM-C(?:900[FY0][D\\d]*)") ? "8976_PRO" : "8976";
        StringBuilder append = new StringBuilder().append("qc").append(File.separator);
        String str = "8996".equalsIgnoreCase(modelNumber) ? LA8996 : "8976".equalsIgnoreCase(modelNumber) ? PRO8976 : modelNumber;
        SPAY_CHIPSET_TYPE_QC = append.append(str).toString();
        append = new StringBuilder().append("tb").append(File.separator);
        if ("8890".equalsIgnoreCase(modelNumber)) {
            str = KB8890;
        } else {
            str = modelNumber;
        }
        SPAY_CIPSET_TYPE_TB = append.append(str).toString();
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
        TA_DIR = DUMMY_TA_DIR + File.separator + (bQC ? SPAY_CHIPSET_TYPE_QC : SPAY_CIPSET_TYPE_TB);
        mMinTypeIdx = LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
        DUMMY_QC_ROOT = TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE;
        PINRANDOMFILE_POSTFIX = ".dat";
    }

    public static synchronized int nextTypeIdx() {
        int i;
        synchronized (TAInfo.class) {
            i = mMinTypeIdx;
            mMinTypeIdx = i + 1;
        }
        return i;
    }

    public TAInfo(String str, Class<?> cls, TACommands tACommands, String str2, String str3, String str4, String str5, String str6, boolean z) {
        this(nextTypeIdx(), str, cls, tACommands, str2, str3, str4, str5, str6, z, false);
    }

    public TAInfo(String str, Class<?> cls, TACommands tACommands, String str2, String str3, String str4, String str5, String str6, boolean z, boolean z2) {
        this(nextTypeIdx(), str, cls, tACommands, str2, str3, str4, str5, str6, z2, z);
    }

    public TAInfo(int i, String str, Class<?> cls, TACommands tACommands, String str2, String str3, String str4, String str5, String str6, boolean z) {
        this(i, str, cls, tACommands, str2, str3, str4, str5, str6, z, false);
    }

    public TAInfo(int i, String str, Class<?> cls, TACommands tACommands, String str2, String str3, String str4, String str5, String str6, boolean z, boolean z2) {
        this.mType = i;
        this.mTATechnology = str;
        this.mTAClass = cls;
        this.mCommands = tACommands;
        this.mUUID = str2;
        this.mRoot = str4;
        this.mProcess = str5;
        this.mQcFileName = str6;
        this.mLSIFileName = str3;
        this.mUsesPinRandom = z;
        this.mLoadFromSystem = z2;
    }

    public static String getTARootDir() {
        return TA_DIR;
    }

    public String getTAPath() {
        return TA_DIR + File.separator + getTAFileName();
    }

    public String getDummyTAPath() {
        return DUMMY_TA_DIR + File.separator + "dummyfile.mp3";
    }

    public int getTAType() {
        return this.mType;
    }

    public String getTATechnology() {
        return this.mTATechnology;
    }

    public String getTAId() {
        return bQC ? this.mProcess : this.mUUID;
    }

    public String getTAFileName() {
        return bQC ? this.mQcFileName : this.mLSIFileName;
    }

    public boolean isLoadFromSystem() {
        return this.mLoadFromSystem;
    }

    public String toString() {
        if (bQC) {
            return "TA - Technology = " + this.mTATechnology + " root = " + this.mRoot + " process = " + this.mProcess;
        }
        return "TA - Technology = " + this.mTATechnology + " uuid = " + this.mUUID;
    }

    public boolean usesPinRandom() {
        return this.mUsesPinRandom;
    }

    public String getPinRandomFileName() {
        if (bQC) {
            String str = getTAFileName().substring(0, getTAFileName().length() - 4) + PINRANDOMFILE_POSTFIX;
            Log.m285d(TAG, "File name for QC is " + str);
            return str;
        }
        str = getTAFileName().substring(getTAFileName().length() - 6, getTAFileName().length() - 4) + PINRANDOMFILE_POSTFIX;
        Log.m285d(TAG, "File name for Tbase is " + str);
        return str;
    }

    public TAConfig getTAConfig() {
        this.mCommands.getCommandsInfo();
        if (isNewTAConfigPresent()) {
            return createNewTAConfig();
        }
        return createOldTAConfig();
    }

    public TAConfig createNewTAConfig() {
        CommandsInfo commandsInfo = this.mCommands.getCommandsInfo();
        try {
            Constructor constructor = TAConfig.class.getConstructor(new Class[]{String.class, String.class, String.class, Integer.TYPE, Integer.TYPE});
            if (constructor == null) {
                Log.m285d(TAG, "New TA Config Constructor not present.");
                return null;
            }
            return (TAConfig) constructor.newInstance(new Object[]{getTATechnology(), getUnifiedRoot(), getUnifiedProcess(), Integer.valueOf(commandsInfo.mMaxRequestSize), Integer.valueOf(commandsInfo.mMaxResponseSize)});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public TAConfig createOldTAConfig() {
        CommandsInfo commandsInfo = this.mCommands.getCommandsInfo();
        try {
            Constructor constructor = TAConfig.class.getConstructor(new Class[]{Integer.TYPE, Integer.TYPE});
            if (constructor == null) {
                Log.m285d(TAG, "Old TA Config Constructor not present.");
                return null;
            }
            return (TAConfig) constructor.newInstance(new Object[]{Integer.valueOf(commandsInfo.mMaxRequestSize), Integer.valueOf(commandsInfo.mMaxResponseSize)});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isNewTAConfigPresent() {
        try {
            if (TAConfig.class.getConstructor(new Class[]{String.class, String.class, String.class, Integer.TYPE, Integer.TYPE}) != null) {
                return true;
            }
            Log.m285d(TAG, "New TA Config Constructor not present. Use old API");
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private String getUnifiedRoot() {
        return bQC ? this.mRoot : DUMMY_QC_ROOT;
    }

    private String getUnifiedProcess() {
        return bQC ? this.mProcess : this.mUUID;
    }
}
