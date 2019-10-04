/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonSyntaxException
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.payload.datamodels.ApduCommand;

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

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isCreateTokenRequestValid(GetTokenResponse getTokenResponse) {
        boolean bl;
        try {
            ApduCommand[] arrapduCommand = getTokenResponse.getApduCommands();
            bl = false;
            if (arrapduCommand == null) return bl;
        }
        catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            bl = false;
            if (getTokenResponse == null) return bl;
            c.d(TAG, "TR Server returned invalid jsonObject :" + getTokenResponse);
            return false;
        }
        int n2 = getTokenResponse.getApduCommands().length;
        bl = false;
        if (n2 <= 0) return bl;
        String string = getTokenResponse.getPanUniqueReference();
        bl = false;
        if (string == null) return bl;
        boolean bl2 = TextUtils.isEmpty((CharSequence)getTokenResponse.getMdesTokenUniqueReference());
        bl = false;
        if (bl2) return bl;
        String string2 = getTokenResponse.getPaymentAppInstanceId();
        bl = false;
        if (string2 == null) return bl;
        return true;
    }

    public static GetTokenResponse parseJson(JsonObject jsonObject) {
        if (jsonObject == null) {
            c.d(TAG, "getTokenResponse is null");
            return null;
        }
        try {
            GetTokenResponse getTokenResponse = (GetTokenResponse)new Gson().fromJson((JsonElement)jsonObject, GetTokenResponse.class);
            return getTokenResponse;
        }
        catch (JsonSyntaxException jsonSyntaxException) {
            jsonSyntaxException.printStackTrace();
            return null;
        }
    }

    public ApduCommand[] getApduCommands() {
        return this.apduCommands;
    }

    public String getAppletInstanceAid() {
        return this.appletInstanceAid;
    }

    public String getMdesTokenUniqueReference() {
        return this.mdesTokenUniqueReference;
    }

    public String getPanUniqueReference() {
        return this.panUniqueReference;
    }

    public String getPaymentAppInstanceId() {
        return this.paymentAppInstanceId;
    }

    public String getPaymentAppProviderId() {
        return this.paymentAppProviderId;
    }

    public String getSeId() {
        return this.seId;
    }

    public String getTdsRegistrationUrl() {
        return this.tdsRegistrationUrl;
    }

    public boolean isDsrpCapable() {
        return this.dsrpCapable;
    }

    public void setApduCommands(ApduCommand[] arrapduCommand) {
        this.apduCommands = arrapduCommand;
    }

    public void setAppletInstanceAid(String string) {
        this.appletInstanceAid = string;
    }

    public void setDsrpCapable(boolean bl) {
        this.dsrpCapable = bl;
    }

    public void setMdesTokenUniqueReference(String string) {
        this.mdesTokenUniqueReference = string;
    }

    public void setPanUniqueReference(String string) {
        this.panUniqueReference = string;
    }

    public void setPaymentAppInstanceId(String string) {
        this.paymentAppInstanceId = string;
    }

    public void setPaymentAppProviderId(String string) {
        this.paymentAppProviderId = string;
    }

    public void setSeId(String string) {
        this.seId = string;
    }

    public void setTdsRegistrationUrl(String string) {
        this.tdsRegistrationUrl = string;
    }
}

