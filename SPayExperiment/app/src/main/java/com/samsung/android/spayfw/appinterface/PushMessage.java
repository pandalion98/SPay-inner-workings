/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class PushMessage
implements Parcelable {
    public static final String CATEGORY_CARD = "CATEGORY_CARD";
    public static final String CATEGORY_TOKEN = "CATEGORY_TOKEN";
    public static final Parcelable.Creator<PushMessage> CREATOR = new Parcelable.Creator<PushMessage>(){

        public PushMessage createFromParcel(Parcel parcel) {
            return new PushMessage(parcel);
        }

        public PushMessage[] newArray(int n2) {
            return new PushMessage[n2];
        }
    };
    public static final String JSON_KEY_CARD = "card";
    public static final String JSON_KEY_CREDENTIALS = "credentials";
    public static final String JSON_KEY_ENROLLMENT = "enrollment";
    public static final String JSON_KEY_ID = "id";
    public static final String JSON_KEY_OOB = "oob";
    public static final String JSON_KEY_SERVICE = "service";
    public static final String JSON_KEY_TOKEN = "token";
    public static final String JSON_KEY_TOKEN_EVENT = "event";
    public static final String JSON_KEY_TRANSACTION = "transaction";
    public static final String JSON_KEY_URL = "url";
    public static final String JSON_KEY_WALLET = "wallet";
    public static final String TYPE_ASSET_CHANGE = "ASSET_CHANGE";
    public static final String TYPE_ENROLL_CC = "ENROLL_CC";
    public static final String TYPE_PROVISION = "PROVISION";
    public static final String TYPE_REPLENISH = "REPLENISH";
    public static final String TYPE_STATUS_CHANGE = "STATUS_CHANGE";
    public static final String TYPE_TOKEN_CHANGE = "TOKEN_CHANGE";
    public static final String TYPE_TRANSACTION = "TRANSACTION";
    public static final String TYPE_UPDATE_CC = "UPDATE_CC";
    private String cardEvent;
    private String cardNumber;
    private String category;
    private String enrollmentId;
    private String message;
    private String notificationId;
    private boolean oob;
    private String tokenEvent;
    private String tokenId;
    private String transactionCredentials;
    private String transactionUrl;
    private String walletId;

    private PushMessage(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public PushMessage(String string) {
        this.message = string;
    }

    public int describeContents() {
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getCardEvent() {
        if (this.cardEvent != null) return this.cardEvent;
        if (this.message == null) {
            return null;
        }
        try {
            this.cardEvent = new JSONObject(this.message).getJSONObject(JSON_KEY_CARD).getString(JSON_KEY_TOKEN_EVENT);
            return this.cardEvent;
        }
        catch (JSONException jSONException) {
            Log.e((String)"PushMessage", (String)"Exception while fetching cardEvent");
            return this.cardEvent;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getCardNumber() {
        if (this.cardNumber != null) return this.cardNumber;
        if (this.message == null) {
            return null;
        }
        try {
            this.cardNumber = new JSONObject(this.message).getJSONObject(JSON_KEY_CARD).getString(JSON_KEY_ID);
            return this.cardNumber;
        }
        catch (JSONException jSONException) {
            Log.e((String)"PushMessage", (String)"Exception while fetching cardNumber");
            return this.cardNumber;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getCategory() {
        if (this.category != null) return this.category;
        if (this.message == null) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(this.message);
            if (!jSONObject.isNull(JSON_KEY_TOKEN)) {
                this.category = CATEGORY_TOKEN;
                return this.category;
            }
            if (jSONObject.isNull(JSON_KEY_CARD)) return this.category;
            this.category = CATEGORY_CARD;
            return this.category;
        }
        catch (JSONException jSONException) {
            Log.e((String)"PushMessage", (String)"Exception while fetching category");
            return this.category;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getEnrollmentId() {
        if (this.enrollmentId != null) return this.enrollmentId;
        if (this.message == null) {
            return null;
        }
        try {
            this.enrollmentId = new JSONObject(this.message).getJSONObject(JSON_KEY_TOKEN).getJSONObject(JSON_KEY_ENROLLMENT).getString(JSON_KEY_ID);
            return this.enrollmentId;
        }
        catch (JSONException jSONException) {
            Log.e((String)"PushMessage", (String)"Exception while fetching enrollmentId");
            return this.enrollmentId;
        }
    }

    public String getMessage() {
        return this.message;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getNotificationId() {
        if (this.notificationId != null) return this.notificationId;
        if (this.message == null) {
            return null;
        }
        try {
            this.notificationId = new JSONObject(this.message).getString(JSON_KEY_ID);
            return this.notificationId;
        }
        catch (JSONException jSONException) {
            Log.e((String)"PushMessage", (String)"Exception while fetching notificationId");
            return this.notificationId;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean getOob() {
        if (this.message == null) {
            return false;
        }
        try {
            this.oob = new JSONObject(this.message).getJSONObject(JSON_KEY_TRANSACTION).getBoolean(JSON_KEY_OOB);
            Log.d((String)"PushMessage", (String)("Fetched Oob. Value = " + this.oob));
            do {
                return this.oob;
                break;
            } while (true);
        }
        catch (JSONException jSONException) {
            Log.e((String)"PushMessage", (String)"Exception while fetching Oob value");
            return this.oob;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getTokenEvent() {
        if (this.tokenEvent != null) return this.tokenEvent;
        if (this.message == null) {
            return null;
        }
        try {
            this.tokenEvent = new JSONObject(this.message).getJSONObject(JSON_KEY_TOKEN).getString(JSON_KEY_TOKEN_EVENT);
            return this.tokenEvent;
        }
        catch (JSONException jSONException) {
            Log.e((String)"PushMessage", (String)"Exception while fetching tokenEvent");
            return this.tokenEvent;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getTokenId() {
        if (this.tokenId != null) return this.tokenId;
        if (this.message == null) {
            return null;
        }
        try {
            this.tokenId = new JSONObject(this.message).getJSONObject(JSON_KEY_TOKEN).getString(JSON_KEY_ID);
            return this.tokenId;
        }
        catch (JSONException jSONException) {
            Log.e((String)"PushMessage", (String)"Exception while fetching tokenId");
            return this.tokenId;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getTransactionCredentials() {
        if (this.transactionCredentials != null) return this.transactionCredentials;
        if (this.message == null) {
            return null;
        }
        try {
            this.transactionCredentials = new JSONObject(this.message).getJSONObject(JSON_KEY_TRANSACTION).getJSONObject(JSON_KEY_SERVICE).getString(JSON_KEY_CREDENTIALS);
            return this.transactionCredentials;
        }
        catch (JSONException jSONException) {
            Log.e((String)"PushMessage", (String)"Exception while fetching transactionCredentials");
            return this.transactionCredentials;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getTransactionUrl() {
        if (this.transactionUrl != null) return this.transactionUrl;
        if (this.message == null) {
            return null;
        }
        try {
            this.transactionUrl = new JSONObject(this.message).getJSONObject(JSON_KEY_TRANSACTION).getJSONObject(JSON_KEY_SERVICE).getString(JSON_KEY_URL);
            return this.transactionUrl;
        }
        catch (JSONException jSONException) {
            Log.e((String)"PushMessage", (String)"Exception while fetching transactionUrl");
            return this.transactionUrl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getWalletId() {
        if (this.walletId != null) return this.walletId;
        if (this.message == null) {
            return null;
        }
        try {
            this.walletId = new JSONObject(this.message).getJSONObject(JSON_KEY_WALLET).getString(JSON_KEY_ID);
            return this.walletId;
        }
        catch (JSONException jSONException) {
            Log.e((String)"PushMessage", (String)"Exception while fetching walletId");
            return this.walletId;
        }
    }

    public void readFromParcel(Parcel parcel) {
        this.message = parcel.readString();
    }

    public String toString() {
        return "PushMessage: message: " + this.message + " tokenEvent: " + this.tokenEvent + " tokenId: " + this.tokenId + " enrollmentId: " + this.enrollmentId + " oob: " + this.oob;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.message);
    }

}

