package com.samsung.android.visasdk.facade.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TokenKey implements Parcelable {
    public static final Creator CREATOR;
    private long tokenId;

    /* renamed from: com.samsung.android.visasdk.facade.data.TokenKey.1 */
    static class C06011 implements Creator {
        C06011() {
        }

        public Object createFromParcel(Parcel parcel) {
            return new TokenKey(parcel);
        }

        public TokenKey[] newArray(int i) {
            return new TokenKey[i];
        }
    }

    public TokenKey(long j) {
        this.tokenId = j;
    }

    public TokenKey(Parcel parcel) {
        this.tokenId = parcel.readLong();
    }

    public long getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(long j) {
        this.tokenId = j;
    }

    public String toString() {
        return Long.toString(this.tokenId);
    }

    public int describeContents() {
        return 0;
    }

    static {
        CREATOR = new C06011();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.tokenId);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof TokenKey) && this.tokenId == ((TokenKey) obj).tokenId) {
            return true;
        }
        return false;
    }
}
