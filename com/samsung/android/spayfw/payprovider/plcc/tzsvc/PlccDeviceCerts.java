package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

import android.spay.CertInfo;
import com.samsung.android.spayfw.cncc.SpayDRKManager;
import com.samsung.android.spayfw.cncc.SpayDRKManager.CertFileInfo;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class PlccDeviceCerts {
    protected static final String EFS_DEVICE_CERT_ROOT_DIR = "/efs/prov_data/plcc_pay/";
    private static final String EFS_ENC_CERT_PATH = "/efs/prov_data/plcc_pay/plcc_pay_enc.dat";
    private static final String EFS_SIGN_CERT_PATH = "/efs/prov_data/plcc_pay/plcc_pay_sign.dat";
    private static final String PAY_ENCCERT_FILENAME = "plcc_pay_enc.dat";
    private static final String PAY_SIGNCERT_FILENAME = "plcc_pay_sign.dat";
    private static final String PLCC_PAY_SERVICE_NAME = "PLCC_PAY";
    private static final String TAG = "PlccDeviceCerts";
    SpayDRKManager certloader;
    private boolean isDRKServiceExist;
    private boolean isDRKServiceUsed;
    private CertInfo mDevicePrivateCerts;
    TAController mTAController;

    PlccDeviceCerts(TAController tAController) {
        this.certloader = new SpayDRKManager();
        this.mDevicePrivateCerts = null;
        this.isDRKServiceExist = false;
        this.isDRKServiceUsed = false;
        Log.m285d(TAG, TAG);
        List arrayList = new ArrayList();
        arrayList.add(PAY_SIGNCERT_FILENAME);
        arrayList.add(PAY_ENCCERT_FILENAME);
        this.mTAController = tAController;
        this.isDRKServiceExist = SpayDRKManager.isSupported(this.mTAController.getContext());
        List arrayList2 = new ArrayList();
        arrayList2.add(new CertFileInfo(PAY_SIGNCERT_FILENAME, 2));
        arrayList2.add(new CertFileInfo(PAY_ENCCERT_FILENAME, 1));
        this.certloader.init(this.mTAController, null, PLCC_PAY_SERVICE_NAME, PlccTAController.TA_INFO.getTAId(), arrayList2);
    }

    public boolean isLoaded() {
        if (this.mDevicePrivateCerts == null || this.mDevicePrivateCerts.mCerts.isEmpty() || getDevicePrivateEncryptionCert() == null || getDevicePrivateEncryptionCert() == null) {
            return false;
        }
        return true;
    }

    private void loadCertsFromDRKService() {
        Log.m285d(TAG, "loadCertsFromDRKService");
        this.mDevicePrivateCerts = this.certloader.getCertInfo();
        this.isDRKServiceUsed = true;
        Log.m286e(TAG, "Device Certicates loaded using DRK Service");
    }

    private boolean alreadyMigrated() {
        File file = new File(this.certloader.getCertFilePath(PAY_SIGNCERT_FILENAME));
        File file2 = new File(this.certloader.getCertFilePath(PAY_ENCCERT_FILENAME));
        if (file.exists() && file2.exists()) {
            return true;
        }
        return false;
    }

    public void loadInternal() {
        if (this.isDRKServiceExist) {
            Log.m285d(TAG, "DRK Service Exist");
            if (TAController.isChipSetQC()) {
                Log.m285d(TAG, "Device is Qualcomm - Directly Use DRK Service as PaymentService approach would anyway not work");
                loadCertsFromDRKService();
                return;
            }
            Log.m285d(TAG, "Device is not Qualcomm");
            if (this.mTAController.isDeviceCertificateMigratable()) {
                Log.m285d(TAG, "Device Certificates are migratable.");
                if (alreadyMigrated()) {
                    Log.m285d(TAG, "Device Certificates already migrated. So no action needed");
                } else {
                    Log.m285d(TAG, "Device Certificates NOT already migrated.");
                    Log.m285d(TAG, "Get CertData from EFS and copy to local folder.");
                    List arrayList = new ArrayList();
                    arrayList.add(EFS_SIGN_CERT_PATH);
                    arrayList.add(EFS_ENC_CERT_PATH);
                    CertInfo checkCertInfo = this.mTAController.checkCertInfo(arrayList);
                    if (checkCertInfo != null) {
                        for (Entry entry : checkCertInfo.mCerts.entrySet()) {
                            if (entry.getValue() != null && ((String) entry.getKey()).equalsIgnoreCase(EFS_SIGN_CERT_PATH)) {
                                Utils.writeFile((byte[]) entry.getValue(), this.certloader.getCertFilePath(PAY_SIGNCERT_FILENAME));
                            } else if (entry.getValue() != null && ((String) entry.getKey()).equalsIgnoreCase(EFS_SIGN_CERT_PATH)) {
                                Utils.writeFile((byte[]) entry.getValue(), this.certloader.getCertFilePath(PAY_ENCCERT_FILENAME));
                            }
                        }
                        Log.m285d(TAG, "Delete the Device Certificates from EFS");
                        this.mTAController.clearDeviceCertificates(EFS_DEVICE_CERT_ROOT_DIR);
                    }
                }
                loadCertsFromDRKService();
                return;
            }
            Log.m285d(TAG, "Device Certificates not migratable. do best effort");
            Log.m285d(TAG, "DRK Service Exist - Device Certificates are not migratable. Not Qualcomm chipset. Use Payment Service first to load Certs");
            this.mDevicePrivateCerts = this.mTAController.getCertInfo();
            if (!isLoaded()) {
                Log.m286e(TAG, "Device Certicates not loaded using PaymentService. Try falling back to DRK Service");
                loadCertsFromDRKService();
                return;
            }
            return;
        }
        Log.m285d(TAG, "DRK Service Not Exist - Should be L Binary - Use Payment Service to load Certs");
        this.mDevicePrivateCerts = this.mTAController.getCertInfo();
    }

    public boolean load() {
        Log.m285d(TAG, "load Device Certificates Start");
        loadInternal();
        if (this.mDevicePrivateCerts != null) {
            Log.m285d(TAG, "load cert done size: " + this.mDevicePrivateCerts.mCerts.size());
        }
        if (isLoaded()) {
            Log.m285d(TAG, "load Device Certificates Success");
            return true;
        }
        Log.m286e(TAG, "loadAllCerts: Error: get Wrapped Certificate Data from file system failed");
        this.mDevicePrivateCerts = null;
        return false;
    }

    public byte[] getDevicePrivateSignCert() {
        if (this.isDRKServiceUsed) {
            return (byte[]) this.mDevicePrivateCerts.mCerts.get(PAY_SIGNCERT_FILENAME);
        }
        return (byte[]) this.mDevicePrivateCerts.mCerts.get(EFS_SIGN_CERT_PATH);
    }

    public byte[] getDevicePrivateEncryptionCert() {
        if (this.isDRKServiceUsed) {
            return (byte[]) this.mDevicePrivateCerts.mCerts.get(PAY_ENCCERT_FILENAME);
        }
        return (byte[]) this.mDevicePrivateCerts.mCerts.get(EFS_ENC_CERT_PATH);
    }
}
