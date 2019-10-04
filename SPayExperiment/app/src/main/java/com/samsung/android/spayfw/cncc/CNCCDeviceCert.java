/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.spay.CertInfo
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.cncc;

import android.content.Context;
import android.spay.CertInfo;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.cncc.CNCCTAController;
import com.samsung.android.spayfw.cncc.SpayDRKManager;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CNCCDeviceCert {
    public static final String CNCC_CERTSIGN_FILENAME = "spaydcmcert.dat";
    public static final String CNCC_SERVICE_NAME = "CNCC_PAY";
    private static final String TAG = "CNCCDeviceCert";
    SpayDRKManager certloader = new SpayDRKManager();
    private CertInfo mDevicePrivateCerts = null;
    TAController mTAController;

    public CNCCDeviceCert(TAController tAController) {
        this.mTAController = tAController;
    }

    public byte[] getSPayDCMDevicePrivateEncryptCert() {
        return (byte[])this.mDevicePrivateCerts.mCerts.get((Object)CNCC_CERTSIGN_FILENAME);
    }

    public byte[] getSPayDCMDevicePrivateSignCert() {
        return (byte[])this.mDevicePrivateCerts.mCerts.get((Object)CNCC_CERTSIGN_FILENAME);
    }

    public boolean load() {
        if (!SpayDRKManager.isSupported(this.mTAController.getContext())) {
            c.e(TAG, "SpayDRKManager.isDRKExist failed");
            return false;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)new SpayDRKManager.CertFileInfo(CNCC_CERTSIGN_FILENAME, 2));
        this.certloader.init(this.mTAController, CNCC_SERVICE_NAME, CNCC_SERVICE_NAME, CNCCTAController.getInstance().getTAInfo().getTAId(), (List<SpayDRKManager.CertFileInfo>)arrayList);
        this.mDevicePrivateCerts = this.certloader.getCertInfo();
        if (this.mDevicePrivateCerts == null || this.mDevicePrivateCerts.mCerts.isEmpty() || this.getSPayDCMDevicePrivateSignCert() == null) {
            c.e(TAG, "load: Error: get Wrapped Certificate Data from file system failed");
            this.mDevicePrivateCerts = null;
            return false;
        }
        return true;
    }
}

