/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class IdvMethod
implements Parcelable {
    public static final Parcelable.Creator<IdvMethod> CREATOR = new Parcelable.Creator<IdvMethod>(){

        public IdvMethod createFromParcel(Parcel parcel) {
            return new IdvMethod(parcel);
        }

        public IdvMethod[] newArray(int n2) {
            return new IdvMethod[n2];
        }
    };
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

    public IdvMethod() {
    }

    public IdvMethod(Parcel parcel) {
        this.readFromParcel(parcel);
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

    public String getData() {
        return this.data;
    }

    public Bundle getExtra() {
        return this.extraData;
    }

    public String getId() {
        return this.id;
    }

    public String getScheme() {
        return this.scheme;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getType() {
        String string = this.type;
        if (this.type == null) return string;
        if (this.scheme != null && IDV_TYPE_CODE.equals((Object)this.type)) {
            if (SCHEME_CALLME.equals((Object)this.scheme)) {
                return IDV_TYPE_CODE_INCOMINGCALL;
            }
            if (SCHEME_TEXTME.equals((Object)this.scheme)) {
                return IDV_TYPE_CODE_SMS;
            }
            if (SCHEME_SENDME.equals((Object)this.scheme)) {
                return IDV_TYPE_CODE_EMAIL;
            }
            if (!SCHEME_AUTHME.equals((Object)this.scheme)) return string;
            return IDV_TYPE_CODE_ONLINEBANKING;
        }
        if (!IDV_TYPE_CALL.equals((Object)this.type)) return string;
        if (this.scheme == null) return IDV_TYPE_CALL_OUTGOINGCALL;
        if (!SCHEME_CALLME.equals((Object)this.scheme)) return IDV_TYPE_CALL_OUTGOINGCALL;
        return IDV_TYPE_CALL_INCOMINGCALL;
    }

    public String getValue() {
        return this.value;
    }

    public void setData(String string) {
        this.data = string;
    }

    public void setExtra(Bundle bundle) {
        this.extraData = bundle;
    }

    public void setId(String string) {
        this.id = string;
    }

    public void setScheme(String string) {
        this.scheme = string;
    }

    public void setType(String string) {
        this.type = string;
    }

    public void setValue(String string) {
        this.value = string;
    }

    public String toString() {
        return "IdvMethod: id: " + this.id + " type: " + this.type + " value: " + this.value + "scheme: " + this.scheme + "data: " + this.data + "extraData: " + (Object)this.extraData;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.id);
        parcel.writeString(this.type);
        parcel.writeString(this.value);
        parcel.writeString(this.scheme);
        parcel.writeBundle(this.extraData);
    }

}

