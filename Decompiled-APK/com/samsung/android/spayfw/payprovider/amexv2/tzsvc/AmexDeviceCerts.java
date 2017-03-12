package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.spay.CertInfo;
import android.util.Log;
import com.samsung.android.spayfw.cncc.SpayDRKManager;
import com.samsung.android.spayfw.cncc.SpayDRKManager.CertFileInfo;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/* renamed from: com.samsung.android.spayfw.payprovider.amexv2.tzsvc.a */
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
        Log.d("SPAY:AmexDeviceCerts", "AmexDeviceCerts");
        this.mTAController = tAController;
        this.isDRKServiceExist = SpayDRKManager.isSupported(this.mTAController.getContext());
        List arrayList = new ArrayList();
        arrayList.add(new CertFileInfo("aexp_pay_sign.dat", 2));
        arrayList.add(new CertFileInfo("aexp_pay_enc.dat", 1));
        this.certloader.init(this.mTAController, null, "AEXP_PAY", AmexTAController.cz().getTAInfo().getTAId(), arrayList);
    }

    public boolean isLoaded() {
        if (this.mDevicePrivateCerts == null || this.mDevicePrivateCerts.mCerts.isEmpty() || getDevicePrivateEncryptionCert() == null || getDevicePrivateEncryptionCert() == null) {
            return false;
        }
        return true;
    }

    private void loadCertsFromDRKService() {
        Log.d("SPAY:AmexDeviceCerts", "loadCertsFromDRKService");
        this.mDevicePrivateCerts = this.certloader.getCertInfo();
        this.isDRKServiceUsed = true;
        Log.e("SPAY:AmexDeviceCerts", "Device Certicates loaded using DRK Service");
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
            Log.d("SPAY:AmexDeviceCerts", "DRK Service Exist");
            if (TAController.isChipSetQC()) {
                Log.d("SPAY:AmexDeviceCerts", "Device is Qualcomm - Directly Use DRK Service as PaymentService approach would anyway not work");
                loadCertsFromDRKService();
                return;
            }
            Log.d("SPAY:AmexDeviceCerts", "Device is not Qualcomm");
            if (this.mTAController.isDeviceCertificateMigratable()) {
                Log.d("SPAY:AmexDeviceCerts", "Device Certificates are migratable.");
                if (alreadyMigrated()) {
                    Log.d("SPAY:AmexDeviceCerts", "Device Certificates already migrated. So no action needed");
                } else {
                    Log.d("SPAY:AmexDeviceCerts", "Device Certificates NOT already migrated.");
                    Log.d("SPAY:AmexDeviceCerts", "Get CertData from EFS and copy to local folder.");
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
                        Log.d("SPAY:AmexDeviceCerts", "Delete the Device Certificates from EFS");
                        this.mTAController.clearDeviceCertificates("/efs/prov_data/aexp_pay/");
                    }
                }
                loadCertsFromDRKService();
                return;
            }
            Log.d("SPAY:AmexDeviceCerts", "Device Certificates not migratable. do best effort");
            Log.d("SPAY:AmexDeviceCerts", "DRK Service Exist - Device Certificates are not migratable. Not Qualcomm chipset. Use Payment Service first to load Certs");
            this.mDevicePrivateCerts = this.mTAController.getCertInfo();
            if (!isLoaded()) {
                Log.e("SPAY:AmexDeviceCerts", "Device Certicates not loaded using PaymentService. Try falling back to DRK Service");
                loadCertsFromDRKService();
                return;
            }
            return;
        }
        Log.d("SPAY:AmexDeviceCerts", "DRK Service Not Exist - Should be L Binary - Use Payment Service to load Certs");
        this.mDevicePrivateCerts = this.mTAController.getCertInfo();
    }

    public boolean load() {
        Log.d("SPAY:AmexDeviceCerts", "load Device Certificates Start");
        loadInternal();
        if (this.mDevicePrivateCerts != null) {
            Log.d("SPAY:AmexDeviceCerts", "load cert done size: " + this.mDevicePrivateCerts.mCerts.size());
        }
        if (isLoaded()) {
            Log.d("SPAY:AmexDeviceCerts", "load Device Certificates Success");
            return true;
        }
        Log.e("SPAY:AmexDeviceCerts", "loadAllCerts: Error: get Wrapped Certificate Data from file system failed");
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
