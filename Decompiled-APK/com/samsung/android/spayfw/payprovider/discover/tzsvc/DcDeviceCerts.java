package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import android.spay.CertInfo;
import android.util.Log;
import com.samsung.android.spayfw.cncc.SpayDRKManager;
import com.samsung.android.spayfw.cncc.SpayDRKManager.CertFileInfo;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAException.Code;
import com.samsung.android.spaytzsvc.api.TAController;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.tzsvc.a */
public class DcDeviceCerts {
    private SpayDRKManager mCertloader;
    private CertInfo mDevicePrivateCerts;
    private TAController mTAController;
    private int wx;
    private boolean wy;

    public DcDeviceCerts(TAController tAController) {
        this.wx = 0;
        this.mCertloader = new SpayDRKManager();
        this.mDevicePrivateCerts = null;
        this.wy = false;
        this.mTAController = tAController;
        List arrayList = new ArrayList();
        arrayList.add(new CertFileInfo("dc_pay_enc.dat", 1));
        arrayList.add(new CertFileInfo("dc_pay_sign.dat", 2));
        this.wx = arrayList.size();
        TAController tAController2 = tAController;
        this.mCertloader.init(tAController2, null, "DC_PAY", DcTAController.eu().getTAInfo().getTAId(), arrayList);
    }

    public boolean ei() {
        this.mDevicePrivateCerts = this.mCertloader.getCertInfo();
        this.wy = this.mDevicePrivateCerts.mCerts.size() >= this.wx;
        if (this.wy) {
            return this.wy;
        }
        throw new DcTAException(Code.ERR_LOAD_CERT_FAILED);
    }

    public boolean ej() {
        if (!this.wy) {
            try {
                ei();
            } catch (DcTAException e) {
                e.printStackTrace();
            }
        }
        return this.wy;
    }

    public byte[] getDevicePrivateEncryptionCert() {
        Log.d("DcDeviceCerts", "getDevicePrivateEncryptionCert: " + this.mDevicePrivateCerts.mCerts.get("dc_pay_enc.dat"));
        return (byte[]) this.mDevicePrivateCerts.mCerts.get("dc_pay_enc.dat");
    }
}
