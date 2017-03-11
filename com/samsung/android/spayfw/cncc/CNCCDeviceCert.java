package com.samsung.android.spayfw.cncc;

import android.spay.CertInfo;
import com.samsung.android.spayfw.cncc.SpayDRKManager.CertFileInfo;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.TAController;
import java.util.ArrayList;
import java.util.List;

public class CNCCDeviceCert {
    public static final String CNCC_CERTSIGN_FILENAME = "spaydcmcert.dat";
    public static final String CNCC_SERVICE_NAME = "CNCC_PAY";
    private static final String TAG = "CNCCDeviceCert";
    SpayDRKManager certloader;
    private CertInfo mDevicePrivateCerts;
    TAController mTAController;

    public CNCCDeviceCert(TAController tAController) {
        this.mDevicePrivateCerts = null;
        this.certloader = new SpayDRKManager();
        this.mTAController = tAController;
    }

    public boolean load() {
        if (SpayDRKManager.isSupported(this.mTAController.getContext())) {
            List arrayList = new ArrayList();
            arrayList.add(new CertFileInfo(CNCC_CERTSIGN_FILENAME, 2));
            this.certloader.init(this.mTAController, CNCC_SERVICE_NAME, CNCC_SERVICE_NAME, CNCCTAController.getInstance().getTAInfo().getTAId(), arrayList);
            this.mDevicePrivateCerts = this.certloader.getCertInfo();
            if (this.mDevicePrivateCerts != null && !this.mDevicePrivateCerts.mCerts.isEmpty() && getSPayDCMDevicePrivateSignCert() != null) {
                return true;
            }
            Log.m286e(TAG, "load: Error: get Wrapped Certificate Data from file system failed");
            this.mDevicePrivateCerts = null;
            return false;
        }
        Log.m286e(TAG, "SpayDRKManager.isDRKExist failed");
        return false;
    }

    public byte[] getSPayDCMDevicePrivateSignCert() {
        return (byte[]) this.mDevicePrivateCerts.mCerts.get(CNCC_CERTSIGN_FILENAME);
    }

    public byte[] getSPayDCMDevicePrivateEncryptCert() {
        return (byte[]) this.mDevicePrivateCerts.mCerts.get(CNCC_CERTSIGN_FILENAME);
    }
}
