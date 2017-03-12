package com.samsung.android.spayauth.sdk;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AuthResponse implements Parcelable {
    public static final Creator<AuthResponse> CREATOR;
    private byte[] data;
    private Bundle extraData;
    private int status;

    /* renamed from: com.samsung.android.spayauth.sdk.AuthResponse.1 */
    static class C03281 implements Creator<AuthResponse> {
        C03281() {
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return m259c(parcel);
        }

        public /* synthetic */ Object[] newArray(int i) {
            return m260h(i);
        }

        public AuthResponse m259c(Parcel parcel) {
            return new AuthResponse(null);
        }

        public AuthResponse[] m260h(int i) {
            return new AuthResponse[i];
        }
    }

    static {
        CREATOR = new C03281();
    }

    private AuthResponse(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        this.status = parcel.readInt();
        this.data = parcel.createByteArray();
        this.extraData = parcel.readBundle();
    }

    public String toString() {
        String str = "TuiVerifyPinResponse: status: " + this.status;
        if (this.data == null || this.data.length <= 0) {
            str = str + " data: " + "null";
        } else {
            str = str + " data: " + new String(this.data);
        }
        return str + " " + String.valueOf(this.extraData);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.status);
        parcel.writeByteArray(this.data);
        parcel.writeBundle(this.extraData);
    }
}
