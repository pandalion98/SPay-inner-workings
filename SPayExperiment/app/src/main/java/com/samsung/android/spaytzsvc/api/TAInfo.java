/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.spay.PaymentTZServiceConfig
 *  android.spay.PaymentTZServiceConfig$TAConfig
 *  java.io.File
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.reflect.Constructor
 */
package com.samsung.android.spaytzsvc.api;

import android.os.Build;
import android.spay.PaymentTZServiceConfig;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.e.d;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TAInfo {
    private static String DUMMY_QC_ROOT;
    private static final String DUMMY_TA_DIR = "ta";
    private static final String KB8890;
    private static final String LA8996;
    private static String PINRANDOMFILE_POSTFIX;
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

    /*
     * Enabled aggressive block sorting
     */
    static {
        modelNumberF = Build.BOARD.replaceAll("[^\\d]*", "");
        String string = modelNumberF.isEmpty() ? d.get("ro.chipname", "0").replaceAll("[^\\d]*", "") : modelNumberF;
        modelNumber = string;
        String string2 = Build.MODEL.matches("(?i)[a-z\\d-]*SM-G(?:891A|93[0|5][ATPVRU08][ZLC\\d]*)") ? "8996" : "8996_LA2";
        LA8996 = string2;
        String string3 = Build.MODEL.matches("(?i)[a-z\\d-]*SM-G(?:93[0|5][FXWSKL][D\\d]*)") ? "8890" : "8890_T311";
        KB8890 = string3;
        String string4 = Build.MODEL.matches("(?i)[a-z\\d-]*SM-C(?:900[FY0][D\\d]*)") ? "8976_PRO" : "8976";
        PRO8976 = string4;
        StringBuilder stringBuilder = new StringBuilder().append("qc").append(File.separator);
        String string5 = "8996".equalsIgnoreCase(modelNumber) ? LA8996 : ("8976".equalsIgnoreCase(modelNumber) ? PRO8976 : modelNumber);
        SPAY_CHIPSET_TYPE_QC = stringBuilder.append(string5).toString();
        StringBuilder stringBuilder2 = new StringBuilder().append("tb").append(File.separator);
        String string6 = "8890".equalsIgnoreCase(modelNumber) ? KB8890 : modelNumber;
        SPAY_CIPSET_TYPE_TB = stringBuilder2.append(string6).toString();
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
        StringBuilder stringBuilder3 = new StringBuilder().append(DUMMY_TA_DIR).append(File.separator);
        String string7 = bQC ? SPAY_CHIPSET_TYPE_QC : SPAY_CIPSET_TYPE_TB;
        TA_DIR = stringBuilder3.append(string7).toString();
        mMinTypeIdx = 1000;
        DUMMY_QC_ROOT = "0";
        PINRANDOMFILE_POSTFIX = ".dat";
    }

    public TAInfo(int n2, String string, Class<?> class_, TACommands tACommands, String string2, String string3, String string4, String string5, String string6, boolean bl) {
        this(n2, string, class_, tACommands, string2, string3, string4, string5, string6, bl, false);
    }

    public TAInfo(int n2, String string, Class<?> class_, TACommands tACommands, String string2, String string3, String string4, String string5, String string6, boolean bl, boolean bl2) {
        this.mType = n2;
        this.mTATechnology = string;
        this.mTAClass = class_;
        this.mCommands = tACommands;
        this.mUUID = string2;
        this.mRoot = string4;
        this.mProcess = string5;
        this.mQcFileName = string6;
        this.mLSIFileName = string3;
        this.mUsesPinRandom = bl;
        this.mLoadFromSystem = bl2;
    }

    public TAInfo(String string, Class<?> class_, TACommands tACommands, String string2, String string3, String string4, String string5, String string6, boolean bl) {
        this(TAInfo.nextTypeIdx(), string, class_, tACommands, string2, string3, string4, string5, string6, bl, false);
    }

    public TAInfo(String string, Class<?> class_, TACommands tACommands, String string2, String string3, String string4, String string5, String string6, boolean bl, boolean bl2) {
        this(TAInfo.nextTypeIdx(), string, class_, tACommands, string2, string3, string4, string5, string6, bl2, bl);
    }

    public static String getTARootDir() {
        return TA_DIR;
    }

    private String getUnifiedProcess() {
        if (bQC) {
            return this.mProcess;
        }
        return this.mUUID;
    }

    private String getUnifiedRoot() {
        if (bQC) {
            return this.mRoot;
        }
        return DUMMY_QC_ROOT;
    }

    public static int nextTypeIdx() {
        Class<TAInfo> class_ = TAInfo.class;
        synchronized (TAInfo.class) {
            int n2 = mMinTypeIdx;
            mMinTypeIdx = n2 + 1;
            // ** MonitorExit[var2] (shouldn't be in output)
            return n2;
        }
    }

    public PaymentTZServiceConfig.TAConfig createNewTAConfig() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        TACommands.CommandsInfo commandsInfo;
        Constructor constructor;
        block4 : {
            commandsInfo = this.mCommands.getCommandsInfo();
            try {
                Class[] arrclass = new Class[]{String.class, String.class, String.class, Integer.TYPE, Integer.TYPE};
                constructor = PaymentTZServiceConfig.TAConfig.class.getConstructor(arrclass);
                if (constructor != null) break block4;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
            Log.d(TAG, "New TA Config Constructor not present.");
            return null;
        }
        Object[] arrobject = new Object[]{this.getTATechnology(), this.getUnifiedRoot(), this.getUnifiedProcess(), commandsInfo.mMaxRequestSize, commandsInfo.mMaxResponseSize};
        PaymentTZServiceConfig.TAConfig tAConfig = (PaymentTZServiceConfig.TAConfig)constructor.newInstance(arrobject);
        return tAConfig;
    }

    public PaymentTZServiceConfig.TAConfig createOldTAConfig() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        TACommands.CommandsInfo commandsInfo;
        Constructor constructor;
        block4 : {
            commandsInfo = this.mCommands.getCommandsInfo();
            try {
                Class[] arrclass = new Class[]{Integer.TYPE, Integer.TYPE};
                constructor = PaymentTZServiceConfig.TAConfig.class.getConstructor(arrclass);
                if (constructor != null) break block4;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
            Log.d(TAG, "Old TA Config Constructor not present.");
            return null;
        }
        Object[] arrobject = new Object[]{commandsInfo.mMaxRequestSize, commandsInfo.mMaxResponseSize};
        PaymentTZServiceConfig.TAConfig tAConfig = (PaymentTZServiceConfig.TAConfig)constructor.newInstance(arrobject);
        return tAConfig;
    }

    public String getDummyTAPath() {
        return DUMMY_TA_DIR + File.separator + "dummyfile.mp3";
    }

    public String getPinRandomFileName() {
        if (bQC) {
            String string = this.getTAFileName().substring(0, -4 + this.getTAFileName().length()) + PINRANDOMFILE_POSTFIX;
            Log.d(TAG, "File name for QC is " + string);
            return string;
        }
        String string = this.getTAFileName().substring(-6 + this.getTAFileName().length(), -4 + this.getTAFileName().length()) + PINRANDOMFILE_POSTFIX;
        Log.d(TAG, "File name for Tbase is " + string);
        return string;
    }

    public PaymentTZServiceConfig.TAConfig getTAConfig() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.mCommands.getCommandsInfo();
        if (!this.isNewTAConfigPresent()) {
            return this.createOldTAConfig();
        }
        return this.createNewTAConfig();
    }

    public String getTAFileName() {
        if (bQC) {
            return this.mQcFileName;
        }
        return this.mLSIFileName;
    }

    public String getTAId() {
        if (bQC) {
            return this.mProcess;
        }
        return this.mUUID;
    }

    public String getTAPath() {
        return TA_DIR + File.separator + this.getTAFileName();
    }

    public String getTATechnology() {
        return this.mTATechnology;
    }

    public int getTAType() {
        return this.mType;
    }

    public boolean isLoadFromSystem() {
        return this.mLoadFromSystem;
    }

    public boolean isNewTAConfigPresent() {
        try {
            Class[] arrclass = new Class[]{String.class, String.class, String.class, Integer.TYPE, Integer.TYPE};
            if (PaymentTZServiceConfig.TAConfig.class.getConstructor(arrclass) == null) {
                Log.d(TAG, "New TA Config Constructor not present. Use old API");
                return false;
            }
            return true;
        }
        catch (Exception exception) {
            return false;
        }
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
}

