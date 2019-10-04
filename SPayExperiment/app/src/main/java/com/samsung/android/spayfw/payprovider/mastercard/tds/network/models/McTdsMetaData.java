/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds.network.models;

import android.text.TextUtils;
import com.samsung.android.spayfw.b.c;

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

    public String getAuthCode() {
        return this.mAuthCode;
    }

    public long getCardMasterId() {
        return this.mCardMasterId;
    }

    public String getHash() {
        return this.mHash;
    }

    public String getLastUpdateTag() {
        return this.mLastUpdateTag;
    }

    public String getPaymentAppInstanceId() {
        return this.mPaymentAppInstanceId;
    }

    public String getTdsRegisterUrl() {
        return this.mTdsRegisterUrl;
    }

    public String getTdsUrl() {
        return this.mTdsUrl;
    }

    public void setAuthCode(String string) {
        this.mAuthCode = string;
    }

    public void setCardMasterId(long l2) {
        this.mCardMasterId = l2;
    }

    public void setHash(String string) {
        this.mHash = string;
    }

    public void setLastUpdateTag(String string) {
        this.mLastUpdateTag = string;
    }

    public void setPaymentAppInstanceId(String string) {
        this.mPaymentAppInstanceId = string;
    }

    public void setTdsRegisterUrl(String string) {
        this.mTdsRegisterUrl = string;
    }

    public void setTdsUrl(String string) {
        this.mTdsUrl = string;
    }

    public boolean validate() {
        if (!TextUtils.isEmpty((CharSequence)this.mTdsUrl) && this.mTdsUrl.length() > 128) {
            c.d(TAG, "validate: invalid tdsUrl :" + this.mTdsUrl);
            return false;
        }
        if (!TextUtils.isEmpty((CharSequence)this.mTdsRegisterUrl) && this.mTdsRegisterUrl.length() > 128) {
            c.d(TAG, "validate: invalid tdsRegistrationUrl :" + this.mTdsRegisterUrl);
            return false;
        }
        if (this.mCardMasterId == -1L) {
            c.d(TAG, "validate: invalid foreign key :" + this.mCardMasterId);
            return false;
        }
        if (!TextUtils.isEmpty((CharSequence)this.mAuthCode) && this.mAuthCode.length() > 64) {
            c.d(TAG, "validate: invalid authCode :" + this.mAuthCode);
            return false;
        }
        if (!TextUtils.isEmpty((CharSequence)this.mLastUpdateTag) && this.mLastUpdateTag.length() > 128) {
            c.d(TAG, "validate: invalid lastUpdateTag :" + this.mLastUpdateTag);
            return false;
        }
        return true;
    }
}

