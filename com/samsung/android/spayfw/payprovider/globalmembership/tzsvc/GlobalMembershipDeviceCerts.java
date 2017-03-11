package com.samsung.android.spayfw.payprovider.globalmembership.tzsvc;

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

/* renamed from: com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.b */
public class GlobalMembershipDeviceCerts {
    SpayDRKManager certloader;
    private boolean isDRKServiceExist;
    private boolean isDRKServiceUsed;
    private CertInfo mDevicePrivateCerts;
    TAController mTAController;

    GlobalMembershipDeviceCerts(TAController tAController) {
        this.certloader = new SpayDRKManager();
        this.mDevicePrivateCerts = null;
        this.isDRKServiceExist = false;
        this.isDRKServiceUsed = false;
        Log.d("SpayFw_GMDeviceCerts", "GlobalMembershipDeviceCerts");
        List arrayList = new ArrayList();
        arrayList.add("krcc_pay_sign.dat");
        arrayList.add("krcc_pay_enc.dat");
        this.mTAController = tAController;
        this.isDRKServiceExist = SpayDRKManager.isSupported(this.mTAController.getContext());
        List arrayList2 = new ArrayList();
        arrayList2.add(new CertFileInfo("krcc_pay_sign.dat", 2));
        arrayList2.add(new CertFileInfo("krcc_pay_enc.dat", 1));
        this.certloader.init(this.mTAController, null, "GLOBAL_MEMBERSHIP_PAY", GlobalMembershipTAController.TA_INFO.getTAId(), arrayList2);
    }

    public boolean isLoaded() {
        if (this.mDevicePrivateCerts == null || this.mDevicePrivateCerts.mCerts.isEmpty() || getDevicePrivateEncryptionCert() == null || getDevicePrivateEncryptionCert() == null) {
            return false;
        }
        return true;
    }

    private void loadCertsFromDRKService() {
        Log.d("SpayFw_GMDeviceCerts", "loadCertsFromDRKService");
        this.mDevicePrivateCerts = this.certloader.getCertInfo();
        this.isDRKServiceUsed = true;
        Log.e("SpayFw_GMDeviceCerts", "Device Certicates loaded using DRK Service");
    }

    private boolean alreadyMigrated() {
        File file = new File(this.certloader.getCertFilePath("krcc_pay_sign.dat"));
        File file2 = new File(this.certloader.getCertFilePath("krcc_pay_enc.dat"));
        if (file.exists() && file2.exists()) {
            return true;
        }
        return false;
    }

    public void loadInternal() {
        if (this.isDRKServiceExist) {
            Log.d("SpayFw_GMDeviceCerts", "DRK Service Exist");
            if (TAController.isChipSetQC()) {
                Log.d("SpayFw_GMDeviceCerts", "Device is Qualcomm - Directly Use DRK Service as PaymentService approach would anyway not work");
                loadCertsFromDRKService();
                return;
            }
            Log.d("SpayFw_GMDeviceCerts", "Device is not Qualcomm");
            if (this.mTAController.isDeviceCertificateMigratable()) {
                Log.d("SpayFw_GMDeviceCerts", "Device Certificates are migratable.");
                if (alreadyMigrated()) {
                    Log.d("SpayFw_GMDeviceCerts", "Device Certificates already migrated. So no action needed");
                } else {
                    Log.d("SpayFw_GMDeviceCerts", "Device Certificates NOT already migrated.");
                    Log.d("SpayFw_GMDeviceCerts", "Get CertData from EFS and copy to local folder.");
                    List arrayList = new ArrayList();
                    arrayList.add("/efs/prov_data/krcc_pay/krcc_pay_sign.dat");
                    arrayList.add("/efs/prov_data/krcc_pay/krcc_pay_enc.dat");
                    CertInfo checkCertInfo = this.mTAController.checkCertInfo(arrayList);
                    if (checkCertInfo != null) {
                        for (Entry entry : checkCertInfo.mCerts.entrySet()) {
                            if (entry.getValue() != null && ((String) entry.getKey()).equalsIgnoreCase("/efs/prov_data/krcc_pay/krcc_pay_sign.dat")) {
                                Utils.writeFile((byte[]) entry.getValue(), this.certloader.getCertFilePath("krcc_pay_sign.dat"));
                            } else if (entry.getValue() != null && ((String) entry.getKey()).equalsIgnoreCase("/efs/prov_data/krcc_pay/krcc_pay_sign.dat")) {
                                Utils.writeFile((byte[]) entry.getValue(), this.certloader.getCertFilePath("krcc_pay_enc.dat"));
                            }
                        }
                        Log.d("SpayFw_GMDeviceCerts", "Delete the Device Certificates from EFS");
                        this.mTAController.clearDeviceCertificates("/efs/prov_data/krcc_pay/");
                    }
                }
                loadCertsFromDRKService();
                return;
            }
            Log.d("SpayFw_GMDeviceCerts", "Device Certificates not migratable. do best effort");
            Log.d("SpayFw_GMDeviceCerts", "DRK Service Exist - Device Certificates are not migratable. Not Qualcomm chipset. Use Payment Service first to load Certs");
            this.mDevicePrivateCerts = this.mTAController.getCertInfo();
            if (!isLoaded()) {
                Log.e("SpayFw_GMDeviceCerts", "Device Certicates not loaded using PaymentService. Try falling back to DRK Service");
                loadCertsFromDRKService();
                return;
            }
            return;
        }
        Log.d("SpayFw_GMDeviceCerts", "DRK Service Not Exist - Should be L Binary - Use Payment Service to load Certs");
        this.mDevicePrivateCerts = this.mTAController.getCertInfo();
    }

    public boolean load() {
        Log.d("SpayFw_GMDeviceCerts", "load Device Certificates Start");
        loadInternal();
        if (this.mDevicePrivateCerts != null) {
            Log.d("SpayFw_GMDeviceCerts", "load cert done size: " + this.mDevicePrivateCerts.mCerts.size());
        }
        if (isLoaded()) {
            Log.d("SpayFw_GMDeviceCerts", "load Device Certificates Success");
            return true;
        }
        Log.e("SpayFw_GMDeviceCerts", "loadAllCerts: Error: get Wrapped Certificate Data from file system failed");
        this.mDevicePrivateCerts = null;
        return false;
    }

    public byte[] getDevicePrivateSignCert() {
        if (this.isDRKServiceUsed) {
            return (byte[]) this.mDevicePrivateCerts.mCerts.get("krcc_pay_sign.dat");
        }
        return (byte[]) this.mDevicePrivateCerts.mCerts.get("/efs/prov_data/krcc_pay/krcc_pay_sign.dat");
    }

    public byte[] getDevicePrivateEncryptionCert() {
        if (this.isDRKServiceUsed) {
            return (byte[]) this.mDevicePrivateCerts.mCerts.get("krcc_pay_enc.dat");
        }
        return (byte[]) this.mDevicePrivateCerts.mCerts.get("/efs/prov_data/krcc_pay/krcc_pay_enc.dat");
    }
}
