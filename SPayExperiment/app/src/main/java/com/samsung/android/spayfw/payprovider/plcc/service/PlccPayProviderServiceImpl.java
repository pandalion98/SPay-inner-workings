/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.io.File
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.HashMap
 */
package com.samsung.android.spayfw.payprovider.plcc.service;

import android.content.Context;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAException;
import com.samsung.android.spayfw.payprovider.plcc.util.Util;
import com.samsung.android.spayfw.utils.h;

import java.io.File;
import java.util.HashMap;

public class PlccPayProviderServiceImpl
implements PlccPayProviderService {
    private static final String TAG = "PlccPayProviderServiceImpl";
    private Context mContext;
    private PlccTAController mPlccTAController;

    public PlccPayProviderServiceImpl(PlccTAController plccTAController, Context context) {
        this.mPlccTAController = plccTAController;
        this.mContext = context;
    }

    @Override
    public byte[] addCard(byte[] arrby) {
        try {
            PlccTAController plccTAController = this.mPlccTAController;
            byte[] arrby2 = plccTAController.addCard("PLCC", arrby);
            return arrby2;
        }
        catch (PlccTAException plccTAException) {
            Log.c(TAG, plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return null;
        }
    }

    @Override
    public boolean authenticateTransaction(byte[] arrby) {
        try {
            boolean bl = this.mPlccTAController.authenticateTransaction(arrby);
            return bl;
        }
        catch (PlccTAException plccTAException) {
            Log.c(TAG, plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return false;
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public HashMap<String, byte[]> getCertDev() {
        PlccTAController.TACerts tACerts;
        HashMap hashMap;
        try {
            PlccTAController plccTAController = this.mPlccTAController;
            tACerts = plccTAController.getAllCerts("PLCC");
            hashMap = new HashMap();
        }
        catch (PlccTAException plccTAException) {
            hashMap = null;
            PlccTAException plccTAException2 = plccTAException;
            Log.c(TAG, plccTAException2.getMessage(), (Throwable)((Object)plccTAException2));
            return hashMap;
        }
        hashMap.put((Object)"cert_enc", (Object)tACerts.encryptcert);
        hashMap.put((Object)"cert_sign", (Object)tACerts.signcert);
        hashMap.put((Object)"cert_ca", (Object)tACerts.drk);
        return hashMap;
        {
            catch (PlccTAException plccTAException) {}
        }
    }

    @Override
    public byte[] getNonce(int n2) {
        try {
            byte[] arrby = this.mPlccTAController.getNonce(n2);
            return arrby;
        }
        catch (PlccTAException plccTAException) {
            Log.c(TAG, plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return new byte[]{0};
        }
    }

    @Override
    public String getTaid(String string) {
        return PlccTAController.getInstance().getTAInfo().getTAId();
    }

    @Override
    public boolean mstConfig(File file) {
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean mstTransmit(PlccCard plccCard, int n2, byte[] arrby) {
        try {
            return this.mPlccTAController.mstTransmit(Util.hexStringToBytes(plccCard.getTzEncCard()), n2, arrby);
        }
        catch (InterruptedException interruptedException) {
            Log.c(TAG, interruptedException.getMessage(), interruptedException);
            do {
                return false;
                break;
            } while (true);
        }
        catch (PlccTAException plccTAException) {
            Log.c(TAG, plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return false;
        }
    }

    @Override
    public void setCertServer(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        PlccTAController plccTAController = this.mPlccTAController;
        plccTAController.setPlccServerCerts("PLCC", arrby, arrby2, arrby3);
        try {
            PlccTAController plccTAController2 = this.mPlccTAController;
            plccTAController2.getAllCerts("PLCC");
            return;
        }
        catch (PlccTAException plccTAException) {
            Log.c(TAG, plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean stopMstTransmit() {
        boolean bl;
        Log.i(TAG, "clearMstData: start " + System.currentTimeMillis());
        try {
            boolean bl2;
            bl = bl2 = this.mPlccTAController.clearMstData();
        }
        catch (PlccTAException plccTAException) {
            Log.c(TAG, plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            bl = false;
        }
        Log.i(TAG, "clearMstData: end " + System.currentTimeMillis());
        return bl;
    }

    @Override
    public byte[] utilityEnc4ServerTransport(byte[] arrby) {
        try {
            long l2 = h.am(this.mContext);
            Log.d(TAG, "Network Time = " + l2);
            PlccTAController plccTAController = this.mPlccTAController;
            byte[] arrby2 = plccTAController.utility_enc4Server_Transport("PLCC", arrby, l2);
            return arrby2;
        }
        catch (PlccTAException plccTAException) {
            Log.c(TAG, plccTAException.getMessage(), (Throwable)((Object)plccTAException));
            return new byte[]{0};
        }
    }
}

