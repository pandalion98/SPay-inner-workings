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
package com.samsung.android.spayfw.payprovider.mastercard.tzsvc;

import android.spay.CertInfo;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.cncc.SpayDRKManager;
import com.samsung.android.spaytzsvc.api.TAController;
import java.util.ArrayList;
import java.util.List;

public class McDeviceCert {
    private static final String MC_PAY_SERVICE_NAME = "MC_PAY";
    private static final String TAG = "McDeviceCert";
    private final String MC_PAY_CASD_CERTIFICATE_PATH = "/efs/mc/mc.dat";
    private final String MC_PAY_CASD_NEW_CERTIFICATE_PATH = "/efs/mc/rst.dat";
    private final String MC_PAY_CERT_PATH_X509 = "/efs/prov_data/mc_pay/mc_pay_sign.dat";
    private List<SpayDRKManager.CertFileInfo> mCertPaths = new ArrayList();
    private SpayDRKManager mCertloader = null;
    private CertInfo mDevicePrivateCerts = null;
    private boolean mIsDRKServiceAvailable = false;
    private TAController mTAController;

    public McDeviceCert(TAController tAController) {
        this.mCertPaths.add((Object)new SpayDRKManager.CertFileInfo("mc_pay_sign.dat", 2));
        this.mTAController = tAController;
        this.mIsDRKServiceAvailable = SpayDRKManager.isSupported(this.mTAController.getContext());
        this.mCertloader = new SpayDRKManager();
        this.mCertloader.init(this.mTAController, MC_PAY_SERVICE_NAME, MC_PAY_SERVICE_NAME, McTAController.getTaid(), this.mCertPaths);
    }

    private McTAController.McCertInfo composeMcCertInfo(int n2, byte[] arrby) {
        McTAController.McCertInfo mcCertInfo = new McTAController.McCertInfo();
        mcCertInfo.type = n2;
        mcCertInfo.blob = arrby;
        return mcCertInfo;
    }

    private boolean isMCCertValid() {
        return this.mDevicePrivateCerts != null && this.mDevicePrivateCerts.mCerts != null && !this.mDevicePrivateCerts.mCerts.isEmpty();
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean load() {
        this.mDevicePrivateCerts = !this.mIsDRKServiceAvailable ? this.mTAController.getCertInfo() : this.mCertloader.getCertInfo();
        if (!this.isMCCertValid()) {
            Log.e(TAG, "loadAllCerts: Error: get Wrapped Certificate Data from file system failed");
            this.mDevicePrivateCerts = null;
            return false;
        }
        return true;
    }

    public McTAController.McCertInfo getCASDCertEx() {
        if (!this.load()) {
            Log.e(TAG, "getCASDCertEx : McCert load failed");
            return null;
        }
        if (!this.isMCCertValid()) {
            Log.e(TAG, "There is no valid MC cert");
            return null;
        }
        byte[] arrby = (byte[])this.mDevicePrivateCerts.mCerts.get((Object)"mc_pay_sign.dat");
        if (arrby != null) {
            Log.d(TAG, "McTAController.MC_PAY_CERT_SIGN_FILENAME is loaded");
            return this.composeMcCertInfo(1, arrby);
        }
        byte[] arrby2 = (byte[])this.mDevicePrivateCerts.mCerts.get((Object)"/efs/prov_data/mc_pay/mc_pay_sign.dat");
        if (arrby2 != null) {
            Log.d(TAG, "MC_PAY_CERT_PATH_X509 is loaded");
            return this.composeMcCertInfo(1, arrby2);
        }
        byte[] arrby3 = (byte[])this.mDevicePrivateCerts.mCerts.get((Object)"/efs/mc/rst.dat");
        if (arrby3 != null) {
            Log.d(TAG, "MC_PAY_CASD_NEW_CERTIFICATE_PATH is loaded");
            return this.composeMcCertInfo(2, arrby3);
        }
        Log.e(TAG, "There is no valid MC cert - end");
        return null;
    }

    public CertInfo getDeviceCasdCert() {
        if (!this.mIsDRKServiceAvailable) {
            this.mDevicePrivateCerts = this.mTAController.getCertInfo();
            return this.mDevicePrivateCerts;
        }
        return null;
    }

    public CertInfo getDeviceMcSignCert() {
        if (!this.load()) {
            Log.d(TAG, "getDeviceMcSignCert : McCert load failed");
            return null;
        }
        return this.mDevicePrivateCerts;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public byte[] loadOldCasdCerts() {
        if (this.mIsDRKServiceAvailable) {
            Log.i(TAG, "Device image is M-version, No old CASD");
            return null;
        }
        if (!this.load()) {
            Log.e(TAG, "loadOldCasdCerts : McCert load failed");
            return null;
        }
        if (this.mDevicePrivateCerts == null || this.mDevicePrivateCerts.mCerts.isEmpty()) {
            Log.e(TAG, "Error : getCertInfo is null ");
            return null;
        }
        byte[] arrby = (byte[])this.mDevicePrivateCerts.mCerts.get((Object)"/efs/mc/mc.dat");
        if (arrby != null) return arrby;
        Log.e(TAG, "Error : CASD certs is null");
        return null;
    }
}

