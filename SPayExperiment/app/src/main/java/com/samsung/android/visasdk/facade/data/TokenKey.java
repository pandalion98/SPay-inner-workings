/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TokenKey
implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        public Object createFromParcel(Parcel parcel) {
            return new TokenKey(parcel);
        }

        public TokenKey[] newArray(int n2) {
            return new TokenKey[n2];
        }
    };
    private long tokenId;

    public TokenKey(long l2) {
        this.tokenId = l2;
    }

    public TokenKey(Parcel parcel) {
        this.tokenId = parcel.readLong();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        return object != null && object instanceof TokenKey && this.tokenId == ((TokenKey)object).tokenId;
    }

    public long getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(long l2) {
        this.tokenId = l2;
    }

    public String toString() {
        return Long.toString((long)this.tokenId);
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeLong(this.tokenId);
    }

}

