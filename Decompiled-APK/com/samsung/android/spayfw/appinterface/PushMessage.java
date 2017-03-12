package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class PushMessage implements Parcelable {
    public static final String CATEGORY_CARD = "CATEGORY_CARD";
    public static final String CATEGORY_TOKEN = "CATEGORY_TOKEN";
    public static final Creator<PushMessage> CREATOR;
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

    /* renamed from: com.samsung.android.spayfw.appinterface.PushMessage.1 */
    static class C03791 implements Creator<PushMessage> {
        C03791() {
        }

        public PushMessage createFromParcel(Parcel parcel) {
            return new PushMessage(null);
        }

        public PushMessage[] newArray(int i) {
            return new PushMessage[i];
        }
    }

    static {
        CREATOR = new C03791();
    }

    private PushMessage(Parcel parcel) {
        readFromParcel(parcel);
    }

    public PushMessage(String str) {
        this.message = str;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTokenEvent() {
        if (this.tokenEvent == null) {
            if (this.message == null) {
                return null;
            }
            try {
                this.tokenEvent = new JSONObject(this.message).getJSONObject(JSON_KEY_TOKEN).getString(JSON_KEY_TOKEN_EVENT);
            } catch (JSONException e) {
                Log.e("PushMessage", "Exception while fetching tokenEvent");
            }
        }
        return this.tokenEvent;
    }

    public String getTokenId() {
        if (this.tokenId == null) {
            if (this.message == null) {
                return null;
            }
            try {
                this.tokenId = new JSONObject(this.message).getJSONObject(JSON_KEY_TOKEN).getString(JSON_KEY_ID);
            } catch (JSONException e) {
                Log.e("PushMessage", "Exception while fetching tokenId");
            }
        }
        return this.tokenId;
    }

    public String getEnrollmentId() {
        if (this.enrollmentId == null) {
            if (this.message == null) {
                return null;
            }
            try {
                this.enrollmentId = new JSONObject(this.message).getJSONObject(JSON_KEY_TOKEN).getJSONObject(JSON_KEY_ENROLLMENT).getString(JSON_KEY_ID);
            } catch (JSONException e) {
                Log.e("PushMessage", "Exception while fetching enrollmentId");
            }
        }
        return this.enrollmentId;
    }

    public String getNotificationId() {
        if (this.notificationId == null) {
            if (this.message == null) {
                return null;
            }
            try {
                this.notificationId = new JSONObject(this.message).getString(JSON_KEY_ID);
            } catch (JSONException e) {
                Log.e("PushMessage", "Exception while fetching notificationId");
            }
        }
        return this.notificationId;
    }

    public String getTransactionUrl() {
        if (this.transactionUrl == null) {
            if (this.message == null) {
                return null;
            }
            try {
                this.transactionUrl = new JSONObject(this.message).getJSONObject(JSON_KEY_TRANSACTION).getJSONObject(JSON_KEY_SERVICE).getString(JSON_KEY_URL);
            } catch (JSONException e) {
                Log.e("PushMessage", "Exception while fetching transactionUrl");
            }
        }
        return this.transactionUrl;
    }

    public boolean getOob() {
        if (this.message == null) {
            return false;
        }
        try {
            this.oob = new JSONObject(this.message).getJSONObject(JSON_KEY_TRANSACTION).getBoolean(JSON_KEY_OOB);
            Log.d("PushMessage", "Fetched Oob. Value = " + this.oob);
        } catch (JSONException e) {
            Log.e("PushMessage", "Exception while fetching Oob value");
        }
        return this.oob;
    }

    public String getTransactionCredentials() {
        if (this.transactionCredentials == null) {
            if (this.message == null) {
                return null;
            }
            try {
                this.transactionCredentials = new JSONObject(this.message).getJSONObject(JSON_KEY_TRANSACTION).getJSONObject(JSON_KEY_SERVICE).getString(JSON_KEY_CREDENTIALS);
            } catch (JSONException e) {
                Log.e("PushMessage", "Exception while fetching transactionCredentials");
            }
        }
        return this.transactionCredentials;
    }

    public String getCategory() {
        if (this.category == null) {
            if (this.message == null) {
                return null;
            }
            try {
                JSONObject jSONObject = new JSONObject(this.message);
                if (!jSONObject.isNull(JSON_KEY_TOKEN)) {
                    this.category = CATEGORY_TOKEN;
                } else if (!jSONObject.isNull(JSON_KEY_CARD)) {
                    this.category = CATEGORY_CARD;
                }
            } catch (JSONException e) {
                Log.e("PushMessage", "Exception while fetching category");
            }
        }
        return this.category;
    }

    public String getCardNumber() {
        if (this.cardNumber == null) {
            if (this.message == null) {
                return null;
            }
            try {
                this.cardNumber = new JSONObject(this.message).getJSONObject(JSON_KEY_CARD).getString(JSON_KEY_ID);
            } catch (JSONException e) {
                Log.e("PushMessage", "Exception while fetching cardNumber");
            }
        }
        return this.cardNumber;
    }

    public String getCardEvent() {
        if (this.cardEvent == null) {
            if (this.message == null) {
                return null;
            }
            try {
                this.cardEvent = new JSONObject(this.message).getJSONObject(JSON_KEY_CARD).getString(JSON_KEY_TOKEN_EVENT);
            } catch (JSONException e) {
                Log.e("PushMessage", "Exception while fetching cardEvent");
            }
        }
        return this.cardEvent;
    }

    public String getWalletId() {
        if (this.walletId == null) {
            if (this.message == null) {
                return null;
            }
            try {
                this.walletId = new JSONObject(this.message).getJSONObject(JSON_KEY_WALLET).getString(JSON_KEY_ID);
            } catch (JSONException e) {
                Log.e("PushMessage", "Exception while fetching walletId");
            }
        }
        return this.walletId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.message);
    }

    public void readFromParcel(Parcel parcel) {
        this.message = parcel.readString();
    }

    public String toString() {
        return "PushMessage: message: " + this.message + " tokenEvent: " + this.tokenEvent + " tokenId: " + this.tokenId + " enrollmentId: " + this.enrollmentId + " oob: " + this.oob;
    }
}
