package com.samsung.android.spayfw.cncc;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.spay.CertInfo;
import com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager;
import com.samsung.android.service.DeviceRootKeyService.Tlv;
import com.samsung.android.spayfw.cncc.CNCCTAController.DataType;
import com.samsung.android.spayfw.cncc.CNCCTAController.ProcessingOption;
import com.samsung.android.spayfw.p002b.Log;
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
import org.bouncycastle.crypto.tls.CipherSuite;

public class SpayDRKManager {
    private static final boolean DEBUG;
    private static final String DEVCERTS_FOLDER = "devicecerts";
    public static final String SKMM_TA_ID;
    private static final String TAG = "SpayDRKManager";
    private static final boolean bQC;
    private static DeviceRootKeyServiceManager gDeviceRootKeyServiceManager;
    private static int mErrorFlag;
    private static boolean mIsInitialized;
    private static boolean mIsSupported;
    private boolean bUseCNCC;
    private TAController mCardTAController;
    private String mCardTAName;
    private List<CertFileInfo> mCertFileNames;
    private String mRootDir;
    private String mSKMMServiceName;
    private String mcardNetworkrName;

    public static class CertFileInfo {
        public static final int CERT_USAGE_ENCRYPTION = 1;
        public static final int CERT_USAGE_SIGNING = 2;
        String mCertFile;
        int mUsageType;

        public CertFileInfo(String str, int i) {
            this.mCertFile = str;
            this.mUsageType = i;
        }
    }

    public enum ErrorFlag {
        E_UNSUPPORTED_OS,
        E_NO_DRKSVC,
        E_INACTIVE_DRKSVC,
        E_NO_DRK;
        
        private int flag;
    }

    public SpayDRKManager() {
        this.bUseCNCC = true;
    }

    static {
        String str;
        DEBUG = TAController.DEBUG;
        bQC = Build.BOARD.matches("(?i)msm[a-z0-9]*");
        if (bQC) {
            str = "skm";
        } else {
            str = "ffffffff00000000000000000000000d";
        }
        SKMM_TA_ID = str;
        mIsSupported = DEBUG;
        mIsInitialized = DEBUG;
        gDeviceRootKeyServiceManager = null;
        mErrorFlag = 0;
    }

    private static synchronized DeviceRootKeyServiceManager getDRKService(Context context) {
        DeviceRootKeyServiceManager deviceRootKeyServiceManager;
        synchronized (SpayDRKManager.class) {
            if (gDeviceRootKeyServiceManager == null) {
                gDeviceRootKeyServiceManager = new DeviceRootKeyServiceManager(context);
            }
            deviceRootKeyServiceManager = gDeviceRootKeyServiceManager;
        }
        return deviceRootKeyServiceManager;
    }

    public void init(TAController tAController, String str, String str2, String str3, List<CertFileInfo> list) {
        this.bUseCNCC = str == null ? true : DEBUG;
        if (str == null) {
            str = CNCCDeviceCert.CNCC_SERVICE_NAME;
        }
        this.mSKMMServiceName = str;
        this.mcardNetworkrName = str2;
        this.mCardTAName = str3;
        this.mCertFileNames = list;
        this.mCardTAController = tAController;
        this.mRootDir = tAController.getContext().getFilesDir() + "/" + DEVCERTS_FOLDER + "/" + this.mcardNetworkrName;
    }

    public static String getDeviceRootKeyUID(Context context) {
        if (isSupported(context)) {
            return getDRKService(context).getDeviceRootKeyUID(1);
        }
        Log.m286e(TAG, "Error: isDRKExist failed");
        return null;
    }

    public String getCertFilePath(String str) {
        return this.mRootDir + "/" + str;
    }

