package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class IdvMethod implements Parcelable {
    public static final Creator<IdvMethod> CREATOR;
    public static final String EXTRA_AMOUNT = "amount";
    public static final String EXTRA_CURRENCY_CODE = "currencyCode";
    public static final String IDV_TYPE_APP = "APP";
    public static final String IDV_TYPE_CALL = "CALL";
    public static final String IDV_TYPE_CALL_INCOMINGCALL = "CALL_INCOMINGCALL";
    public static final String IDV_TYPE_CALL_OUTGOINGCALL = "CALL_OUTGOINGCALL";
    public static final String IDV_TYPE_CODE = "CODE";
    public static final String IDV_TYPE_CODE_EMAIL = "CODE_EMAIL";
    public static final String IDV_TYPE_CODE_INCOMINGCALL = "CODE_INCOMINGCALL";
    public static final String IDV_TYPE_CODE_ONLINEBANKING = "CODE_ONLINEBANKING";
    public static final String IDV_TYPE_CODE_SMS = "CODE_SMS";
    public static final String IDV_TYPE_IVR = "IVR";
    public static final String IDV_TYPE_LINK = "LINK";
    private static final String SCHEME_AUTHME = "AUTH_ME";
    private static final String SCHEME_CALLME = "CALL_ME";
    private static final String SCHEME_SENDME = "SEND_ME";
    private static final String SCHEME_TEXTME = "TEXT_ME";
    private String data;
    private Bundle extraData;
    private String id;
    private String scheme;
    private String type;
    private String value;

    /* renamed from: com.samsung.android.spayfw.appinterface.IdvMethod.1 */
    static class C03631 implements Creator<IdvMethod> {
        C03631() {
        }

        public IdvMethod createFromParcel(Parcel parcel) {
            return new IdvMethod(parcel);
        }

        public IdvMethod[] newArray(int i) {
            return new IdvMethod[i];
        }
    }

    static {
        CREATOR = new C03631();
    }

    public IdvMethod(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.id = parcel.readString();
        this.type = parcel.readString();
        this.value = parcel.readString();
        this.scheme = parcel.readString();
        this.extraData = parcel.readBundle();
    }

    public int describeContents() {
        return 0;
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        String str = this.type;
        if (this.type == null) {
            return str;
        }
        if (this.scheme == null || !IDV_TYPE_CODE.equals(this.type)) {
            if (!IDV_TYPE_CALL.equals(this.type)) {
                return str;
            }
            if (this.scheme == null || !SCHEME_CALLME.equals(this.scheme)) {
                return IDV_TYPE_CALL_OUTGOINGCALL;
            }
            return IDV_TYPE_CALL_INCOMINGCALL;
        } else if (SCHEME_CALLME.equals(this.scheme)) {
            return IDV_TYPE_CODE_INCOMINGCALL;
        } else {
            if (SCHEME_TEXTME.equals(this.scheme)) {
                return IDV_TYPE_CODE_SMS;
            }
            if (SCHEME_SENDME.equals(this.scheme)) {
                return IDV_TYPE_CODE_EMAIL;
            }
            if (SCHEME_AUTHME.equals(this.scheme)) {
                return IDV_TYPE_CODE_ONLINEBANKING;
            }
            return str;
        }
    }

    public String getValue() {
        return this.value;
    }

    public String getData() {
        return this.data;
    }

    public String getScheme() {
        return this.scheme;
    }

    public Bundle getExtra() {
        return this.extraData;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setType(String str) {
        this.type = str;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public void setScheme(String str) {
        this.scheme = str;
    }

    public void setData(String str) {
        this.data = str;
    }

    public void setExtra(Bundle bundle) {
        this.extraData = bundle;
    }

    public String toString() {
        return "IdvMethod: id: " + this.id + " type: " + this.type + " value: " + this.value + "scheme: " + this.scheme + "data: " + this.data + "extraData: " + this.extraData;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.type);
        parcel.writeString(this.value);
        parcel.writeString(this.scheme);
        parcel.writeBundle(this.extraData);
    }
}
