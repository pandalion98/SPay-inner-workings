/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.spay.CertInfo
 *  android.util.Log
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
package com.samsung.android.spayfw.payprovider.globalmembership.tzsvc;

import android.content.Context;
import android.spay.CertInfo;
import android.util.Log;
import com.samsung.android.spayfw.cncc.SpayDRKManager;
import com.samsung.android.spayfw.payprovider.globalmembership.tzsvc.c;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import com.samsung.android.spaytzsvc.api.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class b {
    SpayDRKManager certloader = new SpayDRKManager();
    private boolean isDRKServiceExist = false;
    private boolean isDRKServiceUsed = false;
    private CertInfo mDevicePrivateCerts = null;
    TAController mTAController;

    b(TAController tAController) {
        Log.d((String)"SpayFw_GMDeviceCerts", (String)"GlobalMembershipDeviceCerts");
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)"krcc_pay_sign.dat");
        arrayList.add((Object)"krcc_pay_enc.dat");
        this.mTAController = tAController;
        this.isDRKServiceExist = SpayDRKManager.isSupported(this.mTAController.getContext());
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add((Object)new SpayDRKManager.CertFileInfo("krcc_pay_sign.dat", 2));
        arrayList2.add((Object)new SpayDRKManager.CertFileInfo("krcc_pay_enc.dat", 1));
        this.certloader.init(this.mTAController, null, "GLOBAL_MEMBERSHIP_PAY", c.TA_INFO.getTAId(), (List<SpayDRKManager.CertFileInfo>)arrayList2);
    }

    private boolean alreadyMigrated() {
        File file = new File(this.certloader.getCertFilePath("krcc_pay_sign.dat"));
        File file2 = new File(this.certloader.getCertFilePath("krcc_pay_enc.dat"));
        return file.exists() && file2.exists();
    }

    private void loadCertsFromDRKService() {
        Log.d((String)"SpayFw_GMDeviceCerts", (String)"loadCertsFromDRKService");
        this.mDevicePrivateCerts = this.certloader.getCertInfo();
        this.isDRKServiceUsed = true;
        Log.e((String)"SpayFw_GMDeviceCerts", (String)"Device Certicates loaded using DRK Service");
    }

    public byte[] getDevicePrivateEncryptionCert() {
        if (!this.isDRKServiceUsed) {
            return (byte[])this.mDevicePrivateCerts.mCerts.get((Object)"/efs/prov_data/krcc_pay/krcc_pay_enc.dat");
        }
        return (byte[])this.mDevicePrivateCerts.mCerts.get((Object)"krcc_pay_enc.dat");
    }

    public byte[] getDevicePrivateSignCert() {
        if (!this.isDRKServiceUsed) {
            return (byte[])this.mDevicePrivateCerts.mCerts.get((Object)"/efs/prov_data/krcc_pay/krcc_pay_sign.dat");
        }
        return (byte[])this.mDevicePrivateCerts.mCerts.get((Object)"krcc_pay_sign.dat");
    }

    public boolean isLoaded() {
        return this.mDevicePrivateCerts != null && !this.mDevicePrivateCerts.mCerts.isEmpty() && this.getDevicePrivateEncryptionCert() != null && this.getDevicePrivateEncryptionCert() != null;
    }

    public boolean load() {
        Log.d((String)"SpayFw_GMDeviceCerts", (String)"load Device Certificates Start");
        this.loadInternal();
        if (this.mDevicePrivateCerts != null) {
            Log.d((String)"SpayFw_GMDeviceCerts", (String)("load cert done size: " + this.mDevicePrivateCerts.mCerts.size()));
        }
        if (!this.isLoaded()) {
            Log.e((String)"SpayFw_GMDeviceCerts", (String)"loadAllCerts: Error: get Wrapped Certificate Data from file system failed");
            this.mDevicePrivateCerts = null;
            return false;
        }
        Log.d((String)"SpayFw_GMDeviceCerts", (String)"load Device Certificates Success");
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
                            Log.d((String)"SpayFw_GMDeviceCerts", (String)"DRK Service Not Exist - Should be L Binary - Use Payment Service to load Certs");
                            this.mDevicePrivateCerts = this.mTAController.getCertInfo();
                            return;
                        }
                        Log.d((String)"SpayFw_GMDeviceCerts", (String)"DRK Service Exist");
                        if (TAController.isChipSetQC()) {
                            Log.d((String)"SpayFw_GMDeviceCerts", (String)"Device is Qualcomm - Directly Use DRK Service as PaymentService approach would anyway not work");
                            this.loadCertsFromDRKService();
                            return;
                        }
                        Log.d((String)"SpayFw_GMDeviceCerts", (String)"Device is not Qualcomm");
                        if (!this.mTAController.isDeviceCertificateMigratable()) break block5;
                        Log.d((String)"SpayFw_GMDeviceCerts", (String)"Device Certificates are migratable.");
                        if (this.alreadyMigrated()) break block6;
                        Log.d((String)"SpayFw_GMDeviceCerts", (String)"Device Certificates NOT already migrated.");
                        Log.d((String)"SpayFw_GMDeviceCerts", (String)"Get CertData from EFS and copy to local folder.");
                        ArrayList arrayList = new ArrayList();
                        arrayList.add((Object)"/efs/prov_data/krcc_pay/krcc_pay_sign.dat");
                        arrayList.add((Object)"/efs/prov_data/krcc_pay/krcc_pay_enc.dat");
                        CertInfo certInfo = this.mTAController.checkCertInfo((List<String>)arrayList);
                        if (certInfo == null) break block7;
                        iterator = certInfo.mCerts.entrySet().iterator();
                        break block8;
                    }
                    Log.d((String)"SpayFw_GMDeviceCerts", (String)"Device Certificates already migrated. So no action needed");
                    break block7;
                }
                Log.d((String)"SpayFw_GMDeviceCerts", (String)"Device Certificates not migratable. do best effort");
                Log.d((String)"SpayFw_GMDeviceCerts", (String)"DRK Service Exist - Device Certificates are not migratable. Not Qualcomm chipset. Use Payment Service first to load Certs");
                this.mDevicePrivateCerts = this.mTAController.getCertInfo();
                if (this.isLoaded()) return;
                {
                    Log.e((String)"SpayFw_GMDeviceCerts", (String)"Device Certicates not loaded using PaymentService. Try falling back to DRK Service");
                    this.loadCertsFromDRKService();
                    return;
                }
            }
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry)iterator.next();
                if (entry.getValue() != null && ((String)entry.getKey()).equalsIgnoreCase("/efs/prov_data/krcc_pay/krcc_pay_sign.dat")) {
                    Utils.writeFile((byte[])entry.getValue(), this.certloader.getCertFilePath("krcc_pay_sign.dat"));
                    continue;
                }
                if (entry.getValue() == null || !((String)entry.getKey()).equalsIgnoreCase("/efs/prov_data/krcc_pay/krcc_pay_sign.dat")) continue;
                Utils.writeFile((byte[])entry.getValue(), this.certloader.getCertFilePath("krcc_pay_enc.dat"));
            }
            Log.d((String)"SpayFw_GMDeviceCerts", (String)"Delete the Device Certificates from EFS");
            this.mTAController.clearDeviceCertificates("/efs/prov_data/krcc_pay/");
        }
        this.loadCertsFromDRKService();
    }
}

