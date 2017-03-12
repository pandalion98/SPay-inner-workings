package com.samsung.android.spayfw.payprovider.mastercard.tzsvc;

import android.spay.CertInfo;
import com.samsung.android.spayfw.cncc.SpayDRKManager;
import com.samsung.android.spayfw.cncc.SpayDRKManager.CertFileInfo;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController.McCertInfo;
import com.samsung.android.spaytzsvc.api.TAController;
import java.util.ArrayList;
import java.util.List;

public class McDeviceCert {
    private static final String MC_PAY_SERVICE_NAME = "MC_PAY";
    private static final String TAG = "McDeviceCert";
    private final String MC_PAY_CASD_CERTIFICATE_PATH;
    private final String MC_PAY_CASD_NEW_CERTIFICATE_PATH;
    private final String MC_PAY_CERT_PATH_X509;
    private List<CertFileInfo> mCertPaths;
    private SpayDRKManager mCertloader;
    private CertInfo mDevicePrivateCerts;
    private boolean mIsDRKServiceAvailable;
    private TAController mTAController;

    public McDeviceCert(TAController tAController) {
        this.MC_PAY_CASD_CERTIFICATE_PATH = McTAController.MC_PAY_CASD_CERTIFICATE_PATH;
        this.MC_PAY_CASD_NEW_CERTIFICATE_PATH = "/efs/mc/rst.dat";
        this.MC_PAY_CERT_PATH_X509 = "/efs/prov_data/mc_pay/mc_pay_sign.dat";
        this.mDevicePrivateCerts = null;
        this.mCertPaths = null;
        this.mIsDRKServiceAvailable = false;
        this.mCertloader = null;
        this.mCertPaths = new ArrayList();
        this.mCertPaths.add(new CertFileInfo(McTAController.MC_PAY_CERT_SIGN_FILENAME, 2));
        this.mTAController = tAController;
        this.mIsDRKServiceAvailable = SpayDRKManager.isSupported(this.mTAController.getContext());
        this.mCertloader = new SpayDRKManager();
        this.mCertloader.init(this.mTAController, MC_PAY_SERVICE_NAME, MC_PAY_SERVICE_NAME, McTAController.getTaid(), this.mCertPaths);
    }

    private boolean load() {
        if (this.mIsDRKServiceAvailable) {
            this.mDevicePrivateCerts = this.mCertloader.getCertInfo();
        } else {
            this.mDevicePrivateCerts = this.mTAController.getCertInfo();
        }
        if (isMCCertValid()) {
            return true;
        }
        Log.m286e(TAG, "loadAllCerts: Error: get Wrapped Certificate Data from file system failed");
        this.mDevicePrivateCerts = null;
        return false;
    }

    public CertInfo getDeviceMcSignCert() {
        if (load()) {
            return this.mDevicePrivateCerts;
        }
        Log.m285d(TAG, "getDeviceMcSignCert : McCert load failed");
        return null;
    }

    public CertInfo getDeviceCasdCert() {
        if (this.mIsDRKServiceAvailable) {
            return null;
        }
        this.mDevicePrivateCerts = this.mTAController.getCertInfo();
        return this.mDevicePrivateCerts;
    }

    public McCertInfo getCASDCertEx() {
        if (!load()) {
            Log.m286e(TAG, "getCASDCertEx : McCert load failed");
            return null;
        } else if (isMCCertValid()) {
            byte[] bArr = (byte[]) this.mDevicePrivateCerts.mCerts.get(McTAController.MC_PAY_CERT_SIGN_FILENAME);
            if (bArr != null) {
                Log.m285d(TAG, "McTAController.MC_PAY_CERT_SIGN_FILENAME is loaded");
                return composeMcCertInfo(1, bArr);
            }
            bArr = (byte[]) this.mDevicePrivateCerts.mCerts.get("/efs/prov_data/mc_pay/mc_pay_sign.dat");
            if (bArr != null) {
                Log.m285d(TAG, "MC_PAY_CERT_PATH_X509 is loaded");
                return composeMcCertInfo(1, bArr);
            }
            bArr = (byte[]) this.mDevicePrivateCerts.mCerts.get("/efs/mc/rst.dat");
            if (bArr != null) {
                Log.m285d(TAG, "MC_PAY_CASD_NEW_CERTIFICATE_PATH is loaded");
                return composeMcCertInfo(2, bArr);
            }
            Log.m286e(TAG, "There is no valid MC cert - end");
            return null;
        } else {
            Log.m286e(TAG, "There is no valid MC cert");
            return null;
        }
    }

    public byte[] loadOldCasdCerts() {
        if (this.mIsDRKServiceAvailable) {
            Log.m287i(TAG, "Device image is M-version, No old CASD");
            return null;
        } else if (!load()) {
            Log.m286e(TAG, "loadOldCasdCerts : McCert load failed");
            return null;
        } else if (this.mDevicePrivateCerts == null || this.mDevicePrivateCerts.mCerts.isEmpty()) {
            Log.m286e(TAG, "Error : getCertInfo is null ");
            return null;
        } else {
            byte[] bArr = (byte[]) this.mDevicePrivateCerts.mCerts.get(McTAController.MC_PAY_CASD_CERTIFICATE_PATH);
            if (bArr != null) {
                return bArr;
            }
            Log.m286e(TAG, "Error : CASD certs is null");
            return null;
        }
    }

    private boolean isMCCertValid() {
        if (this.mDevicePrivateCerts == null || this.mDevicePrivateCerts.mCerts == null || this.mDevicePrivateCerts.mCerts.isEmpty()) {
            return false;
        }
        return true;
    }

    private McCertInfo composeMcCertInfo(int i, byte[] bArr) {
        McCertInfo mcCertInfo = new McCertInfo();
        mcCertInfo.type = i;
        mcCertInfo.blob = bArr;
        return mcCertInfo;
    }
}
