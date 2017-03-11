package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TokenStatus implements Parcelable {
    public static final String ACTIVE = "ACTIVE";
    public static final Creator<TokenStatus> CREATOR;
    public static final String DISPOSED = "DISPOSED";
    public static final String EXPIRED = "EXPIRED";
    public static final String PENDING = "PENDING";
    public static final String PENDING_ENROLLED = "ENROLLED";
    public static final String PENDING_PROVISION = "PENDING_PROVISION";
    public static final String SUSPENDED = "SUSPENDED";
    private String code;
    private String reason;

    /* renamed from: com.samsung.android.spayfw.appinterface.TokenStatus.1 */
    static class C03941 implements Creator<TokenStatus> {
        C03941() {
        }

        public TokenStatus createFromParcel(Parcel parcel) {
            return new TokenStatus(null);
        }

        public TokenStatus[] newArray(int i) {
            return new TokenStatus[i];
        }
    }

    static {
        CREATOR = new C03941();
    }

    public TokenStatus(String str, String str2) {
        this.code = str;
        this.reason = str2;
    }

    private TokenStatus(Parcel parcel) {
        readFromParcel(parcel);
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

    public void setCode(String str) {
        this.code = str;
    }

    public void setReason(String str) {
        this.reason = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.code);
        parcel.writeString(this.reason);
    }

    public String toString() {
        return "TokenStatus: code: " + this.code + " reason: " + this.reason;
    }
}
