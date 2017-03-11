package com.samsung.android.spayfw.payprovider.plcc.service;

import android.content.Context;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController.TACerts;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAException;
import com.samsung.android.spayfw.payprovider.plcc.util.PlccConstants;
import com.samsung.android.spayfw.payprovider.plcc.util.Util;
import com.samsung.android.spayfw.utils.Utils;
import java.io.File;
import java.util.HashMap;

public class PlccPayProviderServiceImpl implements PlccPayProviderService {
    private static final String TAG = "PlccPayProviderServiceImpl";
    private Context mContext;
    private PlccTAController mPlccTAController;

    public PlccPayProviderServiceImpl(PlccTAController plccTAController, Context context) {
        this.mPlccTAController = plccTAController;
        this.mContext = context;
    }

    public void setCertServer(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        PlccTAController plccTAController = this.mPlccTAController;
        PlccTAController plccTAController2 = this.mPlccTAController;
        plccTAController.setPlccServerCerts(PlccTAController.CARD_BRAND_PLCC, bArr, bArr2, bArr3);
        try {
            plccTAController = this.mPlccTAController;
            plccTAController2 = this.mPlccTAController;
            plccTAController.getAllCerts(PlccTAController.CARD_BRAND_PLCC);
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
        }
    }

    public HashMap<String, byte[]> getCertDev() {
        HashMap<String, byte[]> hashMap;
        Throwable e;
        try {
            PlccTAController plccTAController = this.mPlccTAController;
            PlccTAController plccTAController2 = this.mPlccTAController;
            TACerts allCerts = plccTAController.getAllCerts(PlccTAController.CARD_BRAND_PLCC);
            hashMap = new HashMap();
            try {
                hashMap.put(PlccConstants.CERT_ENC, allCerts.encryptcert);
                hashMap.put(PlccConstants.CERT_SIGN, allCerts.signcert);
                hashMap.put(PlccConstants.CERT_CA, allCerts.drk);
            } catch (PlccTAException e2) {
                e = e2;
                Log.m284c(TAG, e.getMessage(), e);
                return hashMap;
            }
        } catch (Throwable e3) {
            Throwable th = e3;
            hashMap = null;
            e = th;
            Log.m284c(TAG, e.getMessage(), e);
            return hashMap;
        }
        return hashMap;
    }

    public byte[] utilityEnc4ServerTransport(byte[] bArr) {
        try {
            long am = Utils.am(this.mContext);
            Log.m285d(TAG, "Network Time = " + am);
            PlccTAController plccTAController = this.mPlccTAController;
            PlccTAController plccTAController2 = this.mPlccTAController;
            return plccTAController.utility_enc4Server_Transport(PlccTAController.CARD_BRAND_PLCC, bArr, am);
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
            return new byte[]{(byte) 0};
        }
    }

    public byte[] addCard(byte[] bArr) {
        try {
            PlccTAController plccTAController = this.mPlccTAController;
            PlccTAController plccTAController2 = this.mPlccTAController;
            return plccTAController.addCard(PlccTAController.CARD_BRAND_PLCC, bArr);
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
            return null;
        }
    }

    public boolean stopMstTransmit() {
        Log.m287i(TAG, "clearMstData: start " + System.currentTimeMillis());
        boolean z = false;
        try {
            z = this.mPlccTAController.clearMstData();
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
        }
        Log.m287i(TAG, "clearMstData: end " + System.currentTimeMillis());
        return z;
    }

    public boolean mstTransmit(PlccCard plccCard, int i, byte[] bArr) {
        try {
            return this.mPlccTAController.mstTransmit(Util.hexStringToBytes(plccCard.getTzEncCard()), i, bArr);
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
            return false;
        } catch (Throwable e2) {
            Log.m284c(TAG, e2.getMessage(), e2);
            return false;
        }
    }

    public boolean mstConfig(File file) {
        return false;
    }

    public String getTaid(String str) {
        return PlccTAController.getInstance().getTAInfo().getTAId();
    }

    public byte[] getNonce(int i) {
        try {
            return this.mPlccTAController.getNonce(i);
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
            return new byte[]{(byte) 0};
        }
    }

    public boolean authenticateTransaction(byte[] bArr) {
        try {
            return this.mPlccTAController.authenticateTransaction(bArr);
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
            return false;
        }
    }
}
