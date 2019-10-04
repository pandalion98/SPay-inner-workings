/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.spay.CertInfo
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import android.spay.CertInfo;
import android.util.Log;
import com.samsung.android.spayfw.cncc.SpayDRKManager;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAException;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.b;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class a {
    private SpayDRKManager mCertloader = new SpayDRKManager();
    private CertInfo mDevicePrivateCerts = null;
    private TAController mTAController;
    private int wx = 0;
    private boolean wy = false;

    public a(TAController tAController) {
        this.mTAController = tAController;
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)new SpayDRKManager.CertFileInfo("dc_pay_enc.dat", 1));
        arrayList.add((Object)new SpayDRKManager.CertFileInfo("dc_pay_sign.dat", 2));
        this.wx = arrayList.size();
        this.mCertloader.init(tAController, null, "DC_PAY", b.eu().getTAInfo().getTAId(), (List<SpayDRKManager.CertFileInfo>)arrayList);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean ei() {
        this.mDevicePrivateCerts = this.mCertloader.getCertInfo();
        boolean bl = this.mDevicePrivateCerts.mCerts.size() >= this.wx;
        this.wy = bl;
        if (!this.wy) {
            throw new DcTAException(DcTAException.Code.xK);
        }
        return this.wy;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean ej() {
        if (this.wy) return this.wy;
        try {
            this.ei();
            return this.wy;
        }
        catch (DcTAException dcTAException) {
            dcTAException.printStackTrace();
            return this.wy;
        }
    }

    public byte[] getDevicePrivateEncryptionCert() {
        Log.d((String)"DcDeviceCerts", (String)("getDevicePrivateEncryptionCert: " + this.mDevicePrivateCerts.mCerts.get((Object)"dc_pay_enc.dat")));
        return (byte[])this.mDevicePrivateCerts.mCerts.get((Object)"dc_pay_enc.dat");
    }
}

