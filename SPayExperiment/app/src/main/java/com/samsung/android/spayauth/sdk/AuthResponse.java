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
package com.samsung.android.spayauth.sdk;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class AuthResponse
implements Parcelable {
    public static final Parcelable.Creator<AuthResponse> CREATOR = new Parcelable.Creator<AuthResponse>(){

        public AuthResponse c(Parcel parcel) {
            return new AuthResponse(parcel);
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return this.c(parcel);
        }

        public AuthResponse[] h(int n2) {
            return new AuthResponse[n2];
        }

        public /* synthetic */ Object[] newArray(int n2) {
            return this.h(n2);
        }
    };
    private byte[] data;
    private Bundle extraData;
    private int status;

    public AuthResponse() {
    }

    private AuthResponse(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        this.status = parcel.readInt();
        this.data = parcel.createByteArray();
        this.extraData = parcel.readBundle();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String toString() {
        String string;
        String string2 = "TuiVerifyPinResponse: status: " + this.status;
        if (this.data != null && this.data.length > 0) {
            string = string2 + " data: " + new String(this.data);
            do {
                return string + " " + String.valueOf((Object)this.extraData);
                break;
            } while (true);
        }
        string = string2 + " data: " + "null";
        return string + " " + String.valueOf((Object)this.extraData);
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeInt(this.status);
        parcel.writeByteArray(this.data);
        parcel.writeBundle(this.extraData);
    }

}

