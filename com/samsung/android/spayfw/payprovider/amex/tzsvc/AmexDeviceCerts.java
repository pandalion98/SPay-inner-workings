package com.samsung.android.spayfw.payprovider.amex.tzsvc;

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

/* renamed from: com.samsung.android.spayfw.payprovider.amex.tzsvc.a */
public class AmexDeviceCerts {
    SpayDRKManager certloader;
    private boolean isDRKServiceExist;
    private boolean isDRKServiceUsed;
    private CertInfo mDevicePrivateCerts;
    TAController mTAController;

    AmexDeviceCerts(TAController tAController) {
        this.certloader = new SpayDRKManager();
        this.mDevicePrivateCerts = null;
        this.isDRKServiceExist = false;
        this.isDRKServiceUsed = false;
        Log.m285d("AmexDeviceCerts", "AmexDeviceCerts");
        this.mTAController = tAController;
        this.isDRKServiceExist = SpayDRKManager.isSupported(this.mTAController.getContext());
        List arrayList = new ArrayList();
        arrayList.add(new CertFileInfo("aexp_pay_sign.dat", 2));
        arrayList.add(new CertFileInfo("aexp_pay_enc.dat", 1));
        this.certloader.init(this.mTAController, null, "AEXP_PAY", AmexTAController.ct().getTAInfo().getTAId(), arrayList);
    }

    public boolean isLoaded() {
        if (this.mDevicePrivateCerts == null || this.mDevicePrivateCerts.mCerts.isEmpty() || getDevicePrivateEncryptionCert() == null || getDevicePrivateEncryptionCert() == null) {
            return false;
        }
        return true;
    }

    private void loadCertsFromDRKService() {
        Log.m285d("AmexDeviceCerts", "loadCertsFromDRKService");
        this.mDevicePrivateCerts = this.certloader.getCertInfo();
        this.isDRKServiceUsed = true;
        Log.m286e("AmexDeviceCerts", "Device Certicates loaded using DRK Service");
    }

    private boolean alreadyMigrated() {
        File file = new File(this.certloader.getCertFilePath("aexp_pay_sign.dat"));
        File file2 = new File(this.certloader.getCertFilePath("aexp_pay_enc.dat"));
        if (file.exists() && file2.exists()) {
            return true;
        }
        return false;
    }

    public void loadInternal() {
        if (this.isDRKServiceExist) {
            Log.m285d("AmexDeviceCerts", "DRK Service Exist");
            if (TAController.isChipSetQC()) {
                Log.m285d("AmexDeviceCerts", "Device is Qualcomm - Directly Use DRK Service as PaymentService approach would anyway not work");
                loadCertsFromDRKService();
                return;
            }
            Log.m285d("AmexDeviceCerts", "Device is not Qualcomm");
            if (this.mTAController.isDeviceCertificateMigratable()) {
                Log.m285d("AmexDeviceCerts", "Device Certificates are migratable.");
                if (alreadyMigrated()) {
                    Log.m285d("AmexDeviceCerts", "Device Certificates already migrated. So no action needed");
                } else {
                    Log.m285d("AmexDeviceCerts", "Device Certificates NOT already migrated.");
                    Log.m285d("AmexDeviceCerts", "Get CertData from EFS and copy to local folder.");
                    List arrayList = new ArrayList();
                    arrayList.add("/efs/prov_data/aexp_pay/aexp_pay_sign.dat");
                    arrayList.add("/efs/prov_data/aexp_pay/aexp_pay_enc.dat");
                    CertInfo checkCertInfo = this.mTAController.checkCertInfo(arrayList);
                    if (checkCertInfo != null) {
                        for (Entry entry : checkCertInfo.mCerts.entrySet()) {
                            if (entry.getValue() != null && ((String) entry.getKey()).equalsIgnoreCase("/efs/prov_data/aexp_pay/aexp_pay_sign.dat")) {
                                Utils.writeFile((byte[]) entry.getValue(), this.certloader.getCertFilePath("aexp_pay_sign.dat"));
                            } else if (entry.getValue() != null && ((String) entry.getKey()).equalsIgnoreCase("/efs/prov_data/aexp_pay/aexp_pay_enc.dat")) {
                                Utils.writeFile((byte[]) entry.getValue(), this.certloader.getCertFilePath("aexp_pay_enc.dat"));
                            }
                        }
                        Log.m285d("AmexDeviceCerts", "Delete the Device Certificates from EFS");
                        this.mTAController.clearDeviceCertificates("/efs/prov_data/aexp_pay/");
                    }
                }
                loadCertsFromDRKService();
                return;
            }
            Log.m285d("AmexDeviceCerts", "Device Certificates not migratable. do best effort");
            Log.m285d("AmexDeviceCerts", "DRK Service Exist - Device Certificates are not migratable. Not Qualcomm chipset. Use Payment Service first to load Certs");
            this.mDevicePrivateCerts = this.mTAController.getCertInfo();
            if (!isLoaded()) {
                Log.m286e("AmexDeviceCerts", "Device Certicates not loaded using PaymentService. Try falling back to DRK Service");
                loadCertsFromDRKService();
                return;
            }
            return;
        }
        Log.m285d("AmexDeviceCerts", "DRK Service Not Exist - Should be L Binary - Use Payment Service to load Certs");
        this.mDevicePrivateCerts = this.mTAController.getCertInfo();
    }

    public boolean load() {
        Log.m285d("AmexDeviceCerts", "load Device Certificates Start");
        loadInternal();
        if (this.mDevicePrivateCerts != null) {
            Log.m285d("AmexDeviceCerts", "load cert done size: " + this.mDevicePrivateCerts.mCerts.size());
        }
        if (isLoaded()) {
            Log.m285d("AmexDeviceCerts", "load Device Certificates Success");
            return true;
        }
        Log.m286e("AmexDeviceCerts", "loadAllCerts: Error: get Wrapped Certificate Data from file system failed");
        this.mDevicePrivateCerts = null;
        return false;
    }

    public byte[] getDevicePrivateSignCert() {
        if (this.isDRKServiceUsed) {
            return (byte[]) this.mDevicePrivateCerts.mCerts.get("aexp_pay_sign.dat");
        }
        return (byte[]) this.mDevicePrivateCerts.mCerts.get("/efs/prov_data/aexp_pay/aexp_pay_sign.dat");
    }

    public byte[] getDevicePrivateEncryptionCert() {
        if (this.isDRKServiceUsed) {
            return (byte[]) this.mDevicePrivateCerts.mCerts.get("aexp_pay_enc.dat");
        }
        return (byte[]) this.mDevicePrivateCerts.mCerts.get("/efs/prov_data/aexp_pay/aexp_pay_enc.dat");
    }
}
