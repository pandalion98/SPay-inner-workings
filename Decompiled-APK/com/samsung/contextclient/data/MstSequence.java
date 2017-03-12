package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MstSequence implements Parcelable {
    public static final Creator<MstSequence> CREATOR;
    private String config;
    private long idle;
    private String key;
    private long transmit;

    /* renamed from: com.samsung.contextclient.data.MstSequence.1 */
    static class C06041 implements Creator<MstSequence> {
        C06041() {
        }

        public MstSequence createFromParcel(Parcel parcel) {
            return new MstSequence(parcel);
        }

        public MstSequence[] newArray(int i) {
            return new MstSequence[i];
        }
    }

    public void setKey(String str) {
        this.key = str;
    }

    public String getKey() {
        return this.key;
    }

    public void setTransmit(long j) {
        this.transmit = j;
    }

    public long getTransmit() {
        return this.transmit;
    }

    public void setIdle(long j) {
        this.idle = j;
    }

    public long getIdle() {
        return this.idle;
    }

    public void setConfig(String str) {
        this.config = str;
    }

    public String getConfig() {
        return this.config;
    }

    protected MstSequence(Parcel parcel) {
        this.key = parcel.readString();
        this.transmit = parcel.readLong();
        this.idle = parcel.readLong();
        this.config = parcel.readString();
    }

    static {
        CREATOR = new C06041();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.key);
        parcel.writeLong(this.transmit);
        parcel.writeLong(this.idle);
        parcel.writeString(this.config);
    }
}
