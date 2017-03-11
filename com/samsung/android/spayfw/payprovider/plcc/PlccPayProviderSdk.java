package com.samsung.android.spayfw.payprovider.plcc;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.plcc.db.PlccCardDetailsDao;
import com.samsung.android.spayfw.payprovider.plcc.db.PlccCardDetailsDaoImpl;
import com.samsung.android.spayfw.payprovider.plcc.domain.ActivationResponse;
import com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard;
import com.samsung.android.spayfw.payprovider.plcc.exception.PlccException;
import com.samsung.android.spayfw.payprovider.plcc.service.PlccPayProviderService;
import com.samsung.android.spayfw.payprovider.plcc.service.PlccPayProviderServiceImpl;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.payprovider.plcc.util.PlccConstants;
import com.samsung.android.spayfw.payprovider.plcc.util.Util;
import java.util.HashMap;
import java.util.List;

public class PlccPayProviderSdk {
    private static final String TAG = "PlccPayProvider";
    private static final long UNLOAD_TIMER_EXPIRY_TIME = 60000;
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

    protected void loadTA() {
        this.mPlccTAController.loadTA();
    }

    protected void unloadTA() {
        this.mPlccTAController.unloadTA();
    }

    public String getTaid(String str) {
        return this.mService.getTaid(str);
    }

    public byte[] getNonce(int i) {
        return this.mService.getNonce(i);
    }

    public PlccCard addCard(String str, String str2, JsonObject jsonObject) {
        if (jsonObject == null) {
            Log.m287i(TAG, "Create Token - Response Data was null");
            return null;
        }
        ActivationResponse fromJson = ActivationResponse.fromJson(jsonObject.toString());
        String encrypted = fromJson.getEncrypted();
        String merchantId = fromJson.getMerchantId();
        String mstSeqConfig = fromJson.getMstSeqConfig();
        if (TextUtils.isEmpty(mstSeqConfig)) {
            mstSeqConfig = PlccConstants.DEFAULT_SEQUENCE;
            Log.m287i(TAG, "MstSeqConfig = " + mstSeqConfig);
        }
        return addCard(str2, str, encrypted, merchantId, TokenStatus.ACTIVE, mstSeqConfig, fromJson.getTimestamp());
    }

    public PlccCard addCard(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        PlccCard plccCard = new PlccCard(str, Util.bytesToHex(this.mService.addCard(Base64.decode(str3, 0))), str4, str5, str6, str2, str7);
        this.mPlccPayStorage.addCard(plccCard);
        return plccCard;
    }

    public List<PlccCard> listCard() {
        return this.mPlccPayStorage.listCard();
    }

    public boolean selectCard(String str) {
        this.mCard = this.mPlccPayStorage.selectCard(str);
        if (this.mCard == null) {
            return false;
        }
        return true;
    }

    public void clearCard() {
        this.mCard = null;
    }

    public boolean removeCard(String str) {
        if (this.mCard != null) {
            return this.mPlccPayStorage.removeCard(this.mCard);
        }
        PlccCard plccCard = new PlccCard();
        plccCard.setProviderKey(str);
        return this.mPlccPayStorage.removeCard(plccCard);
    }

    public void setPlccServerCerts(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        this.mService.setCertServer(bArr, bArr2, bArr3);
    }

    public boolean startMstTransmit(int i, byte[] bArr) {
        if (this.mCard != null) {
            return this.mService.mstTransmit(this.mCard, i, bArr);
        }
        throw new PlccException("no card selected");
    }

    public void stopMstTransmit() {
        this.mService.stopMstTransmit();
    }

    public String utilityEnc4ServerTransport(String str) {
        return Base64.encodeToString(this.mService.utilityEnc4ServerTransport(str.getBytes()), 2);
    }

    public HashMap<String, byte[]> getDeviceCertificates() {
        return this.mService.getCertDev();
    }

    public boolean authenticateTransaction(byte[] bArr) {
        return this.mService.authenticateTransaction(bArr);
    }

    public boolean updateCard(String str, String str2) {
        PlccCard plccCard = new PlccCard();
        plccCard.setProviderKey(str);
        plccCard.setTokenStatus(str2);
        return this.mPlccPayStorage.updateCard(plccCard);
    }

    public void updateSequenceConfig(HashMap<String, String> hashMap) {
        for (String str : hashMap.keySet()) {
            this.mPlccPayStorage.updateSequenceConfig(str, (String) hashMap.get(str));
        }
    }

    public String getPayConfig(String str) {
        return this.mPlccPayStorage.getMstConfig(str);
    }
}
