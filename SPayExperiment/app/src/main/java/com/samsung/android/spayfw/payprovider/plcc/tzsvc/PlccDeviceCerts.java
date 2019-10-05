/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.spay.CertInfo
 *  java.io.File
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

import android.spay.CertInfo;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.cncc.SpayDRKManager;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlccDeviceCerts {
    protected static final String EFS_DEVICE_CERT_ROOT_DIR = "/efs/prov_data/plcc_pay/";
    private static final String EFS_ENC_CERT_PATH = "/efs/prov_data/plcc_pay/plcc_pay_enc.dat";
    private static final String EFS_SIGN_CERT_PATH = "/efs/prov_data/plcc_pay/plcc_pay_sign.dat";
    private static final String PAY_ENCCERT_FILENAME = "plcc_pay_enc.dat";
    private static final String PAY_SIGNCERT_FILENAME = "plcc_pay_sign.dat";
    private static final String PLCC_PAY_SERVICE_NAME = "PLCC_PAY";
    private static final String TAG = "PlccDeviceCerts";
    SpayDRKManager certloader = new SpayDRKManager();
    private boolean isDRKServiceExist = false;
    private boolean isDRKServiceUsed = false;
    private CertInfo mDevicePrivateCerts = null;
    TAController mTAController;

    PlccDeviceCerts(TAController tAController) {
        Log.d(TAG, TAG);
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)PAY_SIGNCERT_FILENAME);
        arrayList.add((Object)PAY_ENCCERT_FILENAME);
        this.mTAController = tAController;
        this.isDRKServiceExist = SpayDRKManager.isSupported(this.mTAController.getContext());
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add((Object)new SpayDRKManager.CertFileInfo(PAY_SIGNCERT_FILENAME, 2));
        arrayList2.add((Object)new SpayDRKManager.CertFileInfo(PAY_ENCCERT_FILENAME, 1));
        this.certloader.init(this.mTAController, null, PLCC_PAY_SERVICE_NAME, PlccTAController.TA_INFO.getTAId(), (List<SpayDRKManager.CertFileInfo>)arrayList2);
    }

    private boolean alreadyMigrated() {
        File file = new File(this.certloader.getCertFilePath(PAY_SIGNCERT_FILENAME));
        File file2 = new File(this.certloader.getCertFilePath(PAY_ENCCERT_FILENAME));
        return file.exists() && file2.exists();
    }

    private void loadCertsFromDRKService() {
        Log.d(TAG, "loadCertsFromDRKService");
        this.mDevicePrivateCerts = this.certloader.getCertInfo();
        this.isDRKServiceUsed = true;
        Log.e(TAG, "Device Certicates loaded using DRK Service");
    }

    public byte[] getDevicePrivateEncryptionCert() {
        if (!this.isDRKServiceUsed) {
            return (byte[])this.mDevicePrivateCerts.mCerts.get((Object)EFS_ENC_CERT_PATH);
        }
        return (byte[])this.mDevicePrivateCerts.mCerts.get((Object)PAY_ENCCERT_FILENAME);
    }

    public byte[] getDevicePrivateSignCert() {
        if (!this.isDRKServiceUsed) {
            return (byte[])this.mDevicePrivateCerts.mCerts.get((Object)EFS_SIGN_CERT_PATH);
        }
        return (byte[])this.mDevicePrivateCerts.mCerts.get((Object)PAY_SIGNCERT_FILENAME);
    }

    public boolean isLoaded() {
        return this.mDevicePrivateCerts != null && !this.mDevicePrivateCerts.mCerts.isEmpty() && this.getDevicePrivateEncryptionCert() != null && this.getDevicePrivateEncryptionCert() != null;
    }

    public boolean load() {
        Log.d(TAG, "load Device Certificates Start");
        this.loadInternal();
        if (this.mDevicePrivateCerts != null) {
            Log.d(TAG, "load cert done size: " + this.mDevicePrivateCerts.mCerts.size());
        }
        if (!this.isLoaded()) {
            Log.e(TAG, "loadAllCerts: Error: get Wrapped Certificate Data from file system failed");
            this.mDevicePrivateCerts = null;
            return false;
        }
        Log.d(TAG, "load Device Certificates Success");
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void loadInternal() {
        block7 : {
            Iterator iterator;
            block8 : {
                block5 : {
                    block6 : {
                        if (!this.isDRKServiceExist) {
                            Log.d(TAG, "DRK Service Not Exist - Should be L Binary - Use Payment Service to load Certs");
                            this.mDevicePrivateCerts = this.mTAController.getCertInfo();
                            return;
                        }
                        Log.d(TAG, "DRK Service Exist");
                        if (TAController.isChipSetQC()) {
                            Log.d(TAG, "Device is Qualcomm - Directly Use DRK Service as PaymentService approach would anyway not work");
                            this.loadCertsFromDRKService();
                            return;
                        }
                        Log.d(TAG, "Device is not Qualcomm");
                        if (!this.mTAController.isDeviceCertificateMigratable()) break block5;
                        Log.d(TAG, "Device Certificates are migratable.");
                        if (this.alreadyMigrated()) break block6;
                        Log.d(TAG, "Device Certificates NOT already migrated.");
                        Log.d(TAG, "Get CertData from EFS and copy to local folder.");
                        ArrayList arrayList = new ArrayList();
                        arrayList.add((Object)EFS_SIGN_CERT_PATH);
                        arrayList.add((Object)EFS_ENC_CERT_PATH);
                        CertInfo certInfo = this.mTAController.checkCertInfo((List<String>)arrayList);
                        if (certInfo == null) break block7;
                        iterator = certInfo.mCerts.entrySet().iterator();
                        break block8;
                    }
                    Log.d(TAG, "Device Certificates already migrated. So no action needed");
                    break block7;
                }
                Log.d(TAG, "Device Certificates not migratable. do best effort");
                Log.d(TAG, "DRK Service Exist - Device Certificates are not migratable. Not Qualcomm chipset. Use Payment Service first to load Certs");
                this.mDevicePrivateCerts = this.mTAController.getCertInfo();
                if (this.isLoaded()) return;
                {
                    Log.e(TAG, "Device Certicates not loaded using PaymentService. Try falling back to DRK Service");
                    this.loadCertsFromDRKService();
                    return;
                }
            }
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry)iterator.next();
                if (entry.getValue() != null && ((String)entry.getKey()).equalsIgnoreCase(EFS_SIGN_CERT_PATH)) {
                    Utils.writeFile((byte[])entry.getValue(), this.certloader.getCertFilePath(PAY_SIGNCERT_FILENAME));
                    continue;
                }
                if (entry.getValue() == null || !((String)entry.getKey()).equalsIgnoreCase(EFS_SIGN_CERT_PATH)) continue;
                Utils.writeFile((byte[])entry.getValue(), this.certloader.getCertFilePath(PAY_ENCCERT_FILENAME));
            }
            Log.d(TAG, "Delete the Device Certificates from EFS");
            this.mTAController.clearDeviceCertificates(EFS_DEVICE_CERT_ROOT_DIR);
        }
        this.loadCertsFromDRKService();
    }
}

