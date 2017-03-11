package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.samsung.android.spayfw.p002b.Log;

public class GetTokenResponse {
    private static final String TAG = "GetTokenResponse";
    private ApduCommand[] apduCommands;
    private String appletInstanceAid;
    private boolean dsrpCapable;
    private String mdesTokenUniqueReference;
    private String panUniqueReference;
    private String paymentAppInstanceId;
    private String paymentAppProviderId;
    private String seId;
    private String tdsRegistrationUrl;

    public String getMdesTokenUniqueReference() {
        return this.mdesTokenUniqueReference;
    }

    public void setMdesTokenUniqueReference(String str) {
        this.mdesTokenUniqueReference = str;
    }

    public String getPaymentAppProviderId() {
        return this.paymentAppProviderId;
    }

    public void setPaymentAppProviderId(String str) {
        this.paymentAppProviderId = str;
    }

    public String getPaymentAppInstanceId() {
        return this.paymentAppInstanceId;
    }

    public void setPaymentAppInstanceId(String str) {
        this.paymentAppInstanceId = str;
    }

    public ApduCommand[] getApduCommands() {
        return this.apduCommands;
    }

    public void setApduCommands(ApduCommand[] apduCommandArr) {
        this.apduCommands = apduCommandArr;
    }

    public String getSeId() {
        return this.seId;
    }

    public void setSeId(String str) {
        this.seId = str;
    }

    public String getTdsRegistrationUrl() {
        return this.tdsRegistrationUrl;
    }

    public void setTdsRegistrationUrl(String str) {
        this.tdsRegistrationUrl = str;
    }

    public String getPanUniqueReference() {
        return this.panUniqueReference;
    }

    public void setPanUniqueReference(String str) {
        this.panUniqueReference = str;
    }

    public boolean isDsrpCapable() {
        return this.dsrpCapable;
    }

    public void setDsrpCapable(boolean z) {
        this.dsrpCapable = z;
    }

    public void setAppletInstanceAid(String str) {
        this.appletInstanceAid = str;
    }

    public String getAppletInstanceAid() {
        return this.appletInstanceAid;
    }

    public static GetTokenResponse parseJson(JsonObject jsonObject) {
        if (jsonObject == null) {
            Log.m285d(TAG, "getTokenResponse is null");
            return null;
        }
        try {
            return (GetTokenResponse) new Gson().fromJson((JsonElement) jsonObject, GetTokenResponse.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isCreateTokenRequestValid(GetTokenResponse getTokenResponse) {
        try {
            if (getTokenResponse.getApduCommands() == null || getTokenResponse.getApduCommands().length <= 0 || getTokenResponse.getPanUniqueReference() == null || TextUtils.isEmpty(getTokenResponse.getMdesTokenUniqueReference()) || getTokenResponse.getPaymentAppInstanceId() == null) {
                return false;
            }
            return true;
        } catch (NullPointerException e) {
            e.printStackTrace();
            if (getTokenResponse == null) {
                return false;
            }
            Log.m285d(TAG, "TR Server returned invalid jsonObject :" + getTokenResponse);
            return false;
        }
    }
}
