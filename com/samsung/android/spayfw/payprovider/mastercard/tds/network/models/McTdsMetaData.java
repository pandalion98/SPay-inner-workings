package com.samsung.android.spayfw.payprovider.mastercard.tds.network.models;

import android.text.TextUtils;
import com.samsung.android.spayfw.p002b.Log;

public class McTdsMetaData {
    private static final int MAX_LENGTH_AUTH_CODE = 64;
    private static final int MAX_LENGTH_LAST_UPDATE_TAG = 128;
    private static final int MAX_LENGTH_TDS_REGISTRATION_URL = 128;
    private static final int MAX_LENGTH_TDS_URL = 128;
    private static final String TAG = "McTdsMetaData";
    private String mAuthCode;
    private long mCardMasterId;
    private String mHash;
    private String mLastUpdateTag;
    private String mPaymentAppInstanceId;
    private String mTdsRegisterUrl;
    private String mTdsUrl;

    public long getCardMasterId() {
        return this.mCardMasterId;
    }

    public void setCardMasterId(long j) {
        this.mCardMasterId = j;
    }

    public String getTdsUrl() {
        return this.mTdsUrl;
    }

    public void setTdsUrl(String str) {
        this.mTdsUrl = str;
    }

    public String getTdsRegisterUrl() {
        return this.mTdsRegisterUrl;
    }

    public void setTdsRegisterUrl(String str) {
        this.mTdsRegisterUrl = str;
    }

    public String getHash() {
        return this.mHash;
    }

    public void setHash(String str) {
        this.mHash = str;
    }

    public String getAuthCode() {
        return this.mAuthCode;
    }

    public void setAuthCode(String str) {
        this.mAuthCode = str;
    }

    public String getLastUpdateTag() {
        return this.mLastUpdateTag;
    }

    public void setLastUpdateTag(String str) {
        this.mLastUpdateTag = str;
    }

    public String getPaymentAppInstanceId() {
        return this.mPaymentAppInstanceId;
    }

    public void setPaymentAppInstanceId(String str) {
        this.mPaymentAppInstanceId = str;
    }

    public boolean validate() {
        if (!TextUtils.isEmpty(this.mTdsUrl) && this.mTdsUrl.length() > MAX_LENGTH_TDS_URL) {
            Log.m285d(TAG, "validate: invalid tdsUrl :" + this.mTdsUrl);
            return false;
        } else if (!TextUtils.isEmpty(this.mTdsRegisterUrl) && this.mTdsRegisterUrl.length() > MAX_LENGTH_TDS_URL) {
            Log.m285d(TAG, "validate: invalid tdsRegistrationUrl :" + this.mTdsRegisterUrl);
            return false;
        } else if (this.mCardMasterId == -1) {
            Log.m285d(TAG, "validate: invalid foreign key :" + this.mCardMasterId);
            return false;
        } else if (!TextUtils.isEmpty(this.mAuthCode) && this.mAuthCode.length() > MAX_LENGTH_AUTH_CODE) {
            Log.m285d(TAG, "validate: invalid authCode :" + this.mAuthCode);
            return false;
        } else if (TextUtils.isEmpty(this.mLastUpdateTag) || this.mLastUpdateTag.length() <= MAX_LENGTH_TDS_URL) {
            return true;
        } else {
            Log.m285d(TAG, "validate: invalid lastUpdateTag :" + this.mLastUpdateTag);
            return false;
        }
    }
}
