/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 *  android.util.Base64
 *  com.google.gson.JsonObject
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Set
 */
package com.samsung.android.spayfw.payprovider.plcc;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.plcc.db.PlccCardDetailsDao;
import com.samsung.android.spayfw.payprovider.plcc.db.PlccCardDetailsDaoImpl;
import com.samsung.android.spayfw.payprovider.plcc.domain.ActivationResponse;
import com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard;
import com.samsung.android.spayfw.payprovider.plcc.exception.PlccException;
import com.samsung.android.spayfw.payprovider.plcc.service.PlccPayProviderService;
import com.samsung.android.spayfw.payprovider.plcc.service.PlccPayProviderServiceImpl;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.payprovider.plcc.util.Util;
import java.util.HashMap;
import java.util.List;

public class PlccPayProviderSdk {
    private static final String TAG = "PlccPayProvider";
    private static final long UNLOAD_TIMER_EXPIRY_TIME = 60000L;
    private PlccCard mCard;
    private Context mContext;
    private PlccCardDetailsDao mPlccPayStorage;
    private PlccTAController mPlccTAController;
    private PlccPayProviderService mService;

    public PlccPayProviderSdk(Context context) {
        this.mContext = context;
        this.mPlccTAController = PlccTAController.getInstance();
        this.mService = new PlccPayProviderServiceImpl(this.mPlccTAController, context);
        this.mPlccPayStorage = PlccCardDetailsDaoImpl.getInstance(context);
    }

    public PlccCard addCard(String string, String string2, JsonObject jsonObject) {
        if (jsonObject == null) {
            Log.i(TAG, "Create Token - Response Data was null");
            return null;
        }
        ActivationResponse activationResponse = ActivationResponse.fromJson(jsonObject.toString());
        String string3 = activationResponse.getEncrypted();
        String string4 = activationResponse.getMerchantId();
        String string5 = activationResponse.getMstSeqConfig();
        if (TextUtils.isEmpty((CharSequence)string5)) {
            string5 = "(t2, r300, LZ30, TZ30, D1200)\n(t2, r300, LZ30, TZ30, D1200)\n(t2, r300, LZ30, TZ30, D1200)\n(t2, r300, LZ30, TZ30, D1200)\n(t1, r300, LZ30, TZ4, D0)\n(t2, r300, LZ6, TZ30, R, D1200)\n(t1, r300, LZ30, TZ4, D0)\n(t2, r300, LZ6, TZ30, R, D1200)\n(t1, r300, LZ30, TZ4, D0)\n(t2, r300, LZ6, TZ30, R, D1200)\n(t2, r800, LZ30, TZ30, D1200)\n(t2, r800, LZ30, TZ30, D1200)\n(t2, r800, LZ30, TZ30, D1200)\n(t2, r800, LZ30, TZ30, D1200)\n(t1, r500, LZ30, TZ4, D0)\n(t2, r500, LZ6, TZ30, R, D1200)\n(t1, r500, LZ30, TZ4, D0)\n(t2, r500, LZ6, TZ30, R, D1200)\n(t1, r500, LZ30, TZ4, D0)\n(t2, r500, LZ6, TZ30, R, D0)\n";
            Log.i(TAG, "MstSeqConfig = " + string5);
        }
        return this.addCard(string2, string, string3, string4, "ACTIVE", string5, activationResponse.getTimestamp());
    }

    public PlccCard addCard(String string, String string2, String string3, String string4, String string5, String string6, String string7) {
        byte[] arrby = Base64.decode((String)string3, (int)0);
        PlccCard plccCard = new PlccCard(string, Util.bytesToHex(this.mService.addCard(arrby)), string4, string5, string6, string2, string7);
        this.mPlccPayStorage.addCard(plccCard);
        return plccCard;
    }

    public boolean authenticateTransaction(byte[] arrby) {
        return this.mService.authenticateTransaction(arrby);
    }

    public void clearCard() {
        this.mCard = null;
    }

    public HashMap<String, byte[]> getDeviceCertificates() {
        return this.mService.getCertDev();
    }

    public byte[] getNonce(int n2) {
        return this.mService.getNonce(n2);
    }

    public String getPayConfig(String string) {
        return this.mPlccPayStorage.getMstConfig(string);
    }

    public String getTaid(String string) {
        return this.mService.getTaid(string);
    }

    public List<PlccCard> listCard() {
        return this.mPlccPayStorage.listCard();
    }

    protected void loadTA() {
        this.mPlccTAController.loadTA();
    }

    public boolean removeCard(String string) {
        if (this.mCard != null) {
            return this.mPlccPayStorage.removeCard(this.mCard);
        }
        PlccCard plccCard = new PlccCard();
        plccCard.setProviderKey(string);
        return this.mPlccPayStorage.removeCard(plccCard);
    }

    public boolean selectCard(String string) {
        this.mCard = this.mPlccPayStorage.selectCard(string);
        return this.mCard != null;
    }

    public void setPlccServerCerts(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        this.mService.setCertServer(arrby, arrby2, arrby3);
    }

    public boolean startMstTransmit(int n2, byte[] arrby) {
        if (this.mCard == null) {
            throw new PlccException("no card selected");
        }
        return this.mService.mstTransmit(this.mCard, n2, arrby);
    }

    public void stopMstTransmit() {
        this.mService.stopMstTransmit();
    }

    protected void unloadTA() {
        this.mPlccTAController.unloadTA();
    }

    public boolean updateCard(String string, String string2) {
        PlccCard plccCard = new PlccCard();
        plccCard.setProviderKey(string);
        plccCard.setTokenStatus(string2);
        return this.mPlccPayStorage.updateCard(plccCard);
    }

    public void updateSequenceConfig(HashMap<String, String> hashMap) {
        for (String string : hashMap.keySet()) {
            this.mPlccPayStorage.updateSequenceConfig(string, (String)hashMap.get((Object)string));
        }
    }

    public String utilityEnc4ServerTransport(String string) {
        return Base64.encodeToString((byte[])this.mService.utilityEnc4ServerTransport(string.getBytes()), (int)2);
    }
}