    public CertInfo getCertInfo() {
        try {
            if (generateDeviceCertificates()) {
                if (DEBUG) {
                    Log.m285d(TAG, "TAController::Certificate Files exist. Lets fetch them");
                }
                Log.m285d(TAG, "TAController::Certificate Files exist. Lets fetch them");
                CertInfo certInfo = new CertInfo();
                for (int i = 0; i < this.mCertFileNames.size(); i++) {
                    certInfo.mCerts.put(((CertFileInfo) this.mCertFileNames.get(i)).mCertFile, Utils.readFile(this.mRootDir + "/" + ((CertFileInfo) this.mCertFileNames.get(i)).mCertFile));
                    Log.m285d(TAG, "put certs " + ((CertFileInfo) this.mCertFileNames.get(i)).mCertFile);
                }
                return certInfo;
            }
            Log.m286e(TAG, "generateDeviceCertificates() failed");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean generateDeviceCertificates() {
        synchronized (SpayDRKManager.class) {
            Log.m285d(TAG, "generateDeviceCertificates");
            for (int i = 0; i < this.mCertFileNames.size(); i++) {
                String str = this.mRootDir + "/" + ((CertFileInfo) this.mCertFileNames.get(i)).mCertFile;
                if (new File(str).exists()) {
                    Log.m285d(TAG, "Certificate File " + str + " exists. No need to generate");
                } else {
                    Log.m285d(TAG, "Certificate File " + str + " do not exist. Lets create it");
                    if (!createServiceKey(str, ((CertFileInfo) this.mCertFileNames.get(i)).mUsageType)) {
                        Log.m286e(TAG, "Error: createServiceKey failed");
                        return DEBUG;
                    }
                }
            }
            return true;
        }
    }

    private boolean createServiceKey(String str, int i) {
        try {
            DeviceRootKeyServiceManager dRKService = getDRKService(this.mCardTAController.getContext());
            if (isSupported(this.mCardTAController.getContext())) {
                if (this.bUseCNCC) {
                    CNCCTAController.getInstance().doManagedLoad();
                }
                Log.m285d(TAG, "createServiceKey " + str);
                byte[] createServiceKeySession = dRKService.createServiceKeySession(this.mSKMMServiceName, 1, createSPaySpecificTlv(dRKService, i));
                if (createServiceKeySession == null) {
                    Log.m286e(TAG, "Error: createServiceKeySession failed");
                    return DEBUG;
                }
                Log.m287i(TAG, "createServiceKey: done");
                createServiceKeySession = rewrapCertForActualPayService(createServiceKeySession);
                if (createServiceKeySession == null) {
                    Log.m286e(TAG, "Error: rewrapCertForActualPayService failed");
                    return DEBUG;
                }
                Log.m287i(TAG, "rewrapCertForActualPayService: done");
                if (Utils.writeFile(createServiceKeySession, str)) {
                    if (dRKService.releaseServiceKeySession() != 0) {
                        Log.m286e(TAG, "Error: releaseServiceKeySession failed");
                    }
                    return true;
                }
                Log.m286e(TAG, "Error: Write File failed");
                return DEBUG;
            }
            Log.m286e(TAG, "Error: isDRKExist failed");
            return DEBUG;
        } catch (Exception e) {
            e.printStackTrace();
            return DEBUG;
        }
    }

    private static final boolean isAndroidM() {
        if (VERSION.SDK_INT <= 22) {
            return DEBUG;
        }
        Log.m285d(TAG, "You are using Android Version " + VERSION.SDK_INT);
        return true;
    }

    private static final boolean isDRKServiceAvail() {
        try {
            Class.forName("com.samsung.android.service.DeviceRootKeyService.DeviceRootKeyServiceManager");
            Log.m285d(TAG, "DeviceRootKeyServiceManager is available");
            return true;
        } catch (ClassNotFoundException e) {
            Log.m285d(TAG, "DeviceRootKeyServiceManager is not available");
            return DEBUG;
        }
    }

    public static boolean isSupported(Context context) {
        if (mIsInitialized) {
            Log.m285d(TAG, "mIsSupported = " + mIsSupported);
            if (!mIsSupported) {
                Log.m286e(TAG, "mErrorFlag = " + getErrorStatus());
            }
            return mIsSupported;
        } else if (!isAndroidM()) {
            Log.m286e(TAG, "DRK Service is not available");
            mIsSupported = DEBUG;
            mIsInitialized = true;
            setErrorFlag(ErrorFlag.E_UNSUPPORTED_OS);
            return DEBUG;
        } else if (isDRKServiceAvail()) {
            DeviceRootKeyServiceManager dRKService = getDRKService(context);
            if (!dRKService.isAliveDeviceRootKeyService()) {
                Log.m286e(TAG, "Error: isAliveDeviceRootKeyService failed");
                mIsSupported = DEBUG;
                mIsInitialized = true;
                setErrorFlag(ErrorFlag.E_INACTIVE_DRKSVC);
                return DEBUG;
            } else if (dRKService.isExistDeviceRootKey(1)) {
                mIsSupported = true;
                mIsInitialized = true;
                return true;
            } else {
                Log.m286e(TAG, "Error: isDRKExist failed");
                mIsSupported = DEBUG;
                mIsInitialized = true;
                setErrorFlag(ErrorFlag.E_NO_DRK);
                return DEBUG;
            }
        } else {
            Log.m286e(TAG, "DeviceRootKeyServiceManager is not available");
            mIsSupported = DEBUG;
            mIsInitialized = true;
            setErrorFlag(ErrorFlag.E_NO_DRKSVC);
            return DEBUG;
        }
    }

    private byte[] rewrapCertForActualPayService(byte[] bArr) {
        byte[] processData;
        try {
            if (this.bUseCNCC) {
                processData = CNCCTAController.getInstance().processData(null, bArr, DataType.DATATYPE_CERTIFICATE, ProcessingOption.OPTION_UNWRAP_FROM_SRCTA_AND_WRAP_FOR_DESTTA, SKMM_TA_ID, this.mCardTAName);
                if (processData == null) {
                    Log.m286e(TAG, "Error : rewrappedCert = mCNCCTAController.processData == null");
                    return null;
                }
            }
            processData = bArr;
            if (TAController.isChipSetQC()) {
                processData = this.mCardTAController.decapsulateAndWrap(processData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            processData = null;
        }
        return processData;
    }

    private DERSet getSubectDerSet(String str, String str2) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(new DERObjectIdentifier(str));
        aSN1EncodableVector.add(new DERUTF8String(str2));
        return new DERSet(new DERSequence(aSN1EncodableVector));
    }

    private byte[] getSubjectTlv(String str, String str2) {
        byte[] bArr = null;
        try {
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            aSN1EncodableVector.add(getSubectDerSet("2.5.4.3", "Samsung Corporation"));
            aSN1EncodableVector.add(getSubectDerSet("2.5.4.6", "KR"));
            aSN1EncodableVector.add(getSubectDerSet("2.5.4.7", "Suwon city"));
            aSN1EncodableVector.add(getSubectDerSet("2.5.4.11", "Samsung Mobile"));
            aSN1EncodableVector.add(getSubectDerSet("0.9.2342.19200300.100.1.25", "samsung.com"));
            String str3 = "Unknown UID";
            if (str2 != null) {
                str3 = str2;
            }
            if (str != null) {
                str3 = str3 + ":" + str.toUpperCase();
            }
            aSN1EncodableVector.add(getSubectDerSet("2.5.4.45", str3));
            bArr = new DERSequence(aSN1EncodableVector).getEncoded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bArr;
    }

    private Tlv createSPaySpecificTlv(DeviceRootKeyServiceManager deviceRootKeyServiceManager, int i) {
        int i2;
        Tlv tlv = new Tlv();
        if (i == 1) {
            i2 = 112;
        } else {
            i2 = CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256;
        }
        try {
            tlv.setTlv(5, new DERBitString(i2).getEncoded());
            tlv.setTlv(4, getSubjectTlv(this.mSKMMServiceName, deviceRootKeyServiceManager.getDeviceRootKeyUID(1)));
            return tlv;
        } catch (Exception e) {
            Exception exception = e;
            Log.m286e(TAG, "Error constructing TLV for createServiceKeySession api : " + exception.toString());
            exception.printStackTrace();
            return null;
        }
    }

    private static void setErrorFlag(ErrorFlag errorFlag) {
        mErrorFlag |= errorFlag.flag;
    }

    private static String getErrorStatus() {
        if (mErrorFlag == 0) {
            return "E0000";
        }
        return "E" + Integer.toBinaryString(mErrorFlag);
    }
}
