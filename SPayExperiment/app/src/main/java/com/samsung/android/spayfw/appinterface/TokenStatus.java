/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;

public class TokenStatus
implements Parcelable {
    public static final String ACTIVE = "ACTIVE";
    public static final Parcelable.Creator<TokenStatus> CREATOR = new Parcelable.Creator<TokenStatus>(){

        public TokenStatus createFromParcel(Parcel parcel) {
            return new TokenStatus(parcel);
        }

        public TokenStatus[] newArray(int n2) {
            return new TokenStatus[n2];
        }
    };
    public static final String DISPOSED = "DISPOSED";
    public static final String EXPIRED = "EXPIRED";
    public static final String PENDING = "PENDING";
    public static final String PENDING_ENROLLED = "ENROLLED";
    public static final String PENDING_PROVISION = "PENDING_PROVISION";
    public static final String SUSPENDED = "SUSPENDED";
    private String code;
    private String reason;

    private TokenStatus(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public TokenStatus(String string, String string2) {
        this.code = string;
        this.reason = string2;
    }

    private void readFromParcel(Parcel parcel) {
        this.code = parcel.readString();
        this.reason = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public String getCode() {
        return this.code;
    }

    public String getReason() {
        return this.reason;
    }

    public void setCode(String string) {
        this.code = string;
    }

    public void setReason(String string) {
        this.reason = string;
    }

    public String toString() {
        return "TokenStatus: code: " + this.code + " reason: " + this.reason;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.code);
        parcel.writeString(this.reason);
    }

}

