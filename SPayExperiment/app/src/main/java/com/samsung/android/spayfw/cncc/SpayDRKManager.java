/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.spay.CertInfo
 *  com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager
 *  com.samsung.android.service.DeviceRootKeyService.Tlv
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Class
 *  java.lang.ClassNotFoundException
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.cncc;

import android.content.Context;
import android.os.Build;
import android.spay.CertInfo;
import com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager;
import com.samsung.android.service.DeviceRootKeyService.Tlv;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.Utils;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERUTF8String;

public class SpayDRKManager {
    private static final boolean DEBUG = false;
    private static final String DEVCERTS_FOLDER = "devicecerts";
    public static final String SKMM_TA_ID;
    private static final String TAG = "SpayDRKManager";
    private static final boolean bQC;
    private static DeviceRootKeyServiceManager gDeviceRootKeyServiceManager;
    private static int mErrorFlag;
    private static boolean mIsInitialized;
    private static boolean mIsSupported;
    private boolean bUseCNCC = true;
    private TAController mCardTAController;
    private String mCardTAName;
    private List<CertFileInfo> mCertFileNames;
    private String mRootDir;
    private String mSKMMServiceName;
    private String mcardNetworkrName;

    /*
     * Enabled aggressive block sorting
     */
    static {
        DEBUG = TAController.DEBUG;
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
        String string = bQC ? "skm" : "ffffffff00000000000000000000000d";
        SKMM_TA_ID = string;
        mIsSupported = false;
        mIsInitialized = false;
        gDeviceRootKeyServiceManager = null;
        mErrorFlag = 0;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Tlv createSPaySpecificTlv(DeviceRootKeyServiceManager deviceRootKeyServiceManager, int n2) {
        Tlv tlv = new Tlv();
        int n3 = n2 == 1 ? 112 : 192;
        try {
            tlv.setTlv(5, new DERBitString(n3).getEncoded());
            tlv.setTlv(4, this.getSubjectTlv(this.mSKMMServiceName, deviceRootKeyServiceManager.getDeviceRootKeyUID(1)));
            return tlv;
        }
        catch (Exception exception) {
            Log.e(TAG, "Error constructing TLV for createServiceKeySession api : " + exception.toString());
            exception.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean createServiceKey(String string, int n2) {
        DeviceRootKeyServiceManager deviceRootKeyServiceManager;
        byte[] arrby;
        try {
            deviceRootKeyServiceManager = SpayDRKManager.getDRKService(this.mCardTAController.getContext());
            if (!SpayDRKManager.isSupported(this.mCardTAController.getContext())) {
                Log.e(TAG, "Error: isDRKExist failed");
                return false;
            }
            if (this.bUseCNCC) {
                CNCCTAController.getInstance().doManagedLoad();
            }
            Log.d(TAG, "createServiceKey " + string);
            Tlv tlv = this.createSPaySpecificTlv(deviceRootKeyServiceManager, n2);
            byte[] arrby2 = deviceRootKeyServiceManager.createServiceKeySession(this.mSKMMServiceName, 1, tlv);
            if (arrby2 == null) {
                Log.e(TAG, "Error: createServiceKeySession failed");
                return false;
            }
            Log.i(TAG, "createServiceKey: done");
            arrby = this.rewrapCertForActualPayService(arrby2);
            if (arrby == null) {
                Log.e(TAG, "Error: rewrapCertForActualPayService failed");
                return false;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        Log.i(TAG, "rewrapCertForActualPayService: done");
        if (!Utils.writeFile(arrby, string)) {
            Log.e(TAG, "Error: Write File failed");
            return false;
        }
        if (deviceRootKeyServiceManager.releaseServiceKeySession() != 0) {
            Log.e(TAG, "Error: releaseServiceKeySession failed");
        }
        return true;
    }

    private static DeviceRootKeyServiceManager getDRKService(Context context) {
        Class<SpayDRKManager> class_ = SpayDRKManager.class;
        synchronized (SpayDRKManager.class) {
            if (gDeviceRootKeyServiceManager == null) {
                gDeviceRootKeyServiceManager = new DeviceRootKeyServiceManager(context);
            }
            DeviceRootKeyServiceManager deviceRootKeyServiceManager = gDeviceRootKeyServiceManager;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return deviceRootKeyServiceManager;
        }
    }

    public static String getDeviceRootKeyUID(Context context) {
        if (!SpayDRKManager.isSupported(context)) {
            Log.e(TAG, "Error: isDRKExist failed");
            return null;
        }
        return SpayDRKManager.getDRKService(context).getDeviceRootKeyUID(1);
    }

    private static String getErrorStatus() {
        if (mErrorFlag == 0) {
            return "E0000";
        }
        return "E" + Integer.toBinaryString((int)mErrorFlag);
    }

    private DERSet getSubectDerSet(String string, String string2) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(new DERObjectIdentifier(string));
        aSN1EncodableVector.add(new DERUTF8String(string2));
        return new DERSet(new DERSequence(aSN1EncodableVector));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private byte[] getSubjectTlv(String string, String string2) {
        try {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            aSN1EncodableVector.add(this.getSubectDerSet("2.5.4.3", "Samsung Corporation"));
            aSN1EncodableVector.add(this.getSubectDerSet("2.5.4.6", "KR"));
            aSN1EncodableVector.add(this.getSubectDerSet("2.5.4.7", "Suwon city"));
            aSN1EncodableVector.add(this.getSubectDerSet("2.5.4.11", "Samsung Mobile"));
            aSN1EncodableVector.add(this.getSubectDerSet("0.9.2342.19200300.100.1.25", "samsung.com"));
            String string3 = "Unknown UID";
            if (string2 != null) {
                string3 = string2;
            }
            if (string != null) {
                string3 = string3 + ":" + string.toUpperCase();
            }
            aSN1EncodableVector.add(this.getSubectDerSet("2.5.4.45", string3));
            return new DERSequence(aSN1EncodableVector).getEncoded();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        }
    }

    private static final boolean isAndroidM() {
        if (Build.VERSION.SDK_INT > 22) {
            Log.d(TAG, "You are using Android Version " + Build.VERSION.SDK_INT);
            return true;
        }
        return false;
    }

    private static final boolean isDRKServiceAvail() {
        try {
            Class.forName((String)"com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager");
            Log.d(TAG, "DeviceRootKeyServiceManager is available");
            return true;
        }
        catch (ClassNotFoundException classNotFoundException) {
            Log.d(TAG, "DeviceRootKeyServiceManager is not available");
            return false;
        }
    }

    public static boolean isSupported(Context context) {
        if (mIsInitialized) {
            Log.d(TAG, "mIsSupported = " + mIsSupported);
            if (!mIsSupported) {
                Log.e(TAG, "mErrorFlag = " + SpayDRKManager.getErrorStatus());
            }
            return mIsSupported;
        }
        if (!SpayDRKManager.isAndroidM()) {
            Log.e(TAG, "DRK Service is not available");
            mIsSupported = false;
            mIsInitialized = true;
            SpayDRKManager.setErrorFlag(ErrorFlag.E_UNSUPPORTED_OS);
            return false;
        }
        if (!SpayDRKManager.isDRKServiceAvail()) {
            Log.e(TAG, "DeviceRootKeyServiceManager is not available");
            mIsSupported = false;
            mIsInitialized = true;
            SpayDRKManager.setErrorFlag(ErrorFlag.E_NO_DRKSVC);
            return false;
        }
        DeviceRootKeyServiceManager deviceRootKeyServiceManager = SpayDRKManager.getDRKService(context);
        if (!deviceRootKeyServiceManager.isAliveDeviceRootKeyService()) {
            Log.e(TAG, "Error: isAliveDeviceRootKeyService failed");
            mIsSupported = false;
            mIsInitialized = true;
            SpayDRKManager.setErrorFlag(ErrorFlag.E_INACTIVE_DRKSVC);
            return false;
        }
        if (!deviceRootKeyServiceManager.isExistDeviceRootKey(1)) {
            Log.e(TAG, "Error: isDRKExist failed");
            mIsSupported = false;
            mIsInitialized = true;
            SpayDRKManager.setErrorFlag(ErrorFlag.E_NO_DRK);
            return false;
        }
        mIsSupported = true;
        mIsInitialized = true;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private byte[] rewrapCertForActualPayService(byte[] arrby) {
        byte[] arrby2;
        block4 : {
            block3 : {
                try {
                    if (!this.bUseCNCC) break block3;
                    arrby2 = CNCCTAController.getInstance().processData(null, arrby, CNCCTAController.DataType.DATATYPE_CERTIFICATE, CNCCTAController.ProcessingOption.OPTION_UNWRAP_FROM_SRCTA_AND_WRAP_FOR_DESTTA, SKMM_TA_ID, this.mCardTAName);
                    if (arrby2 == null) {
                        Log.e(TAG, "Error : rewrappedCert = mCNCCTAController.processData == null");
                        return null;
                    }
                    break block4;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    return null;
                }
            }
            arrby2 = arrby;
        }
        if (!TAController.isChipSetQC()) return arrby2;
        byte[] arrby3 = this.mCardTAController.decapsulateAndWrap(arrby2);
        return arrby3;
    }

    private static void setErrorFlag(ErrorFlag errorFlag) {
        mErrorFlag |= errorFlag.flag;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean generateDeviceCertificates() {
        Class<SpayDRKManager> class_ = SpayDRKManager.class;
        synchronized (SpayDRKManager.class) {
            Log.d(TAG, "generateDeviceCertificates");
            int n2 = 0;
            do {
                if (n2 >= this.mCertFileNames.size()) {
                    // ** MonitorExit[var4_1] (shouldn't be in output)
                    return true;
                }
                String string = this.mRootDir + "/" + ((CertFileInfo)this.mCertFileNames.get((int)n2)).mCertFile;
                if (new File(string).exists()) {
                    Log.d(TAG, "Certificate File " + string + " exists. No need to generate");
                } else {
                    Log.d(TAG, "Certificate File " + string + " do not exist. Lets create it");
                    if (!this.createServiceKey(string, ((CertFileInfo)this.mCertFileNames.get((int)n2)).mUsageType)) {
                        Log.e(TAG, "Error: createServiceKey failed");
                        // ** MonitorExit[var4_1] (shouldn't be in output)
                        return false;
                    }
                }
                ++n2;
            } while (true);
        }
    }

    public String getCertFilePath(String string) {
        return this.mRootDir + "/" + string;
    }

    public CertInfo getCertInfo() {
        CertInfo certInfo;
        int n2;
        block6 : {
            if (this.generateDeviceCertificates()) break block6;
            Log.e(TAG, "generateDeviceCertificates() failed");
            return null;
        }
        try {
            if (DEBUG) {
                Log.d(TAG, "TAController::Certificate Files exist. Lets fetch them");
            }
            Log.d(TAG, "TAController::Certificate Files exist. Lets fetch them");
            certInfo = new CertInfo();
            n2 = 0;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        do {
            if (n2 >= this.mCertFileNames.size()) break;
            byte[] arrby = Utils.readFile(this.mRootDir + "/" + ((CertFileInfo)this.mCertFileNames.get((int)n2)).mCertFile);
            certInfo.mCerts.put((Object)((CertFileInfo)this.mCertFileNames.get((int)n2)).mCertFile, (Object)arrby);
            Log.d(TAG, "put certs " + ((CertFileInfo)this.mCertFileNames.get((int)n2)).mCertFile);
            ++n2;
        } while (true);
        return certInfo;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void init(TAController tAController, String string, String string2, String string3, List<CertFileInfo> list) {
        boolean bl = string == null;
        this.bUseCNCC = bl;
        if (string == null) {
            string = "CNCC_PAY";
        }
        this.mSKMMServiceName = string;
        this.mcardNetworkrName = string2;
        this.mCardTAName = string3;
        this.mCertFileNames = list;
        this.mCardTAController = tAController;
        this.mRootDir = (Object)tAController.getContext().getFilesDir() + "/" + DEVCERTS_FOLDER + "/" + this.mcardNetworkrName;
    }

    public static class CertFileInfo {
        public static final int CERT_USAGE_ENCRYPTION = 1;
        public static final int CERT_USAGE_SIGNING = 2;
        String mCertFile;
        int mUsageType;

        public CertFileInfo(String string, int n2) {
            this.mCertFile = string;
            this.mUsageType = n2;
        }
    }

    public static final class ErrorFlag
    extends Enum<ErrorFlag> {
        private static final /* synthetic */ ErrorFlag[] $VALUES;
        public static final /* enum */ ErrorFlag E_INACTIVE_DRKSVC;
        public static final /* enum */ ErrorFlag E_NO_DRK;
        public static final /* enum */ ErrorFlag E_NO_DRKSVC;
        public static final /* enum */ ErrorFlag E_UNSUPPORTED_OS;
        private int flag = 1 << this.ordinal();

        static {
            E_UNSUPPORTED_OS = new ErrorFlag();
            E_NO_DRKSVC = new ErrorFlag();
            E_INACTIVE_DRKSVC = new ErrorFlag();
            E_NO_DRK = new ErrorFlag();
            ErrorFlag[] arrerrorFlag = new ErrorFlag[]{E_UNSUPPORTED_OS, E_NO_DRKSVC, E_INACTIVE_DRKSVC, E_NO_DRK};
            $VALUES = arrerrorFlag;
        }

        public static ErrorFlag valueOf(String string) {
            return (ErrorFlag)Enum.valueOf(ErrorFlag.class, (String)string);
        }

        public static ErrorFlag[] values() {
            return (ErrorFlag[])$VALUES.clone();
        }
    }

}

